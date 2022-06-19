package com.taxi.now.APIGateway;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentRestController {

    private static final String USER_TASK_ASSIGNEE = "user-accept-or-decline" ;

    @RequestMapping(value = "/customer_accept", method = RequestMethod.POST )
    public void onLoad(@RequestParam Map<String, String> requestParams)
            throws Exception {
        //userSessionID, userYCoordinate, userXCoordinate.
        String accept = requestParams.get("accept");

        System.out.println(" \nAccepted : "+ accept);


        SaasAuthentication sa = new SaasAuthentication("aMLk~WQhyZ5BgzOcIx7mDizztELApFAt", "1PP6LP.F5~gRvKuU~lDd7KtC_4frOI56-IoeWVuAM3GT4bOnnxyQu~qBI_71p~9e");
        CamundaTaskListClient client = null;
        try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/a8434432-ba71-4f54-b4ab-aa03d19131e9/graphql").build();
            System.out.println("Client connection successful: " + client);

            List<Task> tasks = client.getTasks(true, USER_TASK_ASSIGNEE, TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            // Complete all tasks
            for(Task task : tasks) {


                // TODO get the variables from the task, do the business logic to complete the user task, and return the results


                client.completeTask(task.getId(), Map.of("userAccepted", accept)); // ADD the return variables (i.e. the user's destination address

            }
        } catch (TaskListException e) {
            System.out.println("ERROR " + e);
            e.printStackTrace();
        }



     //   String  userSessionID = userId + "_" + (new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
        // your code logic
        return ;
    }
    @RequestMapping(value = "/payment_info", method = RequestMethod.POST )
    public void getPaymentInfo(@RequestParam Map<String, String> requestParams)
    throws Exception {
        String userSessionID="";
        String cardHolder = requestParams.get("cardHolder");
        String creditCardNum = requestParams.get("creditCardNum");
        String expireMonth = requestParams.get("expireMonth");
        String expireYear = requestParams.get("expireYear");
        String userId = requestParams.get("userId");
      //  String CVV = requestParams.get("CVV");
        System.out.println("User Id: " + userId + "\n Card Holder: " +  cardHolder +
                "\n Card number: " + creditCardNum +
                "\n Expiration month: "+ expireMonth + "\n Expiration year: " + expireYear );

        // CAMUNDA CLIENT CODE
        SaasAuthentication sa = new SaasAuthentication("aMLk~WQhyZ5BgzOcIx7mDizztELApFAt", "1PP6LP.F5~gRvKuU~lDd7KtC_4frOI56-IoeWVuAM3GT4bOnnxyQu~qBI_71p~9e");
        CamundaTaskListClient client = null;
        try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/a8434432-ba71-4f54-b4ab-aa03d19131e9/graphql").build();
            System.out.println("Client connection successful: " + client);

            List<Task> tasks = client.getTasks(true, "user-enter-payment-details", TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            // Complete all tasks
            for(Task task : tasks) {


                // TODO get the variables from the task, do the business logic to complete the user task, and return the results

                /*try (ZeebeClient client2 = ZeebeClientFactory.getZeebeClient()) {
                    client2.newPublishMessageCommand().messageName("user-msg-send-dest-address").correlationKey("").variables("").send().exceptionally(throwable -> {
                        throw new RuntimeException("Could not publish message", throwable);
                    });
                };*/

                client.completeTask(task.getId(), Map.of("userId", userId, "expireYear", expireYear, "expireMonth", expireMonth, "creditCardNum", creditCardNum, "cardHolder", cardHolder)); // ADD the return variables (i.e. the user's destination address

            }
        } catch (TaskListException e) {
            System.out.println("ERROR " + e);
            e.printStackTrace();
        }


        return ;
    }



}
