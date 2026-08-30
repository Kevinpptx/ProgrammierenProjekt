package avigator.verwaltung;

import java.io.Serial;
import java.io.Serializable;

/**
 * Bündelt die dauerhaft zu speichernden Verwaltungs- und Buchungsdaten der Anwendung.
 *
 * @author Cedric Beckmann
 */
public class Anwendungsdaten implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Verwaltungssystem für Fluggesellschaften, Flugzeuge, Flughäfen und Flüge.
     */
    private final Verwaltungssystem verwaltungssystem;

    /**
     * Buchungssystem für Passagiere und Buchungen.
     */
    private final Buchungssystem buchungssystem;

    /**
     * Erstellt leere Anwendungsdaten mit einem neuen Verwaltungs- und Buchungssystem.
     */
    public Anwendungsdaten() {

        this.verwaltungssystem = new Verwaltungssystem();
        this.buchungssystem = new Buchungssystem();
    }

    /**
     * Gibt das Verwaltungssystem zurück.
     *
     * @return das Verwaltungssystem der Anwendung
     */
    public Verwaltungssystem getVerwaltungssystem() {

        return this.verwaltungssystem;
    }

    /**
     * Gibt das Buchungssystem zurück.
     *
     * @return das Buchungssystem der Anwendung
     */
    public Buchungssystem getBuchungssystem() {

        return this.buchungssystem;
    }
}
