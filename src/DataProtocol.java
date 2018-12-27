import java.util.List;

public interface DataProtocol {

    public boolean StartConnection(String address, int port, String token);
    public boolean GetAllData();
    public List<String> ReturnData();
    public void CloseConnection();
    public void SaveData();

}
