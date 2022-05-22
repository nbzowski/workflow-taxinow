# Templates and Resources

## Writing Code for Camunda

### Deploying and Starting Processes with "None Start"

![None Start Example](images/example-bpmn-none-start.png)

```
try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("send-email.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("send-email")
                    .latestVersion()
                    .variables(Map.of("message_content", "Hello from the Java get started"))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

}       
```

### Deploying and Starting Processes with "Message Start"

![Message Start Example](images/example-bpmn-msg-start.png)

```
try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath(BPMN_FILE_NAME)
                    .send()
                    .join();

            // Correlation key should be blank for starting new processes
            client.newPublishMessageCommand().messageName("start2-msg").correlationKey("").variables("").send().exceptionally( throwable -> { throw new RuntimeException("Could not publish message", throwable); });;
}
```

### User Tasks

![User Tasks Example](images/example-bpmn-user-tasks.png)

### Message Throws

![Message Throw Example](images/example-bpmn-msg-throw.png)

### Service Tasks

![Service Tasks Example](images/example-bpmn-service-tasks.png)
