package io.taxinow.navigation;
import io.camunda.zeebe.client.ZeebeClient;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class FindDrivers {

    private static final Logger LOG = LogManager.getLogger(FindDrivers.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("find-drivers").handler((jobClient, job) -> {
                //final String message_content = (String)job.getVariablesAsMap().get("message_content");

                //LOG.info("Sending email with message content: {}", message_content);

                // START - Return variables preparation
                Map<String, Object> variablesMap = job.getVariablesAsMap();
                //variablesMap.put("returnMsg", "Email sent successfully!");
                // END - Return variables preparation

                // Send the job completed command to Camunda. This is how Camunda knows to move to the next task in the process
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