package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Repräsentiert die Gepäckinformationen einer Buchung. Die Gebühr wird anhand der gebuchten Kofferanzahl berechnet.
 */
public class GepaeckInformation implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Die pauschale Gebühr pro Koffer.
     */
    double gebuehrProKoffer = 40.0;

    /**
     * Die Anzahl der Koffer, die ein Passagier mit sich führt.
     */
    private int anzahlKoffer;

    /**
     * Erstellt Gepäckinformationen mit der angegebenen Kofferanzahl.
     *
     * @param anzahlKoffer die Anzahl der Koffer
     * @throws IllegalArgumentException wenn die Kofferanzahl negativ ist
     */
    public GepaeckInformation(int anzahlKoffer) {

        validiereGepaeckinfo(anzahlKoffer);

        this.anzahlKoffer = anzahlKoffer;
    }

    /**
     * Prüft, ob die angegebene Kofferanzahl gültig ist.
     *
     * @param anzahlKoffer die zu validierende Kofferanzahl
     * @throws IllegalArgumentException wenn die Kofferanzahl negativ ist
     */
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
     *
     * @param anzahlKoffer die neue Anzahl der Koffer
     * @throws IllegalArgumentException wenn die Kofferanzahl negativ ist
     */
    public void setAnzahlKoffer(int anzahlKoffer) {

        validiereGepaeckinfo(anzahlKoffer);

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
