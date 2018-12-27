import javax.naming.*;
import java.rmi.*;

public class Main {

    public static void main(String[] args) throws NamingException, RemoteException {

        AuthenticationServer authenticationServer = new AuthenticationServer();
        Context namingContext = new InitialContext();
        namingContext.bind("rmi:AuthenticationServer", authenticationServer);


    }
}
