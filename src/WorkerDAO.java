import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {



    public void AddWorker(Worker worker) throws SQLException {

        Connection conn = Database.GetInstance().GetConnection();

        PreparedStatement p = conn.prepareStatement("INSERT INTO `worker` (`ID`, `PESEL`, `WorkerType`, `Name`, `Surname`, `BusinessPhone`, `Salary`) VALUES (NULL, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        p.setString(1, worker.GetWorkerPesel());        //Pesel
        p.setInt(2, worker.GetWorkerID());                     //WorkerType
        p.setString(3,worker.GetWorkerName());              //Name
        p.setString(4, worker.GetWorkerSurname());          //Surname;
        p.setString(5,worker.GetWorkerBusinessPhone());     //BusinessPhone
        p.setDouble(6, worker.GetWorkerSalary());            //Salary

        p.executeUpdate();

        ResultSet rs = p.getGeneratedKeys();
        int generatedKey = 0;
        if (rs.next()) {
            generatedKey = rs.getInt(1);
        }

        p.close();

        //For trader
        if(worker.GetWorkerID() == 1)
        {
            WorkerTrader workerTrader = (WorkerTrader) worker;

            PreparedStatement p2 = conn.prepareStatement("INSERT INTO `trader` (`ID`, `WorkerID`, `Commision`, `CommisionLimit`) VALUES (NULL, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            p2.setInt(1, generatedKey);
            p2.setFloat(2, workerTrader.GetCommision());
            p2.setFloat(3, workerTrader.GetCommisionLimit());

            p2.executeUpdate();
            p2.close();
        }

        //For Manager
        if(worker.GetWorkerID() == 2)
        {
            WorkerManager workerManager = (WorkerManager) worker;

            PreparedStatement p2 = conn.prepareStatement("INSERT INTO `manager` (`ID`, `WorkerID`, `BusinessAllowance`, `CostLimit`, `ServiceCardNumber`) VALUES (NULL, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

            p2.setInt(1, generatedKey);
            p2.setFloat(2, workerManager.GetWorkerBusinessAllowance());
            p2.setFloat(3,workerManager.GetWorkerCostLimit());
            p2.setString(4, workerManager.GetServiceCardNumber());

            p2.executeUpdate();
            p2.close();

        }

    }

    public Worker GetWorker(String pesel) throws SQLException {

        String query = "SELECT * FROM `worker` WHERE `PESEL` = " + pesel + ";";
        Connection conn = Database.GetInstance().GetConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        int workerType = 0;

        try{
            while(rs.next())
                workerType = rs.getInt("WorkerType");
        }
        catch(SQLException exception)
        {
            return null;
        }


        String query2 = "";


        switch(workerType)
        {
            case 1:
                query2 = "SELECT * FROM `worker`, `trader` where `worker`.`ID` = `trader`.`WorkerID` WHERE `pesel` = " + pesel +" ;";
                break;
            case 2:
                query2 = "SELECT * FROM `worker`, `manager` where `worker`.`ID` = `manager`.`WorkerID` WHERE `pesel` = " + pesel +" ;";
                break;
            default: return null;
        }

        ResultSet rs2 = st.executeQuery("SELECT * FROM `worker`, `manager` where `worker`.`ID` = `manager`.`WorkerID`;");
        rs2.next();
        return RetrieveInformationFromResultSet(rs2);
    }



    private Worker RetrieveInformationFromResultSet(ResultSet rs) throws SQLException {
        int workerType = rs.getInt("workerType");


        if(workerType == 1)
        {
            int id  = rs.getInt("ID");
            String pesel = rs.getString("Pesel");
            String name = rs.getString("Name");
            String surname = rs.getString("Surname");
            String businessPhone = rs.getString("BusinessPhone");
            float salary = rs.getFloat("Salary");
            float commision = rs.getFloat("Commision");
            float commisionLimit = rs.getFloat("CommisionLimit");

            return new WorkerTrader(id, pesel,name,surname,businessPhone,salary,commision, commisionLimit);
        }

        if(workerType == 2)
        {
            int id  = rs.getInt("ID");
            String pesel = rs.getString("Pesel");
            String name = rs.getString("Name");
            String surname = rs.getString("Surname");
            String businessPhone = rs.getString("BusinessPhone");
            float salary = rs.getFloat("Salary");
            float businessAllowance = rs.getFloat("BusinessAllowance");
            float costLimit = rs.getFloat("CostLimit");
            String serviceCardNumber = rs.getString("ServiceCardNumber");

            return new WorkerManager(id, pesel,name,surname,businessPhone,salary,businessAllowance,costLimit,serviceCardNumber);

        }

        return null;
    }



    public List<Worker> GetWorkers() throws  SQLException
    {

        List<Worker> workers = new ArrayList<>();

        Connection conn = Database.GetInstance().GetConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM `worker`, `manager` where `worker`.`ID` = `manager`.`WorkerID`;");


        while(rs.next())
            workers.add(RetrieveInformationFromResultSet(rs));

        ResultSet rs2 = stmt.executeQuery("SELECT * FROM `worker`, `trader` where `worker`.`ID` = `trader`.`WorkerID`;");

        while(rs2.next())
            workers.add(RetrieveInformationFromResultSet(rs2));


        stmt.close();
        return workers;
    }


    public void UpdateWorker(Worker worker)
    {

    }


    private boolean DeleteWorker(Worker worker) throws SQLException {

        Connection conn = Database.GetInstance().GetConnection();
        Statement stmt = conn.createStatement();
        String query = "";
        String query2 = "DELETE FROM `worker` WHERE `worker`.`ID` = " + worker.GetDatabaseID() + " ;";

        switch(worker.GetWorkerID())
        {
            case 1:
                query = "DELETE FROM `trader` WHERE `trader`.`WorkerID` = "+worker.GetDatabaseID() + " ;";
                break;
            case 2:
                query = "DELETE FROM `manager` WHERE `manager`.`WorkerID` = "+worker.GetDatabaseID() + " ;";
                break;

            default: return false;
        }

        stmt.executeUpdate(query);
        stmt.executeUpdate(query2);
        stmt.close();

        return true;
    }

    public boolean DeleteWorker(String pesel) throws SQLException {

        Worker worker = GetWorker(pesel);

        if(worker != null)
            return DeleteWorker(worker);


        return false;
    }



}
