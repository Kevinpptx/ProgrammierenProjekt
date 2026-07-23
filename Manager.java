import java.util.InputMismatchException;
import java.util.Scanner;



public class Manager {

// Start Methode um den nutzer festzulegen
    public static void start() {
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

        if (auswahl == 1) {
            System.out.print("Admin: ");
            UIMitarbeiter.loggin();
        } else if (auswahl == 2) {
            System.out.print("Kunden: ");
        } else if (auswahl == 3) {
            System.exit(0);
        } else {
            System.out.print("Falsche eingabe ");
            start();
        }
        start();
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
            return "";
        }
    }

}