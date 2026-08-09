import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten
 * Buchungsprozess.
 * Sie dient dazu, die Klassen zu koordinieren und dafür zu sorgen, dass sie
 * miteinander interagieren können.
 * 
 * 
 * @version 1.1
 * @author Marcel Marxkors
 */

public class Buchungssystem implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /** Liste der Buchungen, die schon vorgenommen wurden */
    private ArrayList<Buchung> buchungen;

    /** Liste der schon registrierten Passagiere */
    private ArrayList<Passagier> passagiere;

    /**
     * Anzahl der insgesamt getätigten Buchungsnummern, erste Idee einer möglichen
     * Grundlage für die Buchungsnummer
     */
    private int anzahlBuchungen;

    /**
     * Anzahl der insgesamt registrierten Passagiere, erste Idee einer möglichen
     * Grundlage für die Passagiernummer
     */
    private int anzahlPassagiere;

    /**
     * Konstruktor der Klasse, der die temporäre Lösung der Datenspeicherung
     * initialisiert
     */
    public Buchungssystem() {
        buchungen = new ArrayList<>();
        passagiere = new ArrayList<>();
        anzahlBuchungen = 0;
        anzahlPassagiere = 0;

    }

    /**
     * Erstellt ein Objekt vom Typ Passagier und prüft, ob die übergebenenn
     * Parameter gültig sind.
     * 
     * @param name  : Name, der beim Erstellen eines Passagiers diesem zugewiesen
     *              werden soll
     * @param email : Mail-Adresse des Passagiers, die der Passagier beim
     *              erstmaligen anmelden angeben soll, damit man mit ihm in Kontakt
     *              treten könnte.
     * @return
     */
    public Passagier initialisierePassagier(String name, String email) {
        if (name != null && email != null) {
            anzahlPassagiere++;
            try {
                String vorlaeufigePassagierId = "p" + Integer.toString(anzahlPassagiere);
                Passagier p = new Passagier(vorlaeufigePassagierId, name, email);
                passagiere.add(p);
                return p;
            }
            catch (IllegalArgumentException e) {
                anzahlPassagiere--;
                throw e;
            }



        } else if (name == null) {
            throw new IllegalArgumentException("Fehler! Es wurde kein Name eingegeben.");
        } else {
            throw new IllegalArgumentException("Fehler! Es wurde keine Mail-Adresse eingegeben.");
        }

    }

    /**
     * Erstellt ein Objekt der Klasse Buchung, wenn die eingegebenen Parameter
     * valide sind, der angegebene Flug in der Liste der vorhandenen Fluege ist und
     * der Sitzplatz fuer den Flug existiert
     * 
     * @param passagier       : der Passagier, für den die Buchung erstellt wird
     * @param flug            : Der Flug, der gebucht werden soll
     * @param sitzplatznummer : wird in Reihe und Nummer des Sitzplatzes
     *                        umgewandelt, um die Methode "belegeSitzplatz()" der
     *                        Klasse Flug auszufuehren. Wird außerdem für die
     *                        Erstellung und Zuweisung eines Sitzplatzes benoetigt.
     * @param anzahlKoffer    : Anzahl der gebuchten Koffer
     * @return Buchung, die soeben erstellt wurde
     * @throws RuntimeException basierend darauf, was dazu geführt hat, dass die
     *                          Buchung ungueltig ist
     */

    public Buchung buchungVornehmen(Passagier passagier, Flug flug, String sitzplatznummer, int anzahlKoffer,
            Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem) {
        if ((passagier == null || flug == null || sitzplatznummer == null || sitzklasse == null)) {
            throw new IllegalArgumentException("Fehler! Mindestens einer der übergebenen Werte ist ungültig.");
        }

        // Existiert der Passagier im System? Wenn nicht: Fehler
        if (!passagiere.contains(passagier)) {
            throw new IllegalArgumentException("Der übergebene Passagier existiert nicht (mehr) im System. Bitte einen anderen Passagier wählen oder die Buchung neu vornehmen!");
        }

        // Existiert der Flug im System? Wenn nicht: Fehler
         if (!verwaltungssystem.getFluege().contains(flug)) {
            throw new IllegalArgumentException("Der übergebene Flug existiert nicht (mehr) im System. Bitte einen anderen Flug wählen oder die Buchung neu vornehmen!");
         }

        try {

            // Objekte holen / erstellen
            Sitzplatz sitzplatz = flug.findeSitzplatz(sitzplatznummer);
            GepaeckInformation gepaeckinfo = new GepaeckInformation(anzahlKoffer);
            List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);

            // Platz validieren und ggf. belegen
            try {
                flug.validiereSitzplatz(sitzplatz, sitzklasse, klassenliste);
                flug.belegeSitzplatz(sitzplatz);
            } catch (Exception e) {
                throw e;
            }

            Buchung b = new Buchung(passagier, flug, sitzplatz, gepaeckinfo);
            b.setBuchungsnummer("bu" + anzahlBuchungen);
            anzahlBuchungen++;
            sitzplatz.setBuchung(b);
            buchungen.add(b);
            return b;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Sucht die bisherigen Buchungen nach einer Buchungsnummer ab
     * 
     * @param buchungsnummer
     * @return die Buchung mit der Buchungsnummer
     * @throws IllegalArgumentException wenn die Buchung nicht oder vorhanden ist
     */
    public Buchung sucheBuchungNachNummer(String buchungsnummer) {
        for (int i = 0; i < buchungen.size(); i++) {
            Buchung b = buchungen.get(i);
            if (b.getBuchungsnummer().equals(buchungsnummer)) {
                return b;
            }
        }
        throw new IllegalArgumentException(
                "Fehler! Die Buchung mit der Buchungsnummer " + buchungsnummer + " ist nicht vorhanden.");
    }

    /**
     * Bucht einen Passagier um.
     * Die entgegengenommenen Parameter können neu sein oder nicht, das erfolgt in
     * der Methode {@link validiereUmbuchung}
     * 
     * @param buchung die aktuell vorliegende Buchung
     * @param Flug der betreffende Flug
     * @param sitzplatznummer die betreffende Sitzplatznummer
     * @param sitzklasse mit entsprechender Sitzklasse
     * @return den Buchungsbetrag
     */
    public double umbuchen(Buchung buchung, Flug flug, String sitzplatznummer, Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem) {
    boolean umbuchungMoeglich = false;
    double gebuehr = 0.0;

    // Umbuchung prüfen
    umbuchungMoeglich = validiereUmbuchung(buchung, flug, sitzplatznummer, sitzklasse, verwaltungssystem);

    if (umbuchungMoeglich) {
        // alten Sitzplatz merken
        Sitzplatz alterSitzplatz = buchung.getSitzplatz();

        // neuen Sitzplatz ermitteln
        Sitzplatz neuerSitzplatz = flug.findeSitzplatz(sitzplatznummer);

        // Umbuchungsgebühr berechnen
        gebuehr = berechneUmbuchungsgebuehr(buchung, flug, neuerSitzplatz);

        // neuen Sitzplatz belegen
        neuerSitzplatz.belegen();

        // Buchung aktualisieren
        buchung.setGezahlteUmbuchungsgebuehr(gebuehr);
        buchung.setSitzplatz(neuerSitzplatz);
        neuerSitzplatz.setBuchung(buchung);
        buchung.setFlug(flug);
        buchung.aktualisiereGezahltenPreis();
        buchung.setBuchungsstatus(Buchungsstatus.UMGEBUCHT);

        // Alter Sitzplatz wird erst freigegeben, nachdem die Buchung vollständig vorbereitet wurde
        alterSitzplatz.freigeben();
        alterSitzplatz.setBuchung(null);
    }

    return gebuehr;
}

    /**
     * Schaut, ob eine Umbuchung möglich ist.
     * Geht dafür bei Buchung auf einen neuen Flug alle Sitzplätze des neuen Fluges
     * durch und schaut, ob die Sitzplatznummer im neuen Flug existiert.
     * Bei Buchung auf neue Sitzplatznummer wird nur geprüft, ob der neue Sitzplatz
     * noch frei ist
     * Schaut vorher, ob die Objekte nicht null sind
     * Prüft, ob eine Umbuchung schon vorgenommen wurde
     * 
     * @param buchung
     * @param neuerFlug
     * @param neueSitzplatznummer
     * @return
     */
    public boolean validiereUmbuchung(Buchung buchung, Flug neuerFlug, String neueSitzplatznummer,
            Sitzklasse sitzklasse, Verwaltungssystem verwaltungssystem) {
        // wenn die Buchung nicht vorhanden ist
        if (buchung == null) {
            throw new NoSuchElementException("Es ist keine Buchung angegeben, von der umgebucht werden soll.");
        }

        // wenn neuer Flug gar nicht im System existiert
        else if (!verwaltungssystem.getFluege().contains(neuerFlug)) {
            throw new NoSuchElementException("Der Flug, auf den umgebucht werden soll, ist nicht (mehr) im System registriert.");
        }

        // wenn beide Buchungsparameter leer sind
        else if (neuerFlug == null && neueSitzplatznummer == null) {
            throw new NoSuchElementException("Beide Buchungsparameter sind leer.");
        }

        // wenn schon storniert oder der Flug bereits in der Vergangenheit liegt
        else if (buchung.getBuchungsstatus() == Buchungsstatus.STORNIERT || buchung.getBuchungsstatus() == Buchungsstatus.VERGANGEN) {
            throw new IllegalStateException("Stornierte oder vergangene Buchungen können nicht umgebucht werden.");
        }

        // wenn keine Sitzklasse angegeben wurde
        else if (sitzklasse == null) {
            throw new NoSuchElementException("Es wurde keine Sitzklasse angegeben.");
        }

        // wenn im selben Flug ein anderer Sitzplatz gebucht werden muss
        else if (neuerFlug == buchung.getFlug() || neuerFlug == null) {
            return validiereUmbuchungimSelbenFlug(buchung, neueSitzplatznummer, sitzklasse);
        }

        // wenn im neuen Flug ein Sitzplatz gebucht werden muss(Prüft, ob die
        // Sitzplatznummer im neuen Flug vorhanden ist und ob der Sitzplatz belegt ist)
        else {
            return validiereUmbuchungimNeuenFlug(neuerFlug, neueSitzplatznummer, sitzklasse);
        }

    }

    /**
     * Prüft, ob eine Umbuchung im selben Flug möglich ist.
     * 
     * @param buchung             : Aktuelle Buchung
     * @param neueSitzplatznummer : Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          : Sitzklasse, die der neue Sitzplatz haben soll
     * @return true, wenn die Validierung erfolgreich ist
     * @throws Exception aus der Methode "validiereSitzplatz"
     */
    public boolean validiereUmbuchungimSelbenFlug(Buchung buchung, String neueSitzplatznummer, Sitzklasse sitzklasse) {
        Sitzplatz sitz = buchung.getFlug().findeSitzplatz(neueSitzplatznummer);
        List<Sitzplatz> klassenliste = buchung.getFlug().getFreieSitzplaetzeNachKlasse(sitzklasse);

        try {
            buchung.getFlug().validiereSitzplatz(sitz, sitzklasse, klassenliste);
        } catch (Exception e) {
            throw e;
        }
        return true;

    }

    /**
     * Prüft, ob eine Buchung im neuen Flug gültig ist
     * 
     * @param flug                : neuer Flug, auf den umgebucht werden soll
     * @param neueSitzplatznummer : Sitzplatznummer, auf die umgebucht werden soll
     * @param sitzklasse          : Sitzklasse des neuen Sitzplatzes
     * @return true, wenn die Validierung erfolgreich ist
     * @throws Exception aus der Methode "validiereSitzplatz"
     */
    public boolean validiereUmbuchungimNeuenFlug(Flug flug, String neueSitzplatznummer, Sitzklasse sitzklasse) {
        Sitzplatz sitz = flug.findeSitzplatz(neueSitzplatznummer);
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);
        try {
            flug.validiereSitzplatz(sitz, sitzklasse, klassenliste);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    /**
     * Berechnet die Gebühr für die Umbuchung und gibt diese zurück
     * 
     * @param buchung   : Buchung, die umgebucht wurde
     * @param flug      : flug, auf den umgebucht wurde (wird für den Basispreis
     *                  benötigt)
     * @param sitzplatz : Sitzplatz, auf den umgebucht wurde (wichtig für die
     *                  Sitzklasse)
     * @return
     */
    public double berechneUmbuchungsgebuehr(Buchung buchung, Flug flug, Sitzplatz sitzplatz) {
        // Legt zuerst die Variablen für die Berechnung fest
        double preisAlterFlug = buchung.getGezahlterPreis();
        double preisNeuerFlug;
        double ticketDifferenz;
        double finaleGebuehr;

        //berechnet den neuen Preis anhand der Sitzklasse
        if (sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS) {
            preisNeuerFlug = flug.getBasispreis() * buchung.getBusinesspreisfaktor();
        }
        else {
            preisNeuerFlug = flug.getBasispreis();
        }

        //ergänze Koffergebühr für den neuen Flug 
        preisNeuerFlug += buchung.getGepaeckinformation().berechneGepaeckgebuehr();

        //berechnet die Differenz aus altem und neuen Flug
        ticketDifferenz = preisNeuerFlug - preisAlterFlug;

        // berechnet die finale Gebühr und schlägt die Ticketdifferenz auf, wenn diese positiv ist
        finaleGebuehr = buchung.getUmbuchungsgebuehr() + Math.max(0, ticketDifferenz);

        return finaleGebuehr;
    }

    /**
     * storniert eine vorhandene Buchung; ändert den Buchungsstatus und gibt den Sitzplatz der Buchung frei
     * 
     * @param buchung
     * @return die Storno-Gebühr
     * @throws NoSuchElementException   wenn die zu stornierende Buchung nicht in
     *                                  der Liste "buchungen" ist
     * @throws IllegalArgumentException wenn keine Buchung übergeben wurde, oder wenn die Buchung schon storniert wurde, oder schon vergangen ist.
     */
    public double stornieren(Buchung buchung) {
        double betrag = 0.0;
        if (buchung == null) {
            throw new IllegalArgumentException("Die Buchung enthält eine null-Referenz");
        } else if (buchungen.contains(buchung)) {
            if (buchung.getBuchungsstatus() == Buchungsstatus.AKTIV || buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT) {
                betrag = buchung.stornierenMitGebühr();
                buchung.setBuchungsstatus(Buchungsstatus.STORNIERT);
                buchung.getSitzplatz().freigeben();
            } else {
                throw new IllegalArgumentException(
                        "Sie können eine bereits stornierte oder vergangene Buchung nicht stornieren!");
            }

        }

        else {
            throw new NoSuchElementException(
                    "Die Buchung ist nicht im System vorhanden und kann daher nicht storniert werden!");
        }
        return betrag;
    }

    /**
     * Durchsucht die vorhandenen Buchungen nach solchen, die den angegebenen Flug beinhalten und noch nicht storniert wurden
     * @param flug , der auf nicht stornierte Buchungen überprüft werden soll
     * @return Liste an Buchungen, die die Kriterien erfüllen
     */
    public ArrayList<Buchung> findeRelevanteBuchungen(Flug flug) {
        ArrayList<Buchung> relevanteBuchungen = new ArrayList<>();
        for(int i = 0; i< buchungen.size(); i++) {
            Buchung bTemp = buchungen.get(i);
            if(bTemp.getFlug().equals(flug) && bTemp.getBuchungsstatus() != Buchungsstatus.STORNIERT && bTemp.getBuchungsstatus() != Buchungsstatus.VERGANGEN) {
               relevanteBuchungen.add(bTemp); 
            }
        }
        
        return relevanteBuchungen;
    }

    public double gepaeckAendern(Buchung buchung, int neueAnzahlKoffer) {

    if (buchung == null) {
        throw new IllegalArgumentException("Es wurde keine Buchung angegeben.");
    }

    if (!buchungen.contains(buchung)) {
        throw new NoSuchElementException("Die Buchung ist nicht im System vorhanden.");
    }

    if (buchung.getBuchungsstatus() != Buchungsstatus.AKTIV && buchung.getBuchungsstatus() != Buchungsstatus.UMGEBUCHT) {
        throw new IllegalStateException("Das Gepäck kann bei dieser Buchung nicht mehr geändert werden.");
    }

    if (neueAnzahlKoffer < 0) {
        throw new IllegalArgumentException("Die Anzahl der Koffer darf nicht negativ sein.");
    }

    double alterPreis = buchung.getGezahlterPreis();

    buchung.setAnzahlKoffer(neueAnzahlKoffer);

    double neuerPreis = buchung.getGezahlterPreis();

    return neuerPreis - alterPreis;
}

    /**
     * 
     * @return Anzahl an der getätigten Buchungen
     */
    public int getAnzahlBuchungen() {
        return anzahlBuchungen;
    }

    /**
     * 
     * @return Anzahl der registrierten Passagiere
     */
    public int getAnzahlPassagiere() {
        return anzahlPassagiere;
    }

    /**
     * 
     * @return Liste aller getätigten Buchungen
     */
    public List<Buchung> getBuchungen() {
        return List.copyOf(buchungen);
    }

    public List<Passagier> getPassagiere() {
        return List.copyOf(passagiere);
    }

}