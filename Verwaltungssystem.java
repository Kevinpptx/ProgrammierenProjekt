
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/*
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Verwaltungssystem implements Serializable {

    private final ArrayList<Fluggesellschaft> fluggesellschaften = new ArrayList<>();
    private final ArrayList<Flug> fluege = new ArrayList<>();
    private final ArrayList<Flugzeug> flugzeuge = new ArrayList<>();
    private final ArrayList<Flughafen> flughaefen = new ArrayList<>();

    public Verwaltungssystem() {

    }

    // Verwaltung Fluggesellschaften
    public void fuegeFluggesellschaftHinzu(Fluggesellschaft fluggesellschaft) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaft-Objekt hat eine Nullreferenz");
        } else if (this.fluggesellschaften.contains(fluggesellschaft)) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaften-Objekt ist schon in der Liste enthalten");
        } else {
            this.fluggesellschaften.add(fluggesellschaft);
        }
    }

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

    public List<Fluggesellschaft> getFluggesellschaften() {
        return List.copyOf(this.fluggesellschaften);
    }

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

    public List<Flugzeug> getAlleFlugzeuge() {
        return List.copyOf(this.flugzeuge);
    }

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

    public List<Flughafen> getFlughaefen() {
        return List.copyOf(this.flughaefen);
    }

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
     * Fügt einen Flug der Liste "fluege" hinzu, wenn sie dort noch nicht
     * existieren
     *
     * @param flug
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

    public LocalDateTime erstelleLocalDateTime(int jahr, int monat, int tag, int stunde, int minute) {
        try {
            return LocalDateTime.of(jahr, monat, tag, stunde, minute);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Das eingegebene Datum oder die Uhrzeit ist ungültig.", e);
        }
    }

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

        return airlineCode + String.format("%03d", hoechsteNummer + 1);
    }

    public void entferneFlug(Flug flug) {
        if (flug == null) {
            throw new IllegalArgumentException("Der Flug darf nicht null sein.");
        }
        if (!fluege.remove(flug)) {
            throw new IllegalArgumentException("Der Flug ist nicht im Verwaltungssystem registriert.");
        }
    }

    // bis hier fine glaub ich 
    /**
     * Sucht Fluege, basierend auf dem @param ziel
     *
     * @return Liste, der insgesamt hinzugefügten Flüge
     * @throws NoSuchElementException wenn der gesuchte Flug nicht existiert
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
     * Sucht einen Flug nach einer Route
     *
     * @param start
     * @param ziel
     * @return Liste der gefundenen Fluege
     * @throws NoSuchElementException wenn der gesuchte Flug nicht existiert
     */
    public List<Flug> sucheFluegeNachRoute(Flughafen start, Flughafen ziel) {
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
     * Sucht einen Flug nach einer Flugnummer, die Teil der vorhandenen Flüge
     * sein sollte.
     *
     * @param Flugnummer
     * @return den gesuchten Flug
     * @throws IllegalArgumentException wenn die Flugnummer nicht vorhanden ist
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

    public List<Flug> getFluege() {
        return List.copyOf(fluege);
    }

    /**
     * zeigt alle Fluege, mit ihrer jeweiligen Auslastung
     *
     */
    public List<String> zeigeAlleFluegeMitAuslastung() {
        ArrayList<String> neueListe = new ArrayList<String>();
        for (int i = 0; i < fluege.size(); i++) {
            neueListe.add("\nFlug: " + fluege.get(i).getFlugnummer() + "| Auslastung: " + fluege.get(i).berechneAuslastung());
        }
        return neueListe;
    }
}
