package org.wildhamsters.gameroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameRoomController {

    @GetMapping
    String firstEndpoint() {
        return "index.html";    
    }
}
