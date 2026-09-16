package avigator.frontend;

import avigator.modell.*;
import avigator.verwaltung.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Die Klasse {@code UIKunde} stellt die Konsolenoberflaeche fuer Kunden bereit.
 * <p>
 * Kunden koennen sich neu registrieren oder ein bereits vorhandenes Kundenkonto auswaehlen. Nach der Anmeldung koennen sie
 * Fluege suchen und buchen, bestehende Buchungen umbuchen oder stornieren und ihre Buchungen anzeigen lassen.
 * <p>
 * aenderungen an den Daten werden mithilfe des {@link DatenHandler} gespeichert.
 *
 * @author Lars Pfeiffer, Cedric Beckmann
 * @version 1.2
 */
public class UIKunde {

    /**
     * DatenHandler zum dauerhaften Speichern der Anwendungsdaten.
     */
    private final DatenHandler datenHandler;

    /**
     * Enthaelt die aktuell verwendeten Anwendungsdaten.
     */
    private final Anwendungsdaten anwendungsdaten;

    /**
     * Buchungssystem zur Verwaltung von Passagieren und Buchungen.
     */
    private final Buchungssystem buchungssystem;

    /**
     * Verwaltungssystem zur Verwaltung und Suche von Fluegen und Flughaefen.
     */
    private final Verwaltungssystem verwaltungssystem;

    /**
     * Formatiert Datum und Uhrzeit im Format {@code dd.MM.yyyy HH:mm}.
     */
    private static final DateTimeFormatter datumZeitFormatierer = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Erstellt eine neue Kundenoberflaeche. Das Buchungs- und Verwaltungssystem werden aus den uebergebenen
     * Anwendungsdaten uebernommen.
     *
     * @param datenHandler    der Handler zum Speichern der Anwendungsdaten
     * @param anwendungsdaten geladene oder neu erzeugte Anwendungsdaten
     * @throws IllegalArgumentException wenn der DatenHandler oder die Anwendungsdaten {@code null} sind
     */
    public UIKunde(DatenHandler datenHandler, Anwendungsdaten anwendungsdaten) {

        if (datenHandler == null) {
            throw new IllegalArgumentException("Der DatenHandler darf nicht null sein.");
        }

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten duerfen nicht null sein.");
        }

