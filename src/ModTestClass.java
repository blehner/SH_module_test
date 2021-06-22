import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.List;

public class ModTestClass {

    private void logFailure(String homeID, String subscriber) {
        //hiba logolása fileba
        try {
            File logFile = new File("error_log_file.txt");
            if (logFile.createNewFile()) {
                System.out.println("Elkészítettem a hibák logolására való fájlt: " +
                        logFile.getName() + " és ide írok...");
            } else {
                System.out.println("A már létező log file-ba írok...");
            }
        } catch (IOException e) {
            System.out.println("Hiba történt a log file létrehozása során...");
            e.printStackTrace();
        }
        try {
            FileWriter logWriter = new FileWriter("error_log_file.txt", true);
            logWriter.append("Hibát rögzítettem a " + homeID +
                    " azonosító számú házban, amelynek tulajdonosa a " + subscriber + "." + "\n");
            logWriter.close();
        } catch (Exception e) {
            System.out.println("Hiba történt a fájl kiírása során:");
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------------->

    public void controlCheckService(Subscriber sub) throws Exception {
        int statusFromServer;
        for (int i = 0; i < sub.elvartHomerseklet.length; i++) {
            System.out.println("--------------------" + i + ". szamu lakas tesztelese" + "---------------------------");
            System.out.println("Kívánt hőmérséklet: " + sub.elvartHomerseklet[i] +
                    " - Lekérdezett hőmérséklet: " + sub.jelenlegiHomerseklet[i]);

            double elvartHomerseklet = sub.elvartHomerseklet[i];
            double aktualisHomerseklet = sub.jelenlegiHomerseklet[i];
            double elteres = (elvartHomerseklet - aktualisHomerseklet);
            if (elteres < 0)
                elteres = (elteres * -1);
            System.out.println("Az elteres a homersekleben: " + elteres);

            if (sub.jelenlegiHomerseklet[i] < sub.elvartHomerseklet[i])    //A kértnél alacsonyabb a
            // hőmérésklet, fűtés szükséges!

            {
                //Ha a kivant legalább 20%-al eltér a jelenlegitől, akkor logolni kell a hibat egy fileba

                System.out.println("Fűtés szükséges!");
                //statusFromServer = homeDriver.sendCommand(subscribersList.get(i), true, false);
                System.out.println(sub.homeID[i] + " " + sub.userID[i] + " Futes bekapcsolasa -" + " Hutes kikapcsolva");
                statusFromServer = sub.serverMessage[i];

                if (elteres / (elvartHomerseklet / 100) > 20) {
                    System.out.println("Feltételezhetően hibát észleletem, logolom!");
                    logFailure(sub.homeID[i], sub.userID[i]);
                    statusFromServer = sub.serverMessage[i];
                }
            } else if (sub.jelenlegiHomerseklet[i] > sub.elvartHomerseklet[i]) //Magasabb a hőmérséklet a kértnél, hűtés szükséges!
            {
                System.out.println("Hűtés szükséges!");

                System.out.println(sub.homeID[i] + " " + sub.userID[i] + " Futes kikapcsolasa -" + " Hutes bekapcsolasa");
                statusFromServer = sub.serverMessage[i];

                if (elteres / (elvartHomerseklet / 100) > 20) {
                    System.out.println("Feltételezhetően hibát észleletem, logolom!");
                    logFailure(sub.homeID[i], sub.userID[i]);
                    statusFromServer = sub.serverMessage[i];
                }
            } else
            {
                System.out.println(sub.homeID[i] + " " + sub.userID[i] + " Futes kikapcsolasa -" + " Hutes ki");
                statusFromServer = sub.serverMessage[i];
            }
            //System.out.println(response.toString());
            if (statusFromServer == 100) { //int 100
                System.out.println("A szerver visszaigazolta a sikeres beállítást!");
            }
            if (statusFromServer == 101 || statusFromServer == 102) {
                System.out.println("A szerver hibás parancs választ adott!"); //int 101 vagy 102
            }
        }
    }
}
