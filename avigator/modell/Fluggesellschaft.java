package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Die Klasse {@code avigator.modell.Fluggesellschaft} repräsentiert eine avigator.modell.Fluggesellschaft mit einem Namen, einem Airline-Code und einer
 * Flotte von Flugzeugen.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Fluggesellschaft implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Name der Airline
     */
    private final String name;

    /**
     * Airlinecode, der aus zwei Großbuchstaben besteht.
     */
    private final String airlineCode;

    /**
     * Flotte der Airline, hier eine {@code ArrayList} aus {@code avigator.modell.Flugzeug} - Objekten.
     */
    private final ArrayList<Flugzeug> flotte = new ArrayList<>();

    /**
     * Erzeugt eine neue avigator.modell.Fluggesellschaft mit einem Namen und einem Airline-Code.
     *
     * @param name        der Name der avigator.modell.Fluggesellschaft
     * @param airlineCode der eindeutige Airline-Code der avigator.modell.Fluggesellschaft
     * @throws IllegalArgumentException , wenn die Parameter eine {@code null}- Referenz haben, leer sind oder der
     *                                  Airline-Code nicht das passende Format hat.
     */
    public Fluggesellschaft(String name, String airlineCode) {

        if (name == null || airlineCode == null) {
            throw new IllegalArgumentException("Die angegebenen Parameter dürfen keine null-Referenz enthalten.");
        }

        if (name.isBlank() || airlineCode.isBlank()) {
            throw new IllegalArgumentException("Die angegebenen Parameter dürfennicht leer sein.");
        }

        if (airlineCode.length() != 2 || !Character.isLetter(airlineCode.charAt(0))
            || !Character.isLetter(airlineCode.charAt(1))) {
            throw new IllegalArgumentException("Airline-Code muss aus zwei Buchstaben bestehen!");
        }

        this.name = name;

        // entfernt Leerzeichen im Code, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.airlineCode = airlineCode.trim().toUpperCase(Locale.ROOT);
    }

    /**
     * Fügt ein avigator.modell.Flugzeug zur Flotte der avigator.modell.Fluggesellschaft hinzu. Befindet sich das avigator.modell.Flugzeug bereits in der Flotte,
     * erfolgt keine Änderung.
     *
     * @param flugzeug das hinzuzufügende avigator.modell.Flugzeug
     * @throws IllegalArgumentException wenn das übergebene avigator.modell.Flugzeug eine null-Referenz enthält.
     */
    public void fuegeFlugzeugHinzu(Flugzeug flugzeug) {

        if (flugzeug == null) {
            throw new IllegalArgumentException("Das hinzuzufügende avigator.modell.Flugzeug enthält eine null-Referenz.");
        }

        if (! this.flotte.contains(flugzeug)) {
            this.flotte.add(flugzeug);
        } else {
            throw new IllegalArgumentException("Das avigator.modell.Flugzeug existiert schon in der Flotte.");
        }
    }

    /**
     * Entfernt ein avigator.modell.Flugzeug aus der Flotte der avigator.modell.Fluggesellschaft. Befindet sich das avigator.modell.Flugzeug nicht in der Flotte,
     * erfolgt keine Änderung.
     *
     * @param flugzeug das zu entfernende avigator.modell.Flugzeug
     */
    public void entferneFlugzeug(Flugzeug flugzeug) {

        if (!this.flotte.isEmpty()) {
            this.flotte.remove(flugzeug);
        }
    }

    /**
     * Gibt die Flotte der avigator.modell.Fluggesellschaft zurück.
     *
     * @return eine Liste aller Flugzeuge der avigator.modell.Fluggesellschaft
     */
    public List<Flugzeug> getFlotte() {

        return List.copyOf(flotte);
    }

    /**
     * Gibt den Airline-Code der avigator.modell.Fluggesellschaft zurück.
     *
     * @return der Airline-Code der avigator.modell.Fluggesellschaft
     */
    public String getAirlineCode() {

        return this.airlineCode;
    }

    /**
     * Gibt den Namen der avigator.modell.Fluggesellschaft zurück.
     *
     * @return der Name der avigator.modell.Fluggesellschaft
     */
    public String getName() {
        return this.name;
    }

    /**
     * Prüft, ob die Flotte der Airline ein gewisses avigator.modell.Flugzeug beinhaltet.
     *
     * @param flugzeug : Das zu überprüfende avigator.modell.Flugzeug
     * @return {@code true}, wenn das avigator.modell.Flugzeug in der Flotte der Airline ist
     */
    public boolean beinhaltetFlugzeug(Flugzeug flugzeug) {

        return flotte.contains(flugzeug);
    }

    /**
     * Gibt eine textuelle Beschreibung der avigator.modell.Fluggesellschaft zurück. Die Beschreibung enthält den Namen, den
     * Airline-Code und die Flugzeuge der avigator.modell.Fluggesellschaft.
     *
     * @return die textuelle Beschreibung der avigator.modell.Fluggesellschaft
     */
    @Override
    public String toString() {

        return "Die Airline " + this.name + " betreibt unter Airline Code: " + this.airlineCode + " die Flugzeuge: "
                + this.flotte;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Fluggesellschaft f = (Fluggesellschaft) o;
        return this.airlineCode.equalsIgnoreCase(f.getAirlineCode());
    }
}
