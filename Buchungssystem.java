import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten Buchungsprozess.
 * Sie dient dazu, die Klassen zu koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 * Sollten wir keine GUI implementieren wird hier auch die Nutzerinteraktion integriert.
 */
public class Buchungssystem {
    /** Liste der Fluggesellschaften, die das Programm kennt */
    private ArrayList<Fluggesellschaft> fluggesellschaften;

    /**Liste der Flüge, die das Programm kennt */
    private ArrayList<Flug> fluege;

    /**Liste der Buchungen, die schon vorgenommen wurden */
    private ArrayList<Buchung> buchungen;

    /**Liste der eingetragenen Flughaefen */
    private ArrayList<Flughafen> flughaefen;

    /**Fügt eine Fluggesellschaft in die Liste "fluggesellschaften" hinzu. */
    public void fuegeFluggesellschaftHinzu (Fluggesellschaft fluggesellschaft) {
        this.fluggesellschaften.add(fluggesellschaft);
    }

    /**Fügt einen Flug der Liste "fluege" hinzu. */
    public void fuegeFlugHinzu (Flug flug) {
        this.fluege.add(flug);
    }

    /**
     * Sucht Fluege, basierend auf dem @param ziel
     * @return Liste, der insgesamt hinzugefügten Flüge
     */
    public ArrayList<Flug> sucheFluegeNachZiel (Flughafen ziel) {
        //neue Liste wird erstellt
        ArrayList<Flug> newList = new ArrayList<>();
        
        for (int i = 0; i < fluege.size(), i++) {
            if (fluege.get(i).getZielflughafen() == ziel) {
                newList.add(fluege.get(i));
            } 
        }
        return newList;
    }

    /**
     * Sucht einen Flug nach einer Route
     * @param start
     * @param ziel
     * @return
     */
    public List<Flug> sucheFluegeNachRoute (Flughafen start, Flughafen ziel) {
        ArrayList<Flug> newList = new ArrayList<>();
        
        for (int i = 0; i < fluege.size(), i++) {
            if (fluege.get(i).getZielflughafen() == ziel && fluege.get(i).getStartFlughafen() == start) {
                newList.add(fluege.get(i));
            } 
        }
        return newList;
        return null;
    }

    /**
     * Sucht einen Flug nach einer Flugnummer, die Teil der vorhandenen Flüge sein sollte.
     * @param Flugnummer
     * @return den gesuchten Flug
     * @throws IllegalArgumentException wenn die Flugnummer nicht oder mehrmals vorhanden ist
     */
    public Flug sucheFlugNachNummer(String Flugnummer) {
        return null;
    }

    /**
     * Sucht die bisherigen Buchungen nach einer Buchungsnummer ab
     * @param Buchungsnummer
     * @return die Buchung mit der Buchungsnummer
     * @throws IllegalArgumentException wenn die Buchung nicht oder mehrmals vorhanden ist
     */
    public Buchung sucheBuchungNachNummer(String Buchungsnummer) {
        return null;
    }

    /**
     * Erstellt ein Objekt der Klasse Buchung, wenn die eingegebenen Parameter stimmen
     * @param passagier
     * @param flug
     * @param sitzplatznummer
     * @param anzahlKoffer
     * @return Buchung, die soeben erstellt wurde
     */
    public Buchung buchungVornehmen(Passagier passagier, Flug flug, String sitzplatznummer, int anzahlKoffer) {
        return null;
    }

    /**
     * Bucht einen bereits bestehenden Flug um
     * @param buchung
     * @param neuerFlug
     * @param neueSitzplatznummer
     * @return den Betrag, den nach dem Aufrufen der Methode "umbuchenMitGebühr" der Klasse "Buchung"
     */
    public double umbuchen(Buchung buchung, Flug neuerFlug, String neueSitzplatznummer) {
        return 0.0;
    }

    /**
     * storniert eine vorhandene Buchung
     * @param buchung
     * @return die finale Gebühr, die nach Aufrufen der Methode "stornierenMitGebühr"der Klasse "Buchung" ausgegeben wurde
     */
    public double stornieren(Buchung buchung) {
        return 0.0;
    }

    /**zeigt alle Fluege, mit ihrer jeweiligen Auslastung
     * 
     */
    public void zeigeAlleFluegeMitAuslastung () {
        
    }

    /**
     * gibt eine Übersicht darüber zurück, wie viel Gepäck ein Flug schon gebucht hat
     * @param flug
     */
    public void zeigeGepaekÜbersicht (Flug flug) {

    }

    public static void Main(String[] args) {

    }

}

