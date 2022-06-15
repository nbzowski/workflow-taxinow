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

public class Query_Driver {


    private static final Logger LOG = LogManager.getLogger(Query_Driver.class);
    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("service-task-id").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

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

                    String query = " SELECT * FROM Driver WHERE (Driver_X_Coordinate >= UserXCoordinate - 0.01 AND Driver_X_Coordinate <= UserXCoordinate + 0.01) AND (Driver_Y_Coordinate >= UserYCoordinate - 0.01 AND Driver_Y_Coordinate <= UserYCoordinate + 0.01 AND Availability = 'YES')" ;
                    // create the java statement
                    Statement st = conn.createStatement();

                    // execute the query, and get a java result
                    ResultSet rs = st.executeQuery(query);

                    // iterate through the java result
                    while (rs.next()) {
                        int id = rs.getInt("idDriver");
                        String firstName = rs.getString("First_Name");
                        String lastName = rs.getString("Last_Name");
                        int rating = rs.getInt("Rating");
                        int rating_count = rs.getInt("Rating_Count");
                        String gender = rs.getString("Gender");
                        String username = rs.getString("Username");
                        String availability = rs.getString("Availability");
                        Float driver_x_coordinate  = rs.getFloat("Driver_X_Coordinate");
                        Float driver_y_coordinate  = rs.getFloat("Driver_Y_Coordinate");
                        Float rating_avg  = rs.getFloat("Rating_AVG");


                        // print the results
                        System.out.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n", id, firstName, lastName, rating, rating_count, username, availability, driver_x_coordinate, driver_y_coordinate, gender, rating_avg);
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
