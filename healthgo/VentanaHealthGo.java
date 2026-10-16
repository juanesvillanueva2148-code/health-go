package healthgo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class VentanaHealthGo extends JFrame {

    private final Paciente paciente;
    private final Empresa empresa;
    private final List<Hospital> hospitales;
    private Solicitud actual;

    private final JTextField campoDireccion = new JTextField("Calle 80 #70-20, Bogota", 20);
    private final JComboBox<Integer> comboPrioridad = new JComboBox<>(new Integer[]{1, 2, 3});
    private final JLabel etiquetaEstado = new JLabel();
    private final JTextArea areaLog = new JTextArea(12, 55);
    private final JButton botonPedir = new JButton("Pedir ambulancia");
    private final JButton botonEnCamino = new JButton("Marcar en camino");
    private final JButton botonFinalizar = new JButton("Finalizar");
    private final JButton botonHistorial = new JButton("Ver historial");

    public VentanaHealthGo(Paciente paciente, Empresa empresa, List<Hospital> hospitales) {
        this.paciente = paciente;
        this.empresa = empresa;
        this.hospitales = hospitales;

        setTitle("Health Go");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel filaDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaDatos.add(new JLabel("Direccion:"));
        filaDatos.add(campoDireccion);
        filaDatos.add(new JLabel("Prioridad:"));
        filaDatos.add(comboPrioridad);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaBotones.add(botonPedir);
        filaBotones.add(botonEnCamino);
        filaBotones.add(botonFinalizar);
        filaBotones.add(botonHistorial);

        JPanel norte = new JPanel(new GridLayout(0, 1));
        norte.add(new JLabel("  Paciente: " + paciente.getNombre() + "  |  EPS: " + paciente.getEps()));
        norte.add(filaDatos);
        norte.add(filaBotones);
        norte.add(etiquetaEstado);

        areaLog.setEditable(false);
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(areaLog), BorderLayout.CENTER);

        botonPedir.addActionListener(e -> pedirAmbulancia());
        botonEnCamino.addActionListener(e -> cambiarEstado(EstadoSolicitud.EN_CAMINO));
        botonFinalizar.addActionListener(e -> cambiarEstado(EstadoSolicitud.FINALIZADA));
        botonHistorial.addActionListener(e -> verHistorial());

        redirigirConsola();
        paciente.notificar("Bienvenido a Health Go");
        actualizarPantalla();

        pack();
        setLocationRelativeTo(null);
    }

    private void pedirAmbulancia() {
        String direccion = campoDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe la direccion", "Falta un dato",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (actual == null || actual.getEstado() == EstadoSolicitud.FINALIZADA) {
            int prioridad = (Integer) comboPrioridad.getSelectedItem();
            actual = paciente.crearSolicitud(direccion, prioridad);
        }
        if (actual.asignarAmbulancia(empresa)) {
            actual.asignarHospital(hospitales);
        }
        actualizarPantalla();
    }

    private void cambiarEstado(EstadoSolicitud nuevo) {
        actual.cambiarEstado(nuevo);
        actualizarPantalla();
    }

    private void verHistorial() {
        StringBuilder texto = new StringBuilder("Historial de " + paciente.getNombre() + ":\n");
        for (Solicitud s : paciente.getHistorialSolicitudes()) {
            texto.append("- ").append(s.getDireccion())
                 .append(" | prioridad ").append(s.getPrioridad())
                 .append(" | ").append(s.getEstado()).append("\n");
        }
        JOptionPane.showMessageDialog(this, texto.toString(), "Historial",
                JOptionPane.INFORMATION_MESSAGE);
    }

    
    private void actualizarPantalla() {
        EstadoSolicitud estado = (actual == null) ? null : actual.getEstado();
        boolean sePuedePedir = estado == null
                || estado == EstadoSolicitud.FINALIZADA
                || estado == EstadoSolicitud.PENDIENTE;
        botonPedir.setEnabled(sePuedePedir);
        campoDireccion.setEnabled(sePuedePedir);
        comboPrioridad.setEnabled(sePuedePedir);
        botonEnCamino.setEnabled(estado == EstadoSolicitud.ASIGNADA);
        botonFinalizar.setEnabled(estado == EstadoSolicitud.EN_CAMINO);

        if (actual == null) {
            etiquetaEstado.setText("  Sin solicitud activa");
        } else {
            String placa = (actual.getAmbulancia() == null) ? "sin ambulancia"
                    : actual.getAmbulancia().getPlaca();
            String hospital = (actual.getHospital() == null) ? "sin hospital"
                    : actual.getHospital().getNombre();
            etiquetaEstado.setText("  Estado: " + estado + "  |  " + placa + "  |  " + hospital);
        }
    }

    
    private void redirigirConsola() {
        OutputStream haciaElCuadro = new OutputStream() {
            @Override
            public void write(int b) {
                areaLog.append(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] datos, int inicio, int largo) {
                areaLog.append(new String(datos, inicio, largo, StandardCharsets.UTF_8));
            }
        };
        System.setOut(new PrintStream(haciaElCuadro, true));
    }

   
    private static boolean pedirLogin(Paciente paciente) {
        JTextField correo = new JTextField(15);
        JPasswordField clave = new JPasswordField(15);
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Correo (demo: juan@correo.com)"));
        panel.add(correo);
        panel.add(new JLabel("Contrasena (demo: 1234)"));
        panel.add(clave);

        while (true) {
            int opcion = JOptionPane.showConfirmDialog(null, panel, "Health Go - Iniciar sesion",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) {
                return false;
            }
            if (paciente.iniciarSesion(correo.getText().trim(), new String(clave.getPassword()))) {
                return true;
            }
            JOptionPane.showMessageDialog(null, "Correo o contrasena incorrectos", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }

        SwingUtilities.invokeLater(() -> {
            Paciente paciente = new Paciente("Juan Esteban Villanueva", "juan@correo.com", "1234",
                    "3005550123", "Sura EPS", "Asma leve", "Laura Villanueva - 3105550456");

            Empresa empresa = new Empresa("Ambulancias Bogota SAS", "central@ambbogota.com", "abcd",
                    "6015550000", "N/A", "900123456-7");
            Ambulancia a1 = new Ambulancia("AMB-482", "Medicalizada");
            a1.agregarParamedico("Carlos Rojas");
            a1.agregarParamedico("Marta Diaz");
            Ambulancia a2 = new Ambulancia("AMB-731", "Basica");
            a2.agregarParamedico("Luis Perez");
            empresa.registrarAmbulancia(a1);
            empresa.registrarAmbulancia(a2);

            Hospital h1 = new Hospital("Hospital Simon Bolivar", "Cra 7 #165-00");
            h1.agregarConvenio("Nueva EPS");
            Hospital h2 = new Hospital("Clinica Colsanitas Reina Sofia", "Cl 127 #20-78");
            h2.agregarConvenio("Sura EPS");

            if (pedirLogin(paciente)) {
                new VentanaHealthGo(paciente, empresa, List.of(h1, h2)).setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
