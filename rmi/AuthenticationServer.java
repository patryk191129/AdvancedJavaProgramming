import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServer extends UnicastRemoteObject implements Auth {

    private class Users{
        public String username;
        public String password;
    }

    Users []users;

    public AuthenticationServer() throws RemoteException
    {
        users = new Users[2];

        users[0].username = "root";
        users[0].password = "root";

        users[1].username = "root2";
        users[1].password = "root2";

    }


    public String Authenticate(String username, String password) throws RemoteException {

        return "Auth";
    }


    public boolean CheckAuthentication(String token) {

        return true;
    }

}
