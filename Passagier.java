import java.io.Serial;
import java.io.Serializable;

/**
 * Die Klasse Passagier speichert Informationen über einen Passagier.
 *
 * @param passagierId Die ID des Passagiers.
 * @param name        Der Name des Passagiers.
 * @param email       Die E-Mail-Adresse des Passagiers.
 */
public record Passagier(String passagierId, String name, String email) implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Konstruktor für die Klasse Passagier.
     *
     * @param passagierId blabla Java-Doc kommt noch
     * @param name        blabla Java-Doc kommt noch
     * @param email       blabla Java-Doc kommt noch
     */
    // Konstruktor
    public Passagier {

        if (name == null || email == null) {
            throw new IllegalArgumentException("Die Felder Name und E-Mail dürfen keine null-Referenz beinhalten.");
        }

        if (name.isBlank() || email.isBlank()) {

            throw new IllegalArgumentException("Die Felder Name und E-Mail dürfen nicht leer sein.");
        }

        validiereEmail(email);

    }

    /**
     * Validiert die übergebene E-Mail-Adresse in Bezug darauf, ob sie dem Format einer Mail-Adresse entspricht (mithilfe
     * eines regulären Ausdrucks).
     *
     * @param email blabla Java-Doc kommt noch
     */
    private void validiereEmail(String email) {

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Bitte geben Sie eine gültige E-Mail-Adresse ein!");
        }
    }

    /**
     * Gibt die Passagier-ID zurück.
     *
     * @return die Passagier-ID
     */
    @Override
    public String passagierId() {

        return passagierId;
    }

    /**
     * Gibt den Namen des Passagiers zurück.
     *
     * @return den Namen des Passagiers
     */
    @Override
    public String name() {

        return name;
    }

    /**
     * Gibt die E-Mail-Adresse des Passagiers zurück.
     *
     * @return die E-Mail-Adresse des Passagiers
     */
    @Override
    public String email() {

        return email;
    }

    /**
     * Gibt eine String-Darstellung des Passagiers zurück.
     *
     * @return eine String-Darstellung des Passagiers
     */
    @Override
    public String toString() {

        return "Passagier ID: " + passagierId + ", Name: " + name + ", Email: " + email;
    }

}
