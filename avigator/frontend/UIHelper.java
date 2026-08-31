package avigator.frontend;

/**
 * Stellt Hilfsmethoden fuer eine einheitliche Ausgabe der Konsolenoberflaeche bereit.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class UIHelper {

    /**
     * Einheitliche Breite der Konsolenausgabe.
     */
    private static final int BREITE = 130;

    /**
     * Erzeugt eine neue Instanz der Hilfsklasse.
     */
    public UIHelper() {

    }

    /**
     * Gibt eine zentrierte ueberschrift zwischen zwei Trennlinien aus.
     *
     * @param text der auszugebende ueberschriftentext
     */
    public static void druckeUeberschrift(String text) {

        druckeTrennlinie();
        System.out.println();

        System.out.println(zentriereText(text));

        System.out.println();
        druckeTrennlinie();
    }

    /**
     * Gibt eine Trennlinie ueber die festgelegte Breite der Konsolenoberflaeche aus.
     */
    public static void druckeTrennlinie() {

        System.out.println("-".repeat(BREITE));
    }

    /**
     * Zentriert einen Text innerhalb der festgelegten Breite der Konsolenoberflaeche.
     *
     * @param text der zu zentrierende Text
     * @return der mit fuehrenden Leerzeichen zentrierte Text
     */
    private static String zentriereText(String text) {

        int leerzeichen = Math.max(0, (BREITE - text.length()) / 2);

        return " ".repeat(leerzeichen) + text;
    }

    /**
     * Gibt eine Fehlermeldung mit dem Praefix "Fehler:" aus.
     *
     * @param text der auszugebende Fehlertext
     */
    public static void druckeFehler(String text) {

        System.out.println("Fehler: " + text);
    }

    /**
     * Gibt einen Hinweis mit dem Praefix "Hinweis:" aus.
     *
     * @param text der auszugebende Hinweistext
     */
    public static void druckeHinweis(String text) {

        System.out.println("Hinweis: " + text);
    }

    /**
     * Gibt nach einer Leerzeile eine Erfolgsmeldung aus.
     *
     * @param text der auszugebende Erfolgstext
     */
    public static void druckeErfolg(String text) {

        System.out.println();
        System.out.println(text);
    }

    /**
     * Gibt einen nummerierten Menuepunkt aus.
     *
     * @param nummer die Nummer des Menuepunkts
     * @param text   der Text des Menuepunkts
     */
    public static void druckeMenuepunkt(int nummer, String text) {

        System.out.println(nummer + " - " + text);
    }

    /**
     * Gibt eine Eingabeaufforderung auf der Konsole aus.
     *
     * @param text der Text der Eingabeaufforderung
     */
    public static void druckeEingabeaufforderung(String text) {

        System.out.println(text);
    }
}
