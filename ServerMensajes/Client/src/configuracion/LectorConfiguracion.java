package configuracion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LectorConfiguracion {
    /**
    * @param nombreArch
    * Lee el archivo de configuracion.txt
    * y crea un objeto de clase <i>Configuracion</i> con su contenido.
    *
    * @throws configuracion.NoLecturaConfiguracionException
    * Si ocurre un error con la lectura del archivo de configuracion.
    */
    public static Configuracion leerConfig(String nombreArch) throws NoLecturaConfiguracionException {
        try {
            FileInputStream arch;
            arch = new FileInputStream(nombreArch);
            Scanner sc = new Scanner(arch);    
            
            //directorio 1
            String nroIPDir1 = sc.nextLine();
            String puertoDir1 = sc.nextLine();
            //directorio 2
            String nroIPDir2 = sc.nextLine();
            String puertoDir2 = sc.nextLine();
            sc.close();
            
            Configuracion config = new Configuracion(nroIPDir1, puertoDir1, nroIPDir2, puertoDir2);
            return config;
        } catch (FileNotFoundException e) {
            throw new NoLecturaConfiguracionException(e);
        } catch (NoSuchElementException e) {
            throw new NoLecturaConfiguracionException(e);
        }
    }
}