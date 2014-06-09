/*
 * Copyright (c) 2013, Dwijesh Bhageerutty
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package db;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 2:28:39 PM,
 * Nov 8, 2013 Description:
 */
public class DatabaseConnection implements IDatabaseConnection {

    private final String databaseName;
    private final int portNo;
    private final String hostName;
    private final String userName;
    private final String password;

    private Connection connection;

    public DatabaseConnection(String databaseName, int portNo, String hostName, String userName, String password) {
        this.databaseName = databaseName;
        this.hostName = hostName;
        this.password = password;
        this.portNo = portNo;
        this.userName = userName;
        this.connection = null;
    }

    @Override
    public boolean establishConnection() {
        final String url = "jdbc:mysql://" + hostName + ":" + portNo + "/" + databaseName;
        try {
            System.out.println("Connecting to database...");
            connection = (Connection) DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected!");
            return true;
        } catch (SQLException e) {
            System.err.println("Could not connect to database.");
            return false;
        }
    }

    @Override
    public boolean closeConnection() {
        System.out.println("Closing the connection.");
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException ex) {
                System.out.println("Failed to close connection.");
                return false;
            }
        }
        return true;
    }

    @Override
    public ResultSet executeQuery(String query) {
        if (connection == null) {
            throw new NullPointerException("Trying to execute query when Connection is null.");
        }

        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("SQLException thrown when attempting to execute query" + query);
            System.err.println("Stack trace:\n" + ex.toString());
            System.exit(-1);
        }

        return resultSet;
    }

    @Override
    public void executeInsert(String insert) {
        if (connection == null) {
            throw new NullPointerException("Trying to execute query when Connection is null.");
        }

        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(insert);
        } catch (SQLException ex) {
            System.err.println("SQLException thrown when attempting to execute INSERT statement");
            System.err.println("Stack trace:\n" + ex.toString());
            System.exit(-1);
        }
    }
}