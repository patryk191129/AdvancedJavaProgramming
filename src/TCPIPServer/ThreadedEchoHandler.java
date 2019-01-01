package TCPIPServer;

import Auth.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import DAO.*;

public class ThreadedEchoHandler implements Runnable {

    private Socket incoming;
    private WorkerDAO _workerDAO;


    public ThreadedEchoHandler(Socket i, WorkerDAO workerDAO)
    {
        _workerDAO = workerDAO;
        incoming = i;
    }


    public void run()
    {
        try {
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();

            Scanner in = new Scanner(inStream);
            PrintWriter out = new PrintWriter(outStream, true);

            boolean done = false;




            while (!done && in.hasNextLine()) {
                String line = in.nextLine();

                if (line.trim().equals("GetData"))
                {

                    Registry rgsty = LocateRegistry.getRegistry(3333);
                    Auth remote = (Auth) rgsty.lookup("Auth");
                    boolean isAuth = remote.CheckAuthentication(in.nextLine());


                    System.out.println("User requested get data");

                    if(isAuth)
                        out.write("OK\n");
                    else
                    {
                        out.write("False\n");
                        out.flush();
                        out.close();
                        return;
                    }

                    out.flush();
                    done = true;
                }

            }

            //Wysylanie danych do klienta
            List<String> workerList = _workerDAO.GetWorkersAsStringList();

            StringBuilder sb = new StringBuilder();

            for(int i=0;i<workerList.size();i++)
                sb.append(workerList.get(i)+"\n");


            out.write(sb.toString());
            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } finally {
            try {
                incoming.close();
            }catch(IOException ie)
            {

            }

        }

    }


    }

