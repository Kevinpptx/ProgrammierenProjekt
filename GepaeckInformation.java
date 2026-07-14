/**
 * Repräsentiert Informationen über Gepäck in einem Flugzeug.
 * Gepäck besteht aus einer Anzahl von Koffern, die ein Passagier mit sich führt.
 */
public class GepaeckInformation {

    /** Die Anzahl der Koffer, die ein Passagier mit sich führt. */
    private int anzahlKoffer;

    /**
     * Konstruktor für die Klasse GepäckInformation.
     *
     * @param anzahlKoffer die Anzahl der Koffer
     */
    public GepaeckInformation(int anzahlKoffer) {
        this.anzahlKoffer = anzahlKoffer;
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
     *
     * @param anzahlKoffer die neue Anzahl der Koffer
     */
    public void setAnzahlKoffer(int anzahlKoffer) {
        this.anzahlKoffer = anzahlKoffer;

    }

    /**
     * Berechnet die Gepäckgebühr basierend auf der Anzahl der Koffer.
     *
     * @return die berechnete Gepäckgebühr
     */
    public double berechneGepaeckgebuehr() {
        double gebuehrProKoffer = 40.00; // Beispielgebühr pro Koffer
            return anzahlKoffer * gebuehrProKoffer;

    }   

    /**
     * Gibt eine String-Darstellung der GepäckInformation zurück.
     *
     * @return eine String-Darstellung der GepäckInformation
     */
    @Override
    public String toString() {
        return "GepäckInformation: " +
                "anzahlKoffer= " + anzahlKoffer
                ;
    }

}
