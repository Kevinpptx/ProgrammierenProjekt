
public class Main {

    public static void main(String[] args) {

        DatenHandler datenHandler = new DatenHandler();

        Anwendungsdaten anwendungsdaten = datenHandler.initialisiereAnwendungsdaten();

        Manager manager = new Manager(datenHandler, anwendungsdaten);

        manager.start();
    }
}
