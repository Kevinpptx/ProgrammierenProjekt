import java.util.InputMismatchException;
import java.util.Scanner;



public class Manager {

    private final DatenHandler datenHandler;
    private final Anwendungsdaten anwendungsdaten;
    private final UIKunde uiKunde;
    private final UIMitarbeiter uiMitarbeiter;

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

        int i = 0;
        while ( i < 1) {

        Scanner sc = new Scanner(System.in);
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
            System.out.print("Admin: ");
            uiMitarbeiter.loggin();
            break;
        case 2:
            System.out.print("Kunden: ");
            
            uiKunde.kunde();
            break;
            case 3:
                datenHandler.speichere(anwendungsdaten);
                System.exit(0);
        default:
            System.out.print("Falsche eingabe ");
            start();
    }
}
    }

    // Scanner um nach den richtigen werten zu filtern
    public static int intscanner() {

        try {
            Scanner sc = new Scanner(System.in);
            int auswahl = sc.nextInt();
            return auswahl;
        } catch (InputMismatchException e) {
            System.out.println("kein gültiger wert");
            return -1;
        }
    }

    // Scanner um nach den richtigen werten zu filtern
    public static String Stringscanner() {

        try {
            Scanner sc = new Scanner(System.in);
            String zeichen = sc.nextLine();
            return zeichen;
        } catch (InputMismatchException e) {
            System.out.println("kein gültiger wert");
            return null;
        }
    }

    public static double doublescanner() {

        try {
            Scanner sc = new Scanner(System.in);
            double zahl = sc.nextDouble();
            return zahl;
        } catch (InputMismatchException e) {
            System.out.println("kein gültiger wert");
            return -1.1;
        }
    }


}