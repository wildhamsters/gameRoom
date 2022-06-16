package org.wildhamsters.gameroom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

class GameRoomSuccessLogoutHandler extends
        SimpleUrlLogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
            String userName = authentication.getName();
            long res = GameRoomApplication.JEDIS.del(userName);
            if(res == 0) {
                System.out.println("User not found");
            } else if(res == 1) {
                System.out.println("User logged out");
            } else {
                System.out.println("Something strange happened " + res);
            }
        super.onLogoutSuccess(request, response, authentication);
    }
}
