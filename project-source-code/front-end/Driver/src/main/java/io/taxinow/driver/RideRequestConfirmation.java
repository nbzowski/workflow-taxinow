package io.taxinow.driver;
import io.camunda.zeebe.client.ZeebeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RideRequestConfirmation {

    private static final Logger LOG = LogManager.getLogger(RideRequestConfirmation.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("driver-send-ride-conf").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

                // VARIABLES SENT FROM CAMUNDA ZEEBE PROCESS INSTANCE
                final String userSessionID = (String)job.getVariablesAsMap().get("userSessionID");
                /*final String some_variable_2 = (String)job.getVariablesAsMap().get("some_variable_2");
                final String some_variable_3 = (String)job.getVariablesAsMap().get("some_variable_3");*/

                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***


                // *** START - Return variables preparation ***
                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("driverConfirmed", "true");
                // *** END - Return variables preparation ***


                client.newPublishMessageCommand().messageName("driver-msg-send-confirmation").correlationKey(userSessionID).variables(variablesMap).send().exceptionally( throwable -> { throw new RuntimeException("Could not publish message", throwable); });;

                // *** SERVICE TASK BUSINESS LOGIC ENDS ***


                //jobClient.newCompleteCommand(job.getKey()).send() // Uncomment and use this once for tasks that do not return variables
                jobClient.newCompleteCommand(job.getKey()).variables(variablesMap).send()
                        .whenComplete((result, exception) -> {
                            if (exception == null) {
                                LOG.info("Completed job successfully with result: " + result);
                            } else {
                                LOG.info("Failed to complete job", exception);
                            }
                        });
            }).open();

            // run until System.in receives exit command
            waitUntilSystemInput("exit");
        }
    }

    // Allows the service task to run and continually poll for jobs to work on
    private static void waitUntilSystemInput(final String exitCode) {
        try (final Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                final String nextLine = scanner.nextLine();
                if (nextLine.contains(exitCode)) {
                    return;
                }
            }
        }
    }

}
