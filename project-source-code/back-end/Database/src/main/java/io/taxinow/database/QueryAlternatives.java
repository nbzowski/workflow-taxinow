package io.taxinow.database;

import io.camunda.zeebe.client.ZeebeClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryAlternatives {

    private static final Logger LOG = LogManager.getLogger(QueryAlternatives.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("query-alternative-solutions").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

                // VARIABLES SENT FROM CAMUNDA ZEEBE PROCESS INSTANCE
                final double userXCoordinate = (double)job.getVariablesAsMap().get("userXCoordinate");
                final double userYCoordinate = (double)job.getVariablesAsMap().get("userYCoordinate");


                final String message_content = (String)job.getVariablesAsMap().get("message_content");

                try {

                    // create our mysql database connection
                    String myDriver = "com.mysql.cj.jdbc.Driver";
                    String myUrl = "jdbc:mysql://localhost/Database";
                    Class.forName(myDriver);
                    Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                    // our SQL SELECT query.
                    String query = "SELECT * FROM Alternative_Solution";

                    // create the java statement
                    Statement st = conn.createStatement();

                    // execute the query, and get a java result
                    ResultSet rs = st.executeQuery(query);

                    // iterate through the java result
                    System.out.format("Oops! Looks like there is no driver available in your area\nHere are some alternatives:");
                    while (rs.next()) {
                        String type = rs.getString("Type");


                        // print the results
                        System.out.format("%s\n", type);
                    }
                    st.close();
                } catch (Exception e) {
                    System.err.println("Got an exception! " + e);
                    System.err.println(e.getMessage());
                }



                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***

                // Do something that completes this task

                // *** SERVICE TASK BUSINESS LOGIC BEGINS ***





                // Write a log (change the text to make it job specific
                LOG.info("Sending email with message content: {}", message_content);




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
