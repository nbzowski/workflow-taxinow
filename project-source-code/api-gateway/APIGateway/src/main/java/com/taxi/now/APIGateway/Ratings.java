package com.taxi.now.APIGateway;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
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
        String count = requestParams.get("count");
        String userId = requestParams.get("userId");
        System.out.println("User ID: " + userId + "\n Count: " +  count);

        SaasAuthentication sa = new SaasAuthentication("aMLk~WQhyZ5BgzOcIx7mDizztELApFAt", "1PP6LP.F5~gRvKuU~lDd7KtC_4frOI56-IoeWVuAM3GT4bOnnxyQu~qBI_71p~9e");
        CamundaTaskListClient client = null;
        try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/a8434432-ba71-4f54-b4ab-aa03d19131e9/graphql").build();
            System.out.println("Client connection successful: " + client);

            List<Task> tasks = client.getTasks(true, "rate-driver", TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            // Complete all tasks
            for(Task task : tasks) {


                client.completeTask(task.getId(), Map.of("userId", userId, "count", count)); // ADD the return variables (i.e. the user's destination address

            }
        } catch (TaskListException e) {
            System.out.println("ERROR " + e);
            e.printStackTrace();
        }

        return ;
    }

}
