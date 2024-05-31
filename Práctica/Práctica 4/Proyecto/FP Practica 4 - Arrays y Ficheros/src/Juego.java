import java.io.*;

public class Juego {
    //Realizado por Alejandro Fernández Guerrero bu0024 IWSIM11

    private final Clave claveReactor;
    private final Tripulante[] tripulacion;
    private final int TOTAL_TRIPULANTES;
    private int numTripulantes;
    private final Impostor impostor;

    public Juego(int numTripulantes, int longitudClave) {
        // TODO 4 inicializar convenientemente los atributos
        this.TOTAL_TRIPULANTES = numTripulantes;
        this.numTripulantes = numTripulantes;
        this.claveReactor = new Clave(longitudClave);
        tripulacion = new Tripulante[numTripulantes];
        for (int i = 0; i < TOTAL_TRIPULANTES; i++){
            tripulacion[i] = new Tripulante(100 + i, claveReactor.getDistorsionada());
        }
        int numAleatorio = aleatorio(0,numTripulantes -1);
        impostor = new Impostor(numTripulantes, tripulacion[numAleatorio]);
        System.out.println("Te escondes como tripulante "+tripulacion[numAleatorio].getIdentificador()
                +", que no te descubran");
    }

    private static int aleatorio(int minimo, int maximo) {
        return (int)(Math.random() * (maximo - minimo) + minimo);
    }

    public void jugar() {
        boolean finJuego = false;
        boolean explotada = false;
        do {
            System.out.println("\nLos tripulantes realizan sus actividades en la nave ...");
            this.atacarTripulante();
            if (impostor.quiereJugar()) {
                System.out.println("Iniciando autodestrucción ...");
                if (impostor.adivinaClave(claveReactor)) {
                    System.out.println("EXPLOTÓ LA NAVE. HAS TENIDO ÉXITO");
                    explotada = true;
                    finJuego = true;
                } else {
                    System.out.println("SUENAN LAS ALARMAS");
                    System.out.println("ERES EXPULSADO DE LA NAVE. FRACASASTE EN LA MISIÓN");
                    this.expulsarImpostor();
                    finJuego = true;
                }
            } else {
                int idExpulsado = this.expulsarSospechoso();
                if (idExpulsado == impostor.getIdentificador()) {
                    System.out.println("ERES EXPULSADO DE LA NAVE. FRACASASTE EN LA MISIÓN");
                    finJuego = true;
                }
            }
        } while (!finJuego && numTripulantes > 1);
        if (!finJuego) {
            System.out.println("TE HAS QUEDADO SOLO EN LA NAVE. TRIUNFASTE EN LA MISIÓN");
        }
        if (!explotada) {
            boolean respuesta = Teclado.leerBoolean("¿Guardar los datos de la nave (S/N)? ");
            if (respuesta) {
                String ruta = Teclado.leerString("Nombre del archivo: ");
                this.guardarEstadoNave(ruta);
            }
        }
    }

    public void atacarTripulante() {
        // TODO 5 El impostor ataca a un tripulante para robarle la clave.
        int numAleatorio;
        Tripulante tripulante;
        do{
            numAleatorio = aleatorio(0, numTripulantes);
            tripulante = tripulacion[numAleatorio];
        } while (tripulante.getIdentificador() == impostor.getIdentificador());
        System.out.println("De pronto, el tripulante "+tripulacion[numAleatorio].getIdentificador()+" es atacado y eliminado");
        impostor.robarClave(tripulacion[numAleatorio]);
        eliminarTripulante(numAleatorio);

    }

    public void eliminarTripulante(int posicion) {
        // TODO 6 El tripulante que ocupa la posicion recibida en el array es eliminado.
        for (int i = posicion; i < numTripulantes -1; i++){
            tripulacion[i] = tripulacion[i + 1];
        }
        numTripulantes--;
    }

    public void expulsarImpostor() {
        // TODO 7 Expulsar de la nave al tripulante impostor (lo eliminamos)
        for (int i = 0; i< numTripulantes; i++){
            if (tripulacion[i].getIdentificador() == impostor.getIdentificador()){
                eliminarTripulante(i);
            }
        }
    }

    public int expulsarSospechoso() {
        // TODO 8 Los tripulantes de la nave se reunen e intentan determinar
        //        quien es el impostor para echarle de la nave.
        int opcion = aleatorio(0,3);
        int numeroAleatorio;
        int identificador;
        if (opcion == 0) {
            identificador = -1;
        }else{
            numeroAleatorio = aleatorio(0,numTripulantes);
            System.out.println("Se ha decidido echar de la nave al tripulante "
                    +tripulacion[numeroAleatorio].getIdentificador()+" por sospechoso");
            identificador = tripulacion[numeroAleatorio].getIdentificador();
            eliminarTripulante(numeroAleatorio);
        }
        return identificador;
    }

    public void guardarEstadoNave(String ruta) {
        // TODO 9 Almacenar en un fichero el estado de la nave
        PrintWriter salida = null;
        try {
        salida = new PrintWriter(ruta);
        salida.println("Clave del reactor: "+claveReactor.toString());
        salida.println("Tripulacion de la nave: "+TOTAL_TRIPULANTES);
        salida.println("Tripulantes eliminados: "+(TOTAL_TRIPULANTES - numTripulantes));
        salida.print("Tripulantes supervivientes: ");
        if (numTripulantes > 0) {
            for (int i = 0; i < numTripulantes; i++) {
                salida.printf("\n \t" + tripulacion[i].toString());
            }
        }
        }catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado");
        }finally {
            if (salida != null){
                salida.close();
            }
        }
    }

    public static void main(String[] args) {
        int numTripulantes = Teclado.leerEntero(5, 15,
                "¿Cuántos tripulantes tendrá la nave (min=5 y max=15)? ");
        int longitudClave = Teclado.leerEntero(2, 4,
                "¿Cuál es el tamaño de cada parte de la clave del reactor (min=2 y max=4)? ");
        Juego juego = new Juego(numTripulantes, longitudClave);
        juego.jugar();
    }
}
