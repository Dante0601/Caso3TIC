import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(args[0] + ".txt"));
        
        int ni = 0, base = 0, nc = 0, ns = 0, tam1 = 0, tam2 = 0;
        
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split("=");
            int valor = Integer.parseInt(partes[1]);
            
            switch (partes[0]) {
                case "ni":   ni   = valor; break;
                case "nb":   base = valor; break;
                case "nc":   nc   = valor; break;
                case "ns":   ns   = valor; break;
                case "tam1": tam1 = valor; break;
                case "tam2": tam2 = valor; break;
            }
        }
        br.close();

        Buzon buzonEntrada = new Buzon(-1);
        Buzon buzonAlerta = new Buzon(-1);
        Buzon buzonClasificacion = new Buzon(tam1);
        Buzon[] buzonesServidores = new Buzon[ns];

        for (int i = 0; i < ns; i++){
            buzonesServidores[i] = new Buzon(tam2);
        }
        Sensor[] sensores = new Sensor[ni];

        for (int i = 0; i < ni; i++){
            sensores[i] = new Sensor(Integer.toString(i+1), buzonEntrada);
            sensores[i].inicializar(base, ns);
            sensores[i].start();
        }
        
        int totalEventos = 0;
        for (int i = 1; i <= ni; i++) {
            totalEventos += base * (i);
        }

        Broker broker = new Broker(buzonEntrada, buzonAlerta, buzonClasificacion);
        broker.inicializar(totalEventos);
        broker.start();
        Administrador admin = new Administrador(nc, buzonAlerta, buzonClasificacion);
        admin.start();

        Clasificador[] clasificadores = new Clasificador[nc];
        for (int i = 0; i<nc; i++){
            clasificadores[i] = new Clasificador(Integer.toString(i), buzonClasificacion, buzonesServidores);
            clasificadores[i].inicializar(ns, nc);
            clasificadores[i].start();
        }

        Servidor[] servidores = new Servidor[ns];
        for (int i=0; i<ns; i++){
            servidores[i] = new Servidor(Integer.toString(i+1), buzonesServidores[i]);
            servidores[i].start();
        }

        for (Sensor sensor : sensores) sensor.join();
        broker.join();
        admin.join();
        for (Clasificador c : clasificadores) c.join();
        for (Servidor s : servidores) s.join();


        System.out.println("-------------------------------------------------");
        System.out.println("SIMULACIÓN TERMINADA: Todos los hilos han finalizado.");
        System.out.println("-------------------------------------------------");
    }
}
