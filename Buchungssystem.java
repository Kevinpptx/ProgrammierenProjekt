import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten Buchungsprozess.
 * Sie dient dazu, die Klassen zu koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 * Sollten wir keine GUI implementieren wird hier auch die Nutzerinteraktion integriert.
 */

public class Buchungssystem {
    public static void main(String[] args) {
        
        Buchungssystem b1 = new Buchungssystem();
        
        Fluggesellschaft air1 = new Fluggesellschaft("Air1", "AA");
        Fluggesellschaft air2 = new Fluggesellschaft("Air2", "AB");
        Fluggesellschaft air3 = new Fluggesellschaft("Air3", "AC");

        Flughafen FRA = new Flughafen("Flughafen Frankfurt am Main", "FRA", "Frankfurt am Main", "Deutschland");
        Flughafen LHR = new Flughafen("Flughafen London Heathrow", "FRA", "London", "Vereinigtes Königreich");
        Flughafen SJO = new Flughafen("Flughafen San Jose Santa Maria", "SJO", "San Jose", "Costa Rica");

        Flugzeug flugzeug1 = new Flugzeug("flugzeug1", "AirbusA320neo", 30, 6, 5);
        Flugzeug flugzeug2 = new Flugzeug("flugzeug2", "AirbusA330neo", 50, 8, 8);
        Flugzeug flugzeug3 = new Flugzeug("flugzeug3", "AirbusA350", 50, 8, 8);
        air1.fuegeFlugzeugHinzu(flugzeug1);
        air1.fuegeFlugzeugHinzu(flugzeug2);
        air1.fuegeFlugzeugHinzu(flugzeug3);

        
 Flug flug1 = new Flug(
            "AA420",
            air1,
            flugzeug1,
            FRA,
            LHR,
            LocalDateTime.of(2026, 7, 14, 10, 10),
            LocalDateTime.of(2026, 7, 14, 17, 40),
            140.0);

    Flug flug2 = new Flug(
            "AA421",
            air1,
            flugzeug2,
            FRA,
            LHR,
            LocalDateTime.of(2026, 7, 15, 10, 10),
            LocalDateTime.of(2026, 7, 15, 17, 40),
            150.0);

    Flug flug3 = new Flug(
            "AA422",
            air2,
            flugzeug3,
            FRA,
            LHR,
            LocalDateTime.of(2026, 7, 16, 10, 10),
            LocalDateTime.of(2026, 7, 16, 12, 40),
            100.0);

    b1.fuegeFlugHinzu(flug1);
    b1.fuegeFlugHinzu(flug2);
    b1.fuegeFlugHinzu(flug3);


        ArrayList<Flug> Suchliste1 = b1.sucheFluegeNachZiel(LHR);
        System.out.println(Suchliste1.toString());
        ArrayList<Flug> Suchliste2 = b1.sucheFluegeNachZiel(SJO);
        System.out.println(Suchliste2.toString());

    }



    /** Liste der Fluggesellschaften, die das Programm kennt */
    public ArrayList<Fluggesellschaft> fluggesellschaften = new ArrayList<>();

    /**Liste der Flüge, die das Programm kennt */
    public ArrayList<Flug> fluege = new ArrayList<>();;

    /**Liste der Buchungen, die schon vorgenommen wurden */
    public ArrayList<Buchung> buchungen = new ArrayList<>();;


    /**Fügt eine Fluggesellschaft in die Liste "fluggesellschaften" hinzu, wenn sie dort noch nicht existieren
     * @param fluggesellschaft
     */
    public void fuegeFluggesellschaftHinzu (Fluggesellschaft fluggesellschaft) {
        
        if (!this.fluggesellschaften.contains(fluggesellschaft)) {
            this.fluggesellschaften.add(fluggesellschaft);
        }
    
       
    }

    /**Fügt einen Flug der Liste "fluege" hinzu, wenn sie dort noch nicht existieren
     * @param flug
     */
    public void fuegeFlugHinzu (Flug flug) {
        if (!fluege.contains(flug)) {
            this.fluege.add(flug);
        }
    }

  

    /**
     * Sucht Fluege, basierend auf dem @param ziel
     * @return Liste, der insgesamt hinzugefügten Flüge
     * @throws NoSuchElementException wenn der gesuchte Flug nicht existiert
     */
    public ArrayList<Flug> sucheFluegeNachZiel (Flughafen ziel) {
        //neue Liste wird erstellt
        ArrayList<Flug> newList = new ArrayList<>();
        
        for (int i = 0; i < fluege.size(); i++) {
            if (fluege.get(i).getZielflughafen() == ziel) {
                newList.add(fluege.get(i));
            } 
        }
        if (!newList.isEmpty()) {
            return newList;
        } else {
            System.out.println("Einen Flug nach " + ziel.getStadt() + " gibt es leider nicht.");
            throw new NoSuchElementException();
        }
        
    }

    /**
     * Sucht einen Flug nach einer Route
     * @param start
     * @param ziel
     * @return Liste der gefundenen Fluege
     * @throws NoSuchElementException wenn der gesuchte Flug nicht existiert
     */
    public List<Flug> sucheFluegeNachRoute (Flughafen start, Flughafen ziel) {
        ArrayList<Flug> newList = new ArrayList<Flug>();
        
        for (int i = 0; i < fluege.size(); i++) {
            if (fluege.get(i).getZielflughafen() == ziel && fluege.get(i).getStartFlughafen() == start) {
                newList.add(fluege.get(i));
            } 
            
        }
        if (!newList.isEmpty()) {
            return newList;
        }
        else {
            System.out.println("Einen Flug von " + start.getStadt() + " nach " + ziel.getStadt() + " gibt es leider nicht.");
            throw new NoSuchElementException();
        }
        
        
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

    

}



