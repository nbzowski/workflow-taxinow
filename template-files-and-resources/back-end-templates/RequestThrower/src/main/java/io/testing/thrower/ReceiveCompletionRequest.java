package io.testing.thrower;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.Map;

public class ReceiveCompletionRequest {

    public static void main(String[] args) {


        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {

            // Correlation key should be blank for starting new processes
            client.newPublishMessageCommand().messageName("diver-msg-send-completion-request").correlationKey("1234").variables(Map.of("driverConfirmed", "true")).send().exceptionally(throwable -> { throw new RuntimeException("Could not publish message", throwable); });;

            Thread.sleep(10000);

            //client.newPublishMessageCommand().messageName("receive-driver-conf").correlationKey("").variables("").send().exceptionally( throwable -> { throw new RuntimeException("Could not publish message", throwable); });;

        } catch (Exception e) {
            System.out.println("oh no");
        }
    }

}