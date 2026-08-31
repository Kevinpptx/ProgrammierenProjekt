package avigator.verwaltung;

import avigator.modell.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Zentrale Verwaltungsklasse des Flugbuchungssystems.
 * <p>
 * Das Verwaltungssystem verwaltet Fluggesellschaften, Flugzeuge, Flughaefen und Fluege. Es stellt Methoden zum
 * Hinzufuegen, Entfernen, Erzeugen und Suchen dieser Objekte bereit. Die gespeicherten Daten koennen durch die
 * Implementierung von {@link Serializable} serialisiert werden.
 *
 * @author Cedric Beckmann
 * @version 1.1
 */
public class Verwaltungssystem implements Serializable {

    /**
     * Versionsnummer zur Pruefung der Kompatibilitaet bei der Serialisierung.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Enthaelt alle im Verwaltungssystem registrierten Fluggesellschaften.
     */
    private final ArrayList<Fluggesellschaft> fluggesellschaften = new ArrayList<>();

    /**
     * Enthaelt alle im Verwaltungssystem registrierten Fluege.
     */
    private final ArrayList<Flug> fluege = new ArrayList<>();

    /**
     * Enthaelt alle im Verwaltungssystem registrierten Flugzeuge.
     */
    private final ArrayList<Flugzeug> flugzeuge = new ArrayList<>();

    /**
     * Enthaelt alle im Verwaltungssystem registrierten Flughaefen.
     */
    private final ArrayList<Flughafen> flughaefen = new ArrayList<>();

    /**
     * Erstellt ein neues, zunaechst leeres Verwaltungssystem.
     */
    public Verwaltungssystem() {

    }

    // Verwaltung Fluggesellschaften

    /**
     * Fuegt eine Fluggesellschaft dem Verwaltungssystem hinzu.
     *
     * @param fluggesellschaft die hinzuzufuegende Fluggesellschaft
     * @return die hinzugefuegte Fluggesellschaft
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null} ist oder bereits registriert wurde
     */
    public Fluggesellschaft fuegeFluggesellschaftHinzu(Fluggesellschaft fluggesellschaft) {

        if (fluggesellschaft == null) {

            throw new IllegalArgumentException("Das uebergebene Fluggesellschaft-Objekt hat eine Nullreferenz");

        } else if (this.fluggesellschaften.contains(fluggesellschaft)) {

            throw new IllegalArgumentException(
                    "Das uebergebene Fluggesellschaften-Objekt ist schon in der Liste enthalten");
        } else {

            this.fluggesellschaften.add(fluggesellschaft);
            return fluggesellschaft;
        }
    }

    /**
     * Entfernt eine Fluggesellschaft aus dem Verwaltungssystem. Eine Fluggesellschaft kann nur entfernt werden, wenn
     * ihr keine registrierten Fluege mehr zugeordnet sind.
     *
     * @param fluggesellschaft die zu entfernende Fluggesellschaft
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null} oder nicht registriert ist, noch
     *                                  Flugzeuge enthaelt oder noch einem Flug zugeordnet ist
     */
    public void entferneFluggesellschaft(Fluggesellschaft fluggesellschaft) {

        if (fluggesellschaft == null) {

            throw new IllegalArgumentException("Das uebergebene Fluggesellschaft-Objekt hat eine Nullreferenz");

        } else if (!this.fluggesellschaften.contains(fluggesellschaft)) {

            throw new IllegalArgumentException(
                    "Das uebergebene Fluggesellschaften-Objekt wurde bisher noch nicht hinzugefuegt.");

        } else if (!fluggesellschaft.getFlotte().isEmpty()) {

            // Fluggesellschaften mit zugeordneten Flugzeugen duerfen nicht geloescht werden
            throw new IllegalArgumentException("Der Fluggesellschaft sind noch Flugzeuge zugewiesen.");

        } else {

            for (Flug flug : fluege) {

                if (flug.getFluggesellschaft().equals(fluggesellschaft)) {

                    throw new IllegalArgumentException(
                            "Die Fluggesellschaft konnte nicht entfernt werden, da noch aktuelle Fluege geplant sind.");
                }
            }

            this.fluggesellschaften.remove(fluggesellschaft);
        }
    }

    /**
     * Gibt alle registrierten Fluggesellschaften zurueck.
     *
     * @return unveraenderbare Kopie der registrierten Fluggesellschaften
     */
    public List<Fluggesellschaft> getFluggesellschaften() {

        return List.copyOf(this.fluggesellschaften);
    }

    /**
     * Sucht eine Fluggesellschaft anhand ihres Airline-Codes.
     *
     * @param iataCode der Airline-Code der gesuchten Fluggesellschaft
     * @return die Fluggesellschaft mit dem angegebenen Code
     * @throws IllegalArgumentException wenn keine Fluggesellschaft mit dem angegebenen Code existiert
     */
    public Fluggesellschaft getFluggesellschaft(String iataCode) {

        String code = iataCode.strip().toUpperCase(Locale.ROOT);

        for (Fluggesellschaft fluggesellschaft : this.fluggesellschaften) {

            if (fluggesellschaft.getAirlineCode().equals(code)) {
                return fluggesellschaft;
            }
        }

        throw new IllegalArgumentException("Eine Fluggesellschaft mit dem Code " + code + " existiert nicht.");
    }

