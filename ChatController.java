import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args); // Inicializa a aplicação Spring
    }
}

@RestController
@RequestMapping("/chat")
class ChatController {
    private final Settings settings = new Settings("RV2V2", "Você disse: {message}");

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@Valid @RequestBody ChatMessage message) {
        String responseMessage = settings.getResponseTemplate().replace("{message}", message.getMessage());
        ChatResponse response = new ChatResponse(responseMessage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/settings")
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/settings")
    public ResponseEntity<Settings> updateSettings(@Valid @RequestBody Settings newSettings) {
        settings.setBotName(newSettings.getBotName());
        settings.setResponseTemplate(newSettings.getResponseTemplate());
        return ResponseEntity.ok(settings);
    }
}

class ChatMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class ChatResponse {
    private String response;

    public ChatResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}

class Settings {
    private String botName;
    private String responseTemplate;

    public Settings(String botName, String responseTemplate) {
        this.botName = botName;
        this.responseTemplate = responseTemplate;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(String responseTemplate) {
        this.responseTemplate = responseTemplate;
    }
}
