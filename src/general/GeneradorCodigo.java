package general;

import java.util.Arrays;
import java.util.Collections;

public class GeneradorCodigo {
    private static String Mayusculas = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private static int cantidadMayusculas = 4;
    //private String Minusculas = "abcdefghijklmnopqrstuvwxyz";
    //private int cantidadMinusculas = 2;
    private static String Numeros = "1234567890";
    private static int cantidadNumeros = 4;
    //private static int cantidadEspeciales=2;
    //private static String caracteresEsp ="+-,*/=%&#!?.}";
    
    
    public GeneradorCodigo() {
    }
    
    public static String generarCodigoOperacion(){
        String codigo="";
        codigo=aleatorio(codigo, Mayusculas, 4);
        codigo=aleatorio(codigo, Numeros, 4);
        codigo=Shuffle(codigo);
        return codigo;
    }
    
    public static String generarCodigoMedio(){
        String codigo="";
        codigo=aleatorio(codigo, Mayusculas, 5);
        codigo=aleatorio(codigo, Numeros, 3);
        codigo=Shuffle(codigo);
        return codigo;
    }
    
    private static String aleatorio(String resultado, String palabras, int numero){
        String[] arrayPalabra = palabras.split("");
        int numeroAleatorio;
        for(int i=0;i<numero;i++){
            numeroAleatorio=(int)(Math.random()*(arrayPalabra.length-1));
            resultado=resultado+arrayPalabra[numeroAleatorio];
        }
        return resultado;
    }
    
    private static String Shuffle(String s){
        String[] arrayS=s.split("");
        Collections.shuffle(Arrays.asList(arrayS));
        s="";
        
        for (int i = 0; i < arrayS.length; i++) {
            s+=arrayS[i];
        }
        return s;
    }
}
