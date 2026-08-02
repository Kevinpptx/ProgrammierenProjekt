import java.io.Serializable;

/**
 * Die Klasse Passagier speichert Informationen über einen Passagier.
 */
public class Passagier implements Serializable{

    /**
     * Versionsnummer zur Prüfung der Kompatibilität bei der Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /** Die ID des Passagiers. */
    private String passagierId;
    /** Der Name des Passagiers. */
    private String name;
    /** Die E-Mail-Adresse des Passagiers. */
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
    * @return den Namen des Passagiers
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
