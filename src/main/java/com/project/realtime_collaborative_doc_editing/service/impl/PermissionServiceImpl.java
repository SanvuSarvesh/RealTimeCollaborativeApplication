package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import com.project.realtime_collaborative_doc_editing.repository.DocumentRepository;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.JwtService;
import com.project.realtime_collaborative_doc_editing.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final HttpServletRequest httpServletRequest;

    private final JwtService jwtService;

    @Override
    public BaseResponse allowUserToView(String documentId, String username) {
        BaseResponse baseResponse = new BaseResponse();

        Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
        if (documentDetailsOpt.isEmpty()) {
            throw new RuntimeException("Required document not found.");
        }
        DocumentDetails documentDetails = documentDetailsOpt.get();

        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);
        String currentLoggedInUser = jwtService.extractUsername(tokenWithOutBearer);
        String documentOwner = documentDetails.getDocumentCreatedBy();
        if (Objects.equals(currentLoggedInUser, documentOwner)) {
            Set<String> allowedUsersToView = documentDetails.getUsersCanView();
            allowedUsersToView.add(username);
            documentDetails.setUsersCanView(allowedUsersToView);
            documentRepository.save(documentDetails);

            baseResponse.setMessage("View Access Given Successfully");
            baseResponse.setStatusCode(HttpStatus.OK.toString());
            baseResponse.setSuccess(true);
            return baseResponse;
        }
        baseResponse.setMessage("You are not allowed to grant this permission");
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
        baseResponse.setSuccess(false);
        return baseResponse;
    }

    @Override
    public BaseResponse allowUserToEdit(String documentId, String username, DocumentReqDto documentReqDto) {
        BaseResponse baseResponse = new BaseResponse();

        Optional<DocumentDetails> documentDetailsOpt = documentRepository.findById(documentId);
        if (documentDetailsOpt.isEmpty()) {
            throw new RuntimeException("Required document not found.");
        }
        DocumentDetails documentDetails = documentDetailsOpt.get();

        String accessToken = httpServletRequest.getHeader("Authorization");
        String tokenWithOutBearer = accessToken.substring(7);
        String currentLoggedInUser = jwtService.extractUsername(tokenWithOutBearer);
        String documentOwner = documentDetails.getDocumentCreatedBy();
        if (Objects.equals(currentLoggedInUser, documentOwner)) {
            Set<String> allowedUsersToView = documentDetails.getUsersCanView();
            Set<String> allowedUsersToEdit = documentDetails.getUsersCanEdit();
            allowedUsersToView.add(username);
            allowedUsersToEdit.add(username);
            documentDetails.setUsersCanEdit(allowedUsersToEdit);;
            documentDetails.setUsersCanView(allowedUsersToView);
            documentDetails.setDocumentTitle(documentReqDto.getDocumentTitle());
            documentDetails.setDocumentDescription(documentReqDto.getDocumentDescription());
            documentRepository.save(documentDetails);

            baseResponse.setMessage("Edit Access Given Successfully");
            baseResponse.setStatusCode(HttpStatus.OK.toString());
            baseResponse.setSuccess(true);
            return baseResponse;
        }
        baseResponse.setMessage("You are not allowed to grant this permission");
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
        baseResponse.setSuccess(false);
        return baseResponse;
    }

    @Override
    public BaseResponse withDrawViewPermission() {
        return null;
    }

    @Override
    public BaseResponse withEditViewPermission() {
        return null;
    }

    @Override
    public BaseResponse requestForViewAccess() {
        return null;
    }

    @Override
    public BaseResponse requestForEditAccess() {
        return null;
    }
}
