package io.taxinow.requesthandler;  // CHANGE to match your project

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.taxinow.requesthandler.ZeebeClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendConfirmation {

    private static final Logger LOG = LogManager.getLogger(SendConfirmation.class);

    private static final String JOB_TYPE = "send-confirmation";

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType(JOB_TYPE).handler((jobClient, job) -> {
                final String userSessionID = (String)job.getVariablesAsMap().get("userSessionID");

                LOG.info("Sending message to back-end with user session ID: {}", userSessionID);

                // START - Return variables preparation
                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("userSessionID", userSessionID);
                // END - Return variables preparation

                // messageName -> This is what triggers the corresponding message receive task to become active
                // correlationKey -> This is to identify the process. For example, this could be an emailID, such as email01
                // variables -> variables expected by the message catch event, sent in JSON format

                // For Message Start Events, the message correlation key is blank!!
                client.newPublishMessageCommand().messageName("user-msg-send-dest-address").correlationKey("").variables(variablesMap).send().exceptionally( throwable -> { throw new RuntimeException("Could not publish message", throwable); });;

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