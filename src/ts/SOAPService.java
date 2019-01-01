package ts;

import DAO.WorkerDAO;

import javax.xml.ws.Endpoint;

public class SOAPService{

    int port;
    private Endpoint endpoint;
    private WorkerDAO _workerDAO;


    public static void main(String []args)
    {

    }

    public SOAPService(int port, WorkerDAO workerDAO)
    {
        _workerDAO = workerDAO;
        this.port = port;
        create_endpoint();
        configure_endpoint();
        publish();
    }

    private void configure_endpoint()
    {
        endpoint.setExecutor(new MyThreadPool());

    }

    private void create_endpoint()
    {
        endpoint = Endpoint.create(new SOAPServer(_workerDAO));
    }

    private void publish()
    {
        String url = "http://localhost:" + port + "/src";
        endpoint.publish(url);
        System.out.println("Started SOAP server at: "  +port);

    }

}
