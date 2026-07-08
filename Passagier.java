/**
 * Die Klasse Passagier speichert Informationen über einen Passagier.
 */
public class Passagier {

   
    private String passagierId;
    private String name;
    private String email; 


    /**
     * Konstruktor für die Klasse Passagier. 
     * @param passagierId
     * @param name
     * @param email
     */
    // Konstruktor
    public Passagier(String passagierId, String name, String email) {
        this.passagierId = passagierId;
        this.name = name;
        this.email = email;
    }

    // Getter 

    /**
    *Gibt die Passagier-ID zurück.
    *
    * @return die Passagier-ID
     */
    public String getPassagierId() {
        return passagierId;
    } 
    
    

     /**
    *Gibt den Namen des Passagiers zurück.
    *
    * @return der Name des Passagiers
     */

    public String getName() { 
        return name;
    }

     /**
    *Gibt die E-Mail-Adresse des Passagiers zurück.
    *
    * @return die E-Mail-Adresse des Passagiers
     */
    public String getEmail() {
        return email;
    }


    /**
     * Setzt die E-Mail-Adresse des Passagiers.
     * @param email
     */
    // Setter-Methoden
    public void setEmail(String email) {
        this.email = email;
    }
    

    /**
     * Gibt eine String-Darstellung des Passagiers zurück.
     * @return eine String-Darstellung des Passagiers
     */
    public String toString() {
        return "Passagier ID: " + passagierId + ", Name: " + name + ", Email: " + email;
    }


}
