package com.example.scheduler.utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class db_connect {

    private static Connection conn;

    public static void connect(){
        String url = "jdbc:sqlite:ScheduleDB.db";

         conn = null;

        try{
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to DB has been achieved");
        }
        catch (SQLException E)
        {
            System.out.println(E.getMessage());

            System.out.println("DB not connected");

        }


    }

    public static String getSchedProp(String  id) throws SQLException {

        String query = "SELECT value FROM scheduler_properties WHERE id = ? ";

        PreparedStatement preparedStatement = conn.prepareStatement(query);


        preparedStatement.setString(1, id);

        ResultSet results = preparedStatement.executeQuery();

        System.out.println(results);




        return results.getString ("value");
    }

    public static String getJobs() throws SQLException {
        String query = "SELECT id, job_name, task_name, description FROM job_description";

        PreparedStatement preparedStatement = conn.prepareStatement(query);


       // preparedStatement.setString(preparedStatement);

        ResultSet results = preparedStatement.executeQuery();

        int count = 0;
        int idVal = 0;
        while (results.next ())
        {
            idVal = results.getInt ("id");
            String jobName = results.getString ("job_name");
            String task_name = results.getString ("task_name");
            String description = results.getString("description");
            System.out.println (
                    "id = " + idVal
                            + ", name = " + jobName
                            + ", category = " + task_name);

            ++count;
        }

        return Integer.toString(idVal);
    }
}
