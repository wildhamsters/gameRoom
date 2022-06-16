package org.wildhamsters.gameroom;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameRoomController {
    private AuthenticationManager authManager = new AuthenticationManager() {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String name = authentication.getName();
            String session = authentication.getCredentials().toString();
            
            if (check(name,session)) {
                return new UsernamePasswordAuthenticationToken(
                        name, session, new ArrayList<>());
            } else {
                return authentication;
            }
        }

        boolean check(String name, String session) {
            String userSession = GameRoomApplication.JEDIS.get(name);
            return userSession != null && userSession.equals(session);
        }
    };

    @GetMapping
    String firstEndpoint() {
        return "index.html";
    }

    @GetMapping("/gameroom")
    String placeShips(@RequestParam(name = "userName", required = false, defaultValue = "defaultUser") String userName,
            @RequestParam(name = "sessionId", required = false, defaultValue = "defaultSession") String sessionId,
            HttpServletRequest req) {

        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userName, sessionId);
        Authentication authenticatedUser = authManager.authenticate(loginToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        
        if (authenticatedUser.isAuthenticated())
            return "game.html";
        else
            return "index.html";
    }
}
