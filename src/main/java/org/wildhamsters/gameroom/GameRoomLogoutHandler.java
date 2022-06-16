package org.wildhamsters.gameroom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class GameRoomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
            String userName = authentication.getName();
            long res = GameRoomApplication.JEDIS.del(userName);
            if(res == 0) {
                System.out.println("User not found");
            } else if(res == 1) {
                System.out.println("User logged out");
            } else {
                System.out.println("Something strange happened " + res);
            }
    }
}