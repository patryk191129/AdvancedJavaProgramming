import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TestClass {

    public static WorkerDAO workerDAO = new WorkerDAO();


    public static void main(String[] args)
    {
        Database.SetDBname(args[0]);


        try {
            Runnable r = new SocketListener(args[1]);
            Thread t = new Thread(r);
            t.start();
        }
        catch(IOException ie)
        {
            System.out.println("Nie mozna utworzyc socketu");
        }


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
            Scanner scanner = new Scanner(System.in);

            System.out.print("Adres:\t");
            String address = scanner.nextLine();

            System.out.print("Port:\t");
            String port = scanner.nextLine();

            int portNumber = Integer.parseInt(port);

            DataProtocol dp = new TCPClient();

            System.out.println("============================");
            System.out.print("Ustanawianie polaczenia... ");
            if(dp.StartConnection(address, portNumber))


            if(dp.GetAllData())
            {
                System.out.print("Sukces");
                System.out.println("============================\n");
                System.out.println("Czy zapisac pobrane dane? [T]/[N]: ");
                String opt = scanner.nextLine();

                if(opt.equals("t") || opt.equals("T"))
                {
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

}
