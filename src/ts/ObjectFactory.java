
package ts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetWorkers_QNAME = new QName("http://ts/", "getWorkers");
    private final static QName _GetWorkersResponse_QNAME = new QName("http://ts/", "getWorkersResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetWorkersResponse }
     * 
     */
    public GetWorkersResponse createGetWorkersResponse() {
        return new GetWorkersResponse();
    }

    /**
     * Create an instance of {@link GetWorkers }
     * 
     */
    public GetWorkers createGetWorkers() {
        return new GetWorkers();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWorkers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ts/", name = "getWorkers")
    public JAXBElement<GetWorkers> createGetWorkers(GetWorkers value) {
        return new JAXBElement<GetWorkers>(_GetWorkers_QNAME, GetWorkers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWorkersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ts/", name = "getWorkersResponse")
    public JAXBElement<GetWorkersResponse> createGetWorkersResponse(GetWorkersResponse value) {
        return new JAXBElement<GetWorkersResponse>(_GetWorkersResponse_QNAME, GetWorkersResponse.class, null, value);
    }

}
