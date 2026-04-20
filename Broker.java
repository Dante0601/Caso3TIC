public class Broker extends Thread {
    private static int totalEventosEsperados;
    private Buzon buzonEntrada;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;

    // Se le da como parametro al broker todos los buzones que necesita
    public Broker(Buzon buzonEntrada,
                Buzon buzonAlertas, Buzon buzonClasificacion) {
        super("Broker");
        this.buzonEntrada = buzonEntrada;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    // se inicializa la variable estatica (se puso como variable estatica porque podrian necesitarlo otros threads de la misma clase pero despues cai en cuenta que era uno solo entonces se podia poner en el constructor)
    // no se hizo el cambio por que no hay problema aplicandolo de esta manera
    public void inicializar(int totalEventos){
        totalEventosEsperados = totalEventos;
    }

    @Override
    public void run() {
        // Se retira y deposita todos los elementos esperados
        for (int i =0; i < totalEventosEsperados; i++){
            try {
                // se retira de forma pasiva como dicta el documento
                Evento e = buzonEntrada.retirarPasivo();
                int randomNumber = (int)(Math.random()*201);
                // Se chequea la validez viendo si es multiplo de 8
                if (randomNumber%8 == 0){
                    System.out.println("Broker: Evento " + e.getId() + " considerado ANÓMALO. Enviando a alertas.");
                    buzonAlertas.depositarSemiactivo(e);
                } else {
                    System.out.println("Broker: Evento " + e.getId() + " considerado normal. Enviando a clasificación.");
                    buzonClasificacion.depositarSemiactivoAll(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // una vez reitarados y depositados todos los elementos se empieza a enviar el fin para apagar el admin
        Evento e = Evento.crearFin();
        System.out.println("Broker: Ha procesado todos los eventos. Enviando FIN al administrador.");
        buzonAlertas.depositarSemiactivo(e);
    }
}
