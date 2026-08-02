import java.util.Scanner;



public class Manager {

    private final DatenHandler datenHandler;
    private final Anwendungsdaten anwendungsdaten;
    private final UIKunde uiKunde;
    private final UIMitarbeiter uiMitarbeiter;

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Erzeugt einen Manager mit Zugriff auf die Anwendungsdaten
     * und den DatenHandler.
     *
     * @param datenHandler Handler zum Speichern der Anwendungsdaten
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

// Start Methode um den nutzer festzulegen
    public void start() {


        while (true) {


           ConsoleImagePrinter.printResource("/Aviagator.png", 50);



                System.out.print("\n --------------------------------------------");
                System.out.println("\n Herzlich Wilkommen zum Avigator");
                System.out.print("--------------------------------------------");
                System.out.print("\n Als welche Art von Benutzer möchten sie das System nutzen?");
                System.out.print("\n Drücken sie die 1. Um sich als Admin anzumelden");
                System.out.println("\n Drücken sie die 2. Um sich als Kunde anzumelden");
                System.out.println(" Drücken sie die 3. Um das Programm zu beenden");
                System.out.println("--------------------------------------------");

                int auswahl = intscanner();
                    switch (auswahl) {
                    case 1:
                        uiMitarbeiter.loggin();
                        break;
                    case 2:
                        uiKunde.kunde();
                         break;
                    case 3:
                     datenHandler.speichere(anwendungsdaten);
                     return;

                    default:
                    System.out.print("Falsche eingabe ");
                    break;
    }
}
    }

    // Scanner um nach den richtigen werten zu filtern
    public static int intscanner() {

        try {

            String auswahl = SCANNER.nextLine().trim();
            return  Integer.parseInt(auswahl);
        } catch (NumberFormatException  e) {

            return -1;
        }
    }

    // Scanner um nach den richtigen werten zu filtern
    public static String Stringscanner() {

        try {

            String zeichen = SCANNER.nextLine().trim();
            return zeichen;
        } catch (InputMismatchException e) {
            System.out.println("Kein gültiger Wert! \n");
            return null;
        }
    }

    public static double doublescanner() {

        try {

            String zahl = SCANNER.nextLine().trim().replace(",", ".");

            return Double.parseDouble(zahl);

        } catch (NumberFormatException  e) {

            return -1.1;
        }
    }


}