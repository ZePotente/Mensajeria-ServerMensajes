package modelo_m;

import java.util.ArrayList;

public class RearmadorLista {
    private static final String SEPARADOR_ATRIBUTOS = "_", SEPARADOR_USUARIOS = "-";
    
    public static ArrayList<String[]> obtenerDestinatarios(String lista) {
        ArrayList<String[]> arr = new ArrayList<String[]>();
        String[] sLista;
        try {
            sLista = separarDestinatarios(lista);
            int cant = sLista.length;
            for (int i = 0; i < cant; i++) {
                arr.add(sLista[i].split(SEPARADOR_ATRIBUTOS));
            }
        } catch (Exception e) {
            // la lista estaba vacia, ya esta creada asi que devuelve una con 0 elementos
        }
        return arr;
    }
    
    private static String[] separarDestinatarios(String lista) throws Exception {
        if (lista.contains(SEPARADOR_USUARIOS))
            return lista.split(SEPARADOR_USUARIOS);
        else {
            throw new Exception();
        }
    }
}
