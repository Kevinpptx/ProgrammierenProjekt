public class passenger {

    private String passagierid;
    private String name;
    private String kontakt; // bzw Email

    // Konstruktor
    public passenger(String passagierid, String name, String kontakt) {
        this.passagierid = passagierid;
        this.name = name;
        this.kontakt = kontakt;
    }


    // Getter und Setter
    public String getPassagierid() {
        return passagierid;
    }

    public String getName() {
        return name;
    }

    public String getKontakt() {
        return kontakt;
    }


    // Setter-Methoden
    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }
    
    public void tostring() {
        System.out.println("Passagier ID: " + passagierid);
        System.out.println("Name: " + name);
        System.out.println("Kontakt: " + kontakt);
    }
}
