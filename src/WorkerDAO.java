import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.sql.*;
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

    public Worker GetWorker(int id)
    {
        return null;
    }


    public List<Worker> GetWorkers()
    {
        return null;
    }


    public void UpdateWorker(Worker worker)
    {

    }


    public void DeleteWorker(Worker worker)
    {

    }



}
