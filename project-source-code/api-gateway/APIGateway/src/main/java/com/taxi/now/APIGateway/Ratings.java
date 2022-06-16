package com.taxi.now.APIGateway;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class Ratings {


    @RequestMapping(value = "/ratings", method = RequestMethod.POST )
    @ResponseBody
    public void getMessage(@RequestParam Map<String, String> requestParams)
       
    throws Exception {
        String userSessionID="";
        String rating = requestParams.get("rating");
        String userId = requestParams.get("userId");
        System.out.println("User ID: " + userId + "\n Rating: " +  rating);

        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("workflow-bpmn.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("user-process")
                    .latestVersion()
                    .variables(Map.of("userID", userId, "userSessionID", userSessionID, "rating", rating))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

        }
        return ;
    }

}
