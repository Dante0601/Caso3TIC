public class Servidor extends Thread {
    private Buzon buzonesServidores;

    public Servidor(String name, Buzon buzonesServidores) {
        super(name);
        this.buzonesServidores = buzonesServidores;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Evento e = buzonesServidores.retirar();
                if (e.esFin()) {
                    System.out.println("Servidor " + getName() + ": Recibió FIN. Terminando.");
                    break;
                } else {
                    System.out.println("Servidor " + getName() + ": Procesando evento " + e.getId() + "...");
                    // Simular el tiempo de procesamiento entre 100 ms y 1000 ms
                    long tiempoProcesamiento = (long)(Math.random() * 901) + 100;
                    Thread.sleep(tiempoProcesamiento);
                    System.out.println("Servidor " + getName() + ": Terminó de consolidar evento " + e.getId());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
