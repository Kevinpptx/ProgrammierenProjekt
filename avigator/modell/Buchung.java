package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Die Klasse {@code Buchung} repraesentiert eine Flugbuchung eines Passagiers. Eine Buchung enthaelt Informationen ueber
 * den gebuchten Flug, den Sitzplatz, die Gepaeckinformationen, den Buchungsstatus sowie den gezahlten Preis.
 *
 * @author Kevin Braun
 * @version 1.1
 */
@SuppressWarnings("FieldCanBeLocal")
public class Buchung implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Festgelegte pauschale Umbuchungsgebuehr.
     */
    private final double umbuchungsGebuehr = 100.00;

    /**
     * Festgelegte pauschale Storno-Gebuehr.
     */
    private final double stornierungsGebuehr = 200.00;

    /**
     * Festgelegter pauschaler Preisfaktor, der auf Business-Fluege anfaellt.
     */
    private final double businessPreisFaktor = 1.65;

    /**
     * Die Nummer der Buchung, die anhand der Zahl der schon vorhandenen Buchungen generiert wird.
     */
    private String buchungsnummer;

    /**
     * Der Passagier dieser Buchung. Aus Komplexitaetsgruenden gehen wir davon aus, dass jeder Passagier seinen Flug
     * selber buchen muss.
     */
    private final Passagier passagier;

    /**
     * Der gebuchte Flug.
     */
    private Flug flug;

    /**
     * Der gebuchte Sitzplatz.
     */
    private Sitzplatz sitzplatz;

    /**
     * Die Gepaeckinformationen des Passagiers.
     */
    private final GepaeckInformation gepaeckInformation;

    /**
     * Der aktuelle Status der Buchung.
     */
    private Buchungsstatus buchungsstatus;

    /**
     * Der Preis, der aktuell zu zahlen ist.
     */
    private double gezahlterPreis;

    /**
     * Die Umbuchungsgebuehr, die aktuell zu zahlen ist.
     */
    private double gezahlteUmbuchungsgebuehr = 0.0;

    /**
     * Erzeugt eine neue Buchung mit einem Passagier, einem Flug, einem Sitzplatz und den zugehoerigen
     * Gepaeckinformationen. Der Preis der Buchung wird automatisch berechnet und der Status auf {@code AKTIV} gesetzt.
     * <p>
     * Die Buchungsnummer wird in der Klasse {@code Buchungssystem} generiert.
     *
     * @param passagier          der zugehoerige Passagier
     * @param flug               der gebuchte Flug
     * @param sitzplatz          der gebuchte Sitzplatz
     * @param gepaeckInformation die Gepaeckinformationen zur Buchung
     * @throws IllegalArgumentException wenn einer der Parameter {@code null} ist
     */
    public Buchung(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
                   GepaeckInformation gepaeckInformation
    ) {

        try {
            validiereBuchungsparameter(passagier, flug, sitzplatz, gepaeckInformation);
        } catch (Exception e) {
            throw e;
        }

        this.passagier = passagier;
        this.flug = flug;
        this.sitzplatz = sitzplatz;
        this.gepaeckInformation = gepaeckInformation;
        this.gezahlterPreis = berechneGezahltenPreis();
        this.buchungsstatus = Buchungsstatus.AKTIV;
    }

    /**
     * Prueft, ob die Parameter eine {@code null}-Referenz enthalten.
     *
     * @param passagier          der Passagier der Buchung
     * @param flug               der gebuchte Flug
     * @param sitzplatz          der gebuchte Sitzplatz
     * @param gepaeckInformation die Gepaeckinformation der Buchung
     * @throws IllegalArgumentException wenn einer der Parameter {@code null} ist
     */
    private void validiereBuchungsparameter(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
                                            GepaeckInformation gepaeckInformation
    ) {

        if (passagier == null || flug == null || sitzplatz == null || gepaeckInformation == null) {
            throw new IllegalArgumentException("Einer der Buchungsparameter hat eine null-Referenz.");
        }
    }

    /**
     * Berechnet den fuer die Buchung zu zahlenden Preis. Die Berechnung beruecksichtigt die Sitzklasse und die
     * anfallenden Gepaeckgebuehren.
     *
     * @return der berechnete Buchungspreis
     * @throws UnsupportedOperationException wenn die Sitzklasse nicht unterstuetzt wird
     */
    private double berechneGezahltenPreis() {

        if (this.sitzplatz.getSitzklasse() == Sitzklasse.ECONOMY) {

            return this.flug.getBasispreis() + this.gepaeckInformation.berechneGepaeckgebuehr();

        } else if (this.sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS) {

            return this.flug.getBasispreis() * businessPreisFaktor
                   + this.gepaeckInformation.berechneGepaeckgebuehr();

        } else {
            throw new UnsupportedOperationException("Klasse nicht implementiert.");
        }
    }

    /**
     * Storniert die Buchung und gibt die Storno-Gebuehr zurueck.
     *
     * @return die Stornierungsgebuehr
     */
    public double stornierenMitGebuehr() {

        this.buchungsstatus = Buchungsstatus.STORNIERT;

        return stornierungsGebuehr;
    }

    /**
     * Gibt die Buchungsnummer zurueck.
     *
     * @return die Buchungsnummer
     */
    public String getBuchungsnummer() {

        return this.buchungsnummer;
    }

    /**
     * Legt die Buchungsnummer anhand eines uebergebenen Strings fest. Validiert diesen vorher auf {@code null}-
     * Referenz, ob er leer ist und ob das Format passt.
     *
     * @param buchungsnummer die neue Buchungsnummer
     * @throws IllegalArgumentException wenn die Buchungsnummer {@code null} oder leer ist oder nicht dem erwarteten
     *                                  Format entspricht
     */
    public void setBuchungsnummer(String buchungsnummer) {

        if (buchungsnummer == null) {
            throw new IllegalArgumentException("Der Parameter fuer die Buchungsnummern enthaelt eine null-Referenz.");
        }

        if (buchungsnummer.isBlank()) {
            throw new IllegalArgumentException("Der Parameter fuer die Buchungsnummern ist leer.");
        }

        // prueft, ob der uebergebene String mit "bu" anfaengt und auf mindestens eine
        // Ziffer endet
        if (buchungsnummer.startsWith("bu") && buchungsnummer.matches(".*\\d+$")) {
            this.buchungsnummer = buchungsnummer;
        } else {
            throw new IllegalArgumentException("Der uebergebene String hat nicht das passende Format");
        }
    }

    /**
     * Gibt den zugehoerigen Passagier zurueck.
     *
     * @return der Passagier der Buchung
     */
    public Passagier getPassagier() {

        return this.passagier;
    }

    /**
     * Gibt den gebuchten Flug zurueck.
     *
     * @return der gebuchte Flug
     */
    public Flug getFlug() {

        return this.flug;
    }

    /**
     * aendert den Flug auf den uebergebenen Flug {@code f}.
     *
     * @param f der Flug, der dieser Buchung zugewiesen werden soll
     */
    public void setFlug(Flug f) {

        this.flug = f;
    }

    /**
     * Gibt den gebuchten Sitzplatz zurueck.
     *
     * @return der gebuchte Sitzplatz
     */
    public Sitzplatz getSitzplatz() {

        return this.sitzplatz;
    }

    /**
     * aendert den gebuchten Sitzplatz.
     *
     * @param sitzplatz der neue Sitzplatz der Buchung
     */
    public void setSitzplatz(Sitzplatz sitzplatz) {

        this.sitzplatz = sitzplatz;
    }

    /**
     * Gibt den aktuellen Buchungsstatus zurueck.
     *
     * @return der aktuelle Buchungsstatus
     */
    public Buchungsstatus getBuchungsstatus() {

        return this.buchungsstatus;
    }

    /**
     * aendert den Buchungsstatus auf den uebergebenen {@code Buchungsstatus}.
     *
     * @param buchungsstatus der Buchungsstatus, auf den die Buchung geaendert werden soll
     */
    public void setBuchungsstatus(Buchungsstatus buchungsstatus) {

        this.buchungsstatus = buchungsstatus;
    }

    /**
     * Gibt die Umbuchungsgebuehr zurueck.
     *
     * @return die Umbuchungsgebuehr
     */
    public double getUmbuchungsgebuehr() {

        return umbuchungsGebuehr;
    }

    /**
     * Gibt die Storno-Gebuehr zurueck.
     *
     * @return die Stornierungsgebuehr
     */
    public double getStornierungsgebuehr() {

        return stornierungsGebuehr;
    }

    /**
     * Gibt den Preisfaktor fuer Business-Fluege zurueck.
     *
     * @return den Preisfaktor fuer Business-Fluege
     */
    public double getBusinesspreisfaktor() {

        return businessPreisFaktor;
    }

    /**
     * Gibt die Gepaeckinformationen zurueck.
     *
     * @return die Gepaeckinformationen
     */
    public GepaeckInformation getGepaeckinformation() {

        return gepaeckInformation;
    }

    /**
     * Gibt den gezahlten Preis zurueck.
     *
     * @return der gezahlte Preis
     */
    public double getGezahlterPreis() {

        return gezahlterPreis;
    }

    /**
     * Aktualisiert den gezahlten Preis, indem dieser neu berechnet wird.
     */
    public void aktualisiereGezahltenPreis() {

        this.gezahlterPreis = berechneGezahltenPreis();
    }

    /**
     * Setzt die gezahlte Umbuchungsgebuehr auf den uebergebenen Wert.
     *
     * @param gebuehr die neue gezahlte Umbuchungsgebuehr
     */
    public void setGezahlteUmbuchungsgebuehr(double gebuehr) {

        this.gezahlteUmbuchungsgebuehr = gebuehr;
    }

    /**
     * Gibt eine textuelle Beschreibung der Buchung zurueck. Die Beschreibung enthaelt die Buchungsnummer, den Passagier,
     * den Flug, den Sitzplatz, den Buchungsstatus sowie die Gepaeckinformationen und den gezahlten Preis.
     *
     * @return die textuelle Beschreibung der Buchung
     */
    @Override
    public String toString() {

        return "\n \nDie Buchung mit der Nummer " + this.buchungsnummer +
               " von Passagier " + this.passagier.name() +
               " betreffend Flug " + this.flug.getFlugnummer() +
               " auf Sitzplatz " + this.sitzplatz.getSitzplatzNummer() +
               " in Sitzklasse " + this.sitzplatz.getSitzklasse() +
               " hat den Status " + this.buchungsstatus +
               " und es liegen folgende Gepaeckinformationen vor: " +
               this.gepaeckInformation.toString() +
               "\n---------------------------------------------\n" +
               "Damit betraegt die Buchungssumme: " +
               String.format("%.2f", this.gezahlterPreis) + " Euro" +
               "\nGezahlter Umbuchungsbetrag: "
               + String.format("%.2f", this.gezahlteUmbuchungsgebuehr) + " Euro";
    }
}
