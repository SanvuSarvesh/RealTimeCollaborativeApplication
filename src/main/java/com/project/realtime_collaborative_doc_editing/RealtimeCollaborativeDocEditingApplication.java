package com.project.realtime_collaborative_doc_editing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RealtimeCollaborativeDocEditingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtimeCollaborativeDocEditingApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

}

// adding test comments