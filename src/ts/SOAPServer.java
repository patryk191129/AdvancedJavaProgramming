package ts;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import Auth.*;
import DAO.WorkerDAO;


@WebService(endpointInterface = "ts.SOAPServer")
public class SOAPServer implements SOAPInterface{

    private WorkerDAO _workerDAO;


    public SOAPServer(WorkerDAO workerDAO) {
        _workerDAO = workerDAO;
    }

    private String MarshalToXML()
    {
        try {
            _workerDAO.PrepareToSerialize();

            JAXBContext context = JAXBContext.newInstance(WorkerDAO.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter sw = new StringWriter();
            m.marshal(_workerDAO, sw);

            return sw.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "false";
        } catch (PropertyException e) {
            e.printStackTrace();
            return "false";
        } catch (JAXBException e) {
            e.printStackTrace();
            return "false";
        }

    }


    @Override
    public String getWorkers(
            String token){

        Object remote = null;
        Auth auth = null;
        try {
            Registry rgsty = LocateRegistry.getRegistry(3333);
            auth =  (Auth) rgsty.lookup("Auth");


            if(auth != null)
            {
                boolean authenticate = auth.CheckAuthentication(token);

                if(authenticate)
                {

                    return MarshalToXML();

                }


            }


        } catch (NotBoundException e) {
            e.printStackTrace();
            return "false";
        }
            catch (RemoteException e) {
            e.printStackTrace();
            return "false";
        }


        return "false";

    }


}
