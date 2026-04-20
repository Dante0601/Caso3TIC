public class Evento {
    private String id;
    private int ns;

    public Evento(String id, int ns) {
        this.id = id;
        this.ns = ns;
    }

    // Evento de fin: tipo = 0
    public static Evento crearFin() {
        return new Evento("FIN", 0);
    }

    public boolean esFin()        { return ns == 0; }
    public String getId()         { return id; }
    public int getNumServer()     { return ns; }
}
