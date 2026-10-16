package healthgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ambulancia {

    private final String placa;
    private final String tipo;
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


    public void ocupar() { disponible = false; }
    public void liberar() { disponible = true; }

    public void reportarUbicacion() {
        System.out.println("Ambulancia " + placa + " reportando ubicacion...");
    }
}
