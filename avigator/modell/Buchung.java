package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Die Klasse {@code avigator.modell.Buchung} repräsentiert eine Flugbuchung eines Passagiers. Eine avigator.modell.Buchung enthält Informationen über
 * den gebuchten avigator.modell.Flug, den avigator.modell.Sitzplatz, die Gepäckinformationen, den avigator.modell.Buchungsstatus sowie den gezahlten Preis.
 *
 * @author Kevin Braun
 * @version 1.0
 */
@SuppressWarnings("FieldCanBeLocal")
public class Buchung implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Festgelegte pauschale Umbuchungsgebühr.
     */
    private final double umbuchungsGebuehr = 100.00;

    /**
     * Festgelegte pauschale Storno-Gebühr.
     */
    private final double stornierungsGebuehr = 200.00;

    /**
     * Festgelegter pauschaler Preisfaktor, der auf Business-Flüge anfällt.
     */
    private final double businessPreisFaktor = 1.65;

    /**
     * Die Nummer der avigator.modell.Buchung, die anhand der Zahl der schon vorhandenen Buchungen generiert wird.
     */
    private String buchungsnummer;

    /**
     * Der avigator.modell.Passagier dieser avigator.modell.Buchung. Aus Komplexitätsgründen gehen wir davon aus, dass jeder avigator.modell.Passagier seinen avigator.modell.Flug
     * selber buchen muss.
     */
    private final Passagier passagier;

    /**
     * Der gebuchte avigator.modell.Flug.
     */
    private Flug flug;

    /**
     * Der gebuchte avigator.modell.Sitzplatz.
     */
    private Sitzplatz sitzplatz;

    /**
     * Die Gepäckinformationen des Passagiers.
     */
    private final GepaeckInformation gepaeckInformation;

    /**
     * Der aktuelle Status der avigator.modell.Buchung.
     */
    private Buchungsstatus buchungsstatus;

    /**
     * Der Preis, der aktuell zu zahlen ist.
     */
    private double gezahlterPreis;

    /**
     * Die Umbuchungsgebühr, die aktuell zu zahlen ist.
     */
    private double gezahlteUmbuchungsgebuehr = 0.0;

    /**
     * Erzeugt eine neue avigator.modell.Buchung mit einem avigator.modell.Passagier, einem avigator.modell.Flug, einem avigator.modell.Sitzplatz und den zugehörigen
     * Gepäckinformationen. Der Preis der avigator.modell.Buchung wird automatisch berechnet und der Status auf {@code AKTIV} gesetzt.
     * <p>
     * Die Buchungsnummer wird in der Klasse avigator.verwaltung.Buchungssystem generiert
     *
     * @param passagier          der zugehörige avigator.modell.Passagier
     * @param flug               der gebuchte avigator.modell.Flug
     * @param sitzplatz          der gebuchte avigator.modell.Sitzplatz
     * @param gepaeckInformation die Gepäckinformationen zur avigator.modell.Buchung
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
     * Prüft, ob die Parameter eine {@code null}- Referenz enthalten.
     *
     * @param passagier          : der avigator.modell.Passagier der avigator.modell.Buchung
     * @param flug               : der gebuchte avigator.modell.Flug
     * @param sitzplatz          : der gebuchte avigator.modell.Sitzplatz
     * @param gepaeckInformation : die Gepäckinformation der avigator.modell.Buchung
     */
    private void validiereBuchungsparameter(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
                                            GepaeckInformation gepaeckInformation
    ) {

        if (passagier == null || flug == null || sitzplatz == null || gepaeckInformation == null) {
            throw new IllegalArgumentException("Einer der Buchungsparameter hat eine null-Referenz.");
        }
    }

    /**
     * Berechnet den für die avigator.modell.Buchung zu zahlenden Preis. Die Berechnung berücksichtigt die avigator.modell.Sitzklasse und die
     * anfallenden Gepäckgebühren.
     *
     * @return der berechnete Buchungspreis
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
     * Storniert die avigator.modell.Buchung und gibt die Storno-Gebühr zurück.
     *
     * @return die Stornierungsgebühr
     */
    public double stornierenMitGebuehr() {

        this.buchungsstatus = Buchungsstatus.STORNIERT;

        return stornierungsGebuehr;
    }

    /**
     * Gibt die Buchungsnummer zurück.
     *
     * @return die Buchungsnummer
     */
    public String getBuchungsnummer() {

        return this.buchungsnummer;
    }

    /**
     * Legt die Buchungsnummer anhand eines übergebenen Strings fest. Validiert diesen vorher auf {@code null}-
     * Referenz, ob er leer ist und ob das Format passt.
     *
     * @param buchungsnummer : die neue Buchungsnummer
     */
    public void setBuchungsnummer(String buchungsnummer) {

        if (buchungsnummer == null) {
            throw new IllegalArgumentException("Der Parameter für die Buchungsnummern enthält eine null-Referenz.");
        }

        if (buchungsnummer.isBlank()) {
            throw new IllegalArgumentException("Der Parameter für die Buchungsnummern ist leer.");
        }

        // prüft, ob der übergebene String mit "bu" anfängt und auf mindestens eine
        // Ziffer endet
        if (buchungsnummer.startsWith("bu") && buchungsnummer.matches(".*\\d+$")) {
            this.buchungsnummer = buchungsnummer;
        } else {
            throw new IllegalArgumentException("Der übergebene String hat nicht das passende Format");
        }
    }

    /**
     * Gibt den zugehörigen avigator.modell.Passagier zurück.
     *
     * @return der avigator.modell.Passagier der avigator.modell.Buchung
     */
    public Passagier getPassagier() {

        return this.passagier;
    }

    /**
     * Gibt den gebuchten avigator.modell.Flug zurück.
     *
     * @return der gebuchte avigator.modell.Flug
     */
    public Flug getFlug() {

        return this.flug;
    }

    /**
     * Ändert den avigator.modell.Flug auf den übergebenen avigator.modell.Flug {@code f}
     *
     * @param f : avigator.modell.Flug, der dieser avigator.modell.Buchung zugewiesen werden soll
     */
    public void setFlug(Flug f) {

        this.flug = f;
    }

    /**
     * Gibt den gebuchten avigator.modell.Sitzplatz zurück.
     *
     * @return der gebuchte avigator.modell.Sitzplatz
     */
    public Sitzplatz getSitzplatz() {

        return this.sitzplatz;
    }

    public void setSitzplatz(Sitzplatz sitzplatz) {

        this.sitzplatz = sitzplatz;
    }

    /**
     * Gibt den aktuellen avigator.modell.Buchungsstatus zurück.
     *
     * @return der aktuelle avigator.modell.Buchungsstatus
     */
    public Buchungsstatus getBuchungsstatus() {

        return this.buchungsstatus;
    }

    /**
     * Ändert den avigator.modell.Buchungsstatus auf den übergebenen {@code avigator.modell.Buchungsstatus}
     *
     * @param buchungsstatus : der avigator.modell.Buchungsstatus, auf den die avigator.modell.Buchung geändert werden soll
     */
    public void setBuchungsstatus(Buchungsstatus buchungsstatus) {

        this.buchungsstatus = buchungsstatus;
    }

    /**
     * Gibt die Umbuchungsgebühr zurück.
     *
     * @return die Umbuchungsgebühr
     */
    public double getUmbuchungsgebuehr() {

        return umbuchungsGebuehr;
    }

    /**
     * Gibt die Storno-Gebühr zurück.
     *
     * @return die StornierungsGebühr
     */
    public double getStornierungsgebuehr() {

        return stornierungsGebuehr;
    }

    /**
     * Gibt den Preisfaktor für Business-Flüge zurück.
     *
     * @return den Preisfaktor für Business-Flüge
     */
    public double getBusinesspreisfaktor() {

        return businessPreisFaktor;
    }

    /**
     * Gibt die Gepäckinfos zurück
     *
     * @return die Gepäckinfos
     */
    public GepaeckInformation getGepaeckinformation() {

        return gepaeckInformation;
    }

    /**
     * Gibt den gezahlten Preis zurück.
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
     * Setzt die gezahlte Umbuchungsgebühr auf den übergebenen Parameter
     *
     * @param gebuehr : die neue gezahlte Umbuchungsgebühr
     */
    public void setGezahlteUmbuchungsgebuehr(double gebuehr) {

        this.gezahlteUmbuchungsgebuehr = gebuehr;
    }

    /**
     * Gibt eine textuelle Beschreibung der avigator.modell.Buchung zurück. Die Beschreibung enthält die Buchungsnummer, den avigator.modell.Passagier,
     * den avigator.modell.Flug, den avigator.modell.Sitzplatz, den avigator.modell.Buchungsstatus sowie die Gepäckinformationen und den gezahlten Preis.
     *
     * @return die textuelle Beschreibung der avigator.modell.Buchung
     */
    @Override
    public String toString() {

        return "\n \nDie avigator.modell.Buchung mit der Nummer " + this.buchungsnummer +
               " von avigator.modell.Passagier " + this.passagier.name() +
               " betreffend avigator.modell.Flug " + this.flug.getFlugnummer() +
               " auf avigator.modell.Sitzplatz " + this.sitzplatz.getSitzplatzNummer() +
               " in avigator.modell.Sitzklasse " + this.sitzplatz.getSitzklasse() +
               " hat den Status " + this.buchungsstatus +
               " und es liegen folgende Gepäckinformationen vor: " +
               this.gepaeckInformation.toString() +
               "\n---------------------------------------------\n" +
               "Damit beträgt die Buchungssumme: " +
               String.format("%.2f", this.gezahlterPreis) + " Euro" +
               "\nGezahlter Umbuchungsbetrag: "
               + String.format("%.2f", this.gezahlteUmbuchungsgebuehr) + " Euro";
    }
}