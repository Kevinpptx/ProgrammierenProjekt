import java.util.ArrayList;

public class UIMitarbeiter {


    public static void loggin()
    {
        int passwort = 1234;
        int i;

        for ( i = 3; i >= 0; i--) {

        System.out.println("Geben Sie ein Passwort ein:");


       int eingabe = Manager.intscanner();

        if (passwort == eingabe )
        {
            angemeldet();
        } else if (i == 0) {
            System.out.println("Keine eingaben mehr übrig");
            System.exit(0);
        }
        else {
            System.out.println("Sie haben noch " + i + " Versuche");
        }

    }
    }

    public  static void angemeldet()
    {
       System.out.println("--------------------------------------------");
       System.out.println("Wilkommen Admin was möchten sie tun: ");
       System.out.println("Drücken sie die 1: Um Fluggeselschaften anzulegen");
       System.out.println("Drücken sie die 2 Um Flugzeuge einer Flugeselschaft Flotte hinzuzufügen");

       int auswhal = Manager.intscanner();
       if (auswhal == 1)
       {
           Fluggeselschaftanlegen();
       }
       if (auswhal == 2)
       {
           FlugzeugderFlotteHinzufügen();
       }
       angemeldet();
    }


// Flug geselschaften anlegen und die Flotte
    public static ArrayList<Fluggesellschaft> Fluggeselschaften = new ArrayList<>();
    //Fluggeselschaftanlegen
    public static void Fluggeselschaftanlegen()
    {
        System.out.println("-----------------------------------------");
        System.out.println("Flugeselschaft name: ");
        String name = Manager.Stringscanner();
        System.out.println("Flugeselschaft airlineCode : ");
        String airlinecode = Manager.Stringscanner();


        Fluggesellschaft FluggeselschaftAnlegen  = new Fluggesellschaft(name, airlinecode);

        Fluggeselschaften.add(FluggeselschaftAnlegen);
        System.out.println("Alle bisherrigen Flugeselschaften: ");

        for (int i = 0; i < Fluggeselschaften.size(); i++) {
        System.out.println("Fluggeselschaft: " + i + " " + Fluggeselschaften.get(i).toString());

        }

        // Möchten sie Flugzeuge der Flotte hinzufügen 1, entfernen 2, zurück 3,

        System.out.println("-----------------------------------------");
        System.out.println("Drücken sie die 1 um Flugzeuge der Flotte hinzuzufügen");
        System.out.println("Drücken sie die 2 um Flugzeuge der Flotte zuenfernen");
        System.out.println("Drücken sie die 3 um zurück zugelangen");
        System.out.println("-----------------------------------------");
        int antwort = Manager.intscanner();
        if (antwort == 1)
        {
            FlugzeugderFlotteHinzufügen();
        }




        angemeldet();

    }


    //FlugzeugderFlotteHinzufügen

    public static void FlugzeugderFlotteHinzufügen()
    {

        for (int i = 0; i < Fluggeselschaften.size(); i++) {
            System.out.println("Fluggeselschaft: " + i + " " + Fluggeselschaften.get(i).toString());
        }

        // Auswahl der Fluggeselschaft --> Anzeige der ausgewähltenflugeselschaft
        // dann die einzelnen codes eingeben namen usw....
        // Dann am ende ausgabe   System.out.println("Fluggeselschaft: " + i + " " + Fluggeselschaften.get(i).toString()); mit der ausgewählten flotte



    //    Flugzeug test = new Flugzeug("3223", "A337", 16, "2", "5");
        // Fluggeselschaften.get(1).fuegeFlugzeugHinzu(test);
    }




    /*
    // Flüge anlegen
    public static void fluganlegen()
    {
    String name = Fluggeselschaften.get(0).getName();


        Flug lol = new Flug("121221", name , "Groß", "Frankfurt", "Palma", 2000, 2100, 12 );
    }

     */

}
