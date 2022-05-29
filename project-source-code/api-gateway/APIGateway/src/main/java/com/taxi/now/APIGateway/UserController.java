package com.taxi.now.APIGateway;


import ch.qos.logback.core.net.server.Client;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/user_needs_a_ride")
//TODO still not working as imagined
public class UserController {
        @GetMapping("message")
        @ResponseBody
        public String getMessage(@RequestParam String id) {
            return "Successfully returned: " + id;
        }


}

