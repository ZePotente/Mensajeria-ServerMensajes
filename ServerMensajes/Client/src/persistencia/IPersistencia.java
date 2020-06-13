package persistencia;

import java.io.IOException;

import java.util.ArrayList;

public interface IPersistencia {
    public void persistir(ArrayList<String> objetoAPersistir, boolean reemplazarInfoActual, boolean mensajeAvisoRecepcion) throws IOException;
    public String recuperarPersistencia(boolean mensajesConAvisoDeRecepcion);
}
