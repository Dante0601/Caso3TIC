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

    public void inicializar(int numServers, int numContadores) {
        activos = numContadores;
        ns = numServers;
    }

    private static synchronized boolean decrementarYEsUltimo() {
        activos--;
        return activos == 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Evento e = buzonClasificacion.retirar();
                if (e.esFin()) {
                    System.out.println("Clasificador " + getName() + ": Recibió FIN.");
                    if (decrementarYEsUltimo()) {
                        System.out.println("Clasificador " + getName() + " es el ÚLTIMO en terminar. Enviando FIN a servidores.");
                        for (Buzon buzon : buzonesServidores) {
                            buzon.depositar(e);
                        }
                    }
                    break;
                } else {
                    System.out.println("Clasificador " + getName() + ": Encaminando evento " + e.getId() + " al servidor " + e.getNumServer());
                    buzonesServidores[e.getNumServer() - 1].depositar(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
