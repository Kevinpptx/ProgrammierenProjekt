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
    System.out.println("-------------------Herzlich Willkommen-------------------------");
    System.out.println("Drücken Sie die 1, wenn Sie neuer Kunde sind. ");
    System.out.println("Drücken Sie die 2, wenn Sie bereits Kunde sind. ");
    int auswahl = Manager.intscanner();
    switch (auswahl)
    {
        case 1:
            System.out.println("Bitte geben Sie Ihren Namen ein: ");
            String name = Manager.Stringscanner();
            System.out.println("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
            String mail = Manager.Stringscanner();

    while (true) {

        System.out.println("-------------------Herzlich Wilkommen-------------------------");
        System.out.println("Drücken Sie die 1: Wenn sie neuer kunde sind ");
        System.out.println("Drücken Sie die 2: Wenn sie bereits kunde sind ");
        System.out.println("Drücken Sie die 3: Wenn sie zurück wollen ");

        int auswahl = Manager.intscanner();

        switch (auswahl) {

            case 1:
                System.out.println("Bitte geben sie ihren Namen ein ");
                String name = Manager.Stringscanner();

                System.out.println("Bitte geben sie ihren E-Mail ein ");
                String mail = Manager.Stringscanner();

                try {
                    Passagier passagier = bs.initialisierePassagier(name, mail);

                    datenHandler.speichere(anwendungsdaten);

                    System.out.println("Kunde erfolgreich angelegt:");
                    System.out.println(passagier);

                    hauptmanagerk(passagier);
                    return;

                } catch (Exception e) {
                    System.out.println("Fehler: " + e.getMessage());
                    break;
                }

            case 2:

                if (bs.getPassagiere().isEmpty()) {
                    System.out.println("Es sind noch keine Kunden vorhanden.");
                    break;
                }

                System.out.println("Passagierliste:");

                for (int i = 0; i < bs.getPassagiere().size(); i++) {
                    System.out.println("ID: " + i + ": " + bs.getPassagiere().get(i));
                }

                System.out.println("Bitte wählen Sie einen Passagier über die ID:");
                int id = Manager.intscanner();


                if (id < 0 || id >= bs.getPassagiere().size()) {
                    System.out.println("Diese Passagier-ID existiert nicht.");
                    break;
                }

                Passagier ausgewaehlterPassagier = bs.getPassagiere().get(id);


                System.out.println("Sie haben ausgewählt:");
                System.out.println(ausgewaehlterPassagier);

                datenHandler.speichere(anwendungsdaten);
                hauptmanagerk(ausgewaehlterPassagier);
                break;


            case 3:
                return;

            default:
                System.out.println("Ungültige Eingabe.\n");
                break;

        }
    }


}



    public void hauptmanagerk(Passagier passagier ) {
        // Flüge Buchen und Suchen
        // Sitzplatz auswählen, Gepäck anegeben --> im Buchungsmodus
        // Umbuchung auf andere Flüge vornehmen
        // Buchung stornieren

    while (true) {

        System.out.println("-------------------Willkommen "+ passagier.getName() + " im Hauptmanager-------------------------");
        System.out.println("Willkommen, was möchten Sie tun?: ");
        System.out.println("Drücken Sie die 1, um Flüge zu suchen und zu buchen.");
        System.out.println("Drücken Sie die 2, um eine Umbuchungen auf einen anderen Flug vorzunehmen.");
        System.out.println("Drücken Sie die 3, um eine Buchung stornieren.");
        System.out.println("Drücken Sie die 4, um Buchungen anzuzeigen.");
        System.out.println("Drücken Sie die 5, um zum Start zu gelangen.");

        int auswahl = Manager.intscanner();

        switch(auswahl) {
            case 1:
                flugsundb(passagier);
                break;
            case 2:
                umbuchen(passagier);
                break;

            case 3:
                stornieren(passagier);
                break;

            case 4:
                buchunganzeigen(passagier);
                break;
            case 5:
                return;

            default:
                System.out.println("Ungültige Eingabe.");
                break;

        }

    }

    }



    public void flugsundb(Passagier passagier ) {


        System.out.println("-------------------Willkommen im Bereich für Flugsuche und Buchung-------------------------");

        System.out.println(vs.getFlughaefen().toString());

try {
    ArrayList<Flug> fluege = new ArrayList<>();

    System.out.print("Name Zielflughafen (optional): ");
    String ziel = Manager.Stringscanner();

    System.out.print("Name Startflughafen (optional): ");
     String start = Manager.Stringscanner();

    System.out.print("Flugnummer (optional): ");
    String flugnummer = Manager.Stringscanner();

    if (!flugnummer.isBlank()) {
        fluege.addAll(vs.sucheFluegeNachNummer(flugnummer));

    } else if (!start.isBlank() && !ziel.isBlank()) {
        fluege.addAll(
                vs.sucheFluegeNachRoute(
                        vs.getFlughafenNachName(start),
                        vs.getFlughafenNachName(ziel)
                )
        );

    } else if (!ziel.isBlank()) {
        fluege.addAll(
                vs.sucheFluegeNachZiel(
                        vs.getFlughafenNachName(ziel)
                )
        );
    }

    System.out.println(
            "--------------------------------------------------------------------------------"
    );

    if (fluege.isEmpty()) {
        System.out.println("Keine Flüge gefunden.\n");
        return;
    } else {
        for (int i = 0; i < fluege.size(); i++) {
            System.out.println("Flug " + (i) + ": " + fluege.get(i));

        }
    }

    System.out.println("Bitte geben Sie die Listennummer Ihres gewünschten Fluges ein: ");
    int index = Manager.intscanner();
    fluege.get(index).zeigeSitzplan();

    System.out.println("Bitte geben Sie die Sitzplatznummer ein: ");
    String sitzplatz = Manager.Stringscanner();

    System.out.println("Sitzklasse wählen:");
    System.out.println("1 - Economy");
    System.out.println("2 - Business");

    int auswahl = Manager.intscanner();
    Sitzklasse sitzklasse;
    switch (auswahl) {
        case 1:
            sitzklasse = Sitzklasse.ECONOMY;
            break;
        case 2:
            sitzklasse = Sitzklasse.BUSINESS;
            break;
        default:
            System.out.println("Ungültige Eingabe.");
            return;
    }


    System.out.println("Bitte geben Sie die Anzahl der Koffer ein, die Sie aufgeben möchten: ");
    int  koffer = Manager.intscanner();


    bs.buchungVornehmen(passagier,fluege.get(index), sitzplatz, koffer, sitzklasse );
    datenHandler.speichere(anwendungsdaten);


} catch (Exception e) {
    System.out.println( "Fehler "+ e.getMessage());
}
    }


    public void umbuchen(Passagier passagier)
    {


        System.out.println("-------------------Willkommen im Umbuchungsbereich-------------------------");
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }

        System.out.println("Bitte Buchungsnummer für Umbuchung angeben:");

        String nummer = Manager.Stringscanner();


        System.out.println(vs.getFlughaefen().toString());

        try {
            ArrayList<Flug> fluege = new ArrayList<>();

            System.out.print("Name Zielflughafen (optional): ");
            String ziel = Manager.Stringscanner();

            System.out.print("Name Startflughafen (optional): ");
            String start = Manager.Stringscanner();

            System.out.print("Flugnummer (optional): ");
            String flugnummer = Manager.Stringscanner();

            if (!flugnummer.isBlank()) {
                fluege.addAll(vs.sucheFluegeNachNummer(flugnummer));

            } else if (!start.isBlank() && !ziel.isBlank()) {
                fluege.addAll(
                        vs.sucheFluegeNachRoute(
                                vs.getFlughafenNachName(start),
                                vs.getFlughafenNachName(ziel)
                        )
                );

            } else if (!ziel.isBlank()) {
                fluege.addAll(
                        vs.sucheFluegeNachZiel(
                                vs.getFlughafenNachName(ziel)
                        )
                );
            }

            System.out.println(
                    "--------------------------------------------------------------------------------"
            );

            if (fluege.isEmpty()) {
                System.out.println("Keine Flüge gefunden.\n");
                return;
            } else {
                for (int i = 0; i < fluege.size(); i++) {
                    System.out.println("Flug " + (i) + ": " + fluege.get(i));
                }
            }

            System.out.println("Bitte geben Sie die Listennummer Ihres gewünschten Fluges ein: ");
            int index = Manager.intscanner();
            fluege.get(index).zeigeSitzplan();

            System.out.println("Bitte geben Sie die gewünschte Sitzplatznummer ein: ");
            String sitzplatz = Manager.Stringscanner();

            System.out.println("Sitzklasse wählen:");
            System.out.println("1 - Economy");
            System.out.println("2 - Business");

            int auswahl = Manager.intscanner();
            Sitzklasse sitzklasse;
            switch (auswahl) {
                case 1:
                    sitzklasse = Sitzklasse.ECONOMY;
                    break;
                case 2:
                    sitzklasse = Sitzklasse.BUSINESS;
                    break;
                default:
                    System.out.println("Ungültige Eingabe.\n");
                    return;
            }




            bs.umbuchen( bs.sucheBuchungNachNummer(nummer) ,fluege.get(index), sitzplatz, sitzklasse );


            datenHandler.speichere(anwendungsdaten);



        } catch (Exception e) {
            System.out.println( "Fehler "+ e.getMessage());
        }



    }



    public void stornieren(Passagier passagier)
    {
        System.out.println("-------------------Willkommen im Stornierungsbereich-------------------------");
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }

        System.out.println("Bitte Buchungsnummer für Stornierung angeben:");

        String nummer = Manager.Stringscanner();

        bs.stornieren(bs.sucheBuchungNachNummer(nummer));
        datenHandler.speichere(anwendungsdaten);


    }


    public void buchunganzeigen(Passagier passagier)
    {
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }
    }



}
