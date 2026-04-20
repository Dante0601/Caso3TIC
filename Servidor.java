public class Servidor extends Thread {
    private Buzon buzonServidor;

    public Servidor(String name, Buzon buzonServidor) {
        super(name);
        this.buzonServidor = buzonServidor;
    }

    @Override
    public void run() {
        // while true por que l;a condicion depende del evento
        while (true) {
            try {
                Evento e = buzonServidor.retirarPasivoAll();
                // se revisa si es el final y si no se hace el requerimiento del tiempo
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
