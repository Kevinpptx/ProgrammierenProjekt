package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

/**
 * Repräsentiert einen avigator.modell.Sitzplatz in einem avigator.modell.Flugzeug. Ein avigator.modell.Sitzplatz besitzt eine Sitzplatznummer, eine avigator.modell.Sitzklasse, eine
 * avigator.modell.Buchung (wenn er schon verbucht ist) sowie einen Belegungsstatus.
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
     * avigator.modell.Sitzklasse des Sitzplatzes.
     */
    private final Sitzklasse sitzklasse;

    /**
     * avigator.modell.Buchung, die dem Sitz zugewiesen ist. Bei Erstellung hat dieses Attribut eine null-Referenz.
     */
    private Buchung buchung;

    /**
     * Gibt an, ob der avigator.modell.Sitzplatz belegt ist.
     */
    private boolean belegt;

    /**
     * Erstellt einen neuen avigator.modell.Sitzplatz mit der angegebenen Sitzplatznummer und avigator.modell.Sitzklasse. Der avigator.modell.Sitzplatz ist nach der
     * Erstellung zunächst frei. Die Referenz für die avigator.modell.Buchung ist {@code null}.
     *
     * @param sitzplatzNummer die Nummer des Sitzplatzes
     * @param sitzklasse      die avigator.modell.Sitzklasse des Sitzplatzes
     * @throws IllegalArgumentException , wenn die Parameter {@code null}-Referenzen enthalten, oder wenn die
     *                                  Buchungsnummer leer ist.
     */
    public Sitzplatz(String sitzplatzNummer, Sitzklasse sitzklasse) {

        if (sitzplatzNummer == null || sitzklasse == null) {
            throw new IllegalArgumentException("Keiner der beiden Parameter darf eine null-Referenz enthalten.");
        }

        if (sitzplatzNummer.isBlank()) {
            throw new IllegalArgumentException("Die Sitzplatznummer darf nicht leer sein");
        }

        // entfernt Leerzeichen in der Sitzplatznummer, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.sitzplatzNummer = sitzplatzNummer.trim().toUpperCase(Locale.ROOT);
        this.sitzklasse = sitzklasse;

        freigeben();
    }

    /**
     * Gibt zurück, ob der avigator.modell.Sitzplatz frei ist.
     *
     * @return {@code true}, wenn der avigator.modell.Sitzplatz frei ist, {@code false}, wenn er belegt ist
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
     * Gibt die avigator.modell.Sitzklasse des Sitzplatzes zurück.
     *
     * @return die avigator.modell.Sitzklasse
     */
    public Sitzklasse getSitzklasse() {

        return this.sitzklasse;
    }

    /**
     * Markiert den avigator.modell.Sitzplatz als belegt und weist ihm eine avigator.modell.Buchung zu.
     *
     * @throws IllegalArgumentException , wenn die avigator.modell.Buchung eine {@code null} - Referenz enthält oder wenn der avigator.modell.Sitzplatz
     *                                  schon belegt ist.
     */
    public void belegen(Buchung buchung) {

        if (buchung == null) {
            throw new IllegalArgumentException("Bitte geben Sie eine avigator.modell.Buchung an, die diesen avigator.modell.Sitzplatz belegen soll.");
        }
        if (belegt) {
            throw new IllegalArgumentException("Der Sitz ist leider schon belegt.");
        }
        this.belegt = true;
        this.buchung = buchung;
    }

    /**
     * Markiert den avigator.modell.Sitzplatz als frei und löscht die Referenz auf die avigator.modell.Buchung.
     */
    public void freigeben() {

        this.belegt = false;
        this.buchung = null;
    }

    /**
     * Gibt die Buchungsreferenz des Sitzplatzes zurück.
     *
     * @return buchung
     */
    public Buchung getBuchung() {

        return buchung;
    }

    /**
     * Gibt eine Beschreibung des Sitzplatzes zurück.
     *
     * @return Beschreibung mit Sitzplatznummer, avigator.modell.Sitzklasse und aktuellem Belegungsstatus
     */
    @Override
    public String toString() {

        String output = "Der avigator.modell.Sitzplatz mit Nummer " + this.sitzplatzNummer +
                        " in der Klasse " + this.sitzklasse + " ist ";

        // Output anpassen, je nachdem, ob avigator.modell.Sitzplatz gerade belegt ist
        if (this.belegt) {
            output += "belegt.";
        } else {
            output += "nicht belegt.";
        }

        return output;
    }
}
