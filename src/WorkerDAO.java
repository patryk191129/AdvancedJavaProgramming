import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
                query2 = "SELECT * FROM `worker`, `trader` where `worker`.`ID` = `trader`.`WorkerID` AND `pesel` = " + pesel +" ;";
                break;
            case 2:
                query2 = "SELECT * FROM `worker`, `manager` where `worker`.`ID` = `manager`.`WorkerID` AND `pesel` = " + pesel +" ;";
                break;
            default: return null;
        }

        ResultSet rs2 = st.executeQuery(query2);
        rs2.next();
        return RetrieveInformationFromResultSet(rs2);
    }


    private void ReplaceData(List<Worker> workers) throws SQLException {
        Connection conn = Database.GetInstance().GetConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM `trader`;");
        stmt.executeUpdate("DELETE FROM `manager`");
        stmt.executeUpdate("DELETE FROM `worker`");
        stmt.close();


        for(int i=0;i<workers.size();i++)
        {
            AddWorker(workers.get(i));
        }


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


    public void ArchiveDatabaseToFile(String filename) throws SQLException
    {
        List<Worker> workerList = GetWorkers();

        if(workerList.size() > 0) {
            try {
                FileOutputStream fos = new FileOutputStream(filename + ".zip");
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                for(int i=0;i<workerList.size();i++) {

                    Worker currWorker = workerList.get(i);


                    File fileToZip = new File(currWorker._pesel);
                    fileToZip.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(currWorker._pesel));

                    switch(currWorker.GetWorkerID())
                    {
                        case 1:

                            WorkerTrader trader = (WorkerTrader) currWorker;
                            writer.write("1"); writer.newLine();
                            writer.write(trader.GetWorkerName());writer.newLine();
                            writer.write(trader.GetWorkerSurname());writer.newLine();
                            writer.write(trader.GetWorkerBusinessPhone());writer.newLine();
                            writer.write(Float.toString(trader.GetWorkerSalary()));writer.newLine();
                            writer.write(Float.toString(trader.GetCommision()));writer.newLine();
                            writer.write(Float.toString(trader.GetCommisionLimit()));writer.newLine();

                            break;

                        case 2:
                            WorkerManager manager = (WorkerManager) currWorker;

                            writer.write("2");writer.newLine();
                            writer.write(manager.GetWorkerName());writer.newLine();
                            writer.write(manager.GetWorkerSurname());writer.newLine();
                            writer.write(manager.GetWorkerBusinessPhone());writer.newLine();
                            writer.write(Float.toString(manager.GetWorkerSalary()));writer.newLine();
                            writer.write(Float.toString(manager.GetWorkerBusinessAllowance()));writer.newLine();
                            writer.write(Float.toString(manager.GetWorkerCostLimit()));writer.newLine();
                            writer.write(manager.GetServiceCardNumber());writer.newLine();
                            break;
                    }

                    writer.close();

                    FileInputStream fis = new FileInputStream(fileToZip);

                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                    fileToZip.delete();
                }
                zipOut.close();

                fos.close();

            } catch (IOException exception) {
                System.out.println(exception);


            }
        }
        else
            System.out.println("Nothing to archive");



    }

    public void BackupDatabaseFromFile(String filename) throws SQLException, IOException
    {
        List<Worker> workers = new ArrayList<>();
        ZipFile zf = new ZipFile(filename);

        Enumeration<? extends ZipEntry> entries = zf.entries();


        while(entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement();

            InputStream inputStream = zf.getInputStream(entry);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            List<String> vals = new ArrayList<>();

            str = bufferedReader.readLine();
            int groupID = Integer.parseInt(str);

            while((str = bufferedReader.readLine()) != null)
                vals.add(str);



            switch(groupID)
            {
                case 1:
                    workers.add(new WorkerTrader(1, entry.getName(), vals.get(0), vals.get(1), vals.get(2), Float.parseFloat(vals.get(3)), Float.parseFloat(vals.get(4)), Float.parseFloat(vals.get(5))));
                    break;

                case 2:
                    workers.add(new WorkerManager(2, entry.getName(), vals.get(0), vals.get(1), vals.get(2), Float.parseFloat(vals.get(3)), Float.parseFloat(vals.get(4)), Float.parseFloat(vals.get(5)), vals.get(6)));
                    break;

            }




            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();



        }

        try {
            ReplaceData(workers);
        }
        catch(SQLException exception)
        {
            System.out.println(exception);
        }
    }



}
