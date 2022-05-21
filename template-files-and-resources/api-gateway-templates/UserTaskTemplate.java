package io.taxinow;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import java.util.List;
import java.util.Map;

public class UserTaskTemplate {

    // Connection Attributes
    private static final String BPMN_FILE_NAME = "example.bpmn";  // The name of the bpmn file located in the resources folder in this project
    private static final String BPMN_PROCESS_ID = "example-processID";  // The process ID is set using the Camunda modeller tool. Each pool has its own process ID
    private static final String USER_TASK_ASSIGNEE = "example-assignee"; // This is set for each user task in the BPMN modeller. It allows the various User Task handlers to know which jobs it needs to do


    public UserTaskTemplate() {}

    /** EVENT: User needs a ride
     *
     * This method is used to deploy BPMN diagram to the Zeebe engine, so that it knows what process it will be managing.
     * The BPMN diagram should be in the resources folder in this project
     */
    public void deployBPMN() {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath(BPMN_FILE_NAME)
                    .send()
                    .join();
        }
    }

    /** NONE START EVENT: the start event the in user pool. There is only ever one "none start event" per diagram
     *
     *
     * When a user logs onto the application, a new process needs to be started in the Camunda engine. The below code instructs Camunda to start its
     * process management engine, known as Zeebe
     */
    public void startNewProcessInstance() {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId(BPMN_PROCESS_ID)
                    .latestVersion()
                    .variables(Map.of("user_xCoord", "the x coordinates", "user_yCoord", "the y coordinates")) // Any initial variables to be set are included here. This could be the user's location, for example
                    .send()
                    .join();
        }
    }


    /** USER TASK
     *
     * Note: in order for this code to work, the necessary package dependency needs to be added to your project. It is located in the GitHub repo
     *
     * ONLY CHANGE WHAT'S IN TO DO!!
     *
     */
    public void templateUserTask() {
        SaasAuthentication sa = new SaasAuthentication("F5JcQaWof3-6q3py6wSKrgFLaUA6W_iO", "zzeeXpNVKVvKMkVTSR3eudDU1NKV0kl4_FNh4q_k6D0is-S5vOrHXBzNlFGU2~hJ");
        CamundaTaskListClient client = null;
        try {
            client = new CamundaTaskListClient.Builder().authentication(sa)
                    .taskListUrl("https://bru-2.tasklist.camunda.io/10f14fca-c94d-40e7-be13-18e9a6803cf1/graphql").build();
            System.out.println("Client connection successful: " + client);

            List<Task> tasks = client.getTasks(true, USER_TASK_ASSIGNEE, TaskState.CREATED, null); // CHANGE the user task id in the constants section as needed

            // Complete all tasks
            for(Task task : tasks) {


                // TODO get the variables from the task, process, and return the results


                client.completeTask(task.getId(), Map.of("user-dest-addr", "Kev's house")); // ADD the return variables (i.e. the user's destination address
                System.out.println("Task completed!");
            }


        } catch (TaskListException e) {
            System.out.println("ERROR " + e);
            e.printStackTrace();
        }
    }
}
