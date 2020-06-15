package modelo_m;

import java.io.IOException;

public interface IInternetManager {
    public void abrirServer() throws IOException;
    public void actualizaListaUsuarios(String nroIPDirectorio, int nroPuertoDirectorio) throws IOException;
}
