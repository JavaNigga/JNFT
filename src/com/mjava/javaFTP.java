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

package com.mjava;
import org.apache.commons.io.IOUtils;
import ui.*;
import serverSide.conexiones;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class javaFTP {
    public static String RUTAINSTALACION;
    public static void main(String[] args)
    {
        //Obtener el txt que contiene la ruta de instalacion
        File file = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\JNFT.txt");
        //Saber si existe o no
        if (file.exists() && file.isFile())
        {
            //Obtener ruta
            System.out.println("Esta instalado");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                RUTAINSTALACION = IOUtils.toString(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conexiones.empezarServer(); //Empezar servidor NodeJs
            mainForm.crear("JNFT"); //Llamar mainForm
        }else
        {
            System.out.println("Necesita instalarce");
            install.crear("INSTALACION"); //Llamar formulario de instalacion
        }
    }
}
