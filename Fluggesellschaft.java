import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Die Klasse {@code Fluggesellschaft} repräsentiert eine Fluggesellschaft mit
 * einem Namen, einem Airline-Code
 * und einer Flotte von Flugzeugen.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Fluggesellschaft implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /** Name der Airline */
    private String name;

    /** Airlinecode, der aus zwei Großbuchstaben besteht. */
    private String airlineCode;

    /**
     * Flotte der Airline, hier eine {@code ArrayList} aus {@code Flugzeug} -
     * Objekten.
     */
    private ArrayList<Flugzeug> flotte = new ArrayList<>();

    /**
     * Erzeugt eine neue Fluggesellschaft mit einem Namen und einem Airline-Code.
     *
     * @param name        der Name der Fluggesellschaft
     * @param airlineCode der eindeutige Airline-Code der Fluggesellschaft
     * @throws IllegalArgumentException , wenn die Parameter eine {@code null}-
     *                                  Referenz haben, leer sind oder der
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
     * Fügt ein Flugzeug zur Flotte der Fluggesellschaft hinzu.
     * Befindet sich das Flugzeug bereits in der Flotte, erfolgt keine Änderung.
     *
     * @param f das hinzuzufügende Flugzeug
     * @throws IllegalArgumentException wenn das übergebene Flugzeug eine
     *                                  null-Referenz enthält. Dies sollte
     *                                  eigentlich nicht
     *                                  vorkommen, weil das Verwaltungssystem
     *                                  als einzige Klasse die Methode aufruft und
     *                                  diese vorher ein Flugzeug-Objekt erzeugt.
     */
    public void fuegeFlugzeugHinzu(Flugzeug f) {
        if (f == null) {
            throw new IllegalArgumentException("Das hinzuzufügende Flugzeug enthält eine null-Referenz.");
        }

        if (!this.flotte.contains(f)) {
            this.flotte.add(f);
        } else {
          throw new IllegalArgumentException("Das Flugzeug existiert schon in der Flotte.");
        }  
    }

    /**
     * Entfernt ein Flugzeug aus der Flotte der Fluggesellschaft.
     * Befindet sich das Flugzeug nicht in der Flotte, erfolgt keine Änderung.
     *
     * @param f das zu entfernende Flugzeug
     */
    public void entferneFlugzeug(Flugzeug f) {
        if (!this.flotte.isEmpty()) {
            if (this.flotte.contains(f)) {
                this.flotte.remove(f);
            }
        }
    }

    /**
     * Gibt die Flotte der Fluggesellschaft zurück.
     *
     * @return eine Liste aller Flugzeuge der Fluggesellschaft
     */
    public List<Flugzeug> getFlotte() {
        return List.copyOf(flotte);
    }

    /**
     * Gibt den Namen der Fluggesellschaft zurück.
     *
     * @return der Name der Fluggesellschaft
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gibt den Airline-Code der Fluggesellschaft zurück.
     *
     * @return der Airline-Code der Fluggesellschaft
     */
    public String getAirlineCode() {
        return this.airlineCode;
    }

    /**
     * Prüft, ob die Flotte der Airline ein gewisses Flugzeug beinhaltet.
     * @param flugzeug : Das zu überprüfende Flugzeug
     * @return {@code true}, wenn das Flugzeug in der Flotte der Airline ist 
     */
    public boolean besitztFlugzeug(Flugzeug flugzeug) {
        return flotte.contains(flugzeug);
    }

    /**
     * Gibt eine textuelle Beschreibung der Fluggesellschaft zurück.
     * Die Beschreibung enthält den Namen, den Airline-Code und die Flugzeuge
     * der Fluggesellschaft.
     *
     * @return die textuelle Beschreibung der Fluggesellschaft
     */
    @Override
    public String toString() {
        String s;
        s = "Die Airline " + this.name + " betreibt unter Airline Code: " + this.airlineCode + " die Flugzeuge: "
                + this.flotte.toString();
        return s;
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
