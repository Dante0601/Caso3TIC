import java.io.*;

public class GeneradorArchivo {
    public static void main(String[] args) throws Exception {
        // Ajustar valores según la prueba deseada

        PrintWriter pw = new PrintWriter(new FileWriter(args[0] + ".txt"));

        pw.println("ni=" + args[1]);
        pw.println("nb=" + args[2]);
        pw.println("nc=" + args[3]);
        pw.println("ns=" + args[4]);
        pw.println("tam1=" + args[5]);
        pw.println("tam2=" + args[6]);

        pw.close();
        System.out.println(args[0]+".txt creado");
    }
}
