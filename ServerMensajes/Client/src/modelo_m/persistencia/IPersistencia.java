package modelo_m.persistencia;

import java.io.IOException;

import java.util.ArrayList;

public interface IPersistencia {
    public void persistir(ArrayList<String> objetoAPersistir, boolean reemplazarInfoActual) throws IOException;
    public String recuperarPersistencia();
}
