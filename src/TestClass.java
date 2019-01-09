import Auth.Auth;
import ClientProtocols.DataProtocol;
import ClientProtocols.SOAPClient;
import ClientProtocols.TCPClient;
import DAO.Worker;
import DAO.WorkerDAO;
import DAO.WorkerManager;
import DAO.WorkerTrader;
import Database.Database;
import TCPIPServer.SocketListener;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.rmi.*;
import java.net.*;

public class TestClass {

    public static WorkerDAO workerDAO = new WorkerDAO();


    public static void main(String[] args) throws MalformedURLException {
        Database.SetDBname(args[0]);

        try {
            Runnable r = new SocketListener(args[1], workerDAO);
            Thread t = new Thread(r);
            t.start();
        }
        catch(IOException ie)
        {
            System.out.println("Nie mozna utworzyc socketu");
        }


        int soapPort = Integer.parseInt(args[1])+1;

        ts.SOAPService soapService = new ts.SOAPService(soapPort, workerDAO);




        int option = 0;
        Scanner scanner = new Scanner(System.in);
        Database.GetInstance().Connect();


        do{

            System.out.println("MENU\n1. Lista pracowników\n2. Dodaj pracownika\n3. Usuń pracownika\n4.Kopia zapasowa\n5. Pobierz dane z sieci.\n6. Wyjście z programu\nWybór >");
            option = scanner.nextInt();

            switch(option)
            {
                case 1: ShowWorkerList(); break;
                case 2: AddNewWorker(); break;
                case 3: RemoveWorker(); break;
                case 4: MakeBackup(); break;
                case 5: DownloadData(); break;
            }

        }while(option < 6);

    }

