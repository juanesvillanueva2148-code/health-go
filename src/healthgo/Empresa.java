package healthgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Empresa prestadora del servicio de ambulancias. Es un Usuario (tiene cuenta)
 * y administra su propia flota. Reemplaza al antiguo Conductor.
 */
public class Empresa extends Usuario {

    private final String nit;
    private final List<Ambulancia> flota = new ArrayList<>();

    public Empresa(String nombre, String correo, String contrasena,
                   String telefono, String eps, String nit) {
        super(nombre, correo, contrasena, telefono, eps);
        this.nit = nit;
    }

    public String getNit() { return nit; }

    public List<Ambulancia> getFlota() {
        return Collections.unmodifiableList(flota);
    }

    public void registrarAmbulancia(Ambulancia ambulancia) {
        flota.add(ambulancia);
    }

    /** Primera ambulancia libre de la flota, o null si todas estan ocupadas. */
    public Ambulancia buscarAmbulanciaDisponible() {
        for (Ambulancia a : flota) {
            if (a.isDisponible()) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[Alerta a central de " + getNombre() + "] " + mensaje);
    }
}
