package io.taxinow.database;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryDrivers {

    private static final Logger LOG = LogManager.getLogger(QueryDrivers.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("email").handler((jobClient, job) -> {
                final String message_content = (String)job.getVariablesAsMap().get("message_content");

                LOG.info("Sending email with message content: {}", message_content);

                // START - Return variables preparation
                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("returnMsg", "Email sent successfully!");
                // END - Return variables preparation

                //jobClient.newCompleteCommand(job.getKey()).send()
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