        this.datenHandler = datenHandler;
        this.anwendungsdaten = anwendungsdaten;
        this.buchungssystem = anwendungsdaten.getBuchungssystem();
        this.verwaltungssystem = anwendungsdaten.getVerwaltungssystem();
    }

    /**
     * Zeigt das Anmeldemenue fuer Kunden an.
     * <p>
     * Der Benutzer kann einen neuen Kunden anlegen, einen bestehenden Passagier ueber dessen ID auswaehlen oder zum
     * vorherigen Menue zurueckkehren.
     * <p>
     * Nach einer erfolgreichen Anmeldung wird das Kundenhauptmenue geoeffnet.
     */
    public void kunde() {

        while (true) {

            UIHelper.druckeUeberschrift("Herzlich willkommen");

            UIHelper.druckeMenuepunkt(1, "Als neuer Kunde registrieren");
            UIHelper.druckeMenuepunkt(2, "Als bestehender Kunde anmelden");
            UIHelper.druckeMenuepunkt(0, "Zurueck");

            UIHelper.druckeTrennlinie();

            int auswahl = Manager.intscanner();

            switch (auswahl) {

                case 1:
                    String name;

                    // Erfasst zunaechst einen gueltigen Namen, bevor das Kundenkonto mit der E-Mail angelegt wird.
                    while (true) {

                        UIHelper.druckeEingabeaufforderung("Bitte geben Sie Ihren Namen ein:");
                        name = Manager.stringscanner();

                        if (!name.isBlank()) {
                            break;
                        }

                        UIHelper.druckeFehler("Der Name darf nicht leer sein.");
                    }

                    while (true) {

                        UIHelper.druckeEingabeaufforderung("Bitte geben Sie Ihre E-Mail ein:");
                        String mail = Manager.stringscanner();

                        try {

                            // Speichert den neuen Passagier vor dem Wechsel in dessen persoenlichen Kundenbereich.
                            Passagier passagier = buchungssystem.initialisierePassagier(name, mail);

                            datenHandler.speichere(anwendungsdaten);

                            UIHelper.druckeErfolg("Sie wurden erfolgreich registriert.");

                            hauptmanagerKunde(passagier);

                            return;

                        } catch (Exception e) {
                            UIHelper.druckeFehler(e.getMessage());
                        }
                    }

                case 2:

                    if (buchungssystem.getPassagiere().isEmpty()) {

                        UIHelper.druckeHinweis("Es sind noch keine Kunden vorhanden.");
                        break;
                    }

                    UIHelper.druckeUeberschrift("Passagierauswahl");

                    for (Passagier passagier : buchungssystem.getPassagiere()) {

                        System.out.println(passagier.passagierId() +
                                           " - " +
                                           passagier.name() +
                                           " - " +
                                           passagier.email());
                    }

                    UIHelper.druckeTrennlinie();

                    UIHelper.druckeEingabeaufforderung("Bitte waehlen Sie einen Passagier ueber die ID.");
                    UIHelper.druckeEingabeaufforderung("Geben Sie 0 ein, um zurueckzukehren.");

                    // Wiederholt die Suche, bis eine vorhandene Passagier-ID gewaehlt oder der Vorgang abgebrochen wird.
                    while (true) {

                        String id = Manager.stringscanner();

                        if (id.equals("0")) {
                            break;
                        }

                        Passagier ausgewaehlterPassagier = null;

                        for (Passagier passagier : buchungssystem.getPassagiere()) {

                            if (passagier.passagierId().equalsIgnoreCase(id)) {

                                ausgewaehlterPassagier = passagier;
                                break;
                            }
                        }

                        if (ausgewaehlterPassagier == null) {

                            UIHelper.druckeFehler("Diese Passagier-ID existiert nicht.");
                            UIHelper.druckeEingabeaufforderung(
                                    "Bitte geben Sie eine gueltige Passagier-ID ein oder 0 zum Zurueckkehren:");

                            continue;
                        }

                        UIHelper.druckeErfolg("Angemeldet als " + ausgewaehlterPassagier.name() + ".");

                        hauptmanagerKunde(ausgewaehlterPassagier);

                        return;
                    }

                case 0:
                    return;

                default:
                    UIHelper.druckeFehler("Ungueltige Eingabe.");
                    break;

            }
        }

    }

    /**
     * Zeigt das Kundenhauptmenue fuer einen angemeldeten Passagier an.
     * <p>
     * Von diesem Menue aus kann der Kunde Fluege suchen und buchen, bestehende Buchungen umbuchen oder stornieren sowie
     * seine aktuellen Buchungen anzeigen lassen und die Anzahl der Gepaeckstuecke anpassen.
     *
     * @param passagier der aktuell angemeldete Passagier
     */
    private void hauptmanagerKunde(Passagier passagier) {

        while (true) {

            UIHelper.druckeUeberschrift("Willkommen " + passagier.name() + " im Kundenbereich");

            UIHelper.druckeEingabeaufforderung("Was moechten Sie tun?");

            UIHelper.druckeMenuepunkt(1, "Fluege suchen und buchen");
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
                    UIHelper.druckeFehler("Ungueltige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;

            }
        }
    }

    /**
     * Zeigt das Verwaltungsmenue fuer die Buchungen eines Passagiers an.
     * <p>
     * ueber das Menue koennen Buchungen angezeigt, umgebucht oder storniert sowie Gepaeckinformationen geaendert werden. Das
     * Menue wird so lange angezeigt, bis der Benutzer zum Kundenhauptmenue zurueckkehrt.
     *
     * @param passagier der aktuell angemeldete Passagier
     */
    private void buchungenVerwalten(Passagier passagier) {

        while (true) {

            UIHelper.druckeUeberschrift("Buchungen verwalten");

            UIHelper.druckeMenuepunkt(1, "Buchungen anzeigen");
            UIHelper.druckeMenuepunkt(2, "Buchung umbuchen");
            UIHelper.druckeMenuepunkt(3, "Buchung stornieren");
            UIHelper.druckeMenuepunkt(4, "Gepaeck aendern");
            UIHelper.druckeMenuepunkt(0, "Zurueck");

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
                    UIHelper.druckeFehler("Ungueltige Auswahl. Bitte geben Sie eine der angezeigten Zahlen ein.");
                    break;
            }
        }
    }

    /**
     * Ermoeglicht einem Passagier die Suche und Buchung eines Fluges.
     * <p>
     * Vor der Suche werden vergangene Fluege entfernt und betroffene Buchungen entsprechend aktualisiert. Anschließend
     * kann die Flugsuche anhand verschiedener Kriterien eingeschraenkt werden.
     * <p>
     * Nach Auswahl eines Fluges, eines freien Sitzplatzes und der gewuenschten Gepaeckmenge wird eine Buchungsvorschau
     * mit Ticketpreis, Gepaeckkosten und Gesamtpreis angezeigt. Die Buchung wird erst nach einer ausdruecklichen
     * Bestaetigung des Benutzers durchgefuehrt und anschließend gespeichert.
     *
     * @param passagier der Passagier, fuer den der Flug gebucht wird
     */
    private void fluegeSuchenUndBuchen(Passagier passagier) {

        UIHelper.druckeUeberschrift("Fluege suchen und buchen");

        // Aktualisiert vergangene Fluege und Buchungen, bevor daraus eine buchbare Auswahl entsteht.
        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        this.druckeFlughaefen();

        try {

            ArrayList<Flug> fluege = sucheFluege();

            if (fluege.isEmpty()) {
                UIHelper.druckeHinweis("Keine Fluege gefunden.");
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
                        "Bitte geben Sie die Anzahl der Koffer ein, die Sie aufgeben moechten:");

                koffer = Manager.intscanner();

                if (koffer < 0) {

                    UIHelper.druckeFehler("Die Anzahl der Koffer darf nicht negativ sein.");
                    continue;
                }

                break;
            }

            // Buchungsvorschau, damit der Kunde den ausgewaehlten Flug akzeptieren kann
            Buchung buchungsvorschau = new Buchung(passagier,
                    flug,
                    flug.findeSitzplatz(sitzplatz),
                    new GepaeckInformation(koffer)
            );

            UIHelper.druckeUeberschrift("Buchungsuebersicht");

            System.out.printf("%-13s%s%n", "Flug:", flug.getFlugnummer());
            System.out.printf("%-13s%s%n", "Airline:", flug.getFluggesellschaft().getName());
            System.out.printf("%-13s%s -> %s%n",
                    "Route:",
                    flug.getStartFlughafen().iataCode(),
                    flug.getZielflughafen().iataCode()
            );

            System.out.printf("%-13s%s%n", "Abflug:", flug.getAbflugszeit().format(datumZeitFormatierer));

            System.out.printf("%-13s%s%n", "Sitzplatz:", sitzplatz);
            System.out.printf("%-13s%s%n", "Sitzklasse:", sitzklasse);
            System.out.printf("%-13s%d%n", "Koffer:", koffer);

            double gepaeckpreis = buchungsvorschau.getGepaeckinformation().berechneGepaeckgebuehr();
            double ticketpreis = buchungsvorschau.getGezahlterPreis() - gepaeckpreis;

            System.out.printf("%-13s%.2f Euro%n", "Ticketpreis:", ticketpreis);
            System.out.printf("%-13s%.2f Euro%n", "Gepaeck:", gepaeckpreis);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-13s%.2f Euro%n", "Gesamtpreis:", buchungsvorschau.getGezahlterPreis());

            UIHelper.druckeTrennlinie();

            if (!bestaetigungEinlesen("Moechten Sie die Buchung verbindlich durchfuehren?")) {

                UIHelper.druckeHinweis("Die Buchung wurde abgebrochen.");
                return;
            }

            // Erst nach der Bestaetigung wird der Sitz belegt und die Buchung im Buchungssystem registriert.
            Buchung buchung = buchungssystem.buchungVornehmen(passagier, flug, sitzplatz, koffer, sitzklasse,
                    verwaltungssystem
            );

            UIHelper.druckeUeberschrift("Buchungsbestaetigung");

            System.out.println("Buchungsnummer: " + buchung.getBuchungsnummer());
            System.out.println("Flug:           " + buchung.getFlug().getFlugnummer());
            System.out.println("Sitzplatz:      " + buchung.getSitzplatz().getSitzplatzNummer());
            System.out.println("Sitzklasse:     " + buchung.getSitzplatz().getSitzklasse());
            System.out.println("Koffer:         "
                               + buchung.getGepaeckinformation().getAnzahlKoffer());

            System.out.printf("Gesamtpreis:    %.2f Euro%n", buchung.getGezahlterPreis());

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Buchung wurde erfolgreich durchgefuehrt.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Gibt alle im Verwaltungssystem registrierten Flughaefen in einer tabellarischen uebersicht aus.
     */
    private void druckeFlughaefen() {

        UIHelper.druckeEingabeaufforderung("Verfuegbare Flughaefen:");

        UIHelper.druckeTrennlinie();

        System.out.printf("%-6s | %-50s | %-25s | %-15s%n", "Code", "Flughafen", "Ort", "Land");

        UIHelper.druckeTrennlinie();

        for (Flughafen flughafen : verwaltungssystem.getFlughaefen()) {

            System.out.printf("%-6s | %-50s | %-25s | %-15s%n",
                    flughafen.iataCode(),
                    flughafen.name(),
                    flughafen.stadt(),
                    flughafen.land()
            );
        }

        UIHelper.druckeTrennlinie();
    }

    /**
     * Gibt die uebergebenen Fluege nummeriert in einer tabellarischen uebersicht aus.
     * <p>
     * Angezeigt werden Flugnummer, Fluggesellschaft, Route sowie Abflug- und Ankunftszeit.
     *
     * @param fluege die anzuzeigenden Fluege
     */
    private void druckeFluege(ArrayList<Flug> fluege) {

        UIHelper.druckeUeberschrift("Gefundene Fluege");

        System.out.printf("%-4s | %-8s | %-25s | %-11s | %-17s | %-17s%n",
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
                    "%-4d | %-8s | %-25s | %-11s | %-17s | %-17s%n",
                    i + 1,
                    flug.getFlugnummer(),
                    flug.getFluggesellschaft().getName(),
                    flug.getStartFlughafen().iataCode() + " -> " + flug.getZielflughafen().iataCode(),
                    flug.getAbflugszeit().format(datumZeitFormatierer),
                    flug.getAnkunftszeit().format(datumZeitFormatierer)
            );
        }

        UIHelper.druckeTrennlinie();
    }

    /**
     * Liest die Auswahl eines Fluges aus einer zuvor nummerierten Flugliste ein.
     * <p>
     * Ungueltige Nummern werden abgewiesen und erneut abgefragt.
     *
     * @param fluege die zur Auswahl stehenden Fluege
     * @return der vom Benutzer ausgewaehlte Flug
     */
    private Flug flugAuswaehlen(ArrayList<Flug> fluege) {

        Flug flug = null;

        while (flug == null) {

            UIHelper.druckeEingabeaufforderung(
                    "Bitte geben Sie die Nummer Ihres gewuenschten Fluges ein:"
            );

            int auswahl = Manager.intscanner();

            if (auswahl < 1 || auswahl > fluege.size()) {

                UIHelper.druckeFehler("Ungueltige Flugauswahl.");
                continue;
            }

            flug = fluege.get(auswahl - 1);
        }

        return flug;
    }

    /**
     * Zeigt den Sitzplan eines Fluges an und liest die gewuenschte Sitzplatznummer ein.
     * <p>
     * Nicht vorhandene oder bereits belegte Sitzplaetze werden abgewiesen und erneut abgefragt.
     *
     * @param flug der Flug, fuer den ein Sitzplatz ausgewaehlt werden soll
     * @return die Nummer des ausgewaehlten freien Sitzplatzes
     */
    private String sitzplatzAuswaehlen(Flug flug) {

        UIHelper.druckeUeberschrift("Sitzplatzauswahl");

        flug.zeigeSitzplan();

        while (true) {

            UIHelper.druckeEingabeaufforderung(
                    "Bitte geben Sie die gewuenschte Sitzplatznummer ein:"
            );

            String sitzplatz = Manager.stringscanner().toUpperCase();

            Sitzplatz ausgewaehlterSitzplatz = flug.findeSitzplatz(sitzplatz);

            if (ausgewaehlterSitzplatz == null) {

                UIHelper.druckeFehler("Dieser Sitzplatz existiert nicht.");
                continue;
            }

            if (!ausgewaehlterSitzplatz.getIstFrei()) {

                UIHelper.druckeFehler("Dieser Sitzplatz ist bereits belegt.");
                continue;
            }

            return sitzplatz;
        }
    }

    /**
     * Ermoeglicht das Umbuchen einer bestehenden Buchung des angegebenen Passagiers.
     * <p>
     * Zunaechst werden vergangene Fluege und die zugehoerigen Buchungsstatus aktualisiert. Eine Umbuchung ist nur moeglich,
     * wenn mindestens eine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Nach Auswahl der Buchung wird geprueft, ob diese dem angemeldeten Passagier gehoert und noch umgebucht werden darf.
     * Anschließend waehlt der Benutzer einen neuen Flug und einen freien Sitzplatz aus.
     * <p>
     * Vor der Durchfuehrung werden der bisherige und der neue Buchungspreis, die Umbuchungsgebuehr sowie der zusaetzlich
     * zu zahlende Betrag angezeigt. Die Umbuchung wird erst nach einer ausdruecklichen Bestaetigung durchgefuehrt und
     * anschließend gespeichert.
     * <p>
     * Nach erfolgreicher Umbuchung kann optional auch die Anzahl der gebuchten Koffer geaendert werden.
     *
     * @param passagier der Passagier, dessen Buchung umgebucht werden soll
     */
    private void umbuchen(Passagier passagier) {

        UIHelper.druckeUeberschrift("Buchung umbuchen");

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        if (hatKeineBearbeitbarenBuchungen(passagier)) {

            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, die umgebucht werden koennen.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung("Bitte geben Sie die Buchungsnummer der umzubuchenden Buchung ein:");

        String nummer = Manager.stringscanner();

        Buchung buchung;

        try {

            buchung = buchungssystem.sucheBuchungNachNummer(nummer);

            if (!buchung.getPassagier().equals(passagier)) {

                UIHelper.druckeFehler("Diese Buchung gehoert nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() == Buchungsstatus.STORNIERT ||
                buchung.getBuchungsstatus() == Buchungsstatus.VERGANGEN) {

                UIHelper.druckeFehler("Stornierte oder vergangene Buchungen koennen nicht umgebucht werden.");
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

                UIHelper.druckeHinweis("Keine Fluege gefunden.");
                return;
            }

            druckeFluege(fluege);

            Flug neuerFlug = flugAuswaehlen(fluege);

            String sitzplatz = sitzplatzAuswaehlen(neuerFlug);
            Sitzplatz neuerSitzplatz = neuerFlug.findeSitzplatz(sitzplatz);
            Sitzklasse sitzklasse = neuerSitzplatz.getSitzklasse();

            // Ermittelt den neuen Preis mit einer Vorschau, ohne die bestehende Buchung bereits zu veraendern.
            Buchung buchungsvorschau = new Buchung(passagier,
                    neuerFlug,
                    neuerSitzplatz,
                    new GepaeckInformation(buchung.getGepaeckinformation().getAnzahlKoffer())
            );

            double bisherigerBuchungspreis = buchung.getGezahlterPreis();
            double neuerBuchungspreis = buchungsvorschau.getGezahlterPreis();
            double umbuchungsgebuehr = buchung.getUmbuchungsgebuehr();
            double zusaetzlichZuZahlen = buchungssystem.berechneUmbuchungsgebuehr(buchung, neuerFlug, neuerSitzplatz);

            UIHelper.druckeUeberschrift("Umbuchungsuebersicht");

            System.out.printf("%-27s%s | %s%n",
                    "Bisheriger Flug:",
                    buchung.getFlug().getFlugnummer(),
                    buchung.getFlug().getAbflugszeit().format(datumZeitFormatierer)
            );

            System.out.printf("%-27s%s | %s%n",
                    "Neuer Flug:",
                    neuerFlug.getFlugnummer(),
                    neuerFlug.getAbflugszeit().format(datumZeitFormatierer)
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
            System.out.printf("%-27s%.2f Euro%n", "Umbuchungsgebuehr:", umbuchungsgebuehr);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-27s%.2f Euro%n", "Zusaetzlich zu zahlen:", zusaetzlichZuZahlen);

            UIHelper.druckeTrennlinie();

            if (!bestaetigungEinlesen("Moechten Sie die Umbuchung verbindlich durchfuehren?")) {

                UIHelper.druckeHinweis("Die Umbuchung wurde abgebrochen.");
                return;
            }

            buchungssystem.umbuchen(buchung, neuerFlug, sitzplatz, sitzklasse, verwaltungssystem);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Umbuchung wurde erfolgreich durchgefuehrt.");

            UIHelper.druckeTrennlinie();

            if (bestaetigungEinlesen("Moechten Sie die Anzahl Ihrer Koffer ebenfalls aendern?")) {
                gepaeckAendern(buchung);
            }

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Ermoeglicht die Stornierung einer bestehenden Buchung des angegebenen Passagiers.
     * <p>
     * Zunaechst werden vergangene Fluege und die zugehoerigen Buchungsstatus aktualisiert. Eine Stornierung ist nur
     * moeglich, wenn mindestens eine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Nach Auswahl der Buchung wird geprueft, ob diese dem angemeldeten Passagier gehoert und noch storniert werden darf.
     * Vor der Durchfuehrung werden der aktuelle Buchungspreis, die Stornierungsgebuehr und der daraus resultierende
     * Erstattungsbetrag angezeigt.
     * <p>
     * Die Stornierung wird erst nach einer ausdruecklichen Bestaetigung durchgefuehrt und anschließend gespeichert.
     *
     * @param passagier der Passagier, dessen Buchung storniert werden soll
     */
    private void stornieren(Passagier passagier) {

        UIHelper.druckeUeberschrift("Buchung stornieren");

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        if (hatKeineBearbeitbarenBuchungen(passagier)) {

            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, die storniert werden koennen.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung(
                "Bitte geben Sie die Buchungsnummer der Buchung ein, die Sie stornieren moechten:");

        String nummer = Manager.stringscanner();

        try {

            Buchung buchung = buchungssystem.sucheBuchungNachNummer(nummer);

            if (!buchung.getPassagier().equals(passagier)) {

                UIHelper.druckeFehler("Diese Buchung gehoert nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {

                UIHelper.druckeFehler("Stornierte oder vergangene Buchungen koennen nicht erneut storniert werden.");
                return;
            }

            double buchungspreis = buchung.getGezahlterPreis();
            double stornierungsgebuehr = buchung.getStornierungsgebuehr();
            // Die feste Stornierungsgebuehr kann hoechstens den gesamten Buchungspreis aufzehren.
            double erstattungsbetrag = Math.max(0, buchungspreis - stornierungsgebuehr);

            UIHelper.druckeUeberschrift("Stornierungsuebersicht");

            System.out.printf("%-22s%s%n", "Buchungsnummer:", buchung.getBuchungsnummer());

            System.out.printf("%-22s%s | %s%n", "Flug:",
                    buchung.getFlug().getFlugnummer(),
                    buchung.getFlug().getAbflugszeit().format(datumZeitFormatierer)
            );

            System.out.printf("%-22s%s -> %s%n", "Route:",
                    buchung.getFlug().getStartFlughafen().iataCode(),
                    buchung.getFlug().getZielflughafen().iataCode()
            );

            System.out.printf("%-22s%.2f Euro%n", "Buchungspreis:", buchungspreis);

            System.out.printf("%-22s%.2f Euro%n", "Stornierungsgebuehr:", stornierungsgebuehr);

            UIHelper.druckeTrennlinie();

            System.out.printf("%-22s%.2f Euro%n", "Erstattungsbetrag:", erstattungsbetrag);

            UIHelper.druckeTrennlinie();

            if (!bestaetigungEinlesen("Moechten Sie die Buchung wirklich stornieren?")) {

                UIHelper.druckeHinweis("Die Stornierung wurde abgebrochen.");
                return;
            }

            buchungssystem.stornieren(buchung);
            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Die Buchung wurde erfolgreich storniert.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }

    }

    /**
     * Zeigt alle Buchungen des angegebenen Passagiers in einer tabellarischen uebersicht an.
     * <p>
     * Vor der Anzeige werden vergangene Fluege entfernt und die zugehoerigen Buchungsstatus aktualisiert. Angezeigt
     * werden unter anderem Flug, Route, Abflugzeit, Sitzplatz, Sitzklasse, Gepaeck, Buchungspreis und Buchungsstatus.
     * <p>
     * Sind fuer den Passagier keine Buchungen vorhanden, wird ein entsprechender Hinweis ausgegeben.
     *
     * @param passagier der Passagier, dessen Buchungen angezeigt werden
     */
    private void buchungenAnzeigen(Passagier passagier) {

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

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

        // Beschraenkt die uebersicht auf Buchungen des aktuell angemeldeten Passagiers.
        for (Buchung buchung : buchungssystem.getBuchungen()) {

            if (buchung.getPassagier().equals(passagier)) {

                buchungVorhanden = true;

                Flug flug = buchung.getFlug();

                System.out.printf(
                        "%-8s | %-8s | %-11s | %-17s | %-6s | %-10s | %-6d | %-12s | %-10s%n",
                        buchung.getBuchungsnummer(),
                        flug.getFlugnummer(),
                        flug.getStartFlughafen().iataCode() + " -> " + flug.getZielflughafen().iataCode(),
                        flug.getAbflugszeit().format(datumZeitFormatierer),
                        buchung.getSitzplatz().getSitzplatzNummer(),
                        buchung.getSitzplatz().getSitzklasse(),
                        buchung.getGepaeckinformation().getAnzahlKoffer(),
                        String.format("%.2f Euro", buchung.getGezahlterPreis()),
                        buchung.getBuchungsstatus()
                );
            }
        }

        if (!buchungVorhanden) {
            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen.");
        }
    }

    /**
     * Ermoeglicht die Auswahl einer Buchung, deren Gepaeckmenge geaendert werden soll.
     * <p>
     * Zunaechst werden vergangene Fluege und die zugehoerigen Buchungsstatus aktualisiert. Sind keine bearbeitbaren
     * Buchungen vorhanden, wird der Vorgang beendet.
     * <p>
     * Anschließend waehlt der Passagier eine Buchung ueber deren Buchungsnummer aus. Es wird geprueft, ob die Buchung dem
     * angemeldeten Passagier gehoert und ob ihr Status eine Gepaeckaenderung erlaubt. Die eigentliche aenderung wird
     * anschließend an {@link #gepaeckAendern(Buchung)} uebergeben.
     *
     * @param passagier der Passagier, dessen Gepaeckinformationen geaendert werden sollen
     */
    private void gepaeckAendern(Passagier passagier) {

        UIHelper.druckeUeberschrift("Gepaeck aendern");

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

        if (hatKeineBearbeitbarenBuchungen(passagier)) {

            UIHelper.druckeHinweis("Sie haben derzeit keine Buchungen, bei denen das Gepaeck geaendert werden kann.");
            return;
        }

        buchungenAnzeigen(passagier);

        UIHelper.druckeTrennlinie();

        UIHelper.druckeEingabeaufforderung(
                "Bitte geben Sie die Buchungsnummer der Buchung ein, bei der Sie das Gepaeck anpassen wollen:");

        String nummer = Manager.stringscanner();

        try {

            Buchung buchung = buchungssystem.sucheBuchungNachNummer(nummer);

            if (!buchung.getPassagier().equals(passagier)) {

                UIHelper.druckeFehler("Diese Buchung gehoert nicht zu diesem Passagier.");
                return;
            }

            if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {

                UIHelper.druckeFehler("Das Gepaeck kann bei dieser Buchung nicht mehr geaendert werden.");
                return;
            }

            gepaeckAendern(buchung);

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * aendert die Anzahl der Gepaeckstuecke einer bestehenden Buchung.
     * <p>
     * Zunaechst werden die aktuell gebuchte und die gewuenschte neue Anzahl der Koffer ermittelt. Negative Werte werden
     * abgewiesen und bei unveraenderter Kofferanzahl wird der Vorgang beendet.
     * <p>
     * Vor der tatsaechlichen aenderung wird anhand einer Buchungsvorschau der neue Buchungspreis ermittelt. Anschließend
     * werden der bisherige und der neue Buchungspreis sowie der zusaetzlich zu zahlende Betrag beziehungsweise der
     * Erstattungsbetrag angezeigt.
     * <p>
     * Die Gepaeckaenderung wird erst nach einer ausdruecklichen Bestaetigung durchgefuehrt und anschließend gespeichert.
     *
     * @param buchung die Buchung, deren Gepaeckinformationen geaendert werden sollen
     */
    private void gepaeckAendern(Buchung buchung) {

        verwaltungssystem.alteFluegeLoeschen(buchungssystem);

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

                    UIHelper.druckeHinweis("Die Anzahl der Koffer wurde nicht geaendert.");
                    return;
                }

                break;
            }

            // Berechnet die Preisaenderung zunaechst mit separaten Gepaeckdaten fuer die Vorschau.
            Buchung buchungsvorschau = new Buchung(buchung.getPassagier(),
                    buchung.getFlug(),
                    buchung.getSitzplatz(),
                    new GepaeckInformation(neueAnzahl)
            );

            double bisherigerPreis = buchung.getGezahlterPreis();
            double neuerPreis = buchungsvorschau.getGezahlterPreis();
            double differenz = neuerPreis - bisherigerPreis;

            UIHelper.druckeUeberschrift("Gepaeckaenderung");

            System.out.printf("%-27s%d%n", "Bisherige Koffer:", aktuelleAnzahl);

            System.out.printf("%-27s%d%n", "Neue Koffer:", neueAnzahl);

            System.out.printf("%-27s%.2f Euro%n", "Bisheriger Buchungspreis:", bisherigerPreis);

            System.out.printf("%-27s%.2f Euro%n", "Neuer Buchungspreis:", neuerPreis);

            UIHelper.druckeTrennlinie();

            if (differenz > 0) {
                System.out.printf("%-27s%.2f Euro%n", "Zusaetzlich zu zahlen:", differenz);
            } else {
                System.out.printf("%-27s%.2f Euro%n", "Erstattungsbetrag:", Math.abs(differenz));
            }

            UIHelper.druckeTrennlinie();

            if (!bestaetigungEinlesen("Moechten Sie die Gepaeckaenderung verbindlich durchfuehren?")) {

                UIHelper.druckeHinweis("Die Gepaeckaenderung wurde abgebrochen.");
                return;
            }

            buchungssystem.gepaeckAendern(buchung, neueAnzahl);

            datenHandler.speichere(anwendungsdaten);

            UIHelper.druckeErfolg("Das Gepaeck wurde erfolgreich geaendert.");

        } catch (Exception e) {
            UIHelper.druckeFehler(e.getMessage());
        }
    }

    /**
     * Liest eine Ja-Nein-Bestaetigung des Benutzers ein.
     * <p>
     * Akzeptiert werden die Eingaben {@code j} und {@code n} unabhaengig von Groß- und Kleinschreibung. Ungueltige
     * Eingaben werden erneut abgefragt.
     *
     * @param text der Text der Bestaetigungsfrage
     * @return {@code true} bei einer Bestaetigung mit {@code j}, sonst {@code false}
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

            UIHelper.druckeFehler("Ungueltige Eingabe. Bitte geben Sie j oder n ein.");
        }
    }

    /**
     * Prueft, ob fuer den angegebenen Passagier keine bearbeitbare Buchung vorhanden ist.
     * <p>
     * Als bearbeitbar gelten Buchungen mit dem Status {@code AKTIV} oder {@code UMGEBUCHT}.
     *
     * @param passagier der zu ueberpruefende Passagier
     * @return {@code true}, wenn keine bearbeitbare Buchung vorhanden ist, sonst {@code false}
     */
    private boolean hatKeineBearbeitbarenBuchungen(Passagier passagier) {

        for (Buchung buchung : buchungssystem.getBuchungen()) {

            if (buchung.getPassagier().equals(passagier)
                    && (buchung.getBuchungsstatus() == Buchungsstatus.AKTIV
                    || buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT)) {

                return false;
            }
        }

        return true;
    }

    /**
     * Liest die Suchkriterien fuer eine Flugsuche ein und ermittelt die dazu passenden Fluege.
     * <p>
     * Der Zielflughafen ist verpflichtend. Zusaetzlich koennen ein Startflughafen, eine Flugnummer und ein Datum als
     * weitere Suchkriterien angegeben werden. Bei einer angegebenen Flugnummer wird nach dieser und gegebenenfalls dem
     * Datum gesucht. Ohne Flugnummer werden Ziel, optionaler Start und optionales Datum miteinander kombiniert.
     *
     * @return die Liste der Fluege, die den angegebenen Suchkriterien entsprechen
     */
    @SuppressWarnings("DuplicatedCode")
    private ArrayList<Flug> sucheFluege() {

        ArrayList<Flug> fluege = new ArrayList<>();

        if (verwaltungssystem.keineFluegeVorhanden()) {
            return fluege;
        }

        String start = "", ziel = "", flugnummer = "";
        int zaehler = 0;
        LocalDate datum = null;

        boolean startUeberspringen = false, flugnummerUeberspringen = false, datumUeberspringen = false;

        // Der Zielflughafen bleibt das verpflichtende Mindestkriterium jeder Suche.
        while (ziel.length() != 3 ||
               !Character.isAlphabetic(ziel.charAt(0)) ||
               !Character.isAlphabetic(ziel.charAt(1)) ||
               !Character.isAlphabetic(ziel.charAt(2))) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungueltige Eingabe. Beispiel: FRA");
            }

            UIHelper.druckeEingabeaufforderung("IATA-Code des Zielflughafens (verpflichtend):");
            ziel = Manager.stringscanner().toUpperCase();

            zaehler++;
        }

        zaehler = 0;
        String entscheidung = " ";

        while (!entscheidung.equalsIgnoreCase("j") && !entscheidung.equalsIgnoreCase("n")) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungueltige Eingabe. Bitte geben Sie j oder n ein.");
            }

            UIHelper.druckeEingabeaufforderung("Moechten Sie Ihrer Suche einen Startflughafen hinzufuegen? [j/n]");

            entscheidung = Manager.stringscanner();

            if (entscheidung.equalsIgnoreCase("n")) {
                startUeberspringen = true;
            }

            zaehler++;
        }

        zaehler = 0;

        if (!startUeberspringen) {

            while (start.length() != 3 ||
                   !Character.isAlphabetic(start.charAt(0)) ||
                   !Character.isAlphabetic(start.charAt(1)) ||
                   !Character.isAlphabetic(start.charAt(2))) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungueltige Eingabe. Beispiel: FRA");
                }

                UIHelper.druckeEingabeaufforderung("IATA-Code des Startflughafens:");
                start = Manager.stringscanner().toUpperCase();

                zaehler++;
            }
        }

        zaehler = 0;
        entscheidung = " ";

        while (!entscheidung.equalsIgnoreCase("j") && !entscheidung.equalsIgnoreCase("n")) {

            if (zaehler != 0) {
                UIHelper.druckeFehler("Ungueltige Eingabe. Bitte geben Sie j oder n ein.");
            }

            UIHelper.druckeEingabeaufforderung("Moechten Sie Ihrer Suche eine Flugnummer hinzufuegen? [j/n]");

            entscheidung = Manager.stringscanner();

            if (entscheidung.equalsIgnoreCase("n")) {
                flugnummerUeberspringen = true;
            }

            zaehler++;
        }

        zaehler = 0;
        String datumsString = "";

        if (!flugnummerUeberspringen) {

            // Eine Flugnummer kann wahlweise allein oder zusammen mit einem Datum gesucht werden.
            while (flugnummer.length() != 5 ||
                   !Character.isAlphabetic(flugnummer.charAt(0)) ||
                   !Character.isAlphabetic(flugnummer.charAt(1)) ||
                   !Character.isDigit((flugnummer.charAt(2))) ||
                   !Character.isDigit((flugnummer.charAt(3))) ||
                   !Character.isDigit((flugnummer.charAt(4)))) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungueltige Eingabe. Beispiel: LH123");
                }

                UIHelper.druckeEingabeaufforderung("Flugnummer:");

                flugnummer = Manager.stringscanner().toUpperCase();

                zaehler++;
            }

            zaehler = 0;
            entscheidung = " ";

            while (!entscheidung.equalsIgnoreCase("j") && !entscheidung.equalsIgnoreCase("n")) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungueltige Eingabe. Bitte geben Sie j oder n ein.");
                }

                UIHelper.druckeEingabeaufforderung("Moechten Sie Ihrer Flugnummer ein Datum hinzufuegen? [j/n]");

                entscheidung = Manager.stringscanner();

                if (entscheidung.equalsIgnoreCase("n")) {
                    datumUeberspringen = true;
                }

                zaehler++;
            }

            zaehler = 0;

            if (!datumUeberspringen) {

                String datumRegex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

                while (!datumsString.matches(datumRegex)) {

                    if (zaehler != 0) {
                        UIHelper.druckeFehler("Ungueltige Eingabe. Beispiel: 01.01.2026");
                    }

                    UIHelper.druckeEingabeaufforderung("Bitte geben Sie ein Datum (dd.MM.yyyy) ein:");

                    datumsString = Manager.stringscanner();
                    zaehler++;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                datum = LocalDate.parse(datumsString, formatter);
            }
        } else {

            entscheidung = " ";

            while (!entscheidung.equalsIgnoreCase("j") && !entscheidung.equalsIgnoreCase("n")) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungueltige Eingabe. Bitte geben Sie j oder n ein.");
                }

                UIHelper.druckeEingabeaufforderung("Moechten Sie Ihrer Suche ein Datum hinzufuegen? [j/n]");

                entscheidung = Manager.stringscanner();

                if (entscheidung.equalsIgnoreCase("n")) {
                    datumUeberspringen = true;
                }

                zaehler++;
            }
        }

        zaehler = 0;

        if (!datumUeberspringen) {

            String datumRegex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

            while (!datumsString.matches(datumRegex)) {

                if (zaehler != 0) {
                    UIHelper.druckeFehler("Ungueltige Eingabe. Beispiel: 01.01.2026");
                }

                UIHelper.druckeEingabeaufforderung("Bitte geben Sie ein Datum (dd.MM.yyyy) ein:");

                datumsString = Manager.stringscanner();
                zaehler++;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            datum = LocalDate.parse(datumsString, formatter);
        }

        // Waehlt anhand der angegebenen Kriterien die passende Verwaltungssuche oder kombiniert deren Ergebnisse.
        if (!flugnummer.isBlank()) {

            if (datumUeberspringen) {
                fluege.addAll(verwaltungssystem.sucheFluegeNachNummer(flugnummer));
            } else {
                fluege.add(verwaltungssystem.sucheFlugNachNummer(flugnummer, datum));
            }

        } else if (!datumUeberspringen) {

            ArrayList<Flug> fluegeMitGleichemDatum = verwaltungssystem.sucheFluegeNachDatum(datum);

            if (!start.isBlank() && !ziel.isBlank()) {

                ArrayList<Flug> fluegeMitGleicherRoute = verwaltungssystem.sucheFluegeNachRoute(verwaltungssystem.getFlughafenNachCode(start),
                        verwaltungssystem.getFlughafenNachCode(ziel)
                );

                // uebernimmt nur Fluege, die sowohl am gewuenschten Datum als auch auf der Route liegen.
                for (Flug flug : fluegeMitGleichemDatum) {

                    if (fluegeMitGleicherRoute.contains(flug)) {
                        fluege.add(flug);
                    }
                }

            } else if (!ziel.isBlank()) {

                ArrayList<Flug> fluegeMitGleichemZiel = verwaltungssystem.sucheFluegeNachZiel(verwaltungssystem.getFlughafenNachCode(ziel));

                // Schneidet die Datumsergebnisse mit den Fluegen zum gewuenschten Ziel.
                for (Flug flug : fluegeMitGleichemDatum) {

                    if (fluegeMitGleichemZiel.contains(flug)) {
                        fluege.add(flug);
                    }
                }

            }

        } else if (!start.isBlank() && !ziel.isBlank()) {

            fluege.addAll(
                    verwaltungssystem.sucheFluegeNachRoute(
                            verwaltungssystem.getFlughafenNachCode(start),
                            verwaltungssystem.getFlughafenNachCode(ziel)
                    )
            );

        } else if (!ziel.isBlank()) {

            fluege.addAll(
                    verwaltungssystem.sucheFluegeNachZiel(
                            verwaltungssystem.getFlughafenNachCode(ziel)
                    )
            );
        }

        return fluege;
    }
}
