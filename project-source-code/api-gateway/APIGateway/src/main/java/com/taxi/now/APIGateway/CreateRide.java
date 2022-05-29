package com.taxi.now.APIGateway;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user_needs_a_ride")
public class CreateRide
{
    //TODO tried to return new ride
    @PostMapping("message")
    @ResponseBody
    public String getMessage(@RequestParam(name = "user_id") List<String> user_id)
    //userSessionID, userYCoordinate, userXCoordinate.
    {
        return "ID: " + user_id ;
        //String input = "{\"key\":\"user_id\",\"value\":\"22\"};

        //
    }

}
