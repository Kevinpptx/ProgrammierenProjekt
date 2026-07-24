import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;


/**
 * Die Klasse {@code Buchungssystem} repräsentiert den kompletten Buchungsprozess.
 * Sie dient dazu, die Klassen zu koordinieren und dafür zu sorgen, dass sie miteinander interagieren können.
 * 
 * 
 * @version 1.1
 * @author Marcel Marxkors
 */

public class Buchungssystem implements Serializable {
    public static void main(String[] args) {
        
        //Beispiele zum ersten Testen
        Buchungssystem b1 = new Buchungssystem();
        
        Fluggesellschaft air1 = new Fluggesellschaft("Air1", "AA");
        Fluggesellschaft air2 = new Fluggesellschaft("Air2", "AB");

        Flughafen FRA = new Flughafen("Flughafen Frankfurt am Main", "FRA", "Frankfurt am Main", "Deutschland");
        Flughafen LHR = new Flughafen("Flughafen London Heathrow", "FRA", "London", "Vereinigtes Königreich");

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

            Passagier p1 = Buchungssystem.initialisierePassagier("Manfred Mann", "manfredmann@gmail.com");
            Passagier p2 = Buchungssystem.initialisierePassagier("Frauke Mann", "fraukemann@gmail.com");

            Buchung bu0 = b1.buchungVornehmen(p1, flug1, "1A", 2, Sitzklasse.BUSINESS);
            Buchung bu1 = b1.buchungVornehmen(p2, flug2, "1A", 3, Sitzklasse.BUSINESS);

            b1.umbuchen(bu0, flug2, "1A", Sitzklasse.BUSINESS);
            b1.stornieren(bu1);

            System.out.println(b1.buchungen.toString());

            //System.out.println(b1.zeigeGepaekÜbersicht(flug1).toString());


            
            //System.out.println(b1.sucheFluegeNachZiel(LHR).toString());


        
        
            

    }



    /** Liste der Fluggesellschaften, die das Programm kennt */
    public ArrayList<Fluggesellschaft> fluggesellschaften;

    /**Liste der Flüge, die das Programm kennt */
    public ArrayList<Flug> fluege;

    /**Liste der Buchungen, die schon vorgenommen wurden */
    public ArrayList<Buchung> buchungen;

    /**Liste der schon registrierten Passagiere */
    public ArrayList<Passagier> passagiere;

    /** Anzahl der insgesamt getätigten Buchungsnummern, erste Idee einer möglichen Grundlage für die Buchungsnummer */
    private static int anzahlBuchungen;

    /**Anzahl der insgesamt registrierten Passagiere, erste Idee einer möglichen Grundlage für die Passagiernummer */
    private static int anzahlPassagiere;

    /**Konstruktor der Klasse, der die temporäre Lösung der Datenspeicherung initialisiert */
    public Buchungssystem() {
        fluggesellschaften = new ArrayList<>();
        fluege = new ArrayList<>();
        buchungen = new ArrayList<>();
        passagiere = new ArrayList<>();
        anzahlBuchungen = 0;
        anzahlPassagiere = 0;
    }


    /**Fügt eine Fluggesellschaft in die Liste "fluggesellschaften" hinzu, wenn sie dort noch nicht existieren.
     * @param fluggesellschaft
     */
    public void fuegeFluggesellschaftHinzu (Fluggesellschaft fluggesellschaft) {
        if (fluggesellschaft == null) {
            throw new IllegalArgumentException("Das übergebene Fluggesellschaft-Objekt hat eine Nullreferenz");
        }
        else if (this.fluggesellschaften.contains(fluggesellschaft)) {
           throw new IllegalArgumentException("Das übergebene Fluggesellschaften-Objekt ist schon in der Liste enthalten"); 
        } else {
            this.fluggesellschaften.add(fluggesellschaft);
        }
    }

    /**Fügt einen Flug der Liste "fluege" hinzu, wenn sie dort noch nicht existieren
     * @param flug
     */
    public void fuegeFlugHinzu (Flug flug) {
        if (flug == null) {
            throw new IllegalArgumentException("Das übergebene Flug-Objekt hat eine Nullreferenz");
        }
        else if (this.fluege.contains(flug)) {
           throw new IllegalArgumentException("Das übergebene Flug-Objekt ist schon in der Liste enthalten"); 
        } else {
            this.fluege.add(flug);
        }
    }

    /**
     * Erstellt ein Objekt vom Typ Passagier und prüft, ob die übergebenenn Parameter gültig sind
     * @param name
     * @param email
     * @return
     */
    public static Passagier initialisierePassagier(String name, String email) {
        if(name != null && email != null) {
            String vorlaeufigePassagierId = "p" + Integer.toString(anzahlPassagiere);
            anzahlPassagiere++;
            Passagier p = new Passagier(vorlaeufigePassagierId, name, email);
            return p;
        } else if (name == null) {
            throw new IllegalArgumentException("Du hast keinen Namen angegeben");
        } else {
            throw new IllegalArgumentException("Du hast keine Mail-Adresse eingegeben");
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
            throw new NoSuchElementException("Einen Flug nach " + ziel.getStadt() + " gibt es leider nicht.");
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
            throw new NoSuchElementException("Einen Flug von " + start.getStadt() + " nach " + ziel.getStadt() + " gibt es leider nicht.\"");
        }
        
        
    }

