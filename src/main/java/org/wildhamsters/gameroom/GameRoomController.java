package org.wildhamsters.gameroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameRoomController {

    // @GetMapping
    // String firstEndpoint() {
    //     return "index.html";
    // }

    @GetMapping("/placeShips")
    String placeShips(@RequestParam(name = "height", required = false, defaultValue = "10") int height,
            @RequestParam(name = "width", required = false, defaultValue = "10") int width) {
        System.out.println(height + " " + width);
        return "register.html";
    }
}
