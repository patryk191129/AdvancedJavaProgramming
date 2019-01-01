package Auth;

import javax.naming.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws NamingException, RemoteException, MalformedURLException {


        Registry rgsty = LocateRegistry.createRegistry(3333);

        Registry gks = LocateRegistry.getRegistry(3333);

        AuthenticationServer authenticationServer = new AuthenticationServer();
        rgsty.rebind("Auth", authenticationServer);

        System.out.println("Server ready");
    }
}
