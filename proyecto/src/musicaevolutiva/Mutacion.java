/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;
/**
 *
 * @author usuario
 */
public class Mutacion {
    
    private Mutacion(){}
    
    private static double[] Amplificar(double[] wave){       
        double[] ampWave=new double[wave.length];
        double factor=Math.random()+1;
        
        for(int i=0;i<wave.length;i++){
            ampWave[i]=wave[i]*factor;
        }
        
        return ampWave;         
    }
    
    private static double[] Echoe(double[] wave){
        int retardo= (int)((100+(Math.random()*900))*(StdAudio.SAMPLE_RATE/1000));
        double[] Echoe =new double[wave.length];
         double coefAbsorcion=Math.random();
        
        System.arraycopy(wave, 0, Echoe, 0, wave.length);
        for(int i=retardo;i<wave.length;i++){
            Echoe[i]=(Echoe[i]+(wave[i-retardo]*coefAbsorcion))/2;
        }
        
        return Echoe;
    }
    
    private static double[] Echoe2(double [] wave){
        double[] Echoe =new double[wave.length];
        int nEchoes= (int)Math.ceil(Math.random()*10);
        int retardo= (int)((100+(Math.random()*900))*(StdAudio.SAMPLE_RATE/1000));
        double coefAbsorcion=Math.random();
        
        System.arraycopy(wave, 0, Echoe, 0, wave.length);
        for(int i=1;i<nEchoes;i++){
            for(int j=retardo*i;j<wave.length;j++){
                Echoe[j]=(Echoe[j]+(wave[j-(retardo*i)]*Math.pow(coefAbsorcion, i)))/2;
            }
        }    
        
        return Echoe;        
    }
    
    private static double[] Inversor(double[] wave){  
        double[] inverso=new double[wave.length];
     
        for(int i=0;i<wave.length;i++){
            inverso[i]=wave[wave.length-1-i];
        }
        
        return inverso;
    }
    
    
    private static double[] Intercambio(double[] wave){
        int pos1=(int)Math.floor(Math.random()*wave.length);
        int pos2=(int)Math.floor(Math.random()*wave.length);
        int posIni, posFin;
        int lado=(int)Math.floor(Math.random()*2);
        double[] intercambio=new double[wave.length];
                
        if(pos1>pos2){
            posIni=pos2;
            posFin=pos1;
        }else{
            posIni=pos1;
            posFin=pos2;
        }
        
       
        if(lado==0){
            for(int i=posIni;i<=posFin;i++){
                intercambio[i-posIni]=wave[i];
            }
            for(int i=0;i<posIni;i++){
                intercambio[posFin-posIni+i]=wave[i];
            }
            for(int i=posFin;i<wave.length;i++){
                intercambio[i]=wave[i];
            }
        }
        else if(lado==1){
            for(int i=0;i<posIni;i++){
                intercambio[i]=wave[i];
            }
            for(int i=posFin,j=0; i<wave.length; i++,j++){
                intercambio[posIni+j]=wave[i];
            }
            for(int i=posIni;i<posFin;i++){
                intercambio[wave.length-posFin+i]=wave[i];
            }
        }        
        
        return intercambio;
    }
    
    private static double[] MultiplicarOndas(double[] wave){   
        double[] producto=new double[wave.length];
        int tamVirus=(int)Math.ceil(Math.random()*wave.length);
        Cromosome virus=new Cromosome(tamVirus);
        virus.GetCromosome();
        double[] waveB=virus.GetCromosome();
        
        System.arraycopy(wave, 0, producto, 0, wave.length);
        for(int i=0;i<waveB.length;i++){
            producto[i]*=waveB[i];
        }
        
        return producto;
    }
    
    private static double[] PasoAlto        (double[] wave){     
        double[] norma=new double[wave.length];
        
        for(int i=0;i<norma.length-2;i++){
            norma[i]=(wave[i+1]-wave[i])/2;
        }
        
        return norma;
    }
    
    private static double[] PasoBajo        (double[] wave){
        int base = (int)(Math.random()*3)+2;
        double[] norma=new double[wave.length];
        
        for(int i=0;i<norma.length-base;i++){
            double acum=0;
            for(int j=0;j<base;j++){
                acum+=wave[i+j];
            }
            norma[i]=acum/base;
        }   
        
        return norma;
    }
    
    private static double[] mutarCPrimeraG(double[] wave) {
        double[] mutado=new double[wave.length];
        return mutado;
    }   
    
    public  static double[] Mutar           (double[] waveIn){
        double[] wave=new double[waveIn.length];
        System.arraycopy(waveIn, 0, wave, 0, waveIn.length);
        int tamMutacion=Math.min((int) (Math.ceil(Math.random()*(wave.length-(StdAudio.SAMPLE_RATE/10)))+(StdAudio.SAMPLE_RATE/10)), waveIn.length);
        int init=(int)Math.floor(Math.random()*(wave.length-tamMutacion));
        int tipo=(int)Math.ceil(Math.random()*9);//numero de funciones
//        System.err.println("Tam Mutacion: "+tamMutacion+" inicia en: "+init);
        double[] seccionAMutar  = new double[tamMutacion];   
      
        for(int i=0;i<tamMutacion;i++){
            seccionAMutar[i]=wave[init+i];
        }
        System.out.println(tipo);
        switch(tipo){
            case 1:
                seccionAMutar=Amplificar(seccionAMutar);
                break;
            case 2:
                seccionAMutar=Echoe(seccionAMutar);
                break;
            case 3:
                seccionAMutar=Echoe2(seccionAMutar);
                break;
            case 4:
                seccionAMutar=Inversor(seccionAMutar);
                break;
            case 5:
                seccionAMutar=Intercambio(seccionAMutar);
                break;
            case 6:
                seccionAMutar=MultiplicarOndas(seccionAMutar);
                break;
            case 7:
                seccionAMutar=PasoAlto(seccionAMutar);
                break;
            case 8:
                seccionAMutar=PasoBajo(seccionAMutar);
                break;
            case 9:
                break;//no hacer nada
                    
        }
        for(int i=0;i<seccionAMutar.length;i++){
            wave[init+i]=seccionAMutar[i];
        }
  
        return wave;
    }
    
}
