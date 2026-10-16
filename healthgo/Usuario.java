package healthgo;


public abstract class Usuario {

    private final String nombre;
    private final String correo;
    private final String contrasena;
    private final String telefono;
    private final String eps;

    public Usuario(String nombre, String correo, String contrasena,
                   String telefono, String eps) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.eps = eps;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getEps() { return eps; }
   

    public boolean iniciarSesion(String correo, String contrasena) {
        return this.correo.equals(correo) && this.contrasena.equals(contrasena);
    }

   
    public abstract void notificar(String mensaje);
}
