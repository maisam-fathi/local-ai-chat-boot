package de.solidassist.gpt4all;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gpt4all")
public class Gpt4AllController {

    private final ChatBotService chatBotService;

    public Gpt4AllController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    public String sendMessage(@RequestBody String message) {
        return chatBotService.getResponse(message);
    }
}
