/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author usuario
 */
public class intIO {
    
    private static String carpetaNueva="./newGeneration/";
    private static String carpetaVieja="./oldGeneration/";
    private static File madre;
    private static File padre;
   
    public static void moverGeneracion(){
        File Dir = new File(carpetaNueva);
        File[] lista_Archivos = Dir.listFiles();
        
        File archivo=new File("votacion.txt");

        for(int i=0;i<lista_Archivos.length;i++){
            File nuevo=new File(carpetaVieja+lista_Archivos[i].getName());
            lista_Archivos[i].renameTo(nuevo);
        }
    }
    
    public static void pasarSobrevivientes(String nombreSobreviviente, int numeroIndividuo){
        double [] sobreviviente=StdAudio.read(carpetaVieja+nombreSobreviviente);
        StdAudio.save(carpetaNueva+"sobreviviente"+numeroIndividuo+".wav", sobreviviente);
        
    }
    
    public static void nuevoIndividuo(int numeroIndividuo){
        File Dir = new File(carpetaNueva);
        File[] lista_Archivos = Dir.listFiles();
        double[] muestreoHijo;
        double[] muestreoMadre;
        double[] muestreoPadre;
        Cromosome cromMadre;
        Cromosome cromPadre;
        Cromosome cromHijo;
        madre=lista_Archivos[(int)(Math.floor(Math.random()*lista_Archivos.length))];
        padre=lista_Archivos[(int)(Math.floor(Math.random()*lista_Archivos.length))];
        muestreoMadre=StdAudio.read(carpetaNueva+madre.getName());
        muestreoPadre=StdAudio.read(carpetaNueva+padre.getName());
        
        cromMadre= new Cromosome(muestreoMadre);
        cromPadre= new Cromosome(muestreoPadre);
        Reproduccion r=new Reproduccion();
        cromHijo=r.getHijo(cromMadre,cromPadre);
        
        muestreoHijo=cromHijo.GetCromosome();
        StdAudio.save(carpetaNueva+"nuevo"+numeroIndividuo+".wav", muestreoHijo);
        
    }
    
    //copiado de http://www.nosolounix.com/2010/02/programacion-java-copiar-archivos.html
    private static void copyFile(File s, File t)
    {
        try{
              FileChannel in = (new FileInputStream(s)).getChannel();
              FileChannel out = (new FileOutputStream(t)).getChannel();
              in.transferTo(0, s.length(), out);
              in.close();
              out.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    
}
