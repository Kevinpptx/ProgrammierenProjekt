public class UIKunde {

    // Seine Kunden sachen angeben wie name und E-mail

public static void kunde()
{
    System.out.println("-------------------Herzlich Wilkommen-------------------------");
    System.out.println("Bitte geben sie ihren Namen ein ");
    String name = Manager.Stringscanner();
    System.out.println("Bitte geben sie ihren E-Mail ein ");
    String mail = Manager.Stringscanner();

}



    public static void hauptmanagerk() {
        // Flüge Buchen und Suchen
        // Sitzplatz auswählen, Gepäck anegeben --> im Buchungsmodus
        // Umbuchung auf andere Flüge vornehmen
        // Buchung stornieren


        System.out.println("-------------------Willkommen im Hauptmanager-------------------------");
        System.out.println("Wilkommen  was möchten sie tun: ");
        System.out.println("Drücken Sie die 1: Um Flüge zu suchen und zu Buchen ");
        System.out.println("Drücken Sie die 2 Um Flugzeuge einer FlugeselschaftFlotte hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 3 Um Flughäfen hinzuzufügen oder zu entfernen");
        System.out.println("Drücken Sie die 4 Um einen Flug anzulegen ");
        // An alle flüge mit auslastung denken!!!!!!!!!! anzeigen jetzt bei cedric

        int auswhal = Manager.intscanner();

        switch(auswhal) {
            case 1:

                break;
            case 2:

                break;

            case 3:

                break;

            case 4:

                break;


        }



    }


}
