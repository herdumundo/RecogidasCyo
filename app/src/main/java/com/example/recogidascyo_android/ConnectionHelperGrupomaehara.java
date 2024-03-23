package com.example.recogidascyo_android;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by hvelazquez on 04/04/2018.
 */

public class ConnectionHelperGrupomaehara {
    String IP,DB,DBUsername,DBPassword;
    @SuppressLint("NewaApi")
    public Connection Connections()
    {
        IP="172.16.1.202";
        DB="grupomaehara";
        DBUsername="sa";
        DBPassword="Paraguay2017";
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection=null;
        String ConnectionURL=null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + IP +";databaseName="+ DB + ";user=" + DBUsername+ ";password=" + DBPassword + ";";
            connection= DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        return connection;
    }





}

