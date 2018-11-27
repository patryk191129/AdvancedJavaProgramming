import java.text.DecimalFormat;

public class WorkerManager extends Worker {


    private float _businessAllowance;
    private float _costLimit;
    private String _serviceCardNumber;


    public WorkerManager(String pesel, String name, String surname, String businessPhone, float salary, float businessAllowance, float costLimit, String serviceCardNumber)
    {
        this._pesel = pesel;
        this._workerID = Worker.WorkerID.valueOf("MANAGER");
        this._name = name;
        this._surname = surname;
        this._businessPhone = businessPhone;
        this._salary = salary;
        this._businessAllowance = businessAllowance;
        this._costLimit = costLimit;
        this._serviceCardNumber = serviceCardNumber;
    }

    public float GetWorkerBusinessAllowance()
    {
        return _businessAllowance;
    }

    public float GetWorkerCostLimit()
    {
        return _costLimit;
    }

    public String GetServiceCardNumber()
    {
        return _serviceCardNumber;
    }

}
