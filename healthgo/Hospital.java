package healthgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Hospital {

    private final String nombre;
    private final String direccion;
    private final List<String> epsConvenio = new ArrayList<>();

    public Hospital(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }

    public List<String> getEpsConvenio() {
        return Collections.unmodifiableList(epsConvenio);
    }

    public void agregarConvenio(String eps) {
        epsConvenio.add(eps);
    }

    public boolean atiendeEps(String eps) {
        return epsConvenio.contains(eps);
    }
}
