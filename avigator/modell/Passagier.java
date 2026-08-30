package avigator.modell;

import java.io.Serial;
import java.io.Serializable;

/**
 * Die Klasse {@code Passagier} speichert die Identifikations- und Kontaktdaten eines Passagiers.
 *
 * @param passagierId die ID des Passagiers
 * @param name        der Name des Passagiers
 * @param email       die E-Mail-Adresse des Passagiers
 */
public record Passagier(String passagierId, String name, String email) implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Erstellt einen Passagier mit einer ID, einem Namen und einer E-Mail-Adresse.
     *
     * @param passagierId die ID des Passagiers
     * @param name        der Name des Passagiers
     * @param email       die E-Mail-Adresse des Passagiers
     * @throws IllegalArgumentException wenn Name oder E-Mail-Adresse {@code null} oder leer sind oder die
     *                                  E-Mail-Adresse kein gültiges Format besitzt
     */
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
     * Validiert mithilfe eines regulären Ausdrucks, ob die übergebene E-Mail-Adresse dem erwarteten Format entspricht.
     *
     * @param email die zu validierende E-Mail-Adresse
     * @throws IllegalArgumentException wenn die E-Mail-Adresse kein gültiges Format besitzt
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
