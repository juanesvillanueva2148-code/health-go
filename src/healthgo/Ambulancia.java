package healthgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Vehiculo de la empresa. Los paramedicos se guardan como lista de nombres. */
public class Ambulancia {

    private final String placa;
    private final String tipo; // "Basica" o "Medicalizada"
    private final List<String> paramedicos = new ArrayList<>();
    private boolean disponible = true;

    public Ambulancia(String placa, String tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public String getPlaca() { return placa; }
    public String getTipo() { return tipo; }
    public boolean isDisponible() { return disponible; }

    public List<String> getParamedicos() {
        return Collections.unmodifiableList(paramedicos);
    }

    public void agregarParamedico(String nombre) {
        paramedicos.add(nombre);
    }

    // Estos dos metodos reemplazan a un setDisponible(): el estado solo
    // cambia por una accion con sentido, no con un valor cualquiera.
    public void ocupar() { disponible = false; }
    public void liberar() { disponible = true; }

    public void reportarUbicacion() {
        System.out.println("Ambulancia " + placa + " reportando ubicacion...");
    }
}
