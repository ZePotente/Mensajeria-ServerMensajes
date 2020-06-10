package modelo_m;

import java.io.IOException;

public interface IInternetManager {
    public void abrirServer(String nroIPDirectorio) throws IOException;
    public void actualizaListaUsuarios(String nroIPDirectorio, int nroPuertoDirectorio) throws IOException;
}
