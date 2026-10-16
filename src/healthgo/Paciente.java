package healthgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persona que pide la ambulancia. Hereda los datos de login de Usuario. */
public class Paciente extends Usuario {

    private final String historialMedico;
    private final String contactoEmergencia;
    private final List<Solicitud> historialSolicitudes = new ArrayList<>();

    public Paciente(String nombre, String correo, String contrasena,
                    String telefono, String eps, String historialMedico,
                    String contactoEmergencia) {
        super(nombre, correo, contrasena, telefono, eps);
        this.historialMedico = historialMedico;
        this.contactoEmergencia = contactoEmergencia;
    }

    public String getHistorialMedico() { return historialMedico; }
    public String getContactoEmergencia() { return contactoEmergencia; }

    /** Devuelve una vista de solo lectura para que nadie modifique la lista desde afuera. */
    public List<Solicitud> getHistorialSolicitudes() {
        return Collections.unmodifiableList(historialSolicitudes);
    }

    public Solicitud crearSolicitud(String direccion, int prioridad) {
        Solicitud solicitud = new Solicitud(this, direccion, prioridad);
        historialSolicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[Notificacion push a paciente " + getNombre() + "] " + mensaje);
    }
}
