package avigator.verwaltung;

import avigator.modell.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code avigator.verwaltung.Buchungssystem} repräsentiert den kompletten Buchungsprozess. Sie dient dazu, die Klassen zu
 * koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 *
 * @author Marcel Marxkors
 * @version 1.1
 */

public class Buchungssystem implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Liste der Buchungen, die schon vorgenommen wurden
     */
    private final ArrayList<Buchung> buchungen;

    /**
     * Liste der schon registrierten Passagiere
     */
    private final ArrayList<Passagier> passagiere;

    /**
     * Anzahl der insgesamt getätigten Buchungsnummern, erste Idee einer möglichen Grundlage für die Buchungsnummer
     */
    private int anzahlBuchungen;

    /**
     * Anzahl der insgesamt registrierten Passagiere, erste Idee einer möglichen Grundlage für die Passagiernummer
     */
    private int anzahlPassagiere;

    /**
     * Konstruktor der Klasse, der die temporäre Lösung der Datenspeicherung initialisiert
     */
    public Buchungssystem() {

        buchungen = new ArrayList<>();
        passagiere = new ArrayList<>();
        anzahlBuchungen = 0;
        anzahlPassagiere = 0;
    }

    /**
     * Erstellt und registriert einen neuen avigator.modell.Passagier.
     * <p>
     * Für den avigator.modell.Passagier wird automatisch eine fortlaufende avigator.modell.Passagier-ID erzeugt. Kann der avigator.modell.Passagier aufgrund ungültiger
     * Daten nicht erstellt werden, wird der interne Zähler wieder zurückgesetzt.
     *
     * @param name  der Name des Passagiers
     * @param email die E-Mail-Adresse des Passagiers
     * @return der neu erstellte und registrierte avigator.modell.Passagier
     * @throws IllegalArgumentException wenn Name oder E-Mail-Adresse ungültig oder {@code null} sind
     */
    public Passagier initialisierePassagier(String name, String email) {

        if (name != null && email != null) {

            anzahlPassagiere++;

            try {

                String vorlaeufigePassagierId = "p" + anzahlPassagiere;
                Passagier passagier = new Passagier(vorlaeufigePassagierId, name, email);
                passagiere.add(passagier);
                return passagier;

            } catch (IllegalArgumentException e) {

                anzahlPassagiere--;
                throw e;
            }

        } else if (name == null) {
            throw new IllegalArgumentException("Fehler! Es wurde kein Name eingegeben.");
        } else {
            throw new IllegalArgumentException("Fehler! Es wurde keine Mail-Adresse eingegeben.");
        }

    }

    /**
     * Erstellt und registriert eine neue avigator.modell.Buchung für einen avigator.modell.Passagier.
     * <p>
     * Vor der avigator.modell.Buchung wird geprüft, ob avigator.modell.Passagier und avigator.modell.Flug im jeweiligen System vorhanden sind und ob der gewünschte
     * avigator.modell.Sitzplatz für die angegebene avigator.modell.Sitzklasse gültig und frei ist.
     * <p>
     * Bei erfolgreicher avigator.modell.Buchung wird eine Buchungsnummer vergeben, der avigator.modell.Sitzplatz belegt und die avigator.modell.Buchung im
     * avigator.verwaltung.Buchungssystem gespeichert.
     *
     * @param passagier         der avigator.modell.Passagier, für den die avigator.modell.Buchung erstellt wird
     * @param flug              der zu buchende avigator.modell.Flug
     * @param sitzplatznummer   die Nummer des gewünschten Sitzplatzes
     * @param anzahlKoffer      die Anzahl der aufzugebenden Koffer
     * @param sitzklasse        die gewünschte avigator.modell.Sitzklasse
     * @param verwaltungssystem das avigator.verwaltung.Verwaltungssystem zur Prüfung des Fluges
     * @return die neu erstellte avigator.modell.Buchung
     * @throws IllegalArgumentException wenn übergebene Werte ungültig sind oder avigator.modell.Passagier, avigator.modell.Flug beziehungsweise
     *                                  avigator.modell.Sitzplatz nicht gültig sind
     */
    public Buchung buchungVornehmen(Passagier passagier, Flug flug, String sitzplatznummer, int anzahlKoffer,
                                    Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem
    ) {

        if ((passagier == null || flug == null || sitzplatznummer == null || sitzklasse == null)) {
            throw new IllegalArgumentException("Fehler! Mindestens einer der übergebenen Werte ist ungültig.");
        }

        // Existiert der avigator.modell.Passagier im System? Wenn nicht: Fehler
        if (!passagiere.contains(passagier)) {
            throw new IllegalArgumentException(
                    "Der übergebene avigator.modell.Passagier existiert nicht (mehr) im System. Bitte einen anderen avigator.modell.Passagier wählen oder die avigator.modell.Buchung neu vornehmen!");
        }

        // Existiert der avigator.modell.Flug im System? Wenn nicht: Fehler
        if (!verwaltungssystem.getFluege().contains(flug)) {
            throw new IllegalArgumentException(
                    "Der übergebene avigator.modell.Flug existiert nicht (mehr) im System. Bitte einen anderen avigator.modell.Flug wählen oder die avigator.modell.Buchung neu vornehmen!");
        }


        // Objekte holen / erstellen
        // Der avigator.modell.Sitzplatz referiert einen avigator.modell.Sitzplatz im avigator.modell.Flugzeug, daher wirkt sich die
        // Belegung dieses Sitzplatzes auch auf den avigator.modell.Flug aus.
        Sitzplatz sitzplatz = flug.findeSitzplatz(sitzplatznummer);
        GepaeckInformation gepaeckInfo = new GepaeckInformation(anzahlKoffer);
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);

        // Platz validieren und ggf. belegen
        flug.validiereSitzplatz(sitzplatz, sitzklasse, klassenliste);

        Buchung buchung = new Buchung(passagier, flug, sitzplatz, gepaeckInfo);

        buchung.setBuchungsnummer("bu" + anzahlBuchungen);
        anzahlBuchungen++;
        sitzplatz.belegen(buchung);
        buchungen.add(buchung);

        return buchung;
    }

    /**
     * Sucht die bisherigen Buchungen nach einer Buchungsnummer ab
     *
     * @return die avigator.modell.Buchung mit der Buchungsnummer
     * @throws IllegalArgumentException wenn die avigator.modell.Buchung nicht oder vorhanden ist
     */
    public Buchung sucheBuchungNachNummer(String buchungsnummer) {

        for (Buchung b : buchungen) {
            if (b.getBuchungsnummer().equals(buchungsnummer)) {
                return b;
            }
        }

        throw new IllegalArgumentException(
                "Fehler! Die avigator.modell.Buchung mit der Buchungsnummer " + buchungsnummer + " ist nicht vorhanden.");
    }

    /**
     * Bucht eine bestehende avigator.modell.Buchung auf einen anderen avigator.modell.Flug beziehungsweise avigator.modell.Sitzplatz um.
     * <p>
     * Vor der Änderung wird die Umbuchung validiert und der zusätzlich zu zahlende Umbuchungsbetrag berechnet.
     * Anschließend werden der neue avigator.modell.Sitzplatz, avigator.modell.Flug und Buchungspreis übernommen, der avigator.modell.Buchungsstatus auf
     * {@code UMGEBUCHT} gesetzt und der bisherige avigator.modell.Sitzplatz freigegeben.
     *
     * @param buchung           die umzubuchende avigator.modell.Buchung
     * @param flug              der neue avigator.modell.Flug
     * @param sitzplatznummer   die Nummer des neuen Sitzplatzes
     * @param sitzklasse        die avigator.modell.Sitzklasse des neuen Sitzplatzes
     * @param verwaltungssystem das avigator.verwaltung.Verwaltungssystem zur Validierung des Fluges
     */
    public void umbuchen(Buchung buchung,
                         Flug flug,
                         String sitzplatznummer,
                         Sitzklasse sitzklasse,
                         Verwaltungssystem verwaltungssystem
    ) {

        boolean umbuchungMoeglich;
        double gebuehr;

        // Umbuchung prüfen
        umbuchungMoeglich = validiereUmbuchung(buchung, flug, sitzplatznummer, sitzklasse, verwaltungssystem);

        if (umbuchungMoeglich) {
            // alten avigator.modell.Sitzplatz merken
            Sitzplatz alterSitzplatz = buchung.getSitzplatz();

            // neuen avigator.modell.Sitzplatz ermitteln
            Sitzplatz neuerSitzplatz = flug.findeSitzplatz(sitzplatznummer);

            // Umbuchungsgebühr berechnen
            gebuehr = berechneUmbuchungsgebuehr(buchung, flug, neuerSitzplatz);

            // neuen avigator.modell.Sitzplatz belegen
            neuerSitzplatz.belegen(buchung);

            // avigator.modell.Buchung aktualisieren
            buchung.setGezahlteUmbuchungsgebuehr(gebuehr);
            buchung.setSitzplatz(neuerSitzplatz);
            buchung.setFlug(flug);
            buchung.aktualisiereGezahltenPreis();
            buchung.setBuchungsstatus(Buchungsstatus.UMGEBUCHT);

            // Alter avigator.modell.Sitzplatz wird erst freigegeben, nachdem die avigator.modell.Buchung vollständig vorbereitet wurde
            alterSitzplatz.freigeben();
        }

    }

    /**
     * Prüft, ob eine Umbuchung mit den angegebenen Daten durchgeführt werden kann.
     * <p>
     * Dabei werden unter anderem die Existenz des neuen Fluges, der avigator.modell.Buchungsstatus sowie avigator.modell.Sitzplatz und avigator.modell.Sitzklasse
     * geprüft. Abhängig davon, ob innerhalb desselben Fluges oder auf einen anderen avigator.modell.Flug umgebucht wird, erfolgt die
     * entsprechende Sitzplatzvalidierung.
     *
     * @param buchung             die umzubuchende avigator.modell.Buchung
     * @param neuerFlug           der gewünschte neue avigator.modell.Flug
     * @param neueSitzplatznummer die Nummer des gewünschten neuen Sitzplatzes
     * @param sitzklasse          die gewünschte avigator.modell.Sitzklasse
     * @param verwaltungssystem   das avigator.verwaltung.Verwaltungssystem zur Prüfung des neuen Fluges
     * @return {@code true}, wenn die Umbuchung gültig ist
     * @throws NoSuchElementException wenn benötigte Buchungs- oder Flugdaten fehlen
     * @throws IllegalStateException  wenn die avigator.modell.Buchung storniert oder vergangen ist
     */
    private boolean validiereUmbuchung(Buchung buchung, Flug neuerFlug, String neueSitzplatznummer,
                                      Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem
    ) {

        // wenn die avigator.modell.Buchung nicht vorhanden ist
        if (buchung == null) {
            throw new NoSuchElementException("Es ist keine avigator.modell.Buchung angegeben, von der umgebucht werden soll.");
        }

        // wenn neuer avigator.modell.Flug gar nicht im System existiert
        else if (!verwaltungssystem.getFluege().contains(neuerFlug)) {
            throw new NoSuchElementException(
                    "Der avigator.modell.Flug, auf den umgebucht werden soll, ist nicht (mehr) im System registriert.");
        }

        // wenn beide Buchungsparameter leer sind
        else if (neuerFlug == null && neueSitzplatznummer == null) {
            throw new NoSuchElementException("Beide Buchungsparameter sind leer.");
        }

        // wenn schon storniert oder der avigator.modell.Flug bereits in der Vergangenheit liegt
        else if (buchung.getBuchungsstatus() == Buchungsstatus.STORNIERT ||
                buchung.getBuchungsstatus() == Buchungsstatus.VERGANGEN) {
            throw new IllegalStateException("Stornierte oder vergangene Buchungen können nicht umgebucht werden.");
        }

        // wenn keine avigator.modell.Sitzklasse angegeben wurde
        else if (sitzklasse == null) {
            throw new NoSuchElementException("Es wurde keine avigator.modell.Sitzklasse angegeben.");
        }

        // wenn im selben avigator.modell.Flug ein anderer avigator.modell.Sitzplatz gebucht werden muss
        else if (neuerFlug == buchung.getFlug() || neuerFlug == null) {
            return validiereUmbuchungimSelbenFlug(buchung, neueSitzplatznummer, sitzklasse);
        }

        // wenn im neuen avigator.modell.Flug ein avigator.modell.Sitzplatz gebucht werden muss(Prüft, ob die
        // Sitzplatznummer im neuen avigator.modell.Flug vorhanden ist und ob der avigator.modell.Sitzplatz belegt ist)
        else {
            return validiereUmbuchungimNeuenFlug(neuerFlug, neueSitzplatznummer, sitzklasse);
        }

    }

    /**
     * Prüft, ob eine Umbuchung im selben avigator.modell.Flug möglich ist.
     *
     * @param buchung             : Aktuelle avigator.modell.Buchung
     * @param neueSitzplatznummer : Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          : avigator.modell.Sitzklasse, die der neue avigator.modell.Sitzplatz haben soll
     * @return true, wenn die Validierung erfolgreich ist
     */
    private boolean validiereUmbuchungimSelbenFlug(Buchung buchung, String neueSitzplatznummer, Sitzklasse sitzklasse) {

        Sitzplatz sitz = buchung.getFlug().findeSitzplatz(neueSitzplatznummer);
        List<Sitzplatz> klassenliste = buchung.getFlug().getFreieSitzplaetzeNachKlasse(sitzklasse);

        buchung.getFlug().validiereSitzplatz(sitz, sitzklasse, klassenliste);

        return true;
    }

    /**
     * Prüft, ob eine avigator.modell.Buchung im neuen avigator.modell.Flug gültig ist
     *
     * @param flug                : neuer avigator.modell.Flug, auf den umgebucht werden soll
     * @param neueSitzplatznummer : Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          : avigator.modell.Sitzklasse des neuen Sitzplatzes
     * @return true, wenn die Validierung erfolgreich ist
     */
    private boolean validiereUmbuchungimNeuenFlug(Flug flug, String neueSitzplatznummer, Sitzklasse sitzklasse) {

        Sitzplatz sitz = flug.findeSitzplatz(neueSitzplatznummer);
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);
        flug.validiereSitzplatz(sitz, sitzklasse, klassenliste);

        return true;
    }

    /**
     * Berechnet den bei einer Umbuchung zusätzlich zu zahlenden Betrag.
     * <p>
     * Der Betrag setzt sich aus der festen Umbuchungsgebühr und einer möglichen positiven Differenz zwischen dem
     * bisherigen und dem neuen Buchungspreis zusammen. Ist der neue avigator.modell.Flug günstiger, wird die Preisdifferenz nicht
     * erstattet.
     *
     * @param buchung   die bisherige avigator.modell.Buchung
     * @param flug      der neue avigator.modell.Flug
     * @param sitzplatz der neue avigator.modell.Sitzplatz
     * @return der zusätzlich zu zahlende Umbuchungsbetrag
     */
    public double berechneUmbuchungsgebuehr(Buchung buchung, Flug flug, Sitzplatz sitzplatz) {

        // Legt zuerst die Variablen für die Berechnung fest
        double preisAlterFlug = buchung.getGezahlterPreis();
        double preisNeuerFlug, ticketDifferenz, finaleGebuehr;

        // berechnet den neuen Preis anhand der avigator.modell.Sitzklasse
        if (sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS) {
            preisNeuerFlug = flug.getBasispreis() * buchung.getBusinesspreisfaktor();
        } else {
            preisNeuerFlug = flug.getBasispreis();
        }

        //ergänze Koffergebühr für den neuen avigator.modell.Flug
        preisNeuerFlug += buchung.getGepaeckinformation().berechneGepaeckgebuehr();

        //berechnet die Differenz aus altem und neuen avigator.modell.Flug
        ticketDifferenz = preisNeuerFlug - preisAlterFlug;

        // berechnet die finale Gebühr und schlägt die Ticketdifferenz auf, wenn diese
        // positiv ist
        finaleGebuehr = buchung.getUmbuchungsgebuehr() + Math.max(0, ticketDifferenz);

        return finaleGebuehr;
    }

    /**
     * storniert eine vorhandene avigator.modell.Buchung; ändert den avigator.modell.Buchungsstatus und gibt den avigator.modell.Sitzplatz der avigator.modell.Buchung frei
     *
     * @param buchung , die storniert werden soll
     * @return die Storno-Gebühr
     * @throws NoSuchElementException   wenn die zu stornierende avigator.modell.Buchung nicht in der Liste "buchungen" ist
     * @throws IllegalArgumentException wenn keine avigator.modell.Buchung übergeben wurde, oder wenn die avigator.modell.Buchung schon storniert wurde,
     *                                  oder schon vergangen ist.
     */
    @SuppressWarnings("UnusedReturnValue") // TODO: Muss geklärt werden
    public double stornieren(Buchung buchung) {

        double betrag;

        if (buchung == null) {
            throw new IllegalArgumentException("Die avigator.modell.Buchung enthält eine null-Referenz");
        } else if (buchungen.contains(buchung)) {

            if (buchung.getBuchungsstatus() == Buchungsstatus.AKTIV
                    || buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT) {

                betrag = buchung.stornierenMitGebuehr();

                buchung.setBuchungsstatus(Buchungsstatus.STORNIERT);
                buchung.getSitzplatz().freigeben();

            } else {
                throw new IllegalArgumentException(
                        "Sie können eine bereits stornierte oder vergangene avigator.modell.Buchung nicht stornieren!");
            }

        } else {
            throw new NoSuchElementException(
                    "Die avigator.modell.Buchung ist nicht im System vorhanden und kann daher nicht storniert werden!");
        }

        return betrag;
    }

    /**
     * Durchsucht die vorhandenen Buchungen nach solchen, die den angegebenen avigator.modell.Flug beinhalten und noch nicht storniert
     * wurden
     *
     * @param flug , der auf nicht stornierte Buchungen überprüft werden soll
     * @return Liste an Buchungen, die die Kriterien erfüllen
     */
    public ArrayList<Buchung> findeRelevanteBuchungen(Flug flug) {

        ArrayList<Buchung> relevanteBuchungen = new ArrayList<>();

        for (Buchung buchung : buchungen) {

            if (buchung.getFlug().equals(flug) &&
                    buchung.getBuchungsstatus() != Buchungsstatus.STORNIERT &&
                    buchung.getBuchungsstatus() != Buchungsstatus.VERGANGEN) {

                relevanteBuchungen.add(buchung);
            }
        }

        return relevanteBuchungen;
    }

    /**
     * Ändert die Anzahl der gebuchten Koffer einer bestehenden avigator.modell.Buchung.
     * <p>
     * Die Änderung ist nur für vorhandene Buchungen mit dem Status {@code AKTIV} oder {@code UMGEBUCHT} möglich. Nach
     * der Änderung der Gepäckmenge wird der Buchungspreis neu berechnet.
     *
     * @param buchung          die zu ändernde avigator.modell.Buchung
     * @param neueAnzahlKoffer die neue Anzahl der gebuchten Koffer
     * @throws IllegalArgumentException wenn keine avigator.modell.Buchung angegeben wurde oder die Kofferanzahl negativ ist
     * @throws NoSuchElementException   wenn die avigator.modell.Buchung nicht im System vorhanden ist
     * @throws IllegalStateException    wenn der avigator.modell.Buchungsstatus keine Änderung erlaubt
     */
    public void gepaeckAendern(Buchung buchung, int neueAnzahlKoffer) {

        if (buchung == null) {
            throw new IllegalArgumentException("Es wurde keine avigator.modell.Buchung angegeben.");
        }

        if (!buchungen.contains(buchung)) {
            throw new NoSuchElementException("Die avigator.modell.Buchung ist nicht im System vorhanden.");
        }

        if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {
            throw new IllegalStateException("Das Gepäck kann bei dieser avigator.modell.Buchung nicht mehr geändert werden.");
        }

        if (neueAnzahlKoffer < 0) {
            throw new IllegalArgumentException("Die Anzahl der Koffer darf nicht negativ sein.");
        }

        buchung.getGepaeckinformation().setAnzahlKoffer(neueAnzahlKoffer);
        buchung.aktualisiereGezahltenPreis();
    }

    /**
     *
     * @return Liste aller getätigten Buchungen
     */
    public List<Buchung> getBuchungen() {
        return List.copyOf(buchungen);
    }

    /**
     * Gibt alle registrierten Passagiere zurück.
     *
     * @return unveränderbare Kopie der registrierten Passagiere
     */
    public List<Passagier> getPassagiere() {
        return List.copyOf(passagiere);
    }
}