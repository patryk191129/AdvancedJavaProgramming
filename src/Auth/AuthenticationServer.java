package Auth;


import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.*;
import java.util.Base64.Encoder;

public class AuthenticationServer extends UnicastRemoteObject implements Auth {


    private class Token{
        public String token;
        public String owner;
        public double expirationTime;
    }

    List<Token> tokens;




    public AuthenticationServer() throws RemoteException
    {
        tokens = new ArrayList<>();
    }


    public String Authenticate(String username, String password) throws RemoteException
    {
        SecureRandom random = new SecureRandom();


        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://82.145.72.13:389");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);

        //Jezeli jest polaczenie z ZUTEM xD
        /*
        DirContext ctx;

        try {
            ctx = new InitialDirContext(env);
            if(ctx == null)
            {
                System.out.println("NULL");
                return "false";
            }

            ctx.close();


        } catch (NamingException e) {

            System.out.println("Invalid authorization");
            return "false";
        }
        */


        RemoveExistingToken(username);
        byte bytes[] = new byte[128];
        random.nextBytes(bytes);
        Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        AddToken(username, token);


        return token;
    }



    public void AddToken(String username, String token)
    {
        Token currentToken = new Token();
        currentToken.token = token;
        currentToken.owner = username;

        //2 minuty waznosci
        currentToken.expirationTime = System.currentTimeMillis() + (1000*120);
        System.out.println("Generated new token: " + token);
        tokens.add(currentToken);
    }


    public void RemoveExistingToken(String username)
    {
        for(int i=tokens.size()-1;i>=0;i--)
        {
            if(tokens.get(i).owner.equals(username)) {
                System.out.println("Deleting token: " + tokens.get(i));
                tokens.remove(i);
            }
        }
    }

    public boolean CheckAuthentication(String token) {

        for(int i=0;i<tokens.size();i++)
            if(tokens.get(i).token.equals(token))
            {
                System.out.println("Used token to authenticate: " + token);
                tokens.remove(i);
                return true;
            }

        System.out.println("Wrong authentication");
        return false;
    }

}
