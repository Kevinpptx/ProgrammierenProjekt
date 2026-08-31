package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

/**
 * Die Klasse {@code Flughafen} repraesentiert einen Flughafen. Ein Flughafen wird durch seinen Namen, seinen IATA-Code,
 * die Stadt sowie das Land beschrieben.
 * <p>
 * Die Klasse dient als Grundlage fuer weitere Klassen, beispielsweise zur Modellierung von Fluegen.
 *
 * @param name     der Name des Flughafens
 * @param iataCode der IATA-Code des Flughafens
 * @param stadt    die Stadt, in der sich der Flughafen befindet
 * @param land     das Land, in dem sich der Flughafen befindet
 * @author Kevin Braun
 * @version 1.1
 */
public record Flughafen(String name, String iataCode, String stadt, String land) implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Erstellt einen neuen Flughafen mit den angegebenen Eigenschaften.
     *
     * @param name     der Name des Flughafens
     * @param iataCode der IATA-Code des Flughafens
     * @param stadt    die Stadt des Flughafens
     * @param land     das Land des Flughafens
     * @throws IllegalArgumentException wenn die Strings eine {@code null}-Referenz enthalten oder leer sind oder die
     *                                  Eingabe fuer den IATA-Code nicht genau drei Zeichen umfasst
     */
    public Flughafen(String name, String iataCode, String stadt, String land) {

        if (name == null || iataCode == null || stadt == null || land == null) {
            throw new IllegalArgumentException("Die angegebenen Parameter duerfen keine null-Referenz enthalten.");
        }

        if (name.isBlank() || iataCode.isBlank() || stadt.isBlank() || land.isBlank()) {
            throw new IllegalArgumentException("Die angegebenen Parameter duerfen nicht leer sein.");
        }
        if (iataCode.length() != 3) {
            throw new IllegalArgumentException("Der IATA-Code muss dreistellig im Format XYZ eingegeben werden.");
        }

        this.name = name;

        // entfernt fuehrende und nachfolgende Leerzeichen im IATA-Code, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhaengig von der Spracheinstellung des Computers
        this.iataCode = iataCode.trim().toUpperCase(Locale.ROOT);
        this.stadt = stadt;
        this.land = land;
    }

    /**
     * Gibt den Namen des Flughafens zurueck.
     *
     * @return der Name des Flughafens
     */
    @Override
    public String name() {

        return this.name;
    }

    /**
     * Gibt den IATA-Code des Flughafens zurueck.
     *
     * @return der IATA-Code des Flughafens
     */
    @Override
    public String iataCode() {

        return this.iataCode;
    }

    /**
     * Gibt die Stadt des Flughafens zurueck.
     *
     * @return die Stadt des Flughafens
     */
    @Override
    public String stadt() {

        return this.stadt;
    }

    /**
     * Gibt das Land des Flughafens zurueck.
     *
     * @return das Land des Flughafens
     */
    @Override
    public String land() {

        return this.land;
    }

    /**
     * ueberschreibt die toString()-Methode, um eine Beschreibung des Flughafens zu liefern.
     *
     * @return die Beschreibung des Flughafens mit Name, IATA-Code, Stadt und Land
     */
    @Override
    public String toString() {

        return "Der Flughafen " + this.name + " mit IATA-Code "
                + this.iataCode + " befindet sich in " + this.stadt
                + ", " + this.land;
    }

    /**
     * Vergleicht zwei Flughaefen anhand ihres IATA-Codes.
     *
     * @param o das zu vergleichende Objekt
     * @return {@code true}, wenn beide Flughaefen denselben IATA-Code besitzen, sonst {@code false}
     */
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
