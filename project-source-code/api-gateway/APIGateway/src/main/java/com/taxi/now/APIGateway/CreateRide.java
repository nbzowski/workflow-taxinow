package com.taxi.now.APIGateway;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user_needs_a_ride")
public class CreateRide //@RequestBody Ride newRide
{
    //TODO tried to return new ride
    @PostMapping("message")
    public String getMessage()
    {
        String input = "{\"key\":\"user_id\",\"value\":\"22\"}";
        return input;
    }

}
