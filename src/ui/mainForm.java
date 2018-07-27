package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import serverSide.conexiones;

public class mainForm {

    public static JFrame  crear(String titulo)
    {
        //Crear la ventana
        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(new Dimension(500, 100));
        ventana.setResizable(false);

        //Poner en el medio de la pantalla
        Dimension tamanoVentana = Toolkit.getDefaultToolkit().getScreenSize();
        ventana.setLocation(new Point((tamanoVentana.width - 500) / 2, (tamanoVentana.height - 500) / 2));

        JPanel panel = new JPanel();
        JLabel ip = new JLabel();
        ip.setFont(new Font("", Font.BOLD, 15));

        //Decirle al usuario que entre a la ruta donde se ejecuta el server
        try {
            ip.setText("Visite esta direccion para acceder: " + Inet4Address.getLocalHost().getHostAddress() + ":3000/directorios");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        panel.add(ip);
        ventana.add(panel);

        ventana.setVisible(true);
        //acabar Node JS server cuando se apague la app
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                conexiones.acabarServer();//Acabar server cuando cierran la app
                super.windowClosing(e);

            }
        });
        return ventana;
    }
}
