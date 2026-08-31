package avigator.modell;

/**
 * Gibt den Status einer Buchung an.
 * <p>
 * Aktive Buchungen haben den Status {@code AKTIV} oder {@code UMGEBUCHT}.
 * Nicht mehr gueltige Buchungen haben den Status {@code STORNIERT} oder {@code VERGANGEN}.
 */
public enum Buchungsstatus {

    /**
     * Kennzeichnet eine urspruengliche, aktive Buchung.
     */
    AKTIV,

    /**
     * Kennzeichnet eine stornierte und damit nicht mehr gueltige Buchung.
     */
    STORNIERT,

    /**
     * Kennzeichnet eine weiterhin aktive, bereits geaenderte Buchung.
     */
    UMGEBUCHT,

    /**
     * Kennzeichnet eine nicht mehr gueltige Buchung, deren Flug bereits abgeflogen ist.
     */
    VERGANGEN
}
