package client;

import java.util.ArrayList;
import java.util.HashMap;

public class Agenda {
    private HashMap<String, Usuario> usuarios = new HashMap<>();
    public Agenda() {
        super();
    }
    
    public HashMap<String, Usuario> actualizarUsuarios(String lista) {
        HashMap<String, Usuario> aux = new HashMap<>();
        ArrayList<String[]> listaString = RearmadorLista.obtenerDestinatarios(lista);     
        for (String[] arrString : listaString) {
            Usuario usuario = new Usuario(arrString[0], arrString[1]);
            usuario.setEstado(arrString[2].equalsIgnoreCase("true"));
            aux.put(usuario.getNombre(), usuario);
        }
        this.usuarios = aux;
        return usuarios;
    }
    
    public HashMap<String, Usuario> obtenerUsuarios() {
        return usuarios;
    }
    
    public boolean isUserOnline(String nombre) {
        return usuarios.containsKey(nombre) && usuarios.get(nombre).getEstado();
    }
}