    /**
     * Sucht einen Flug nach einer Flugnummer, die Teil der vorhandenen Flüge sein sollte.
     * @param Flugnummer
     * @return den gesuchten Flug
     * @throws IllegalArgumentException wenn die Flugnummer nicht vorhanden ist
     */
    public Flug sucheFlugNachNummer(String Flugnummer) {
        for (int i = 0; i < fluege.size(); i++) {
            Flug f = fluege.get(i);
            if (f.getFlugnummer() == Flugnummer) {
                return f;
            }
        }
        throw new NoSuchElementException("Es gibt diese Flugnummer nicht");
    }

   /**
     * Erstellt ein Objekt der Klasse Buchung, wenn die eingegebenen Parameter stimmen
     * @param passagier
     * @param flug
     * @param sitzplatznummer
     * @param anzahlKoffer
     * @return Buchung, die soeben erstellt wurde
     */
    public Buchung buchungVornehmen(Passagier passagier, Flug flug, String sitzplatznummer, int anzahlKoffer, Sitzklasse sitzklasse) {
        GepaeckInformation g = new GepaeckInformation(anzahlKoffer);
        Sitzplatz sitzplatz = new Sitzplatz(sitzplatznummer, sitzklasse);
        Buchung b = new Buchung (passagier, flug, sitzplatz, g);
        b.setBuchungsnummer("bu" + anzahlBuchungen);
        anzahlBuchungen++;
        sitzplatz.setAnzahlKoffer(anzahlKoffer);
        buchungen.add(b);
        return b;
    }

    /**
     * Sucht die bisherigen Buchungen nach einer Buchungsnummer ab
     * @param Buchungsnummer
     * @return die Buchung mit der Buchungsnummer
     * @throws IllegalArgumentException wenn die Buchung nicht oder vorhanden ist
     */
    public Buchung sucheBuchungNachNummer(String buchungsnummer) {
        for (int i = 0; i < buchungen.size(); i++) {
            Buchung b = buchungen.get(i);
            if (b.getBuchungsnummer() == buchungsnummer) {
                return b;
            }
        }
        throw new IllegalArgumentException("Es gibt diese Buchungsnummer nicht");
    }


    /**
     * Bucht einen Passagier um.
     * Die entgegengenommenen Parameter können neu sein oder nicht, das erfolgt in der Methode {@link validiereUmbuchung}
     * @param buchung
     * @param sitzplatznummer
     * @return den Buchungsbetrag
     */
    public double umbuchen(Buchung buchung,Flug flug, String sitzplatznummer, Sitzklasse sitzklasse) {
        boolean umbuchungMoeglich = false;
        double gebuehr = 0.0;
        try {
            umbuchungMoeglich = validiereUmbuchung(buchung, flug, sitzplatznummer, sitzklasse);
        }
        catch (Exception e) {
            throw e;
        }
        if (umbuchungMoeglich) {
            Sitzplatz platz = new Sitzplatz(sitzplatznummer, sitzklasse);
            gebuehr = berechneUmbuchungsgebuehr(buchung, flug, platz);
            buchung.setSitzplatz(platz);
            buchung.setFlug(flug);
            buchung.setBuchungsstatus(Buchungsstatus.UMGEBUCHT);
        }
        
        return gebuehr;
        
    }

    /**
     * Schaut, ob eine Umbuchung möglich ist.
     * Geht dafür bei Buchung auf einen neuen Flug alle Sitzplätze des neuen Fluges durch und schaut, ob die Sitzplatznummer im neuen Flug existiert.
     * Bei Buchung auf neue Sitzplatznummer wird nur geprüft, ob der neue Sitzplatz noch frei ist
     * Schaut vorher, ob die Objekte nicht null sind 
     * Prüft, ob eine Umbuchung schon vorgenommen wurde
     * @param buchung
     * @param neuerFlug
     * @param neueSitzplatznummer
     * @return
     */
    public boolean validiereUmbuchung(Buchung buchung, Flug neuerFlug, String neueSitzplatznummer, Sitzklasse sitzklasse) {
        //wenn die Buchung nicht vorhanden ist
        if(buchung == null) { 
            throw new NoSuchElementException("Es ist keine Buchung angegeben, von der Umgebucht werden soll");
        }

        //wenn beide Buchungsparameter leer sind
        else if (neuerFlug == null && neueSitzplatznummer == null) {
            throw new NoSuchElementException("Beide Buchungsparameter sind leer");
        }

        //wenn schon einmal umgebucht wurde
        else if (buchung.getBuchungsstatus() == Buchungsstatus.UMGEBUCHT) {
            throw new IllegalStateException("Du hast schon eine Umbuchung vorgenommen");
        }

        //wenn keine Sitzklasse angegeben wurde
        else if(sitzklasse == null) {
            throw new NoSuchElementException("Du hast keine Sitzklasse eingegeben");
        }

        //wenn im selben Flug ein anderer Sitzplatz gebucht werden muss
        else if(neuerFlug == buchung.getFlug()) {
            return validiereUmbuchungimSelbenFlug(buchung, neuerFlug, neueSitzplatznummer, sitzklasse);
        }

        //wenn im neuen Flug ein Sitzplatz gebucht werden muss(Prüft, ob die Sitzplatznummer im neuen Flug vorhanden ist und ob der Sitzplatz belegt ist)
        else {
            return validiereUmbuchungimNeuenFlug(neuerFlug, neueSitzplatznummer, sitzklasse);
        }
        
        

    }

