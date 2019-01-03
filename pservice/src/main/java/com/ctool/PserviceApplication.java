package com.ctool;

import com.ctool.board.webSocket.BoardNettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PserviceApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(PserviceApplication.class, args);
        new BoardNettyServer(12345).start();
    }

}

