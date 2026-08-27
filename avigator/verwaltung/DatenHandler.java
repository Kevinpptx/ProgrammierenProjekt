package avigator.verwaltung;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verwaltet das Speichern und Laden der avigator.verwaltung.Anwendungsdaten. Die avigator.verwaltung.Anwendungsdaten werden mithilfe der Java-Serialisierung im
 * Ordner {@code data} gespeichert.
 *
 * <p>Falls beim Programmstart keine gültige Speicherdatei vorhanden ist,
 * werden neue avigator.verwaltung.Anwendungsdaten erzeugt.</p>
 *
 * @author Cedric Beckmann
 * @version 1.0
 */
public class DatenHandler {

    /**
     * Pfad zu der Datei, in der die serialisierten avigator.verwaltung.Anwendungsdaten gespeichert werden.
     */
    private static final Path DATEI_PFAD = Path.of(System.getProperty("user.dir"), "data", "avigator.verwaltung.Anwendungsdaten.ser");

    /**
     * Erzeugt einen neuen avigator.verwaltung.DatenHandler.
     */
    public DatenHandler() {

    }

    /**
     * Serialisiert die übergebenen avigator.verwaltung.Anwendungsdaten und speichert sie in der dafür vorgesehenen Datei.
     *
     * <p>Falls der Ordner {@code data} noch nicht existiert,
     * wird dieser automatisch erzeugt.</p>
     *
     * @param anwendungsdaten die zu speichernden avigator.verwaltung.Anwendungsdaten
     * @throws IllegalArgumentException wenn die avigator.verwaltung.Anwendungsdaten {@code null} sind
     */
    public void speichere(Anwendungsdaten anwendungsdaten) {

        if (anwendungsdaten == null) {
            throw new IllegalArgumentException("Die avigator.verwaltung.Anwendungsdaten duerfen nicht null sein.");
        }

        try {
            // Erstellt den Ordner "data", falls er noch nicht existiert
            Files.createDirectories(DATEI_PFAD.getParent());

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(DATEI_PFAD))) {

                objectOutputStream.writeObject(anwendungsdaten);

                System.out.println("Die avigator.verwaltung.Anwendungsdaten wurden erfolgreich gespeichert.");
            }

        } catch (IOException e) {
            System.err.println("Fehler! Die avigator.verwaltung.Anwendungsdaten konnten nicht gespeichert werden.");
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert die avigator.verwaltung.Anwendungsdaten beim Programmstart.
     *
     * <p>Falls eine Speicherdatei vorhanden ist, werden die darin
     * gespeicherten avigator.verwaltung.Anwendungsdaten geladen. Wenn keine gültige Speicherdatei vorhanden ist, werden neue
     * avigator.verwaltung.Anwendungsdaten erzeugt.</p>
     *
     * @return die geladenen oder neu erzeugten avigator.verwaltung.Anwendungsdaten
     */
    public Anwendungsdaten initialisiereAnwendungsdaten() {

        if (Files.notExists(DATEI_PFAD)) {

            System.out.println(
                    "Es konnten keine gespeicherten avigator.verwaltung.Anwendungsdaten gefunden werden. Neue avigator.verwaltung.Anwendungsdaten werden erzeugt.");
            return new Anwendungsdaten();

        } else {

            try {

                return leseAusDatei();

            } catch (IOException | ClassNotFoundException | ClassCastException e) {

                System.err.println(
                        "Gespeicherte avigator.verwaltung.Anwendungsdaten konnten nicht geladen werden. Neue avigator.verwaltung.Anwendungsdaten werden erzeugt.");

                e.printStackTrace();

                return new Anwendungsdaten();

            }
        }
    }

    /**
     * Liest serialisierte avigator.verwaltung.Anwendungsdaten aus der Speicherdatei.
     *
     * @return die aus der Datei gelesenen avigator.verwaltung.Anwendungsdaten
     * @throws IOException            wenn beim Lesen der Datei ein Fehler auftritt
     * @throws ClassNotFoundException wenn die Klasse des gespeicherten Objekts nicht gefunden werden kann
     * @throws ClassCastException     wenn die Datei kein Objekt der Klasse {@link Anwendungsdaten} enthält
     */
    private Anwendungsdaten leseAusDatei() throws IOException, ClassNotFoundException {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(DATEI_PFAD))) {

            Object objekt = objectInputStream.readObject();

            if ((objekt instanceof Anwendungsdaten)) {
                return (Anwendungsdaten) objekt;
            } else {
                throw new ClassCastException("Die gespeicherte Datei enthaelt keine avigator.verwaltung.Anwendungsdaten");
            }
        }
    }
}
