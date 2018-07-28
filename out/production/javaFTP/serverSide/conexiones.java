package serverSide;

import com.mjava.javaFTP;

import java.io.File;
import java.io.IOException;

public class conexiones {
    public static ProcessBuilder processBuilder;
    public  static Process process;
    public static void empezarServer()
    {
        //Crear el proceso y linkearlo con la consola
        processBuilder = new ProcessBuilder("node", javaFTP.RUTAINSTALACION + "\\server.js");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        try {
            //empezar el server
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //Acabar el server
    public static void acabarServer()
    {
        process.destroy();
        System.out.println("Se Cerro!");
    }
}
