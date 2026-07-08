public class Passagier {

    private String passagierId;
    private String name;
    private String email; 

    // Konstruktor
    public Passagier(String passagierId, String name, String email) {
        this.passagierId = passagierId;
        this.name = name;
        this.email = email;
    }

    // Getter 
    public String getPassagierId() {
        return passagierId;
    }   

    public String getName() { 
        return name;
    }


    public String getEmail() {
        return email;
    }


    // Setter-Methoden
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String toString() {
        return "Passagier ID: " + passagierId + ", Name: " + name + ", Email: " + email;
    }


}
