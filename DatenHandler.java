
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Die Klasse {@code DatenHandler}
 * Verwaltet das Speichern und Laden des Buchungssystems.
 * Das Buchungssystem wird mithilfe der Java-Serialisierung
 * im Ordner {@code data} gespeichert.
 * 
 * @author Cedric Beckmann
 * @version 1.0
 */
public class DatenHandler {

    /**
     * Pfad zur Datei, in der das Buchungssystem gespeichert wird.
     */
    private static final Path DATEI_PFAD = Path.of(System.getProperty("user.dir"), "data", "Buchungssystem.ser");

    /**
     * Erzeugt einen neuen DatenHandler.
     */
    public DatenHandler() {

    }
    /**
     * Serialisiert das übergebene Buchungssystem und speichert es
     * in der dafür vorgesehenen Datei.
     * Falls der Ordner {@code data} noch nicht existiert,
     * wird dieser automatisch erzeugt.
     *
     * @param buchungssystem das zu speichernde Buchungssystem
     */
    public void speichere(Buchungssystem buchungssystem) {

        try {
            // Erstellt den Ordner "data", falls er noch nicht existiert
            Files.createDirectories(DATEI_PFAD.getParent());

            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(DATEI_PFAD))) {
  
                oos.writeObject(buchungssystem);

                System.out.println("Buchungssystem wurde gespeichert");
            }

        } catch (IOException e) {
            System.err.println("Das Buchungssystem konnte nicht gespeichert werden");
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert das Buchungssystem beim Programmstart.
     * Falls eine Speicherdatei vorhanden ist, wird das darin
     * gespeicherte Buchungssystem geladen.
     * Wenn keine gültige Speicherdatei vorhanden ist,
     * wird ein neues Buchungssystem erzeugt.
     *
     * @return das geladene oder neu erzeugte Buchungssystem
     */
    public Buchungssystem initialisiereBuchungssystem() {

        if (Files.notExists(DATEI_PFAD)) {

            System.out.println("Es konnte kein gespeichertes Buchungssystem gefunden werden. Ein neues Buchungssystem wird erzeugt.");
            return new Buchungssystem();

        } else {
            try {

                return leseAusDatei();

            } catch (IOException | ClassNotFoundException | ClassCastException e) {

                System.err.println("Gespeichertes Buchungssystem konnte nicht geladen werden. Ein neues Buchungssystem wird erzeugt.");

                e.printStackTrace();

                return new Buchungssystem();

            }
        }
    }

    /**
     * Liest ein serialisiertes Buchungssystem aus der Speicherdatei.
     *
     * @return das aus der Datei gelesene Buchungssystem
     * @throws IOException wenn beim Lesen der Datei ein Fehler auftritt
     * @throws ClassNotFoundException wenn die Klasse des gespeicherten
     *         Objekts nicht gefunden werden kann
     * @throws ClassCastException wenn die Datei kein Objekt der Klasse
     *         {@link Buchungssystem} enthält
     */
    private Buchungssystem leseAusDatei() throws IOException, ClassNotFoundException {

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(DATEI_PFAD))) {

            Object objekt = ois.readObject();

            if ((objekt instanceof Buchungssystem)) {
                return (Buchungssystem) objekt;
            } else { 
                throw new ClassCastException("Die gespeicherte Datei enthält kein Buchungssystem");
            }
        }
    }
}

