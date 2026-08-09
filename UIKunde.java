import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code UIKunde} stellt die Konsolenoberfläche für Kunden bereit.
 *
 * Kunden können sich neu registrieren oder ein bereits vorhandenes
 * Kundenkonto auswählen. Nach der Anmeldung können sie Flüge suchen und
 * buchen, bestehende Buchungen umbuchen oder stornieren und ihre Buchungen
 * anzeigen lassen.
 *
 * Änderungen an den Daten werden mithilfe des {@link DatenHandler}
 * gespeichert.
 *
 * @author Lars Pfeiffer
 * @version 1.0
 */

public class UIKunde {

    //static Buchungssystem bs = new Buchungssystem();

    /**
     * DatenHandler zum dauerhaften Speichern der Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthält die aktuell verwendeten Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * Buchungssystem zur Verwaltung von Passagieren und Buchungen.
     */
    private final Buchungssystem bs;

    /**
     * Verwaltungssystem zur Verwaltung und Suche von Flügen und Flughäfen.
     */
    private final Verwaltungssystem vs;


    /**
     *Erstellt eine neue Kundenoberfläche.
     *Das Buchungs- und Verwaltungssystem werden aus den übergebenen
     * Anwendungsdaten übernommen.
     *
     * @param datenHandler Handler zum Speichern der Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte Anwendungsdaten
     * @throws IllegalArgumentException wenn der DatenHandler oder dieAnwendungsdaten {@code null} sind
     */
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


