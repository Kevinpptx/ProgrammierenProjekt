import java.io.Serializable;
import java.util.Locale;

/**
 * Repräsentiert ein Flugzeug mit einem Sitzplan.
 * Das Flugzeug besitzt einen eindeutigen Code, ein Modell
 * sowie eine Vorlage aller vorhandenen Sitzplätze.
 */
public class Flugzeug implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Eindeutiger Code des Flugzeugs.
     */
    private String code;

    /**
     * Modellbezeichnung des Flugzeugs.
     */
    private String modell;

    /**
     * Anzahl der Economy-Sitzplätze.
     */
    private int anzahlEconomy;

    /**
     * Anzahl der Business-Sitzplätze.
     */
    private int anzahlBusiness;

    /**
     * Vorlage aller Sitzplätze. Die erste Dimension beschreibt die Sitzreihen,
     * die zweite Dimension die Sitzposition innerhalb einer Reihe.
     */
    private Sitzplatz[][] sitzplaetzeVorlage;

    /**
     * Erstellt ein neues Flugzeug mit dem angegebenen Sitzplan.
     *
     * Die Sitzplätze werden automatisch nummeriert (z. B. 1A, 1B, 1C, ...). Die
     * ersten {@code businessReihen} Reihen werden als Business-Class angelegt,
     * alle übrigen Reihen als Economy.
     *
     * @param code           eindeutiger Code des Flugzeugs
     * @param modell         Modellbezeichnung
     * @param anzahlReihen   Anzahl der Sitzreihen
     * @param sitzeProReihe  Anzahl der Sitzplätze pro Reihe
     * @param businessReihen Anzahl der Business-Reihen
     */
    public Flugzeug(String code,
            String modell,
            int anzahlReihen,
            int sitzeProReihe,
            int businessReihen) {

        // Validiert die übergebenen Attribute 

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Bitte geben Sie einen Code an.");
        }

        if (modell == null || modell.isBlank()) {
            throw new IllegalArgumentException("Bitte geben Sie einen Modellnamen an.");
        }

        if (anzahlReihen <= 0) {
            throw new IllegalArgumentException(
                    "Bitte geben Sie eine positive, ganzzahlige Anzahl an Sitzreihen von mindestens 1 ein!");
        }

        //Es ist erlaubt, dass ein Flugzeug keine Bisuness-Reihen hat (siehe Flugzeuge von "Billig-Airlines")
        if (businessReihen < 0 || businessReihen > anzahlReihen) {
            throw new IllegalArgumentException("Bitte geben Sie eine positive, ganzzahlige Anzahl an Business-Reihen von mindestens 0 ein. Die Anzahl der Business-Reihen darf zudem nicht größer als die Anzahl der verfügbaren Reihen sein.");
        }

        if (anzahlReihen > 100) {
            throw new IllegalArgumentException("Die Anzahl der Sitzreihen darf nicht größer als 100 sein.");
        }

        if (sitzeProReihe > 26) {
            throw new IllegalArgumentException("Die Anzahl der Sitzplätze pro Reihe darf nicht größer als 26 sein.");
        }
        
        if (sitzeProReihe < 1) {
            throw new IllegalArgumentException("Die Anzahl der Sitze pro Reihe muss mindestens 1 betragen.");
        }
      
        // entfernt Leerzeichen im Code, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers

        this.code = code.trim().toUpperCase(Locale.ROOT);
        this.modell = modell;

        this.sitzplaetzeVorlage = new Sitzplatz[anzahlReihen][sitzeProReihe];

        char sitzbuchstabe;

        for (int reihe = 0; reihe < anzahlReihen; reihe++) {

            for (int platz = 0; platz < sitzeProReihe; platz++) {

                sitzbuchstabe = (char) ('A' + platz);

                Sitzklasse sitzklasse;

                if (reihe < businessReihen) {
                    sitzklasse = Sitzklasse.BUSINESS;
                    this.anzahlBusiness++;
                } else {
                    sitzklasse = Sitzklasse.ECONOMY;
                    this.anzahlEconomy++;
                }

                String sitzplatzNummer = (reihe + 1) + "" + sitzbuchstabe;

                this.sitzplaetzeVorlage[reihe][platz] = new Sitzplatz(sitzplatzNummer, sitzklasse);
            }
        }
    }

    /**
     * Gibt die Gesamtanzahl aller Sitzplätze zurück.
     *
     * @return Gesamtanzahl der Sitzplätze
     */
    public int getGesamtSitzanzahl() {
        return this.anzahlBusiness + this.anzahlEconomy;
    }

    /**
     * Gibt den Code des Flugzeugs zurück.
     *
     * @return Code des Flugzeugs
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gibt die Modellbezeichnung des Flugzeugs zurück.
     *
     * @return Modellbezeichnung
     */
    public String getModell() {
        return this.modell;
    }

    /**
     * Gibt die Kopie Sitzplatzvorlage des Flugzeugs zurück.
     * Geht dafür die Zeilen der Vorlage durch und fügt diese mit "clone" in die
     * Kopie ein.
     * 
     * @return Kopie des zweidimensionalen Arrays mit allen Sitzplätzen
     */
    public Sitzplatz[][] getSitzplaetzeVorlage() {
        Sitzplatz[][] kopieVorlage = new Sitzplatz[sitzplaetzeVorlage.length][];

        for (int i = 0; i < sitzplaetzeVorlage.length; i++) {
            kopieVorlage[i] = sitzplaetzeVorlage[i].clone();
        }
        return kopieVorlage;
    }

    /**
     * Gibt eine Beschreibung des Flugzeugs einschließlich der
     * Sitzplatz-Anordnung zurück.
     *
     * @return Beschreibung des Flugzeugs und des Sitzplans
     */
    @Override
    public String toString() {

        String output = "Flugzeug " + this.code
                + " (" + this.modell + ")"
                + " besitzt " + getGesamtSitzanzahl()
                + " Sitzplätze ("
                + this.anzahlBusiness + " Business, "
                + this.anzahlEconomy + " Economy)."
                + "\n\nSitzplan:\n";

        for (int reihe = 0; reihe < this.sitzplaetzeVorlage.length; reihe++) {

            for (int platz = 0; platz < this.sitzplaetzeVorlage[reihe].length; platz++) {

                output += String.format("%-4s",
                        this.sitzplaetzeVorlage[reihe][platz].getSitzplatzNummer());

                // Nach der Hälfte der Sitze einen Gang darstellen
                if (platz == this.sitzplaetzeVorlage[reihe].length / 2 - 1) {
                    output += " | ";
                }
            }

            output += "\n";
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flugzeug f = (Flugzeug) o;
        return this.getCode().equalsIgnoreCase(f.getCode());
    }
}
