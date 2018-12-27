import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ThreadedEchoHandler implements Runnable {

    private Socket incoming;

    public ThreadedEchoHandler(Socket i)
    {
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
                    System.out.println("User requested get data");
                    out.write("OK\n");
                    out.flush();
                    done = true;
                }

            }

            //Wysylanie danych do klienta
            List<String> workerList = TestClass.workerDAO.GetWorkersAsStringList();

            StringBuilder sb = new StringBuilder();

            for(int i=0;i<workerList.size();i++)
                sb.append(workerList.get(i)+"\n");


            out.write(sb.toString());
            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                incoming.close();
            }catch(IOException ie)
            {

            }

        }

    }


    }

