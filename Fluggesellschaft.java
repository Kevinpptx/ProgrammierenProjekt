import java.util.ArrayList;

/**
 * Die Klasse {@code Fluggesellschaft} repräsentiert eine Fluggesellschaft mit einem Namen, einem Airline-Code
 * und einer Flotte von Flugzeugen.
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class Fluggesellschaft {

    private String name;
    private String airlineCode; 
    private ArrayList<Flugzeug> flotte = new ArrayList<>(); 

     /**
     * Erzeugt eine neue Fluggesellschaft mit einem Namen und einem Airline-Code.
     *
     * @param name der Name der Fluggesellschaft
     * @param airlineCode der eindeutige Airline-Code der Fluggesellschaft
     */
    public Fluggesellschaft(String name, String airlineCode) {
        this.name = name;
        this.airlineCode = airlineCode;
    }

    /**
     * Fügt ein Flugzeug zur Flotte der Fluggesellschaft hinzu.
     * Befindet sich das Flugzeug bereits in der Flotte, erfolgt keine Änderung.
     *
     * @param f das hinzuzufügende Flugzeug
     */
    public void fuegeFlugzeugHinzu(Flugzeug f) {
        if(!this.flotte.isEmpty()){
            if(!this.flotte.contains(f)) {
                this.flotte.add(f);
            }
        } else {
            this.flotte.add(f);
        }
    }
        
    /**
     * Entfernt ein Flugzeug aus der Flotte der Fluggesellschaft.
     * Befindet sich das Flugzeug nicht in der Flotte, erfolgt keine Änderung.
     *
     * @param f das zu entfernende Flugzeug
     */
    public void entferneFlugzeug(Flugzeug f) {
        if(!this.flotte.isEmpty())
        {
            if(this.flotte.contains(f)) {
                this.flotte.remove(f);
            } 
        }  
    }

    /**
     * Gibt die Flotte der Fluggesellschaft zurück.
     *
     * @return eine Liste aller Flugzeuge der Fluggesellschaft
     */
    public ArrayList<Flugzeug> getFlotte() {
        return this.flotte;
    }

    /**
     * Gibt den Namen der Fluggesellschaft zurück.
     *
     * @return der Name der Fluggesellschaft
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gibt den Airline-Code der Fluggesellschaft zurück.
     *
     * @return der Airline-Code der Fluggesellschaft
     */
    public String getAirlineCode() {
        return this.airlineCode;
    }

    public boolean besitztFlugzeug(Flugzeug flugzeug) {
        return flotte.contains(flugzeug);
    }

    /**
     * Gibt eine textuelle Beschreibung der Fluggesellschaft zurück.
     * Die Beschreibung enthält den Namen, den Airline-Code und die Flugzeuge
     * der Fluggesellschaft.
     *
     * @return die textuelle Beschreibung der Fluggesellschaft
     */
    @Override
    public String toString() {
        String s; 
        s = "Die Airline " + this.name + " betreibt unter Airline Code: " + this.airlineCode + " die Flugzeuge: " + this.flotte.toString();
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Fluggesellschaft f = (Fluggesellschaft) o;
        return this.airlineCode.equalsIgnoreCase(f.getAirlineCode());
    }
}
