import java.time.LocalDate;
import java.time.LocalDateTime;

public class UIMitarbeiter {


    private final DatenHandler datenHandler; 
    private final Anwendungsdaten anwendungsdaten;
    private final Buchungssystem bs; 
    private final Verwaltungssystem vs;


    public UIMitarbeiter(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten dürfen nicht null sein.");
        }


        this.datenHandler = datenHandler; 
        this.anwendungsdaten = anwendungsdaten;
        this.bs = anwendungsdaten.getBuchungssystem();
        this.vs = anwendungsdaten.getVerwaltungssystem();
    }

    // Der Mitarbeiter meldet sich über ein Passwort als Admin an
    public void loggin() {
        int passwort = 1234;
        int i;

        for (i = 3; i >= 0; i--) {

            System.out.println("Geben Sie bitte Ihr Passwort ein:");

            int eingabe = Manager.intscanner();


            if (passwort == eingabe) {
                System.out.println("Sie haben sich erfolgreich angemeldet! ");
                hauptmanager();
                return;

            } else if (i == 0) {
                System.out.println("Keine Eingaben mehr übrig, bitte wenden Sie sich an den Administrator!");
                //  System.exit(0);
            } else {
                System.out.println("Falsche Eingabe. Sie haben noch " + i + " Versuche.");
            }

        }
        }


    // Wenn der Mitarbeiter das Passwort eingegeben hat, kann er mehrere
    public void hauptmanager() {

        while (true) {

        System.out.println("-------------------Willkommen im Hauptmanager-------------------------");
        System.out.println("Willkommen Admin, was möchten Sie tun?: ");
        System.out.println("Drücken Sie die 1, um Fluggesellschaften hinzufügen oder zu entfernen. ");
        System.out.println("Drücken Sie die 2, um Flugzeuge einer Flugesellschaftsflotte hinzuzufügen oder zu entfernen.");
        System.out.println("Drücken Sie die 3, um Flughäfen hinzuzufügen oder zu entfernen.");
        System.out.println("Drücken Sie die 4, um einen neuen Flug anzulegen. ");
        System.out.println("Drücken Sie die 5, um einen Flug zu entfernen. ");
        System.out.println("Drücken Sie die 6, um die Buchungen zu sehen. ");
        System.out.println("Drücken Sie die 7, um sich abzumelden.");

            int auswahl = Manager.intscanner();

            switch (auswahl) {
                case 1:
                    fluggesellschaftenManager();
                    break;
                case 2:
                    flottenManager();
                    break;

                case 3:
                    flughafenManager();
                    break;

                case 4:
                    fluganlegen();
                    break;
                case 5:
                    flugentfernen();
                    break;

                case 6:
                    System.out.println(bs.getBuchungen());
                    System.out.println("Anzahl Buchungen: " + bs.getAnzahlBuchungen());
                    break;

               case 7:
                   return;

                default:
                    System.out.println("Ungültige Eingabe.");
                    break;
            }
        }
    }


    public  void fluggesellschaftenManager() {

        while (true) {
        System.out.println("---------Willkommen im Fluggesellschaftenmanager---------");
        System.out.println("Drücken Sie die 1, um eine Fluggesellschaft hinzuzufügen.  ");
        System.out.println("Drücken Sie die 2, um eine Fluggesellschaft zu entfernen.");
        System.out.println("Drücken Sie die 3, um zurück zum Hauptmanager zu gelangen.");
        System.out.println("---------------------------------------------------------");

            int auswahl = Manager.intscanner();
            switch (auswahl) {
                case 1:
                    fluggesellschaftAnlegen();
                    break;
                case 2:
                    fluggeselschaftentfernen();
                    break;
                case 3:
                    return;

                default:
                    System.out.print("Ungültige Eingabe");
                    break;
            }
        }

    }


    public  void flottenManager() {

        while (true) {

            System.out.println("--------------Willkommen im FlottenManager---------------");
            System.out.println("Drücken Sie die 1, um Flugzeuge der Flotte hinzuzufügen.");
            System.out.println("Drücken Sie die 2, um Flugzeug zu entfernen.");
            System.out.println("Drücken Sie die 3, um zurück zum Hauptmanager zu gelangen.");
            System.out.println("---------------------------------------------------------");

            int auswahl = Manager.intscanner();

            switch (auswahl) {
                case 1:
                    flugzeugderFlotteHinzufügen();
                    break;
                case 2:
                    flugzeugeEntfernen();
                    break;
                case 3:
                    return;
                default:
                    System.out.print("Ungültige Eingabe");
                    break;
            }
        }

    }


    public  void flughafenManager() {

        while (true) {
            System.out.println("--------------Willkommen im Flughafenmanager---------------");
            System.out.println("Drücken Sie die 1, um einen Flughafen hinzuzufügen. ");
            System.out.println("Drücken Sie die 2, um einen Flughafen zu entfernen.");
            System.out.println("Drücken Sie die 3, um zurück zum Hauptmanager zu gelangen.");
            System.out.println("---------------------------------------------------------");
            int auswahl = Manager.intscanner();
            switch (auswahl) {
                case 1:
                    hinzufügenFlughafen();
                    break;
                case 2:
                    entferneFlughafen();
                    break;
                case 3:
                    return;
                default:
                    System.out.print("Ungültige Eingabe! \n");
                    break;
            }

        }


    }


    //Fluggeselschaftanlegen
    public void fluggesellschaftAnlegen() {
        System.out.println("--------------Fluggesellschaft anlegen---------------------------");
        System.out.println("Name Fluggesellschaft: ");
        String name = Manager.Stringscanner();
        System.out.println("Airlinecode der Fluggesellschaft: ");
        String airlinecode = Manager.Stringscanner();

        Fluggesellschaft FluggeselschaftAnlegen = new Fluggesellschaft(name, airlinecode);




        try {
            vs.fuegeFluggesellschaftHinzu(FluggeselschaftAnlegen);
            datenHandler.speichere(anwendungsdaten);
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
            return;
        }

        System.out.println("Alle bisherigen Fluggesellschaften: ");

        System.out.println(vs.getFluggesellschaften());

    }

    


    public  void fluggeselschaftentfernen() {
        System.out.println("--------------Fluggesellschaft entfernen---------------------------");
        System.out.println(" Welche Fluggesellschaft möchten Sie entfernen?: ");
        System.out.println(vs.getFluggesellschaften());

        System.out.println(" Bitte geben Sie den Airlinecode der Fluggesellschaft ein, die Sie entfernen möchten:");
        try {
                String code = Manager.Stringscanner();
            vs.entferneFluggesellschaft(vs.getFluggesellschaft(code));

            datenHandler.speichere(anwendungsdaten);
            System.out.println("Fluggesellschaft erfolgreich entfernt.");

        }
        catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        }

    }


    //FlugzeugderFlotteHinzufügen

    public  void flugzeugderFlotteHinzufügen() {
        int laufvariable = -1;
        System.out.println("----------------------Flotte Flugzeuge hinzufügen------------------------------");
        System.out.println("Liste der bestehenden Fluggesellschaften, deren Flotte ein Flugzeug hinzugefügt werden kann:");
        System.out.println(vs.getFluggesellschaften());


        // Auswahl der Fluggeselschafften
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Bitte wählen Sie über den Airlinecode die Fluggesellschaft aus, der Sie Flugzeuge hinzufügen möchten: ");
            String auswahl = Manager.Stringscanner();
            try {
                vs.getFluggesellschaft(auswahl);
                datenHandler.speichere(anwendungsdaten);
            }
            catch (Exception e)
            {
                System.out.println("Fehler: " + e.getMessage());
                return;
            }


            System.out.println("--------------------------------------------------------------------");
            System.out.println("Bitte geben Sie den Code des Flugzeuges ein: ");
            String code = Manager.Stringscanner();
            System.out.println("Bitte geben Sie das Modell des Flugzeuges ein: ");
            String modell = Manager.Stringscanner();
            System.out.println("Bitte geben Sie die Anzahl der Reihen des Flugzeuges ein: ");
            int reihen = Manager.intscanner();
            System.out.println("Bitte geben Sie die Anzahl der Sitze pro Reihe des Flugzeuges ein: ");
            int sitze = Manager.intscanner();
            System.out.println("Bitte geben Sie die Anzahl der Businessreihen des Flugzeuges ein: ");
            int business = Manager.intscanner();


            // Flugzeug erstellen
        try {
            vs.erzeugeFlugzeug(vs.getFluggesellschaft(auswahl), code, modell, reihen, sitze, business);
            datenHandler.speichere(anwendungsdaten);
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
            return;
        }
            System.out.println("Das Flugzeug wurde der Flotte erfolgreich hinzugefügt!");
        }


     public  void flugzeugeEntfernen() {

         System.out.println("----------------------Flugzeug entfernen------------------------------");

         try {
             System.out.println(vs.getAlleFlugzeuge());
             System.out.println("Bitte geben Sie den Flugzeugcode zur Löschung ein:");
             String code = Manager.Stringscanner();
             vs.entferneFlugzeug(code);

             datenHandler.speichere(anwendungsdaten);

             System.out.println("Flugzeug wurde erfolgreich gelöscht!");

         } catch (Exception e) {
             System.out.println("Fehler: " + e.getMessage());
             return;
         }
     }




    public  void hinzufügenFlughafen()
    {
        System.out.println("----------------------Flughafen hinzufügen------------------------------");
        System.out.println("Geben Sie den Namen des Flughafens ein:");
        String name = Manager.Stringscanner();
        System.out.println("Geben Sie den IATA-Code des Flughafens ein:");
        String iatacode  = Manager.Stringscanner();
        System.out.println("Geben Sie die Stadt des Flughafens ein:");
        String stadt = Manager.Stringscanner();
        System.out.println("Geben Sie das Land des Flughafens ein: ");
        String land = Manager.Stringscanner();

        //Methode zum hinzufügen
        try {
            vs.erzeugeFlughafen(name, iatacode, stadt, land);
            datenHandler.speichere(anwendungsdaten);
        }
        catch (Exception e){
            System.out.println("Fehler: " + e.getMessage());
            return;
        }

        System.out.println("Flughafen: " + vs.getFlughafenNachCode(iatacode)  + " erfolgreich hinzugefügt!");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Zurück zum Flughafenmanager.");

    }

    public void entferneFlughafen()
    {
        System.out.println("----------------------Flughafen entfernen-------------------------------");
        System.out.println("Welchen Flughafen möchten Sie entfernen (Auswahl über IATA-Code)?: ");
        System.out.println(vs.getFlughaefen());
        System.out.println("Welchen Flughafen möchten Sie enternen (iataCode) ");

        String iataCode = Manager.Stringscanner();

        //Methode zum entfernen
        try {
            Flughafen flughafen = vs.getFlughafenNachCode(iataCode);

            vs.entferneFlughafen(flughafen);
            datenHandler.speichere(anwendungsdaten);
            System.out.println("Flughafen " + iataCode + " erfolgreich entfernt.");
        }
        catch (Exception e)
        {
            System.out.println("Fehler: " + e.getMessage());

        }

    }


    // Flüge anlegen
    public  void fluganlegen()
    {
        String geselschaft = "";
        String flugzeug = "";
        String startflughafen = "";
        String zielflughafen = "";
        LocalDateTime abflug = LocalDateTime.now();
        LocalDateTime ankunft =  LocalDateTime.now();

        System.out.println("---------------------------Willkommen im Bereich Flug anlegen-----------------------------");


        // Abfrage des Basispreises
        System.out.println("Bitte geben Sie den Basispreis Ihres Flugs ein: ");
        double basispreis = Manager.doublescanner();

        //Anzeige der gesamten Fluggesellschaften
        System.out.println("Welche Airline führt den Flug durch (Auswahl über Airlinecode)?:");
        System.out.println(vs.getFluggesellschaften());
        System.out.println("---------------------------------------------------------------------------------");

        try {
            geselschaft = Manager.Stringscanner();
            System.out.println("Welches Flugzeug soll für den Flug genutzt werden (Auswahl über Flugzeugcode)?: ");
            System.out.println(vs.getFluggesellschaft(geselschaft).getFlotte());
            System.out.println("------------------------------------------------------------------------");
            flugzeug  = Manager.Stringscanner();
            System.out.println("Ihr ausgewähltes Flugzeug: ");
            System.out.println(vs.getFlugzeug(flugzeug));

            System.out.println("------------------------------------------------------------------------");
            System.out.println("Wählen Sie den Start- und Zielflughafen aus:");
            System.out.println(vs.getFlughaefen());
            System.out.println("Wählen Sie den Startflughafen aus (Auswahl über IATA-Code): ");
            startflughafen  = Manager.Stringscanner();
            System.out.println(vs.getFlughafenNachCode(startflughafen));
            System.out.println("Wählen Sie den Zielflughafen aus (Auswahl über IATA-Code): ");
            zielflughafen  = Manager.Stringscanner();
            System.out.println(vs.getFlughafenNachCode(zielflughafen));


            System.out.println("---------------------------Bitte geben Sie die Abflugszeiten an-----------------------------");
            abflug = anabflug("des Abfluges");
            System.out.println("---------------------------Bitte geben Sie die Ankuftszeiten an-----------------------------");
            ankunft =  anabflug("der Ankunft");

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("Wollen Sie noch einen Rückflug hinzufügen? Drücken Sie die 1.");
            System.out.println("Wollen Sie keinen Rückflug hinzufügen? Drücken Sie die 2.");
            int ruck = Manager.intscanner();

            if (ruck == 1)
            {
                System.out.println("---------------------------Rückflug-----------------------------");

                boolean rucke = true;

                System.out.println("An wie vielen Tagen soll der Flug stattfinden?: ");
                int wiederholung = Manager.intscanner();

                vs.fuegeFlugHinzu(vs.getFluggesellschaft(geselschaft), vs.getFlugzeug(flugzeug), vs.getFlughafenNachCode(startflughafen), vs.getFlughafenNachCode(zielflughafen), abflug, ankunft, basispreis, rucke, wiederholung);
                datenHandler.speichere(anwendungsdaten);

            }
            else {
                vs.fuegeFlugHinzu(vs.getFluggesellschaft(geselschaft), vs.getFlugzeug(flugzeug), vs.getFlughafenNachCode(startflughafen), vs.getFlughafenNachCode(zielflughafen), abflug, ankunft, basispreis);
                datenHandler.speichere(anwendungsdaten);


            }



        }
        catch ( IllegalArgumentException e )
        {
            System.out.println("Fehler: " + e.getMessage());

        }

    }


    public  LocalDateTime  anabflug(String text)
    {
        System.out.println("Bitte geben Sie das Jahr " + text + " ein: ");
        int jahr  = Manager.intscanner();
        System.out.println("Bitte geben Sie den Monat " + text + " ein: ");
        int monat = Manager.intscanner();
        System.out.println("Bitte geben Sie den Tag " + text + " ein: ");
        int tag = Manager.intscanner();
        System.out.println("Bitte geben Sie die Stunde " + text + " ein: ");
        int stunde = Manager.intscanner();
        System.out.println("Bitte geben Sie die Minute " + text + " ein:");
        int minute = Manager.intscanner();
        datenHandler.speichere(anwendungsdaten);

    
        return vs.erstelleLocalDateTime(jahr, monat, tag, stunde, minute);

    }


    //Fluglöschen

    public  void flugentfernen()
    {
        LocalDate abflug = LocalDate.now();

        System.out.println("---------------------------Willkommen im Bereich Flug entfernen-----------------------------");
        System.out.println(vs.getFluege());
try {
    System.out.println("Bitte geben Sie die Flugnummer zum Löschen ein:");
    String flugnummer = Manager.Stringscanner();
    System.out.println("Bitte geben Sie die Abflugszeit ein:");
    System.out.println("Bitte geben Sie das Jahr des Abfluges ein: ");
    int jahr  = Manager.intscanner();
    System.out.println("Bitte geben Sie den Monat des Abfluges ein: ");
    int monat = Manager.intscanner();
    System.out.println("Bitte geben Sie den Tag des Abfluges ein: ");
    int tag = Manager.intscanner();
    abflug =  LocalDate.of(jahr, monat, tag );

    Flug flug = vs.sucheFlugNachNummer(flugnummer, abflug);

    if(!bs.findeRelevanteBuchungen(flug).isEmpty()) {
        vs.entferneFlug(flug);
        datenHandler.speichere(anwendungsdaten);
        System.out.println(flug + " wurde erfolgreich gelöscht!");
    }
    else {
        System.out.println("Dieser Flug beinhaltet leider noch Buchungen und kann daher nicht gelöscht werden.");
    }
    

}catch (Exception e)
{
    System.out.println("Fehler beim Entfernen: " + e.getMessage());
}
    }



}

