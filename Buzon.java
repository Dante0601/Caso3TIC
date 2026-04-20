import java.util.LinkedList;

public class Buzon {
    private int capacidad;           // -1 = ilimitado
    private LinkedList<Evento> cola;

    public Buzon(int capacidad) {    // -1 para ilimitado, tam1/tam2 para limitados
        this.capacidad = capacidad;
        this.cola = new LinkedList<>();
    }

    public int getCapacidad()    { return capacidad; }
    public int getTamano()       { return cola.size(); }
    public boolean esIlimitado() { return capacidad == -1; }
    public boolean estaLleno()   { return !esIlimitado() && cola.size() >= capacidad; }
    public boolean estaVacio()   { return cola.isEmpty(); }

    // Depositar sin wait — para productores con espera semi-activa (yield externo)
    public synchronized void depositarDirecto(Evento e) {
        cola.add(e);
        notify();
    }


    public synchronized void depositar(Evento e) throws InterruptedException {
        while (!esIlimitado() && cola.size() == capacidad) wait();
        cola.add(e);
        notifyAll();
    }

    public synchronized Evento retirar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        Evento e = cola.removeFirst();
        notifyAll();
        return e;
    }
}
