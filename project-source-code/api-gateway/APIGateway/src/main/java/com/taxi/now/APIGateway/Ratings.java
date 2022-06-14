package com.taxi.now.APIGateway;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class Ratings {

   
    @RequestMapping(value = "/ratings", method = RequestMethod.POST )
    public void getMessage(@RequestParam Map<String, String> requestParams)
       
    throws Exception {
        String rating = requestParams.get("rating");
        String description = requestParams.get("description");
        System.out.println("Rating: " +  rating +
                "\n Description :" + description);
              
        return ;
    }

}
