
package client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SOAPServerService", targetNamespace = "http://ts/", wsdlLocation = "http://localhost:1112/src?wsdl")
public class SOAPServerService
    extends Service
{

    private final static URL SOAPSERVERSERVICE_WSDL_LOCATION;
    private final static WebServiceException SOAPSERVERSERVICE_EXCEPTION;
    private final static QName SOAPSERVERSERVICE_QNAME = new QName("http://ts/", "SOAPServerService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:1112/src?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SOAPSERVERSERVICE_WSDL_LOCATION = url;
        SOAPSERVERSERVICE_EXCEPTION = e;
    }

    public SOAPServerService() {
        super(__getWsdlLocation(), SOAPSERVERSERVICE_QNAME);
    }

    public SOAPServerService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SOAPSERVERSERVICE_QNAME, features);
    }

    public SOAPServerService(URL wsdlLocation) {
        super(wsdlLocation, SOAPSERVERSERVICE_QNAME);
    }

    public SOAPServerService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SOAPSERVERSERVICE_QNAME, features);
    }

    public SOAPServerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SOAPServerService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ts.SOAPServer
     */
    @WebEndpoint(name = "SOAPServerPort")
    public SOAPServer getSOAPServerPort() {
        return super.getPort(new QName("http://ts/", "SOAPServerPort"), SOAPServer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ts.SOAPServer
     */
    @WebEndpoint(name = "SOAPServerPort")
    public SOAPServer getSOAPServerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ts/", "SOAPServerPort"), SOAPServer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SOAPSERVERSERVICE_EXCEPTION!= null) {
            throw SOAPSERVERSERVICE_EXCEPTION;
        }
        return SOAPSERVERSERVICE_WSDL_LOCATION;
    }

}
