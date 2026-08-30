package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Repräsentiert Informationen über Gepäck in einem avigator.modell.Flugzeug. Gepäck besteht aus einer Anzahl von Koffern, die ein
 * avigator.modell.Passagier mit sich führt.
 */
public class GepaeckInformation implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Die Der Preis eines Koffers. Wert 40 ist ein fiktiver Standardwert.
     */
    double gebuehrProKoffer = 40.0;

    /**
     * Die Anzahl der Koffer, die ein avigator.modell.Passagier mit sich führt.
     */
    private int anzahlKoffer;

    /**
     * Konstruktor für die Klasse GepäckInformation.
     *
     * @param anzahlKoffer die Anzahl der Koffer
     */
    public GepaeckInformation(int anzahlKoffer) {

        validiereGepaeckinfo(anzahlKoffer);

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
     *
     * @param anzahlKoffer die neue Anzahl der Koffer
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
