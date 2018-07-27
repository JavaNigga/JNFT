/*
 * @author Merli Mejia
 * @version 0.1
 *
 * ESTE SOFTWARE FUE CREADO CON EL FIN DE PRACTICAR TANTO JAVA COMO NODE JS
 *
 * NODE JS DEBE ESTAR INSTALADO EN LA PC PARA SU BUEN FUNCIONAMIENTO
 *
 * GIT: https://github.com/JavaNigga/JNTF


 *
 */

package ui;

import com.mjava.javaFTP;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.Object;

public class install {

    public static JFrame crear(String titulo)
    {
        //Poner look and fell
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //Rutas
        String rutaServer = System.getProperty("user.dir").replace("\\out\\artifacts\\javaFTP_jar", "\\out\\production\\javaFTP\\serverSide");
        String rutaJAR = System.getProperty("user.dir") + "\\javaFTP.jar";
        System.out.println(rutaServer + "  " + rutaJAR);
        //Crear Ventana
        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(new Dimension(600, 400));
        ventana.setResizable(false);
        Dimension tamanoVentana = Toolkit.getDefaultToolkit().getScreenSize();
        ventana.setLocation(new Point((tamanoVentana.width - 600) / 2, (tamanoVentana.height - 400) / 2));

        //Panel principal
        JPanel principal = new JPanel();
        principal.setLayout(new BorderLayout());

        //Cabezera
        JLabel texto = new JLabel("Elija donde instalar JNFT");
        texto.setFont(new Font("", Font.PLAIN, 25));
        JPanel cabezera = new JPanel();
        cabezera.add(texto);
        principal.add(cabezera, BorderLayout.NORTH);

        //Descripcion
        JTextArea descripcion = new JTextArea("JNFT(Java NodeJs File Transfer Es un software creado con el proposito" +
                "\n de practicar tanto Java como Node JS. Este proyecto necesita que Node Js \neste instalado para que pueda correr de manera eficiente. " +
                "\n \nSi quieres contribuir en este proyecto te dejo el Git donde podras hechar un vistazo a todo");
        descripcion.setEditable(false);
        descripcion.setFont(new Font("", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(descripcion);
        principal.add(scrollPane, BorderLayout.CENTER);

        JPanel derecha = new JPanel();
        JPanel izquierda = new JPanel();

        //ruta donde instalar
        JTextField ruta = new JTextField(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
        ruta.setEditable(false);
        ruta.setFont(new Font("", Font.PLAIN, 20));

        /*Dialogo*/

        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame, "ERROR", true);
        dialog.setSize(new Dimension(400, 100));
        dialog.setLocation((tamanoVentana.width - 400) / 2, (tamanoVentana.height - 100) / 2);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        JLabel Eltexto= new JLabel("RUTA SELECCIONADA NO PERMITIDA");
        JPanel paneltexto = new JPanel();
        paneltexto.add(Eltexto);
        Eltexto.setFont(new Font("", Font.BOLD, 10));
        JButton ok = new JButton("OK");
        JPanel panelBotton = new JPanel();
        panelBotton.add(ok);
        dialog.add(paneltexto, BorderLayout.NORTH);
        dialog.add(panelBotton, BorderLayout.CENTER);
        //Opcion OK del panel
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        /**************************************************************************************/

        /******************************Carga***********************************************/

        JFrame cargaFrame = new JFrame("CARGANDO");
        JDialog cargaDialogo = new JDialog(cargaFrame);
        cargaDialogo.setSize(400, 30);
        cargaDialogo.setLocation((tamanoVentana.width - 400) / 2, (tamanoVentana.height - 100) / 2);
        cargaDialogo.setUndecorated(true);


        JProgressBar carga = new JProgressBar();
        carga.setIndeterminate(true);
        cargaDialogo.add(carga);

        /************************************************************************************/


        //botones
        JButton cambiarRuta = new JButton("CAMBIAR RUTA");
        cambiarRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//Solo ver directorios/carpetas

                int boton = fileChooser.showDialog(principal, null);//Mostrar dialogo

                if (boton == JFileChooser.APPROVE_OPTION)
                {
                    ruta.setText(fileChooser.getSelectedFile().toString());//Obtener la ruta
                }
            }
        });

        JButton instalar = new JButton("INSTALAR");
        instalar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileUtils.forceMkdir(new File(ruta.getText() + "\\JNFT"));//Crear directorio

                } catch (IOException e1) {

                    dialog.setVisible(true);
                }

                File destDir = new File(ruta.getText() + "\\JNFT");
                File srcDir = new File(rutaServer);
                ventana.setVisible(false);
                cargaDialogo.setVisible(true);

                //Hilo para copiar los archivos
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(srcDir.toString() + "   " + destDir.toString());
                            FileUtils.copyDirectory(srcDir, destDir);//Copiar todo

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            FileUtils.copyFileToDirectory(new File(rutaJAR), destDir);//Copiar el .JAR
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        ShellLink sc = new ShellLink();

                        try {
                            sc.createLink(rutaJAR, System.getProperty("user.home") + "\\Desktop\\JNFT.lnk");//Crear acceso directo
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        try {
                            //Crear archivo .txt para guardar la ruta de instalacion
                            File txt = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\JNFT.txt");
                            PrintWriter writer = new PrintWriter(txt);
                            writer.print(destDir.toString());
                            writer.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }

                        cargaDialogo.setVisible(false);
                        String[] args = new String[1];
                        args[0] = "HOLA!";
                        javaFTP.main(args);//Llamar la clase principal
                    }
                });
                hilo.start();//Empezar el hilo


            }
        });

        //Panel de abajo
        JPanel abajo = new JPanel();
        abajo.add(ruta);
        abajo.add(cambiarRuta);
        abajo.add(instalar);

        //Agregar todo
        principal.add(abajo, BorderLayout.SOUTH);
        principal.add(izquierda, BorderLayout.WEST);
        principal.add(derecha, BorderLayout.EAST);
        ventana.add(principal);

        ventana.setVisible(true);

        return ventana;

    }
}
