package io.taxinow.requesthandler;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.taxinow.requesthandler.ZeebeClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessAlternatives {

    private static final Logger LOG = LogManager.getLogger(ProcessAlternatives.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("process-alternatives").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

                // VARIABLES SENT FROM CAMUNDA ZEEBE PROCESS INSTANCE
                final String some_variable_1 = (String)job.getVariablesAsMap().get("some_variable_1");
                final String some_variable_2 = (String)job.getVariablesAsMap().get("some_variable_2");
                final String some_variable_3 = (String)job.getVariablesAsMap().get("some_variable_3");




                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***

                // Do something that completes this task

                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***





                // Write a log (change the text to make it job specific
                //LOG.info("Sending email with message content: {}", message_content);




                // *** START - Return variables preparation ***

                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("key", "value");

                // *** END - Return variables preparation ***





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