import java.io.Serializable;
import java.util.Locale;

/**
 * Die Klasse {@code Flughafen} repräsentiert einen Flughafen. Ein Flughafen wird durch seinen Namen, seinen IATA-Code,
 * die Stadt sowie das Land beschrieben.
 * <p>
 * Die Klasse dient als Grundlage für weitere Klassen, beispielsweise zur Modellierung von Flügen.
 *
 * @author Kevin Braun
 * @version 1.0
 */
public class Flughafen implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name des Flughafens.
     */
    private final String name;

    /**
     * Dreistelliger IATA-Code des Flughafens.
     */
    private final String iataCode;

    /**
     * Stadt, in der sich der Flughafen befindet.
     */
    private final String stadt;

    /**
     * Land, in dem sich der Flughafen befindet.
     */
    private final String land;

    /**
     * Erstellt einen neuen Flughafen mit den angegebenen Eigenschaften.
     *
     * @param name     Name des Flughafens
     * @param iataCode IATA-Code des Flughafens
     * @param stadt    Stadt des Flughafens
     * @param land     Land des Flughafens
     * @throws IllegalArgumentException , wenn die Strings eine {@code null} - Referenz enthalten oder leer sind.
     */
    public Flughafen(String name, String iataCode, String stadt, String land) {

        if (name == null || iataCode == null || stadt == null || land == null) {
            throw new IllegalArgumentException("Die angegebenen Parameter dürfen keine null-Referenz enthalten.");
        }

        if (name.isBlank() || iataCode.isBlank() || stadt.isBlank() || land.isBlank()) {
            throw new IllegalArgumentException("Die angegebenen Parameter dürfen nicht leer sein.");
        }
        if (iataCode.length() != 3) {
            throw new IllegalArgumentException("Der IATA-Code muss dreistellig im Format XYZ eingegeben werden.");
        }

        this.name = name;
        // entfernt Leerzeichen im IATA-Code, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.iataCode = iataCode.trim().toUpperCase(Locale.ROOT);
        this.stadt = stadt;
        this.land = land;
    }

    /**
     * Gibt den Namen des Flughafens zurück.
     *
     * @return Name des Flughafens
     */
    public String getName() {

        return this.name;
    }

    /**
     * Gibt den IATA-Code des Flughafens zurück.
     *
     * @return IATA-Code des Flughafens
     */
    public String getIataCode() {

        return this.iataCode;
    }

    /**
     * Gibt die Stadt des Flughafens zurück.
     *
     * @return Stadt des Flughafens
     */
    public String getStadt() {

        return this.stadt;
    }

    /**
     * Gibt das Land des Flughafens zurück.
     *
     * @return Land des Flughafens
     */
    public String getLand() {

        return this.land;
    }

    /**
     * Überschreibt die toString()-Methode, um eine Beschreibung des Flughafens zu liefern.
     *
     * @return Beschreibung des Flughafens mit Name, IATA-Code, Stadt und Land
     */
    @Override
    public String toString() {

        return "Der Flughafen " + this.name + " mit IATA-Code "
               + this.iataCode + " befindet sich in " + this.stadt
               + ", " + this.land;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flughafen f = (Flughafen) o;
        return this.iataCode.equalsIgnoreCase(f.getIataCode());
    }
}