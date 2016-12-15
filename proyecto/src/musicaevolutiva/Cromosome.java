/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

/**
 *
 * @author malebb
 */

public class Cromosome {
    
    private double[] cromosome;
    
    public Cromosome(int size){
        cromosome= new double[size];
        NewCromosome();
    }
    
    public Cromosome(double[] cromosome){
        this.cromosome=cromosome;
    }
    
    public double[] GetCromosome(){
        return cromosome;
    }
    
    private void NewCromosome(){
        int position=0;
        do{
            
            int typeWave=(int)Math.ceil(Math.random()*7);
            
            int duration=(int)Math.floor(Math.random()*StdAudio.SAMPLE_RATE);//duracion entre 0 y 1 segundos
            double frecuence=(Math.random()*19980)+20;//El espectro audible del una persona joven va aproximadamente desde los 20hz hasta los 20khz
            double amplitude=Math.random();//la aplitud se presenta en un porcentaje y este influye en el volumen del sonido   
            double[] waveAux={};
            switch (typeWave){               
                case 1:                  
                    waveAux=StdAudio.Senosoidales(frecuence, duration, amplitude);
                    break;
                case 2:
                    waveAux=StdAudio.Triangulares(frecuence, duration, amplitude);
                    break;
                case 3:
                    waveAux=StdAudio.Cuadradas(frecuence, duration, amplitude);                            
                    break;
                case 4:
                    waveAux=StdAudio.DienteSierra(frecuence, duration, amplitude);
                    break;
                case 5:
                    waveAux=StdAudio.RuidoBlanco(duration, amplitude);              
                    break;
                case 6:
                    waveAux=StdAudio.RuidoRosa(duration, amplitude);              
                    break;
                case 7:
                    waveAux=StdAudio.Silencio(duration);
                    break;    
            }
            for(int i=0;i<waveAux.length&&position+i<cromosome.length;i++){
                cromosome[i+position]=waveAux[i];
            }
            position+=duration;
           
        }while(position<cromosome.length);
    }  
    
}
