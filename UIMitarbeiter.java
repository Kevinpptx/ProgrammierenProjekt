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

            System.out.println("Geben Sie ein Passwort ein:");
            System.out.println("Geben Sie ein Passwort ein:");


            int eingabe = Manager.intscanner();


            if (passwort == eingabe) {
                System.out.println("Sie haben sich erflogreich Angemeldet: ");
                hauptmanager();
            } else if (i == 0) {
                System.out.println("Keine eingaben mehr übrig");
                //  System.exit(0);
            } else {
                System.out.println("Sie haben noch " + i + " Versuche");
            }

        }
        }


    // Wenn der Mitarbeiter das Passwort eingegeben hat, kann er mehrere
    public void hauptmanager() {

        System.out.println("-------------------Willkommen im Hauptmanager-------------------------");
        System.out.println("Wilkommen Admin was möchten sie tun: ");
        System.out.println("Drücken Sie die 1: Um Fluggeselschaften hinzufügen oder zu entfernen ");
        System.out.println("Drücken Sie die 2 Um Flugzeuge einer FlugeselschaftFlotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 Um Flughäfen hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 4 Um einen Flug anzulegen ");
        System.out.println("Drücken Sie die 5 Um einen Flug zu entfernen ");
        // An alle flüge mit auslastung denken!!!!!!!!!! anzeigen jetzt bei cedric

        int auswhal = Manager.intscanner();

        switch (auswhal) {
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
                case 5:
                    flugentfernen();
            default:
                hauptmanager();
        }
        hauptmanager();
    }


    public  void fluggeselschaftenManager() {
        System.out.println("---------Willkommen im fluggesselschaftenManager---------");
        System.out.println("Drücken Sie die 1 um Fluggesselschaften hinzuzufügen  ");
        System.out.println("Drücken Sie die 2 um Fluggesselschaften zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
        System.out.println("---------------------------------------------------------");

        int auswahl = Manager.intscanner();
        switch (auswahl) {
            case 1:
                fluggeselschaftanlegen();
                break;
            case 2:
                fluggeselschaftentfernen();
                break;
            case 3:
                hauptmanager();
                break;
            default:
                System.out.print("Falsche eingabe ");
                flottenManager();
        }

    }


    public  void flottenManager() {
        System.out.println("--------------Willkommen im FlottenManager---------------");
        System.out.println("Drücken Sie die 1 um Flugzeuge der Flotte hinzuzufügen ");
        System.out.println("Drücken Sie die 2 um Flugzeug zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
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
                hauptmanager();
                break;
            default:
                System.out.print("Falsche eingabe ");
                flottenManager();
        }


    }


    public  void flughafenManager() {
        System.out.println("--------------Willkommen im flughafenManager---------------");
        System.out.println("Drücken Sie die 1 um einen Flughafen hinzuzufügen ");
        System.out.println("Drücken Sie die 2 um einen Flughafen zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Hauptmanager zu gelangen");
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
                hauptmanager();
                break;
            default:
                System.out.print("Falsche eingabe ");
                flughafenManager();
        }


    }


    //Fluggeselschaftanlegen
    public void fluggeselschaftanlegen() {
        System.out.println("--------------Fluggeselschaft anlegen---------------------------");
        System.out.println("Flugeselschaft name: ");
        String name = Manager.Stringscanner();
        System.out.println("Flugeselschaft airlineCode : ");
        String airlinecode = Manager.Stringscanner();

        Fluggesellschaft FluggeselschaftAnlegen = new Fluggesellschaft(name, airlinecode);




        try {
            vs.fuegeFluggesellschaftHinzu(FluggeselschaftAnlegen);
            datenHandler.speichere(anwendungsdaten);
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
            fluggeselschaftenManager();
        }

        System.out.println("Alle bisherrigen Flugeselschaften: ");

        System.out.println(vs.getFluggesellschaften());


        // Möchten sie Flugzeuge der Flotte hinzufügen 1, entfernen 2, zurück 3,

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Drücken Sie die 1 um Flugzeuge der Flotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 2 um eine weitere Fluggeselschafft hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Haupt Manager zu gelangen");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Drücken Sie die 1 um Flugzeuge der Flotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 2 um eine weitere Fluggeselschafft hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 um zurück zum Haupt Manager zu gelangen");
        System.out.println("-------------------------------------------------------------------------");
        int antwort = Manager.intscanner();

        switch (antwort) {
            case 1:
                flottenManager();
                break;
            case 2:
                fluggeselschaftenManager();
                break;
            case 3:
                hauptmanager();
            default:
                System.out.print("Falsche eingabe ");
                hauptmanager();
        }

    }

    // Flugeselschaftentfernen


    public  void fluggeselschaftentfernen() {
        System.out.println("--------------Fluggeselschaft Entfernen---------------------------");
        System.out.println(" welche Fluggeselschaften möchten Sie Entfernen: ");
        System.out.println(vs.getFluggesellschaften());

        System.out.println(" Bitte geben sie den code der Fluggeselschaft ein, welche Sie entfernen möchten");
        try {
                String code = Manager.Stringscanner();

            vs.entferneFluggesellschaft(vs.getFluggesellschaft(code));
        }
        catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
            fluggeselschaftenManager();

        }

        fluggeselschaftenManager();
    }


    //FlugzeugderFlotteHinzufügen

    public  void flugzeugderFlotteHinzufügen() {
        int laufvariable = -1;
        System.out.println("----------------------Flugzeuge der Flotte Hinzufügen------------------------------");
        System.out.println("liste der Bestehenden Fluggeselschaften um zu deren Flotte ein Flugzeug hinzuzufügen");
        System.out.println(vs.getFluggesellschaften());


        // Auswahl der Fluggeselschafften
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Bitte Wählen sie über den Code ihre Fluggeselschaft aus welcher sie Flugzeuge hinzufügen möchten ");
            String auswahl = Manager.Stringscanner();
            try {
                vs.getFluggesellschaft(auswahl);
                datenHandler.speichere(anwendungsdaten);
            }
            catch (Exception e)
            {
                System.out.println("Fehler: " + e.getMessage());
                flottenManager();
            }


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
        try {
            vs.erzeugeFlugzeug(vs.getFluggesellschaft(auswahl), code, modell, reihen, sitze, business);
            datenHandler.speichere(anwendungsdaten);
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
            flottenManager();
        }

            System.out.println("Das Flugzeug wurde erfolgreich der Flotte Hinzugefügt ");
            flottenManager();
        }


     public  void flugzeugeEntfernen() {

         System.out.println("----------------------Flugzeuge Entfernen------------------------------");

         try {
             System.out.println(vs.getAlleFlugzeuge());
             System.out.println("Bitte wählen sie den Flugzeug code zum löschen aus");
             String code = Manager.Stringscanner();
             vs.entferneFlugzeug(code);
             System.out.println("Erfolgreich gelöscht");
             datenHandler.speichere(anwendungsdaten);

         } catch (Exception e) {
             System.out.println("Fehler: " + e.getMessage());
             flottenManager();
         }
     }




    public  void hinzufügenFlughafen()
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
        try {
            vs.erzeugeFlughafen(name, iatacode, stadt, land);
            datenHandler.speichere(anwendungsdaten);
        }
        catch (Exception e){
            System.out.println("Fehler: " + e.getMessage());
            flughafenManager();
        }

        System.out.println("flughafen: " + vs.getFlughafenNachCode(iatacode)  + " erfolgreich hinzugefügt");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Zurück zum Flughafen Manager ");
        flughafenManager();
    }

    public void entferneFlughafen()
    {
        System.out.println("----------------------Flughafen Entfernen-------------------------------");
        System.out.println("Welchen Flughafen möchten Sie enternen (iataCode) ");
        System.out.println(vs.getFlughaefen());
        String iataCode = Manager.Stringscanner();

        //Methode zum entfernen
        try {
            vs.entferneFlughafen(vs.getFlughafenNachCode(iataCode));
            datenHandler.speichere(anwendungsdaten);
        }
        catch (Exception e)
        {
            System.out.println("Fehler: " + e.getMessage());
            flughafenManager();
        }
        System.out.println("flughafen: " + vs.getFlughafenNachCode(iataCode) + " erfolgreich entfernt");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Zurück zum Flughafen Manager ");
        flughafenManager();
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

        System.out.println("---------------------------Willkommen beim Flug anlegen -----------------------------");

        // Abfrage der Flugnummer --> Macht cedric automatisch

        // Abfrage des Basispreises
        System.out.println("Bitte geben Sie den Basispreis ihres Flugs an ");
        double basispreis = Manager.doublescanner();

        //Anzeige der gesamten Fluggeselschaften
        System.out.println("Auswahl der Fluggeselschaft welche den Flug durchführen soll");
        System.out.println(vs.getFluggesellschaften());
        System.out.println("---------------------------------------------------------------------------------");

        try {
            geselschaft = Manager.Stringscanner();
            System.out.println("Nun wählen sie ein Flugzeug der Flotte über den Code aus: ");
            System.out.println(vs.getFluggesellschaft(geselschaft).getFlotte());
            System.out.println("------------------------------------------------------------------------");
            flugzeug  = Manager.Stringscanner();
            System.out.println("Ihr ausgewähltes Flugzeug ");
            System.out.println(vs.getFlugzeug(flugzeug));

            System.out.println("------------------------------------------------------------------------");
            System.out.println("Wählen Sie den Start und Zielflughafen aus");
            System.out.println(vs.getFlughaefen());
            System.out.println("Wählen Sie den Startflughafen über den iataCode aus ");
            startflughafen  = Manager.Stringscanner();
            System.out.println(vs.getFlughafenNachCode(startflughafen));
            System.out.println("Wählen Sie den Zielflughafen über den iataCode aus ");
            zielflughafen  = Manager.Stringscanner();
            System.out.println(vs.getFlughafenNachCode(zielflughafen));


            System.out.println("---------------------------Bitte geben Sie die Abflugszeiten an-----------------------------");
            abflug = anabflug("des Abfluges");
            System.out.println("---------------------------Bitte geben Sie die Ankuftszeiten an-----------------------------");
            ankunft =  anabflug("der Ankunft");

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("Wollen sie noch einen Rückfluck hinzufügen drücken sie die 1");
            System.out.println("Wollen sie keinen Rückflug hinzufügen drücken sie die 2");
            int ruck = Manager.intscanner();

            if (ruck == 1)
            {
                System.out.println("---------------------------Rückflug-----------------------------");

                boolean rucke = true;

                System.out.println("An wie vielen Tagen soll der Flugstatt finden: ");
                int wiederholung = Manager.intscanner();

                vs.fuegeFlugHinzu(vs.getFluggesellschaft(geselschaft), vs.getFlugzeug(flugzeug), vs.getFlughafenNachCode(startflughafen), vs.getFlughafenNachCode(zielflughafen), abflug, ankunft, basispreis, rucke, wiederholung);
                datenHandler.speichere(anwendungsdaten);
                hauptmanager();
            }
            else {
                vs.fuegeFlugHinzu(vs.getFluggesellschaft(geselschaft), vs.getFlugzeug(flugzeug), vs.getFlughafenNachCode(startflughafen), vs.getFlughafenNachCode(zielflughafen), abflug, ankunft, basispreis);
                datenHandler.speichere(anwendungsdaten);
                hauptmanager();
            }



        }
        catch ( IllegalArgumentException e )
        {
            System.out.println("Fehler: " + e.getMessage());
            hauptmanager();
        }

    }


    public  LocalDateTime  anabflug(String text)
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
        datenHandler.speichere(anwendungsdaten);

        // Cedrics Methode zurückgeben
        return vs.erstelleLocalDateTime(jahr, monat, tag, stunde, minute);

    }


    //Fluglöschen

    public  void flugentfernen()
    {
        LocalDate abflug = LocalDate.now();

        System.out.println("---------------------------Willkommen beim Flug Entfernen -----------------------------");
        System.out.println(vs.getFluege());
try {
    System.out.println("Bitte Zum Löschen die Flugnummer angeben ");
    String flugnummer = Manager.Stringscanner();
    System.out.println("Bitte geben Sie die Abflugszeiten an");
    System.out.println("Bitte geben Sie das Jahr des Abfluges ein ");
    int jahr  = Manager.intscanner();
    System.out.println("Bitte geben Sie den Monat Jahr des Abfluges ein ");
    int monat = Manager.intscanner();
    System.out.println("Bitte geben Sie den TagJahr des Abfluges ein ");
    int tag = Manager.intscanner();
    abflug =  LocalDate.of(jahr, monat, tag );

    System.out.print(vs.sucheFlugNachNummer(flugnummer, abflug ) + " wurde gelöscht");
    vs.entferneFlug(vs.sucheFlugNachNummer(flugnummer, abflug ));
    datenHandler.speichere(anwendungsdaten);



}catch (Exception e)
{
    System.out.println("Fehler beim Entfernen: " + e.getMessage());
}
    }



}

