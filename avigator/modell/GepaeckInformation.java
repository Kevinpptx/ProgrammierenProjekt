package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Repraesentiert die Gepaeckinformationen einer Buchung. Die Gebuehr wird anhand der gebuchten Kofferanzahl berechnet.
 */
public class GepaeckInformation implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Die pauschale Gebuehr pro Koffer.
     */
    double gebuehrProKoffer = 40.0;

    /**
     * Die Anzahl der Koffer, die ein Passagier mit sich fuehrt.
     */
    private int anzahlKoffer;

    /**
     * Erstellt Gepaeckinformationen mit der angegebenen Kofferanzahl.
     *
     * @param anzahlKoffer die Anzahl der Koffer
     * @throws IllegalArgumentException wenn die Kofferanzahl negativ ist
     */
    public GepaeckInformation(int anzahlKoffer) {

        validiereGepaeckinfo(anzahlKoffer);

        this.anzahlKoffer = anzahlKoffer;
    }

    /**
     * Prueft, ob die angegebene Kofferanzahl gueltig ist.
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
     * Gibt die Anzahl der Koffer zurueck.
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
     * Berechnet die Gepaeckgebuehr basierend auf der Anzahl der Koffer.
     *
     * @return die berechnete Gepaeckgebuehr
     */
    public double berechneGepaeckgebuehr() {

        return anzahlKoffer * gebuehrProKoffer;
    }

    /**
     * Gibt eine String-Darstellung der GepaeckInformation zurueck.
     *
     * @return eine String-Darstellung der GepaeckInformation
     */
    @Override
    public String toString() {

        return "Die gebuchte Kofferanzahl betraegt " + this.anzahlKoffer +
               " und es wurde eine Gebuehr von " + String.format("%.2f", this.berechneGepaeckgebuehr()) +
               " Euro entrichtet.";
    }

}
