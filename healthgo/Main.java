package healthgo;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
     
        Paciente paciente = new Paciente("Juan Esteban Villanueva",
                "juan@correo.com", "1234", "3005550123", "Sura EPS",
                "Asma leve", "Laura Villanueva - 3105550456");
        Empresa empresa = new Empresa("Ambulancias Bogota SAS",
                "central@ambbogota.com", "abcd", "6015550000",
                "N/A", "900123456-7");

        System.out.println("Login paciente: "
                + paciente.iniciarSesion("juan@correo.com", "1234"));
        System.out.println("Login con clave mala: "
                + paciente.iniciarSesion("juan@correo.com", "9999"));

        
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(paciente);
        usuarios.add(empresa);
        for (Usuario u : usuarios) {
            u.notificar("Bienvenido a Health Go");
        }

    
        Ambulancia amb = new Ambulancia("AMB-482", "Medicalizada");
        amb.agregarParamedico("Carlos Rojas");
        amb.agregarParamedico("Marta Diaz");
        empresa.registrarAmbulancia(amb);

        
        Hospital h1 = new Hospital("Hospital Simon Bolivar", "Cra 7 #165-00");
        h1.agregarConvenio("Nueva EPS");
        Hospital h2 = new Hospital("Clinica Colsanitas Reina Sofia", "Cl 127 #20-78");
        h2.agregarConvenio("Sura EPS");
        List<Hospital> hospitales = List.of(h1, h2);

     
        Solicitud s = paciente.crearSolicitud("Calle 80 #70-20, Bogota", 1);
        s.asignarAmbulancia(empresa);
        amb.reportarUbicacion();
        s.asignarHospital(hospitales);
        s.cambiarEstado(EstadoSolicitud.EN_CAMINO);
        s.cambiarEstado(EstadoSolicitud.FINALIZADA);

        System.out.println("Ambulancia libre otra vez: " + amb.isDisponible());
        System.out.println("Solicitudes en el historial: "
                + paciente.getHistorialSolicitudes().size());
    }
}
