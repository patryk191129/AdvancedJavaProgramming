import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TestClass {

    static WorkerDAO workerDAO = new WorkerDAO();


    public static void main(String[] args)
    {

        int option = 0;
        Scanner scanner = new Scanner(System.in);
        Database.GetInstance().Connect();

        do{

            System.out.println("MENU\n1. Lista pracowników\n2. Dodaj pracownika\n3. Usuń pracownika\n4.Kopia zapasowa\n5. Wyjście z programu\nWybór >");
            option = scanner.nextInt();

            switch(option)
            {
                case 1: ShowWorkerList(); break;
                case 2: AddNewWorker(); break;
                case 3: RemoveWorker(); break;
                case 4: MakeBackup(); break;
            }

        }while(option != 5);

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


            //Dla handlowca
            if(opt == 1)
            {
                float commision, commisionLimit;

                System.out.print("Prowizja w procentach:");
                commision = scanner.nextFloat();
                System.out.print("Limit prowizji/miesiac");
                commisionLimit = scanner.nextFloat();


                WorkerTrader workerTrader = new WorkerTrader(0, pesel, name, surname, phoneNumber, salary, commision, commisionLimit);
                workerDAO.AddWorker(workerTrader);

            }

            if(opt == 2)
            {
                float businessAllowance, costLimit;
                String serviceCardNumber;

                System.out.print("Dodatek służbowy: ");
                businessAllowance = scanner.nextFloat();
                System.out.print("Karta służbowa numer: ");
                serviceCardNumber = scanner.nextLine();
                System.out.print("Limit kosztów/miesiąc: ");
                costLimit = scanner.nextFloat();

                WorkerManager workerManager = new WorkerManager(0, pesel, name, surname, phoneNumber, salary, businessAllowance, costLimit, serviceCardNumber);

                workerDAO.AddWorker(workerManager);
            }

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

                workerDAO.DeleteWorker(pesel);

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

    }

}
