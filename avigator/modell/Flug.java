package avigator.modell;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code Flug} repräsentiert einen konkreten Flug einer Fluggesellschaft zwischen einem Start- und einem
 * Zielflughafen.
 * <p>
 * Ein Flug besitzt eine Flugnummer, eine Fluggesellschaft, ein eingesetztes Flugzeug, einen Start- und Zielflughafen,
 * eine Abflug- und Ankunftszeit sowie einen Basispreis.
 * <p>
 * Zusätzlich besitzt jeder Flug einen eigenen Sitzplan. Dieser wird bei der Erstellung des Fluges anhand der
 * Sitzplatzvorlage des eingesetzten Flugzeugs initialisiert. Dadurch kann die Sitzplatzbelegung für jeden Flug
 * unabhängig verwaltet werden.
 *
 * @author Cedric Beckmann
 * @version 1.1
 */
public class Flug implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Die Flugnummer des Fluges.
     */
    private final String flugnummer;

    /**
     * Die Fluggesellschaft, die den Flug durchführt.
     */
    private final Fluggesellschaft fluggesellschaft;

    /**
     * Das für den Flug eingesetzte Flugzeug.
     */
    private final Flugzeug flugzeug;

    /**
     * Der Flughafen, von dem der Flug startet.
     */
    private final Flughafen startFlughafen;

    /**
     * Der Flughafen, an dem der Flug endet.
     */
    private final Flughafen zielFlughafen;

    /**
     * Der geplante Zeitpunkt des Abflugs.
     */
    private final LocalDateTime abflugzeit;

    /**
     * Der geplante Zeitpunkt der Ankunft.
     */
    private final LocalDateTime ankunftszeit;

    /**
     * Der Basispreis des Fluges.
     */
    private final double basispreis;

    /**
     * Der individuelle Sitzplan dieses Fluges. Jeder Sitzplatz besitzt einen eigenen Belegungsstatus.
     */
    private Sitzplatz[][] sitzplan;

    /**
     * Erzeugt einen neuen Flug mit den angegebenen Flugdaten.
     * <p>
     * Beim Erstellen des Fluges wird ein eigener Sitzplan anhand der Sitzplatzvorlage des übergebenen Flugzeugs
     * initialisiert.
     * <p>
     * Der Basispreis darf nicht negativ sein und die Ankunftszeit muss nach der Abflugzeit liegen.
     *
     * @param flugnummer       die Flugnummer des Fluges
     * @param fluggesellschaft die Fluggesellschaft, die den Flug durchführt
     * @param flugzeug         das für den Flug eingesetzte Flugzeug
     * @param startFlughafen   der Startflughafen des Fluges
     * @param zielFlughafen    der Zielflughafen des Fluges
     * @param abflugzeit       der geplante Zeitpunkt des Abflugs
     * @param ankunftszeit     der geplante Zeitpunkt der Ankunft
     * @param basispreis       der Basispreis des Fluges
     * @throws IllegalArgumentException wenn erforderliche Flugdaten {@code null} oder die Flugnummer leer ist, der
     *                                  Basispreis negativ ist oder die Ankunftszeit nicht nach der Abflugzeit liegt
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

        if (basispreis < 0) {
            throw new IllegalArgumentException("Der Basispreis darf nicht negativ sein.");
        }

        // Verhindert, dass ein Flug vor oder zu seinem Abflugzeitpunkt ankommt
        if (!ankunftszeit.isAfter(abflugzeit)) {
            throw new IllegalArgumentException("Die Ankunftszeit darf nicht vor der Abflugzeit liegen.");
        }

        // entfernt führende und nachfolgende Leerzeichen in der Flugnummer, wandelt Klein- in Großbuchstaben um und
        // behandelt Eingaben unabhängig von der Spracheinstellung des Computers
        this.flugnummer = flugnummer.trim().toUpperCase(Locale.ROOT);
        this.fluggesellschaft = fluggesellschaft;
        this.flugzeug = flugzeug;
        this.startFlughafen = startFlughafen;
        this.zielFlughafen = zielFlughafen;
        this.abflugzeit = abflugzeit;
        this.ankunftszeit = ankunftszeit;
        this.basispreis = basispreis;

        this.initialisiereSitzplan(this.flugzeug.getSitzplaetzeVorlage());
    }

    /**
     * Initialisiert den Sitzplan des Fluges anhand einer Sitzplatzvorlage.
     * <p>
     * Für jeden Sitzplatz der Vorlage wird ein neues {@code Sitzplatz}-Objekt erzeugt. Dadurch besitzt jeder Flug einen
     * eigenen unabhängigen Sitzplan und Änderungen an der Sitzplatzbelegung wirken sich nicht auf andere Flüge oder auf
     * die ursprüngliche Sitzplatzvorlage des Flugzeugs aus.
     *
     * @param vorlage die Sitzplatzvorlage des eingesetzten Flugzeugs
     */
    private void initialisiereSitzplan(Sitzplatz[][] vorlage) {

        sitzplan = new Sitzplatz[vorlage.length][];

        for (int i = 0; i < vorlage.length; i++) {

            sitzplan[i] = new Sitzplatz[vorlage[i].length];

            for (int j = 0; j < vorlage[i].length; j++) {

                Sitzplatz original = vorlage[i][j];

                // Erstellt eine unabhängige Kopie des jeweiligen Sitzplatzes
                sitzplan[i][j] = new Sitzplatz(original.getSitzplatzNummer(), original.getSitzklasse());
            }
        }
    }

    /**
     * Ermittelt alle freien Sitzplätze einer bestimmten Sitzklasse.
     * <p>
     * Ein Sitzplatz wird nur zurückgegeben, wenn er sowohl frei ist als auch der angegebenen Sitzklasse entspricht.
     *
     * @param sitzklasse die Sitzklasse, nach der gefiltert werden soll
     * @return eine Liste mit allen freien Sitzplätzen der angegebenen Sitzklasse
     */
    public List<Sitzplatz> getFreieSitzplaetzeNachKlasse(Sitzklasse sitzklasse) {

        List<Sitzplatz> freieSitzplaetze = new ArrayList<>();

        for (Sitzplatz[] sitzplaetze : sitzplan) {

            for (Sitzplatz sitzplatz : sitzplaetze) {

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
     * Für jeden Sitzplatz wird zunächst die Sitzplatznummer und anschließend der aktuelle Belegungsstatus dargestellt.
     * <p>
     * Ein freier Sitzplatz wird durch {@code [ ]} und ein belegter Sitzplatz durch {@code [X]} gekennzeichnet.
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

    /**
     * Gibt einen Text innerhalb einer Trennlinie zentriert aus.
     *
     * @param anzahlErsteKlasseSitzplaetze der innerhalb der Trennlinie auszugebende Text
     * @param breite                       die Gesamtbreite der Trennlinie
     */
    private void berechneStrichAnzahlUndGebeStricheAus(String anzahlErsteKlasseSitzplaetze, int breite) {

        int anzahlStriche = Math.max(0, breite - anzahlErsteKlasseSitzplaetze.length());

        int links = anzahlStriche / 2;
        int rechts = anzahlStriche - links;

        System.out.println("-".repeat(links) + anzahlErsteKlasseSitzplaetze + "-".repeat(rechts));
        System.out.println();
    }

    /**
     * Sucht einen Sitzplatz anhand seiner Sitzplatznummer im Sitzplan des Fluges.
     *
     * @param sitzplatznummer die Nummer des gesuchten Sitzplatzes
     * @return der gefundene Sitzplatz oder {@code null}, wenn kein Sitzplatz mit dieser Nummer existiert
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
     * Prüft, ob ein Sitzplatz vorhanden und frei ist sowie der gewünschten Sitzklasse angehört oder in der
     * übergebenen Klassenliste enthalten ist.
     *
     * @param sitz         der zu überprüfende Sitzplatz
     * @param sitzklasse   die gewünschte Sitzklasse
     * @param klassenliste die freien Sitzplätze der gewünschten Sitzklasse
     * @throws NoSuchElementException   wenn der Sitzplatz nicht vorhanden ist
     * @throws IllegalArgumentException wenn der Sitzplatz bereits belegt ist oder sowohl einer anderen Sitzklasse
     *                                  angehört als auch nicht in der Klassenliste enthalten ist
     */
    public void validiereSitzplatz(Sitzplatz sitz, Sitzklasse sitzklasse, List<Sitzplatz> klassenliste) {

        // Eine null-Referenz kennzeichnet einen nicht im Sitzplan vorhandenen Sitzplatz
        if (sitz == null) {
            throw new NoSuchElementException("Sitzplatz nicht vorhanden.");
        } else if (! sitz.getIstFrei()) {
            throw new IllegalArgumentException("Sitzplatz bereits belegt.");
        } else if (sitz.getSitzklasse() != sitzklasse && ! klassenliste.contains(sitz)) {
            throw new IllegalArgumentException("Der Sitzplatz ist nicht in der richtigen Sitzklasse");
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

    /**
     * Gibt den Startflughafen des Fluges zurück.
     *
     * @return der Startflughafen
     */
    public Flughafen getStartFlughafen() {

        return startFlughafen;
    }

    /**
     * Gibt den Zielflughafen des Fluges zurück.
     *
     * @return der Zielflughafen
     */
    public Flughafen getZielflughafen() {

        return zielFlughafen;
    }

    /**
     * Gibt die Abflugzeit des Fluges zurück.
     *
     * @return die Abflugzeit
     */
    public LocalDateTime getAbflugszeit() {

        return abflugzeit;
    }

    /**
     * Gibt die Ankunftszeit des Fluges zurück.
     *
     * @return die Ankunftszeit
     */
    public LocalDateTime getAnkunftszeit() {

        return ankunftszeit;
    }

    /**
     * Gibt das für den Flug eingesetzte Flugzeug zurück.
     *
     * @return das eingesetzte Flugzeug
     */
    public Flugzeug getFlugzeug() {

        return flugzeug;
    }

    /**
     * Gibt die den Flug durchführende Fluggesellschaft zurück.
     *
     * @return die durchführende Fluggesellschaft
     */
    public Fluggesellschaft getFluggesellschaft() {

        return fluggesellschaft;
    }

    /**
     * Gibt eine textuelle Beschreibung des Fluges zurück.
     * <p>
     * Die Beschreibung enthält die Fluggesellschaft, die vollständige Flugnummer, den Start- und Zielflughafen, die
     * Abflug- und Ankunftszeit, das eingesetzte Flugzeug sowie die aktuelle Auslastung.
     *
     * @return eine textuelle Beschreibung des Fluges
     */
    @Override
    public String toString() {

        return this.fluggesellschaft.getName()
               + " Flug " + this.flugnummer
               + " von " + this.startFlughafen.iataCode()
               + " nach " + this.zielFlughafen.iataCode()
               + ", Abflug: " + this.abflugzeit
               + ", Ankunft: " + this.ankunftszeit
               + ", Flugzeug: " + this.flugzeug.getModell()
               + " (" + this.flugzeug.getCode() + ")"
               + " ist zu " + this.berechneAuslastung()
               + "% ausgelastet.";
    }

    /**
     * Vergleicht zwei Flüge anhand ihrer Flugnummer, ihres Abflugdatums und ihrer Route.
     *
     * @param o das zu vergleichende Objekt
     * @return {@code true}, wenn beide Flüge dieselben Identifikationsmerkmale besitzen, sonst {@code false}
     */
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
