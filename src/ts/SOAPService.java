package ts;


import javax.xml.ws.Endpoint;

public class SOAPService{

    int port;
    private Endpoint endpoint;

    public static void main(String []args)
    {

    }

    public SOAPService(int port)
    {
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
        endpoint = Endpoint.create(new SOAPServer());
    }

    private void publish()
    {
        String url = "http://localhost:" + port + "/src";
        endpoint.publish(url);
        System.out.println("Started SOAP server at: "  +port);

    }

}