    /**
     * Erzeugt ein neues Flugzeug und ordnet es einer registrierten Fluggesellschaft zu.
     * <p>
     * Das Flugzeug wird sowohl in der Flotte der Fluggesellschaft als auch zentral im Verwaltungssystem gespeichert.
     *
     * @param fluggesellschaft die Fluggesellschaft, der das Flugzeug gehoert
     * @param code             der eindeutige Code des Flugzeugs
     * @param modell           die Modellbezeichnung des Flugzeugs
     * @param anzahlReihen     die Gesamtzahl der Sitzreihen
     * @param sitzeProReihe    die Anzahl der Sitzplaetze pro Reihe
     * @param businessReihen   die Anzahl der Business-Class-Reihen
     * @return das neu erzeugte Flugzeug
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null} oder nicht registriert ist, die
     *                                  Flugzeugdaten ungueltig sind oder bereits ein Flugzeug mit demselben Code
     *                                  existiert
     */
    public Flugzeug erzeugeFlugzeug(Fluggesellschaft fluggesellschaft,
                                    String code,
                                    String modell,
                                    int anzahlReihen,
                                    int sitzeProReihe,
                                    int businessReihen
    ) {

        String flugzeugCode = code.strip().toUpperCase(Locale.ROOT);

        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Die uebergebene Fluggesellschaft existiert nicht.");
        }

        if (!fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Die Fluggesellschaft ist nicht im Verwaltungssystem registriert.");
        }

        for (Flugzeug flugzeug : this.flugzeuge) {

            if (flugzeug.getCode().equalsIgnoreCase(flugzeugCode)) {
                throw new IllegalArgumentException("Ein Flugzeug mit dem Code " + flugzeugCode + " existiert bereits.");
            }
        }

        // Registriert das Flugzeug sowohl zentral als auch in der Flotte seiner Fluggesellschaft.
        Flugzeug flugzeug = new Flugzeug(flugzeugCode, modell, anzahlReihen, sitzeProReihe, businessReihen);

        fluggesellschaft.fuegeFlugzeugHinzu(flugzeug);
        this.flugzeuge.add(flugzeug);

