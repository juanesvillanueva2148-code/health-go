package healthgo;

import java.time.LocalDateTime;
import java.util.List;


public class Solicitud {

    private final Paciente paciente;
    private final String direccion;
    private final int prioridad;
    private final LocalDateTime fechaCreacion = LocalDateTime.now();
    private Ambulancia ambulancia;
    private Hospital hospital;
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    
    Solicitud(Paciente paciente, String direccion, int prioridad) {
        this.paciente = paciente;
        this.direccion = direccion;
        this.prioridad = prioridad;
        paciente.notificar("Solicitud creada. Estado: " + estado);
    }

    public Paciente getPaciente() { return paciente; }
    public String getDireccion() { return direccion; }
    public int getPrioridad() { return prioridad; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Ambulancia getAmbulancia() { return ambulancia; }
    public Hospital getHospital() { return hospital; }
    public EstadoSolicitud getEstado() { return estado; }

    
    public boolean asignarAmbulancia(Empresa empresa) {
        Ambulancia libre = empresa.buscarAmbulanciaDisponible();
        if (libre == null) {
            paciente.notificar("No hay ambulancias disponibles por ahora.");
            empresa.notificar("Solicitud sin ambulancia para " + paciente.getNombre());
            return false;
        }
        libre.ocupar();
        this.ambulancia = libre;
        cambiarEstado(EstadoSolicitud.ASIGNADA);
        return true;
    }

   
    public boolean asignarHospital(List<Hospital> hospitales) {
        for (Hospital h : hospitales) {
            if (h.atiendeEps(paciente.getEps())) {
                this.hospital = h;
                paciente.notificar("Hospital asignado: " + h.getNombre());
                return true;
            }
        }
        paciente.notificar("Ningun hospital tiene convenio con " + paciente.getEps());
        return false;
    }

    public void cambiarEstado(EstadoSolicitud nuevo) {
        this.estado = nuevo;
        paciente.notificar("Tu solicitud cambio a: " + nuevo);
        if (nuevo == EstadoSolicitud.FINALIZADA && ambulancia != null) {
            ambulancia.liberar();
        }
    }
}
