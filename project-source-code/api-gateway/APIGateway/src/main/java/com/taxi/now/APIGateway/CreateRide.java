package com.taxi.now.APIGateway;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class CreateRide
{
    //TODO tried to return new ride
    @PostMapping("/request_a_ride")
    @ResponseBody
    public String getRide(@RequestParam(name = "user_id") List<String> user_id, @RequestParam(name = "userSessionID") List<String> userSessionID, @RequestParam(name="userYCoordinate") List<String> userYCoordinate, @RequestParam(name="userXCoordinate") List<String> userXCoordinate)
    //userSessionID, userYCoordinate, userXCoordinate.
    {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("group6.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("send-email")
                    .latestVersion()
                    .variables(Map.of(user_id, "Hello from the Java get started"))  // variables in "key1, value1, key2, value2" format
                    .variables(Map.of(userSessionID, "Hello from the Java get started"))  // variables in "key1, value1, key2, value2" format
                    .variables(Map.of(userYCoordinate, "Hello from the Java get started"))  // variables in "key1, value1, key2, value2" format
                    .variables(Map.of(userXCoordinate, "Hello from the Java get started"))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

        }
        return "ID: " + user_id + "userXCoordinate" + userXCoordinate ;
    }
    
    //ERALDA & NICK tried to return new ride
    @RequestMapping(value = "/message", method = RequestMethod.POST )
    public void getMessage(@RequestParam Map<String, String> requestParams)
    //userSessionID, userYCoordinate, userXCoordinate.
    throws Exception {
        String x = requestParams.get("x");
        String y = requestParams.get("y");
        String distance = requestParams.get("distance");
        String duration = requestParams.get("duration");
        System.out.println("Latitude: " +  x +
                "\n Longitude :" + y +
                "\n Distance : "+ distance + "\n Duration:" + duration);
        
        
        // your code logic
        return ;
    }

}
