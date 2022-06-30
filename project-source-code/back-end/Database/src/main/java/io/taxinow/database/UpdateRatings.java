package io.taxinow.database;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateRatings {

    private static final Logger LOG = LogManager.getLogger(UpdateRatings.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("update-driver-rating").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

                // TODO get variables from Camunda
                // VARIABLES SENT FROM CAMUNDA ZEEBE PROCESS INSTANCE
                //final String some_variable_1 = (String)job.getVariablesAsMap().get("some_variable_1");
                //final String some_variable_2 = (String)job.getVariablesAsMap().get("some_variable_2");
                //final String some_variable_3 = (String)job.getVariablesAsMap().get("some_variable_3");
                //-------------------------------------------------------------------------------------------------------------------------------------
                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***

                // TODO something that completes this task

                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***
                //-------------------------------------------------------------------------------------------------------------------------------------
                // *** START - Return variables preparation ***

                //TODO prepare return variables to send back to Camunda
                //Map<String, Object> variablesMap = new HashMap<>();
                //variablesMap.put("key", "value");

                // *** END - Return variables preparation ***
                //-------------------------------------------------------------------------------------------------------------------------------------

                jobClient.newCompleteCommand(job.getKey()).send() // Replace the line below with this one to use for tasks that do not return variables
                //jobClient.newCompleteCommand(job.getKey()).variables(variablesMap).send()
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
