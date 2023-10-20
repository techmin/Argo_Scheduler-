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

    public static void getJobs(String id) throws SQLException {

        String query = "SELECT * FROM scheduler_properties WHERE id = ? ";

        PreparedStatement preparedStatement = conn.prepareStatement(query);


        preparedStatement.setString(1, id);

        ResultSet results = preparedStatement.executeQuery();





        preparedStatement.close();
    }

    public static void insertJobs()
    {

    }
}
