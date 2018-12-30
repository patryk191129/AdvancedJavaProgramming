package ts;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(endpointInterface = "ts.SOAPServer")
public class SOAPServer implements SOAPInterface{

    @Override
    public String getWorkers(
            String arg0){

        return "SSS";

    }


}
