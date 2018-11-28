import java.text.DecimalFormat;

public class Worker {

    public enum WorkerID{
        TRADER(1),
        MANAGER(2);

        private int numVal;

        WorkerID(int numVal)
        {
            this.numVal = numVal;
        }

        public int getNumVal()
        {
            return numVal;
        }
    }


    protected WorkerID _workerID;
    protected String _pesel;
    protected String _name;
    protected String _surname;
    protected float _salary;
    protected String _businessPhone;
    protected int _id;

    public String GetWorkerPesel()
    {
        return _pesel;
    }

    public String GetWorkerName()
    {
        return _name;
    }

    public String GetWorkerSurname()
    {
        return _surname;
    }

    public float GetWorkerSalary()
    {
        return _salary;
    }

    public String GetWorkerBusinessPhone()
    {
        return _businessPhone;
    }

    public int GetWorkerID()
    {
        return _workerID.getNumVal();
    }

    public WorkerID GetEnumWorkerID()
    {
        return _workerID;
    }

    public String GetWorkerInfo()
    {

        return "";

    }

    public int GetDatabaseID()
    {
        return _id;
    }



}
