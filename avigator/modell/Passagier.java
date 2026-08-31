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
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
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
     *                                  E-Mail-Adresse kein gueltiges Format besitzt
     */
    public Passagier {

        if (name == null || email == null) {
            throw new IllegalArgumentException("Die Felder Name und E-Mail duerfen keine null-Referenz beinhalten.");
        }

        if (name.isBlank() || email.isBlank()) {

            throw new IllegalArgumentException("Die Felder Name und E-Mail duerfen nicht leer sein.");
        }

        validiereEmail(email);

    }

    /**
     * Validiert mithilfe eines regulaeren Ausdrucks, ob die uebergebene E-Mail-Adresse dem erwarteten Format entspricht.
     *
     * @param email die zu validierende E-Mail-Adresse
     * @throws IllegalArgumentException wenn die E-Mail-Adresse kein gueltiges Format besitzt
     */
    private void validiereEmail(String email) {

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Bitte geben Sie eine gueltige E-Mail-Adresse ein!");
        }
    }

    /**
     * Gibt die Passagier-ID zurueck.
     *
     * @return die Passagier-ID
     */
    @Override
    public String passagierId() {

        return passagierId;
    }

    /**
     * Gibt den Namen des Passagiers zurueck.
     *
     * @return den Namen des Passagiers
     */
    @Override
    public String name() {

        return name;
    }

    /**
     * Gibt die E-Mail-Adresse des Passagiers zurueck.
     *
     * @return die E-Mail-Adresse des Passagiers
     */
    @Override
    public String email() {

        return email;
    }

    /**
     * Gibt eine String-Darstellung des Passagiers zurueck.
     *
     * @return eine String-Darstellung des Passagiers
     */
    @Override
    public String toString() {

        return "Passagier ID: " + passagierId + ", Name: " + name + ", Email: " + email;
    }

}
