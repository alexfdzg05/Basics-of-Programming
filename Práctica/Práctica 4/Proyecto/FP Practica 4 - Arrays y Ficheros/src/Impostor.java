public class Impostor {
    private final Clave[] claves;
    private int numClaves;
    private final Tripulante tripulanteImpostor;

    public Impostor(int numTripulantes, Tripulante tripulanteImpostor) {
        // TODO 1 Completar constructor inicializando convenientemente los atributos
        numClaves = 1;
        claves = new Clave[numTripulantes];
        this.tripulanteImpostor = tripulanteImpostor;
        claves[tripulanteImpostor.getIdentificador()- 100] = tripulanteImpostor.getClave();
    }

    public int getIdentificador(){
        return tripulanteImpostor.getIdentificador();
    }

    public void robarClave(Tripulante tripulante) {
        // TODO 2 Robar la clave del tripulante
        claves[tripulante.getIdentificador() - 100] = tripulante.getClave();
        numClaves++;
        System.out.println("Has obtenido una nueva clave del reactor. Ya tienes las siguientes: ");
        mostrarClaves();
    }

    private void mostrarClaves() {
        // TODO 3 Visualizar las claves que tiene el impostor en su poder
        for (int i = 0; i < claves.length; i++) {
            if (claves[i] != null) {
                System.out.println(claves[i]);
            }
        }
    }

    public boolean quiereJugar() {
        return Teclado.leerBoolean("Quieres tratar de adivinar la clave del reactor (S/N)? ");
    }

    public boolean adivinaClave(Clave claveReactor) {
        boolean resultado;
        String claveLetras = Teclado.leerString("Introduce la parte alfabética de la clave: ");
        String claveDigitos = Teclado.leerString("Introduce la parte numérica de la clave: ");
        resultado = claveReactor.equals(new Clave(claveLetras, claveDigitos));
        if (!resultado) {
            System.out.println("Has fallado. La clave del reactor era " + claveReactor);
        }
        return resultado;
    }

}
