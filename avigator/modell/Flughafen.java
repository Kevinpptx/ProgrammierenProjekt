package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

/**
 * Die Klasse {@code avigator.modell.Flughafen} repräsentiert einen avigator.modell.Flughafen. Ein avigator.modell.Flughafen wird durch seinen Namen, seinen IATA-Code,
 * die Stadt sowie das Land beschrieben.
 * <p>
 * Die Klasse dient als Grundlage für weitere Klassen, beispielsweise zur Modellierung von Flügen.
 *
 * @param name     Name des Flughafens.
 * @param iataCode Dreistelliger IATA-Code des Flughafens.
 * @param stadt    Stadt, in der sich der avigator.modell.Flughafen befindet.
 * @param land     Land, in dem sich der avigator.modell.Flughafen befindet.
 * @author Kevin Braun
 * @version 1.0
 */
public record Flughafen(String name, String iataCode, String stadt, String land) implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Erstellt einen neuen avigator.modell.Flughafen mit den angegebenen Eigenschaften.
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
    @Override
    public String name() {

        return this.name;
    }

    /**
     * Gibt den IATA-Code des Flughafens zurück.
     *
     * @return IATA-Code des Flughafens
     */
    @Override
    public String iataCode() {

        return this.iataCode;
    }

    /**
     * Gibt die Stadt des Flughafens zurück.
     *
     * @return Stadt des Flughafens
     */
    @Override
    public String stadt() {

        return this.stadt;
    }

    /**
     * Gibt das Land des Flughafens zurück.
     *
     * @return Land des Flughafens
     */
    @Override
    public String land() {

        return this.land;
    }

    /**
     * Überschreibt die toString()-Methode, um eine Beschreibung des Flughafens zu liefern.
     *
     * @return Beschreibung des Flughafens mit Name, IATA-Code, Stadt und Land
     */
    @Override
    public String toString() {

        return "Der avigator.modell.Flughafen " + this.name + " mit IATA-Code "
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

        Flughafen flughafen = (Flughafen) o;

        return this.iataCode.equalsIgnoreCase(flughafen.iataCode());
    }
}