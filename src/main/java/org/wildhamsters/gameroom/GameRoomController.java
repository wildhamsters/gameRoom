package org.wildhamsters.gameroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameRoomController {

    @GetMapping
    String firstEndpoint() {
        return "index.html";
    }

    @GetMapping("/gameroom")
    String placeShips(@RequestParam(name = "userName", required = false, defaultValue = "defaultUser") String userName,
            @RequestParam(name = "sessionId", required = false, defaultValue = "defaultSession") String sessionId) {
        System.out.println(userName + " " + sessionId);
        return "game.html";
    }
}
