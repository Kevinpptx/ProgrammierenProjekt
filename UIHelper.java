public class UIHelper {

    private static final int BREITE = 130;

    public static void druckeUeberschrift(String text) {
        druckeTrennlinie();
        System.out.println();

        System.out.println(zentriereText(text));

        System.out.println();
        druckeTrennlinie();
    }

    public static void druckeTrennlinie() {
        System.out.println("-".repeat(BREITE));
    }

    public static String zentriereText(String text) {
        int leerzeichen = Math.max(0, (BREITE - text.length()) / 2);
        return " ".repeat(leerzeichen) + text;
    }

    public static void druckeFehler(String text) {
        System.out.println("Fehler: " + text);
    }

    public static void druckeHinweis(String text) {
        System.out.println("Hinweis: " + text);
    }

    public static void druckeErfolg(String text) {
        System.out.println();
        System.out.println(text);
    }

    public static void druckeMenuepunkt(int nummer, String text) {
        System.out.println(nummer + " - " + text);
    }

    public static void druckeEingabeaufforderung(String text) {
        System.out.println(text);
    }
}