    /**
     * Zeigt das Anmeldemenü für Kunden an.
     *
     * Der Benutzer kann einen neuen Kunden anlegen, einen bestehenden
     * Passagier über dessen ID auswählen oder zum vorherigen Menü
     * zurückkehren.
     *
     * Nach einer erfolgreichen Anmeldung wird das Kundenhauptmenü geöffnet.
     */
public void kunde()
{


    while (true) {

        System.out.println("-------------------Herzlich Wilkommen-------------------------");
        System.out.println("Drücken Sie die 1: Wenn sie neuer kunde sind ");
        System.out.println("Drücken Sie die 2: Wenn sie bereits kunde sind ");
        System.out.println("Drücken Sie die 3: Wenn sie zurück wollen ");

        int auswahl = Manager.intscanner();

        switch (auswahl) {

            case 1:
                System.out.println("Bitte geben sie ihren Namen ein ");
                String name = Manager.stringscanner();

                System.out.println("Bitte geben sie ihren E-Mail ein ");
                String mail = Manager.stringscanner();

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

                for (Passagier passagier : bs.getPassagiere()) {
                    System.out.println(passagier.toString());
            }

                System.out.println("\nBitte wählen Sie einen Passagier über die ID:");
                String id = Manager.stringscanner();

                Passagier ausgewaehlterPassagier = null;


                for (Passagier passagier : bs.getPassagiere()) {
                    if (passagier.getPassagierId().equalsIgnoreCase(id)) {
                        ausgewaehlterPassagier = passagier;
                        break;
                    }
                }



            if (ausgewaehlterPassagier == null){
                System.out.println("Diese Passagier-ID existiert nicht.");
                break;
            }


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


    /**
     * Zeigt das Kundenhauptmenü für einen angemeldeten Passagier an.
     *
     * Von diesem Menü aus kann der Kunde Flüge suchen und buchen,
     * bestehende Buchungen umbuchen oder stornieren sowie seine
     * aktuellen Buchungen anzeigen lassen und die Anzahl der Gepäckstücke anpassen.
     *
     * @param passagier der aktuell angemeldete Passagier
     */
    public void hauptmanagerk(Passagier passagier ) {


    while (true) {

        System.out.println("-------------------Willkommen "+ passagier.getName() + " im Hauptmanager-------------------------");
        System.out.println("Willkommen, was möchten Sie tun?: ");
        System.out.println("Drücken Sie die 1, um Flüge zu suchen und zu buchen.");
        System.out.println("Drücken Sie die 2, um eine Umbuchung auf einen anderen Flug vorzunehmen.");
        System.out.println("Drücken Sie die 3, um eine Buchung zu stornieren.");
        System.out.println("Drücken Sie die 4, um Buchungen anzuzeigen.");
        System.out.println("Drücken Sie die 5, um das Gepäck ihrer Buchung anzupassen.");
        System.out.println("Drücken Sie die 6, um zum Start zu gelangen.");

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
                gepaeckAendern(passagier);
                break;
            case 6:
                return;

            default:
                System.out.println("Ungültige Eingabe.");
                break;

        }

    }

    }

    /**
     * Ermöglicht einem Passagier die Suche und Buchung eines Fluges.
     *
     * Vor der Suche werden veraltete Flüge entfernt und die betroffenen
     * Buchungen aktualisiert. Danach kann nach Flugnummer, Flugroute oder
     * Zielflughafen gesucht werden.
     *
     * Der Kunde wählt anschließend einen Flug, einen Sitzplatz und die
     * Anzahl der aufzugebenden Koffer aus. Nach erfolgreicher Buchung
     * werden die Anwendungsdaten gespeichert.
     *
     * @param passagier der Passagier, für den der Flug gebucht wird
     */

    public void flugsundb(Passagier passagier ) {


        System.out.println("-------------------Willkommen im Bereich für Flugsuche und Buchung-------------------------");

        System.out.println(vs.getFlughaefen().toString());

        // alte Fluege loeschen, danach die Buchungsstatusse der betroffenen Fluege auf VERGANGEN aendern
        ArrayList<String> betroffeneFlugnummern = vs.alteFluegeLoeschen();

        // Wenn welche geloescht wurden, durch alle Buchungen iterieren und entsprechen abaendern
        if (!betroffeneFlugnummern.isEmpty()) {

            List<Buchung> buchungen = bs.getBuchungen();

            for (String flugnummer : betroffeneFlugnummern) {

                for (Buchung buchung : buchungen) {

                    Flug flug = buchung.getFlug();

                    // zweiter Check verhindert, dass Buchungen mit selber Flugnummer an anderen Tagen geloescht werden
                    if (flug.getFlugnummer().equals(flugnummer)
                        && flug.getAbflugszeit().toLocalDate().equals(LocalDate.now())) {

                    buchung.setBuchungsstatus(Buchungsstatus.VERGANGEN);
                }
                }
            }
        }

try {
    ArrayList<Flug> fluege = new ArrayList<>();

    System.out.print("Name Zielflughafen (optional): ");
    String ziel = Manager.stringscanner();

    System.out.print("Name Startflughafen (optional): ");
     String start = Manager.stringscanner();

    System.out.print("Flugnummer (optional): ");
    String flugnummer = Manager.stringscanner();

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

    int flugIndex = -1;
    Flug flug = null;

    while (flugIndex == -1) {

        System.out.println("Bitte geben Sie die Listennummer Ihres gewünschten Fluges ein: ");
        flugIndex = Manager.intscanner();

        try {
            flug = fluege.get(flugIndex);
        } catch (Exception e) {
            System.out.println("Listennummer existiert nicht. Bitte geben Sie eine gültige Nummer ein!");
            flugIndex = -1;
        }
    }

    flug.zeigeSitzplan();

    String sitzplatz = "";

    while (sitzplatz.isEmpty() || flug.findeSitzplatz(sitzplatz) == null) {
        System.out.println("Bitte geben Sie die Sitzplatznummer ein: ");
        sitzplatz = Manager.stringscanner();
    }

    Sitzklasse sitzklasse = flug.findeSitzplatz(sitzplatz).getSitzklasse();

    System.out.println("Bitte geben Sie die Anzahl der Koffer ein, die Sie aufgeben möchten: ");
    int  koffer = Manager.intscanner();

    bs.buchungVornehmen(passagier, flug, sitzplatz, koffer, sitzklasse, vs );
    datenHandler.speichere(anwendungsdaten);


} catch (Exception e) {
    System.out.println( "Fehler "+ e.getMessage());
}
    }

    /**
     * Führt die Umbuchung einer vorhandenen Buchung durch.
     *
     * Zunächst werden alle Buchungen des Passagiers angezeigt. Der Kunde
     * wählt eine Buchung über die Buchungsnummer aus. Danach sucht er
     * einen neuen Flug und wählt einen neuen Sitzplatz sowie eine
     * Sitzklasse.
     *
     * Die Umbuchung wird nur durchgeführt, wenn die angegebene Buchung
     * dem angemeldeten Passagier gehört.
     * 
     * Im Anschluss an eine erfolgreiche Umbuchung kann der Kunde
     * zusätzlich die Anzahl der Gepäckstücke anpassen.
     *
     * @param passagier der Passagier, dessen Buchung geändert werden soll
     */
    public void umbuchen(Passagier passagier)
    {


        System.out.println("-------------------Willkommen im Umbuchungsbereich-------------------------");
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }

        System.out.println("Bitte Buchungsnummer für Umbuchung angeben:");

        String nummer = Manager.stringscanner();

        try {
        Buchung buchung = bs.sucheBuchungNachNummer(nummer);

        if (!buchung.getPassagier().equals(passagier)) {
            System.out.println("Fehler: Diese Buchung gehört nicht zu diesem Passagier!.");
            return;
        }
        }
        catch (Exception e) {
            System.out.println( "Fehler "+ e.getMessage());
            return;
        }



        System.out.println(vs.getFlughaefen().toString());

        try {
            ArrayList<Flug> fluege = new ArrayList<>();

            System.out.print("Name Zielflughafen (optional): ");
            String ziel = Manager.stringscanner();

            System.out.print("Name Startflughafen (optional): ");
            String start = Manager.stringscanner();

            System.out.print("Flugnummer (optional): ");
            String flugnummer = Manager.stringscanner();

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
            String sitzplatz = Manager.stringscanner();

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

            Buchung buchung = bs.sucheBuchungNachNummer(nummer);

            bs.umbuchen(buchung, fluege.get(index), sitzplatz, sitzklasse, vs);
            datenHandler.speichere(anwendungsdaten);

            
            System.out.println("Umbuchung erfolgreich.");

            System.out.println("Möchten Sie die Anzahl Ihrer Koffer ändern?");
            System.out.println("1 - Ja");
            System.out.println("2 - Nein");

            int auswahlGepaeck = Manager.intscanner();

            if (auswahlGepaeck == 1) {
                gepaeckAendern(buchung);
            }

        } catch (Exception e) {
            System.out.println( "Fehler "+ e.getMessage());
        }



    }


    /**
     * Storniert eine Buchung des angegebenen Passagiers.
     *
     * Es werden zunächst alle Buchungen des Passagiers angezeigt.
     * Anschließend wird die gewünschte Buchung über ihre Buchungsnummer
     * ausgewählt. Die Stornierung ist nur möglich, wenn die Buchung dem
     * angemeldeten Passagier gehört.
     *
     * Nach erfolgreicher Stornierung werden die Anwendungsdaten
     * gespeichert.
     *
     * @param passagier der Passagier, dessen Buchung storniert werden soll
     */
    public void stornieren(Passagier passagier)
    {
        System.out.println("-------------------Willkommen im Stornierungsbereich-------------------------");
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }

        System.out.println("Bitte Buchungsnummer für Stornierung angeben:");

        String nummer = Manager.stringscanner();
try {
    Buchung buchung = bs.sucheBuchungNachNummer(nummer);

    if (!buchung.getPassagier().equals(passagier)) {
        System.out.println("Fehler: Diese Buchung gehört nicht zu diesem Passagier!.");
        return;
    }

    bs.stornieren(buchung);
    datenHandler.speichere(anwendungsdaten);


}
catch (Exception e) {
    System.out.println( "Fehler "+ e.getMessage());
}





    }

