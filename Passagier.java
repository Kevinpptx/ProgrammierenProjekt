import java.io.Serializable;

/**
 * Die Klasse Passagier speichert Informationen über einen Passagier.
 */
public class Passagier implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die ID des Passagiers.
     */
    private final String passagierId;

    /**
     * Der Name des Passagiers.
     */
    private final String name;

    /**
     * Die E-Mail-Adresse des Passagiers.
     */
    private String email;

    /**
     * Konstruktor für die Klasse Passagier.
     *
     * @param passagierId
     * @param name
     * @param email
     */
    // Konstruktor
    public Passagier(String passagierId, String name, String email) {

        if (name == null || email == null) {
            throw new IllegalArgumentException("Die Felder Name und E-Mail dürfen keine null-Referenz beinhalten.");
        }

        if (name.isBlank() || email.isBlank()) {

            throw new IllegalArgumentException("Die Felder Name und E-Mail dürfen nicht leer sein.");
        }

        try {
            validiereEmail(email);
        } catch (Exception e) {
            throw e;
        }

        this.passagierId = passagierId;
        this.name = name;
        this.email = email;
    }

    /**
     * Validiert die übergebene Email-Adresse in Bezug darauf, ob sie dem Format einer Mail-Adresse entspricht (mithilfe
     * eines regulären Ausdrucks).
     *
     * @param email
     */
    private void validiereEmail(String email) {

        if (! email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Bitte geben Sie eine gültige E-Mail-Adresse ein!");
        }
    }

    /**
     * Gibt die Passagier-ID zurück.
     *
     * @return die Passagier-ID
     */
    public String getPassagierId() {

        return passagierId;
    }

    /**
     * Gibt den Namen des Passagiers zurück.
     *
     * @return den Namen des Passagiers
     */
    public String getName() {

        return name;
    }

    /**
     * Gibt die E-Mail-Adresse des Passagiers zurück.
     *
     * @return die E-Mail-Adresse des Passagiers
     */
    public String getEmail() {

        return email;
    }

    /**
     * Setzt die E-Mail-Adresse des Passagiers. Validiert sie genau wie im Konstruktor.
     *
     * @param email
     */
    // Setter-Methoden
    public void setEmail(String email) {

        try {
            validiereEmail(email);
        } catch (Exception e) {
            throw e;
        }
        this.email = email;
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
