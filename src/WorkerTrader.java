import java.text.DecimalFormat;

public class WorkerTrader extends Worker {

    private float _commision;
    private float _commisionLimit;




    public WorkerTrader(String pesel, String name, String surname, String businessPhone, float salary, float commision, float commsionLimit)
    {
        this._pesel = pesel;
        this._workerID = Worker.WorkerID.valueOf("TRADER");
        this._name = name;
        this._surname = surname;
        this._businessPhone = businessPhone;
        this._salary = salary;
        this._commision = commision;
        this._commisionLimit = commsionLimit;
    }


    public float GetCommision()
    {
        return _commision;
    }


    public float GetCommisionLimit()
    {
        return _commisionLimit;
    }


}
