import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Die Klasse {@code Flug} repräsentiert einen konkreten Flug einer
 * Fluggesellschaft zwischen einem Start- und einem Zielflughafen.
 *
 * Ein Flug besitzt eine Flugnummer, eine Fluggesellschaft, ein eingesetztes
 * Flugzeug, einen Start- und Zielflughafen, eine Abflug- und Ankunftszeit
 * sowie einen Basispreis.
 *
 * Zusätzlich besitzt jeder Flug einen eigenen Sitzplan. Dieser wird bei der
 * Erstellung des Fluges anhand der Sitzplatzvorlage des eingesetzten Flugzeugs
 * initialisiert. Dadurch kann die Sitzplatzbelegung für jeden Flug unabhängig
 * verwaltet werden.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Flug implements Serializable {

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /** Die eindeutige Flugnummer des Fluges */
    private String flugnummer;

    /** Die Fluggesellschaft, die den Flug durchführt */
    private Fluggesellschaft fluggesellschaft;

    /** Das für den Flug eingesetzte Flugzeug */
    private Flugzeug flugzeug;

    /** Der Flughafen, von dem der Flug startet */
    private Flughafen startFlughafen; 

    /** Der Flughafen, an dem der Flug endet */
    private Flughafen zielFlughafen;

    /** Der geplante Zeitpunkt des Abflugs */
    private LocalDateTime abflugzeit;

    /** Der geplante Zeitpunkt der Ankunft */
    private LocalDateTime ankunftszeit;

    /** Der Basispreis des Fluges */
    private double basispreis;

    /**
     * Der individuelle Sitzplan dieses Fluges.
     * Jeder Sitzplatz besitzt einen eigenen Belegungsstatus.
     */
    private Sitzplatz[][] sitzplan;

    
    /**
     * Erzeugt einen neuen Flug mit den angegebenen Flugdaten.
     *
     * Beim Erstellen des Fluges wird ein eigener Sitzplan anhand der
     * Sitzplatzvorlage des übergebenen Flugzeugs initialisiert.
     *
     * Der Basispreis darf nicht negativ sein und die Ankunftszeit darf
     * zeitlich nicht vor der Abflugzeit liegen.
     *
     * @param flugnummer die Flugnummer des Fluges
     * @param fluggesellschaft die Fluggesellschaft, die den Flug durchführt
     * @param flugzeug das für den Flug eingesetzte Flugzeug
     * @param startFlughafen der Startflughafen des Fluges
     * @param zielFlughafen der Zielflughafen des Fluges
     * @param abflugzeit der geplante Zeitpunkt des Abflugs
     * @param ankunftszeit der geplante Zeitpunkt der Ankunft
     * @param basispreis der Basispreis des Fluges
     *
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
                double basispreis) {

        if (flugnummer == null || flugnummer.isBlank()) {
            throw new IllegalArgumentException("Die Flugnummer darf nicht leer sein.");
        }

        // Prüft, ob der angegebene Basispreis gültig ist
        if (basispreis < 0) {
            throw new IllegalArgumentException("Der Basispreis darf nicht negativ sein.");
        }

        // Verhindert, dass ein Flug vor seinem Abflug ankommt
        if (!ankunftszeit.isAfter(abflugzeit)) {
            throw new IllegalArgumentException("Die Ankunftszeit darf nicht vor der Abflugzeit liegen.");
        }

        if (fluggesellschaft == null
                || flugzeug == null
                || startFlughafen == null
                || zielFlughafen == null
                || abflugzeit == null
                || ankunftszeit == null) {

            throw new IllegalArgumentException("Die übergebenen Flugdaten dürfen nicht null sein.");
        }


        this.flugnummer = flugnummer;
        this.fluggesellschaft = fluggesellschaft;
        this.flugzeug = flugzeug;
        this.startFlughafen = startFlughafen;
        this.zielFlughafen = zielFlughafen;
        this.abflugzeit = abflugzeit;
        this.ankunftszeit = ankunftszeit;
        this.basispreis = basispreis;

        // Erstellt einen eigenen Sitzplan für diesen konkreten Flug
        this.initialisiereSitzplan(this.flugzeug.getSitzplaetzeVorlage());
    }

    /**
     * Initialisiert den Sitzplan des Fluges anhand einer Sitzplatzvorlage.
     *
     * Für jeden Sitzplatz der Vorlage wird ein neues {@code Sitzplatz}-Objekt
     * erzeugt. Dadurch besitzt jeder Flug einen eigenen unabhängigen Sitzplan
     * und Änderungen an der Sitzplatzbelegung wirken sich nicht auf andere
     * Flüge oder auf die ursprüngliche Sitzplatzvorlage des Flugzeugs aus.
     *
     * @param vorlage die Sitzplatzvorlage des eingesetzten Flugzeugs
     */
    private void initialisiereSitzplan(Sitzplatz[][] vorlage) {
        // Erstellt zunächst die äußere Arraystruktur entsprechend der Vorlage
        sitzplan = new Sitzplatz[vorlage.length][];

        for (int i = 0; i < vorlage.length; i++) {

            // Jede Reihe erhält die gleiche Anzahl an Sitzplätzen wie in der Vorlage
            sitzplan[i] = new Sitzplatz[vorlage[i].length];

            for (int j = 0; j < vorlage[i].length; j++){

                Sitzplatz original = vorlage[i][j];

                // Erstellt eine unabhängige Kopie des jeweiligen Sitzplatzes
                sitzplan[i][j] = new Sitzplatz(original.getSitzplatzNummer(), original.getSitzklasse());
            }
        }
    }

     /**
     * Belegt einen Sitzplatz anhand seiner Reihe und seiner Position innerhalb
     * dieser Reihe.
     *
     * Die Nummerierung beginnt für den Benutzer bei {@code 1}. Intern werden
     * die Arrayindizes entsprechend um {@code 1} reduziert.
     *
     * Ist der gewünschte Sitzplatz bereits belegt oder existiert nicht,
     * wird eine entsprechende Meldung auf der Konsole ausgegeben.
     *
     * @param reihe die Nummer der gewünschten Sitzreihe
     * @param nummer die Position des gewünschten Sitzplatzes innerhalb der Reihe
     * @throws IllegalArgumentException wenn der Sitzplatz nicht existiert
     * @throws IllegalStateException wenn der Sitzplatz bereits belegt ist
     */
    public void belegeSitzplatz(int reihe, int nummer) {

        // Prüft, ob die angegebene Reihe und Sitzplatznummer im Sitzplan existieren
        if(reihe >= 1 && reihe -1 < this.sitzplan.length 
        && nummer >= 1 && nummer -1 < this.sitzplan[reihe-1].length) {

            Sitzplatz sitzplatz = this.sitzplan[reihe-1][nummer-1];

            
            // Ein Sitzplatz kann nur belegt werden, wenn er noch frei ist
            if(sitzplatz.getIstFrei()) {
                sitzplatz.belegen();
            } else {
                throw new IllegalArgumentException("Der Sitzplatz " + sitzplatz.getSitzplatzNummer() + " ist bereits belegt.");
            }

        } else {
            throw new IllegalStateException("Sitzplatz in Reihe " + reihe + " mit Nummer " + nummer + " existiert nicht. Bitte gültigen Sitzplatz von Reihe 1 bis " + this.sitzplan.length + " und Nummer von 1 bis " +  this.sitzplan[0].length + " wählen.");
        }
    }

    /**
     * Belegt einen Sitzplatz anhand seiner Reihe und seiner Position innerhalb
     * dieser Reihe.
     *
     * Die Nummerierung beginnt für den Benutzer bei {@code 1}. Intern werden
     * die Arrayindizes entsprechend um {@code 1} reduziert.
     *
     * Ist der gewünschte Sitzplatz bereits belegt oder existiert nicht,
     * wird eine entsprechende Meldung auf der Konsole ausgegeben.
     *
     * @param sitzplatz der zu belegende Sitzplatz
     * @throws IllegalStateException wenn der Sitzplatz bereits belegt ist
     */
    public void belegeSitzplatz(Sitzplatz sitzplatz) {
        // Ein Sitzplatz kann nur belegt werden, wenn er noch frei ist
        if(sitzplatz.getIstFrei()) {
            sitzplatz.belegen();
        } else {
            throw new IllegalStateException("Der Sitzplatz " + sitzplatz.getSitzplatzNummer() + " ist bereits belegt.");
        }

    }

    /**
     * Ermittelt alle aktuell freien Sitzplätze des Fluges.
     *
     * @return eine Liste mit allen freien Sitzplätzen
     */
    public List<Sitzplatz> getFreieSitzplaetze() {
        List<Sitzplatz> freieSitzplaetze = new ArrayList<>();
        
        // Durchläuft den vollständigen Sitzplan
        for (int i = 0; i < sitzplan.length; i++) {
            for (int j = 0; j < this.sitzplan[i].length; j++) {

                // Nur freie Sitzplätze werden der Ergebnisliste hinzugefügt
                if (this.sitzplan[i][j].getIstFrei()) {
                    freieSitzplaetze.add(sitzplan[i][j]);
                }
            }
        }
        return freieSitzplaetze;        
    }
       
    /**
     * Ermittelt alle freien Sitzplätze einer bestimmten Sitzklasse.
     *
     * Ein Sitzplatz wird nur zurückgegeben, wenn er sowohl frei ist als auch
     * der angegebenen Sitzklasse entspricht.
     *
     * @param sitzklasse die Sitzklasse, nach der gefiltert werden soll
     * @return eine Liste mit allen freien Sitzplätzen der angegebenen Sitzklasse
     */
    public List<Sitzplatz> getFreieSitzplaetzeNachKlasse(Sitzklasse sitzklasse){
        List<Sitzplatz> freieSitzplaetze = new ArrayList<>();
        
        // Durchläuft den vollständigen Sitzplan
        for (int i = 0; i < sitzplan.length; i++) {
            for (int j = 0; j < this.sitzplan[i].length; j++) {

                // Prüft gleichzeitig den Belegungsstatus und die Sitzklasse
                if (this.sitzplan[i][j].getIstFrei() && this.sitzplan[i][j].getSitzklasse() == sitzklasse) {
                    freieSitzplaetze.add(sitzplan[i][j]);
                }
            }
        }
        return freieSitzplaetze; 
    }

    /**
     * Prüft, ob der Flug vollständig ausgebucht ist.
     *
     * Ein Flug gilt als ausgebucht, wenn keine freien Sitzplätze mehr
     * vorhanden sind.
     *
     * @return {@code true}, wenn keine freien Sitzplätze mehr vorhanden sind,
     *         sonst {@code false}
     */
    public boolean istAusgebucht() {
        return this.getFreieSitzplaetze().isEmpty();
    }

    /**
     * Berechnet die prozentuale Auslastung des Fluges.
     *
     * Dazu wird die Anzahl aller belegten Sitzplätze durch die Gesamtzahl
     * aller vorhandenen Sitzplätze geteilt und anschließend mit {@code 100}
     * multipliziert.
     *
     * @return die Auslastung des Fluges in Prozent
     */
    public double berechneAuslastung() {
        int anzahlBelegt = 0;
        int anzahlGesamt = 0;

        // Durchläuft alle Sitzplätze und zählt Gesamtanzahl und belegte Plätze
        for (int i = 0; i < this.sitzplan.length; i++) {
            for (int j = 0; j < this.sitzplan[i].length; j++) {
                anzahlGesamt++;
                if (!this.sitzplan[i][j].getIstFrei()) {
                    anzahlBelegt++;
                }
            }
        }

        // Rückgabe rundet auf 2 Nachkommastellen und gibt Wert in Prozent aus
        return Math.round(((double) anzahlBelegt / anzahlGesamt * 100.0) * 100.0) / 100.0;
    }

    /**
     * Gibt den aktuellen Sitzplan des Fluges auf der Konsole aus.
     *
     * Für jeden Sitzplatz wird zunächst die Sitzplatznummer und anschließend
     * der aktuelle Belegungsstatus dargestellt.
     *
     * Ein freier Sitzplatz wird durch {@code [ ]} und ein belegter Sitzplatz
     * durch {@code [X]} gekennzeichnet.
     *
     * In der Mitte jeder Reihe wird ein Mittelgang dargestellt. Zusätzlich
     * wird beim Wechsel zwischen verschiedenen Sitzklassen eine horizontale
     * Trennlinie ausgegeben.
     */
    public void zeigeSitzplan() {

        for (int i = 0; i < this.sitzplan.length; i++) {
            
            /*
             * Prüft ab der zweiten Reihe, ob sich die Sitzklasse gegenüber
             * der vorherigen Reihe verändert hat.
             */
            if(i>0 && this.sitzplan[i-1][0].getSitzklasse() != this.sitzplan[i][0].getSitzklasse()) {

                // Horizontale Trennlinie zwischen zwei Sitzklassen
                for (int j = 0; j < this.sitzplan[i].length; j++) {
                    System.out.print("-----");
                }

                System.out.println();
                System.out.println();
            }


            // Gibt zunächst die Sitzplatznummern der aktuellen Reihe aus
            for (int j = 0; j < this.sitzplan[i].length ; j++) {
              
                // Fügt in der Mitte der Sitzreihe einen Gang ein
                if(j == this.sitzplan[i].length / 2){
                    System.out.print("|  ");
                }

                System.out.printf("%-5s", this.sitzplan[i][j].getSitzplatzNummer());
            } 

            System.out.println();


            // Gibt anschließend den Belegungsstatus der Sitzplätze aus
            for (int j = 0; j < this.sitzplan[i].length; j++) {
                
                // Fügt auch in der Statusanzeige den Mittelgang ein
                if(j == this.sitzplan[i].length/2) {
                    System.out.print("|  ");
                }

                if(this.sitzplan[i][j].getIstFrei()) {
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
     * Findet einen Sitzplatz in einem Flug und gibt ihn zurück
     * War ursprünglich in der Methode "Buchungssystem", ist aber hier sinnvoller
     * @param sitzplatznummer
     * @return
     */
    public Sitzplatz findeSitzplatz(String sitzplatznummer) {

        for (int i = 0; i < sitzplan.length; i++) {
            for (int j = 0; j < sitzplan[i].length; j++) {

                if (sitzplan[i][j].getSitzplatzNummer()
                        .equals(sitzplatznummer)) {

                    return sitzplan[i][j];
                }
            }
        }

        return null;
    }

        /**
     * Prüft, ob ein Sitzplatz vorhanden oder belegt ist oder nicht in der gegebenen Sitzklasse existiert
     * @param sitz : zu überprüfender Sitzplatz
     * @param sitzklasse : Sitzklasse, die zu dem Sitzplatz gehören soll
     * @param klassenliste : Liste mit Sitzplätzen, die die angegebene Sitzklasse haben
     */
    public void validiereSitzplatz(Sitzplatz sitz, Sitzklasse sitzklasse, List<Sitzplatz> klassenliste) {
        //man muss auf null prüfen, weil die Methode "findeSitzplatz" null zurückgeben kann.
        if (sitz == null) {
            throw new NoSuchElementException("Sitzplatz nicht vorhanden.");
        }

        else if (!sitz.getIstFrei()) {
            throw new IllegalArgumentException("Sitzplatz bereits belegt.");
        }
        else if (sitz.getSitzklasse() != sitzklasse && !klassenliste.contains(sitz)) {
            throw new IllegalArgumentException("Der Sitzplatz ist nicht in der richtigen Sitzklasse");
        } 
    }

    /**
     * Gibt eine Übersicht darüber zurück, wie viel Gepäck ein Flug schon gebucht
     * hat
     * 
     * @return Liste der Sitzplaetze mit der Anzahl der Koffer der jeweiligen
     *         Buchung als Strings
     */
    public ArrayList<String> zeigeGepackUebersicht() {
        ArrayList<String> neueListe = new ArrayList<>();
        for (int i = 0; i < sitzplan.length; i++) {
            for (int j = 0; j < sitzplan[i].length; j++) {
                if (sitzplan[i][j].getIstFrei()) continue;
                neueListe.add("\n Sitzplatz: " + sitzplan[i][j].getSitzplatzNummer() + " | Anzahl Koffer: "
                        + sitzplan[i][j].getBuchung().getGepaeckinformation().getAnzahlKoffer());
            }

        }

        return neueListe;
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

    public Sitzplatz[][] getSitzplan() {
        return sitzplan;
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
     *
     * Die Beschreibung enthält die Fluggesellschaft, die vollständige
     * Flugnummer, den Start- und Zielflughafen, die Abflug- und Ankunftszeit,
     * das eingesetzte Flugzeug sowie die aktuelle Auslastung.
     *
     * @return eine textuelle Beschreibung des Fluges
     */
    @Override
    public String toString() {
        return this.fluggesellschaft.getName()  
            + " Flug " + this.flugnummer
            + " von " + this.startFlughafen.getIataCode()
            + " nach " + this.zielFlughafen.getIataCode()
            + ", Abflug: " + this.abflugzeit
            + ", Ankunft: " + this.ankunftszeit
            + ", Flugzeug: " + this.flugzeug.getModell() 
            + " (" + this.flugzeug.getCode() + ")"
            + " ist zu " + this.berechneAuslastung() 
            + "% ausgelastet.";
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flug f = (Flug) o;
        return this.flugnummer.equalsIgnoreCase(f.getFlugnummer()) 
                && this.abflugzeit.toLocalDate().equals(f.getAbflugszeit().toLocalDate());
    }
}
