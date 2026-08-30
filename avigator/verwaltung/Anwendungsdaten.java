package avigator.verwaltung;

import java.io.Serial;
import java.io.Serializable;

public class Anwendungsdaten implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private final Verwaltungssystem verwaltungssystem;

    private final Buchungssystem buchungssystem;

    public Anwendungsdaten() {

        this.verwaltungssystem = new Verwaltungssystem();
        this.buchungssystem = new Buchungssystem();
    }

    public Verwaltungssystem getVerwaltungssystem() {

        return this.verwaltungssystem;
    }

    public Buchungssystem getBuchungssystem() {

        return this.buchungssystem;
    }
}
