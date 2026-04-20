import java.io.*;

public class GeneradorArchivo {
    /**
     * Método principal que genera un archivo de texto con los parámetros de configuración
     * necesarios para inicializar la simulación del sistema.
     *
     * @param args Arreglo de strings pasado por consola. Se esperan exactamente 7 argumentos:
     *             args[0]: Nombre del archivo a generar (sin la extensión .txt).
     *             args[1]: Número de sensores (ni).
     *             args[2]: Multiplicador base de eventos por sensor (nb).
     *             args[3]: Número de clasificadores (nc).
     *             args[4]: Número de servidores (ns).
     *             args[5]: Tamaño máximo del buzón de clasificación (tam1).
     *             args[6]: Tamaño máximo de los buzones de los servidores (tam2).
     * @throws Exception Si ocurre un error de entrada/salida al crear o escribir el archivo.
     */
    public static void main(String[] args) throws Exception {
        // Validación de seguridad para evitar que el programa colapse si faltan argumentos
        if (args.length < 7) {
            System.out.println("Error: Faltan argumentos. Se requieren 7 parámetros.");
            System.out.println("Uso: java GeneradorArchivo <nombre_archivo> <ni> <nb> <nc> <ns> <tam1> <tam2>");
            return;
        }

        PrintWriter pw = new PrintWriter(new FileWriter(args[0] + ".txt"));

        pw.println("ni=" + args[1]);
        pw.println("nb=" + args[2]);
        pw.println("nc=" + args[3]);
        pw.println("ns=" + args[4]);
        pw.println("tam1=" + args[5]);
        pw.println("tam2=" + args[6]);

        pw.close();
        
        System.out.println(args[0] + ".txt creado\n");
    }
}
