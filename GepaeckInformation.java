import java.io.Serializable;

/**
 * Repräsentiert Informationen über Gepäck in einem Flugzeug.
 * Gepäck besteht aus einer Anzahl von Koffern, die ein Passagier mit sich
 * führt.
 */
public class GepaeckInformation implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /** Die Anzahl der Koffer, die ein Passagier mit sich führt. */
    private int anzahlKoffer;

    /** Die Der Preis eines Koffers. Wert 40 ist ein fiktiver Standardwert. */
    double gebuehrProKoffer = 40.0;

    /**
     * Konstruktor für die Klasse GepäckInformation.
     *
     * @param anzahlKoffer die Anzahl der Koffer
     */
    public GepaeckInformation(int anzahlKoffer) {
        try {
            validiereGepaeckinfo(anzahlKoffer);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        this.anzahlKoffer = anzahlKoffer;
    }

    private void validiereGepaeckinfo(int anzahlKoffer) {
        if (anzahlKoffer < 0) {
            throw new IllegalArgumentException(
                    "Die angegebene Kofferanzahl darf nicht kleiner als 0 sein. Bitte korrigieren Sie die Eingabe.");
        }
    }

    /**
     * Gibt die Anzahl der Koffer zurück.
     *
     * @return die Anzahl der Koffer
     */
    public int getAnzahlKoffer() {
        return anzahlKoffer;
    }

    /**
     * Setzt die Anzahl der Koffer.
     * <p>
     * WICHTIG: Diese Methode wird MOMENTAN nicht genutzt, weil man in einer
     * Umbuchung nicht die Anzahl der Koffer ändern kann.
     * 
     * @param anzahlKoffer die neue Anzahl der Koffer
     */
    public void setAnzahlKoffer(int anzahlKoffer) {
        try {
            validiereGepaeckinfo(anzahlKoffer);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        this.anzahlKoffer = anzahlKoffer;
    }

    /**
     * Berechnet die Gepäckgebühr basierend auf der Anzahl der Koffer.
     *
     * @return die berechnete Gepäckgebühr
     */
    public double berechneGepaeckgebuehr() {
        return anzahlKoffer * gebuehrProKoffer;
    }

    /**
     * Gibt eine String-Darstellung der GepäckInformation zurück.
     *
     * @return eine String-Darstellung der GepäckInformation
     */
    @Override
    public String toString() {
        return "Die gebuchte Kofferanzahl beträgt " + this.anzahlKoffer +
                " und es wurde eine Gebühr von " + String.format("%.2f", this.berechneGepaeckgebuehr()) +
                " Euro entrichtet.";
    }

}
