package io.taxinow.database;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class QueryDrivers {

    private static final Logger LOG = LogManager.getLogger(QueryDrivers.class);

    public static void main(String[] args) {
        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newWorker().jobType("email").handler((jobClient, job) -> {
                final String message_content = (String)job.getVariablesAsMap().get("message_content");



                        try {
                            // create our mysql database connection
                            String myDriver = "com.mysql.cj.jdbc.Driver";
                            String myUrl = "jdbc:mysql://localhost/Database";
                            Class.forName(myDriver);
                            Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                            // our SQL SELECT query.
                            // if you only need a few columns, specify them by name instead of using "*"
                            String query = "SELECT * FROM Driver WHERE Distance <= 2.5";

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
                                int distance = rs.getInt("Distance");
                                String gender = rs.getString("Gender");

                                // print the results
                                System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, rating, distance, gender);
                            }
                            st.close();
                        } catch (Exception e) {
                            System.err.println("Got an exception! " + e);
                            System.err.println(e.getMessage());
                        }


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