import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketListener implements Runnable {

    ServerSocket s;
    private int _port = 1111;

    public SocketListener(String port) throws IOException {
        _port = Integer.parseInt(port);
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
                        Runnable r = new ThreadedEchoHandler(incoming);
                        Thread t = new Thread(r);
                        t.start();
                    }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
