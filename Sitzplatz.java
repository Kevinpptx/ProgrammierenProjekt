/**
 * Repräsentiert einen Sitzplatz in einem Flugzeug.
 * Ein Sitzplatz besitzt eine Sitzplatznummer, eine Sitzklasse
 * sowie einen Belegungsstatus.
 */
public class Sitzplatz {

    /** Eindeutige Nummer des Sitzplatzes. */
    private String sitzplatzNummer;

    /** Sitzklasse des Sitzplatzes. */
    private Sitzklasse sitzklasse;

    private int anzahlKoffer;

    /** Gibt an, ob der Sitzplatz belegt ist. */
    private boolean belegt;

    /**
     * Erstellt einen neuen Sitzplatz mit der angegebenen
     * Sitzplatznummer und Sitzklasse.
     * Der Sitzplatz ist nach der Erstellung zunächst frei.
     *
     * @param sitzplatznummer die Nummer des Sitzplatzes
     * @param sitzklasse die Sitzklasse des Sitzplatzes
     */
    public Sitzplatz(String sitzplatzNummer, Sitzklasse sitzklasse) {
        this.sitzplatzNummer = sitzplatzNummer;
        this.sitzklasse = sitzklasse;
        this.anzahlKoffer = 0;
        freigeben();
    }

    /**
     * Gibt zurück, ob der Sitzplatz frei ist.
     *
     * @return {@code true}, wenn der Sitzplatz frei ist,
     *         {@code false}, wenn er belegt ist
     */
    public boolean getIstFrei() {
        return !this.belegt;
    }

    /**
     * Gibt die Sitzplatznummer zurück.
     *
     * @return die Sitzplatznummer
     */
    public String getSitzplatzNummer() {
        return this.sitzplatzNummer;
    }

    /**
     * Gibt die Sitzklasse des Sitzplatzes zurück.
     *
     * @return die Sitzklasse
     */
    public Sitzklasse getSitzklasse() {
        return this.sitzklasse;
    }

    /**
     * Markiert den Sitzplatz als belegt.
     */
    public void belegen() {
        this.belegt = true;
    }

    /**
     * Markiert den Sitzplatz als frei.
     */
    public void freigeben() {
        this.belegt = false;
    }

    public int getAnzahlKoffer() {
        return anzahlKoffer;
    }

    public void setAnzahlKoffer(int anzahlKoffer) {
        this.anzahlKoffer = anzahlKoffer;
    }

    /**
     * Gibt eine Beschreibung des Sitzplatzes zurück.
     *
     * @return Beschreibung mit Sitzplatznummer, Sitzklasse
     *         und aktuellem Belegungsstatus
     */
    @Override
    public String toString() {

        String output = "Der Sitzplatz mit Nummer " + this.sitzplatzNummer +
                " in der Klasse " + this.sitzklasse + " ist ";

        // Output anpassen, je nachdem, ob Sitzplatz gerade belegt ist
        if (this.belegt) {
            output += "belegt.";
        } else {
            output += "nicht belegt.";
        }

        return output;
    }
}
