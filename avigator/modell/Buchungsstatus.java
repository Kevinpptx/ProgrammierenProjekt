package avigator.modell;

/**
 * Gibt den Status einer avigator.modell.Buchung an.
 * <p>
 * AKTIV: ursprüngliche, aktive avigator.modell.Buchung
 * <p>
 * UMGEBUCHT: weiterhin aktive, aber bereits ein- oder mehrmals geänderte avigator.modell.Buchung
 * <p>
 * STORNIERT: nicht mehr gültige avigator.modell.Buchung, weil sie schon storniert ist
 * <p>
 * VERGANGEN: eine avigator.modell.Buchung in der Vergangenheit (anhand der Abflugzeit des Fluges, da abgeflogene Flüge nicht mehr gebucht werden können)
 * <p>
 * WICHTIG: ALLE Methoden behandeln AKTIV und UMGEBUCHT als gültige Buchungen und STORNIERT und VERGANGEN als ungültig.
 */
public enum Buchungsstatus {
    AKTIV,
    STORNIERT,
    UMGEBUCHT,
    VERGANGEN
}
