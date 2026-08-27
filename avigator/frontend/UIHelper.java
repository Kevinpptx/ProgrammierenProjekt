package avigator.frontend;

/**
 * Stellt Hilfsmethoden für eine einheitliche Ausgabe der Konsolenoberfläche bereit.
 */
public class UIHelper {

    private static final int BREITE = 130;

    /**
     * Gibt eine zentrierte Überschrift zwischen zwei Trennlinien aus.
     *
     * @param text der auszugebende Überschriftentext
     */
    public static void druckeUeberschrift(String text) {

        druckeTrennlinie();
        System.out.println();

        System.out.println(zentriereText(text));

        System.out.println();
        druckeTrennlinie();
    }

    /**
     * Gibt eine Trennlinie über die festgelegte Breite der Konsolenoberfläche aus.
     */
    public static void druckeTrennlinie() {

        System.out.println("-".repeat(BREITE));
    }

    /**
     * Zentriert einen Text innerhalb der festgelegten Breite der Konsolenoberfläche.
     *
     * @param text der zu zentrierende Text
     * @return der mit führenden Leerzeichen zentrierte Text
     */
    private static String zentriereText(String text) {

        int leerzeichen = Math.max(0, (BREITE - text.length()) / 2);

        return " ".repeat(leerzeichen) + text;
    }

    /**
     * Gibt eine Fehlermeldung mit dem Präfix "Fehler:" aus.
     *
     * @param text der auszugebende Fehlertext
     */
    public static void druckeFehler(String text) {

        System.out.println("Fehler: " + text);
    }

    /**
     * Gibt einen Hinweis mit dem Präfix "Hinweis:" aus.
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
     * Gibt einen nummerierten Menüpunkt aus.
     *
     * @param nummer die Nummer des Menüpunkts
     * @param text   der Text des Menüpunkts
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