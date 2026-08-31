package avigator.verwaltung;

import java.io.Serial;
import java.io.Serializable;

/**
 * Buendelt die dauerhaft zu speichernden Verwaltungs- und Buchungsdaten der Anwendung.
 *
 * @author Cedric Beckmann
 */
public class Anwendungsdaten implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Verwaltungssystem fuer Fluggesellschaften, Flugzeuge, Flughaefen und Fluege.
     */
    private final Verwaltungssystem verwaltungssystem;

    /**
     * Buchungssystem fuer Passagiere und Buchungen.
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
     * Gibt das Verwaltungssystem zurueck.
     *
     * @return das Verwaltungssystem der Anwendung
     */
    public Verwaltungssystem getVerwaltungssystem() {

        return this.verwaltungssystem;
    }

    /**
     * Gibt das Buchungssystem zurueck.
     *
     * @return das Buchungssystem der Anwendung
     */
    public Buchungssystem getBuchungssystem() {

        return this.buchungssystem;
    }
}
