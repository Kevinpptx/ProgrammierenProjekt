
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Zentrale Verwaltungsklasse des Flugbuchungssystems.
 * <p>
 * Das Verwaltungssystem verwaltet Fluggesellschaften, Flugzeuge, Flughäfen und
 * Flüge. Es stellt Methoden zum Hinzufügen, Entfernen, Erzeugen und Suchen
 * dieser Objekte bereit. Die gespeicherten Daten können durch die
 * Implementierung von {@link Serializable} serialisiert werden.
 * </p>
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Verwaltungssystem implements Serializable {

    /**
     * Enthält alle im Verwaltungssystem registrierten Fluggesellschaften.
     */
    private final ArrayList<Fluggesellschaft> fluggesellschaften = new ArrayList<>();

    /**
     * Enthält alle im Verwaltungssystem registrierten Flüge.
     */
    private final ArrayList<Flug> fluege = new ArrayList<>();

    /**
     * Enthält alle im Verwaltungssystem registrierten Flugzeuge.
     */
    private final ArrayList<Flugzeug> flugzeuge = new ArrayList<>();

    /**
     * Enthält alle im Verwaltungssystem registrierten Flughäfen.
     */
    private final ArrayList<Flughafen> flughaefen = new ArrayList<>();

    /**
     * Erstellt ein neues, zunächst leeres Verwaltungssystem.
     */
    public Verwaltungssystem() {

    }

    // Verwaltung Fluggesellschaften
    /**
     * Fügt eine Fluggesellschaft dem Verwaltungssystem hinzu.
     *
     * @param fluggesellschaft die hinzuzufügende Fluggesellschaft
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null}
     * ist oder bereits registriert wurde
     */
    public void fuegeFluggesellschaftHinzu(Fluggesellschaft fluggesellschaft) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaft-Objekt hat eine Nullreferenz");
        } else if (this.fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaften-Objekt ist schon in der Liste enthalten");
        } else {
            this.fluggesellschaften.add(fluggesellschaft);
        }
    }

    /**
     * Entfernt eine Fluggesellschaft aus dem Verwaltungssystem. Eine
     * Fluggesellschaft kann nur entfernt werden, wenn ihr keine registrierten
     * Flüge mehr zugeordnet sind.
     *
     * @param fluggesellschaft die zu entfernende Fluggesellschaft
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null}
     * oder nicht registriert ist oder noch einem Flug zugeordnet ist
     */
    public void entferneFluggesellschaft(Fluggesellschaft fluggesellschaft) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaft-Objekt hat eine Nullreferenz");
        } else if (!this.fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaften-Objekt wurde bisher noch nicht hinzugefügt.");
        } else {

            Iterator<Flug> iterator = fluege.iterator();

            while (iterator.hasNext()) {
                Flug f = iterator.next();

                if (f.getFluggesellschaft().equals(fluggesellschaft)) {
                    throw new IllegalArgumentException("Die Fluggesellschaft konnte nicht entfernt werden, da noch aktuelle Flüge geplant sind.");
                }
            }
            this.fluggesellschaften.remove(fluggesellschaft);
        }
    }

    /**
     * Gibt alle registrierten Fluggesellschaften zurück.
     *
     * @return unveränderbare Kopie der registrierten Fluggesellschaften
     */
    public List<Fluggesellschaft> getFluggesellschaften() {
        return List.copyOf(this.fluggesellschaften);
    }

    /**
     * Sucht eine Fluggesellschaft anhand ihres Airline-Codes.
     *
     * @param code der IATA-Code der gesuchten Fluggesellschaft
     * @return die Fluggesellschaft mit dem angegebenen Code
     * @throws IllegalArgumentException wenn keine Fluggesellschaft mit dem
     * angegebenen Code existiert
     */
    public Fluggesellschaft getFluggesellschaft(String code) {

        Iterator<Fluggesellschaft> iterator = this.fluggesellschaften.iterator();

        while (iterator.hasNext()) {
            Fluggesellschaft f = iterator.next();
            if (f.getAirlineCode().equals(code)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Eine Fluggesellschaft mit dem Code " + code + " existiert nicht.");

    }

    /**
     * Erzeugt ein neues Flugzeug und ordnet es einer registrierten
     * Fluggesellschaft zu.
     * <p>
     * Das Flugzeug wird sowohl in der Flotte der Fluggesellschaft als auch
     * zentral im Verwaltungssystem gespeichert.
     * </p>
     *
     * @param fluggesellschaft die Fluggesellschaft, der das Flugzeug gehört
     * @param code der eindeutige Code des Flugzeugs
     * @param modell die Modellbezeichnung des Flugzeugs
     * @param anzahlReihen die Gesamtzahl der Sitzreihen
     * @param sitzeProReihe die Anzahl der Sitzplätze pro Reihe
     * @param businessReihen die Anzahl der Business-Class-Reihen
     * @return das neu erzeugte Flugzeug
     * @throws IllegalArgumentException wenn die Fluggesellschaft {@code null}
     * oder nicht registriert ist oder bereits ein Flugzeug mit demselben Code
     * existiert
     */
    public Flugzeug erzeugeFlugzeug(Fluggesellschaft fluggesellschaft, String code, String modell, int anzahlReihen, int sitzeProReihe, int businessReihen) {

        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Die übergebene Fluggesellschaft existiert nicht.");
        }
        if (!fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Die Fluggesellschaft ist nicht im Verwaltungssystem registriert.");
        }

        Iterator<Flugzeug> iterator = this.flugzeuge.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getCode().equals(code)) {
                throw new IllegalArgumentException("Ein Flugzeug mit dem Code " + code + " existiert bereits.");
            }
        }
        Flugzeug f = new Flugzeug(code, modell, anzahlReihen, sitzeProReihe, businessReihen);

        fluggesellschaft.fuegeFlugzeugHinzu(f);
        this.flugzeuge.add(f);
        return f;

    }

    /**
     * Entfernt ein Flugzeug aus der Flotte.
     *
     * @param flugzeug das zu entfernende Flugzeug
     * @throws IllegalArgumentException wenn das Flugzeug null ist
     * @throws IllegalStateException wenn das Flugzeug nicht zur Flotte gehört
     */
    public void entferneFlugzeug(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Der Flugzeug-Code darf nicht leer sein.");
        }

        Flugzeug flug = this.getFlugzeug(code);

        if(flug == null) {
            throw new IllegalArgumentException("Es existiert kein Flugzeug mit dem Code " + code + ".");
        }

        Iterator<Flug> iteratorFlug = this.fluege.iterator();

        while(iteratorFlug.hasNext()) {
            Flug tempFlug = iteratorFlug.next();

            if(tempFlug.getFlugzeug().equals(flug)) {
                throw new IllegalStateException("Das FLugzeug kann nicht entfernt werden, da noch Flüge damit geplant sind.");
            }
        }

        Iterator<Fluggesellschaft> iteratorFlugGes = this.fluggesellschaften.iterator();

        while (iteratorFlugGes.hasNext()) {
            Fluggesellschaft flugGes = iteratorFlugGes.next();

            if(flugGes.besitztFlugzeug(flug)) {
                flugGes.entferneFlugzeug(flug);
                this.flugzeuge.remove(flug);
                return;
            }
        }

        throw new IllegalStateException("Das Flugzeug konnte keiner Fluggesellschaft zugeordnet werden");
    }

    /**
     * Gibt alle registrierten Flugzeuge zurück.
     *
     * @return unveränderbare Kopie aller registrierten Flugzeuge
     */
    public List<Flugzeug> getAlleFlugzeuge() {
        return List.copyOf(this.flugzeuge);
    }

    /**
     * Sucht ein Flugzeug anhand seines Codes.
     *
     * @param code der Code des gesuchten Flugzeugs
     * @return das Flugzeug mit dem angegebenen Code
     * @throws IllegalArgumentException wenn kein Flugzeug mit dem angegebenen
     * Code existiert
     */
    public Flugzeug getFlugzeug(String code) {

        Iterator<Flugzeug> iterator = this.flugzeuge.iterator();

        while (iterator.hasNext()) {
            Flugzeug f = iterator.next();
            if (f.getCode().equals(code)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Ein Flugzeug mit dem Code " + code + " existiert nicht.");
    }

    // Verwaltung Flughafen
    /**
     * Erzeugt einen neuen Flughafen und registriert ihn im Verwaltungssystem.
     * Name und IATA-Code müssen innerhalb des Verwaltungssystems eindeutig
     * sein.
     *
     * @param name der Name des Flughafens
     * @param iataCode der eindeutige IATA-Code des Flughafens
     * @param stadt die Stadt, in der sich der Flughafen befindet
     * @param land das Land, in dem sich der Flughafen befindet
     * @return der neu erzeugte Flughafen
     * @throws IllegalArgumentException wenn bereits ein Flughafen mit demselben
     * Namen oder IATA-Code existiert
     */
    public Flughafen erzeugeFlughafen(String name, String iataCode, String stadt, String land) {

        Iterator<Flughafen> iterator = this.flughaefen.iterator();

        while (iterator.hasNext()) {
            Flughafen f = iterator.next();
            if (f.getIataCode().equals(iataCode)) {
                throw new IllegalArgumentException("Ein Flughafen mit dem IATACode " + iataCode + " existiert bereits.");
            } else if (f.getName().equals(name)) {
                throw new IllegalArgumentException("Ein Flughafen mit dem Namen " + name + " existiert bereits.");
            }
        }

        Flughafen f = new Flughafen(name, iataCode, stadt, land);
        this.flughaefen.add(f);

        return f;
    }

    /**
     * Entfernt einen Flughafen aus dem Verwaltungssystem. Ein Flughafen kann
     * nur entfernt werden, wenn er bei keinem registrierten Flug als Start-
     * oder Zielflughafen verwendet wird.
     *
     * @param flughafen der zu entfernende Flughafen
     * @throws IllegalArgumentException wenn der Flughafen {@code null} oder
     * nicht registriert ist oder noch einem Flug zugeordnet ist
     */
    public void entferneFlughafen(Flughafen flughafen) {
        if (flughafen == null) {
            throw new IllegalArgumentException("Der Flughafen darf nicht null sein.");
        }

        if (this.flughaefen.contains(flughafen)) {

            Iterator<Flug> iterator = fluege.iterator();

            while (iterator.hasNext()) {
                Flug f = iterator.next();

                if (f.getStartFlughafen().equals(flughafen) || f.getZielflughafen().equals(flughafen)) {
                    throw new IllegalArgumentException("Der Flughafen konnte nicht entfernt werden, da hier noch aktuelle Flüge geplant sind.");
                }
            }
            this.flughaefen.remove(flughafen);
        } else {
            throw new IllegalArgumentException("Der Flughafen " + flughafen.toString() + " ist nicht aktiv.");
        }

    }

    /**
     * Sucht einen Flughafen anhand seines IATA-Codes.
     *
     * @param iataCode der IATA-Code des gesuchten Flughafens
     * @return der Flughafen mit dem angegebenen IATA-Code
     * @throws IllegalArgumentException wenn kein Flughafen mit dem angegebenen
     * IATA-Code existiert
     */
    public Flughafen getFlughafenNachCode(String iataCode) {

        Iterator<Flughafen> iterator = this.flughaefen.iterator();

        while (iterator.hasNext()) {
            Flughafen f = iterator.next();
            if (f.getIataCode().equals(iataCode)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Der Flughafen mit dem IATACode " + iataCode + " konnte nicht gefunden werden.");
    }

    /**
     * Sucht einen Flughafen anhand seines Namens.
     *
     * @param name der Name des gesuchten Flughafens
     * @return der Flughafen mit dem angegebenen Namen
     * @throws IllegalArgumentException wenn kein Flughafen mit dem angegebenen
     * Namen existiert
     */
    public Flughafen getFlughafenNachName(String name) {

        Iterator<Flughafen> iterator = this.flughaefen.iterator();

        while (iterator.hasNext()) {
            Flughafen f = iterator.next();
            if (f.getName().equals(name)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Der Flughafen mit dem Namen " + name + " konnte nicht gefunden werden.");
    }

    /**
     * Sucht den ersten registrierten Flughafen in einer bestimmten Stadt.
     *
     * @param stadt die Stadt des gesuchten Flughafens
     * @return der erste gefundene Flughafen in der angegebenen Stadt
     * @throws IllegalArgumentException wenn in der angegebenen Stadt kein
     * Flughafen gefunden wurde
     */
    public Flughafen getFlughafenNachStadt(String stadt) {

        Iterator<Flughafen> iterator = this.flughaefen.iterator();

        while (iterator.hasNext()) {
            Flughafen f = iterator.next();
            if (f.getStadt().equals(stadt)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Der Flughafen mit dem Namen " + stadt + " konnte nicht gefunden werden.");
    }

    /**
     * Gibt alle registrierten Flughäfen zurück.
     *
     * @return unveränderbare Kopie aller registrierten Flughäfen
     */
    public List<Flughafen> getFlughaefen() {
        return List.copyOf(this.flughaefen);
    }

    /**
     * Sucht alle registrierten Flughäfen in einem bestimmten Land.
     *
     * @param land das Land, nach dessen Flughäfen gesucht wird
     * @return Liste aller gefundenen Flughäfen in dem angegebenen Land
     * @throws IllegalArgumentException wenn in dem Land kein Flughafen gefunden
     * wurde
     */
    public ArrayList<Flughafen> getFlughaefenInLand(String land) {

        Iterator<Flughafen> iterator = this.flughaefen.iterator();

        ArrayList<Flughafen> ret = new ArrayList<>();

        while (iterator.hasNext()) {
            Flughafen f = iterator.next();
            if (f.getLand().equals(land)) {
                ret.add(f);
            }
        }

        if (!ret.isEmpty()) {
            return ret;
        } else {
            throw new IllegalArgumentException("Im Land " + land + " konnte kein Flughafen gefunden werden.");
        }
    }

    // Verwaltung Flüge
    /**
     * Erzeugt einen neuen Flug und registriert ihn im Verwaltungssystem.
     * <p>
     * Die Flugnummer wird automatisch aus dem Airline-Code und einer
     * fortlaufenden Nummer für den jeweiligen Abflugtag gebildet. Zusätzlich
     * wird geprüft, ob das Flugzeug im angegebenen Zeitraum bereits für einen
     * anderen Flug eingeplant ist.
     * </p>
     *
     * @param fluggesellschaft die ausführende Fluggesellschaft
     * @param flugzeug das für den Flug eingesetzte Flugzeug
     * @param startFlughafen der Startflughafen
     * @param zielFlughafen der Zielflughafen
     * @param abflugzeit Datum und Uhrzeit des Abflugs
     * @param ankunftszeit Datum und Uhrzeit der Ankunft
     * @param basispreis der Basispreis des Flugs
     * @param rueckflug ob ein Rückflug angelegt werden soll
     * @param anzahlTageWiederholungen Anzahl der Tage, an denen der Flug nacheinander stattfindet (bei einmaligen Flügen: 1)
     * @return die neu erzeugten und registrierten Flüge in einer ArrayList<Flug>
     * @throws IllegalArgumentException wenn eine übergebene Referenz
     * {@code null} ist, benötigte Objekte nicht registriert sind, das Flugzeug
     * nicht zur Fluggesellschaft gehört, der Flug bereits existiert oder sich
     * die Einsatzzeiten des Flugzeugs überschneiden
     */
    public ArrayList<Flug> fuegeFlugHinzu(Fluggesellschaft fluggesellschaft, Flugzeug flugzeug, Flughafen startFlughafen, Flughafen zielFlughafen, LocalDateTime abflugzeit,
            LocalDateTime ankunftszeit, double basispreis, boolean rueckflug, int anzahlTageWiederholungen) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Die Fluggesellschaft darf nicht null sein.");
        }

        if (flugzeug == null) {
            throw new IllegalArgumentException("Das Flugzeug darf nicht null sein.");
        }

        if (startFlughafen == null || zielFlughafen == null) {
            throw new IllegalArgumentException("Start- und Zielflughafen dürfen nicht null sein.");
        }

        if (abflugzeit == null || ankunftszeit == null) {
            throw new IllegalArgumentException("Abflug- und Ankunftszeit dürfen nicht null sein.");
        }

        if (!fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Die Fluggesellschaft ist nicht im Verwaltungssystem registriert.");
        }

        if (!flughaefen.contains(startFlughafen) || !flughaefen.contains(zielFlughafen)) {
            throw new IllegalArgumentException("Start- und Zielflughafen müssen im Verwaltungssystem registriert sein.");
        }

        if (!fluggesellschaft.besitztFlugzeug(flugzeug)) {
            throw new IllegalArgumentException("Das Flugzeug gehört nicht zur angegbenen Fluggesellschaft");
        }

        if (anzahlTageWiederholungen <1) {
            throw new IllegalArgumentException("Der Flug muss mindestens an einem Tag stattfinden.");
        }

        ArrayList<Flug> erzeugteFluege = new ArrayList<>();

        for (int i = 0; i < anzahlTageWiederholungen; i++) {
            // Erstelle Flug und Rückflug
            erzeugteFluege.add(this.fuegeFlugHinzu(fluggesellschaft, flugzeug, startFlughafen, zielFlughafen, abflugzeit, ankunftszeit, basispreis));

            if(rueckflug){
                Duration flugdauer = Duration.between(abflugzeit, ankunftszeit);
                Duration turnAroundTime = Duration.ofHours(1);
                LocalDateTime abflugszeitRueckflug = ankunftszeit.plus(turnAroundTime);

                /*
                    Fluggesellschaft und Flugzeug bleiben gleich
                    zielFlughafen und startFlughafen werden getauscht 
                    Die Ankfuntszeit des Hinflugs wird plus eine TurnAroundTime von 1 Std als neue Abflugzeit gesetzt
                    Die Ankunftszeit des Rückfluges ist die aus dem Hinflug errechnete Flugdauer auf die neue Abflugszeit addiert
                    Basispreis bleibt gleich wie beim Hinflug
                    Rückflug wird auf false gesetzt, damit nicht noch ein Flug erzeugt wird. 
                */
                erzeugteFluege.add(this.fuegeFlugHinzu(fluggesellschaft, flugzeug, zielFlughafen, startFlughafen, abflugszeitRueckflug, abflugszeitRueckflug.plus(flugdauer), basispreis));
            }

            abflugzeit.plusDays(1);
            ankunftszeit.plusDays(1);
        }
        return erzeugteFluege;
    }

    /**
     * Erzeugt einen neuen Flug und registriert ihn im Verwaltungssystem.
     * <p>
     * Die Flugnummer wird automatisch aus dem Airline-Code und einer
     * fortlaufenden Nummer für den jeweiligen Abflugtag gebildet. Zusätzlich
     * wird geprüft, ob das Flugzeug im angegebenen Zeitraum bereits für einen
     * anderen Flug eingeplant ist.
     * </p>
     *
     * @param fluggesellschaft die ausführende Fluggesellschaft
     * @param flugzeug das für den Flug eingesetzte Flugzeug
     * @param startFlughafen der Startflughafen
     * @param zielFlughafen der Zielflughafen
     * @param abflugzeit Datum und Uhrzeit des Abflugs
     * @param ankunftszeit Datum und Uhrzeit der Ankunft
     * @param basispreis der Basispreis des Flugs
     * @return der neu erzeugte und registrierte Flug
     * @throws IllegalArgumentException wenn eine übergebene Referenz
     * {@code null} ist, benötigte Objekte nicht registriert sind, das Flugzeug
     * nicht zur Fluggesellschaft gehört, der Flug bereits existiert oder sich
     * die Einsatzzeiten des Flugzeugs überschneiden
     */
    public Flug fuegeFlugHinzu(Fluggesellschaft fluggesellschaft, Flugzeug flugzeug, Flughafen startFlughafen, Flughafen zielFlughafen, LocalDateTime abflugzeit,
            LocalDateTime ankunftszeit, double basispreis) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Die Fluggesellschaft darf nicht null sein.");
        }

        if (flugzeug == null) {
            throw new IllegalArgumentException("Das Flugzeug darf nicht null sein.");
        }

        if (startFlughafen == null || zielFlughafen == null) {
            throw new IllegalArgumentException("Start- und Zielflughafen dürfen nicht null sein.");
        }

        if (abflugzeit == null || ankunftszeit == null) {
            throw new IllegalArgumentException("Abflug- und Ankunftszeit dürfen nicht null sein.");
        }

        if (!fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Die Fluggesellschaft ist nicht im Verwaltungssystem registriert.");
        }

        if (!flughaefen.contains(startFlughafen) || !flughaefen.contains(zielFlughafen)) {
            throw new IllegalArgumentException("Start- und Zielflughafen müssen im Verwaltungssystem registriert sein.");
        }

        if (!fluggesellschaft.besitztFlugzeug(flugzeug)) {
            throw new IllegalArgumentException("Das Flugzeug gehört nicht zur angegbenen Fluggesellschaft");
        }

        String flugnummer = this.erzeugeFlugnummer(fluggesellschaft, abflugzeit);

        Iterator<Flug> iterator = fluege.iterator();

        while (iterator.hasNext()) {
            Flug vorhandenerFlug = iterator.next();

            boolean gleicherFlugAmSelbenTag
                    = vorhandenerFlug.getFlugnummer().equalsIgnoreCase(flugnummer)
                    && vorhandenerFlug.getAbflugszeit().toLocalDate().equals(abflugzeit.toLocalDate());

            if (gleicherFlugAmSelbenTag) {
                throw new IllegalArgumentException("Der Flug " + flugnummer + " existiert an diesem Tag bereits.");
            }

            boolean gleichesFlugzeug
                    = vorhandenerFlug.getFlugzeug().equals(flugzeug);

            if (gleichesFlugzeug) {

                // Überschneidung liegt nur vor, wenn der neue Flug vor dem Ende des alten beginnt und nach dem Beginn des alten endet
                boolean zeitenUeberschneidenSich
                        = abflugzeit.isBefore(vorhandenerFlug.getAnkunftszeit())
                        && ankunftszeit.isAfter(vorhandenerFlug.getAbflugszeit());

                if (zeitenUeberschneidenSich) {
                    throw new IllegalArgumentException("Das Flugzeug ist in diesem Zeitraum bereits eingeplant.");
                }
            }
        }

        Flug flug = new Flug(flugnummer, fluggesellschaft, flugzeug, startFlughafen, zielFlughafen, abflugzeit, ankunftszeit, basispreis);

        if (this.fluege.contains(flug)) {
            throw new IllegalArgumentException("Das übergebene Flug-Objekt ist schon in der Liste enthalten");
        } else {
            this.fluege.add(flug); 
            return flug;
        }
    }


    /**
     * Erstellt ein {@link LocalDateTime}-Objekt aus den angegebenen
     * Datums- und Zeitbestandteilen.
     *
     * @param jahr das Jahr
     * @param monat der Monat von 1 bis 12
     * @param tag der Tag des Monats
     * @param stunde die Stunde von 0 bis 23
     * @param minute die Minute von 0 bis 59
     * @return das erzeugte Datum mit Uhrzeit
     * @throws IllegalArgumentException wenn die angegebenen Werte kein gültiges
     *                                  Datum oder keine gültige Uhrzeit ergeben
     */
    public LocalDateTime erstelleLocalDateTime(int jahr, int monat, int tag, int stunde, int minute) {
        try {
            return LocalDateTime.of(jahr, monat, tag, stunde, minute);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Das eingegebene Datum oder die Uhrzeit ist ungültig.", e);
        }
    }

    /**
     * Erzeugt für eine Fluggesellschaft und einen Abflugtag die nächste freie
     * Flugnummer.
     * <p>
     * Die Flugnummer besteht aus dem großgeschriebenen Airline-Code und einer
     * dreistelligen, pro Tag und Fluggesellschaft fortlaufenden Nummer.
     * </p>
     *
     * @param fluggesellschaft die Fluggesellschaft des Flugs
     * @param abflugzeit die Abflugzeit, deren Datum für die Nummerierung gilt
     * @return die erzeugte Flugnummer, beispielsweise {@code LH001}
     */
    private String erzeugeFlugnummer(Fluggesellschaft fluggesellschaft, LocalDateTime abflugzeit) {

        String airlineCode = fluggesellschaft.getAirlineCode().toUpperCase();

        int hoechsteNummer = 0;

        Iterator<Flug> iterator = fluege.iterator();

        while (iterator.hasNext()) {

            Flug vorhandenerFlug = iterator.next();

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

        return airlineCode.toUpperCase() + String.format("%03d", hoechsteNummer + 1);
    }

    /**
     * Entfernt einen Flug aus dem Verwaltungssystem.
     *
     * @param flug der zu entfernende Flug
     * @throws IllegalArgumentException wenn der Flug {@code null} oder nicht im
     *                                  Verwaltungssystem registriert ist
     */
    public void entferneFlug(Flug flug) {
        if (flug == null) {
            throw new IllegalArgumentException("Der Flug darf nicht null sein.");
        }
        if (!fluege.remove(flug)) {
            throw new IllegalArgumentException("Der Flug ist nicht im Verwaltungssystem registriert.");
        }
    }

    /**
     * Sucht alle Flüge zu einem bestimmten Zielflughafen.
     *
     * @param ziel der gesuchte Zielflughafen
     * @return Liste aller Flüge zum angegebenen Zielflughafen
     * @throws IllegalArgumentException wenn der Zielflughafen {@code null} ist
     * @throws NoSuchElementException wenn kein Flug zu dem Zielflughafen
     *                                gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachZiel(Flughafen ziel) {
        //neue Liste wird erstellt
        if (ziel == null) {
            throw new IllegalArgumentException("Der Zielflughafen darf nicht null sein.");
        }

        ArrayList<Flug> newList = new ArrayList<>();

        for (int i = 0; i < fluege.size(); i++) {
            if (fluege.get(i).getZielflughafen().equals(ziel)) {
                newList.add(fluege.get(i));
            }
        }
        if (!newList.isEmpty()) {
            return newList;
        } else {
            throw new NoSuchElementException("Einen Flug nach " + ziel.getStadt() + " gibt es leider nicht.");
        }
    }

    /**
     * Sucht alle Flüge auf einer bestimmten Route.
     *
     * @param start der gesuchte Startflughafen
     * @param ziel der gesuchte Zielflughafen
     * @return Liste aller Flüge zwischen Start- und Zielflughafen
     * @throws IllegalArgumentException wenn Start- oder Zielflughafen
     *                                  {@code null} ist
     * @throws NoSuchElementException wenn auf der Route kein Flug gefunden wurde
     */
    public ArrayList<Flug> sucheFluegeNachRoute(Flughafen start, Flughafen ziel) {
        if (start == null || ziel == null) {
            throw new IllegalArgumentException("Start- und Zielflughafen darf nicht null sein.");
        }
        ArrayList<Flug> newList = new ArrayList<Flug>();

        for (int i = 0; i < fluege.size(); i++) {
            if (fluege.get(i).getZielflughafen().equals(ziel) && fluege.get(i).getStartFlughafen().equals(start)) {
                newList.add(fluege.get(i));
            }
        }
        if (!newList.isEmpty()) {
            return newList;
        } else {
            throw new NoSuchElementException("Einen Flug von " + start.getStadt() + " nach " + ziel.getStadt() + " gibt es leider nicht.");
        }

    }

    /**
     * Sucht alle Flüge mit einer bestimmten Flugnummer.
     * Da Flugnummern an unterschiedlichen Tagen erneut vergeben werden können,
     * kann die Ergebnisliste mehrere Flüge enthalten.
     *
     * @param flugnummer die gesuchte Flugnummer
     * @return Liste aller Flüge mit der angegebenen Flugnummer
     * @throws IllegalArgumentException wenn die Flugnummer {@code null} ist
     * @throws NoSuchElementException wenn kein Flug mit der Flugnummer gefunden
     *                                wurde
     */
    public ArrayList<Flug> sucheFluegeNachNummer(String flugnummer) {
        if (flugnummer == null) {
            throw new IllegalArgumentException("Die Flugnummmer darf nicht null sein.");
        }

        Iterator<Flug> iterator = fluege.iterator();
        ArrayList<Flug> ret = new ArrayList<>();

        while (iterator.hasNext()) {
            Flug f = iterator.next();
            if (f.getFlugnummer().equalsIgnoreCase(flugnummer)) {
                ret.add(f);
            }
        }
        if (!ret.isEmpty()) {
            return ret;
        }
        throw new NoSuchElementException("Es wurden keine Flüge mit der Flugnummer " + flugnummer + " gefunden");
    }

    /**
     * Sucht einen bestimmten Flug anhand seiner Flugnummer und seines
     * Abflugdatums.
     *
     * @param flugnummer die gesuchte Flugnummer
     * @param datum das Abflugdatum des gesuchten Flugs
     * @return der Flug mit der angegebenen Flugnummer am angegebenen Datum
     * @throws IllegalArgumentException wenn die Flugnummer {@code null} oder
     *                                  leer ist oder das Datum {@code null} ist
     * @throws NoSuchElementException wenn kein passender Flug gefunden wurde
     */
    public Flug sucheFlugNachNummer(String flugnummer, LocalDate datum) {

        if (flugnummer == null || flugnummer.isBlank()) {
            throw new IllegalArgumentException("Die Flugnummer darf nicht leer sein.");
        }

        if (datum == null) {
            throw new IllegalArgumentException("Das Datum darf nicht null sein.");
        }

        Iterator<Flug> iterator = fluege.iterator();

        while (iterator.hasNext()) {
            Flug f = iterator.next();
            if (f.getFlugnummer().equalsIgnoreCase(flugnummer)) {
                if (f.getAbflugszeit().toLocalDate().equals(datum)) {
                    return f;
                }
            }
        }
        throw new NoSuchElementException("Der Flug " + flugnummer + " am " + datum + " wurde nicht gefunden.");
    }

    /**
     * Gibt alle registrierten Flüge zurück.
     *
     * @return unveränderbare Kopie aller registrierten Flüge
     */
    public List<Flug> getFluege() {
        return List.copyOf(fluege);
    }

    /**
     * Erstellt für jeden registrierten Flug eine Textdarstellung aus
     * Flugnummer und aktueller Auslastung.
     *
     * @return Liste mit einer Textzeile für jeden registrierten Flug
     */
    public List<String> zeigeAlleFluegeMitAuslastung() {
        ArrayList<String> neueListe = new ArrayList<String>();
        for (int i = 0; i < fluege.size(); i++) {
            neueListe.add("\nFlug: " + fluege.get(i).getFlugnummer() + "| Auslastung: " + fluege.get(i).berechneAuslastung());
        }
        return neueListe;
    }
}
