package persistencia;

import java.io.IOException;

public interface IPersistencia {
    public void persistir(String objetoAPersistir, boolean reemplazarInfoActual) throws IOException;
    public String recuperarPersistencia();
}
