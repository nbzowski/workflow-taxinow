package io.taxinow.database;

import com.google.common.base.Joiner;
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
import java.util.*;


public class Query_Driver{


    private static final Logger LOG = LogManager.getLogger(Query_Driver.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("query-drivers").handler((jobClient, job) -> { // service-task-id must match the task type in Camunda!

                System.out.println("Connected to Zeebe");

                // VARIABLES SENT FROM CAMUNDA ZEEBE PROCESS INSTANCE




                List<Integer> Driver_id = new ArrayList<Integer>();
                List<String> Firstname = new ArrayList<String>();
                List<String> Lastname = new ArrayList<String>();
                List<Integer> rating = new ArrayList<Integer>();
                List<String> gender = new ArrayList<String>();
                List<String> availability = new ArrayList<String>();
                List<Integer> ratingcount = new ArrayList<Integer>();
                List<String> username = new ArrayList<String>();
                List<Float> d_x_coordinate = new ArrayList<Float>();
                List<Float> d_y_coordinate = new ArrayList<Float>();
                List<Float> ratingavg = new ArrayList<Float>();


                //final String message_content = (String)job.getVariablesAsMap().get("message_content");

                try {
                    final double userXCoordinate = Double.parseDouble((String) job.getVariablesAsMap().get("userXCoordinate"));
                    final double userYCoordinate = Double.parseDouble((String) job.getVariablesAsMap().get("userYCoordinate"));

                    System.out.println("Querying database");

                    // create our mysql database connection
                    String myDriver = "com.mysql.cj.jdbc.Driver";
                    String myUrl = "jdbc:mysql://localhost/Database";
                    Class.forName(myDriver);
                    Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                    // our SQL SELECT query.

                    String query = " SELECT * FROM Driver WHERE (Driver_X_Coordinate >= "+ userXCoordinate +" - 0.5 AND Driver_X_Coordinate <= "+ userXCoordinate +" + 0.5) AND (Driver_Y_Coordinate >= "+ userYCoordinate +" - 0.5 AND Driver_Y_Coordinate <= " + userYCoordinate +" + 0.5 AND Availability = 'YES')" ;
                    // create the java statement
                    Statement st = conn.createStatement();

                    // execute the query, and get a java result
                    ResultSet rs = st.executeQuery(query);


                    // iterate through the java result
                    while (rs.next()) {

                        int id = rs.getInt("idDriver");
                        Driver_id.add(id);


                        String firstName = rs.getString("First_Name");
                        Firstname.add(firstName);


                        String lastName = rs.getString("Last_Name");
                        Lastname.add(lastName);


                        int Rating = rs.getInt("Rating");
                        rating.add(Rating);


                        int rating_count = rs.getInt("Rating_Count");
                        ratingcount.add(rating_count);


                        String Gender = rs.getString("Gender");
                        gender.add(Gender);

                        String Username = rs.getString("Username");
                        username.add(Username);


                        String Availability = rs.getString("Availability");
                        availability.add(Availability);

                        Float driver_x_coordinate = rs.getFloat("Driver_X_Coordinate");
                        d_x_coordinate.add(driver_x_coordinate);


                        Float driver_y_coordinate = rs.getFloat("Driver_Y_Coordinate");
                        d_y_coordinate.add(driver_y_coordinate);


                        Float rating_avg = rs.getFloat("Rating_AVG");
                        ratingavg.add(rating_avg);


                        // print the results

                        System.out.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n", Driver_id, Firstname, Lastname, rating, ratingcount, username, availability, d_x_coordinate, d_y_coordinate, gender, ratingavg);

                    }


                    st.close();
                } catch (Exception e) {
                    System.err.println("Got an exception! " + e);
                    System.err.println(e.getMessage());
                }


                String allFirstNames = String.join(",", Firstname);
                String allLastNames = String.join(",", Lastname);
                String allRating = Joiner.on(",").join(rating);
                String allRatingCount = Joiner.on(",").join(ratingcount);


                System.out.print(allFirstNames);
                System.out.print(allLastNames);
                System.out.print(allRating);
                System.out.print(allRatingCount);


                // * SERVICE TASK BUSINESS LOGIC BEGINS *

                // Do something that completes this task

                // * SERVICE TASK BUSINESS LOGIC BEGINS *


                // Write a log (change the text to make it job specific
                //  LOG.info("Sending email with message content: {}", message_content);


                // * START - Return variables preparation *

                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put("firstname", allFirstNames);
                variablesMap.put("lastname", allLastNames);
                variablesMap.put("Rating", allRating);
                variablesMap.put("rating_count", allRatingCount);

                System.out.format(String.valueOf(variablesMap));


                // * END - Return variables preparation *

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
