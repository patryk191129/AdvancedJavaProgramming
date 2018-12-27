import java.rmi.*;


public interface Auth extends Remote
{
    String Authenticate(String username, String password) throws RemoteException;
    boolean CheckAuthentication(String token) throws RemoteException;
}
