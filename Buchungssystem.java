import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten Buchungsprozess.
 * Sie dient dazu, die Klassen zu koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 * Sollten wir keine GUI implementieren wird hier auch die Nutzerinteraktion integriert.
 */

public class Buchungssystem {
    public static void main(String[] args) {
        
        Fluggesellschaft Huftlansa = new Fluggesellschaft("Huftlansa", "HL");
        Fluggesellschaft AirBnB = new Fluggesellschaft("AirBnB", "AB");
        Fluggesellschaft AirHintertupfingen = new Fluggesellschaft("AirHintertupfingen", "AB");

        Flughafen FRA = new Flughafen("Flughafen Frankfurt am Main", "FRA", "Frankfurt am Main", "Deutschland");
        Flughafen LHR = new Flughafen("Flughafen London Heathrow", "FRA", "London", "Vereinigtes Königreich");
        Flughafen SJO = new Flughafen("Flughafen San Jose Santa Maria", "SJO", "San Jose", "Costa Rica");

        Flugzeug fluggi = new Flugzeug("Fluggi", "AirbusA320neo", 30, 9, 5);

        Flug LH420 = new Flug("LH420", AirHintertupfingen, fluggi, LHR, SJO, LocalDateTime.of(2026, 7, 14, 10, 10), LocalDateTime.of(2026, 7, 14, 17, 40), 140.0);
        fuegeFlugHinzu(LH420);

        System.out.println(Huftlansa.toString());
        System.out.println(AirBnB.toString());
        System.out.println(AirHintertupfingen.toString());
        System.out.println(FRA.toString());
        System.out.println(LHR.toString());
        System.out.println(SJO.toString());
        System.out.println(fluggi.toString());
        System.out.println(LH420.toString());


    }



    /** Liste der Fluggesellschaften, die das Programm kennt */
    public static ArrayList<Fluggesellschaft> fluggesellschaften;

    /**Liste der Flüge, die das Programm kennt */
    public static ArrayList<Flug> fluege;

    /**Liste der Buchungen, die schon vorgenommen wurden */
    public static ArrayList<Buchung> buchungen;

    /**Liste der eingetragenen Flughaefen */
    public static ArrayList<Flughafen> flughaefen;

    /**Liste der eingetragenen Flugzeuge */
    public static ArrayList<Flugzeug> flugzeuge;

    /**Fügt eine Fluggesellschaft in die Liste "fluggesellschaften" hinzu, wenn sie dort noch nicht existieren
     * @param fluggesellschaft
     */
    public static void fuegeFluggesellschaftHinzu (Fluggesellschaft fluggesellschaft) {
        if (fluggesellschaften.contains(fluggesellschaft) == false) {
            Buchungssystem.fluggesellschaften.add(fluggesellschaft);
        }
    }

    /**Fügt einen Flug der Liste "fluege" hinzu, wenn sie dort noch nicht existieren
     * @param flug
     */
    public static void fuegeFlugHinzu (Flug flug) {
        if (fluege.contains(flug) == false) {
            Buchungssystem.fluege.add(flug);
        }
    }

    /**
     * Fügt einen Flughafen der Liste "flughaefen", wenn sie dort noch nicht existieren
     * @param flughafen
     */
    public static void fuegeFlughafenHinzu (Flughafen flughafen) {
        if(flughaefen.contains(flughafen) == false) {
            Buchungssystem.flughaefen.add(flughafen);
        }    
    }

   /**
    * Fügt ein Flugzeug der Liste "flugzeuge", wenn sie dort noch nicht existieren
    * @param flugzeug
    */ 
    public static void fuegeFlugzeugHinzu (Flugzeug flugzeug) {
        if(flugzeuge.contains(flugzeug) == false) {
            Buchungssystem.flugzeuge.add(flugzeug);
        }    
    }

    /**
     * Sucht Fluege, basierend auf dem @param ziel
     * @return Liste, der insgesamt hinzugefügten Flüge
     */
    public ArrayList<Flug> sucheFluegeNachZiel (Flughafen ziel) {
        //neue Liste wird erstellt
        ArrayList<Flug> newList = new ArrayList<>();
        
        for (int i = 0; i < fluege.size(); i++) {
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
        ArrayList<Flug> newList = new ArrayList<Flug>();
        
        for (int i = 0; i < fluege.size(); i++) {
            if (fluege.get(i).getZielflughafen() == ziel && fluege.get(i).getStartFlughafen() == start) {
                newList.add(fluege.get(i));
            } 
            else{
                System.out.println("Sorry, diese Route gibt es leider nicht");
            }
        }
        return newList;
        
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



