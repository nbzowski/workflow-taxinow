package io.taxinow.navigation;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FindDrivers {

    private static final Logger LOG = LogManager.getLogger(FindDrivers.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("find-drivers").handler((jobClient, job) -> {
                final String UserXCoordinate = (String)job.getVariablesAsMap().get("userXCoordinate");
                final String UserYCoordinate = (String)job.getVariablesAsMap().get("userYCoordinate");
                // LOG.info("Sending email with message content: {}", message_content);
                // fut logjiken e taskut tat. Queries per koordinatat
                // START - Return variables preparation
                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("returnMsg", "Email sent successfully!");
                // END - Return variables preparation

                // heero

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
