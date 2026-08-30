package avigator.frontend;

import avigator.modell.*;
import avigator.verwaltung.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

/**
 * Die Klasse {@code avigator.frontend.UIMitarbeiter} stellt die Konsolenoberfläche für Mitarbeiter und Administratoren bereit.
 * <p>
 * Nach erfolgreicher Anmeldung können Fluggesellschaften, Flugzeuge, Flughäfen und Flüge verwaltet werden. Zusätzlich
 * können alle vorhandenen Buchungen angezeigt werden.
 * <p>
 * Änderungen werden mithilfe des {@link DatenHandler} dauerhaft gespeichert.
 *
 * @author Lars Pfeiffer, Cedric Beckmann
 * @version 1.1
 */
public class UIMitarbeiter {

    /**
     * avigator.verwaltung.DatenHandler zum Speichern der avigator.verwaltung.Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthält die aktuell verwendeten avigator.verwaltung.Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * avigator.verwaltung.Buchungssystem zur Verwaltung und Anzeige der Buchungen.
     */
    private final Buchungssystem buchungssystem;

    /**
     * avigator.verwaltung.Verwaltungssystem zur Verwaltung von Fluggesellschaften, Flugzeugen, Flughäfen und Flügen.
     */
    private final Verwaltungssystem verwaltungssystem;

    /**
     * Formatiert Datum und Uhrzeit im Format {@code dd.MM.yyyy HH:mm}.
     */
    private static final DateTimeFormatter datumZeitFormatierer = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Erstellt eine neue Mitarbeiteroberfläche.
     * <p>
     * Das Buchungs- und avigator.verwaltung.Verwaltungssystem werden aus den übergebenen avigator.verwaltung.Anwendungsdaten übernommen.
     *
     * @param datenHandler    Handler zum Speichern der avigator.verwaltung.Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte avigator.verwaltung.Anwendungsdaten
     * @throws IllegalArgumentException wenn der avigator.verwaltung.DatenHandler oder die avigator.verwaltung.Anwendungsdaten {@code null} sind
     */
    public UIMitarbeiter(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der avigator.verwaltung.DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die avigator.verwaltung.Anwendungsdaten dürfen nicht null sein.");
        }