        return flugzeug;
    }

    /**
     * Entfernt ein Flugzeug aus dem Verwaltungssystem und aus der Flotte der zugehoerigen Fluggesellschaft.
     * <p>
     * Ein Flugzeug kann nur entfernt werden, wenn es fuer keinen vorhandenen Flug mehr eingeplant ist.
     *
     * @param code der Code des zu entfernenden Flugzeugs
     * @throws IllegalArgumentException wenn der Code {@code null} ist oder kein Flugzeug mit diesem Code existiert
     * @throws IllegalStateException    wenn das Flugzeug noch fuer einen Flug eingeplant ist oder keiner
     *                                  Fluggesellschaft zugeordnet werden kann
     */
    public void entferneFlugzeug(String code) {

        if (code == null) {
            throw new IllegalArgumentException("Der Flugzeug-Code darf nicht leer sein.");
        }

        Flugzeug flug = this.getFlugzeug(code.strip().toUpperCase(Locale.ROOT));

        // Entfernt nur Flugzeuge, die aktuell keinem geplanten Flug zugeordnet sind.
        for (Flug flugTemporaer : this.fluege) {

            if (flugTemporaer.getFlugzeug().equals(flug)) {

                throw new IllegalStateException(
                        "Das FLugzeug kann nicht entfernt werden, da noch Fluege damit geplant sind.");
            }
        }

        // Haelt beim Entfernen die zentrale Flugzeugliste und die zugehoerige Flotte synchron.
        for (Fluggesellschaft fluggesellschaft : this.fluggesellschaften) {

            if (fluggesellschaft.beinhaltetFlugzeug(flug)) {

                fluggesellschaft.entferneFlugzeug(flug);
                this.flugzeuge.remove(flug);

                return;
            }
        }

        throw new IllegalStateException("Das Flugzeug konnte keiner Fluggesellschaft zugeordnet werden");
    }

    /**
     * Sucht ein Flugzeug anhand seines Codes.
     *
     * @param code der Code des gesuchten Flugzeugs
     * @return das Flugzeug mit dem angegebenen Code
     * @throws IllegalArgumentException wenn kein Flugzeug mit dem angegebenen Code existiert
     */
    public Flugzeug getFlugzeug(String code) {

        Iterator<Flugzeug> iterator = this.flugzeuge.iterator();

        String flugzeugCode = code.strip().toUpperCase(Locale.ROOT);

        while (iterator.hasNext()) {

            Flugzeug flugzeug = iterator.next();

            if (flugzeug.getCode().equals(flugzeugCode)) {
                return flugzeug;
            }
        }

        throw new IllegalArgumentException("Ein Flugzeug mit dem Code " + flugzeugCode + " existiert nicht.");
    }

    // Verwaltung Flughafen

    /**
     * Erzeugt einen neuen Flughafen und registriert ihn im Verwaltungssystem. Name und IATA-Code muessen innerhalb des
     * Verwaltungssystems eindeutig sein.
     *
     * @param name     der Name des Flughafens
     * @param iataCode der eindeutige IATA-Code des Flughafens
     * @param stadt    die Stadt, in der sich der Flughafen befindet
     * @param land     das Land, in dem sich der Flughafen befindet
     * @return der neu erzeugte Flughafen
     * @throws IllegalArgumentException wenn die Flughafendaten ungueltig sind oder bereits ein Flughafen mit demselben
     *                                  Namen oder IATA-Code existiert
     */
    public Flughafen erzeugeFlughafen(String name, String iataCode, String stadt, String land) {

        String code = iataCode.strip().toUpperCase(Locale.ROOT);

        for (Flughafen flughafen : this.flughaefen) {

            if (flughafen.iataCode().equals(code)) {
                throw new IllegalArgumentException("Ein Flughafen mit dem IATACode " + code + " existiert bereits.");
            } else if (flughafen.name().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Ein Flughafen mit dem Namen " + name + " existiert bereits.");
            }
        }

        Flughafen flughafen = new Flughafen(name, code, stadt, land);
        this.flughaefen.add(flughafen);

        return flughafen;
    }

    /**
     * Entfernt einen Flughafen aus dem Verwaltungssystem. Ein Flughafen kann nur entfernt werden, wenn er bei keinem
     * registrierten Flug als Start- oder Zielflughafen verwendet wird.
     *
     * @param flughafen der zu entfernende Flughafen
     * @throws IllegalArgumentException wenn der Flughafen {@code null} oder nicht registriert ist oder noch einem Flug
     *                                  zugeordnet ist
     */
    public void entferneFlughafen(Flughafen flughafen) {

        if (flughafen == null) {
            throw new IllegalArgumentException("Der Flughafen darf nicht null sein.");
        }

        if (this.flughaefen.contains(flughafen)) {

            // Verhindert verwaiste Start- oder Zielreferenzen in bereits geplanten Fluegen.
            for (Flug flug : fluege) {

                if (flug.getStartFlughafen().equals(flughafen) || flug.getZielflughafen().equals(flughafen)) {

                    throw new IllegalArgumentException(
                            "Der Flughafen konnte nicht entfernt werden, da hier noch aktuelle Fluege geplant sind.");
                }
            }

            this.flughaefen.remove(flughafen);

        } else {
            throw new IllegalArgumentException("Der Flughafen " + flughafen + " ist nicht aktiv.");
        }

    }

    /**
     * Sucht einen Flughafen anhand seines IATA-Codes.
     *
     * @param iataCode der IATA-Code des gesuchten Flughafens
     * @return der Flughafen mit dem angegebenen IATA-Code
     * @throws IllegalArgumentException wenn kein Flughafen mit dem angegebenen IATA-Code existiert
     */
    public Flughafen getFlughafenNachCode(String iataCode) {

        String code = iataCode.strip().toUpperCase(Locale.ROOT);

        for (Flughafen flughafen : this.flughaefen) {

            if (flughafen.iataCode().equals(code)) {
                return flughafen;
            }
        }

        throw new IllegalArgumentException("Der Flughafen mit dem IATACode " + code + " konnte nicht gefunden werden.");
    }

    /**
     * Gibt alle registrierten Flughaefen zurueck.
     *
     * @return unveraenderbare Kopie aller registrierten Flughaefen
     */
    public List<Flughafen> getFlughaefen() {

        return List.copyOf(this.flughaefen);
    }

    /**
     * Erzeugt und registriert einen oder mehrere Fluege im Verwaltungssystem.
     * <p>
     * Fuer jeden angegebenen Tag wird ein Hinflug erzeugt. Ist {@code rueckflug} gesetzt, wird zu jedem Hinflug
     * zusaetzlich ein Rueckflug erzeugt. Die Flugnummern werden automatisch anhand der Fluggesellschaft und des
     * jeweiligen Abflugtages vergeben.
     * <p>
     * Vor dem Speichern wird geprueft, ob die erzeugten Fluege gueltig sind und ob sich die Einsatzzeiten des verwendeten
     * Flugzeugs mit vorhandenen oder neu erzeugten Fluegen ueberschneiden.
     *
     * @param fluggesellschaft         die ausfuehrende Fluggesellschaft
     * @param flugzeug                 das fuer die Fluege eingesetzte Flugzeug
     * @param startFlughafen           der Startflughafen
     * @param zielFlughafen            der Zielflughafen
     * @param abflugzeit               das Datum und die Uhrzeit des ersten Abflugs
     * @param ankunftszeit             das Datum und die Uhrzeit der ersten Ankunft
     * @param basispreis               der Basispreis der Fluege
     * @param rueckflug                {@code true}, wenn zusaetzlich Rueckfluege erzeugt werden sollen
     * @param anzahlTageWiederholungen die Anzahl der aufeinanderfolgenden Tage, an denen der Flug stattfinden soll
     * @throws IllegalArgumentException wenn die uebergebenen Daten ungueltig sind, benoetigte Objekte nicht registriert
     *                                  sind, das Flugzeug nicht zur Fluggesellschaft gehoert, weniger als ein
     *                                  Wiederholungstag angegeben wurde oder sich Flugzeiten des verwendeten Flugzeugs
     *                                  ueberschneiden
     */
    public void fuegeFlugHinzu(Fluggesellschaft fluggesellschaft,
                               Flugzeug flugzeug,
                               Flughafen startFlughafen,
                               Flughafen zielFlughafen,
                               LocalDateTime abflugzeit,
                               LocalDateTime ankunftszeit,
                               double basispreis,
                               boolean rueckflug,
                               int anzahlTageWiederholungen
    ) {

        validiereFlug(fluggesellschaft, flugzeug, startFlughafen, zielFlughafen);

        if (anzahlTageWiederholungen < 1) {
            throw new IllegalArgumentException("Der Flug muss mindestens an einem Tag stattfinden.");
        }

        ArrayList<Flug> erzeugteFluege = new ArrayList<>();

        LocalDateTime datumZeitAbflug = abflugzeit;
        LocalDateTime datumZeitAnkunft = ankunftszeit;

        if (datumZeitAbflug == null || datumZeitAnkunft == null) {
            throw new IllegalArgumentException("Die Abflugs- bzw. Ankunftszeit darf nicht null sein.");
        }

        // Sammelt alle Fluege vor dem Speichern, damit auch ueberschneidungen innerhalb der Serie erkannt werden
        for (int i = 0; i < anzahlTageWiederholungen; i++) {

            String flugnummer = erzeugeFlugnummer(fluggesellschaft, datumZeitAbflug, erzeugteFluege);

            Flug hinflug = new Flug(flugnummer,
                    fluggesellschaft,
                    flugzeug,
                    startFlughafen,
                    zielFlughafen,
                    datumZeitAbflug,
                    datumZeitAnkunft,
                    basispreis
            );

            erzeugteFluege.add(hinflug);

            datumZeitAbflug = datumZeitAbflug.plusDays(1);
            datumZeitAnkunft = datumZeitAnkunft.plusDays(1);
        }

        // Erzeugt zu jedem Hinflug einen Rueckflug mit einer Stunde Umkehrzeit
        if (rueckflug) {

            List<Flug> rueckfluege = new ArrayList<>();
            Duration turnAroundTime = Duration.ofHours(1);

            for (Flug hinflug : erzeugteFluege) {

                Duration flugdauer = Duration.between(hinflug.getAbflugszeit(), hinflug.getAnkunftszeit());

                LocalDateTime abflugszeitRueckflug = hinflug.getAnkunftszeit().plus(turnAroundTime);
                LocalDateTime ankunftszeitRueckflug = abflugszeitRueckflug.plus(flugdauer);

                String flugnummer = erzeugeFlugnummer(fluggesellschaft, abflugszeitRueckflug, erzeugteFluege);
                Flug flugZurueck = new Flug(flugnummer,
                        fluggesellschaft,
                        flugzeug,
                        hinflug.getZielflughafen(),
                        hinflug.getStartFlughafen(),
                        abflugszeitRueckflug,
                        ankunftszeitRueckflug,
                        hinflug.getBasispreis()
                );

                rueckfluege.add(flugZurueck);
            }

            erzeugteFluege.addAll(rueckfluege);

        }

        try {

            // Speichert die Serie erst, nachdem jeder Flug gegen alle relevanten Flugzeiten geprueft wurde.
            for (Flug flug : erzeugteFluege) {
                pruefeFlugAufZeitlicheOderRegistrationsUeberschneidung(flug, erzeugteFluege);
            }

            fluege.addAll(erzeugteFluege);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Das Flugpaar konnte nicht erstellt werden: " + e.getMessage());
        }
    }

    /**
     * Erzeugt einen neuen Flug und registriert ihn im Verwaltungssystem.
     * <p>
     * Die Flugnummer wird automatisch aus dem Airline-Code und einer fortlaufenden Nummer fuer den jeweiligen Abflugtag
     * gebildet. Zusaetzlich wird geprueft, ob das Flugzeug im angegebenen Zeitraum bereits fuer einen anderen Flug
     * eingeplant ist.
     *
     * @param fluggesellschaft die ausfuehrende Fluggesellschaft
     * @param flugzeug         das fuer den Flug eingesetzte Flugzeug
     * @param startFlughafen   der Startflughafen
     * @param zielFlughafen    der Zielflughafen
     * @param abflugzeit       das Datum und die Uhrzeit des Abflugs
     * @param ankunftszeit     das Datum und die Uhrzeit der Ankunft
     * @param basispreis       der Basispreis des Flugs
     * @throws IllegalArgumentException wenn Fluggesellschaft, Flugzeug oder Flughaefen {@code null} beziehungsweise
     *                                  nicht registriert sind, das Flugzeug nicht zur Fluggesellschaft gehoert, der
     *                                  Basispreis negativ ist, die Ankunftszeit nicht nach der Abflugzeit liegt, der
     *                                  Flug bereits existiert oder sich die Einsatzzeiten des Flugzeugs ueberschneiden
     * @throws NullPointerException     wenn eine {@code null}-Referenz fuer die Abflug- oder Ankunftszeit bereits vor
     *                                  der Flugerzeugung ausgewertet wird
     */
    public void fuegeFlugHinzu(Fluggesellschaft fluggesellschaft,
                               Flugzeug flugzeug,
                               Flughafen startFlughafen,
                               Flughafen zielFlughafen,
                               LocalDateTime abflugzeit,
                               LocalDateTime ankunftszeit,
                               double basispreis
    ) {

        validiereFlug(fluggesellschaft, flugzeug, startFlughafen, zielFlughafen);

        String flugnummer = this.erzeugeFlugnummer(fluggesellschaft, abflugzeit);

        // Prueft vor der Erstellung sowohl die taegliche Flugnummer als auch die Einsatzzeit des Flugzeugs.
        for (Flug vorhandenerFlug : fluege) {

            boolean gleicherFlugAmSelbenTag
                    = vorhandenerFlug.getFlugnummer().equalsIgnoreCase(flugnummer)
                    && vorhandenerFlug.getAbflugszeit().toLocalDate().equals(abflugzeit.toLocalDate());

            if (gleicherFlugAmSelbenTag) {
                throw new IllegalArgumentException("Der Flug " + flugnummer + " existiert an diesem Tag bereits.");
            }

            boolean gleichesFlugzeug
                    = vorhandenerFlug.getFlugzeug().equals(flugzeug);

            if (gleichesFlugzeug) {

                // Eine ueberschneidung liegt vor, wenn der neue Flug vor dem Ende des vorhandenen beginnt und nach
                // dessen Beginn endet
                boolean zeitenUeberschneidenSich
                        = abflugzeit.isBefore(vorhandenerFlug.getAnkunftszeit())
                        && ankunftszeit.isAfter(vorhandenerFlug.getAbflugszeit());

                if (zeitenUeberschneidenSich) {
                    throw new IllegalArgumentException("Das Flugzeug ist in diesem Zeitraum bereits eingeplant.");
                }
            }
        }

        Flug flug = new Flug(flugnummer,
                fluggesellschaft,
                flugzeug,
                startFlughafen,
                zielFlughafen,
                abflugzeit,
                ankunftszeit,
                basispreis
        );

        if (this.fluege.contains(flug)) {
            throw new IllegalArgumentException("Das uebergebene Flug-Objekt ist schon in der Liste enthalten");
        } else {
            this.fluege.add(flug);
        }
    }

    /**
     * Validiert den Flug in Bezug darauf, ob Fluggesellschaften und Flughaefen registriert sind und das Flugzeug zur
     * Fluggesellschaft gehoert.
     *
     * @param fluggesellschaft die den Flug durchfuehren soll
     * @param flugzeug         mit dem der Flug durchgefuehrt werden soll
     * @param startFlughafen   von dem der Flug starten soll
     * @param zielFlughafen    zu dem der Flug fliegen soll
     * @throws IllegalArgumentException wenn die Fluggesellschaft oder die Flughaefen nicht registriert sind oder das
     *                                  Flugzeug nicht zur Fluggesellschaft gehoert beziehungsweise nicht verwaltet wird
     */
    private void validiereFlug(Fluggesellschaft fluggesellschaft,
                              Flugzeug flugzeug,
                              Flughafen startFlughafen,
                              Flughafen zielFlughafen
    ) {

        if (!fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Die Fluggesellschaft ist nicht im Verwaltungssystem registriert.");
        }

        if (!flughaefen.contains(startFlughafen) || !flughaefen.contains(zielFlughafen)) {
            throw new IllegalArgumentException("Start- und Zielflughafen muessen im Verwaltungssystem registriert sein.");
        }

        if (!fluggesellschaft.beinhaltetFlugzeug(flugzeug)) {
            throw new IllegalArgumentException("Das Flugzeug gehoert nicht zur angegbenen Fluggesellschaft");
        }

        if (!flugzeuge.contains(flugzeug)) {
            throw new IllegalArgumentException("Das Flugzeug wird nicht vom Verwaltungssystem verwaltet");
        }

    }

    /**
     * Prueft einen neu erzeugten Flug auf zeitliche ueberschneidungen mit bereits registrierten sowie weiteren neu
     * erzeugten Fluegen.
     *
     * @param neuerFlug  der zu pruefende Flug
     * @param neueFluege die gemeinsam neu erzeugten Fluege
     * @throws IllegalArgumentException wenn sich die Einsatzzeiten desselben Flugzeugs ueberschneiden
     */
    private void pruefeFlugAufZeitlicheOderRegistrationsUeberschneidung(Flug neuerFlug, List<Flug> neueFluege) {

        // Prueft zuerst gegen bereits registrierte Fluege
        for (Flug vorhandenerFlug : fluege) {
            pruefeFlugAufZeitlicheUeberschneidung(neuerFlug, vorhandenerFlug);
        }

        // Prueft danach gegen die gemeinsam erzeugten Serienfluege
        for (Flug vorhandenerNeuerFlug : neueFluege) {

            if (vorhandenerNeuerFlug == neuerFlug) {
                continue;
            }

            pruefeFlugAufZeitlicheUeberschneidung(neuerFlug, vorhandenerNeuerFlug);
        }
    }

    /**
     * Prueft zwei Fluege auf eine zeitliche ueberschneidung beim Einsatz desselben Flugzeugs.
     *
     * @param neuerFlug       der neu zu planende Flug
     * @param vorhandenerFlug der Vergleichsflug
     * @throws IllegalArgumentException wenn dasselbe Flugzeug in sich ueberschneidenden Zeitraeumen eingesetzt wird
     */
    private void pruefeFlugAufZeitlicheUeberschneidung(Flug neuerFlug, Flug vorhandenerFlug) {

        boolean gleichesFlugzeug = neuerFlug.getFlugzeug().equals(vorhandenerFlug.getFlugzeug());

        if (!gleichesFlugzeug) {
            return;
        }

        boolean zeitlicheUeberschneidung = neuerFlug.getAbflugszeit().isBefore(vorhandenerFlug.getAnkunftszeit()) &&
                                           neuerFlug.getAnkunftszeit().isAfter(vorhandenerFlug.getAbflugszeit());

        if (zeitlicheUeberschneidung) {
            throw new IllegalArgumentException("Das Flugzeug ist zu diesem Zeitpunkt bereits verplant");
        }
    }

    /**
     * Erzeugt fuer eine Fluggesellschaft und einen Abflugtag die naechste freie Flugnummer.
     * <p>
     * Die Flugnummer besteht aus dem großgeschriebenen Airline-Code und einer mindestens dreistelligen, pro Tag und
     * Fluggesellschaft fortlaufenden Nummer.
     *
     * @param fluggesellschaft die Fluggesellschaft des Flugs
     * @param abflugzeit       die Abflugzeit, deren Datum fuer die Nummerierung gilt
     * @return die erzeugte Flugnummer, beispielsweise {@code LH001}
     */
    private String erzeugeFlugnummer(Fluggesellschaft fluggesellschaft, LocalDateTime abflugzeit) {

        String airlineCode = fluggesellschaft.getAirlineCode();
        int hoechsteFlugnummer = berechneHoechsteFlugnummer(fluggesellschaft, abflugzeit, airlineCode);

        return airlineCode.toUpperCase() + String.format("%03d", hoechsteFlugnummer + 1);
    }

    /**
     * Erzeugt fuer eine Fluggesellschaft und einen Abflugtag die naechste freie Flugnummer.
     * <p>
     * Die Flugnummer besteht aus dem großgeschriebenen Airline-Code und einer mindestens dreistelligen, pro Tag und
     * Fluggesellschaft fortlaufenden Nummer.
     *
     * @param fluggesellschaft die Fluggesellschaft des Flugs
     * @param abflugzeit       die Abflugzeit, deren Datum fuer die Nummerierung gilt
     * @param neueFluege       die erzeugten, aber noch nicht gespeicherten Fluege; dies ist fuer die Erzeugung von
     *                         Serienfluegen noetig
     * @return die erzeugte Flugnummer, beispielsweise {@code LH001}
     */
    private String erzeugeFlugnummer(Fluggesellschaft fluggesellschaft,
                                     LocalDateTime abflugzeit,
                                     List<Flug> neueFluege
    ) {

        String airlineCode = fluggesellschaft.getAirlineCode();
        int hoechsteFlugnummer = berechneHoechsteFlugnummer(fluggesellschaft, abflugzeit, airlineCode);

        // Beruecksichtigt auch Fluege der aktuellen Serie, die noch nicht zentral gespeichert wurden.
        for (Flug neuerFlug : neueFluege) {

            boolean gleicherTag = neuerFlug.getAbflugszeit().toLocalDate().equals(abflugzeit.toLocalDate());

            if (!gleicherTag) {
                continue;
            }

            boolean gleicheFluggesellschaft = neuerFlug.getFluggesellschaft().equals(fluggesellschaft);

            if (!gleicheFluggesellschaft) {
                continue;
            }

            String nummernteilDerFlugnummer = neuerFlug.getFlugnummer().substring(airlineCode.length());
            int nummer = Integer.parseInt(nummernteilDerFlugnummer);

            if (nummer > hoechsteFlugnummer) {
                hoechsteFlugnummer = nummer;
            }
        }

        return airlineCode.toUpperCase() + String.format("%03d", hoechsteFlugnummer + 1);
    }

    /**
     * Entfernt einen Flug aus dem Verwaltungssystem.
     *
     * @param flug der zu entfernende Flug
     * @throws IllegalArgumentException wenn der Flug {@code null} oder nicht im Verwaltungssystem registriert ist
     * @throws IllegalStateException    wenn auf dem Flug noch mindestens ein Sitzplatz belegt ist
     */
    public void entferneFlug(Flug flug) {

        if (flug == null) {
            throw new IllegalArgumentException("Der Flug darf nicht null sein.");
        }

        if (!fluege.contains(flug)) {
            throw new IllegalArgumentException("Der Flug ist nicht im Verwaltungssystem registriert.");
        }

        // Belegte Sitze kennzeichnen noch bestehende Buchungen und verhindern das Entfernen.
        if (flug.berechneAuslastung() > 0) {
            throw new IllegalStateException("Der Flug kann nicht entfernt werden, da noch Buchungen vorhanden sind.");
        }

        fluege.remove(flug);
    }

    /**
     * Sucht alle Fluege zu einem bestimmten Zielflughafen.
     *
     * @param ziel der gesuchte Zielflughafen
     * @return die Liste aller Fluege zum angegebenen Zielflughafen
     * @throws IllegalArgumentException wenn der Zielflughafen {@code null} ist
     * @throws NoSuchElementException   wenn kein Flug zu dem Zielflughafen gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachZiel(Flughafen ziel) {

        if (ziel == null) {
            throw new IllegalArgumentException("Der Zielflughafen darf nicht null sein.");
        }

        ArrayList<Flug> newList = new ArrayList<>();

        for (Flug flug : fluege) {

            if (flug.getZielflughafen().equals(ziel)) {
                newList.add(flug);
            }
        }

        if (!newList.isEmpty()) {
            return newList;
        } else {
            throw new NoSuchElementException("Einen Flug nach " + ziel.stadt() + " gibt es leider nicht.");
        }
    }

    /**
     * Sucht alle Fluege auf einer bestimmten Route.
     *
     * @param start der gesuchte Startflughafen
     * @param ziel  der gesuchte Zielflughafen
     * @return die Liste aller Fluege zwischen Start- und Zielflughafen
     * @throws IllegalArgumentException wenn Start- oder Zielflughafen {@code null} ist
     * @throws NoSuchElementException   wenn auf der Route kein Flug gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachRoute(Flughafen start, Flughafen ziel) {

        if (start == null || ziel == null) {
            throw new IllegalArgumentException("Start- und Zielflughafen darf nicht null sein.");
        }

        ArrayList<Flug> newList = new ArrayList<>();

        for (Flug flug : fluege) {

            if (flug.getZielflughafen().equals(ziel) && flug.getStartFlughafen().equals(start)) {
                newList.add(flug);
            }
        }

        if (!newList.isEmpty()) {
            return newList;
        } else {
            throw new NoSuchElementException("Einen Flug von " +
                                             start.stadt() +
                                             " nach " +
                                             ziel.stadt() +
                                             " gibt es leider nicht.");
        }

    }

    /**
     * Sucht alle Fluege mit einer bestimmten Flugnummer. Da Flugnummern an unterschiedlichen Tagen erneut vergeben
     * werden koennen, kann die Ergebnisliste mehrere Fluege enthalten.
     *
     * @param flugnummer die gesuchte Flugnummer
     * @return die Liste aller Fluege mit der angegebenen Flugnummer
     * @throws IllegalArgumentException wenn die Flugnummer {@code null} ist
     * @throws NoSuchElementException   wenn kein Flug mit der Flugnummer gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachNummer(String flugnummer) {

        if (flugnummer == null) {
            throw new IllegalArgumentException("Die Flugnummmer darf nicht null sein.");
        }

        Iterator<Flug> iterator = fluege.iterator();
        ArrayList<Flug> alleFluegeMitFlugnummer = new ArrayList<>();

        while (iterator.hasNext()) {

            Flug flug = iterator.next();

            if (flug.getFlugnummer().equalsIgnoreCase(flugnummer)) {
                alleFluegeMitFlugnummer.add(flug);
            }
        }

        if (!alleFluegeMitFlugnummer.isEmpty()) {
            return alleFluegeMitFlugnummer;
        }

        throw new NoSuchElementException("Es wurden keine Fluege mit der Flugnummer " + flugnummer + " gefunden");
    }

    /**
     * Sucht alle Fluege an einem bestimmten Datum.
     *
     * @param datum das Datum
     * @return die Liste aller Fluege an dem angegebenen Datum
     * @throws IllegalArgumentException wenn das Datum {@code null} ist
     * @throws NoSuchElementException   wenn kein Flug an dem Datum gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachDatum(LocalDate datum) {

        if (datum == null) {
            throw new IllegalArgumentException("Das Datum darf nicht null sein.");
        }

        Iterator<Flug> iterator = fluege.iterator();
        ArrayList<Flug> gefundeneFluege = new ArrayList<>();

        while (iterator.hasNext()) {

            Flug flug = iterator.next();

            if (flug.getAbflugszeit().toLocalDate().equals(datum)) {
                gefundeneFluege.add(flug);
            }
        }

        if (!gefundeneFluege.isEmpty()) {
            return gefundeneFluege;
        }
        throw new NoSuchElementException("Es wurden keine Fluege an dem Datum " + datum + " gefunden.");
    }

    /**
     * Sucht einen bestimmten Flug anhand seiner Flugnummer und seines Abflugdatums.
     *
     * @param flugnummer die gesuchte Flugnummer
     * @param datum      das Abflugdatum des gesuchten Flugs
     * @return der Flug mit der angegebenen Flugnummer am angegebenen Datum
     * @throws IllegalArgumentException wenn die Flugnummer {@code null} oder leer ist oder das Datum {@code null} ist
     * @throws NoSuchElementException   wenn kein passender Flug gefunden wurde
     */
    public Flug sucheFlugNachNummer(String flugnummer, LocalDate datum) {

        if (flugnummer == null || flugnummer.isBlank()) {
            throw new IllegalArgumentException("Die Flugnummer darf nicht leer sein.");
        }

        if (datum == null) {
            throw new IllegalArgumentException("Das Datum darf nicht null sein.");
        }

        for (Flug flug : fluege) {

            if (flug.getFlugnummer().equalsIgnoreCase(flugnummer)) {

                if (flug.getAbflugszeit().toLocalDate().equals(datum)) {
                    return flug;
                }
            }
        }

        throw new NoSuchElementException("Der Flug " + flugnummer + " am " + datum + " wurde nicht gefunden.");
    }

    /**
     * Gibt alle registrierten Fluege zurueck.
     *
     * @return unveraenderbare Kopie aller registrierten Fluege
     */
    public List<Flug> getFluege() {

        return List.copyOf(fluege);
    }

    /**
     * Entfernt alle Fluege aus dem Verwaltungssystem, deren Abflugzeit bereits vergangen ist.
     * <p>
     * Vor dem Entfernen eines solchen Fluges werden alle zugehoerigen Buchungen auf den Buchungsstatus {@code VERGANGEN}
     * gesetzt.
     *
     * @param buchungssystem das Buchungssystem mit den zu aktualisierenden Buchungen
     */
    public void alteFluegeLoeschen(Buchungssystem buchungssystem) {

        Iterator<Flug> iterator = fluege.iterator();

        while (iterator.hasNext()) {

            Flug flug = iterator.next();

            if (flug.getAbflugszeit().isBefore(LocalDateTime.now())) {

                // Markiert zugehoerige Buchungen vor dem Entfernen des Flugs als vergangen.
                for (Buchung buchung : buchungssystem.getBuchungen()) {

                    if (buchung.getFlug() == flug) {
                        buchung.setBuchungsstatus(Buchungsstatus.VERGANGEN);
                    }
                }

                iterator.remove();
            }
        }
    }

    /**
     * Ermittelt die hoechste am angegebenen Tag vergebene Flugnummer einer Fluggesellschaft.
     *
     * @param fluggesellschaft die Fluggesellschaft, deren Flugnummern beruecksichtigt werden
     * @param abflugzeit       die Abflugzeit, deren Datum fuer die Suche gilt
     * @param airlineCode      der Airline-Code am Anfang der Flugnummer
     * @return der hoechste numerische Anteil der passenden Flugnummern oder {@code 0}, wenn kein Flug vorhanden ist
     */
    private int berechneHoechsteFlugnummer(Fluggesellschaft fluggesellschaft, LocalDateTime abflugzeit, String airlineCode) {

        airlineCode = airlineCode.toUpperCase();

        int hoechsteNummer = 0;

        // Fuer die naechste Nummer zaehlen nur Fluege derselben Airline am gleichen Abflugtag.
        for (Flug vorhandenerFlug : fluege) {

            boolean gleicherTag = vorhandenerFlug.getAbflugszeit().toLocalDate().equals(abflugzeit.toLocalDate());

            if (gleicherTag) {

                boolean gleicheAirline = vorhandenerFlug.getFluggesellschaft().equals(fluggesellschaft);

                if (gleicheAirline) {

                    String nummernTeil = vorhandenerFlug.getFlugnummer().substring(airlineCode.length());

                    int nummer = Integer.parseInt(nummernTeil);

                    if (nummer > hoechsteNummer) {
                        hoechsteNummer = nummer;
                    }
                }
            }
        }

        return hoechsteNummer;
    }

    /**
     * Prueft, ob keine Fluege registriert sind.
     *
     * @return {@code true}, wenn keine Fluege vorhanden sind, sonst {@code false}
     */
    public boolean keineFluegeVorhanden() {
        return fluege.isEmpty();
    }
}
