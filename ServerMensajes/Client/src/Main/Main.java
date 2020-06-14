package main;

import modelo_m.SistemaM;

public class Main {
    public Main() {
        super();
    }

    public static void main(String[] args) {
        SistemaM sistema = SistemaM.getInstance();
        sistema.iniciarServer();
    }
}
