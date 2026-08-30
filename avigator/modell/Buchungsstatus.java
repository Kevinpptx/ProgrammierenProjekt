package avigator.modell;

/**
 * Gibt den Status einer Buchung an.
 * <p>
 * Aktive Buchungen haben den Status {@code AKTIV} oder {@code UMGEBUCHT}.
 * Nicht mehr gültige Buchungen haben den Status {@code STORNIERT} oder {@code VERGANGEN}.
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
