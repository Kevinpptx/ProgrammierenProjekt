package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

/**
 * Repräsentiert einen Sitzplatz in einem Flugzeug. Ein Sitzplatz besitzt eine Sitzplatznummer, eine Sitzklasse, eine
 * Buchung (wenn er schon verbucht ist) sowie einen Belegungsstatus.
 */
public class Sitzplatz implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Eindeutige Nummer des Sitzplatzes.
     */
    private final String sitzplatzNummer;

    /**
     * Sitzklasse des Sitzplatzes.
     */
    private final Sitzklasse sitzklasse;

    /**
     * Buchung, die dem Sitz zugewiesen ist. Bei Erstellung hat dieses Attribut eine {@code null}-Referenz.
     */
    private Buchung buchung;

    /**
     * Gibt an, ob der Sitzplatz belegt ist.
     */
    private boolean belegt;

    /**
     * Erstellt einen neuen Sitzplatz mit der angegebenen Sitzplatznummer und Sitzklasse. Der Sitzplatz ist nach der
     * Erstellung zunächst frei. Die Referenz für die Buchung ist {@code null}.
     *
     * @param sitzplatzNummer die Nummer des Sitzplatzes
     * @param sitzklasse      die Sitzklasse des Sitzplatzes
     * @throws IllegalArgumentException wenn die Parameter {@code null}-Referenzen enthalten oder die
     *                                  Sitzplatznummer leer ist
     */
    public Sitzplatz(String sitzplatzNummer, Sitzklasse sitzklasse) {

        if (sitzplatzNummer == null || sitzklasse == null) {
            throw new IllegalArgumentException("Keiner der beiden Parameter darf eine null-Referenz enthalten.");
        }

        if (sitzplatzNummer.isBlank()) {
            throw new IllegalArgumentException("Die Sitzplatznummer darf nicht leer sein");
        }

        // entfernt führende und nachfolgende Leerzeichen in der Sitzplatznummer, wandelt Klein- in Großbuchstaben um
        // und behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.sitzplatzNummer = sitzplatzNummer.trim().toUpperCase(Locale.ROOT);
        this.sitzklasse = sitzklasse;

        freigeben();
    }

    /**
     * Gibt zurück, ob der Sitzplatz frei ist.
     *
     * @return {@code true}, wenn der Sitzplatz frei ist, {@code false}, wenn er belegt ist
     */
    public boolean getIstFrei() {

        return ! this.belegt;
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
     * Markiert den Sitzplatz als belegt und weist ihm eine Buchung zu.
     *
     * @param buchung die Buchung, die dem Sitzplatz zugewiesen wird
     * @throws IllegalArgumentException wenn die Buchung eine {@code null}-Referenz enthält oder der Sitzplatz
     *                                  schon belegt ist
     */
    public void belegen(Buchung buchung) {

        if (buchung == null) {
            throw new IllegalArgumentException("Bitte geben Sie eine Buchung an, die diesen Sitzplatz belegen soll.");
        }
        if (belegt) {
            throw new IllegalArgumentException("Der Sitz ist leider schon belegt.");
        }
        this.belegt = true;
        this.buchung = buchung;
    }

    /**
     * Markiert den Sitzplatz als frei und löscht die Referenz auf die Buchung.
     */
    public void freigeben() {

        this.belegt = false;
        this.buchung = null;
    }

    /**
     * Gibt die Buchungsreferenz des Sitzplatzes zurück.
     *
     * @return die zugewiesene Buchung oder {@code null}, wenn der Sitzplatz frei ist
     */
    public Buchung getBuchung() {

        return buchung;
    }

    /**
     * Gibt eine Beschreibung des Sitzplatzes zurück.
     *
     * @return die Beschreibung mit Sitzplatznummer, Sitzklasse und aktuellem Belegungsstatus
     */
    @Override
    public String toString() {

        String output = "Der Sitzplatz mit Nummer " + this.sitzplatzNummer +
                        " in der Klasse " + this.sitzklasse + " ist ";

        if (this.belegt) {
            output += "belegt.";
        } else {
            output += "nicht belegt.";
        }

        return output;
    }
}
