import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DecimalFormat;


@XmlRootElement(name = "WorkerManager")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkerManager extends Worker {


    private float _businessAllowance;
    private float _costLimit;
    private String _serviceCardNumber;


    public WorkerManager()
    {

    }

    public WorkerManager(int id,String pesel, String name, String surname, String businessPhone, float salary, float businessAllowance, float costLimit, String serviceCardNumber)
    {
        this._id = id;
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


    public String GetWorkerInfo()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Identyfikator PESEL: ");
        stringBuilder.append(this._pesel);
        stringBuilder.append("\nImię: ");
        stringBuilder.append(this._name);
        stringBuilder.append("\nNazwisko: ");
        stringBuilder.append(this._surname);
        stringBuilder.append("\nStanowisko: Dyrektor");
        stringBuilder.append("\nWynagrodzenie: ");
        stringBuilder.append(this._salary);
        stringBuilder.append("\nTelefon służbowy: ");
        stringBuilder.append(this._businessPhone);
        stringBuilder.append("\nDodatek służbowy: ");
        stringBuilder.append(this._businessAllowance);
        stringBuilder.append("\nKarta służbowa numer: ");
        stringBuilder.append(this._serviceCardNumber);
        stringBuilder.append("\nLimit kosztów/miesiąc: ");
        stringBuilder.append(this._costLimit);
        stringBuilder.append("\n");


        return stringBuilder.toString();
    }


}
