package modelo_m.configuracion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class LectorConfiguracion {
    /**
     * @param nombreArch
     * Lee el archivo de configuracion.txt
     * y crea un objeto de clase <i>Configuracion</i> con su contenido.
     *
     * @throws modelo_m.configuracion.NoLecturaConfiguracionException
     * Si ocurre un error con la lectura del archivo de configuracion.
     */
    public static Configuracion leerConfig(String nombreArch) throws NoLecturaConfiguracionException {
        try {
            FileInputStream arch;
            arch = new FileInputStream(nombreArch);
            Scanner sc = new Scanner(arch);    
            
            String nroIPDirectorio = sc.nextLine(); 
            Configuracion config = new Configuracion(nroIPDirectorio);
            sc.close();
            return config;
        } catch (FileNotFoundException e) {
            throw new NoLecturaConfiguracionException(e);
        }  
    }
}
