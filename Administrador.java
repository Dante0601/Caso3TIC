public class Administrador extends Thread {
    private int nc;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;

    public Administrador(int nc, Buzon buzonAlertas, Buzon buzonClasificacion) {
        super("Administrador");
        this.nc = nc;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    public int getNc() { return nc; }

    @Override
    public void run() {
        while(true){
            try {
                Evento e = buzonAlertas.retirar();
                int randomNumber = (int)(Math.random() *21);
                if (e.esFin()){
                    System.out.println("Administrador: Recibió FIN. Avisando a clasificadores y terminando.");
                    for (int i =0; i< nc; i++){
                        buzonClasificacion.depositar(e);
                    }
                    break;
                }
                if (randomNumber % 4 == 0){
                    System.out.println("Administrador: Evento " + e.getId() + " es un FALSO POSITIVO. Enviando a clasificación.");
                    buzonClasificacion.depositar(e);
                } else {
                    System.out.println("Administrador: Evento " + e.getId() + " CONFIRMADO como malicioso. Descartado.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
