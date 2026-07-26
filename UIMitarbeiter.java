import java.time.LocalDateTime;
import java.util.ArrayList;

public class UIMitarbeiter {

    // Der Mitarbeiter meldet sich über ein Passwort als Admin an
    public static void loggin()
    {
        int passwort = 1234;
        int i;

        for ( i = 3; i >= 0; i--) {

            System.out.println("Geben Sie ein Passwort ein:");


            int eingabe = Manager.intscanner();

            if (passwort == eingabe )
            {
                System.out.println("Sie haben sich erflogreich Angemeldet: ");
                hauptmanager();
            } else if (i == 0) {
                System.out.println("Keine eingaben mehr übrig");
                //  System.exit(0);
            }
            else {
                System.out.println("Sie haben noch " + i + " Versuche");
            }

        }
    }


    // Wenn der Mitarbeiter das Passwort eingegeben hat, kann er mehrere
    public  static void hauptmanager()
    {

       System.out.println("-------------------Willkommen im Hauptmanager-------------------------");
       System.out.println("Wilkommen Admin was möchten sie tun: ");
       System.out.println("Drücken Sie die 1: Um Fluggeselschaften hinzufügen oder zu entfernen ");
       System.out.println("Drücken Sie die 2 Um Flugzeuge einer FlugeselschaftFlotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 Um Flughäfen hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 4 Um einen Flug anzulegen ");
        // An alle flüge mit auslastung denken!!!!!!!!!! anzeigen jetzt bei cedric

       int auswhal = Manager.intscanner();

       switch(auswhal) {
           case 1:
               fluggeselschaftenManager();
               break;
           case 2:
               flugzeugderFlotteHinzufügen();
               break;

               case 3:
                   flughafenManager();
                   break;

                   case 4:
                       fluganlegen();
                       break;


       }
        hauptmanager();
    }


public static void fluggeselschaftenManager()
{
    System.out.println("---------Willkommen im fluggesselschaftenManager---------");
    System.out.println("Drücken Sie die 1 um Fluggesselschaften hinzuzufügen  ");
    System.out.println("Drücken Sie die 2 um Fluggesselschaften zu entfernen");
    System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
    System.out.println("---------------------------------------------------------");

    int auswahl  = Manager.intscanner();
    switch(auswahl) {
        case 1:
            fluggeselschaftanlegen();
            break;
        case 2:
            //fluggeselschaftentfernen
            break;
        case 3 :
            hauptmanager();
            break;
        default:
            System.out.print("Falsche eingabe ");
            flottenManager();
    }

}


