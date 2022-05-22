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

The io.camunda.tasklist dependency must be imported into the IntelliJ project.

```
SaasAuthentication sa = new SaasAuthentication("F5JcQaWof3-6q3py6wSKrgFLaUA6W_iO", "zzeeXpNVKVvKMkVTSR3eudDU1NKV0kl4_FNh4q_k6D0is-S5vOrHXBzNlFGU2~hJ");
CamundaTaskListClient client = null;
try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/10f14fca-c94d-40e7-be13-18e9a6803cf1/graphql").build();
            System.out.println("Client connection successful: " + client);

            List<Task> tasks = client.getTasks(true, USER_TASK_ASSIGNEE, TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            // Complete all tasks
            for(Task task : tasks) {


                // TODO get the variables from the task, do the business logic to complete the user task, and return the results


                client.completeTask(task.getId(), Map.of("user-dest-addr", "Kev's house")); // ADD the return variables (i.e. the user's destination address
              
            }
}
```

### Message Throws

![Message Throw Example](images/example-bpmn-msg-throw.png)

### Service Tasks

![Service Tasks Example](images/example-bpmn-service-tasks.png)
