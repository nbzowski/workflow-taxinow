package com.taxi.now.APIGateway;


import ch.qos.logback.core.net.server.Client;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user_needs_a_ride")
public class UserController {
        @GetMapping("message")
        public String getMessage() {
            return "Successfully returned";
        }


}

