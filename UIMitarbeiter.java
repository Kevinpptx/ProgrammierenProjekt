
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

/**
 * Die Klasse {@code UIMitarbeiter} stellt die Konsolenoberfläche für
 * Mitarbeiter und Administratoren bereit.
 *
 * Nach erfolgreicher Anmeldung können Fluggesellschaften, Flugzeuge, Flughäfen
 * und Flüge verwaltet werden. Zusätzlich können alle vorhandenen Buchungen
 * angezeigt werden.
 *
 * Änderungen werden mithilfe des {@link DatenHandler} dauerhaft gespeichert.
 *
 * @author Lars Pfeiffer, Cedric Beckmann
 * @version 1.1
 */
public class UIMitarbeiter {

    /**
     * DatenHandler zum Speichern der Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthält die aktuell verwendeten Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * Buchungssystem zur Verwaltung und Anzeige der Buchungen.
     */
    private final Buchungssystem bs;

    /**
     * Verwaltungssystem zur Verwaltung von Fluggesellschaften, Flugzeugen,
     * Flughäfen und Flügen.
     */
    private final Verwaltungssystem vs;

    /**
     * Formatiert Datum und Uhrzeit im Format {@code dd.MM.yyyy HH:mm}.
     */
    private static final DateTimeFormatter DATUM_ZEIT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Erstellt eine neue Mitarbeiteroberfläche.
     *
     * Das Buchungs- und Verwaltungssystem werden aus den übergebenen
     * Anwendungsdaten übernommen.
     *
     * @param datenHandler Handler zum Speichern der Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte Anwendungsdaten
     * @throws IllegalArgumentException wenn der DatenHandler oder die
     * Anwendungsdaten {@code null} sind
     */
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

    /**
     * Führt die Anmeldung eines Mitarbeiters durch.
     *
     * Der Mitarbeiter muss das festgelegte vierstellige Passwort eingeben.
     * Insgesamt stehen vier Anmeldeversuche zur Verfügung. Nach einer
     * erfolgreichen Anmeldung wird das Hauptmenü geöffnet.
     *
     * Sind alle Versuche aufgebraucht, wird die Anmeldung beendet.
     */
    // Der Mitarbeiter meldet sich über ein Passwort als Admin an
    public void login() {
        int passwort = 1234;
        int i;

        for (i = 3; i >= 0; i--) {

            UIHelper.druckeEingabeaufforderung("Geben Sie bitte Ihr Passwort ein:");

            int eingabe = Manager.intscanner();

            if (passwort == eingabe) {
                UIHelper.druckeErfolg("Sie haben sich erfolgreich angemeldet.");
                hauptmanager();
                return;

            } else if (i == 0) {
                UIHelper.druckeFehler("Keine Versuche mehr übrig. Bitte wenden Sie sich an den Administrator.");
                //  System.exit(0);
            } else {
                UIHelper.druckeFehler("Falsche Eingabe. Sie haben noch " + i + " Versuche.");
            }

        }
    }

    /**
     * Zeigt das Hauptmenü für Mitarbeiter an.
     *
     * Über dieses Menü können Fluggesellschaften, Flugzeuge, Flughäfen und
     * Flüge verwaltet werden. Außerdem können die vorhandenen Buchungen
     * angezeigt werden.
     *
     * Das Menü wird so lange wiederholt, bis sich der Mitarbeiter abmeldet.
     */
    // Wenn der Mitarbeiter das Passwort eingegeben hat, kann er mehrere
    public void hauptmanager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Hauptmanager");

            UIHelper.druckeEingabeaufforderung("Was möchten Sie tun?");

            UIHelper.druckeMenuepunkt(1, "Fluggesellschaften verwalten");
            UIHelper.druckeMenuepunkt(2, "Flugzeuge verwalten");
            UIHelper.druckeMenuepunkt(3, "Flughäfen verwalten");
            UIHelper.druckeMenuepunkt(4, "Flug anlegen");
            UIHelper.druckeMenuepunkt(5, "Flug entfernen");
            UIHelper.druckeMenuepunkt(6, "Buchungen anzeigen");
            UIHelper.druckeMenuepunkt(7, "Flugübersicht anzeigen");
            UIHelper.druckeMenuepunkt(0, "Abmelden");

