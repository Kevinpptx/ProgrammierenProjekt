
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

/**
 * Verwaltet das Speichern und Laden der Anwendungsdaten.
 * Die Anwendungsdaten werden mithilfe der Java-Serialisierung
 * im Ordner {@code data} gespeichert.
 *
 * <p>Falls beim Programmstart keine gültige Speicherdatei vorhanden ist,
 * werden neue Anwendungsdaten erzeugt.</p>
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class DatenHandler {

    /**
     * Pfad zu der Datei, in der die serialisierten Anwendungsdaten
     * gespeichert werden.
     */
    private static final Path DATEI_PFAD = Path.of(System.getProperty("user.dir"), "data", "Anwendungsdaten.ser");

    /**
     * Erzeugt einen neuen DatenHandler.
     */
    public DatenHandler() {

    }
    
    /**
     * Serialisiert die übergebenen Anwendungsdaten und speichert sie
     * in der dafür vorgesehenen Datei.
     *
     * <p>Falls der Ordner {@code data} noch nicht existiert,
     * wird dieser automatisch erzeugt.</p>
     *
     * @param anwendungsdaten die zu speichernden Anwendungsdaten
     * @throws IllegalArgumentException wenn die Anwendungsdaten
     *         {@code null} sind
     */
    public void speichere(Anwendungsdaten anwendungsdaten) {

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten dürfen nicht null sein.");
        }

        try {
            // Erstellt den Ordner "data", falls er noch nicht existiert
            Files.createDirectories(DATEI_PFAD.getParent());

            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(DATEI_PFAD))) {
  
                oos.writeObject(anwendungsdaten);

                System.out.println("Die Anwendungsdaten wurden erfolgreich gespeichert.");
            }

        } catch (IOException e) {
            System.err.println("Fehler! Die Anwendungsdaten konnten nicht gespeichert werden.");
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert die Anwendungsdaten beim Programmstart.
     *
     * <p>Falls eine Speicherdatei vorhanden ist, werden die darin
     * gespeicherten Anwendungsdaten geladen. Wenn keine gültige
     * Speicherdatei vorhanden ist, werden neue Anwendungsdaten erzeugt.</p>
     *
     * @return die geladenen oder neu erzeugten Anwendungsdaten
     */
    public Anwendungsdaten initialisiereAnwendungsdaten() {

        if (Files.notExists(DATEI_PFAD)) {

            System.out.println("Es konnten keine gespeicherten Anwendungsdaten gefunden werden. Neue Anwendungsdaten werden erzeugt.");
            return new Anwendungsdaten();

        } else {
            try {

                return leseAusDatei();

            } catch (IOException | ClassNotFoundException | ClassCastException e) {

                System.err.println("Gespeicherte Anwendungsdaten konnten nicht geladen werden. Neue Anwendungsdaten werden erzeugt.");

                e.printStackTrace();

                return new Anwendungsdaten();

            }
        }
    }

    /**
     * Liest serialisierte Anwendungsdaten aus der Speicherdatei.
     *
     * @return die aus der Datei gelesenen Anwendungsdaten
     * @throws IOException wenn beim Lesen der Datei ein Fehler auftritt
     * @throws ClassNotFoundException wenn die Klasse des gespeicherten
     *         Objekts nicht gefunden werden kann
     * @throws ClassCastException wenn die Datei kein Objekt der Klasse
     *         {@link Anwendungsdaten} enthält
     */
    private Anwendungsdaten leseAusDatei() throws IOException, ClassNotFoundException {

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(DATEI_PFAD))) {

            Object objekt = ois.readObject();

            if ((objekt instanceof Anwendungsdaten)) {
                return (Anwendungsdaten) objekt;
            } else { 
                throw new ClassCastException("Die gespeicherte Datei enthält keine Anwendungsdaten");
            }
        }
    }

    /**
     * Überprüft, ob es "alte" Flüge gibt, die in der Vergangenheit liegen.
     * Diese kann man nicht mehr buchen.
     * Falls ja, werden diese also aus dem Speicher gelöscht.
     * 
     * @param anwendungsdaten die zu speichernden Anwendungsdaten
     * @throws IllegalArgumentException wenn die Anwendungsdaten
     *         {@code null} sind
     */
    public void alteFluegeLoeschen(Anwendungsdaten anwendungsdaten) {

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die Anwendungsdaten dürfen nicht null sein.");
        }

        List<Flug> fluege = anwendungsdaten.getVerwaltungssystem().getFluege();
        Iterator<Flug> iterator = fluege.iterator();

        while (iterator.hasNext()) {
            
            Flug f = iterator.next();

            if (f.getAbflugszeit().isBefore(LocalDateTime.now())) {
                iterator.remove();
            }
        }
    }
}
