package com.email.service.impl;

import com.email.model.EmailRequest;
import com.email.service.EmailGeneratorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorServiceImpl implements EmailGeneratorService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorServiceImpl(WebClient.Builder webClient) {
        this.webClient = WebClient.builder().build();
    }

    @Override
    public String generateEmailReply(EmailRequest emailRequest) {
        //Build the prompt
        String prompt = buildPrompt(emailRequest);

        //Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        //Do request and get the response
        String response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

//      extract response  return
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        }catch (Exception e){
            return "Error processing request: "+e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional").append(emailRequest.getPlatform()).append("reply for the following content. Please do not generate subject header.");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("\n Please use a ").append(emailRequest.getTone()).append(" tone");
        }
        prompt.append("\n Original Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