    private static void DownloadData()
    {

        try {
            boolean xml = false;
            Scanner scanner = new Scanner(System.in);

            Console console = System.console();
            System.out.print("Podaj użytkownika: ");
            String username = scanner.nextLine();
            //System.out.println("Podaj hasło: ");
            char[] pass = console.readPassword("Podaj haslo: ");

            String password = String.valueOf(pass);

            System.out.print("Protokół [T]cp/IP czy [S]OAP?: ");
            String protocol = scanner.nextLine();


            DataProtocol dp = null;

            if(protocol.equals("T") || protocol.equals("t"))
                dp = new TCPClient();

            if(protocol.equals("S") || protocol.equals("s"))
            {
                dp = new SOAPClient();
                xml = true;
            }


            if(dp == null)
            {
                System.out.println("Błąd - niepoprawna opcja;");
                return;
            }


            System.out.print("Adres:\t");
            String address = scanner.nextLine();

            System.out.print("Port:\t");
            String port = scanner.nextLine();

            int portNumber = Integer.parseInt(port);


            String token = "";

            try{


                System.out.print("Autentykacja użytkownika...");


                Registry rgsty = LocateRegistry.getRegistry(3333);
                Auth remote = (Auth) rgsty.lookup("Auth");
                token = remote.Authenticate(username, password);

                if(token.equals("false"))
                {
                    System.out.println("Niepowodzenie");
                    return;
                }
                else
                    System.out.println("Sukces");

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

            System.out.println("============================");
            System.out.print("Ustanawianie polaczenia... ");

            if(!dp.StartConnection(address, portNumber, token))
            {
                System.out.println("Niepowodzenie");
                return;
            }
            else
                System.out.println("Sukces");

            System.out.print("Pobieranie... ");

            if(dp.GetAllData())
            {
                System.out.println("Sukces");
                System.out.println("============================\n");
                System.out.println("Czy zapisac pobrane dane? [T]/[N]: ");
                String opt = scanner.nextLine();

                if(opt.equals("t") || opt.equals("T"))
                {
                    if(xml)
                        DeserializeFromJaxb(dp.ReturnData());
                    else
                        workerDAO.BackupDatabaseFromProtocol(dp.ReturnData());
                }

            }
            else
            {
                System.out.println("Niepowodzenie - brak danych do pobrania");
            }





        }
        catch(java.util.InputMismatchException e)
        {
            System.out.println("Niepoprawne dane wejsciowe");
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    public static void AddNewWorker()
    {
        try {

            Scanner scanner = new Scanner(System.in);

            System.out.print("1 - handlowiec, 2 - dyrektor");

            int opt = scanner.nextInt();

            String pesel, name, surname, phoneNumber;
            float salary;
            scanner.nextLine();
            System.out.print("Identyfikator pesel: ");
            pesel = scanner.nextLine();


            System.out.print("Imię: ");
            name = scanner.nextLine();
            System.out.print("Nazwisko: ");
            surname = scanner.nextLine();
            System.out.print("Telefon służbowy: ");
            phoneNumber = scanner.nextLine();
            System.out.print("Wynagrodzenie: ");
            salary = scanner.nextFloat();


            boolean created = false;


            //Dla handlowca
            if(opt == 1)
            {
                float commision, commisionLimit;

                System.out.print("Prowizja w procentach:");
                commision = scanner.nextFloat();
                System.out.print("Limit prowizji/miesiac");
                commisionLimit = scanner.nextFloat();


                created = workerDAO.AddWorker(new WorkerTrader(0, pesel, name, surname, phoneNumber, salary, commision, commisionLimit));
            }

            if(opt == 2)
            {
                float businessAllowance, costLimit;
                String serviceCardNumber;

                System.out.print("Dodatek służbowy: ");
                businessAllowance = scanner.nextFloat();
                scanner.nextLine();
                System.out.print("Karta służbowa numer: ");
                serviceCardNumber = scanner.nextLine();
                System.out.print("Limit kosztów/miesiąc: ");
                costLimit = scanner.nextFloat();

                created = workerDAO.AddWorker(new WorkerManager(0, pesel, name, surname, phoneNumber, salary, businessAllowance, costLimit, serviceCardNumber));
            }

            if(!created)
                System.out.println("Nie utworzono pracownika (niepoprawny numer pesel badz został już zajęty");
            else
                System.out.println("Poprawnie utworzono nowego pracownika");


        }
        catch(SQLException exception)
        {
            System.out.println(exception);
        }
        catch(Exception e)
        {
            System.out.println("Niepoprawnie wprowadzony parametr.");
        }
    }



    public void Test()
    {
        System.out.println("Test");
    }

    private static void ShowWorkerList()
    {
        try {
            List<Worker> workerList = workerDAO.GetWorkers();

            Scanner scanner = new Scanner(System.in);

            for(int i=0;i<((List) workerList).size();i++)
            {
                System.out.println(workerList.get(i).GetWorkerInfo());
                scanner.nextLine();

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void RemoveWorker()
    {
        Scanner scanner = new Scanner(System.in);


            System.out.print("Podaj identyfikator PESEL: ");
            String pesel = scanner.nextLine();

        try {
            Worker worker = workerDAO.GetWorker(pesel);

            if(worker != null)
            {
                System.out.println("Informacje o pracowniku");
                System.out.print(worker.GetWorkerInfo());

                System.out.println("-----------------------------");
                System.out.print("Czy chcesz usunąć pracownika? [T]ak/[N]ie");
                String opt = scanner.next();

                if(opt.equals("T"))
                {
                    workerDAO.DeleteWorker(pesel);
                }

            }
            else
            {
                System.out.println("Pracownik nie istnieje");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void MakeBackup()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[Z]achowaj/[O]dtwórz: \t");

        String opt = scanner.next();

        if(opt.equals("Z"))
        {
            System.out.print("Nazwa pliku: \t");
            String filename = scanner.next();
            try {
                workerDAO.ArchiveDatabaseToFile(filename);
            }
            catch(SQLException exception)
            {

            }
        }

        if(opt.equals("O"))
        {
            System.out.print("Nazwa pliku: \t");
            String filename = scanner.next();

            try{
                workerDAO.BackupDatabaseFromFile(filename);
            }
            catch(SQLException sqlException)
            {

            }
            catch(IOException exception)
            {
                System.out.println("Błąd: niepoprawny plik");
            }


        }
    }


    private static void DeserializeFromJaxb(List<String> dataList) throws SAXException, JAXBException {

        String stream = dataList.get(0);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("schema.xsd"));

        JAXBContext jc = JAXBContext.newInstance(WorkerDAO.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        StringReader reader = new StringReader(stream);
        workerDAO = (WorkerDAO) unmarshaller.unmarshal(reader);
        try {
            workerDAO.DeserializeToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
