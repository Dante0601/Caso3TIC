public class Sensor extends Thread {
    private static int nb;
    private Buzon buzonEntrada;
    private static int ns;

    public Sensor(String name, Buzon buzonEntrada) {
        super(name);
        this.buzonEntrada = buzonEntrada;
    }

    public void inicializar(int numb, int nums){
        nb = numb;
        ns = nums;
    }

    public int getNumber(){
        return Integer.parseInt(this.getName());
    }

    public int getNumEventos() { return nb*this.getNumber(); }

    @Override
    public void run() {
        // Se crean los eventos como lo exige el documento
        for (int i = 1; i <= this.getNumEventos(); i++) {
            int randomNum = (int)(Math.random() * ns) + 1;
            Evento e = new Evento(getName() + "-" + i, randomNum);
            buzonEntrada.depositarSemiactivo(e);
            System.out.println("Sensor " + getName() + ": Generó evento " + e.getId() + " dirigido al servidor " + e.getNumServer());
        }
        System.out.println("Sensor " + getName() + ": Ha terminado de generar todos sus eventos.");
    }
}
