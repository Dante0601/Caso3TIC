import java.util.LinkedList;

public class Buzon {
    private int capacidad;
    private LinkedList<Evento> cola;


    // el constructor de un buzon tiene una capacidad (use -1 si es ilimitada) y una cola que es donde se guardan los eventos depositados
    public Buzon(int capacidad) {
        this.capacidad = capacidad;
        this.cola = new LinkedList<>();
    }

    // se crean estos metodos para evitar repeticion en los chequeos de condiciones
    public boolean esIlimitado() {
        return capacidad == -1;
    }
    public boolean estaLleno() {
        return !esIlimitado() && cola.size() >= capacidad;
    }
    public boolean estaVacio() {
        return cola.isEmpty();
    }
    // Se deposita de forma pasiva, pero se notifica a todos los threads que les pueda interesar. Este se utiliza en el deposito que hacen los clasificadores para los servidores.
    public synchronized void depositarPasivoAll(Evento e) throws InterruptedException {
        while (estaLleno()) {
            wait();
        }
        cola.add(e);
        notifyAll();
    }

    // Se retira del buzon de forma pasiva notificando unicamente al primer thread interesado, en este caso se usa en el retiro del broker
    public synchronized Evento retirarPasivo() throws InterruptedException {
        while (estaVacio()) {
            wait();
        }
        Evento e = cola.removeFirst();
        notify();
        return e;
    }

    // Se retira del buzon de forma pasiva pero notificando a todos los threads interesados, en este caso se utiliza cuando los c=lasificadores retiran para llamar al admin y al broker y de igual manera lo hacen los servidores para llamar a todos los clasificadores
    public synchronized Evento retirarPasivoAll() throws InterruptedException {
        while (estaVacio()) {
            wait();
        }
        Evento e = cola.removeFirst();
        notifyAll();
        return e;
    }


    // Se usan estos metodos para poder hacer la espera semiactiva
    private synchronized boolean intentarDepositar(Evento e) {
        if (!estaLleno()) {
            cola.add(e);
            notify();
            return true;
        }
        return false;
    }

    private synchronized boolean intentarDepositarAll(Evento e) {
        if (!estaLleno()) {
            cola.add(e);
            notifyAll();
            return true;
        }
        return false;
    }

    private synchronized Evento intentarRetirar() {
        if (!estaVacio()) {
            Evento e = cola.removeFirst();
            notify();
            return e;
        }
        return null;
    }

    private synchronized Evento intentarRetirarAll() {
        if (!estaVacio()) {
            Evento e = cola.removeFirst();
            notifyAll();
            return e;
        }
        return null;
    }

    // este metodo deposita un evento dentro del buzon esperando semiactivamente, este se utiliza para depositar al buzon de alertas, pues, solo se debe notificar al admin
    public void depositarSemiactivo(Evento e) {
        while (!intentarDepositar(e)) {
            Thread.yield();
        }
    }
    // funciona igual que el anterior solo que con notifyall, este notifica a todos los administradores
    public void depositarSemiactivoAll(Evento e) {
        while (!intentarDepositarAll(e)) {
            Thread.yield();
        }
    }

    // Este metodo retira de un buzon de forma semiactiva, cediendo el procesador con yield(). En este caso se utiliza cuando el admin retira del buzon de alertas
    public Evento retirarSemiactivo() {
        Evento e;
        while ((e = intentarRetirar()) == null) {
            Thread.yield();
        }
        return e;
    }

}
