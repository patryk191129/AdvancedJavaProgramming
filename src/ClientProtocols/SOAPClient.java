package ClientProtocols;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SOAPClient implements DataProtocol {

    client.SOAPServer server;
    String token;
    String output;

    @Override
    public boolean StartConnection(String address, int port, String token) {


        URL url = null;
        try {
            url = new URL("http://"+address+":"+Integer.toString(port)+"/src?wsdl");
            client.SOAPServerService soapServerService = new client.SOAPServerService(url);
            server = soapServerService.getSOAPServerPort();
            this.token = token;

            if(server != null)
            {
                output = server.getWorkers(token);

                if(output.equals("false"))
                    return false;


                return true;

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }




        return false;
    }

    @Override
    public boolean GetAllData() {

        if(output.equals("false"))
            return false;


        return true;

    }

    @Override
    public List<String> ReturnData() {
        List<String> data = new ArrayList<>();
        data.add(output);

        return data;
    }

    @Override
    public void CloseConnection() {

    }

    @Override
    public void SaveData() {

    }
}
