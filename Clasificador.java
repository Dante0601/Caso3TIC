public class Clasificador extends Thread {
    private Buzon buzonClasificacion;
    private Buzon[] buzonesServidores;
    private static int activos;
    private static int ns;

    public Clasificador(String name, Buzon buzonClasificacion,
            Buzon[] buzonesServidores) {
        super(name);
        this.buzonClasificacion = buzonClasificacion;
        this.buzonesServidores = buzonesServidores;
    }
    // Se usan las variables estaticas para saber cuantos clasificadores hay activos y cuantos servidores hay en general.
    public void inicializar(int numServers, int numClasificadores) {
        activos = numClasificadores;
        ns = numServers;
    }

    // Se usa este metodo para evitar que los hilos entren desincronizados a cambiar la variable estatica. Por ejemplo si tienes activos = 2 y 2 threads entraran al mismo tiempo a hacer cambios podria haber fallo de que nunca hay fin. 
    // Note que esto tambien genera interbloqueo o deadlock dado que los servidores espern a este thread para poder apagarse.
    private static synchronized boolean decrementarYEsUltimo() {
        activos--;
        return activos == 0;
    }

    @Override
    public void run() {
        // se usa while true por que la condicion de cancelacion esta interna dependiendo del evento
        while (true) {
            try {
                Evento e = buzonClasificacion.retirarPasivoAll();
                // se verifica si el evento es fin y si no se manda al server correspondiente
                if (e.esFin()) {
                    System.out.println("Clasificador " + getName() + ": Recibió FIN.");
                    if (decrementarYEsUltimo()) {
                        System.out.println("Clasificador " + getName() + " es el ÚLTIMO en terminar. Enviando FIN a servidores.");
                        for (Buzon buzon : buzonesServidores) {
                            buzon.depositarPasivoAll(e);
                        }
                    }
                    break;
                } else {
                    System.out.println("Clasificador " + getName() + ": Encaminando evento " + e.getId() + " al servidor " + e.getNumServer());
                    buzonesServidores[e.getNumServer() - 1].depositarPasivoAll(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
