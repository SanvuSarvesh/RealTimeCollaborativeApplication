package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.exceptions.DocumentNotFoundException;
import com.project.realtime_collaborative_doc_editing.exceptions.PermissionNotGrantedException;
import com.project.realtime_collaborative_doc_editing.exceptions.UserNotFoundException;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import com.project.realtime_collaborative_doc_editing.model.HistoryDetails;
import com.project.realtime_collaborative_doc_editing.model.UserProfile;
import com.project.realtime_collaborative_doc_editing.repository.DocumentRepository;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.DocumentService;
import com.project.realtime_collaborative_doc_editing.service.JwtService;
import jakarta.mail.Header;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final HttpServletRequest httpServletRequest;

    private final DocumentRepository documentRepository;

    private final RedisService redisService;

    @Override
    public BaseResponse createNewDocument(DocumentReqDto documentReqDto) {
        BaseResponse baseResponse = new BaseResponse();

        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);

        String userName = jwtService.extractUsername(tokenWithOutBearer);
        Optional<UserProfile> userOpt = userRepository.findByEmail(userName);
        if(Objects.isNull(userOpt)){
            throw new UserNotFoundException("User Not found.",HttpStatus.NOT_FOUND);
        }
        UserProfile user = userOpt.get();
        Set<String> userCanEdit = new HashSet<>();
        Set<String> userCanView = new HashSet<>();
        boolean isTokenValid = jwtService.isTokenValid(tokenWithOutBearer,user);
        if(isTokenValid){
            DocumentDetails documentDetails = documentDtoToDocument(documentReqDto,userName);
            userCanEdit.add(userName);
            userCanView.add(userName);
            documentDetails.setUsersCanEdit(userCanEdit);
            documentDetails.setUsersCanView(userCanView);

            HistoryDetails historyDetails = new HistoryDetails();
            historyDetails.setOperationType("CREATED");
            historyDetails.setUpdatedAt(new Date());
            historyDetails.setUpdatedBy(userName);

            List<HistoryDetails> history = new ArrayList<>();
            history.add(historyDetails);
            documentDetails.setHistoryDetails(history);

            documentRepository.save(documentDetails);
            baseResponse.setPayload(documentDetails);
            baseResponse.setSuccess(true);
            baseResponse.setMessage("New Document Created.");
            baseResponse.setStatusCode(HttpStatus.CREATED.toString());
            return baseResponse;
        }

        baseResponse.setSuccess(false);
        baseResponse.setMessage("Document Creation failed, It seems like you have permission for this operation");
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
        return baseResponse;
    }

    @Override
    public BaseResponse getDocumentById(String documentId) {
        BaseResponse baseResponse = new BaseResponse();
        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);
        String userName = jwtService.extractUsername(tokenWithOutBearer);
        Optional<UserProfile> userOpt = userRepository.findByEmail(userName);
        if(userOpt.isEmpty()){
            throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
        }
        UserProfile user = userOpt.get();
        String emailId = user.getEmail();
        DocumentDetails details = redisService.getFromRedis(documentId, DocumentDetails.class);
        System.out.println("Details from redis : "+details);
        if(!Objects.isNull(details)){
            System.out.println("----------> Response from Redis Cache.");
            baseResponse.setMessage("Document has been fetch.");
            baseResponse.setSuccess(true);
            baseResponse.setPayload(details);
            baseResponse.setStatusCode(HttpStatus.OK.toString());
        }else{
            System.out.println("----------> API is making a Db call.");
            Optional<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentId(documentId);
            if(documentDetailsOpt.isEmpty()){
                throw new DocumentNotFoundException("Document not found with given document Id..",HttpStatus.NOT_FOUND);
            }
            if(!Objects.isNull(documentDetailsOpt)){
                redisService.setIntoRedis(documentId, documentDetailsOpt.get(),(long)30*60*60);
                System.out.println("Response has been set into Redis ");
            }
            DocumentDetails documentDetails = documentDetailsOpt.get();
            if(!documentDetails.getUsersCanView().contains(emailId)){
                throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
            }

            baseResponse.setMessage("Document has been fetch.");
            baseResponse.setSuccess(true);
            baseResponse.setPayload(documentDetails);
            baseResponse.setStatusCode(HttpStatus.OK.toString());

            return baseResponse;
        }
        throw new RuntimeException("Something went wrong, it was not a reachable statement.");
    }

    @Override
    public BaseResponse getDocumentByDocumentTitle(String documentName) {
        BaseResponse baseResponse = new BaseResponse();
        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);

        String userName = jwtService.extractUsername(tokenWithOutBearer);
        Optional<UserProfile> userOpt = userRepository.findByEmail(userName);
        if(userOpt.isEmpty()){
            throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
        }
        UserProfile user = userOpt.get();
        String emailId = user.getEmail();
        Optional<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentTitle(documentName);
        if(documentDetailsOpt.isEmpty()){
            throw new RuntimeException("Document not found.");
        }
        DocumentDetails documentDetails = documentDetailsOpt.get();
        if(!documentDetails.getUsersCanView().contains(emailId)){
            throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
        }
        baseResponse.setMessage("Document has been fetch.");
        baseResponse.setSuccess(true);
        baseResponse.setPayload(documentDetails);
        baseResponse.setStatusCode(HttpStatus.OK.toString());

        return baseResponse;
    }

    @Override
    public BaseResponse editDocument(String documentId, DocumentReqDto documentReqDto) {
        BaseResponse baseResponse  = new BaseResponse();
        Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
        if(documentDetailsOpt.isEmpty()){
            throw new DocumentNotFoundException("Document not found by the given Document ID",HttpStatus.NOT_FOUND);
        }
        DocumentDetails documentDetails = documentDetailsOpt.get();
        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithoutBearer = accessToken.substring(7);
        String username = jwtService.extractUsername(tokenWithoutBearer);
        if(documentDetails.getUsersCanEdit().contains(username)){
            if(!Objects.isNull(documentReqDto.getDocumentTitle())){
                documentDetails.setDocumentTitle(documentReqDto.getDocumentTitle());
            }
            documentDetails.setLastEditedAt(new Date());
            documentDetails.setLastEditedBy(username);
            documentDetails.setDocumentDescription(documentReqDto.getDocumentDescription());
            documentRepository.save(documentDetails);
            baseResponse.setPayload(documentDetails);
            baseResponse.setMessage("Document edited Successfully.");
            baseResponse.setSuccess(true);
            baseResponse.setStatusCode(HttpStatus.OK.toString());
            return baseResponse;
        }
        baseResponse.setPayload(documentDetails);
        baseResponse.setMessage("You don't have access to edit this Document.");
        baseResponse.setSuccess(false);
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
        return  baseResponse;
    }

    @Override
    public BaseResponse deleteDocument(String documentId) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentId(documentId);
        if(documentDetailsOpt.isEmpty()){
            throw new DocumentNotFoundException("Document not found by the given Document ID",HttpStatus.NOT_FOUND);
        }
        DocumentDetails documentDetails = documentDetailsOpt.get();
        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithoutBearer = accessToken.substring(7);
        String username = jwtService.extractUsername(tokenWithoutBearer);
        System.out.println("-----------> username : "+username);
        System.out.println("-----------> createdBy : "+documentDetails.getDocumentCreatedBy());
        if(Objects.equals(username,documentDetails.getDocumentCreatedBy())){
            documentRepository.delete(documentDetails);
            baseResponse.setMessage("Document deleted Successfully.");
            baseResponse.setSuccess(true);
            baseResponse.setStatusCode(HttpStatus.OK.toString());
        }
        baseResponse.setMessage("You don't have permission to delete this Document.");
        baseResponse.setSuccess(false);
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
        return baseResponse;
    }

    @Override
    public BaseResponse searchDocumentsByKeyword(String keyword) {
        BaseResponse baseResponse = new BaseResponse();
        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);

        String userName = jwtService.extractUsername(tokenWithOutBearer);
        Optional<UserProfile> userOpt = userRepository.findByEmail(userName);
        if(userOpt.isEmpty()){
            throw new UserNotFoundException("User not found with username : "+userName,HttpStatus.NOT_FOUND);
        }
        UserProfile user = userOpt.get();
        String emailId = user.getEmail();
        List<DocumentDetails> documentDetailsOpt = documentRepository.findByDocumentTitleContainingIgnoreCase(keyword);
        if(documentDetailsOpt.isEmpty()){
            throw new RuntimeException("Document not found.");
        }
//        DocumentDetails documentDetails = documentDetailsOpt;
//        if(!documentDetails.getUsersCanView().contains(emailId)){
//            throw new PermissionNotGrantedException("You do not have the permission to view this document.",HttpStatus.FORBIDDEN);
//        }
        baseResponse.setMessage("Document has been fetch.");
        baseResponse.setSuccess(true);
        baseResponse.setPayload(documentDetailsOpt);
        baseResponse.setStatusCode(HttpStatus.OK.toString());

        return baseResponse;
    }

    private DocumentDetails documentDtoToDocument(DocumentReqDto documentReqDto, String username){

        DocumentDetails documentDetails = new DocumentDetails();
        documentDetails.setDocumentCreatedAt(new Date());
        documentDetails.setDocumentDescription(documentReqDto.getDocumentDescription());
        documentDetails.setDocumentTitle(documentReqDto.getDocumentTitle());
        documentDetails.setDocumentCreatedBy(username);
        return documentDetails;

    }
}