        this.datenHandler = datenHandler;
        this.anwendungsdaten = anwendungsdaten;
        this.buchungssystem = anwendungsdaten.getBuchungssystem();
        this.verwaltungssystem = anwendungsdaten.getVerwaltungssystem();
    }

    /**
     * Führt die Anmeldung eines Mitarbeiters durch.
     * <p>
     * Der Mitarbeiter muss das festgelegte vierstellige Passwort eingeben. Insgesamt stehen vier Anmeldeversuche zur
     * Verfügung. Nach einer erfolgreichen Anmeldung wird das Hauptmenü geöffnet.
     * <p>
     * Sind alle Versuche aufgebraucht, wird die Anmeldung beendet.
     */
    // Der Mitarbeiter meldet sich über ein Passwort als Admin an
    public void login() {

        int passwort = 1234;

        for (int i = 3; i >= 0; i--) {

            UIHelper.druckeEingabeaufforderung("Geben Sie bitte Ihr Passwort ein:");

            int eingabe = Manager.intscanner();

            if (passwort == eingabe) {

                UIHelper.druckeErfolg("Sie haben sich erfolgreich angemeldet.");
                hauptmanagerMitarbeiter();

                return;

            } else if (i == 0) {
                UIHelper.druckeFehler("Keine Versuche mehr übrig. Bitte wenden Sie sich an den Administrator.");
            } else {
                UIHelper.druckeFehler("Falsche Eingabe. Sie haben noch " + i + " Versuche.");
            }

        }
    }

    /**
     * Zeigt das Hauptmenü für Mitarbeiter an.
     * <p>
     * Über dieses Menü können Fluggesellschaften, Flugzeuge, Flughäfen und Flüge verwaltet werden. Außerdem können die
     * vorhandenen Buchungen angezeigt werden.
     * <p>
     * Das Menü wird so lange wiederholt, bis sich der Mitarbeiter abmeldet.
     */
    // Wenn der Mitarbeiter das Passwort eingegeben hat, kann er mehrere
    private void hauptmanagerMitarbeiter() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Hauptmanager");

            UIHelper.druckeEingabeaufforderung("Was möchten Sie tun?");

            UIHelper.druckeMenuepunkt(1, "Fluggesellschaften verwalten");
            UIHelper.druckeMenuepunkt(2, "Flugzeuge verwalten");
            UIHelper.druckeMenuepunkt(3, "Flughäfen verwalten");
            UIHelper.druckeMenuepunkt(4, "avigator.modell.Flug anlegen");
            UIHelper.druckeMenuepunkt(5, "avigator.modell.Flug entfernen");
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
     * <p>
     * Der Mitarbeiter kann eine neue avigator.modell.Fluggesellschaft anlegen, eine vorhandene avigator.modell.Fluggesellschaft entfernen oder zum
     * Hauptmenü zurückkehren.
     */
    private void fluggesellschaftenManager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Fluggesellschaftenmanager");

            UIHelper.druckeMenuepunkt(1, "avigator.modell.Fluggesellschaft hinzufügen");
            UIHelper.druckeMenuepunkt(2, "avigator.modell.Fluggesellschaft entfernen");
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
     * <p>
     * Der Mitarbeiter kann ein avigator.modell.Flugzeug zur Flotte einer avigator.modell.Fluggesellschaft hinzufügen, ein vorhandenes avigator.modell.Flugzeug
     * entfernen oder zum Hauptmenü zurückkehren.
     */
    private void flottenManager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Flottenmanager");

            UIHelper.druckeMenuepunkt(1, "avigator.modell.Flugzeug hinzufügen");
            UIHelper.druckeMenuepunkt(2, "avigator.modell.Flugzeug entfernen");
            UIHelper.druckeMenuepunkt(0, "Zurück zum Hauptmanager");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    flugzeugDerFlotteHinzufuegen();
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
     * <p>
     * Der Mitarbeiter kann einen neuen avigator.modell.Flughafen hinzufügen, einen vorhandenen avigator.modell.Flughafen entfernen oder zum Hauptmenü
     * zurückkehren.
     */
    private void flughafenManager() {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen im Flughafenmanager");

            UIHelper.druckeMenuepunkt(1, "avigator.modell.Flughafen hinzufügen");
            UIHelper.druckeMenuepunkt(2, "avigator.modell.Flughafen entfernen");
            UIHelper.druckeMenuepunkt(0, "Zurück zum Hauptmanager");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    flughafenHinzufuegen();
                    break;

                case 2:
                    flughafenEntfernen();
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
     * Legt eine neue avigator.modell.Fluggesellschaft an.
     * <p>
     * Der Mitarbeiter gibt den Namen und den Airlinecode ein. Anschließend wird die avigator.modell.Fluggesellschaft dem
     * avigator.verwaltung.Verwaltungssystem hinzugefügt und dauerhaft gespeichert.
     */

    private void fluggesellschaftAnlegen() {

        UIHelper.druckeUeberschrift("avigator.modell.Fluggesellschaft anlegen");

        UIHelper.druckeEingabeaufforderung("Name avigator.modell.Fluggesellschaft: ");
        String name = Manager.stringscanner();

        UIHelper.druckeEingabeaufforderung("Airlinecode der avigator.modell.Fluggesellschaft: ");
        String airlinecode = Manager.stringscanner();

        try {

            Fluggesellschaft fluggesellschaftAnlegen = new Fluggesellschaft(name, airlinecode);
            Fluggesellschaft fluggesellschaft = verwaltungssystem.fuegeFluggesellschaftHinzu(fluggesellschaftAnlegen);
            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die avigator.modell.Fluggesellschaft " +
                                  fluggesellschaft.getAirlineCode() +
                                  " - " +
                                  fluggesellschaft.getName() +
                                  " wurde erfolgreich angelegt.");
        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }

        UIHelper.druckeTrennlinie();

        System.out.println("Alle bisherigen Fluggesellschaften: ");

        for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

    }

    /**
     * Entfernt eine vorhandene avigator.modell.Fluggesellschaft.
     * <p>
     * Die avigator.modell.Fluggesellschaft wird über ihren Airlinecode ausgewählt. Nach erfolgreicher Entfernung werden die
     * avigator.verwaltung.Anwendungsdaten gespeichert.
     */
    private void fluggeselschaftentfernen() {

        UIHelper.druckeUeberschrift("avigator.modell.Fluggesellschaft entfernen");

        System.out.println("Folgende Fluggesellschaften können entfernt werden:");

        for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Airlinecode der zu entfernenden avigator.modell.Fluggesellschaft ein:");

        try {

            String code = Manager.stringscanner();
            verwaltungssystem.entferneFluggesellschaft(verwaltungssystem.getFluggesellschaft(code));

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("avigator.modell.Fluggesellschaft erfolgreich entfernt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }

    }

    /**
     * Fügt der Flotte einer avigator.modell.Fluggesellschaft ein neues avigator.modell.Flugzeug hinzu.
     * <p>
     * Zuerst wird die avigator.modell.Fluggesellschaft über ihren Airlinecode ausgewählt. Danach werden Flugzeugcode, Modell,
     * Reihenanzahl, Sitze pro Reihe und Anzahl der Businessreihen abgefragt.
     * <p>
     * Nach erfolgreicher Erstellung wird das avigator.modell.Flugzeug gespeichert.
     */

    private void flugzeugDerFlotteHinzufuegen() {

        UIHelper.druckeUeberschrift("avigator.modell.Flugzeug der Flotte hinzufügen");

        System.out.println("Folgenden Fluggesellschaften kann ein avigator.modell.Flugzeug hinzugefügt werden:");

        UIHelper.druckeTrennlinie();

        for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

        // Auswahl der Fluggesellschaften
        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung(
                "Bitte wählen Sie über den Airlinecode die avigator.modell.Fluggesellschaft aus, der Sie Flugzeuge hinzufügen möchten: ");

        String auswahl = Manager.stringscanner();

        try {

            verwaltungssystem.getFluggesellschaft(auswahl);

        } catch (Exception e) {

            UIHelper.druckeFehler(e.getMessage());
            return;
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie einen Code für das avigator.modell.Flugzeug ein:");
        String code = Manager.stringscanner();

        while (code.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen Code für das avigator.modell.Flugzeug an: ");
            code = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie das Modell des Flugzeuges ein: ");
        String modell = Manager.stringscanner();

        while (modell.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie ein Modell für das avigator.modell.Flugzeug an: ");
            modell = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Reihen des Flugzeuges ein: ");
        int reihen = Manager.intscanner();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Sitze pro Reihe des Flugzeuges ein: ");
        int sitze = Manager.intscanner();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Anzahl der Businessreihen des Flugzeuges ein: ");
        int business = Manager.intscanner();

        // avigator.modell.Flugzeug erstellen
        try {

            Flugzeug flugzeug = verwaltungssystem.erzeugeFlugzeug(verwaltungssystem.getFluggesellschaft(auswahl),
                    code,
                    modell,
                    reihen,
                    sitze,
                    business
            );

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Das avigator.modell.Flugzeug " + flugzeug.getCode() + " wurde erfolgreich hinzugefügt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Entfernt ein avigator.modell.Flugzeug aus dem avigator.verwaltung.Verwaltungssystem.
     * <p>
     * Zunächst werden alle vorhandenen Flugzeuge angezeigt. Das gewünschte avigator.modell.Flugzeug wird anschließend über seinen
     * Flugzeugcode ausgewählt.
     * <p>
     * Nach erfolgreicher Entfernung werden die avigator.verwaltung.Anwendungsdaten gespeichert.
     */
    private void flugzeugeEntfernen() {

        UIHelper.druckeUeberschrift("avigator.modell.Flugzeug entfernen");

        try {

            for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {

                System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());

                if (fluggesellschaft.getFlotte().isEmpty()) {

                    UIHelper.druckeHinweis("Keine Flugzeuge vorhanden.");

                } else {

                    for (Flugzeug flugzeug : fluggesellschaft.getFlotte()) {

                        System.out.println(flugzeug.getCode() + " | " + flugzeug.getModell()
                                           + " | Business-Sitze: " + flugzeug.getAnzahlBusiness()
                                           + " | Economy-Sitze: " + flugzeug.getAnzahlEconomy()
                                           + " | Gesamt: " + flugzeug.getGesamtSitzanzahl()
                        );
                    }
                }

                UIHelper.druckeTrennlinie();

            }

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Flugzeugcode des zu entfernenden Flugzeugs ein:");

            String code = Manager.stringscanner();
            verwaltungssystem.entferneFlugzeug(code);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Das avigator.modell.Flugzeug wurde erfolgreich entfernt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Fügt dem avigator.verwaltung.Verwaltungssystem einen neuen avigator.modell.Flughafen hinzu.
     * <p>
     * Der Mitarbeiter gibt den Namen, IATA-Code, die Stadt und das Land des Flughafens ein. Anschließend wird der
     * avigator.modell.Flughafen erzeugt und gespeichert.
     */
    private void flughafenHinzufuegen() {

        UIHelper.druckeUeberschrift("avigator.modell.Flughafen hinzufügen");

        UIHelper.druckeEingabeaufforderung("Geben Sie den Namen des Flughafens ein:");
        String name = Manager.stringscanner();

        while (name.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen Namen für den avigator.modell.Flughafen ein: ");
            name = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Geben Sie den IATA-Code des Flughafens ein:");
        String iataCode = Manager.stringscanner();

        while (iataCode.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie einen IATA-Code für den avigator.modell.Flughafen ein: ");
            iataCode = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Geben Sie die Stadt des Flughafens ein:");
        String stadt = Manager.stringscanner();

        while (stadt.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie eine Stadt für den avigator.modell.Flughafen ein: ");
            stadt = Manager.stringscanner();
        }

        UIHelper.druckeEingabeaufforderung("Geben Sie das Land des Flughafens ein: ");
        String land = Manager.stringscanner();

        while (land.isEmpty()) {

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie ein Land für den avigator.modell.Flughafen ein: ");
            land = Manager.stringscanner();
        }

        try {

            Flughafen flughafen = verwaltungssystem.erzeugeFlughafen(name, iataCode, stadt, land);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Der avigator.modell.Flughafen " +
                                  flughafen.iataCode() +
                                  " " +
                                  flughafen.name() +
                                  " wurde erfolgreich hinzugefügt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }

    }

    /**
     * Entfernt einen vorhandenen avigator.modell.Flughafen.
     * <p>
     * Der avigator.modell.Flughafen wird über seinen IATA-Code ausgewählt. Nach erfolgreicher Entfernung werden die avigator.verwaltung.Anwendungsdaten
     * gespeichert.
     */
    private void flughafenEntfernen() {

        UIHelper.druckeUeberschrift("avigator.modell.Flughafen entfernen");

        System.out.println("Folgende Flughäfen können entfernt werden: ");

        UIHelper.druckeTrennlinie();

        System.out.printf("%-6s | %-35s | %-15s | %-15s%n", "IATA", "avigator.modell.Flughafen", "Ort", "Land");

        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : verwaltungssystem.getFlughaefen()) {

            System.out.printf("%-4s | %-35s | %-15s | %s%n",
                    flughafen.iataCode(),
                    flughafen.name(),
                    flughafen.stadt(),
                    flughafen.land()
            );
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Wählen Sie den avigator.modell.Flughafen zum Entfernen über den IATA-Code: ");

        String iataCode = Manager.stringscanner();

        try {

            Flughafen flughafen = verwaltungssystem.getFlughafenNachCode(iataCode);

            verwaltungssystem.entferneFlughafen(flughafen);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("avigator.modell.Flughafen " + iataCode + " erfolgreich entfernt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Legt einen neuen avigator.modell.Flug oder mehrere wiederkehrende Flugpaare im avigator.verwaltung.Verwaltungssystem an.
     * <p>
     * Zunächst werden Basispreis, avigator.modell.Fluggesellschaft und ein zugehöriges avigator.modell.Flugzeug ausgewählt. Anschließend werden Start-
     * und Zielflughafen sowie Abflug- und Ankunftszeit erfasst. Ungültige Codes für Fluggesellschaften, Flugzeuge oder
     * Flughäfen werden erneut abgefragt.
     * <p>
     * Danach kann entweder ein einzelner avigator.modell.Flug oder ein avigator.modell.Flug mit Rückflug angelegt werden. Bei einem avigator.modell.Flug mit Rückflug
     * wird zusätzlich angegeben, an wie vielen aufeinanderfolgenden Tagen das Flugpaar stattfinden soll.
     * <p>
     * Erfolgreich erzeugte Flüge werden anschließend dauerhaft gespeichert.
     */
    private void flugAnlegen() {

        String fluggesellschaft2, flugzeug, startflughafen, zielflughafen;
        LocalDateTime ankunft, abflug;

        UIHelper.druckeUeberschrift("avigator.modell.Flug anlegen");

        // Basispreis
        UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Basispreis des Flugs ein:");
        double basispreis = Manager.doublescanner();

        System.out.println();
        UIHelper.druckeTrennlinie();

        // avigator.modell.Fluggesellschaft auswählen
        UIHelper.druckeEingabeaufforderung("Welche Airline führt den avigator.modell.Flug durch? Auswahl über Airlinecode:");

        for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {

            System.out.println(
                    fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());
        }

        UIHelper.druckeTrennlinie();

        Fluggesellschaft ausgewaehlteFluggesellschaft;

        while (true) {

            fluggesellschaft2 = Manager.stringscanner().toUpperCase();

            try {

                ausgewaehlteFluggesellschaft = verwaltungssystem.getFluggesellschaft(fluggesellschaft2);
                break;

            } catch (Exception e) {

                UIHelper.druckeFehler(e.getMessage());
                UIHelper.druckeEingabeaufforderung("Bitte geben Sie einen gültigen Airlinecode ein:");
            }
        }

        // avigator.modell.Flugzeug auswählen

        if (ausgewaehlteFluggesellschaft.getFlotte().isEmpty()) {

            UIHelper.druckeHinweis("Für diese avigator.modell.Fluggesellschaft sind keine Flugzeuge vorhanden.");
            return;
        }

        for (Flugzeug flugzeugTemporaer : ausgewaehlteFluggesellschaft.getFlotte()) {

            System.out.println(
                    flugzeugTemporaer.getCode()
                    + " | " + flugzeugTemporaer.getModell()
                    + " | Business-Sitze: " + flugzeugTemporaer.getAnzahlBusiness()
                    + " | Economy-Sitze: " + flugzeugTemporaer.getAnzahlEconomy()
                    + " | Gesamt: " + flugzeugTemporaer.getGesamtSitzanzahl()
            );
        }

        UIHelper.druckeTrennlinie();

        Flugzeug ausgewaehltesFlugzeug;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie den Flugzeugcode ein:");

            flugzeug = Manager.stringscanner().toUpperCase();

            try {

                ausgewaehltesFlugzeug = verwaltungssystem.getFlugzeug(flugzeug);

                if (!ausgewaehlteFluggesellschaft.getFlotte().contains(ausgewaehltesFlugzeug)) {

                    UIHelper.druckeFehler("Dieses avigator.modell.Flugzeug gehört nicht zur ausgewählten avigator.modell.Fluggesellschaft.");
                    continue;
                }

                break;

            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        UIHelper.druckeHinweis("Ausgewähltes avigator.modell.Flugzeug: " +
                               ausgewaehltesFlugzeug.getCode() +
                               " - " +
                               ausgewaehltesFlugzeug.getModell() +
                               ", "
                               +
                               ausgewaehltesFlugzeug.getGesamtSitzanzahl() +
                               " Sitzplätze");

        UIHelper.druckeTrennlinie();

        // Flughäfen anzeigen
        UIHelper.druckeEingabeaufforderung("Folgende Flughäfen stehen zur Verfügung: ");

        UIHelper.druckeTrennlinie();
        System.out.printf("%-6s | %-35s | %-15s | %-15s%n", "IATA", "avigator.modell.Flughafen", "Ort", "Land");
        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : verwaltungssystem.getFlughaefen()) {

            System.out.printf("%-6s | %-35s | %-15s | %-15s%n",
                    flughafen.iataCode(),
                    flughafen.name(),
                    flughafen.stadt(),
                    flughafen.land()
            );
        }

        UIHelper.druckeTrennlinie();

        Flughafen ausgewaehlterStartflughafen;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Wählen Sie den Startflughafen aus. Auswahl über IATA-Code:");

            startflughafen = Manager.stringscanner().toUpperCase();

            try {

                ausgewaehlterStartflughafen = verwaltungssystem.getFlughafenNachCode(startflughafen);
                break;

            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        Flughafen ausgewaehlterZielflughafen;

        while (true) {

            UIHelper.druckeEingabeaufforderung("Wählen Sie den Zielflughafen aus. Auswahl über IATA-Code:");

            zielflughafen = Manager.stringscanner().toUpperCase();

            try {

                ausgewaehlterZielflughafen = verwaltungssystem.getFlughafenNachCode(zielflughafen);
                break;

            } catch (Exception e) {
                UIHelper.druckeFehler(e.getMessage());
            }
        }

        UIHelper.druckeErfolg("Der avigator.modell.Flug geht von " +
                              ausgewaehlterStartflughafen.name() +
                              " nach " +
                              ausgewaehlterZielflughafen.name() +
                              ".");

        UIHelper.druckeTrennlinie();

        // Zeiten
        abflug = datumUndUhrzeitEinlesen("des Abfluges");
        ankunft = datumUndUhrzeitEinlesen("der Ankunft");

        UIHelper.druckeTrennlinie();

        // Rückflug / Serienflug
        boolean gueltigeAuswahl = false;

        while (!gueltigeAuswahl) {

            UIHelper.druckeMenuepunkt(1, "avigator.modell.Flug mit Rückflug anlegen");
            UIHelper.druckeMenuepunkt(0, "avigator.modell.Flug ohne Rückflug anlegen");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    UIHelper.druckeEingabeaufforderung("An wie vielen Tagen soll das Flugpaar stattfinden?");

                    int wiederholung = Manager.intscanner();

                    try {

                        verwaltungssystem.fuegeFlugHinzu(
                                ausgewaehlteFluggesellschaft,
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

                        verwaltungssystem.fuegeFlugHinzu(
                                ausgewaehlteFluggesellschaft,
                                ausgewaehltesFlugzeug,
                                ausgewaehlterStartflughafen,
                                ausgewaehlterZielflughafen,
                                abflug,
                                ankunft,
                                basispreis
                        );

                        datenHandler.speichere(anwendungsdaten);

                        UIHelper.druckeErfolg("Der avigator.modell.Flug wurde erfolgreich angelegt.");

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
     * Liest Datum und Uhrzeit für einen Flugzeitpunkt ein und gibt diese als LocalDateTime zurück. Ungültige Eingaben
     * werden erneut abgefragt.
     *
     * @param text Beschreibung des Zeitpunkts, z. B. "des Abfluges"
     * @return eingegebenes Datum mit Uhrzeit als LocalDateTime
     */
    private LocalDateTime datumUndUhrzeitEinlesen(String text) {

        DateTimeFormatter datumZeitFormatierer = DateTimeFormatter
                .ofPattern("dd.MM.uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter zeitFormatierer = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

        LocalDate datum = null;
        LocalTime zeit = null;

        while (datum == null) {

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie das Datum (dd.MM.yyyy) " + text + " ein:");

            try {

                datum = LocalDate.parse(Manager.stringscanner(), datumZeitFormatierer);
                System.out.println();

            } catch (DateTimeException e) {
                UIHelper.druckeFehler("Ungültiges Datum. Beispiel: 01.01.2026");
            }

        }

        while (zeit == null) {

            UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Uhrzeit (HH:mm) " + text + " ein:");

            try {

                zeit = LocalTime.parse(Manager.stringscanner(), zeitFormatierer);
                System.out.println();

            } catch (DateTimeException e) {
                UIHelper.druckeFehler("Ungültige Uhrzeit. Beispiel: 10:30");
            }
        }

        return datum.atTime(zeit);
    }

    /**
     * Entfernt einen ausgewählten avigator.modell.Flug aus dem avigator.verwaltung.Verwaltungssystem.
     * <p>
     * Zunächst werden vergangene Flüge entfernt und alle verbleibenden Flüge gruppiert nach avigator.modell.Fluggesellschaft nummeriert
     * angezeigt. Der Mitarbeiter wählt den zu entfernenden avigator.modell.Flug anschließend über diese Nummer aus oder bricht den
     * Vorgang mit {@code 0} ab.
     * <p>
     * Ein avigator.modell.Flug kann nur entfernt werden, wenn keine relevanten Buchungen mehr für ihn vorhanden sind. Nach
     * erfolgreicher Entfernung werden die avigator.verwaltung.Anwendungsdaten gespeichert.
     */
    private void flugEntfernen() {

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        UIHelper.druckeUeberschrift("avigator.modell.Flug entfernen");

        ArrayList<Flug> auswaehlbareFluege = new ArrayList<>();
        int nummer = 1;

        for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {

            System.out.println();
            System.out.println(fluggesellschaft.getAirlineCode() + " - " + fluggesellschaft.getName());

            boolean hatFluege = false;

            for (Flug flug : verwaltungssystem.getFluege()) {

                if (flug.getFluggesellschaft().equals(fluggesellschaft)) {

                    hatFluege = true;
                    auswaehlbareFluege.add(flug);

                    System.out.println(
                            String.format("%3d: ", nummer)
                            + flug.getFlugnummer()
                            + " | "
                            + flug.getStartFlughafen().iataCode()
                            + " -> "
                            + flug.getZielflughafen().iataCode()
                            + " | Abflug: "
                            + flug.getAbflugszeit().format(datumZeitFormatierer)
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

        if (buchungssystem.findeRelevanteBuchungen(flug).isEmpty()) {

            verwaltungssystem.entferneFlug(flug);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Der avigator.modell.Flug " +
                                  flug.getFlugnummer() +
                                  " am " +
                                  flug.getAbflugszeit().format(datumZeitFormatierer) +
                                  " wurde erfolgreich entfernt.");

        } else {
            UIHelper.druckeFehler("Dieser avigator.modell.Flug beinhaltet noch Buchungen und kann daher nicht entfernt werden.");
        }
    }

    /**
     * Zeigt alle aktiven Flüge gruppiert nach avigator.modell.Fluggesellschaft an.
     * <p>
     * Für Fluggesellschaften ohne aktive Flüge wird ein entsprechender Hinweis ausgegeben. Anschließend kann ein avigator.modell.Flug
     * ausgewählt werden, um weitere Informationen anzuzeigen.
     */
    private void fluguebersicht() {

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        while (true) {

            UIHelper.druckeUeberschrift("Flugübersicht");

            ArrayList<Flug> auswaehlbareFluege = new ArrayList<>();

            int nummer = 1;

            for (Fluggesellschaft fluggesellschaft : verwaltungssystem.getFluggesellschaften()) {

                System.out.println();
                System.out.println(fluggesellschaft.getName() + " (" + fluggesellschaft.getAirlineCode() + ")");

                boolean hatFluege = false;

                for (Flug flug : verwaltungssystem.getFluege()) {

                    if (flug.getFluggesellschaft().equals(fluggesellschaft)) {

                        hatFluege = true;

                        auswaehlbareFluege.add(flug);

                        System.out.println(
                                String.format("%3d: ", nummer)
                                + flug.getFlugnummer()
                                + " | "
                                + flug.getStartFlughafen().iataCode()
                                + " -> "
                                + flug.getZielflughafen().iataCode()
                                + " | Abflug: "
                                + flug.getAbflugszeit().format(datumZeitFormatierer)
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
            UIHelper.druckeEingabeaufforderung(
                    "Geben Sie die Nummer eines Fluges ein, um weitere Informationen anzuzeigen.");
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
            druckeFlugdetails(ausgewaehlterFlug);
        }
    }

    /**
     * Zeigt Detailinformationen zu einem ausgewählten avigator.modell.Flug und stellt weitere Ansichten für diesen avigator.modell.Flug bereit.
     * <p>
     * Angezeigt werden unter anderem avigator.modell.Fluggesellschaft, Route, Abflug- und Ankunftszeit, eingesetztes avigator.modell.Flugzeug und
     * aktuelle Auslastung. Über ein Untermenü können zusätzlich der Sitzplan sowie eine avigator.modell.Passagier- und Gepäckübersicht
     * aufgerufen werden.
     * <p>
     * Das Untermenü wird so lange angezeigt, bis der Benutzer zur Flugübersicht zurückkehrt.
     *
     * @param flug der avigator.modell.Flug, dessen Detailinformationen angezeigt werden
     */
    private void druckeFlugdetails(Flug flug) {

        while (true) {

            UIHelper.druckeUeberschrift("Flugdetails - " + flug.getFlugnummer());

            System.out.println("avigator.modell.Flug: " + flug.getFlugnummer());
            System.out.println("avigator.modell.Fluggesellschaft: " + flug.getFluggesellschaft().getName());
            System.out.println("Route: " +
                               flug.getStartFlughafen().iataCode() +
                               " -> " +
                               flug.getZielflughafen().iataCode());
            System.out.println("Abflug: " + flug.getAbflugszeit().format(datumZeitFormatierer));
            System.out.println("Ankunft: " + flug.getAnkunftszeit().format(datumZeitFormatierer));
            System.out.println("avigator.modell.Flugzeug: " +
                               flug.getFlugzeug().getModell() +
                               " (" +
                               flug.getFlugzeug().getCode() +
                               ")");
            System.out.println("Auslastung: " + flug.berechneAuslastung() + "%");

            UIHelper.druckeTrennlinie();

            UIHelper.druckeMenuepunkt(1, "Sitzplan anzeigen");
            UIHelper.druckeMenuepunkt(2, "avigator.modell.Passagier- und Gepäckübersicht anzeigen");
            UIHelper.druckeMenuepunkt(0, "Zurück zur Flugübersicht");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    UIHelper.druckeUeberschrift("Sitzplan für avigator.modell.Flug " + flug.getFlugnummer());
                    flug.zeigeSitzplan();
                    break;

                case 2:
                    UIHelper.druckeUeberschrift("avigator.modell.Passagier- und Gepäckübersicht für avigator.modell.Flug " + flug.getFlugnummer());

                    ArrayList<Buchung> buchungen = buchungssystem.findeRelevanteBuchungen(flug);

                    if (buchungen.isEmpty()) {
                        UIHelper.druckeHinweis("Für diesen avigator.modell.Flug liegen keine Buchungen vor.");
                    } else {

                        int nummer = 1;

                        for (Buchung buchung : buchungen) {

                            System.out.println(
                                    String.format("%3d: ", nummer)
                                    + "avigator.modell.Passagier: " + buchung.getPassagier().passagierId() + ", "
                                    + buchung.getPassagier().name() + " | avigator.modell.Sitzplatz: "
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

    /**
     * Zeigt alle vorhandenen Buchungen in einer tabellarischen Übersicht an.
     * <p>
     * Vor der Anzeige werden vergangene Flüge entfernt und die betroffenen avigator.modell.Buchungsstatus aktualisiert. Angezeigt
     * werden unter anderem avigator.modell.Passagier, avigator.modell.Flug, Route, Abflugzeit, avigator.modell.Sitzplatz, avigator.modell.Sitzklasse, Gepäck, Buchungspreis und
     * avigator.modell.Buchungsstatus.
     * <p>
     * Zusätzlich wird die aktuelle Anzahl der gespeicherten Buchungen ausgegeben. Sind keine Buchungen vorhanden, wird
     * ein entsprechender Hinweis angezeigt.
     */
    private void buchungenAnzeigen() {

        UIHelper.druckeUeberschrift("Buchungsübersicht");

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        if (buchungssystem.getBuchungen().isEmpty()) {

            UIHelper.druckeHinweis("Es sind keine Buchungen vorhanden.");
            return;
        }

        System.out.printf(
                "%-8s | %-11s | %-7s | %-11s | %-16s | %-5s | %-8s | %-6s | %-12s | %-9s%n",
                "avigator.modell.Buchung", "avigator.modell.Passagier", "avigator.modell.Flug", "Route", "Abflug", "Sitz", "Klasse", "Koffer", "Preis", "Status"
        );

        UIHelper.druckeTrennlinie();

        for (Buchung buchung : buchungssystem.getBuchungen()) {

            Flug flug = buchung.getFlug();

            String passagier = buchung.getPassagier().passagierId()
                               + " " + buchung.getPassagier().name();

            System.out.printf(
                    "%-8s | %-11.11s | %-7s | %-11s | %-16s | %-5s | %-8s | %-6d | %-12s | %-9s%n",
                    buchung.getBuchungsnummer(),
                    passagier,
                    flug.getFlugnummer(),
                    flug.getStartFlughafen().iataCode() + " -> " + flug.getZielflughafen().iataCode(),
                    flug.getAbflugszeit().format(datumZeitFormatierer),
                    buchung.getSitzplatz().getSitzplatzNummer(),
                    buchung.getSitzplatz().getSitzklasse(),
                    buchung.getGepaeckinformation().getAnzahlKoffer(),
                    String.format("%.2f Euro", buchung.getGezahlterPreis()),
                    buchung.getBuchungsstatus()
            );
        }

        UIHelper.druckeTrennlinie();

        UIHelper.druckeHinweis("Anzahl der Buchungen: " + buchungssystem.getBuchungen().size());
    }

}
