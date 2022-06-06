package com.taxi.now.APIGateway;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class Ratings {

    @PostMapping("/ratings")
    @ResponseBody
    public List showRatings (@RequestParam(name = "rating") List<Integer> rating, @RequestParam(name = "description") List<String> description)
    {
        List list = new ArrayList<>();
        list.add(rating);
        list.add(description);
        return list;
    }

}
