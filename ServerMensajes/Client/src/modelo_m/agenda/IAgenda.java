package modelo_m.agenda;

import java.util.HashMap;

public interface IAgenda {
    public HashMap<String, Usuario> actualizarUsuarios(String lista);

    public HashMap<String, Usuario> obtenerUsuarios();

    public boolean isUserOnline(String nombre);
}
