import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DecimalFormat;


@XmlRootElement(name = "WorkerTrader")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkerTrader extends Worker {

    private float _commision;
    private float _commisionLimit;


    public WorkerTrader()
    {

    }


    public WorkerTrader(int id,String pesel, String name, String surname, String businessPhone, float salary, float commision, float commsionLimit)
    {
        this._id = id;
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

    public String GetWorkerInfo()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Identyfikator PESEL: ");
        stringBuilder.append(this._pesel);
        stringBuilder.append("\nImię: ");
        stringBuilder.append(this._name);
        stringBuilder.append("\nNazwisko: ");
        stringBuilder.append(this._surname);
        stringBuilder.append("\nStanowisko: Handlowiec");
        stringBuilder.append("\nWynagrodzenie: ");
        stringBuilder.append(this._salary);
        stringBuilder.append("\nTelefon służbowy: ");
        stringBuilder.append(this._businessPhone);
        stringBuilder.append("\nProwizja(%): ");
        stringBuilder.append(this._commision);
        stringBuilder.append("\nLimit prowizji/miesiac: ");
        stringBuilder.append(this._commisionLimit);
        stringBuilder.append("\n");


        return stringBuilder.toString();
    }

}
