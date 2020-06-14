package configuracion;

public class NoLecturaConfiguracionException extends Exception {
    public NoLecturaConfiguracionException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public NoLecturaConfiguracionException(Throwable throwable) {
        super(throwable);
    }

    public NoLecturaConfiguracionException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public NoLecturaConfiguracionException(String string) {
        super(string);
    }

    public NoLecturaConfiguracionException() {
        super();
    }
}
