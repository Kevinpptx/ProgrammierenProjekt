import java.io.Serializable;

/**
 * Die Klasse {@code Buchung} repräsentiert eine Flugbuchung eines Passagiers.
 * Eine Buchung enthält Informationen über den gebuchten Flug, den Sitzplatz,
 * die Gepäckinformationen, den Buchungsstatus sowie den gezahlten Preis.
 *
 * @author Kevin Braun
 * @version 1.0
 */
public class Buchung implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die Nummer der Buchung, die anhand der Zahl der schon vorhandenen Buchungen
     * generiert wird.
     */
    private String buchungsnummer;

    /**
     * Der Passagier dieser Buchung. Aus Komplexitätsgründen gehen wir davon aus,
     * dass jeder Passagier seinen Flug selber buchen muss.
     */
    private Passagier passagier;

    /** Der gebuchte Flug */
    private Flug flug;

    /** Der gebuchte Sitzplatz */
    private Sitzplatz sitzplatz;

    /** Die Anzahl der Koffer und die Gebühr pro Koffer */
    private GepaeckInformation gepaeckInformation;

    /** Der aktuelle Status der Buchung */
    private Buchungsstatus buchungsstatus;
    private double gezahlterPreis;

    /** Festgelegte pauschale Umbuchungsgebühr */
    private final double umbuchungsGebühr = 100.00;

    /** Festgelegte pauschale Storno-Gebühr */
    private final double stornierungsGebühr = 200.00;

    /** Festgelegter pauschaler Preisfaktor, der auf Business-Flüge anfällt. */
    private final double businessPreisFaktor = 1.65;

    /**
     * Erzeugt eine neue Buchung mit einem Passagier,
     * einem Flug, einem Sitzplatz und den zugehörigen Gepäckinformationen.
     * Der Preis der Buchung wird automatisch berechnet und der Status
     * auf {@code AKTIV} gesetzt.
     *
     * Die Buchungsnummer wird in der Klasse Buchungssystem generiert
     * 
     * @param passagier          der zugehörige Passagier
     * @param flug               der gebuchte Flug
     * @param sitzplatz          der gebuchte Sitzplatz
     * @param gepaeckInformation die Gepäckinformationen zur Buchung
     */
    public Buchung(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
            GepaeckInformation gepaeckInformation) {
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
     * Erzeugt eine neue Buchung mit einer Buchungsnummer, einem Passagier,
     * einem Flug, einem Sitzplatz und den zugehörigen Gepäckinformationen.
     * Der Preis der Buchung wird automatisch berechnet und der Status
     * auf {@code AKTIV} gesetzt.
     *
     * Die Buchungsnummer wird in der Klasse Buchungssystem generiert
     * 
     * @param passagier          der zugehörige Passagier
     * @param flug               der gebuchte Flug
     * @param sitzplatz          der gebuchte Sitzplatz
     * @param gepaeckInformation die Gepäckinformationen zur Buchung
     */
    public Buchung(String buchungsnummer, Passagier passagier, Flug flug, Sitzplatz sitzplatz,
            GepaeckInformation gepaeckInformation) {

        try {
            validiereBuchungsparameter(passagier, flug, sitzplatz, gepaeckInformation);
        } catch (Exception e) {
            throw e;
        }

        if (buchungsnummer == null) {
            throw new IllegalArgumentException("Die Buchungsnummer hat eine null-Referenz.");
        }

        if (buchungsnummer.isBlank()) {
            throw new IllegalArgumentException("Die Buchungsnummer ist leer.");
        }
        this.buchungsnummer = buchungsnummer;
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
     * @param passagier
     * @param flug
     * @param sitzplatz
     * @param gepaeckInformation
     */
    private void validiereBuchungsparameter(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
            GepaeckInformation gepaeckInformation) {
        if (passagier == null || flug == null || sitzplatz == null || gepaeckInformation == null) {
            throw new IllegalArgumentException("Einer der Buchungsparameter hat eine null-Referenz.");
        }
    }

    /**
     * Berechnet den für die Buchung zu zahlenden Preis.
     * Die Berechnung berücksichtigt die Sitzklasse und die
     * anfallenden Gepäckgebühren.
     *
     * @return der berechnete Buchungspreis
     */
    private double berechneGezahltenPreis() {
        if (this.sitzplatz.getSitzklasse() == Sitzklasse.ECONOMY)
            return this.flug.getBasispreis() + this.gepaeckInformation.berechneGepaeckgebuehr();
        else if (this.sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS)
            return this.flug.getBasispreis() * businessPreisFaktor
                    + this.gepaeckInformation.berechneGepaeckgebuehr();
        else
            throw new UnsupportedOperationException("Klasse nicht implementiert.");
    }

    public double stornierenMitGebühr() {
        this.buchungsstatus = Buchungsstatus.STORNIERT;
        return stornierungsGebühr;
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
     * Gibt den zugehörigen Passagier zurück.
     *
     * @return der Passagier der Buchung
     */
    public Passagier getPassagier() {
        return this.passagier;
    }

    /**
     * Gibt den gebuchten Flug zurück.
     *
     * @return der gebuchte Flug
     */
    public Flug getFlug() {
        return this.flug;
    }

    /**
     * Gibt den gebuchten Sitzplatz zurück.
     *
     * @return der gebuchte Sitzplatz
     */
    public Sitzplatz getSitzplatz() {
        return this.sitzplatz;
    }

    public void setSitzplatz(Sitzplatz s) {
        this.sitzplatz = s;
    }

    /**
     * Gibt den aktuellen Buchungsstatus zurück.
     *
     * @return der aktuelle Buchungsstatus
     */
    public Buchungsstatus getBuchungsstatus() {
        return this.buchungsstatus;
    }

    /**
     * Legt die Buchungsnummer anhand eines übergebenen Strings fest. Validiert
     * diesen vorher auf {@code null}- Referenz, ob er leer ist und ob das Format
     * passt.
     * 
     * @param s : der zu überprüfende String
     */
    public void setBuchungsnummer(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Der Parameter für die Buchungsnummern enthält eine null-Referenz.");
        }
        if (s.isBlank()) {
            throw new IllegalArgumentException("Der Parameter für die Buchungsnummern ist leer.");
        }
        // prüft, ob der übergebene String mit "bu" anfängt und auf mindestens eine
        // Ziffer endet
        if (s.startsWith("bu") && s.matches(".*\\d+$")) {
            this.buchungsnummer = s;
        } else {
            throw new IllegalArgumentException("Der übergebene String hat nicht das passende Format");
        }
    }

    /**
     * Gibt die Unbuchungsgebühr zurück.
     * @return umbuchungsGebühr
     */
    public double getUmbuchungsgebuehr() {
        return umbuchungsGebühr;
    }

    /**
     * Gibt die Storno-Gebühr zurück.
     * @return stornierungsGebühr
     */
    public double getStornierungsgebuehr() {
        return stornierungsGebühr;
    }

    /**
     * Gibt den Preisfaktor für Business-Flüge zurück.
     * @return businessPreisFaktor
     */
    public double getBusinesspreisfaktor() {
        return businessPreisFaktor;
    }

    /**
     * Ändert den Flug auf das übergebene Objekt {@code f}
     * @param f : Flug, der dieser Buchung zugewiesen werden soll
     */
    public void setFlug(Flug f) {
        this.flug = f;
    }

    /**
     * Ändert den Buchungsstatus auf das übergebene {@code enum}
     * @param status : der Buchungsstatus, auf den die Buchung geändert werden soll
     */
    public void setBuchungsstatus(Buchungsstatus status) {
        this.buchungsstatus = status;
    }

    /**
     * Gibt die Gepäckinfos zurück
     * @return gepaeckInformation
     */
    public GepaeckInformation getGepaeckinformation() {
        return gepaeckInformation;
    }

    /**
     * Gibt den gezahlten Preis der Buchung zurück
     * @return gezahlterPreis
     */
    public double getGezahlterPreis() {
        return gezahlterPreis;
    }

    /**
     * Ändert den gezahlten Preis auf den übergebenen {@code double}.
     * @param preis
     */
    public void setGezahlterPreis(double preis) {
        this.gezahlterPreis = preis;
    }

    /**
     * Gibt eine textuelle Beschreibung der Buchung zurück.
     * Die Beschreibung enthält die Buchungsnummer, den Passagier,
     * den Flug, den Sitzplatz, den Buchungsstatus sowie die
     * Gepäckinformationen und den gezahlten Preis.
     *
     * @return die textuelle Beschreibung der Buchung
     */
    @Override
    public String toString() {
        return "\n \nDie Buchung mit der Nummer " + this.buchungsnummer +
                " von Passagier " + this.passagier.getName() +
                " betreffend Flug " + this.flug.getFlugnummer() +
                " auf Sitzplatz " + this.sitzplatz.getSitzplatzNummer() +
                " in Sitzklasse " + this.sitzplatz.getSitzklasse() +
                " hat den Status " + this.buchungsstatus +
                " und es liegen folgende Gepäckinformationen vor: " +
                this.gepaeckInformation.toString() +
                "\n---------------------------------------------\n" +
                "Damit beträgt die Buchungssumme: " +
                String.format("%.2f", this.gezahlterPreis) + "€";
    }
}