package com.taxi.now.APIGateway;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class CreateRide
{
    //TODO tried to return new ride

    //ERALDA & NICK tried to return new ride
    @RequestMapping(value = "/message", method = RequestMethod.POST )
    public void getMessage(@RequestParam Map<String, String> requestParams)
    throws Exception {
        //userSessionID, userYCoordinate, userXCoordinate.
        String userSessionID = "";
        String latitude = requestParams.get("lat");
        String longitude = requestParams.get("lng");
        String userId = requestParams.get("userId");
        System.out.println("Latitude: " +  latitude +
                "\n Longitude :" + longitude +
                "\n User ID : "+ userId);

        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("workflow-bpmn.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("user-process")
                    .latestVersion()
                    .variables(Map.of("userID", userId, "userSessionID", userSessionID, "userYCoordinate", longitude, "userXCoordinate", latitude))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

        }
        // your code logic
        return ;
    }

}
