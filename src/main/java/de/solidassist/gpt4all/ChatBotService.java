package de.solidassist.gpt4all;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatBotService {

    private final RestTemplate restTemplate;

    public ChatBotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponse(String message) {
        String gpt4allUrl = "http://localhost:5000/v1/chat/completions";

        String requestJson = """
        {
          "model": "Llama 3.2 1B Instruct",
          "max_tokens": 300,
          "messages": [
            {
              "role": "user",
              "content": "%s"
            }
          ]
        }
        """.formatted(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(gpt4allUrl, entity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}
