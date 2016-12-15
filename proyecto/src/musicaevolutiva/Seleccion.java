/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

import java.util.ArrayList;


/**
 *
 * @author malebb
 */

public class Seleccion {

    private ArrayList<String> individuos;
    private ArrayList<Integer> aptitudes;
    private ArrayList<String> sobrevivientes;
    private int porcentaje=80;
    
    public Seleccion(){
        
    }
    
    public void getNewGeneration(){
        iVotacion votacion= new iVotacion();
        votacion.leerVotacion();
        individuos=votacion.getIndividuos();
        aptitudes=votacion.getAptitudes();
        generacionSobrevivientes();
        intIO.moverGeneracion();
        
        for(int i=0; i<sobrevivientes.size();i++){
            intIO.pasarSobrevivientes(sobrevivientes.get(i), i);
        }
        
        for(int i=0; i<individuos.size();i++){
            intIO.nuevoIndividuo(i);
        }
        
        iVotacion.crearVotacion();
        
    }
    
    protected void generacionSobrevivientes(){
        int zAptitud=0;
        sobrevivientes=new ArrayList();
        
        int cantidadSobrevivientes=(int)Math.ceil(individuos.size()*porcentaje/100);
        
        for(int i=0;i<aptitudes.size();i ++){
            zAptitud+=aptitudes.get(i);
        }
        
        
        for(int i=0; i<cantidadSobrevivientes;i++){ 
            int a=(int)Math.ceil(Math.random()*zAptitud);
            int restantes=aptitudes.size();
            int b=0;
 
            for(int j=0;j<=restantes;j++){
                b+=aptitudes.get(j);
                if(a<=b){
                    sobrevivientes.add(individuos.get(j));
                    zAptitud-=aptitudes.get(j);
                    aptitudes.remove(j);
                    individuos.remove(j);
                    j=restantes+1;
                }
            }
        }
        
        
    }
    
}
