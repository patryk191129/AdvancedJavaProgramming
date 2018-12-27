import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TCPClient implements DataProtocol {

        Socket socket;
        List<String> workerData;

    public boolean StartConnection(String address, int port) {

        try {
            socket = new Socket(address, port);

            return socket != null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean GetAllData()
    {
        try {
            workerData = new ArrayList<>();
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("GetData");
            writer.flush();
            socket.shutdownOutput();

            boolean started = false;


            while(in.hasNextLine())
            {
                String line = in.nextLine();

                if(started)
                {
                    if(line.equals("END"))
                    {
                        System.out.println("Connection closed, all data were sent");
                        break;
                    }

                    workerData.add(line);
                }


                if(line.equals("OK") && !started)
                {
                    System.out.println("\nSocket starting sending data");
                    started = true;
                }

                System.out.println(line +"\n");

                if(line.equals("False"))
                {
                    System.out.println("Socket refused to send data");
                    socket.close();
                }


            }

            if(workerData.size() > 0)
                return true;

            return false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> ReturnData() {
        return workerData;
    }


    public void CloseConnection()
    {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SaveData() {

    }
}
