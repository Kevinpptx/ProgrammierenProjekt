import java.time.LocalDate;
import java.util.ArrayList;

public class UIKunde {

    //static Buchungssystem bs = new Buchungssystem();

    private final DatenHandler datenHandler; 
    private final Anwendungsdaten anwendungsdaten;
    private final Buchungssystem bs; 
    private final Verwaltungssystem vs;


    public UIKunde(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten dürfen nicht null sein.");
        }


        this.datenHandler = datenHandler; 
        this.anwendungsdaten = anwendungsdaten;
        this.bs = anwendungsdaten.getBuchungssystem();
        this.vs = anwendungsdaten.getVerwaltungssystem();
    }



public void kunde()
{
    System.out.println("-------------------Herzlich Wilkommen-------------------------");
    System.out.println("Drücken Sie die 1: Wenn sie neuer kunde sind ");
    System.out.println("Drücken Sie die 2: Wenn sie bereits kunde sind ");
    int auswahl = Manager.intscanner();
    switch (auswahl)
    {
        case 1:
            System.out.println("Bitte geben sie ihren Namen ein ");
            String name = Manager.Stringscanner();
            System.out.println("Bitte geben sie ihren E-Mail ein ");
            String mail = Manager.Stringscanner();

            Passagier p = bs.initialisierePassagier(name, mail);

            System.out.println( p.toString());
            datenHandler.speichere(anwendungsdaten);

            case 2:

                System.out.println("Passagierliste:");

                for (int i = 0; i < bs.getPassagiere().size(); i++) {
                    System.out.println(i + ": " + bs.getPassagiere().get(i));
                }

                System.out.println("Bitte wählen Sie einen Passagier über die ID:");
                int id = Manager.intscanner();

                Passagier ausgewaehlterPassagier = bs.getPassagiere().get(id);

                System.out.println("Sie haben ausgewählt:");
                System.out.println(ausgewaehlterPassagier);

                datenHandler.speichere(anwendungsdaten);
                hauptmanagerk(ausgewaehlterPassagier);


                break;

        default:
            System.out.println("Ungültige Eingabe.");
            kunde();
            break;

    }



}



    public void hauptmanagerk(Passagier passagier ) {
        // Flüge Buchen und Suchen
        // Sitzplatz auswählen, Gepäck anegeben --> im Buchungsmodus
        // Umbuchung auf andere Flüge vornehmen
        // Buchung stornieren


        System.out.println("-------------------Willkommen "+ passagier.getName() + " im Hauptmanager-------------------------");
        System.out.println("Wilkommen was möchten sie tun: ");
        System.out.println("Drücken Sie die 1: Um Flüge zu suchen und zu Buchen ");
        System.out.println("Drücken Sie die 2: Umbuchungen auf andere Flüge vornehmen");
        System.out.println("Drücken Sie die 3: Buchung stornieren");
        System.out.println("Drücken Sie die 4: schließen ");
        // An alle flüge mit auslastung denken!!!!!!!!!! anzeigen jetzt bei cedric

        int auswhal = Manager.intscanner();

        switch(auswhal) {
            case 1:
                flugsundb(passagier);
                break;
            case 2:

                break;

            case 3:

                break;

            case 4:
               // Manager.start();
                break;


        }



    }



    public void flugsundb(Passagier passagier ) {

        String start = "";
        System.out.println("-------------------Willkommen bei Flüge Buchen und Suchen-------------------------");

        System.out.println(vs.getFlughaefen().toString());

        System.out.print("Startflughafen name (leer lassen, wenn egal): ");
        start = Manager.Stringscanner();

        System.out.print("Zielflughafen name (leer lassen, wenn egal): ");
        String ziel = Manager.Stringscanner();

        System.out.print("Flugnummer (leer lassen, wenn unbekannt): ");
        String flugnummer = Manager.Stringscanner();

try {
    ArrayList<Flug> fluege = null;
    if (!ziel.isBlank()) {

         fluege = vs.sucheFluegeNachZiel(vs.getFlughafenNachName(ziel));
        System.out.println("Gefundene Flüge nach Ziel; " + fluege.toString());

        if (!start.isBlank()) {
            System.out.println("Gefundene Flüge nach Start und Ziel; " + vs.sucheFluegeNachRoute(vs.getFlughafenNachName(start), vs.getFlughafenNachName(ziel)).toString());
        }


        if (!flugnummer.isBlank()) {
            System.out.println("Gefundene Flüge nach Flugnummer; " + vs.sucheFluegeNachNummer(flugnummer).toString());
        }

    }

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("Bitte geben sie die Flugnumer ihres gewünschten Fluges ein: ");
    int index = Manager.intscanner();
    fluege.get(index).zeigeSitzplan();
    fluege.get(index).getFlugnummer();



    // System.out.println("Bitte geben sie das Datum ihres gewünschten Fluges ein: ");


   // vs.sucheFluegeNachNummer(flugnummer).getFreieSitzplaetze();



    System.out.println("Bitte geben sie die Sitzplatz nummer ein: ");
    String sitzplatz = Manager.Stringscanner();
    System.out.println("Bitte geben sie die Sitzklasse ein: ");



    // bs.buchungVornehmen(passagier,vs.sucheFluegeNachNummer(flugnummer), sitzplatz,  )

} catch (Exception e) {
    System.out.println( "Fehler "+ e.getMessage());
}
    }




}