            UIHelper.druckeTrennlinie();

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
                    flugAnlegen();
                    break;
                case 5:
                    flugEntfernen();
                    break;

                case 6:
                    buchungenAnzeigen();
                    break;

                case 7:
                    fluguebersicht();
                    break;

                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }
    }

    /**
     * Zeigt das Verwaltungsmenü für Fluggesellschaften an.
     *
     * Der Mitarbeiter kann eine neue Fluggesellschaft anlegen, eine vorhandene
     * Fluggesellschaft entfernen oder zum Hauptmenü zurückkehren.
     */
    public void fluggesellschaftenManager() {

        while (true) {
            UIHelper.druckeUeberschrift("Willkommen im Fluggesellschaftenmanager");

            UIHelper.druckeMenuepunkt(1, "Fluggesellschaft hinzufügen");
            UIHelper.druckeMenuepunkt(2, "Fluggesellschaft entfernen");
            UIHelper.druckeMenuepunkt(0, "Zurück zum Hauptmanager");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();
            switch (auswahl) {
                case 1:
                    fluggesellschaftAnlegen();
                    break;
                case 2:
                    fluggeselschaftentfernen();
                    break;
                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }

    }

    /**
     * Zeigt das Verwaltungsmenü für Flugzeugflotten an.
     *
     * Der Mitarbeiter kann ein Flugzeug zur Flotte einer Fluggesellschaft
     * hinzufügen, ein vorhandenes Flugzeug entfernen oder zum Hauptmenü
     * zurückkehren.
     */
    public void flottenManager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Flottenmanager");

            UIHelper.druckeMenuepunkt(1, "Flugzeug hinzufügen");
            UIHelper.druckeMenuepunkt(2, "Flugzeug entfernen");
            UIHelper.druckeMenuepunkt(0, "Zurück zum Hauptmanager");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {
                case 1:
                    flugzeugderFlotteHinzufügen();
                    break;
                case 2:
                    flugzeugeEntfernen();
                    break;
                case 0:
                    return;
                default:    
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }

    }

    /**
     * Zeigt das Verwaltungsmenü für Flughäfen an.
     *
     * Der Mitarbeiter kann einen neuen Flughafen hinzufügen, einen vorhandenen
     * Flughafen entfernen oder zum Hauptmenü zurückkehren.
     */
    public void flughafenManager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Flughafenmanager");

            UIHelper.druckeMenuepunkt(1, "Flughafen hinzufügen");
            UIHelper.druckeMenuepunkt(2, "Flughafen entfernen");
            UIHelper.druckeMenuepunkt(0, "Zurück zum Hauptmanager");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();
            switch (auswahl) {
                case 1:
                    hinzufügenFlughafen();
                    break;
                case 2:
                    entferneFlughafen();
                    break;
                case 0:
                    return;
                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }

        }

    }

    /**
     * Legt eine neue Fluggesellschaft an.
     *
     * Der Mitarbeiter gibt den Namen und den Airlinecode ein. Anschließend wird
     * die Fluggesellschaft dem Verwaltungssystem hinzugefügt und dauerhaft
     * gespeichert.
     */
    //Fluggeselschaftanlegen
    public void fluggesellschaftAnlegen() {

        UIHelper.druckeUeberschrift("Fluggesellschaft anlegen");

        UIHelper.druckeEingabeaufforderung("Name Fluggesellschaft: ");
        String name = Manager.stringscanner();

        UIHelper.druckeEingabeaufforderung("Airlinecode der Fluggesellschaft: ");
        String airlinecode = Manager.stringscanner();

        try {
            Fluggesellschaft fluggesellschaftAnlegen = new Fluggesellschaft(name, airlinecode);
            Fluggesellschaft fluggesellschaft = vs.fuegeFluggesellschaftHinzu(fluggesellschaftAnlegen);
            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Fluggesellschaft " + fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName() + " wurde erfolgreich angelegt.");
        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }

        UIHelper.druckeTrennlinie();

        System.out.println("Alle bisherigen Fluggesellschaften: ");

        for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

    }

    /**
     * Entfernt eine vorhandene Fluggesellschaft.
     *
     * Die Fluggesellschaft wird über ihren Airlinecode ausgewählt. Nach
     * erfolgreicher Entfernung werden die Anwendungsdaten gespeichert.
     */
    public void fluggeselschaftentfernen() {

        UIHelper.druckeUeberschrift("Fluggesellschaft entfernen");

        System.out.println("Folgende Fluggesellschaften können entfernt werden:");
        for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

        UIHelper.druckeTrennlinie();
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Airlinecode der zu entfernenden Fluggesellschaft ein:");
        try {
            String code = Manager.stringscanner();
            vs.entferneFluggesellschaft(vs.getFluggesellschaft(code));

            datenHandler.speichere(anwendungsdaten);
            UIHelper.druckeErfolg("Fluggesellschaft erfolgreich entfernt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }

    }

    /**
     * Fügt der Flotte einer Fluggesellschaft ein neues Flugzeug hinzu.
     *
     * Zuerst wird die Fluggesellschaft über ihren Airlinecode ausgewählt.
     * Danach werden Flugzeugcode, Modell, Reihenanzahl, Sitze pro Reihe und
     * Anzahl der Businessreihen abgefragt.
     *
     * Nach erfolgreicher Erstellung wird das Flugzeug gespeichert.
     */
    //FlugzeugderFlotteHinzufügen
    public void flugzeugderFlotteHinzufügen() {

        UIHelper.druckeUeberschrift("Flugzeug der Flotte hinzufügen");


        System.out.println("Folgenden Fluggesellschaften kann ein Flugzeug hinzugefügt werden:");

        UIHelper.druckeTrennlinie();

        for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

        // Auswahl der Fluggeselschafften
        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Bitte wählen Sie über den Airlinecode die Fluggesellschaft aus, der Sie Flugzeuge hinzufügen möchten: ");

        String auswahl = Manager.stringscanner();
        try {
            vs.getFluggesellschaft(auswahl);

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }

        UIHelper.druckeTrennlinie();
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie einen Code für das Flugzeug ein:");
        String code = Manager.stringscanner();
        while(code.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen Code für das Flugzeug an: ");
            code = Manager.stringscanner();
        } 
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie das Modell des Flugzeuges ein: ");
        String modell = Manager.stringscanner();
        while(modell.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie ein Modell für das Flugzeug an: ");
            modell = Manager.stringscanner();
        } 
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Reihen des Flugzeuges ein: ");
        int reihen = Manager.intscanner();
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Sitze pro Reihe des Flugzeuges ein: ");
        int sitze = Manager.intscanner();
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Businessreihen des Flugzeuges ein: ");
        int business = Manager.intscanner();

        // Flugzeug erstellen
        try {
             Flugzeug flugzeug = vs.erzeugeFlugzeug(vs.getFluggesellschaft(auswahl), code, modell, reihen, sitze, business);
             datenHandler.speichere(anwendungsdaten);
             UIHelper.druckeErfolg("Das Flugzeug " + flugzeug.getCode() + " wurde erfolgreich hinzugefügt.");
        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }
    }

    /**
     * Entfernt ein Flugzeug aus dem Verwaltungssystem.
     *
     * Zunächst werden alle vorhandenen Flugzeuge angezeigt. Das gewünschte
     * Flugzeug wird anschließend über seinen Flugzeugcode ausgewählt.
     *
     * Nach erfolgreicher Entfernung werden die Anwendungsdaten gespeichert.
     */
    public void flugzeugeEntfernen() {

        UIHelper.druckeUeberschrift("Flugzeug entfernen");

        try {
            for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {

                System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());

                if (fluggesellschaft.getFlotte().isEmpty()) {
                    UIHelper.druckeHinweis("Keine Flugzeuge vorhanden.");
                } else {

                    for (Flugzeug f : fluggesellschaft.getFlotte()) {

                        System.out.println(f.getCode() + " | " + f.getModell()
                                + " | Business-Sitze: " + f.getAnzahlBusiness()
                                + " | Economy-Sitze: " + f.getAnzahlEconomy()
                                + " | Gesamt: " + f.getGesamtSitzanzahl()
                        );
                    }
                }

                UIHelper.druckeTrennlinie();

            }

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Flugzeugcode des zu entfernenden Flugzeugs ein:");

            String code = Manager.stringscanner();
            vs.entferneFlugzeug(code);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Das Flugzeug wurde erfolgreich entfernt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }
    }

    /**
     * Fügt dem Verwaltungssystem einen neuen Flughafen hinzu.
     *
     * Der Mitarbeiter gibt den Namen, IATA-Code, die Stadt und das Land des
     * Flughafens ein. Anschließend wird der Flughafen erzeugt und gespeichert.
     */
    public void hinzufügenFlughafen() {

        UIHelper.druckeUeberschrift("Flughafen hinzufügen");

        UIHelper.druckeEingabeaufforderung("Geben Sie den Namen des Flughafens ein:");
        String name = Manager.stringscanner();
        while(name.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen Namen für den Flughafen ein: ");
            name = Manager.stringscanner();
        } 

        UIHelper.druckeEingabeaufforderung("Geben Sie den IATA-Code des Flughafens ein:");
        String iatacode = Manager.stringscanner();
        while(iatacode.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen IATA-Code für den Flughafen ein: ");
            iatacode = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Geben Sie die Stadt des Flughafens ein:");
        String stadt = Manager.stringscanner();
        while(stadt.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie eine Stadt für den Flughafen ein: ");
            stadt = Manager.stringscanner();
        }

        
        UIHelper.druckeEingabeaufforderung("Geben Sie das Land des Flughafens ein: ");
        String land = Manager.stringscanner();
        while(land.isEmpty()) {
            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie ein Land für den Flughafen ein: ");
            land = Manager.stringscanner();
        }

        //Methode zum hinzufügen
        try {
            Flughafen flughafen = vs.erzeugeFlughafen(name, iatacode, stadt, land);

            datenHandler.speichere(anwendungsdaten);
            UIHelper.druckeErfolg("Der Flughafen " +  flughafen.getIataCode() + " " + flughafen.getName() + " wurde erfolgreich hinzugefügt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }

    }

    /**
     * Entfernt einen vorhandenen Flughafen.
     *
     * Der Flughafen wird über seinen IATA-Code ausgewählt. Nach erfolgreicher
     * Entfernung werden die Anwendungsdaten gespeichert.
     */
    public void entferneFlughafen() {

        UIHelper.druckeUeberschrift("Flughafen entfernen");

        System.out.println("Folgende Flughäfen können entfernt werden: ");

        UIHelper.druckeTrennlinie();

        System.out.printf("%-6s | %-35s | %-15s | %-15s%n","IATA", "Flughafen", "Ort", "Land");

        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : vs.getFlughaefen()) {
            System.out.printf("%-4s | %-35s | %-15s | %s%n", flughafen.getIataCode(), flughafen.getName(), flughafen.getStadt(), flughafen.getLand());
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Wählen Sie den Flughafen zum Entfernen über den IATA-Code: ");

        String iataCode = Manager.stringscanner();

        //Methode zum entfernen
        try {
            Flughafen flughafen = vs.getFlughafenNachCode(iataCode);

            vs.entferneFlughafen(flughafen);
            datenHandler.speichere(anwendungsdaten);
            UIHelper.druckeErfolg("Flughafen " + iataCode + " erfolgreich entfernt.");
        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());

        }

    }

    /**
     * Legt einen neuen Flug im Verwaltungssystem an.
     *
     * Für den Flug werden der Basispreis, die Fluggesellschaft, das Flugzeug,
     * der Startflughafen, der Zielflughafen sowie die Abflug- und Ankunftszeit
     * abgefragt.
     *
     * Zusätzlich kann ein Rückflug beziehungsweise eine Wiederholung ausgewählt
     * werden. Nach erfolgreicher Erstellung werden die Anwendungsdaten
     * gespeichert.
     */
    // Flüge anlegen
    public void flugAnlegen() {

        String gesellschaft;
        String flugzeug;
        String startflughafen;
        String zielflughafen;
        LocalDateTime abflug;
        LocalDateTime ankunft;

        UIHelper.druckeUeberschrift("Flug anlegen");

        // Basispreis
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Basispreis des Flugs ein:");
        double basispreis = Manager.doublescanner();
        System.out.println();

        UIHelper.druckeTrennlinie();

        // Fluggesellschaft auswählen
        UIHelper.druckeEingabeaufforderung("Welche Airline führt den Flug durch? Auswahl über Airlinecode:");

        for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {
            System.out.println(
                    fluggesellschaft.getAirlineCode() + " - "+ fluggesellschaft.getName());
        }

        UIHelper.druckeTrennlinie();

        Fluggesellschaft ausgewaehlteGesellschaft;

        while (true) {

            gesellschaft = Manager.stringscanner().toUpperCase();

            try {
                ausgewaehlteGesellschaft = vs.getFluggesellschaft(gesellschaft);
                break;
            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
                UIHelper.druckeEingabeaufforderung("Bitte geben Sie einen gültigen Airlinecode ein:");
            }
        }

        // Flugzeug auswählen

        if (ausgewaehlteGesellschaft.getFlotte().isEmpty()) {
            UIHelper.druckeHinweis("Für diese Fluggesellschaft sind keine Flugzeuge vorhanden.");
            return;
        }

        for (Flugzeug f : ausgewaehlteGesellschaft.getFlotte()) {
            System.out.println(
                    f.getCode()
                    + " | " + f.getModell()
                    + " | Business-Sitze: " + f.getAnzahlBusiness()
                    + " | Economy-Sitze: " + f.getAnzahlEconomy()
                    + " | Gesamt: " + f.getGesamtSitzanzahl()
            );
        }

        UIHelper.druckeTrennlinie();

        Flugzeug ausgewaehltesFlugzeug;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Flugzeugcode ein:");

            flugzeug = Manager.stringscanner().toUpperCase();

            try {
                ausgewaehltesFlugzeug = vs.getFlugzeug(flugzeug);

                if (!ausgewaehlteGesellschaft.getFlotte().contains(ausgewaehltesFlugzeug)) {
                    UIHelper.druckeFehler("Dieses Flugzeug gehört nicht zur ausgewählten Fluggesellschaft.");
                    continue;
                }

                break;

            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        UIHelper.druckeHinweis("Ausgewähltes Flugzeug: " + ausgewaehltesFlugzeug.getCode() + " - " + ausgewaehltesFlugzeug.getModell() + ", " 
                + ausgewaehltesFlugzeug.getGesamtSitzanzahl() + " Sitzplätze");

        UIHelper.druckeTrennlinie();


        // Flughäfen anzeigen
        UIHelper.druckeEingabeaufforderung("Folgende Flughäfen stehen zur Verfügung: ");

        UIHelper.druckeTrennlinie();
        System.out.printf("%-6s | %-35s | %-15s | %-15s%n","IATA", "Flughafen", "Ort", "Land");
        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : vs.getFlughaefen()) {
            System.out.printf("%-6s | %-35s | %-15s | %-15s%n", flughafen.getIataCode(), flughafen.getName(), flughafen.getStadt(), flughafen.getLand());
        }

        UIHelper.druckeTrennlinie();

        // Startflughafen
        Flughafen ausgewaehlterStartflughafen;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Wählen Sie den Startflughafen aus. Auswahl über IATA-Code:");

            startflughafen = Manager.stringscanner().toUpperCase();

            try {
                ausgewaehlterStartflughafen = vs.getFlughafenNachCode(startflughafen);
                break;
            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        // Zielflughafen
        Flughafen ausgewaehlterZielflughafen;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Wählen Sie den Zielflughafen aus. Auswahl über IATA-Code:");

            zielflughafen = Manager.stringscanner().toUpperCase();

            try {
                ausgewaehlterZielflughafen = vs.getFlughafenNachCode(zielflughafen);
                break;
            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        UIHelper.druckeErfolg("Der Flug geht von " + ausgewaehlterStartflughafen.getName() + " nach " + ausgewaehlterZielflughafen.getName() + ".");

        UIHelper.druckeTrennlinie();
        
        // Zeiten
        abflug = datumUndUhrzeitEinlesen("des Abfluges");

        ankunft = datumUndUhrzeitEinlesen("der Ankunft");

        UIHelper.druckeTrennlinie();

        // Rückflug / Serienflug
        boolean gueltigeAuswahl = false;

        while (!gueltigeAuswahl) {

            UIHelper.druckeMenuepunkt(1, "Flug mit Rückflug anlegen");
            UIHelper.druckeMenuepunkt(0, "Flug ohne Rückflug anlegen");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    UIHelper.druckeEingabeaufforderung("An wie vielen Tagen soll das Flugpaar stattfinden?");

                    int wiederholung = Manager.intscanner();

                    try {
                        vs.fuegeFlugHinzu(
                                ausgewaehlteGesellschaft,
                                ausgewaehltesFlugzeug,
                                ausgewaehlterStartflughafen,
                                ausgewaehlterZielflughafen,
                                abflug,
                                ankunft,
                                basispreis,
                                true,
                                wiederholung
                        );

                        datenHandler.speichere(anwendungsdaten);

                        UIHelper.druckeErfolg("Die Flüge wurden erfolgreich angelegt.");

                        gueltigeAuswahl = true;

                    } catch (Exception e) {
                        UIHelper.druckeFehler(e.getMessage());
                        return;
                    }

                    break;

                case 0:
                    try {
                        vs.fuegeFlugHinzu(
                                ausgewaehlteGesellschaft,
                                ausgewaehltesFlugzeug,
                                ausgewaehlterStartflughafen,
                                ausgewaehlterZielflughafen,
                                abflug,
                                ankunft,
                                basispreis
                        );

                        datenHandler.speichere(anwendungsdaten);

                        UIHelper.druckeErfolg("Der Flug wurde erfolgreich angelegt.");

                    } catch (Exception e) {
                        UIHelper.druckeFehler(e.getMessage());
                    }

                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie 1 oder 0 ein.");
            }
        }
    }

    /**
     * Liest Datum und Uhrzeit für einen Flugzeitpunkt ein und gibt diese als
     * LocalDateTime zurück. Ungültige Eingaben werden erneut abgefragt.
     *
     * @param text Beschreibung des Zeitpunkts, z. B. "des Abfluges"
     * @return eingegebenes Datum mit Uhrzeit als LocalDateTime
     */
    public LocalDateTime datumUndUhrzeitEinlesen(String text) {

        DateTimeFormatter datumsFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter zeitFormatter = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

        LocalDate datum = null;
        LocalTime zeit = null;

        while (datum == null) {
            UIHelper.druckeEingabeaufforderung("Bitte geben Sie das Datum (dd.MM.yyyy) " + text + " ein:");

            try {
                datum = LocalDate.parse(Manager.stringscanner(),datumsFormatter);
                System.out.println();
            } catch (DateTimeException e) {
                UIHelper.druckeFehler("Ungültiges Datum. Beispiel: 01.01.2026");
            }
            
        }

        while (zeit == null) {
            UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Uhrzeit (HH:mm) " + text + " ein:");

            try {
                zeit = LocalTime.parse(Manager.stringscanner(), zeitFormatter);
                System.out.println();
            } catch (DateTimeException e) {
                UIHelper.druckeFehler("Ungültige Uhrzeit. Beispiel: 10:30");
            }
        }

        return datum.atTime(zeit);
    }

    /**
     * Entfernt einen vorhandenen Flug aus dem Verwaltungssystem.
     *
     * Der gewünschte Flug wird anhand seiner Flugnummer und seines Abflugdatums
     * gesucht. Ein Flug kann nur entfernt werden, wenn keine relevanten
     * Buchungen für diesen Flug vorhanden sind.
     *
     * Nach erfolgreicher Entfernung werden die Anwendungsdaten gespeichert.
     */
    public void flugEntfernen() {

        vs.alteFluegeLoeschen(bs);

        UIHelper.druckeUeberschrift("Flug entfernen");

        ArrayList<Flug> auswaehlbareFluege = new ArrayList<>();
        int nummer = 1;

        for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {

            System.out.println();
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());

            boolean hatFluege = false;

            for (Flug flug : vs.getFluege()) {

                if (flug.getFluggesellschaft().equals(fluggesellschaft)) {

                    hatFluege = true;
                    auswaehlbareFluege.add(flug);

                    System.out.println(
                            String.format("%3d: ", nummer)
                            + flug.getFlugnummer()
                            + " | "
                            + flug.getStartFlughafen().getIataCode()
                            + " -> "
                            + flug.getZielflughafen().getIataCode()
                            + " | Abflug: "
                            + flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER)
                    );

                    nummer++;
                }
            }

            if (!hatFluege) {
                UIHelper.druckeHinweis("Keine aktiven Flüge.");
            }
        }

        System.out.println();

        if (auswaehlbareFluege.isEmpty()) {
            UIHelper.druckeHinweis("Es sind keine Flüge zum Entfernen vorhanden.");
            return;
        }

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Nummer des zu entfernenden Fluges ein:");
        UIHelper.druckeEingabeaufforderung("Geben Sie 0 ein, um zum Hauptmanager zurückzukehren.");

        int auswahl = Manager.intscanner();

        if (auswahl == 0) {
            return;
        }

        if (auswahl < 1 || auswahl > auswaehlbareFluege.size()) {
            UIHelper.druckeFehler("Ungültige Auswahl.");
            return;
        }

        Flug flug = auswaehlbareFluege.get(auswahl - 1);

        if (bs.findeRelevanteBuchungen(flug).isEmpty()) {

            vs.entferneFlug(flug);
            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Der Flug " + flug.getFlugnummer() + " am " + flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER) + " wurde erfolgreich entfernt.");

        } else {
            UIHelper.druckeFehler("Dieser Flug beinhaltet noch Buchungen und kann daher nicht entfernt werden.");
        }
    }

    /**
     * Zeigt alle aktiven Flüge gruppiert nach Fluggesellschaft an.
     *
     * Für Fluggesellschaften ohne aktive Flüge wird ein entsprechender Hinweis
     * ausgegeben. Anschließend kann ein Flug ausgewählt werden, um weitere
     * Informationen anzuzeigen.
     */
    private void fluguebersicht() {

        vs.alteFluegeLoeschen(bs);

        while (true) {

            UIHelper.druckeUeberschrift("Flugübersicht");

            ArrayList<Flug> auswaehlbareFluege = new ArrayList<>();
            int nummer = 1;

            for (Fluggesellschaft fluggesellschaft : vs.getFluggesellschaften()) {

                System.out.println();
                System.out.println(fluggesellschaft.getName() + " (" + fluggesellschaft.getAirlineCode() + ")");

                boolean hatFluege = false;

                for (Flug flug : vs.getFluege()) {

                    if (flug.getFluggesellschaft().equals(fluggesellschaft)) {

                        hatFluege = true;
                        auswaehlbareFluege.add(flug);

                        System.out.println(
                                String.format("%3d: ", nummer)
                                + flug.getFlugnummer()
                                + " | "
                                + flug.getStartFlughafen().getIataCode()
                                + " -> "
                                + flug.getZielflughafen().getIataCode()
                                + " | Abflug: "
                                + flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER)
                                + " | Auslastung: "
                                + flug.berechneAuslastung()
                                + "%"
                        );

                        nummer++;
                    }
                }

                if (!hatFluege) {
                    UIHelper.druckeHinweis("Keine aktiven Flüge.");
                }
            }

            UIHelper.druckeTrennlinie();
            UIHelper.druckeEingabeaufforderung("Geben Sie die Nummer eines Fluges ein, um weitere Informationen anzuzeigen.");
            UIHelper.druckeEingabeaufforderung("Drücken Sie 0, um zum Hauptmanager zurückzukehren.");

            int auswahl = Manager.intscanner();

            if (auswahl == 0) {
                return;
            }

            if (auswahl < 1 || auswahl > auswaehlbareFluege.size()) {
                UIHelper.druckeFehler("Ungültige Auswahl.");
                continue;
            }

            Flug ausgewaehlterFlug = auswaehlbareFluege.get(auswahl - 1);
            flugdetails(ausgewaehlterFlug);
        }
    }

    /**
     * Zeigt Detailinformationen zu einem ausgewählten Flug an.
     *
     * @param flug der ausgewählte Flug
     */
    public void flugdetails(Flug flug) {

        while (true) {

            UIHelper.druckeUeberschrift("Flugdetails - " +  flug.getFlugnummer());
            System.out.println("Flug: " + flug.getFlugnummer());
            System.out.println("Fluggesellschaft: " + flug.getFluggesellschaft().getName());
            System.out.println("Route: " + flug.getStartFlughafen().getIataCode() + " -> " + flug.getZielflughafen().getIataCode());
            System.out.println("Abflug: " + flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER));
            System.out.println("Ankunft: " + flug.getAnkunftszeit().format(DATUM_ZEIT_FORMATTER));
            System.out.println("Flugzeug: " + flug.getFlugzeug().getModell() + " (" + flug.getFlugzeug().getCode() + ")");
            System.out.println("Auslastung: " + flug.berechneAuslastung() + "%");

            UIHelper.druckeTrennlinie();

            UIHelper.druckeMenuepunkt(1, "Sitzplan anzeigen");
            UIHelper.druckeMenuepunkt(2, "Passagier- und Gepäckübersicht anzeigen");
            UIHelper.druckeMenuepunkt(0, "Zurück zur Flugübersicht");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    UIHelper.druckeUeberschrift("Sitzplan für Flug " + flug.getFlugnummer());
                    flug.zeigeSitzplan();
                    break;

                case 2:
                    UIHelper.druckeUeberschrift("Passagier- und Gepäckübersicht für Flug " + flug.getFlugnummer());

                    ArrayList<Buchung> buchungen = bs.findeRelevanteBuchungen(flug);

                    if (buchungen.isEmpty()) {
                        UIHelper.druckeHinweis("Für diesen Flug liegen keine Buchungen vor.");
                    } else {
                        int nummer = 1;

                        for (Buchung buchung : buchungen) {
                            System.out.println(
                                    String.format("%3d: ", nummer)
                                    + "Passagier: " + buchung.getPassagier().getPassagierId() + ", "
                                    + buchung.getPassagier().getName() + " | Sitzplatz: "
                                    + buchung.getSitzplatz().getSitzplatzNummer() + " | Koffer: "
                                    + buchung.getGepaeckinformation().getAnzahlKoffer()
                            );
                            nummer++;
                        }
                    }
                    break;

                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Eingabe.");
                    break;
            }
        }
    }

    private void buchungenAnzeigen() {

        UIHelper.druckeUeberschrift("Buchungsübersicht");

        vs.alteFluegeLoeschen(bs);

        if (bs.getBuchungen().isEmpty()) {
            UIHelper.druckeHinweis("Es sind keine Buchungen vorhanden.");
            return;
        }

        System.out.printf(
                "%-8s | %-11s | %-7s | %-11s | %-16s | %-5s | %-8s | %-6s | %-12s | %-9s%n",
                "Buchung", "Passagier", "Flug", "Route", "Abflug", "Sitz", "Klasse", "Koffer", "Preis", "Status");

        UIHelper.druckeTrennlinie();

        for (Buchung buchung : bs.getBuchungen()) {

            Flug flug = buchung.getFlug();

            String passagier = buchung.getPassagier().getPassagierId()
                    + " " + buchung.getPassagier().getName();

            System.out.printf(
                    "%-8s | %-11.11s | %-7s | %-11s | %-16s | %-5s | %-8s | %-6d | %-12s | %-9s%n",
                    buchung.getBuchungsnummer(),
                    passagier,
                    flug.getFlugnummer(),
                    flug.getStartFlughafen().getIataCode() + " -> " + flug.getZielflughafen().getIataCode(),
                    flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER),
                    buchung.getSitzplatz().getSitzplatzNummer(),
                    buchung.getSitzplatz().getSitzklasse(),
                    buchung.getGepaeckinformation().getAnzahlKoffer(),
                    String.format("%.2f Euro", buchung.getGezahlterPreis()),
                    buchung.getBuchungsstatus()
            );
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeHinweis("Anzahl der Buchungen: " + bs.getBuchungen().size());
    }

}