    public boolean validiereUmbuchungimSelbenFlug (Buchung buchung, Flug flug, String neueSitzplatznummer, Sitzklasse sitzklasse) {
        Sitzplatz sitz = findeSitzplatz(neueSitzplatznummer, buchung.getFlug());
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);

        try {
            validiereSitzplatz(sitz, sitzklasse, klassenliste);
        } catch (Exception e) {
            throw e;
        }
        return true;

        
    }

    public boolean validiereUmbuchungimNeuenFlug(Flug flug, String neueSitzplatznummer, Sitzklasse sitzklasse){
        Sitzplatz sitz =findeSitzplatz(neueSitzplatznummer, flug);
        List<Sitzplatz> klassenliste = flug.getFreieSitzplaetzeNachKlasse(sitzklasse);
        try {
                    validiereSitzplatz(sitz, sitzklasse, klassenliste);
                } catch (Exception e) {
                    throw e;
                }
                return true;
    }

    public void validiereSitzplatz(Sitzplatz sitz, Sitzklasse sitzklasse, List<Sitzplatz> klassenliste) {
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


    public Sitzplatz findeSitzplatz(String sitzplatznummer, Flug flug) {

    Sitzplatz[][] plan = flug.getSitzplan();

    for (int i = 0; i < plan.length; i++) {
        for (int j = 0; j < plan[i].length; j++) {

            if (plan[i][j].getSitzplatzNummer()
                    .equals(sitzplatznummer)) {

                return plan[i][j];
            }
        }
    }

    return null;
}

    public double berechneUmbuchungsgebuehr (Buchung buchung, Flug flug, Sitzplatz sitzplatz) {
        // Wenn positiv, also der neue Flug mehr kostet, dann auf Gebühr aufschlagen
        double ticketDifferenz = buchung.getFlug().getBasispreis() - flug.getBasispreis();
        double finaleGebühr = 0.0;

        if (ticketDifferenz > 0) {
            // Business Class ggf. aufschlagen
            if (sitzplatz.getSitzklasse() == Sitzklasse.ECONOMY) {
                finaleGebühr = buchung.getUmbuchungsgebuehr() + ticketDifferenz;
            } else if (sitzplatz.getSitzklasse() == Sitzklasse.BUSINESS) {
                finaleGebühr = buchung.getUmbuchungsgebuehr() + ticketDifferenz * buchung.getBusinesspreisfaktor();
            }
        } else
            finaleGebühr = buchung.getUmbuchungsgebuehr();

        return finaleGebühr;
    }

    /**
     * storniert eine vorhandene Buchung
     * @param buchung
     * @return die finale Gebühr, die nach Aufrufen der Methode "stornierenMitGebühr"der Klasse "Buchung" ausgegeben wurde
     */
    public double stornieren(Buchung buchung) {
        double betrag = 0.0;
        if(buchung != null) {
            betrag = buchung.stornierenMitGebühr();
        }
        return betrag;
    }

    /**zeigt alle Fluege, mit ihrer jeweiligen Auslastung
     * 
     */
    public List<String> zeigeAlleFluegeMitAuslastung () {
        ArrayList<String> neueListe = new ArrayList<String>();
        for (int i = 0; i < fluege.size(); i++) {
            neueListe.add("\nFlug: " + fluege.get(i).getFlugnummer() + "| Auslastung: " + fluege.get(i).berechneAuslastung());
        }
        return neueListe;
    }

    /**
     * gibt eine Übersicht darüber zurück, wie viel Gepäck ein Flug schon gebucht hat
     * 
     * 
     */
    public ArrayList<String> zeigeGepaekÜbersicht (Flug flug) {
       ArrayList<String> neueListe = new ArrayList<>();
        for (int i = 0; i < flug.getSitzplan().length; i++) {
            for (int j = 0; j < flug.getSitzplan()[i].length; j++) {
                    neueListe.add("\n Sitzplatz: " + flug.getSitzplan()[i][j].getSitzplatzNummer() + " | Anzahl Koffer: " + flug.getSitzplan()[i][j].getAnzahlKoffer());
                }


        }
        
        return neueListe;
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
     * @return Anzahl der registrierten Buchungen
     */
    public int getAnzahlPassagiere(){
        return anzahlPassagiere;
    }
}