    public static void flottenManager()
    {
        System.out.println("--------------Willkommen im FlottenManager---------------");
        System.out.println("Drücken Sie die 1 um Flugzeuge der Flotte hinzuzufügen ");
        System.out.println("Drücken Sie die 2 um Flugzeuge der Flotte zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
        System.out.println("---------------------------------------------------------");
        int auswahl  = Manager.intscanner();
        switch(auswahl) {
            case 1:
                flugzeugderFlotteHinzufügen();
                break;
            case 2:
                flugzeugederFlotteEntfernen();
                break;
            case 3 :
                hauptmanager();
                break;
            default:
                System.out.print("Falsche eingabe ");
                flottenManager();
        }


    }


    public static void flughafenManager()
    {
        System.out.println("--------------Willkommen im flughafenManager---------------");
        System.out.println("Drücken Sie die 1 um einen Flughafen hinzuzufügen ");
        System.out.println("Drücken Sie die 2 um einen Flughafen zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
        System.out.println("---------------------------------------------------------");
        int auswahl  = Manager.intscanner();
        switch(auswahl) {
            case 1:
                hinzufügenFlughafen();
                break;
            case 2:
                entferneFlughafen();
                break;
            case 3 :
                hauptmanager();
                break;
            default:
                System.out.print("Falsche eingabe ");
                flughafenManager();
        }


    }




    // Flug geselschaften anlegen und die Flotte
    public static ArrayList<Fluggesellschaft> Fluggeselschaften = new ArrayList<>();

    //Fluggeselschaftanlegen
    public static void fluggeselschaftanlegen()
    {
        System.out.println("--------------Fluggeselschaft anlegen---------------------------");
        System.out.println("Flugeselschaft name: ");
        String name = Manager.Stringscanner();
        System.out.println("Flugeselschaft airlineCode : ");
        String airlinecode = Manager.Stringscanner();

        Fluggesellschaft FluggeselschaftAnlegen  = new Fluggesellschaft(name, airlinecode);

        Fluggeselschaften.add(FluggeselschaftAnlegen);
        System.out.println("Alle bisherrigen Flugeselschaften: ");

        for (int i = 0; i < Fluggeselschaften.size(); i++) {
        System.out.println("Fluggeselschaft: " +"ID "+ i  + " " + Fluggeselschaften.get(i).toString());

        }


        // Möchten sie Flugzeuge der Flotte hinzufügen 1, entfernen 2, zurück 3,

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Drücken Sie die 1 um Flugzeuge der Flotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 2 um eine weitere Fluggeselschafft hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Haupt Manager zu gelangen");
        System.out.println("-------------------------------------------------------------------------");
        int antwort = Manager.intscanner();

        switch(antwort) {
            case 1: flottenManager();
                break;
                case 2: fluggeselschaftenManager();
                break;
                case 3: hauptmanager();
            default:
            System.out.print("Falsche eingabe ");
                hauptmanager();
        }

    }

    // Flugeselschaftentfernen





    //FlugzeugderFlotteHinzufügen --> Marcels Methode aufrufen

    public static void flugzeugderFlotteHinzufügen()
    {
        int laufvariable = -1;
        System.out.println("----------------------Flugzeuge der Flotte Hinzufügen------------------------------");
        // Liste der Bestehenden Fluggeselschaften um diese zu einer Flotte hinzuzufügen
        for (int i = 0; i < Fluggeselschaften.size(); i++) {
            System.out.println("Fluggeselschaft: " +  "ID: " + i  + " " + Fluggeselschaften.get(i).toString());
            laufvariable = i;
        }
        if (laufvariable == -1)
        {
            System.out.println("Es gibt keine Fluggeselschafften");
            hauptmanager();
        }


        // Auswahl der Fluggeselschafften
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Bitte Wählen sie über die ID ihre Fluggeselschaft aus welcher sie Flugzeuge hinzufügen möchten ");
        int antwort = Manager.intscanner();


            if ( antwort >= 0 &&  antwort < Fluggeselschaften.size()) {
                System.out.println("Ihre Auswahl: " +  Fluggeselschaften.get(antwort).toString());
                System.out.println("--------------------------------------------------------------------");
                System.out.println("Bitte geben Sie den Code des Flugzeuges an ");
                String code = Manager.Stringscanner();
                System.out.println("Bitte geben Sie das Modell des Flugzeuges an ");
                String modell = Manager.Stringscanner();
                System.out.println("Bitte geben Sie die Anzahl der Reihen des Flugzeuges an ");
                int reihen = Manager.intscanner();
                System.out.println("Bitte geben Sie die Anzahl der Sitze pro Reihe des Flugzeuges an ");
                int sitze = Manager.intscanner();
                System.out.println("Bitte geben Sie die Anzahl der Business Reihen des Flugzeuges an ");
                int business = Manager.intscanner();

                // Flugzeug erstellen
                Flugzeug Flugzeug = new Flugzeug(code, modell, reihen, sitze, business); // Das dann cedricsa methode übergeben
                Fluggeselschaften.get(antwort).fuegeFlugzeugHinzu(Flugzeug);


                System.out.println("erflogreich ");
                 flottenManager();
            }
            // Bei Falscher auswahl
            else
            {
                System.out.println("--------------------------------------------------------------------");
                System.out.println("Auswahl nicht verfügbar zurück zum FlottenManager ");
                flottenManager();
            }


    }

    public static void flugzeugederFlotteEntfernen()
    {
        int laufvariable = -1;
        System.out.println("----------------------Flugzeuge der Flotte Entfernen------------------------------");
        // Liste der Bestehenden Fluggeselschaften um diese zu einer Flotte hinzuzufügen
        for (int i = 0; i < Fluggeselschaften.size(); i++) {
            System.out.println("Fluggeselschaft: " +  "ID: " + i  + " " + Fluggeselschaften.get(i).toString());
            laufvariable = i;
        }
        if (laufvariable == -1)
        {
            System.out.println("Es gibt keine Fluggeselschafften");
            hauptmanager();
        }


        // Auswahl der Fluggeselschafften
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Bitte Wählen sie über die ID ihre Fluggeselschaft aus welcher sie Flugzeuge enternen möchten ");
        int antwort = Manager.intscanner();


        if ( antwort >= 0 &&  antwort < Fluggeselschaften.size()) {

            System.out.println("Ihre Auswahl: " +  Fluggeselschaften.get(antwort).toString());
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Bitte geben Sie den Code des Flugzeuges an, welches sie entfernen möchten ");
            String code = Manager.Stringscanner();
         // Cedrics entfern methode und den code übergeben
        }
        // Bei Falscher auswahl
        else
        {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Auswahl nicht verfügbar zurück zum FlottenManager ");
            flottenManager();
        }

    }

    public static void hinzufügenFlughafen()
    {
        System.out.println("----------------------Flughafen Hinzufügen------------------------------");
        System.out.println("Geben sie den Namen des Flugahfens ein");
        String name = Manager.Stringscanner();
        System.out.println("Geben sie den iatacode des Flugahfens ein");
        String iatacode  = Manager.Stringscanner();
        System.out.println("Geben sie die Stadt des Flugahfens ein");
        String stadt = Manager.Stringscanner();
        System.out.println("Geben sie das Land des Flugahfens ein");
        String land = Manager.Stringscanner();

        //Methode zum hinzufügen
      //  System.out.println("flughafen: " + // Methode aufrufen  + " erfolgreich hinzugefügt");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Zurück zum Flughafen Manager ");
        flughafenManager();
    }

    public static void entferneFlughafen()
    {
        System.out.println("----------------------Flughafen Entfernen-------------------------------");
        System.out.println("Welchen Flughafen möchten Sie enternen (iataCode) ");
        String Name = Manager.Stringscanner();

        // Dann über get den flughafen ziehen mit dem Namen und dann in die entfern methode
        //Methode zum entfernen
     //   System.out.println("flughafen: " + flughafen + " erfolgreich entfernt");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Zurück zum Flughafen Manager ");
        flughafenManager();
    }




    // Flüge anlegen
    public static void fluganlegen()
    {
        System.out.println("---------------------------Willkommen beim Flug anlegen -----------------------------");

        // Abfrage der Flugnummer --> Macht cedric automatisch

        // Abfrage des Basispreises
        System.out.println("Bitte geben Sie den Basispreis ihres Flugs an ");
        double basispreis = Manager.doublescanner();

        //Anzeige der gesamten Fluggeselschaften --> Auswahl welche will man übergeben
        System.out.println("Auswahl der Fluggeselschaft welche den Flug durchführen soll");

        // Auswahl über die to string methode der Fluggeselschafft / welches flugzeug
        //Anzeige der Flugzeuge --> Auswahl welche man übergibt


        // Flughafen über den code auswählen / liste ein mal anzeigen lassen

        System.out.println("Wählen Sie den Start und Zielflughafen aus");
        // Liste aller Flugäfen
        System.out.println("Wählen Sie den Startflughafen über den iataCode aus ");
        String startflughafen  = Manager.Stringscanner();
        // get methode um den Flughafen zu bekommen
        System.out.println("Wählen Sie den Zielflughafen über den iataCode aus ");
        String zielflughafen  = Manager.Stringscanner();
        // get methode um den Flughafen zu bekommen

        System.out.println("--------------------------------------------------------");


        System.out.println("---------------------------Bitte geben Sie die Abflugszeiten an-----------------------------");
        LocalDateTime abflug = anabflug("des Abfluges");
        System.out.println("---------------------------Bitte geben Sie die Ankuftszeiten an-----------------------------");
        LocalDateTime ankunft =  anabflug("der Ankunft");




// Flug anlegen
//Flug Flug = new Flug("121221", Fluggeselschaften.get(0) , "flugzeug", startflughafen, zielflughafen, abflug, ankunft, basispreis );

        // ist ein Flug erstellt nach rückflug fragen

        // neuer basis preis und turn arround zeit
    }


    public static LocalDateTime  anabflug(String text)
    {
        System.out.println("Bitte geben Sie das Jahr " + text + " ein ");
        int jahr  = Manager.intscanner();
        System.out.println("Bitte geben Sie den Monat " + text + " ein ");
        int monat = Manager.intscanner();
        System.out.println("Bitte geben Sie den Tag " + text + " ein ");
        int tag = Manager.intscanner();
        System.out.println("Bitte geben Sie die Stunde " + text + " ein ");
        int stunde = Manager.intscanner();
        System.out.println("Bitte geben Sie die Minute " + text + " ein ");
        int minute = Manager.intscanner();

        // Cedrics Methode zurückgeben
        return LocalDateTime.now().minusMinutes(minute);

    }


    public static void ruckflug()
    {


    }


}
