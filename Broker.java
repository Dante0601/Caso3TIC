public class Broker extends Thread {
    private static int totalEventosEsperados;
    private Buzon buzonEntrada;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;

    public Broker(Buzon buzonEntrada,
                Buzon buzonAlertas, Buzon buzonClasificacion) {
        super("Broker");
        this.buzonEntrada = buzonEntrada;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    public void inicializar(int totalEventos){
        totalEventosEsperados = totalEventos;
    }

    public int getTotalEventosEsperados() { return totalEventosEsperados; }

    @Override
    public void run() {
        for (int i =0; i < totalEventosEsperados; i++){
            try {
                Evento e = buzonEntrada.retirar();
                int randomNumber = (int)(Math.random()*201);
                if (randomNumber%8 == 0){
                    System.out.println("Broker: Evento " + e.getId() + " considerado ANÓMALO. Enviando a alertas.");
                    buzonAlertas.depositarDirecto(e);
                } else {
                    System.out.println("Broker: Evento " + e.getId() + " considerado normal. Enviando a clasificación.");
                    buzonClasificacion.depositar(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Evento e = Evento.crearFin();
        System.out.println("Broker: Ha procesado todos los eventos. Enviando FIN al administrador.");
        buzonAlertas.depositarDirecto(e);
    }
}
