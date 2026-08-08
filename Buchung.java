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

    private String buchungsnummer;
    private Passagier passagier;
    private Flug flug;
    private Sitzplatz sitzplatz;
    private GepaeckInformation gepaeckInformation;
    private Buchungsstatus buchungsstatus;
    private double gezahlterPreis;

    // Beispielwerte
    private final double umbuchungsGebühr = 100.00;
    private final double stornierungsGebühr = 200.00;
    private final double businessPreisFaktor = 1.65;

    /**
     * Erzeugt eine neue Buchung mit einer Buchungsnummer, einem Passagier,
     * einem Flug, einem Sitzplatz und den zugehörigen Gepäckinformationen.
     * Der Preis der Buchung wird automatisch berechnet und der Status
     * auf {@code AKTIV} gesetzt.
     *
     * Die Buchungsnummer wird in der Klasse Buchungssystem generiert
     * @param passagier der zugehörige Passagier
     * @param flug der gebuchte Flug
     * @param sitzplatz der gebuchte Sitzplatz
     * @param gepaeckInformation die Gepäckinformationen zur Buchung
     */
    public Buchung(Passagier passagier, Flug flug, Sitzplatz sitzplatz,
            GepaeckInformation gepaeckInformation) {
        this.passagier = passagier;
        this.flug = flug;
        this.sitzplatz = sitzplatz;
        this.gepaeckInformation = gepaeckInformation;
        this.gezahlterPreis = berechneGezahltenPreis();
        this.buchungsstatus = Buchungsstatus.AKTIV;
    }

    public Buchung(String buchungsnummer, Passagier passagier, Flug flug, Sitzplatz sitzplatz,
            GepaeckInformation gepaeckInformation) {
        
        this.buchungsnummer = buchungsnummer;
        this.passagier = passagier;
        this.flug = flug;
        this.sitzplatz = sitzplatz;
        this.gepaeckInformation = gepaeckInformation;
        this.gezahlterPreis = berechneGezahltenPreis();
        this.buchungsstatus = Buchungsstatus.AKTIV;
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

    public void setBuchungsnummer(String s) {
        if (s.startsWith("bu")) {
            this.buchungsnummer = s;
        }
    }

    public double getUmbuchungsgebuehr() {
        return umbuchungsGebühr;
    }

    public double getStornierungsgebuehr() {
        return stornierungsGebühr;
    }

    public double getBusinesspreisfaktor() {
        return businessPreisFaktor;
    }

    //der Setter beinhaltet keine Validierung, da die Methode "validiereUmbuchung()" dies für alle Buchungsparameter erledigt
    public void setFlug(Flug f) {
        this.flug = f;
    }

    public void setBuchungsstatus(Buchungsstatus status) {
        this.buchungsstatus = status;
    }

    public GepaeckInformation getGepaeckinformation() {
        return gepaeckInformation;
    }

    public double getGezahlterPreis() {
        return gezahlterPreis;
    }

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