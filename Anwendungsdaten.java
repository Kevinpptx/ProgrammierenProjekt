
import java.io.Serializable;

public class Anwendungsdaten implements Serializable{

    private final Verwaltungssystem verwaltungssystem;
    private final Buchungssystem buchungssystem; 

    public Anwendungsdaten() {
        this.verwaltungssystem = new Verwaltungssystem();
        this.buchungssystem = new Buchungssystem();
    }

    public Verwaltungssystem getVerwaltungssystem() {
        return this.verwaltungssystem;
    }

    public Buchungssystem getBuchungssystem() {
        return this.buchungssystem;
    }
}
