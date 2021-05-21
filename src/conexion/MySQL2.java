package conexion;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQL2 {

    public static Connection getConnection()
    {
        if(connection == null) {
            try {
                connectToServer();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    static Connection connection = null;
    static Session session = null;
    static String dataBaseName = "libreria";

    static void connectToServer() throws SQLException {
        connectSSH();
        connectToDataBase(dataBaseName);
    }

    static int localPort = 2022; // any free port can be used
    static void connectSSH() throws SQLException {
        String sshHost = "edu5975.ddns.net";
        String sshuser = "ubuntu";
        File file = new File("src/conexion/");
        String SshKeyFilepath = file.getAbsolutePath()+"/masterpass5975.pem";

        String remoteHost = "localhost";
        int remotePort = 3306;

        String driverName = "com.mysql.cj.jdbc.Driver";

        try {
            java.util.Properties config = new java.util.Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshuser, sshHost, 22);
            jsch.addIdentity(SshKeyFilepath);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "3");
            session.setConfig(config);
            session.connect();

            System.out.println("SSH Connected");

            Class.forName(driverName).newInstance();

            int assinged_port = session.setPortForwardingL("127.0.0.1",localPort, remoteHost, remotePort);

            System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
            System.out.println("Port Forwarded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void connectToDataBase(String dataBaseName) throws SQLException {
        String dbuserName = "root";
        String dbpassword = "masterpass5975";
        String localSSHUrl = "localhost";
        try {

            //mysql database connectivity
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(dbuserName);
            dataSource.setAllowMultiQueries(true);

            dataSource.setPassword(dbpassword);
            dataSource.setDatabaseName(dataBaseName);

            connection = dataSource.getConnection();

            System.out.print("Connection to server successful!:" + connection + "\n\n");

            List<String> data = getAllDBNames();
            for(String s:data){
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
    }

    static void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    static ResultSet executeMyQuery(String query, String dataBaseName) {
        ResultSet resultSet = null;

        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            System.out.println("Database connection success");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }



    static List<String> getAllDBNames() {
        // get all live db names incentral DB
        List<String> organisationDbNames = new ArrayList<String>();
        ResultSet resultSet = executeMyQuery("show databases", "DB1");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("Database");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organisationDbNames;
    }

    
}
