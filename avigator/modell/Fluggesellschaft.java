package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Die Klasse {@code Fluggesellschaft} repraesentiert eine Fluggesellschaft mit einem Namen, einem Airline-Code und einer
 * Flotte von Flugzeugen.
 *
 * @author Cedric Beckmann
 * @version 1.1
 */
public class Fluggesellschaft implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Name der Airline.
     */
    private final String name;

    /**
     * Airlinecode, dessen Eingabe aus zwei Buchstaben bestehen muss.
     */
    private final String airlineCode;

    /**
     * Flotte der Airline, hier eine {@code ArrayList} aus {@code Flugzeug}-Objekten.
     */
    private final ArrayList<Flugzeug> flotte = new ArrayList<>();

    /**
     * Erzeugt eine neue Fluggesellschaft mit einem Namen und einem Airline-Code.
     *
     * @param name        der Name der Fluggesellschaft
     * @param airlineCode der eindeutige Airline-Code der Fluggesellschaft
     * @throws IllegalArgumentException wenn die Parameter eine {@code null}-Referenz haben, leer sind oder der
     *                                  Airline-Code nicht das passende Format hat
     */
    public Fluggesellschaft(String name, String airlineCode) {

        if (name == null || airlineCode == null) {
            throw new IllegalArgumentException("Die angegebenen Parameter duerfen keine null-Referenz enthalten.");
        }

        if (name.isBlank() || airlineCode.isBlank()) {
            throw new IllegalArgumentException("Die angegebenen Parameter duerfennicht leer sein.");
        }

        if (airlineCode.length() != 2 || !Character.isLetter(airlineCode.charAt(0))
            || !Character.isLetter(airlineCode.charAt(1))) {
            throw new IllegalArgumentException("Airline-Code muss aus zwei Buchstaben bestehen!");
        }

        this.name = name;

        // entfernt fuehrende und nachfolgende Leerzeichen im Code, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhaengig von der Spracheinstellung des Computers
        this.airlineCode = airlineCode.trim().toUpperCase(Locale.ROOT);
    }

    /**
     * Fuegt ein Flugzeug zur Flotte der Fluggesellschaft hinzu.
     *
     * @param flugzeug das hinzuzufuegende Flugzeug
     * @throws IllegalArgumentException wenn das uebergebene Flugzeug {@code null} ist oder bereits zur Flotte gehoert
     */
    public void fuegeFlugzeugHinzu(Flugzeug flugzeug) {

        if (flugzeug == null) {
            throw new IllegalArgumentException("Das hinzuzufuegende Flugzeug enthaelt eine null-Referenz.");
        }

        if (! this.flotte.contains(flugzeug)) {
            this.flotte.add(flugzeug);
        } else {
            throw new IllegalArgumentException("Das Flugzeug existiert schon in der Flotte.");
        }
    }

    /**
     * Entfernt ein Flugzeug aus der Flotte der Fluggesellschaft. Befindet sich das Flugzeug nicht in der Flotte,
     * erfolgt keine aenderung.
     *
     * @param flugzeug das zu entfernende Flugzeug
     */
    public void entferneFlugzeug(Flugzeug flugzeug) {

        if (!this.flotte.isEmpty()) {
            this.flotte.remove(flugzeug);
        }
    }

    /**
     * Gibt die Flotte der Fluggesellschaft zurueck.
     *
     * @return eine Liste aller Flugzeuge der Fluggesellschaft
     */
    public List<Flugzeug> getFlotte() {

        return List.copyOf(flotte);
    }

    /**
     * Gibt den Airline-Code der Fluggesellschaft zurueck.
     *
     * @return der Airline-Code der Fluggesellschaft
     */
    public String getAirlineCode() {

        return this.airlineCode;
    }

    /**
     * Gibt den Namen der Fluggesellschaft zurueck.
     *
     * @return der Name der Fluggesellschaft
     */
    public String getName() {
        return this.name;
    }

    /**
     * Prueft, ob die Flotte der Airline ein gewisses Flugzeug beinhaltet.
     *
     * @param flugzeug das zu ueberpruefende Flugzeug
     * @return {@code true}, wenn das Flugzeug in der Flotte der Airline ist
     */
    public boolean beinhaltetFlugzeug(Flugzeug flugzeug) {

        return flotte.contains(flugzeug);
    }

    /**
     * Gibt eine textuelle Beschreibung der Fluggesellschaft zurueck. Die Beschreibung enthaelt den Namen, den
     * Airline-Code und die Flugzeuge der Fluggesellschaft.
     *
     * @return die textuelle Beschreibung der Fluggesellschaft
     */
    @Override
    public String toString() {

        return "Die Airline " + this.name + " betreibt unter Airline Code: " + this.airlineCode + " die Flugzeuge: "
                + this.flotte;
    }

    /**
     * Vergleicht zwei Fluggesellschaften anhand ihres Airline-Codes.
     *
     * @param o das zu vergleichende Objekt
     * @return {@code true}, wenn beide Fluggesellschaften denselben Airline-Code besitzen, sonst {@code false}
     */
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
