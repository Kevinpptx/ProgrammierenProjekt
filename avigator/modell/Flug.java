package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code avigator.modell.Flug} repräsentiert einen konkreten avigator.modell.Flug einer avigator.modell.Fluggesellschaft zwischen einem Start- und einem
 * Zielflughafen.
 * <p>
 * Ein avigator.modell.Flug besitzt eine Flugnummer, eine avigator.modell.Fluggesellschaft, ein eingesetztes avigator.modell.Flugzeug, einen Start- und Zielflughafen,
 * eine Abflug- und Ankunftszeit sowie einen Basispreis.
 * <p>
 * Zusätzlich besitzt jeder avigator.modell.Flug einen eigenen Sitzplan. Dieser wird bei der Erstellung des Fluges anhand der
 * Sitzplatzvorlage des eingesetzten Flugzeugs initialisiert. Dadurch kann die Sitzplatzbelegung für jeden avigator.modell.Flug
 * unabhängig verwaltet werden.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Flug implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Die eindeutige Flugnummer des Fluges
     */
    private final String flugnummer;

    /**
     * Die avigator.modell.Fluggesellschaft, die den avigator.modell.Flug durchführt
     */
    private final Fluggesellschaft fluggesellschaft;

    /**
     * Das für den avigator.modell.Flug eingesetzte avigator.modell.Flugzeug
     */
    private final Flugzeug flugzeug;

    /**
     * Der avigator.modell.Flughafen, von dem der avigator.modell.Flug startet
     */
    private final Flughafen startFlughafen;

    /**
     * Der avigator.modell.Flughafen, an dem der avigator.modell.Flug endet
     */
    private final Flughafen zielFlughafen;

    /**
     * Der geplante Zeitpunkt des Abflugs
     */
    private final LocalDateTime abflugzeit;

    /**
     * Der geplante Zeitpunkt der Ankunft
     */
    private final LocalDateTime ankunftszeit;

    /**
     * Der Basispreis des Fluges
     */
    private final double basispreis;

    /**
     * Der individuelle Sitzplan dieses Fluges. Jeder avigator.modell.Sitzplatz besitzt einen eigenen Belegungsstatus.
     */
    private Sitzplatz[][] sitzplan;

    /**
     * Erzeugt einen neuen avigator.modell.Flug mit den angegebenen Flugdaten.
     * <p>
     * Beim Erstellen des Fluges wird ein eigener Sitzplan anhand der Sitzplatzvorlage des übergebenen Flugzeugs
     * initialisiert.
     * <p>
     * Der Basispreis darf nicht negativ sein und die Ankunftszeit darf zeitlich nicht vor der Abflugzeit liegen.
     *
     * @param flugnummer       die Flugnummer des Fluges
     * @param fluggesellschaft die avigator.modell.Fluggesellschaft, die den avigator.modell.Flug durchführt
     * @param flugzeug         das für den avigator.modell.Flug eingesetzte avigator.modell.Flugzeug
     * @param startFlughafen   der Startflughafen des Fluges
     * @param zielFlughafen    der Zielflughafen des Fluges
     * @param abflugzeit       der geplante Zeitpunkt des Abflugs
     * @param ankunftszeit     der geplante Zeitpunkt der Ankunft
     * @param basispreis       der Basispreis des Fluges
     * @throws IllegalArgumentException wenn der Basispreis negativ ist
     * @throws IllegalArgumentException wenn die Ankunftszeit vor der Abflugzeit liegt
     */
    public Flug(String flugnummer,
                Fluggesellschaft fluggesellschaft,
                Flugzeug flugzeug,
                Flughafen startFlughafen,
                Flughafen zielFlughafen,
                LocalDateTime abflugzeit,
                LocalDateTime ankunftszeit,
                double basispreis
    ) {

        if (fluggesellschaft == null
            || flugzeug == null
            || startFlughafen == null
            || zielFlughafen == null
            || abflugzeit == null
            || ankunftszeit == null) {

            throw new IllegalArgumentException("Die übergebenen Flugdaten dürfen nicht null sein.");
        }

        if (flugnummer == null || flugnummer.isBlank()) {
            throw new IllegalArgumentException("Die Flugnummer darf nicht leer sein.");
        }

        // Prüft, ob der angegebene Basispreis gültig ist
        if (basispreis < 0) {
            throw new IllegalArgumentException("Der Basispreis darf nicht negativ sein.");
        }

        // Verhindert, dass ein avigator.modell.Flug vor seinem Abflug ankommt
        if (!ankunftszeit.isAfter(abflugzeit)) {
            throw new IllegalArgumentException("Die Ankunftszeit darf nicht vor der Abflugzeit liegen.");
        }

        // entfernt Leerzeichen in der Flugnummer, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.flugnummer = flugnummer.trim().toUpperCase(Locale.ROOT);
        this.fluggesellschaft = fluggesellschaft;
        this.flugzeug = flugzeug;
        this.startFlughafen = startFlughafen;
        this.zielFlughafen = zielFlughafen;
        this.abflugzeit = abflugzeit;
        this.ankunftszeit = ankunftszeit;
        this.basispreis = basispreis;

        // Erstellt einen eigenen Sitzplan für diesen konkreten avigator.modell.Flug
        this.initialisiereSitzplan(this.flugzeug.getSitzplaetzeVorlage());
    }

    /**
     * Initialisiert den Sitzplan des Fluges anhand einer Sitzplatzvorlage.
     * <p>
     * Für jeden avigator.modell.Sitzplatz der Vorlage wird ein neues {@code avigator.modell.Sitzplatz}-Objekt erzeugt. Dadurch besitzt jeder avigator.modell.Flug einen
     * eigenen unabhängigen Sitzplan und Änderungen an der Sitzplatzbelegung wirken sich nicht auf andere Flüge oder auf
     * die ursprüngliche Sitzplatzvorlage des Flugzeugs aus.
     *
     * @param vorlage die Sitzplatzvorlage des eingesetzten Flugzeugs
     */
    private void initialisiereSitzplan(Sitzplatz[][] vorlage) {

        // Erstellt zunächst die äußere Arraystruktur entsprechend der Vorlage
        sitzplan = new Sitzplatz[vorlage.length][];

        for (int i = 0; i < vorlage.length; i++) {

            // Jede Reihe erhält die gleiche Anzahl an Sitzplätzen wie in der Vorlage
            sitzplan[i] = new Sitzplatz[vorlage[i].length];

            for (int j = 0; j < vorlage[i].length; j++) {

                Sitzplatz original = vorlage[i][j];

                // Erstellt eine unabhängige Kopie des jeweiligen Sitzplatzes
                sitzplan[i][j] = new Sitzplatz(original.getSitzplatzNummer(), original.getSitzklasse());
            }
        }
    }

    /**
     * Ermittelt alle freien Sitzplätze einer bestimmten avigator.modell.Sitzklasse.
     * <p>
     * Ein avigator.modell.Sitzplatz wird nur zurückgegeben, wenn er sowohl frei ist als auch der angegebenen avigator.modell.Sitzklasse entspricht.
     *
     * @param sitzklasse die avigator.modell.Sitzklasse, nach der gefiltert werden soll
     * @return eine Liste mit allen freien Sitzplätzen der angegebenen avigator.modell.Sitzklasse
     */
    public List<Sitzplatz> getFreieSitzplaetzeNachKlasse(Sitzklasse sitzklasse) {

        List<Sitzplatz> freieSitzplaetze = new ArrayList<>();

        // Durchläuft den vollständigen Sitzplan
        for (Sitzplatz[] sitzplaetze : sitzplan) {

            for (Sitzplatz sitzplatz : sitzplaetze) {

                // Prüft gleichzeitig den Belegungsstatus und die avigator.modell.Sitzklasse
                if (sitzplatz.getIstFrei() && sitzplatz.getSitzklasse() == sitzklasse) {
                    freieSitzplaetze.add(sitzplatz);
                }
            }
        }

        return freieSitzplaetze;
    }

    /**
     * Berechnet die prozentuale Auslastung des Fluges.
     * <p>
     * Dazu wird die Anzahl aller belegten Sitzplätze durch die Gesamtzahl aller vorhandenen Sitzplätze geteilt und
     * anschließend mit {@code 100} multipliziert.
     *
     * @return die Auslastung des Fluges in Prozent
     */
    public double berechneAuslastung() {

        int anzahlBelegt = 0;
        int anzahlGesamt = 0;

        // Durchläuft alle Sitzplätze und zählt Gesamtanzahl und belegte Plätze
        for (Sitzplatz[] sitzplaetze : this.sitzplan) {

            for (Sitzplatz sitzplatz : sitzplaetze) {

                anzahlGesamt++;

                if (!sitzplatz.getIstFrei()) {
                    anzahlBelegt++;
                }
            }
        }

        // Rückgabe rundet auf 2 Nachkommastellen und gibt Wert in Prozent aus
        return Math.round(((double) anzahlBelegt / anzahlGesamt * 100.0) * 100.0) / 100.0;
    }

    /**
     * Gibt den aktuellen Sitzplan des Fluges auf der Konsole aus.
     * <p>
     * Für jeden avigator.modell.Sitzplatz wird zunächst die Sitzplatznummer und anschließend der aktuelle Belegungsstatus dargestellt.
     * <p>
     * Ein freier avigator.modell.Sitzplatz wird durch {@code [ ]} und ein belegter avigator.modell.Sitzplatz durch {@code [X]} gekennzeichnet.
     * <p>
     * In der Mitte jeder Reihe wird ein Mittelgang dargestellt. Zusätzlich wird beim Wechsel zwischen verschiedenen
     * Sitzklassen eine horizontale Trennlinie ausgegeben.
     */
    public void zeigeSitzplan() {

        String ersteKlasse = " " + this.sitzplan[0][0].getSitzklasse() + " ";

        int breite = this.sitzplan[0].length * 5 + 1;
        berechneStrichAnzahlUndGebeStricheAus(ersteKlasse, breite);

        for (int i = 0; i < this.sitzplan.length; i++) {

            if (i > 0 && this.sitzplan[i - 1][0].getSitzklasse()
                         != this.sitzplan[i][0].getSitzklasse()) {

                String text = " " + this.sitzplan[i][0].getSitzklasse() + " ";

                int breiteKlasse = this.sitzplan[i].length * 5 + 1;
                berechneStrichAnzahlUndGebeStricheAus(text, breiteKlasse);
            }

            // Gibt zunächst die Sitzplatznummern der aktuellen Reihe aus
            for (int j = 0; j < this.sitzplan[i].length; j++) {

                // Fügt in der Mitte der Sitzreihe einen Gang ein
                if (j == this.sitzplan[i].length / 2) {
                    System.out.print("|  ");
                }

                System.out.printf("%-5s", this.sitzplan[i][j].getSitzplatzNummer());
            }

            System.out.println();

            // Gibt anschließend den Belegungsstatus der Sitzplätze aus
            for (int j = 0; j < this.sitzplan[i].length; j++) {

                // Fügt auch in der Statusanzeige den Mittelgang ein
                if (j == this.sitzplan[i].length / 2) {
                    System.out.print("|  ");
                }

                if (this.sitzplan[i][j].getIstFrei()) {
                    System.out.printf("%-5s", "[ ]");
                } else {
                    System.out.printf("%-5s", "[X]");
                }
            }

            System.out.println();
            System.out.println();
        }
    }

    private void berechneStrichAnzahlUndGebeStricheAus(String anzahlErsteKlasseSitzplaetze, int breite) {

        int anzahlStriche = Math.max(0, breite - anzahlErsteKlasseSitzplaetze.length());

        int links = anzahlStriche / 2;
        int rechts = anzahlStriche - links;

        System.out.println("-".repeat(links) + anzahlErsteKlasseSitzplaetze + "-".repeat(rechts));
        System.out.println();
    }

    /**
     * Findet einen avigator.modell.Sitzplatz in einem avigator.modell.Flug und gibt ihn zurück War ursprünglich in der Methode "avigator.verwaltung.Buchungssystem", ist
     * aber hier sinnvoller
     *
     * @param sitzplatznummer blabla wird nochmal geändert javadoc
     * @return blabla wird nochmal geändert javadoc
     */
    public Sitzplatz findeSitzplatz(String sitzplatznummer) {

        for (Sitzplatz[] sitzplaetze : sitzplan) {

            for (Sitzplatz sitzplatz : sitzplaetze) {

                if (sitzplatz.getSitzplatzNummer()
                        .equals(sitzplatznummer)) {

                    return sitzplatz;
                }
            }
        }

        return null;
    }

    /**
     * Prüft, ob ein avigator.modell.Sitzplatz vorhanden oder belegt ist oder nicht in der gegebenen avigator.modell.Sitzklasse existiert
     *
     * @param sitz         : zu überprüfender avigator.modell.Sitzplatz
     * @param sitzklasse   : avigator.modell.Sitzklasse, die zu dem avigator.modell.Sitzplatz gehören soll
     * @param klassenliste : Liste mit Sitzplätzen, die die angegebene avigator.modell.Sitzklasse haben
     */
    public void validiereSitzplatz(Sitzplatz sitz, Sitzklasse sitzklasse, List<Sitzplatz> klassenliste) {

        //man muss auf null prüfen, weil die Methode "findeSitzplatz" null zurückgeben kann.
        if (sitz == null) {
            throw new NoSuchElementException("avigator.modell.Sitzplatz nicht vorhanden.");
        } else if (! sitz.getIstFrei()) {
            throw new IllegalArgumentException("avigator.modell.Sitzplatz bereits belegt.");
        } else if (sitz.getSitzklasse() != sitzklasse && ! klassenliste.contains(sitz)) {
            throw new IllegalArgumentException("Der avigator.modell.Sitzplatz ist nicht in der richtigen avigator.modell.Sitzklasse");
        }
    }

    /**
     * Gibt die Flugnummer des Fluges zurück.
     *
     * @return die Flugnummer
     */
    public String getFlugnummer() {

        return flugnummer;
    }

    /**
     * Gibt den Basispreis des Fluges zurück.
     *
     * @return der Basispreis des Fluges
     */
    public double getBasispreis() {

        return basispreis;
    }

    public Flughafen getStartFlughafen() {

        return startFlughafen;
    }

    public Flughafen getZielflughafen() {

        return zielFlughafen;
    }

    public LocalDateTime getAbflugszeit() {

        return abflugzeit;
    }

    public LocalDateTime getAnkunftszeit() {

        return ankunftszeit;
    }

    public Flugzeug getFlugzeug() {

        return flugzeug;
    }

    public Fluggesellschaft getFluggesellschaft() {

        return fluggesellschaft;
    }

    /**
     * Gibt eine textuelle Beschreibung des Fluges zurück.
     * <p>
     * Die Beschreibung enthält die avigator.modell.Fluggesellschaft, die vollständige Flugnummer, den Start- und Zielflughafen, die
     * Abflug- und Ankunftszeit, das eingesetzte avigator.modell.Flugzeug sowie die aktuelle Auslastung.
     *
     * @return eine textuelle Beschreibung des Fluges
     */
    @Override
    public String toString() {

        return this.fluggesellschaft.getName()
               + " avigator.modell.Flug " + this.flugnummer
               + " von " + this.startFlughafen.iataCode()
               + " nach " + this.zielFlughafen.iataCode()
               + ", Abflug: " + this.abflugzeit
               + ", Ankunft: " + this.ankunftszeit
               + ", avigator.modell.Flugzeug: " + this.flugzeug.getModell()
               + " (" + this.flugzeug.getCode() + ")"
               + " ist zu " + this.berechneAuslastung()
               + "% ausgelastet.";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flug f = (Flug) o;

        return this.flugnummer.equalsIgnoreCase(f.getFlugnummer())
               && this.abflugzeit.toLocalDate().equals(f.getAbflugszeit().toLocalDate())
               && this.startFlughafen.equals(f.startFlughafen)
               && this.zielFlughafen.equals(f.zielFlughafen);
    }
}
