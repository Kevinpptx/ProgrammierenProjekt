/**
 * Die Klasse {@code Flughafen} repräsentiert einen Flughafen.
 * Ein Flughafen wird durch seinen Namen, seinen IATA-Code,
 * die Stadt sowie das Land beschrieben.
 *
 * Die Klasse dient als Grundlage für weitere Anwendungen,
 * beispielsweise zur Modellierung von Flügen.
 *
 * @author Kevin Braun
 * @version 1.0
 */
public class Flughafen {

    /** Name des Flughafens. */
    private String name;

    /** Dreistelliger IATA-Code des Flughafens. */
    private String iataCode;

    /** Stadt, in der sich der Flughafen befindet. */
    private String stadt;

    /** Land, in dem sich der Flughafen befindet. */
    private String land;

    /**
     * Erstellt einen neuen Flughafen mit den angegebenen Eigenschaften.
     *
     * @param name Name des Flughafens
     * @param iataCode IATA-Code des Flughafens
     * @param stadt Stadt des Flughafens
     * @param land Land des Flughafens
     */
    public Flughafen(String name, String iataCode, String stadt, String land) {
        this.name = name;
        this.iataCode = iataCode;
        this.stadt = stadt;
        this.land = land;
    }

    /**
     * Gibt den Namen des Flughafens zurück.
     *
     * @return Name des Flughafens
     */
    public String GetName() {
        return this.name;
    }

    /**
     * Gibt den IATA-Code des Flughafens zurück.
     *
     * @return IATA-Code des Flughafens
     */
    public String GetIataCode() {
        return this.iataCode;
    }

    /**
     * Gibt die Stadt des Flughafens zurück.
     *
     * @return Stadt des Flughafens
     */
    public String GetStadt() {
        return this.stadt;
    }

    /**
     * Gibt das Land des Flughafens zurück.
     *
     * @return Land des Flughafens
     */
    public String GetLand() {
        return this.land;
    }

    /**
     * Setzt den Namen des Flughafens.
     *
     * @param name Neuer Name des Flughafens
     */
    public void SetName(String name) {
        this.name = name;
    }

    /**
     * Setzt den IATA-Code des Flughafens.
     *
     * @param iataCode Neuer IATA-Code des Flughafens
     */
    public void SetIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    /**
     * Setzt die Stadt des Flughafens.
     *
     * @param stadt Neue Stadt des Flughafens
     */
    public void SetStadt(String stadt) {
        this.stadt = stadt;
    }

    /**
     * Setzt das Land des Flughafens.
     *
     * @param land Neues Land des Flughafens
     */
    public void SetLand(String land) {
        this.land = land;
    }

    /**
     * Überschreibt die toString()-Methode, um eine Beschreibung des Flughafens zu liefern.
     *
     * @return Beschreibung des Flughafens mit Name, IATA-Code,
     *         Stadt und Land
     */
    @Override
    public String toString() {
        return "Der Flughafen " + this.name + " mit IATA-Code "
                + this.iataCode + " befindet sich in " + this.stadt
                + ", " + this.land;
    }
}