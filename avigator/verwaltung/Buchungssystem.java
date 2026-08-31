package avigator.verwaltung;

import avigator.modell.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten Buchungsprozess. Sie dient dazu, die Klassen zu
 * koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 *
 * @author Marcel Marxkors
 * @version 1.2
 */

public class Buchungssystem implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Liste der bereits vorgenommenen Buchungen.
     */
    private final ArrayList<Buchung> buchungen;

    /**
     * Liste der registrierten Passagiere.
     */
    private final ArrayList<Passagier> passagiere;

    /**
     * Zähler zur Erzeugung fortlaufender Buchungsnummern.
     */
    private int anzahlBuchungen;

    /**
     * Zähler zur Erzeugung fortlaufender Passagier-IDs.
     */
    private int anzahlPassagiere;

    /**
     * Erstellt ein leeres Buchungssystem ohne Passagiere und Buchungen.
     */
    public Buchungssystem() {

        buchungen = new ArrayList<>();
        passagiere = new ArrayList<>();
        anzahlBuchungen = 0;
        anzahlPassagiere = 0;
    }

    /**
     * Erstellt und registriert einen neuen Passagier.
     * <p>
     * Für den Passagier wird automatisch eine fortlaufende Passagier-ID erzeugt. Kann der Passagier aufgrund ungültiger
     * Daten nicht erstellt werden, wird der interne Zähler wieder zurückgesetzt.
     *
     * @param name  der Name des Passagiers
     * @param email die E-Mail-Adresse des Passagiers
     * @return der neu erstellte und registrierte Passagier
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

                // Ungültige Passagierdaten dürfen keine Lücke in der fortlaufenden ID erzeugen
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
     * Erstellt und registriert eine neue Buchung für einen Passagier.
     * <p>
     * Vor der Buchung wird geprüft, ob Passagier und Flug im jeweiligen System vorhanden sind und ob der gewünschte
     * Sitzplatz für die angegebene Sitzklasse gültig und frei ist.
     * <p>
     * Bei erfolgreicher Buchung wird eine Buchungsnummer vergeben, der Sitzplatz belegt und die Buchung im
     * Buchungssystem gespeichert.
     *
     * @param passagier         der Passagier, für den die Buchung erstellt wird
     * @param flug              der zu buchende Flug
     * @param sitzplatznummer   die Nummer des gewünschten Sitzplatzes
     * @param anzahlKoffer      die Anzahl der aufzugebenden Koffer
     * @param sitzklasse        die gewünschte Sitzklasse
     * @param verwaltungssystem das Verwaltungssystem zur Prüfung des Fluges
     * @return die neu erstellte Buchung
     * @throws IllegalArgumentException wenn übergebene Werte ungültig sind oder Passagier, Flug beziehungsweise
     *                                  Sitzplatz nicht gültig sind
     * @throws NoSuchElementException   wenn der angegebene Sitzplatz nicht vorhanden ist
     */
    public Buchung buchungVornehmen(Passagier passagier, Flug flug, String sitzplatznummer, int anzahlKoffer,
                                    Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem
    ) {

        if ((passagier == null || flug == null || sitzplatznummer == null || sitzklasse == null)) {
            throw new IllegalArgumentException("Fehler! Mindestens einer der übergebenen Werte ist ungültig.");
        }

        if (!passagiere.contains(passagier)) {
            throw new IllegalArgumentException(
                    "Der übergebene Passagier existiert nicht (mehr) im System. Bitte einen anderen Passagier wählen oder die Buchung neu vornehmen!");
        }

        if (!verwaltungssystem.getFluege().contains(flug)) {
            throw new IllegalArgumentException(
                    "Der übergebene Flug existiert nicht (mehr) im System. Bitte einen anderen Flug wählen oder die Buchung neu vornehmen!");
        }

        // Bereitet Sitzplatz und Gepäckdaten vor und prüft den Sitz, bevor die Buchung angelegt wird.
        Sitzplatz sitzplatz = flug.findeSitzplatz(sitzplatznummer);
        GepaeckInformation gepaeckInfo = new GepaeckInformation(anzahlKoffer);
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);

        flug.validiereSitzplatz(sitzplatz, sitzklasse, klassenliste);

        Buchung buchung = new Buchung(passagier, flug, sitzplatz, gepaeckInfo);

        // Vergibt anschließend die fortlaufende Nummer und verknüpft Buchung und Sitzplatz miteinander.
        buchung.setBuchungsnummer("bu" + anzahlBuchungen);
        anzahlBuchungen++;
        sitzplatz.belegen(buchung);
        buchungen.add(buchung);

        return buchung;
    }

    /**
     * Sucht die bisherigen Buchungen nach einer Buchungsnummer ab.
     *
     * @param buchungsnummer die Nummer der gesuchten Buchung
     * @return die Buchung mit der Buchungsnummer
     * @throws IllegalArgumentException wenn die Buchung nicht vorhanden ist
     */
    public Buchung sucheBuchungNachNummer(String buchungsnummer) {

        for (Buchung b : buchungen) {
            if (b.getBuchungsnummer().equals(buchungsnummer)) {
                return b;
            }
        }

        throw new IllegalArgumentException(
                "Fehler! Die Buchung mit der Buchungsnummer " + buchungsnummer + " ist nicht vorhanden.");
    }

    /**
     * Bucht eine bestehende Buchung auf einen anderen Flug beziehungsweise Sitzplatz um.
     * <p>
     * Vor der Änderung wird die Umbuchung validiert und der zusätzlich zu zahlende Umbuchungsbetrag berechnet.
     * Anschließend werden der neue Sitzplatz, Flug und Buchungspreis übernommen, der Buchungsstatus auf
     * {@code UMGEBUCHT} gesetzt und der bisherige Sitzplatz freigegeben.
     *
     * @param buchung           die umzubuchende Buchung
     * @param flug              der neue Flug
     * @param sitzplatznummer   die Nummer des neuen Sitzplatzes
     * @param sitzklasse        die Sitzklasse des neuen Sitzplatzes
     * @param verwaltungssystem das Verwaltungssystem zur Validierung des Fluges
     * @throws NoSuchElementException   wenn erforderliche Buchungs-, Flug- oder Sitzplatzdaten fehlen
     * @throws IllegalArgumentException wenn der Sitzplatz belegt ist oder nicht zur gewünschten Sitzklasse gehört
     * @throws IllegalStateException    wenn die Buchung storniert oder vergangen ist
     */
    public void umbuchen(Buchung buchung,
                         Flug flug,
                         String sitzplatznummer,
                         Sitzklasse sitzklasse,
                         Verwaltungssystem verwaltungssystem
    ) {

        boolean umbuchungMoeglich;
        double gebuehr;

        umbuchungMoeglich = validiereUmbuchung(buchung, flug, sitzplatznummer, sitzklasse, verwaltungssystem);

        if (umbuchungMoeglich) {
            Sitzplatz alterSitzplatz = buchung.getSitzplatz();

            Sitzplatz neuerSitzplatz = flug.findeSitzplatz(sitzplatznummer);

            // Berechnet den Mehrbetrag noch mit den bisherigen Buchungsdaten, bevor Flug und Sitz ersetzt werden.
            gebuehr = berechneUmbuchungsgebuehr(buchung, flug, neuerSitzplatz);

            neuerSitzplatz.belegen(buchung);

            buchung.setGezahlteUmbuchungsgebuehr(gebuehr);
            buchung.setSitzplatz(neuerSitzplatz);
            buchung.setFlug(flug);
            buchung.aktualisiereGezahltenPreis();
            buchung.setBuchungsstatus(Buchungsstatus.UMGEBUCHT);

            // Der alte Sitzplatz bleibt bis zur vollständigen Aktualisierung der Buchung belegt
            alterSitzplatz.freigeben();
        }

    }

    /**
     * Prüft, ob eine Umbuchung mit den angegebenen Daten durchgeführt werden kann.
     * <p>
     * Dabei werden unter anderem die Existenz des neuen Fluges, der Buchungsstatus sowie Sitzplatz und Sitzklasse
     * geprüft. Abhängig davon, ob innerhalb desselben Fluges oder auf einen anderen Flug umgebucht wird, erfolgt die
     * entsprechende Sitzplatzvalidierung.
     *
     * @param buchung             die umzubuchende Buchung
     * @param neuerFlug           der gewünschte neue Flug
     * @param neueSitzplatznummer die Nummer des gewünschten neuen Sitzplatzes
     * @param sitzklasse          die gewünschte Sitzklasse
     * @param verwaltungssystem   das Verwaltungssystem zur Prüfung des neuen Fluges
     * @return {@code true}, wenn die Umbuchung gültig ist
     * @throws NoSuchElementException wenn benötigte Buchungs- oder Flugdaten fehlen
     * @throws IllegalArgumentException wenn der Sitzplatz belegt ist oder nicht zur gewünschten Sitzklasse gehört
     * @throws IllegalStateException  wenn die Buchung storniert oder vergangen ist
     */
    private boolean validiereUmbuchung(Buchung buchung, Flug neuerFlug, String neueSitzplatznummer,
                                      Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem
    ) {

        if (buchung == null) {
            throw new NoSuchElementException("Es ist keine Buchung angegeben, von der umgebucht werden soll.");
        }

        else if (!verwaltungssystem.getFluege().contains(neuerFlug)) {
            throw new NoSuchElementException(
                    "Der Flug, auf den umgebucht werden soll, ist nicht (mehr) im System registriert.");
        }

        else if (neuerFlug == null && neueSitzplatznummer == null) {
            throw new NoSuchElementException("Beide Buchungsparameter sind leer.");
        }

        else if (buchung.getBuchungsstatus() == Buchungsstatus.STORNIERT ||
                buchung.getBuchungsstatus() == Buchungsstatus.VERGANGEN) {
            throw new IllegalStateException("Stornierte oder vergangene Buchungen können nicht umgebucht werden.");
        }

        else if (sitzklasse == null) {
            throw new NoSuchElementException("Es wurde keine Sitzklasse angegeben.");
        }

        // Im selben Flug wird gegen dessen Sitzplan geprüft, andernfalls gegen den Sitzplan des neuen Flugs.
        else if (neuerFlug == buchung.getFlug() || neuerFlug == null) {
            return validiereUmbuchungimSelbenFlug(buchung, neueSitzplatznummer, sitzklasse);
        }

        else {
            return validiereUmbuchungimNeuenFlug(neuerFlug, neueSitzplatznummer, sitzklasse);
        }

    }

    /**
     * Prüft, ob eine Umbuchung im selben Flug möglich ist.
     *
     * @param buchung             die aktuelle Buchung
     * @param neueSitzplatznummer die Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          die Sitzklasse, die der neue Sitzplatz haben soll
     * @return {@code true}, wenn die Validierung erfolgreich ist
     * @throws NoSuchElementException   wenn der Sitzplatz nicht vorhanden ist
     * @throws IllegalArgumentException wenn der Sitzplatz belegt ist oder nicht zur gewünschten Sitzklasse gehört
     */
    private boolean validiereUmbuchungimSelbenFlug(Buchung buchung, String neueSitzplatznummer, Sitzklasse sitzklasse) {

        Sitzplatz sitz = buchung.getFlug().findeSitzplatz(neueSitzplatznummer);
        List<Sitzplatz> klassenliste = buchung.getFlug().getFreieSitzplaetzeNachKlasse(sitzklasse);

        buchung.getFlug().validiereSitzplatz(sitz, sitzklasse, klassenliste);

        return true;
    }

    /**
     * Prüft, ob eine Buchung im neuen Flug gültig ist.
     *
     * @param flug                der neue Flug, auf den umgebucht werden soll
     * @param neueSitzplatznummer die Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          die Sitzklasse des neuen Sitzplatzes
     * @return {@code true}, wenn die Validierung erfolgreich ist
     * @throws NoSuchElementException   wenn der Sitzplatz nicht vorhanden ist
     * @throws IllegalArgumentException wenn der Sitzplatz belegt ist oder nicht zur gewünschten Sitzklasse gehört
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
     * bisherigen und dem neuen Buchungspreis zusammen. Ist der neue Flug günstiger, wird die Preisdifferenz nicht
     * erstattet.
     *
     * @param buchung   die bisherige Buchung
     * @param flug      der neue Flug
     * @param sitzplatz der neue Sitzplatz
     * @return der zusätzlich zu zahlende Umbuchungsbetrag
     */
    public double berechneUmbuchungsgebuehr(Buchung buchung, Flug flug, Sitzplatz sitzplatz) {

        double preisAlterFlug = buchung.getGezahlterPreis();
        double preisNeuerFlug, ticketDifferenz, finaleGebuehr;

        // Für Business-Sitzplätze gilt derselbe Preisfaktor wie bei einer neuen Buchung
        if (sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS) {
            preisNeuerFlug = flug.getBasispreis() * buchung.getBusinesspreisfaktor();
        } else {
            preisNeuerFlug = flug.getBasispreis();
        }

        preisNeuerFlug += buchung.getGepaeckinformation().berechneGepaeckgebuehr();

        ticketDifferenz = preisNeuerFlug - preisAlterFlug;

        // Eine negative Preisdifferenz wird bei der Umbuchung nicht erstattet
        finaleGebuehr = buchung.getUmbuchungsgebuehr() + Math.max(0, ticketDifferenz);

        return finaleGebuehr;
    }

    /**
     * Storniert eine vorhandene Buchung, ändert den Buchungsstatus und gibt den Sitzplatz der Buchung frei.
     *
     * @param buchung die Buchung, die storniert werden soll
     * @return die Storno-Gebühr
     * @throws NoSuchElementException   wenn die zu stornierende Buchung nicht in der Liste "buchungen" ist
     * @throws IllegalArgumentException wenn keine Buchung übergeben wurde, die Buchung bereits storniert wurde oder
     *                                  bereits vergangen ist
     */
    @SuppressWarnings("UnusedReturnValue")
    public double stornieren(Buchung buchung) {

        double betrag;

        if (buchung == null) {
            throw new IllegalArgumentException("Die Buchung enthält eine null-Referenz");
        } else if (buchungen.contains(buchung)) {

            if (buchung.getBuchungsstatus() == Buchungsstatus.AKTIV
                    || buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT) {

                betrag = buchung.stornierenMitGebuehr();

                // Statusänderung und Sitzfreigabe machen den Platz wieder für andere Buchungen verfügbar.
                buchung.setBuchungsstatus(Buchungsstatus.STORNIERT);
                buchung.getSitzplatz().freigeben();

            } else {
                throw new IllegalArgumentException(
                        "Sie können eine bereits stornierte oder vergangene Buchung nicht stornieren!");
            }

        } else {
            throw new NoSuchElementException(
                    "Die Buchung ist nicht im System vorhanden und kann daher nicht storniert werden!");
        }

        return betrag;
    }

    /**
     * Durchsucht die vorhandenen Buchungen nach solchen, die den angegebenen Flug beinhalten und weder storniert noch
     * vergangen sind.
     *
     * @param flug der Flug, der auf nicht stornierte Buchungen überprüft werden soll
     * @return die Liste der Buchungen, die die Kriterien erfüllen
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
     * Ändert die Anzahl der gebuchten Koffer einer bestehenden Buchung.
     * <p>
     * Die Änderung ist nur für vorhandene Buchungen mit dem Status {@code AKTIV} oder {@code UMGEBUCHT} möglich. Nach
     * der Änderung der Gepäckmenge wird der Buchungspreis neu berechnet.
     *
     * @param buchung          die zu ändernde Buchung
     * @param neueAnzahlKoffer die neue Anzahl der gebuchten Koffer
     * @throws IllegalArgumentException wenn keine Buchung angegeben wurde oder die Kofferanzahl negativ ist
     * @throws NoSuchElementException   wenn die Buchung nicht im System vorhanden ist
     * @throws IllegalStateException    wenn der Buchungsstatus keine Änderung erlaubt
     */
    public void gepaeckAendern(Buchung buchung, int neueAnzahlKoffer) {

        if (buchung == null) {
            throw new IllegalArgumentException("Es wurde keine Buchung angegeben.");
        }

        if (!buchungen.contains(buchung)) {
            throw new NoSuchElementException("Die Buchung ist nicht im System vorhanden.");
        }

        if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV &&
                buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {
            throw new IllegalStateException("Das Gepäck kann bei dieser Buchung nicht mehr geändert werden.");
        }

        if (neueAnzahlKoffer < 0) {
            throw new IllegalArgumentException("Die Anzahl der Koffer darf nicht negativ sein.");
        }

        // Übernimmt die neue Gepäckmenge und hält den gespeicherten Buchungspreis damit synchron.
        buchung.getGepaeckinformation().setAnzahlKoffer(neueAnzahlKoffer);
        buchung.aktualisiereGezahltenPreis();
    }

    /**
     * Gibt alle getätigten Buchungen zurück.
     *
     * @return unveränderbare Kopie aller getätigten Buchungen
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
