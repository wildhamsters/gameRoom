package org.wildhamsters.gameroom;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

class Logger {
    static void log(Log.Level level, Class<?> className, String msg) {
        RestTemplate restTemplate = new RestTemplate();
        Log log = new Log(level.toString(), LocalDateTime.now().toString(),
                "gameRoom", className.getName(), msg);
        restTemplate.postForObject("http://elk:8000/log", log, Log.class);
    }
}
