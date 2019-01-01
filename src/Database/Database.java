package Database;

import java.sql.*;


public class Database {


    private static Database _instance = new Database();
    private Connection _connection;

    private static String _dbName;


    public static void SetDBname(String value)
    {
        _dbName = value;
    }


    private Database()
    {

    }

    public static Database GetInstance()
    {
        return _instance;
    }

    public Connection GetConnection()
    {
        return _connection;
    }

    public void Connect()
    {
        if(_connection != null)
            return;


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }


        try{
            _connection = DriverManager.getConnection("jdbc:mysql://localhost/"+_dbName, "root", "");
            System.out.print("Database.Database is connected!");

        }
        catch(Exception exc)
        {
            System.out.print("Unable to connect to Database\n");
        }


    }


}



