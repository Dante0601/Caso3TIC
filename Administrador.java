public class Administrador extends Thread {
    private int nc;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;

    // Se crea el admin que tenga acceso al buzon de alertas para ver los evbentos sospechosos y los de clafiriacion para los falsos positivos.
    // Tambien se le dan el numero de contadores para poder hacerlos finalizar.
    public Administrador(int nc, Buzon buzonAlertas, Buzon buzonClasificacion) {
        super("Administrador");
        this.nc = nc;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    @Override

    public void run() {
        // Se usa while true dado que el caso de salida es con el evento fin.
        while(true){
            // Se retira del buzon de forma semiactiva como se pidio en el doc
            Evento e = buzonAlertas.retirarSemiactivo();
            int randomNumber = (int)(Math.random() *21);
            // se chequea si el evento es el final para asi empezar a apagar clasificadores
            if (e.esFin()){
                System.out.println("Administrador: Recibió FIN. Avisando a clasificadores y terminando.");
                for (int i =0; i< nc; i++){
                    buzonClasificacion.depositarSemiactivoAll(e);
                }
                break;
            }
            // se revisa si es falso positivo o no basado en la condicion dada en el caso de ser multiplo de 4
            if (randomNumber % 4 == 0){
                System.out.println("Administrador: Evento " + e.getId() + " es un FALSO POSITIVO. Enviando a clasificación.");
                buzonClasificacion.depositarSemiactivoAll(e);
            } else {
                System.out.println("Administrador: Evento " + e.getId() + " CONFIRMADO como malicioso. Descartado.");
            }
        }
    }
}
