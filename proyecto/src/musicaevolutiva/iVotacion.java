/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author malebb
 */
public class iVotacion {
    
    private ArrayList<String> individuos;
    private ArrayList<Integer> aptitudes;
    private ArrayList<Integer> nVotantes;
    
    public ArrayList<String> getIndividuos(){
        return individuos;
    }
    
    public ArrayList<Integer> getAptitudes(){
        return aptitudes;
        
    }
    
    public ArrayList<Integer> getNVotanteas(){
        return nVotantes;
    }
    
    public static void crearVotacion(){
        
        File Dir = new File("./newGeneration/");
        File[] lista_Archivos = Dir.listFiles();
        
        File listado=new File("./ratingtxt/listado.txt");
        File listVotacion= new File("./ratingtxt/rtgitems.txt");
        
        try{
            FileWriter writeListado = new FileWriter(listado);
            FileWriter writeVotacion = new FileWriter(listVotacion);
            BufferedWriter bwl = new BufferedWriter(writeListado);
            BufferedWriter bwv = new BufferedWriter(writeVotacion);
            PrintWriter wrl = new PrintWriter(bwl);
            PrintWriter wrv = new PrintWriter(bwv);
            
            wrv.write("");
            wrl.write(lista_Archivos.length+"\n");
            
            for(int i=0;i<lista_Archivos.length;i++){
                wrl.write(lista_Archivos[i].getName()+" \n");
                wrv.write("rt_"+lista_Archivos[i].getName()+"^1^1\n");
            }
            
            wrl.close();
            bwl.close();    
            wrv.close();
            bwv.close();    
        }catch(IOException e){};
        
    }
    
    public void leerVotacion(){
         File archivo=new File("./ratingtxt/rtgitems.txt");
         individuos=new ArrayList();
         aptitudes=new ArrayList();
         nVotantes=new ArrayList();
         
         try{
            FileReader reader = new FileReader(archivo);
            BufferedReader br = new BufferedReader(reader);
            StringTokenizer token;
            String linea;
            while((linea=br.readLine())!=null){
                token=new StringTokenizer(linea,"_^");  
                token.nextToken();
                individuos.add(token.nextToken());
                aptitudes.add(Integer.parseInt(token.nextToken()));
                //nVotantes.add(Integer.parseInt(token.nextToken()));
            }                      
            br.close();    
            
        }catch(IOException e){};
        
    }
    
    public static void leer(){
         File archivo=new File("./ratingtxt/rtgitems.txt");
         
         try{
            FileReader reader = new FileReader(archivo);

            BufferedReader br = new BufferedReader(reader);
            StringTokenizer token;
            String linea;
            while((linea=br.readLine())!=null){
                token=new StringTokenizer(linea,"_^");  
                token.nextToken();
                System.out.println("Individuo: "+token.nextToken());
                System.out.println("Aptitud:"+token.nextToken());
                System.out.println("N.Votantes:"+token.nextToken());
            }                      
            br.close();    
            
        }catch(IOException e){System.out.println("error");}
        
    }
    
    
    public static void main(String args[]){
       crearVotacion();

    }
    
    
}
