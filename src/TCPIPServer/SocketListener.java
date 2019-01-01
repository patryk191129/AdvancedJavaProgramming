package TCPIPServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import DAO.*;


public class SocketListener implements Runnable {

    ServerSocket s;
    private WorkerDAO _workerDAO;
    private int _port = 1111;

    public SocketListener(String port, WorkerDAO workerDAO) throws IOException {
        _workerDAO = workerDAO;
        _port = Integer.parseInt(port);
        System.out.println("Started TCP listenere at: " + _port);
        s = new ServerSocket(_port);
    }


    @Override
    public void run()
    {
        while(true)
        {
            Socket incoming = null;
            try {

                    incoming = s.accept();

                    if(incoming != null)
                    {
                        Runnable r = new ThreadedEchoHandler(incoming, _workerDAO);
                        Thread t = new Thread(r);

                        t.start();
                    }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
