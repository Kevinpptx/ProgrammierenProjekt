import java.util.Scanner;

/**
 * Die Klasse {@code Manager} steuert den Programmablauf des Avigator-Systems. Sie zeigt das Hauptmenü an und leitet den
 * Benutzer abhängig von seiner Auswahl zum Mitarbeiter- oder Kundenbereich weiter.
 * <p>
 * Außerdem stellt die Klasse Methoden zur Verfügung, mit denen Eingaben über die Konsole eingelesen und überprüft
 * werden können.
 *
 * @author Lars Pfeiffer
 * @version 1.0
 */
public class Manager {

    /**
     * DatenHandler zum Speichern der Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthält die aktuell verwendeten Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * Benutzeroberfläche für Kunden.
     */
    private final UIKunde uiKunde;

    /**
     * Benutzeroberfläche für Mitarbeiter.
     */
    private final UIMitarbeiter uiMitarbeiter;

    /**
     * Zentraler Scanner zum Einlesen von Konsoleneingaben.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Erzeugt einen Manager mit Zugriff auf die Anwendungsdaten und den DatenHandler.
     *
     * @param datenHandler    Handler zum Speichern der Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte Anwendungsdaten
     */
    public Manager(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten dürfen nicht null sein.");
        }

        this.datenHandler = datenHandler;
        this.anwendungsdaten = anwendungsdaten;
        this.uiKunde = new UIKunde(datenHandler, anwendungsdaten);
        this.uiMitarbeiter = new UIMitarbeiter(datenHandler, anwendungsdaten);
    }

    /**
     * Startet das Avigator-System und zeigt das Hauptmenü an.
     * <p>
     * Der Benutzer kann sich als Administrator oder Kunde anmelden. Außerdem kann das Programm beendet werden. Beim
     * Beenden werden die aktuellen Anwendungsdaten gespeichert.
     * <p>
     * Das Menü wird so lange angezeigt, bis der Benutzer das Programm über die entsprechende Auswahl beendet.
     */
    public void start() {

        while (true) {

            UIHelper.druckeUeberschrift("Herzlich Willkommen beim Avigator!");

            UIHelper.druckeEingabeaufforderung("Als welche Art von Benutzer möchten Sie das System nutzen?");

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
                    UIHelper.druckeFehler("Ungültige Eingabe! Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }
    }

    /**
     * Liest eine Ganzzahl über die Konsole ein.
     * <p>
     * Die Eingabe wird als Text eingelesen und anschließend in einen {@code int}-Wert umgewandelt. Bei einer ungültigen
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
                UIHelper.druckeFehler("Ungültige Eingabe! Bitte geben Sie eine Ganzzahl ein.");
            }

        }
    }

    /**
     * Liest eine Zeichenkette über die Konsole ein.
     * <p>
     * Leerzeichen am Anfang und Ende der Eingabe werden entfernt.
     *
     * @return die eingegebene Zeichenkette
     */
    public static String stringscanner() {

        return scanner.nextLine().trim();
    }

    /**
     * Liest eine Dezimalzahl über die Konsole ein.
     * <p>
     * Die Methode akzeptiert sowohl einen Punkt als auch ein Komma als Dezimaltrennzeichen. Ein Komma wird vor der
     * Umwandlung durch einen Punkt ersetzt. Bei einer ungültigen Eingabe wird eine Fehlermeldung ausgegeben und die
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
                UIHelper.druckeFehler("Ungültige Eingabe! Bitte geben Sie eine Dezimalzahl ein.");
            }
        }
    }
}