    /**
     * Zeigt alle Buchungen des angegebenen Passagiers an.
     *
     * Dafür werden sämtliche Buchungen durchsucht. Angezeigt werden nur
     * Buchungen, die dem übergebenen Passagier zugeordnet sind.
     *
     * @param passagier der Passagier, dessen Buchungen angezeigt werden
     */
    public void buchunganzeigen(Passagier passagier)
    {
        for (Buchung b : bs.getBuchungen()) {
            if (b.getPassagier().equals(passagier)) {
                System.out.println(b);
            }
        }
    }


    /**
     * Ermöglicht die nachträgliche Anpassung der Anzahl der Gepäckstücke
     * für eine Buchung des angegebenen Passagiers.
     *
     * Zunächst werden die Buchungen des Passagiers angezeigt und anschließend
     * die gewünschte Buchung anhand der eingegebenen Buchungsnummer ermittelt.
     * Es wird geprüft, ob die ausgewählte Buchung tatsächlich dem angegebenen
     * Passagier zugeordnet ist. Anschließend wird die Änderung der
     * {@code Gepaeckinformation} für diese Buchung durchgeführt.
     *
     * @param passagier der Passagier, dessen Gepäckinformationen geändert werden sollen
     */
    public void gepaeckAendern(Passagier passagier) {

        System.out.println("Ihre Buchungen:");

        buchunganzeigen(passagier);

        System.out.println("Bitte Buchungsnummer angeben:");
        String nummer = Manager.stringscanner();

        try {
            Buchung buchung = bs.sucheBuchungNachNummer(nummer);

            if (!buchung.getPassagier().equals(passagier)) {
                System.out.println("Diese Buchung gehört nicht zu diesem Passagier.");
                return;
            }

            gepaeckAendern(buchung);

        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }


    /**
     * Ändert die Anzahl der Gepäckstücke einer bestehenden Buchung.
     *
     * Die aktuell gebuchte Anzahl der Koffer wird angezeigt und eine neue
     * Anzahl wird vom Benutzer abgefragt. Anschließend werden die
     * {@code Gepaeckinformation} und der Preis der Buchung entsprechend angepasst.
     * Die Änderungen werden danach persistent gespeichert und die entstandene
     * Preisdifferenz wird ausgegeben.
     *
     * @param buchung die Buchung, deren Gepäckinformationen geändert werden sollen
     */
    public void gepaeckAendern(Buchung buchung) {

        try {
            System.out.println("Aktuell gebuchte Koffer: " + buchung.getGepaeckinformation().getAnzahlKoffer() );

            System.out.println("Neue Anzahl Koffer:");
            int neueAnzahl = Manager.intscanner();

            double differenz = bs.gepaeckAendern(buchung, neueAnzahl);

            datenHandler.speichere(anwendungsdaten);

            System.out.println("Gepäck wurde erfolgreich geändert.");
            System.out.printf("Preisänderung: %.2f €%n", differenz);

        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }
}
