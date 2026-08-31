package avigator.verwaltung;

import avigator.frontend.UIHelper;
import avigator.frontend.UIKunde;
import avigator.frontend.UIMitarbeiter;

import java.util.Scanner;

/**
 * Die Klasse {@code Manager} steuert den Programmablauf des Avigator-Systems. Sie zeigt das Hauptmenue an und leitet den
 * Benutzer abhaengig von seiner Auswahl zum Mitarbeiter- oder Kundenbereich weiter.
 * <p>
 * Außerdem stellt die Klasse Methoden zur Verfuegung, mit denen Eingaben ueber die Konsole eingelesen und ueberprueft
 * werden koennen.
 *
 * @author Lars Pfeiffer
 * @version 1.1
 */
public class Manager {

    /**
     * DatenHandler zum Speichern der Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthaelt die aktuell verwendeten Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * Benutzeroberflaeche fuer Kunden.
     */
    private final UIKunde uiKunde;

    /**
     * Benutzeroberflaeche fuer Mitarbeiter.
     */
    private final UIMitarbeiter uiMitarbeiter;

    /**
     * Zentraler Scanner zum Einlesen von Konsoleneingaben.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Erzeugt einen Manager mit Zugriff auf die Anwendungsdaten und den DatenHandler.
     *
     * @param datenHandler    der Handler zum Speichern der Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte Anwendungsdaten
     * @throws IllegalArgumentException wenn der DatenHandler oder die Anwendungsdaten {@code null} sind
     */
    public Manager(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten duerfen nicht null sein.");
        }

        this.datenHandler = datenHandler;
        this.anwendungsdaten = anwendungsdaten;
        this.uiKunde = new UIKunde(datenHandler, anwendungsdaten);
        this.uiMitarbeiter = new UIMitarbeiter(datenHandler, anwendungsdaten);
    }

    /**
     * Startet das Avigator-System und zeigt das Hauptmenue an.
     * <p>
     * Der Benutzer kann sich als Administrator oder Kunde anmelden. Außerdem kann das Programm beendet werden. Beim
     * Beenden werden die aktuellen Anwendungsdaten gespeichert.
     * <p>
     * Das Menue wird so lange angezeigt, bis der Benutzer das Programm ueber die entsprechende Auswahl beendet.
     */
    public void start() {

        while (true) {

            UIHelper.druckeUeberschrift("Herzlich Willkommen beim Avigator!");

            UIHelper.druckeEingabeaufforderung("Als welche Art von Benutzer moechten Sie das System nutzen?");

            UIHelper.druckeMenuepunkt(1, "Admin");
            UIHelper.druckeMenuepunkt(2, "Kunde");
            UIHelper.druckeMenuepunkt(0, "Programm beenden");

            UIHelper.druckeTrennlinie();

            int auswahl = intscanner();

            switch (auswahl) {
                case 1:
                    uiMitarbeiter.login();
                    break;
                case 2:
                    uiKunde.kunde();
                    break;
                case 0:
                    datenHandler.speichere(anwendungsdaten);
                    return;

                default:
                    UIHelper.druckeFehler("Ungueltige Eingabe! Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }
    }

    /**
     * Liest eine Ganzzahl ueber die Konsole ein.
     * <p>
     * Die Eingabe wird als Text eingelesen und anschließend in einen {@code int}-Wert umgewandelt. Bei einer ungueltigen
     * Eingabe wird eine Fehlermeldung ausgegeben und die Eingabe erneut abgefragt.
     *
     * @return die eingegebene Ganzzahl
     */
    public static int intscanner() {

        while (true) {

            String auswahl = scanner.nextLine().trim();

            try {
                return Integer.parseInt(auswahl);
            } catch (NumberFormatException e) {
                UIHelper.druckeFehler("Ungueltige Eingabe! Bitte geben Sie eine Ganzzahl ein.");
            }

        }
    }

    /**
     * Liest eine Zeichenkette ueber die Konsole ein.
     * <p>
     * Leerzeichen am Anfang und Ende der Eingabe werden entfernt.
     *
     * @return die eingegebene Zeichenkette
     */
    public static String stringscanner() {

        return scanner.nextLine().trim();
    }

    /**
     * Liest eine Dezimalzahl ueber die Konsole ein.
     * <p>
     * Die Methode akzeptiert sowohl einen Punkt als auch ein Komma als Dezimaltrennzeichen. Ein Komma wird vor der
     * Umwandlung durch einen Punkt ersetzt. Bei einer ungueltigen Eingabe wird eine Fehlermeldung ausgegeben und die
     * Eingabe erneut abgefragt.
     *
     * @return die eingegebene Dezimalzahl
     */
    public static double doublescanner() {

        while (true) {

            String dezimalzahl = scanner.nextLine().trim().replace(",", ".");

            try {

                return Double.parseDouble(dezimalzahl);

            } catch (NumberFormatException e) {
                UIHelper.druckeFehler("Ungueltige Eingabe! Bitte geben Sie eine Dezimalzahl ein.");
            }
        }
    }
}
