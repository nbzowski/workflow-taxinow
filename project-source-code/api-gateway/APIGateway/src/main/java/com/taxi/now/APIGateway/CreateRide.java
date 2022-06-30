package com.taxi.now.APIGateway;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.*;
//import io.camunda.tasklist.exception.TaskListException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class CreateRide
{
    private static final String USER_TASK_ASSIGNEE = "user-api-gateway" ;
    //TODO tried to return new ride

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public void getHealth() {
        return ;
    }

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
        SaasAuthentication sa = new SaasAuthentication("aMLk~WQhyZ5BgzOcIx7mDizztELApFAt", "1PP6LP.F5~gRvKuU~lDd7KtC_4frOI56-IoeWVuAM3GT4bOnnxyQu~qBI_71p~9e");
        CamundaTaskListClient client = null;
        try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/a8434432-ba71-4f54-b4ab-aa03d19131e9/graphql").build();
            System.out.println("Client connection successful: " + client);

            //List<Task> tasks = client.getTasks(true, USER_TASK_ASSIGNEE, TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            List<Task> tasks = client.getTasksWithVariables(true, "user-api-gateway", TaskState.CREATED, null);

            // Complete all tasks
            for(Task task : tasks) {


                // TODO get the variables from the task, do the business logic to complete the user task, and return the results

                try (ZeebeClient client2 = ZeebeClientFactory.getZeebeClient()) {
                    System.out.println("Throwing message");

                    // Get and send all variables
                    // ADDED: task.getVariables();
                    //System.out.println("Variables: " + task.getVariables().get(0));

                    client2.newPublishMessageCommand().messageName("send-dest-addr").correlationKey(userId).variables(Map.of("userSessionID", userId)).send().exceptionally(throwable -> {
                        throw new RuntimeException("Could not publish message", throwable);
                    });
                    Thread.sleep(10000);
                };

                //client.completeTask(task.getId(), Map.of("test", "test"));
                client.completeTask(task.getId(), Map.of("user-dest-addr", "Starbucks")); // ADD the return variables (i.e. the user's destination address

            }
        } catch (Exception e) {
            System.out.println("ERROR " + e);
            //e.printStackTrace();
        }

        // your code logic
        return ;
    }



    @RequestMapping(value = "/load", method = RequestMethod.POST )
    public void onLoad(@RequestParam Map<String, String> requestParams)
            throws Exception {
        //userSessionID, userYCoordinate, userXCoordinate.
        String userId = requestParams.get("userId");

        System.out.println(" \nUser ID : "+ userId);

        String  userSessionID = userId + "_" + (new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));


        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("combined-bpmn.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("user-process-combined")
                    .latestVersion()
                    .variables(Map.of("userId", userId, "userSessionID", userId))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

        }
        // your code logic
        return ;
    }

}
