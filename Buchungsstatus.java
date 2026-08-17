/**
 * Gibt den Status einer Buchung an.
 * <p>
 * AKTIV: ursprüngliche aktive Buchung
 * <p>
 * UMGEBUCHT: weiterhin aktive, aber bereits einmal geänderte Buchung
 * <p>
 * STORNIERT: nicht mehr gültige Buchung, weil sie schon storniert ist.
 * <p>
 * VERGANGEN: eine Buchung in der Vergangenheit (anhand der Abflugzeit des Fluges)
 * <p>
 * WICHTIG: ALLE Methoden behandeln AKTIV und UMGEBUCHT als gültige Buchungen und STORNIERT und VERGANGEN als ungültig.
 * Man kann aber Buchungen nur einmal umbuchen, also nur dann, wenn der Status AKTIV ist
 */
public enum Buchungsstatus {
    AKTIV,
    STORNIERT,
    UMGEBUCHT,
    VERGANGEN
}
