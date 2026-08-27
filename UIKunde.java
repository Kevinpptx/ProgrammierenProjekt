import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Die Klasse {@code UIKunde} stellt die Konsolenoberfläche für Kunden bereit.
 * <p>
 * Kunden können sich neu registrieren oder ein bereits vorhandenes Kundenkonto auswählen. Nach der Anmeldung können sie
 * Flüge suchen und buchen, bestehende Buchungen umbuchen oder stornieren und ihre Buchungen anzeigen lassen.
 * <p>
 * Änderungen an den Daten werden mithilfe des {@link DatenHandler} gespeichert.
 *
 * @author Lars Pfeiffer, Cedric Beckmann
 * @version 1.1
 */
public class UIKunde {

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
     * Formatiert Datum und Uhrzeit im Format {@code dd.MM.yyyy HH:mm}.
     */
    private static final DateTimeFormatter DATUM_ZEIT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Erstellt eine neue Kundenoberfläche. Das Buchungs- und Verwaltungssystem werden aus den übergebenen
     * Anwendungsdaten übernommen.
     *
     * @param datenHandler    Handler zum Speichern der Anwendungsdaten
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
     * <p>
     * Der Benutzer kann einen neuen Kunden anlegen, einen bestehenden Passagier über dessen ID auswählen oder zum
     * vorherigen Menü zurückkehren.
     * <p>
     * Nach einer erfolgreichen Anmeldung wird das Kundenhauptmenü geöffnet.
     */
    public void kunde() {

        while (true) {

            UIHelper.druckeUeberschrift("Herzlich willkommen");

            UIHelper.druckeMenuepunkt(1, "Als neuer Kunde registrieren");
            UIHelper.druckeMenuepunkt(2, "Als bestehender Kunde anmelden");
            UIHelper.druckeMenuepunkt(0, "Zurück");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    String name;

                    while (true) {
                        UIHelper.druckeEingabeaufforderung("Bitte geben Sie Ihren Namen ein:");
                        name = Manager.stringscanner();

                        if (! name.isBlank()) {
                            break;
                        }

                        UIHelper.druckeFehler("Der Name darf nicht leer sein.");
                    }

                    while (true) {
                        UIHelper.druckeEingabeaufforderung("Bitte geben Sie Ihre E-Mail ein:");
                        String mail = Manager.stringscanner();

                        try {
                            Passagier passagier = bs.initialisierePassagier(name, mail);

                            datenHandler.speichere(anwendungsdaten);

                            UIHelper.druckeErfolg("Sie wurden erfolgreich registriert.");

                            hauptmanagerk(passagier);
                            return;

                        } catch (Exception e) {
                            UIHelper.druckeFehler(e.getMessage());
                        }
                    }

                case 2:

                    if (bs.getPassagiere().isEmpty()) {
                        UIHelper.druckeHinweis("Es sind noch keine Kunden vorhanden.");
                        break;
                    }

                    UIHelper.druckeUeberschrift("Passagierauswahl");

                    for (Passagier passagier : bs.getPassagiere()) {
                        System.out.println(passagier.getPassagierId() +
                                           " - " +
                                           passagier.getName() +
                                           " - " +
                                           passagier.getEmail());
                    }

                    UIHelper.druckeTrennlinie();

                    UIHelper.druckeEingabeaufforderung("Bitte wählen Sie einen Passagier über die ID.");
                    UIHelper.druckeEingabeaufforderung("Geben Sie 0 ein, um zurückzukehren.");

                    while (true) {
                        String id = Manager.stringscanner();

                        if (id.equals("0")) {
                            break;
                        }

                        Passagier ausgewaehlterPassagier = null;

                        for (Passagier passagier : bs.getPassagiere()) {
                            if (passagier.getPassagierId().equalsIgnoreCase(id)) {
                                ausgewaehlterPassagier = passagier;
                                break;
                            }
                        }

                        if (ausgewaehlterPassagier == null) {
                            UIHelper.druckeFehler("Diese Passagier-ID existiert nicht.");
                            UIHelper.druckeEingabeaufforderung(
                                    "Bitte geben Sie eine gültige Passagier-ID ein oder 0 zum Zurückkehren:");
                            continue;
                        }

                        UIHelper.druckeErfolg("Angemeldet als " + ausgewaehlterPassagier.getName() + ".");

                        hauptmanagerk(ausgewaehlterPassagier);
                        return;
                    }

                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Eingabe.");
                    break;

            }
        }

    }

    /**
     * Zeigt das Kundenhauptmenü für einen angemeldeten Passagier an.
     * <p>
     * Von diesem Menü aus kann der Kunde Flüge suchen und buchen, bestehende Buchungen umbuchen oder stornieren sowie
     * seine aktuellen Buchungen anzeigen lassen und die Anzahl der Gepäckstücke anpassen.
     *
     * @param passagier der aktuell angemeldete Passagier
     */
    public void hauptmanagerk(Passagier passagier) {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen " + passagier.getName() + " im Kundenbereich");

            UIHelper.druckeEingabeaufforderung("Was möchten Sie tun?");
            UIHelper.druckeMenuepunkt(1, "Flüge suchen und buchen");
            UIHelper.druckeMenuepunkt(2, "Buchungen verwalten");
            UIHelper.druckeMenuepunkt(0, "Abmelden");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {
                case 1:
                    fluegeSuchenUndBuchen(passagier);
                    break;
                case 2:
                    buchungenVerwalten(passagier);
                    break;
                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;

            }
        }
    }

    /**
     * Zeigt das Verwaltungsmenü für die Buchungen eines Passagiers an.
     * <p>
     * Über das Menü können Buchungen angezeigt, umgebucht oder storniert sowie Gepäckinformationen geändert werden. Das
     * Menü wird so lange angezeigt, bis der Benutzer zum Kundenhauptmenü zurückkehrt.
     *
     * @param passagier der aktuell angemeldete Passagier
     */
    public void buchungenVerwalten(Passagier passagier) {

        while (true) {

            UIHelper.druckeUeberschrift("Buchungen verwalten");

            UIHelper.druckeMenuepunkt(1, "Buchungen anzeigen");
            UIHelper.druckeMenuepunkt(2, "Buchung umbuchen");
            UIHelper.druckeMenuepunkt(3, "Buchung stornieren");
            UIHelper.druckeMenuepunkt(4, "Gepäck ändern");
            UIHelper.druckeMenuepunkt(0, "Zurück");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    UIHelper.druckeUeberschrift("Meine Buchungen");
                    buchungenAnzeigen(passagier);
                    break;

                case 2:
                    umbuchen(passagier);
                    break;

                case 3:
                    stornieren(passagier);
                    break;

                case 4:
                    gepaeckAendern(passagier);
                    break;

                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungültige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }
    }

    /**
     * Ermöglicht einem Passagier die Suche und Buchung eines Fluges.
     * <p>
     * Vor der Suche werden vergangene Flüge entfernt und betroffene Buchungen entsprechend aktualisiert. Anschließend
     * kann die Flugsuche anhand verschiedener Kriterien eingeschränkt werden.
     * <p>
     * Nach Auswahl eines Fluges, eines freien Sitzplatzes und der gewünschten Gepäckmenge wird eine Buchungsvorschau
     * mit Ticketpreis, Gepäckkosten und Gesamtpreis angezeigt. Die Buchung wird erst nach einer ausdrücklichen
     * Bestätigung des Benutzers durchgeführt und anschließend gespeichert.
     *
     * @param passagier der Passagier, für den der Flug gebucht wird
     */
    public void fluegeSuchenUndBuchen(Passagier passagier) {

        UIHelper.druckeUeberschrift("Flüge suchen und buchen");

        vs.alteFluegeLoeschen(bs);

        this.druckeFlughaefen();

        try {

            ArrayList<Flug> fluege = sucheFluege();

            if (fluege.isEmpty()) {
                UIHelper.druckeHinweis("Keine Flüge gefunden.");
                return;
            } else {
                this.druckeFluege(fluege);
            }

            Flug flug = flugAuswaehlen(fluege);

            String sitzplatz = sitzplatzAuswaehlen(flug);

            Sitzklasse sitzklasse = flug.findeSitzplatz(sitzplatz).getSitzklasse();

            int koffer;

            while (true) {

                UIHelper.druckeEingabeaufforderung(
                        "Bitte geben Sie die Anzahl der Koffer ein, die Sie aufgeben möchten:");

                koffer = Manager.intscanner();

                if (koffer < 0) {
                    UIHelper.druckeFehler("Die Anzahl der Koffer darf nicht negativ sein.");
                    continue;
                }

                break;
            }

            // Buchungsvorschau, damit der Kunde den ausgewählten Flug akzeptieren kann
            Buchung buchungsvorschau = new Buchung(passagier,
                    flug,
                    flug.findeSitzplatz(sitzplatz),
                    new GepaeckInformation(koffer)
            );

            UIHelper.druckeUeberschrift("Buchungsübersicht");

            System.out.printf("%-13s%s%n", "Flug:", flug.getFlugnummer());
            System.out.printf("%-13s%s%n", "Airline:", flug.getFluggesellschaft().getName());
            System.out.printf("%-13s%s -> %s%n",
                    "Route:",
                    flug.getStartFlughafen().iataCode(),
                    flug.getZielflughafen().iataCode()
            );

            System.out.printf("%-13s%s%n", "Abflug:", flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER));

            System.out.printf("%-13s%s%n", "Sitzplatz:", sitzplatz);
            System.out.printf("%-13s%s%n", "Sitzklasse:", sitzklasse);
            System.out.printf("%-13s%d%n", "Koffer:", koffer);

            double gepaeckpreis = buchungsvorschau.getGepaeckinformation().berechneGepaeckgebuehr();
            double ticketpreis = buchungsvorschau.getGezahlterPreis() - gepaeckpreis;

            System.out.printf("%-13s%.2f Euro%n", "Ticketpreis:", ticketpreis);
            System.out.printf("%-13s%.2f Euro%n", "Gepäck:", gepaeckpreis);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-13s%.2f Euro%n", "Gesamtpreis:", buchungsvorschau.getGezahlterPreis());

            UIHelper.druckeTrennlinie();

            if (! bestaetigungEinlesen("Möchten Sie die Buchung verbindlich durchführen?")) {
                UIHelper.druckeHinweis("Die Buchung wurde abgebrochen.");
                return;
            }

            Buchung buchung = bs.buchungVornehmen(passagier, flug, sitzplatz, koffer, sitzklasse, vs);

            UIHelper.druckeUeberschrift("Buchungsbestätigung");

            System.out.println("Buchungsnummer: " + buchung.getBuchungsnummer());
            System.out.println("Flug:           " + buchung.getFlug().getFlugnummer());
            System.out.println("Sitzplatz:      " + buchung.getSitzplatz().getSitzplatzNummer());
            System.out.println("Sitzklasse:     " + buchung.getSitzplatz().getSitzklasse());
            System.out.println("Koffer:         "
                               + buchung.getGepaeckinformation().getAnzahlKoffer());

            System.out.printf("Gesamtpreis:    %.2f Euro%n", buchung.getGezahlterPreis());

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Buchung wurde erfolgreich durchgeführt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Gibt alle im Verwaltungssystem registrierten Flughäfen in einer tabellarischen Übersicht aus.
     */
    private void druckeFlughaefen() {

        UIHelper.druckeEingabeaufforderung("Verfügbare Flughäfen:");

        UIHelper.druckeTrennlinie();

        System.out.printf("%-6s | %-35s | %-15s | %-15s%n", "Code", "Flughafen", "Ort", "Land");

        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : vs.getFlughaefen()) {
            System.out.printf("%-6s | %-35s | %-15s | %-15s%n",
                    flughafen.iataCode(),
                    flughafen.name(),
                    flughafen.stadt(),
                    flughafen.land()
            );
        }

        UIHelper.druckeTrennlinie();
    }

    /**
     * Gibt die übergebenen Flüge nummeriert in einer tabellarischen Übersicht aus.
     * <p>
     * Angezeigt werden Flugnummer, Fluggesellschaft, Route sowie Abflug- und Ankunftszeit.
     *
     * @param fluege die anzuzeigenden Flüge
     */
    private void druckeFluege(ArrayList<Flug> fluege) {

        UIHelper.druckeUeberschrift("Gefundene Flüge");

        System.out.printf("%-4s | %-8s | %-15s | %-11s | %-17s | %-17s%n",
                "Nr.",
                "Flug",
                "Airline",
                "Route",
                "Abflug",
                "Ankunft"
        );

        UIHelper.druckeTrennlinie();

        for (int i = 0; i < fluege.size(); i++) {

            Flug flug = fluege.get(i);

            System.out.printf(
                    "%-4d | %-8s | %-15s | %-11s | %-17s | %-17s%n",
                    i + 1,
                    flug.getFlugnummer(),
                    flug.getFluggesellschaft().getName(),
                    flug.getStartFlughafen().iataCode() + " -> " + flug.getZielflughafen().iataCode(),
                    flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER),
                    flug.getAnkunftszeit().format(DATUM_ZEIT_FORMATTER)
            );
        }

        UIHelper.druckeTrennlinie();
    }

    /**
     * Liest die Auswahl eines Fluges aus einer zuvor nummerierten Flugliste ein.
     * <p>
     * Ungültige Nummern werden abgewiesen und erneut abgefragt.
     *
     * @param fluege die zur Auswahl stehenden Flüge
     * @return der vom Benutzer ausgewählte Flug
     */
    private Flug flugAuswaehlen(ArrayList<Flug> fluege) {

        Flug flug = null;

        while (flug == null) {

            UIHelper.druckeEingabeaufforderung(
                    "Bitte geben Sie die Nummer Ihres gewünschten Fluges ein:"
            );

            int auswahl = Manager.intscanner();

            if (auswahl < 1 || auswahl > fluege.size()) {
                UIHelper.druckeFehler("Ungültige Flugauswahl.");
                continue;
            }

            flug = fluege.get(auswahl - 1);
        }

        return flug;
    }

    /**
     * Zeigt den Sitzplan eines Fluges an und liest die gewünschte Sitzplatznummer ein.
     * <p>
     * Nicht vorhandene oder bereits belegte Sitzplätze werden abgewiesen und erneut abgefragt.
     *
     * @param flug der Flug, für den ein Sitzplatz ausgewählt werden soll
     * @return die Nummer des ausgewählten freien Sitzplatzes
     */
    private String sitzplatzAuswaehlen(Flug flug) {

        UIHelper.druckeUeberschrift("Sitzplatzauswahl");

        flug.zeigeSitzplan();

        while (true) {

            UIHelper.druckeEingabeaufforderung(
                    "Bitte geben Sie die gewünschte Sitzplatznummer ein:"
            );

            String sitzplatz = Manager.stringscanner().toUpperCase();

            Sitzplatz ausgewaehlterSitzplatz = flug.findeSitzplatz(sitzplatz);

            if (ausgewaehlterSitzplatz == null) {
                UIHelper.druckeFehler("Dieser Sitzplatz existiert nicht.");
                continue;
            }

            if (! ausgewaehlterSitzplatz.getIstFrei()) {
                UIHelper.druckeFehler("Dieser Sitzplatz ist bereits belegt.");
                continue;
            }

            return sitzplatz;
        }
    }

    /**
     * Ermöglicht das Umbuchen einer bestehenden Buchung des angegebenen Passagiers.
     * <p>
     * Zunächst werden vergangene Flüge und die zugehörigen Buchungsstatus aktualisiert. Eine Umbuchung ist nur möglich,
     * wenn mindestens eine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Nach Auswahl der Buchung wird geprüft, ob diese dem angemeldeten Passagier gehört und noch umgebucht werden darf.
     * Anschließend wählt der Benutzer einen neuen Flug und einen freien Sitzplatz aus.
     * <p>
     * Vor der Durchführung werden der bisherige und der neue Buchungspreis, die Umbuchungsgebühr sowie der zusätzlich
     * zu zahlende Betrag angezeigt. Die Umbuchung wird erst nach einer ausdrücklichen Bestätigung durchgeführt und
     * anschließend gespeichert.
     * <p>
     * Nach erfolgreicher Umbuchung kann optional auch die Anzahl der gebuchten Koffer geändert werden.
     *
     * @param passagier der Passagier, dessen Buchung umgebucht werden soll
     */
    public void umbuchen(Passagier passagier) {

        UIHelper.druckeUeberschrift("Buchung umbuchen");

        vs.alteFluegeLoeschen(bs);

        if (! hatBearbeitbareBuchungen(passagier)) {
            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, die umgebucht werden können.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Buchungsnummer der umzubuchenden Buchung ein:");

        String nummer = Manager.stringscanner();

        Buchung buchung;

        try {
            buchung = bs.sucheBuchungNachNummer(nummer);

            if (! buchung.getPassagier().equals(passagier)) {
                UIHelper.druckeFehler("Diese Buchung gehört nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() == Buchungsstatus.STORNIERT ||
                buchung.getBuchungsstatus() == Buchungsstatus.VERGANGEN) {
                UIHelper.druckeFehler("Stornierte oder vergangene Buchungen können nicht umgebucht werden.");
                return;
            }

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
            return;
        }

        druckeFlughaefen();

        try {
            ArrayList<Flug> fluege = sucheFluege();

            if (fluege.isEmpty()) {
                UIHelper.druckeHinweis("Keine Flüge gefunden.");
                return;
            }

            druckeFluege(fluege);

            Flug neuerFlug = flugAuswaehlen(fluege);

            String sitzplatz = sitzplatzAuswaehlen(neuerFlug);

            Sitzplatz neuerSitzplatz = neuerFlug.findeSitzplatz(sitzplatz);
            Sitzklasse sitzklasse = neuerSitzplatz.getSitzklasse();

            Buchung buchungsvorschau = new Buchung(passagier,
                    neuerFlug,
                    neuerSitzplatz,
                    new GepaeckInformation(buchung.getGepaeckinformation().getAnzahlKoffer())
            );

            double bisherigerBuchungspreis = buchung.getGezahlterPreis();
            double neuerBuchungspreis = buchungsvorschau.getGezahlterPreis();
            double umbuchungsgebuehr = buchung.getUmbuchungsgebuehr();
            double zusaetzlichZuZahlen = bs.berechneUmbuchungsgebuehr(buchung, neuerFlug, neuerSitzplatz);

            UIHelper.druckeUeberschrift("Umbuchungsübersicht");

            System.out.printf("%-27s%s | %s%n",
                    "Bisheriger Flug:",
                    buchung.getFlug().getFlugnummer(),
                    buchung.getFlug().getAbflugszeit().format(DATUM_ZEIT_FORMATTER)
            );

            System.out.printf("%-27s%s | %s%n",
                    "Neuer Flug:",
                    neuerFlug.getFlugnummer(),
                    neuerFlug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER)
            );

            System.out.printf("%-27s%s -> %s%n",
                    "Neue Route:",
                    neuerFlug.getStartFlughafen().iataCode(),
                    neuerFlug.getZielflughafen().iataCode()
            );

            System.out.printf("%-27s%s%n", "Neuer Sitzplatz:", sitzplatz);

            System.out.printf("%-27s%s%n", "Sitzklasse:", sitzklasse);

            System.out.printf("%-27s%.2f Euro%n", "Bisheriger Buchungspreis:", bisherigerBuchungspreis);

            System.out.printf("%-27s%.2f Euro%n", "Neuer Buchungspreis:", neuerBuchungspreis);

            System.out.printf("%-27s%.2f Euro%n", "Umbuchungsgebühr:", umbuchungsgebuehr);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-27s%.2f Euro%n", "Zusätzlich zu zahlen:", zusaetzlichZuZahlen);

            UIHelper.druckeTrennlinie();

            if (! bestaetigungEinlesen("Möchten Sie die Umbuchung verbindlich durchführen?")) {
                UIHelper.druckeHinweis("Die Umbuchung wurde abgebrochen.");
                return;
            }

            bs.umbuchen(buchung, neuerFlug, sitzplatz, sitzklasse, vs);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Umbuchung wurde erfolgreich durchgeführt.");

            UIHelper.druckeTrennlinie();

            if (bestaetigungEinlesen("Möchten Sie die Anzahl Ihrer Koffer ebenfalls ändern?")) {
                gepaeckAendern(buchung);
            }

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Ermöglicht die Stornierung einer bestehenden Buchung des angegebenen Passagiers.
     * <p>
     * Zunächst werden vergangene Flüge und die zugehörigen Buchungsstatus aktualisiert. Eine Stornierung ist nur
     * möglich, wenn mindestens eine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Nach Auswahl der Buchung wird geprüft, ob diese dem angemeldeten Passagier gehört und noch storniert werden darf.
     * Vor der Durchführung werden der aktuelle Buchungspreis, die Stornierungsgebühr und der daraus resultierende
     * Erstattungsbetrag angezeigt.
     * <p>
     * Die Stornierung wird erst nach einer ausdrücklichen Bestätigung durchgeführt und anschließend gespeichert.
     *
     * @param passagier der Passagier, dessen Buchung storniert werden soll
     */
    public void stornieren(Passagier passagier) {

        UIHelper.druckeUeberschrift("Buchung stornieren");

        vs.alteFluegeLoeschen(bs);

        if (! hatBearbeitbareBuchungen(passagier)) {
            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, die storniert werden können.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung(
                "Bitte geben Sie die Buchungsnummer der Buchung ein, die Sie stornieren möchten:");

        String nummer = Manager.stringscanner();
        try {
            Buchung buchung = bs.sucheBuchungNachNummer(nummer);

            if (! buchung.getPassagier().equals(passagier)) {
                UIHelper.druckeFehler("Diese Buchung gehört nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {
                UIHelper.druckeFehler("Stornierte oder vergangene Buchungen können nicht erneut storniert werden.");
                return;
            }

            double buchungspreis = buchung.getGezahlterPreis();
            double stornierungsgebuehr = buchung.getStornierungsgebuehr();
            double erstattungsbetrag = Math.max(0, buchungspreis - stornierungsgebuehr);

            UIHelper.druckeUeberschrift("Stornierungsübersicht");

            System.out.printf("%-22s%s%n", "Buchungsnummer:", buchung.getBuchungsnummer());

            System.out.printf("%-22s%s | %s%n", "Flug:",
                    buchung.getFlug().getFlugnummer(),
                    buchung.getFlug().getAbflugszeit().format(DATUM_ZEIT_FORMATTER)
            );

            System.out.printf("%-22s%s -> %s%n", "Route:",
                    buchung.getFlug().getStartFlughafen().iataCode(),
                    buchung.getFlug().getZielflughafen().iataCode()
            );

            System.out.printf("%-22s%.2f Euro%n", "Buchungspreis:", buchungspreis);

            System.out.printf("%-22s%.2f Euro%n", "Stornierungsgebühr:", stornierungsgebuehr);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-22s%.2f Euro%n", "Erstattungsbetrag:", erstattungsbetrag);

            UIHelper.druckeTrennlinie();

            if (! bestaetigungEinlesen("Möchten Sie die Buchung wirklich stornieren?")) {
                UIHelper.druckeHinweis("Die Stornierung wurde abgebrochen.");
                return;
            }

            bs.stornieren(buchung);
            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Buchung wurde erfolgreich storniert.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }

    }

    /**
     * Zeigt alle Buchungen des angegebenen Passagiers in einer tabellarischen Übersicht an.
     * <p>
     * Vor der Anzeige werden vergangene Flüge entfernt und die zugehörigen Buchungsstatus aktualisiert. Angezeigt
     * werden unter anderem Flug, Route, Abflugzeit, Sitzplatz, Sitzklasse, Gepäck, Buchungspreis und Buchungsstatus.
     * <p>
     * Sind für den Passagier keine Buchungen vorhanden, wird ein entsprechender Hinweis ausgegeben.
     *
     * @param passagier der Passagier, dessen Buchungen angezeigt werden
     */
    public void buchungenAnzeigen(Passagier passagier) {

        vs.alteFluegeLoeschen(bs);

        boolean buchungVorhanden = false;

        System.out.printf("%-8s | %-8s | %-11s | %-17s | %-6s | %-10s | %-6s | %-12s | %-10s%n",
                "Buchung",
                "Flug",
                "Route",
                "Abflug",
                "Sitz",
                "Klasse",
                "Koffer",
                "Preis",
                "Status"
        );

        UIHelper.druckeTrennlinie();

        for (Buchung buchung : bs.getBuchungen()) {

            if (buchung.getPassagier().equals(passagier)) {

                buchungVorhanden = true;

                Flug flug = buchung.getFlug();

                System.out.printf(
                        "%-8s | %-8s | %-11s | %-17s | %-6s | %-10s | %-6d | %-12s | %-10s%n",
                        buchung.getBuchungsnummer(),
                        flug.getFlugnummer(),
                        flug.getStartFlughafen().iataCode() + " -> " + flug.getZielflughafen().iataCode(),
                        flug.getAbflugszeit().format(DATUM_ZEIT_FORMATTER),
                        buchung.getSitzplatz().getSitzplatzNummer(),
                        buchung.getSitzplatz().getSitzklasse(),
                        buchung.getGepaeckinformation().getAnzahlKoffer(),
                        String.format("%.2f Euro", buchung.getGezahlterPreis()),
                        buchung.getBuchungsstatus()
                );
            }
        }

        if (! buchungVorhanden) {
            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen.");
        }
    }

    /**
     * Ermöglicht die Auswahl einer Buchung, deren Gepäckmenge geändert werden soll.
     * <p>
     * Zunächst werden vergangene Flüge und die zugehörigen Buchungsstatus aktualisiert. Sind keine bearbeitbaren
     * Buchungen vorhanden, wird der Vorgang beendet.
     * <p>
     * Anschließend wählt der Passagier eine Buchung über deren Buchungsnummer aus. Es wird geprüft, ob die Buchung dem
     * angemeldeten Passagier gehört und ob ihr Status eine Gepäckänderung erlaubt. Die eigentliche Änderung wird
     * anschließend an {@link #gepaeckAendern(Buchung)} übergeben.
     *
     * @param passagier der Passagier, dessen Gepäckinformationen geändert werden sollen
     */
    public void gepaeckAendern(Passagier passagier) {

        UIHelper.druckeUeberschrift("Gepäck ändern");

        vs.alteFluegeLoeschen(bs);

        if (! hatBearbeitbareBuchungen(passagier)) {
            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, bei denen das Gepäck geändert werden kann.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung(
                "Bitte geben Sie die Buchungsnummer der Buchung ein, bei der Sie das Gepäck anpassen wollen:");

        String nummer = Manager.stringscanner();

        try {
            Buchung buchung = bs.sucheBuchungNachNummer(nummer);

            if (! buchung.getPassagier().equals(passagier)) {
                UIHelper.druckeFehler("Diese Buchung gehört nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {
                UIHelper.druckeFehler("Das Gepäck kann bei dieser Buchung nicht mehr geändert werden.");
                return;
            }

            gepaeckAendern(buchung);

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Ändert die Anzahl der Gepäckstücke einer bestehenden Buchung.
     * <p>
     * Zunächst werden die aktuell gebuchte und die gewünschte neue Anzahl der Koffer ermittelt. Negative Werte werden
     * abgewiesen und bei unveränderter Kofferanzahl wird der Vorgang beendet.
     * <p>
     * Vor der tatsächlichen Änderung wird anhand einer Buchungsvorschau der neue Buchungspreis ermittelt. Anschließend
     * werden der bisherige und der neue Buchungspreis sowie der zusätzlich zu zahlende Betrag beziehungsweise der
     * Erstattungsbetrag angezeigt.
     * <p>
     * Die Gepäckänderung wird erst nach einer ausdrücklichen Bestätigung durchgeführt und anschließend gespeichert.
     *
     * @param buchung die Buchung, deren Gepäckinformationen geändert werden sollen
     */
    public void gepaeckAendern(Buchung buchung) {

        vs.alteFluegeLoeschen(bs);

        try {
            int aktuelleAnzahl = buchung.getGepaeckinformation().getAnzahlKoffer();

            UIHelper.druckeEingabeaufforderung("Aktuell gebuchte Koffer: " + aktuelleAnzahl);

            int neueAnzahl;

            while (true) {

                UIHelper.druckeEingabeaufforderung("Bitte geben Sie die neue Anzahl der Koffer ein:");

                neueAnzahl = Manager.intscanner();

                if (neueAnzahl < 0) {
                    UIHelper.druckeFehler("Die Anzahl der Koffer darf nicht negativ sein.");
                    continue;
                }

                if (neueAnzahl == aktuelleAnzahl) {
                    UIHelper.druckeHinweis("Die Anzahl der Koffer wurde nicht geändert.");
                    return;
                }

                break;
            }

            Buchung buchungsvorschau = new Buchung(buchung.getPassagier(),
                    buchung.getFlug(),
                    buchung.getSitzplatz(),
                    new GepaeckInformation(neueAnzahl)
            );

            double bisherigerPreis = buchung.getGezahlterPreis();
            double neuerPreis = buchungsvorschau.getGezahlterPreis();
            double differenz = neuerPreis - bisherigerPreis;

            UIHelper.druckeUeberschrift("Gepäckänderung");

            System.out.printf("%-27s%d%n", "Bisherige Koffer:", aktuelleAnzahl);

            System.out.printf("%-27s%d%n", "Neue Koffer:", neueAnzahl);

            System.out.printf("%-27s%.2f Euro%n", "Bisheriger Buchungspreis:", bisherigerPreis);

            System.out.printf("%-27s%.2f Euro%n", "Neuer Buchungspreis:", neuerPreis);

            UIHelper.druckeTrennlinie();

            if (differenz > 0) {
                System.out.printf("%-27s%.2f Euro%n", "Zusätzlich zu zahlen:", differenz);
            } else {
                System.out.printf("%-27s%.2f Euro%n", "Erstattungsbetrag:", Math.abs(differenz));
            }

            UIHelper.druckeTrennlinie();

            if (! bestaetigungEinlesen("Möchten Sie die Gepäckänderung verbindlich durchführen?")) {
                UIHelper.druckeHinweis("Die Gepäckänderung wurde abgebrochen.");
                return;
            }

            bs.gepaeckAendern(buchung, neueAnzahl);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Das Gepäck wurde erfolgreich geändert.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Liest eine Ja-Nein-Bestätigung des Benutzers ein.
     * <p>
     * Akzeptiert werden die Eingaben {@code j} und {@code n} unabhängig von Groß- und Kleinschreibung. Ungültige
     * Eingaben werden erneut abgefragt.
     *
     * @param text der Text der Bestätigungsfrage
     * @return {@code true} bei einer Bestätigung mit {@code j}, sonst {@code false}
     */
    private boolean bestaetigungEinlesen(String text) {

        while (true) {
            UIHelper.druckeEingabeaufforderung(text + " [j/n]");

            String eingabe = Manager.stringscanner();

            if (eingabe.equalsIgnoreCase("j")) {
                return true;
            }

            if (eingabe.equalsIgnoreCase("n")) {
                return false;
            }

            UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie j oder n ein.");
        }
    }

    /**
     * Prüft, ob für den angegebenen Passagier mindestens eine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Als bearbeitbar gelten Buchungen mit dem Status {@code AKTIV} oder {@code UMGEBUCHT}.
     *
     * @param passagier der zu überprüfende Passagier
     * @return {@code true}, wenn mindestens eine bearbeitbare Buchung vorhanden ist, sonst {@code false}
     */
    private boolean hatBearbeitbareBuchungen(Passagier passagier) {

        for (Buchung buchung : bs.getBuchungen()) {

            if (buchung.getPassagier().equals(passagier)
                && (buchung.getBuchungsstatus() == Buchungsstatus.AKTIV
                    || buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Liest die Suchkriterien für eine Flugsuche ein und ermittelt die dazu passenden Flüge.
     * <p>
     * Der Zielflughafen ist verpflichtend. Zusätzlich können ein Startflughafen, eine Flugnummer und ein Datum als
     * weitere Suchkriterien angegeben werden. Die eingegebenen Kriterien werden miteinander kombiniert und über die
     * Suchmethoden des Verwaltungssystems ausgewertet.
     *
     * @return Liste der Flüge, die den angegebenen Suchkriterien entsprechen
     */
    private ArrayList<Flug> sucheFluege() {

        ArrayList<Flug> fluege = new ArrayList<>();

        String start = "", ziel = "", flugnummer = "";
        int zaehler = 0;
        LocalDate datum = null;

        boolean startUeberspringen = false, flugnummerUeberspringen = false, datumUeberspringen = false;

        while (ziel.length() != 3 ||
               ! Character.isAlphabetic(ziel.charAt(0)) ||
               ! Character.isAlphabetic(ziel.charAt(1)) ||
               ! Character.isAlphabetic(ziel.charAt(2))) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungültige Eingabe. Beispiel: FRA");
            }

            UIHelper.druckeEingabeaufforderung("IATA-Code des Zielflughafens (verpflichtend):");
            ziel = Manager.stringscanner().toUpperCase();

            zaehler++;
        }

        zaehler = 0;
        String entscheidung = " ";

        while (! entscheidung.equalsIgnoreCase("j") && ! entscheidung.equalsIgnoreCase("n")) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie j oder n ein.");
            }
            UIHelper.druckeEingabeaufforderung("Möchten Sie Ihrer Suche einen Startflughafen hinzufügen? [j/n]");

            entscheidung = Manager.stringscanner();

            if (entscheidung.equalsIgnoreCase("n")) {
                startUeberspringen = true;
            }

            zaehler++;
        }

        zaehler = 0;

        if (! startUeberspringen) {
            while (start.length() != 3 ||
                   ! Character.isAlphabetic(start.charAt(0)) ||
                   ! Character.isAlphabetic(start.charAt(1)) ||
                   ! Character.isAlphabetic(start.charAt(2))) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungültige Eingabe. Beispiel: FRA");
                }

                UIHelper.druckeEingabeaufforderung("IATA-Code des Startflughafens (optional):");
                start = Manager.stringscanner().toUpperCase();

                zaehler++;
            }
        }

        zaehler = 0;
        entscheidung = " ";

        while (! entscheidung.equalsIgnoreCase("j") && ! entscheidung.equalsIgnoreCase("n")) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie j oder n ein.");
            }
            UIHelper.druckeEingabeaufforderung("Möchten Sie Ihrer Suche eine Flugnummer hinzufügen? [j/n]");

            entscheidung = Manager.stringscanner();

            if (entscheidung.equalsIgnoreCase("n")) {
                flugnummerUeberspringen = true;
            }

            zaehler++;
        }

        zaehler = 0;
        String datumsString = "";

        if (! flugnummerUeberspringen) {

            while (flugnummer.length() != 5 ||
                   ! Character.isAlphabetic(flugnummer.charAt(0)) ||
                   ! Character.isAlphabetic(flugnummer.charAt(1)) ||
                   ! Character.isDigit((flugnummer.charAt(2))) ||
                   ! Character.isDigit((flugnummer.charAt(3))) ||
                   ! Character.isDigit((flugnummer.charAt(4)))) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungültige Eingabe. Beispiel: LH123");
                }

                UIHelper.druckeEingabeaufforderung("Flugnummer (optional):");
                flugnummer = Manager.stringscanner().toUpperCase();

                zaehler++;
            }

            zaehler = 0;
            entscheidung = " ";

            while (! entscheidung.equalsIgnoreCase("j") && ! entscheidung.equalsIgnoreCase("n")) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie j oder n ein.");
                }
                UIHelper.druckeEingabeaufforderung("Möchten Sie Ihrer Flugnummer ein Datum hinzufügen? [j/n]");

                entscheidung = Manager.stringscanner();

                if (entscheidung.equalsIgnoreCase("n")) {
                    datumUeberspringen = true;
                }

                zaehler++;
            }

            zaehler = 0;

            if (! datumUeberspringen) {

                String datumRegex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

                while (! datumsString.matches(datumRegex)) {
                    if (zaehler != 0) {
                        UIHelper.druckeFehler("Ungültige Eingabe. Beispiel: 01.01.2026");
                    }
                    UIHelper.druckeEingabeaufforderung("Bitte geben Sie ein Datum (dd.MM.yyyy) ein:");
                    datumsString = Manager.stringscanner();
                    zaehler++;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                datum = LocalDate.parse(datumsString, formatter);
            }
        } else {

            zaehler = 0;
            entscheidung = " ";

            while (! entscheidung.equalsIgnoreCase("j") && ! entscheidung.equalsIgnoreCase("n")) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungültige Eingabe. Bitte geben Sie j oder n ein.");
                }
                UIHelper.druckeEingabeaufforderung("Möchten Sie Ihrer Suche ein Datum hinzufügen? [j/n]");

                entscheidung = Manager.stringscanner();

                if (entscheidung.equalsIgnoreCase("n")) {
                    datumUeberspringen = true;
                }

                zaehler++;
            }
        }

        zaehler = 0;

        if (! datumUeberspringen) {

            String datumRegex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

            while (! datumsString.matches(datumRegex)) {
                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungültige Eingabe. Beispiel: 01.01.2026");
                }
                UIHelper.druckeEingabeaufforderung("Bitte geben Sie ein Datum (dd.MM.yyyy) ein:");
                datumsString = Manager.stringscanner();
                zaehler++;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            datum = LocalDate.parse(datumsString, formatter);
        }

        if (! flugnummer.isBlank()) {

            if (datumUeberspringen) {
                fluege.addAll(vs.sucheFluegeNachNummer(flugnummer));
            } else {
                fluege.add(vs.sucheFlugNachNummer(flugnummer, datum));
            }

        } else if (! datumUeberspringen) {

            ArrayList<Flug> fluegeMitGleichemDatum = vs.sucheFluegeNachDatum(datum);

            if (! start.isBlank() && ! ziel.isBlank()) {

                ArrayList<Flug> fluegeMitGleicherRoute = vs.sucheFluegeNachRoute(vs.getFlughafenNachCode(start),
                        vs.getFlughafenNachCode(ziel)
                );

                for (Flug f : fluegeMitGleichemDatum) {
                    if (fluegeMitGleicherRoute.contains(f)) {
                        fluege.add(f);
                    }
                }

            } else if (! ziel.isBlank()) {

                ArrayList<Flug> fluegeMitGleichemZiel = vs.sucheFluegeNachZiel(vs.getFlughafenNachCode(ziel));

                for (Flug f : fluegeMitGleichemDatum) {
                    if (fluegeMitGleichemZiel.contains(f)) {
                        fluege.add(f);
                    }
                }

            }

        } else if (! start.isBlank() && ! ziel.isBlank()) {
            fluege.addAll(
                    vs.sucheFluegeNachRoute(
                            vs.getFlughafenNachCode(start),
                            vs.getFlughafenNachCode(ziel)
                    )
            );

        } else if (! ziel.isBlank()) {
            fluege.addAll(
                    vs.sucheFluegeNachZiel(
                            vs.getFlughafenNachCode(ziel)
                    )
            );
        }

        return fluege;
    }
}
