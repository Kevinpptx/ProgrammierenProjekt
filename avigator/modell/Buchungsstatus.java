package avigator.modell;

/**
 * Gibt den Status einer Buchung an und unterscheidet zwischen aktiven und nicht mehr gültigen Buchungen.
 * <p>
 * {@code AKTIV}: ursprüngliche, aktive Buchung.
 * <p>
 * {@code UMGEBUCHT}: weiterhin aktive, aber bereits ein- oder mehrmals geänderte Buchung.
 * <p>
 * {@code STORNIERT}: nicht mehr gültige, stornierte Buchung.
 * <p>
 * {@code VERGANGEN}: nicht mehr gültige Buchung, deren Flug bereits abgeflogen ist.
 * <p>
 * Die Status {@code AKTIV} und {@code UMGEBUCHT} kennzeichnen gültige Buchungen. {@code STORNIERT} und
 * {@code VERGANGEN} kennzeichnen ungültige Buchungen.
 */
public enum Buchungsstatus {

    /**
     * Kennzeichnet eine ursprüngliche, aktive Buchung.
     */
    AKTIV,

    /**
     * Kennzeichnet eine stornierte und damit nicht mehr gültige Buchung.
     */
    STORNIERT,

    /**
     * Kennzeichnet eine weiterhin aktive, bereits geänderte Buchung.
     */
    UMGEBUCHT,

    /**
     * Kennzeichnet eine nicht mehr gültige Buchung, deren Flug bereits abgeflogen ist.
     */
    VERGANGEN
}
