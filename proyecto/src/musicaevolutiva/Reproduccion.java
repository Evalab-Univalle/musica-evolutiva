/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

/**
 *
 * @author malebb
 */
public class Reproduccion {
    private double[] madre;
    private double[] padre;
    private int tamHijo;
    private double[] hijo;
    
    public void Reproduccion(){
        
    }
    
    public Cromosome getHijo(Cromosome madre, Cromosome padre){
        this.madre=madre.GetCromosome();
        this.padre=padre.GetCromosome();
        int tipo=(int)Math.ceil(Math.random()*5);
        tipo=5;
        switch(tipo){
            case 1:
                tipo1();
                break;
            case 2:
                tipo2();
                break;
            case 3:
                tipo3();
                break;
            case 4:
                tipo4();
                break;
            case 5:
                tipo5();
                break;
        }               
        return new Cromosome(Mutacion.Mutar(hijo));
    }
    
    //Mezcla
    private void tipo1(){
        tamHijo=madre.length;
        hijo=new double[tamHijo];
        
        if(madre.length<=padre.length){
            for(int i=0;i<madre.length;i++){
                hijo[i]=(madre[i]+padre[i])/2;
            }
        }
        else{
            int difMadrePadre=madre.length-padre.length;
            int init=(int)Math.floor(Math.random()*difMadrePadre);
            System.arraycopy(madre, 0, hijo, 0, madre.length);
            
            for(int i=0;i<padre.length;i++){
                hijo[init+i]=(hijo[init+i]+padre[i])/2;
            }
        }
    }   
    
    //particion miti-miti
    private void tipo2(){
        tamHijo=(int)Math.max(padre.length, madre.length);
        hijo=new double[tamHijo];
        int posParticion=(int)Math.floor(Math.random()*tamHijo);
        for(int i=0;i<posParticion&&i<padre.length;i++){
            hijo[i]=padre[i];
        }
        for(int i=posParticion, j=0;j<madre.length&&i<tamHijo;i++,j++){
            hijo[i]=madre[j];
        }
    } 
    
    //particion aleatoria entre cero y un segundo
    private void tipo3(){
        
        if(madre.length<=padre.length){
            tamHijo=madre.length;
            hijo=new double[tamHijo];
            System.arraycopy(madre, 0, hijo, 0, tamHijo);
        }else{
            tamHijo=padre.length;
            hijo=new double[tamHijo];
            System.arraycopy(padre, 0, hijo, 0, tamHijo);
        }
        
        int tamParte=(int)Math.floor(Math.random()*StdAudio.SAMPLE_RATE);
        for(int i=0; i+tamParte<tamHijo;){
            int madreOPadre=(int)Math.ceil(Math.random()*2);
            if(madreOPadre==1){
                System.arraycopy(madre, i, hijo, i, tamParte);
            }else{
                System.arraycopy(padre, i, hijo, i, tamParte);
            }
            i+=tamParte;
            
            tamParte=(int)Math.floor(Math.random()*StdAudio.SAMPLE_RATE);
        }
        
    }
    
//    //combinacion entre mezcla y particion aleatoria
    private void tipo4(){
        tipo3();
        int tamParte=(int)Math.floor(Math.random()*StdAudio.SAMPLE_RATE);
        for(int i=0; i+tamParte<tamHijo;){
            int madrePadre=(int)Math.ceil(Math.random()*3);
            if(madrePadre==1){
                System.arraycopy(madre, i, hijo, i, tamParte);
            }else if(madrePadre==2){
                System.arraycopy(padre, i, hijo, i, tamParte);
            }
            i+=tamParte;
            tamParte=(int)Math.floor(Math.random()*StdAudio.SAMPLE_RATE);
        }
    }
    
    private void tipo5(){
        tamHijo=Math.min((madre.length+padre.length),(StdAudio.SAMPLE_RATE*20));

        hijo=new double[tamHijo];
        
        for(int i=0;i<madre.length&&i<tamHijo;i++){
            hijo[i]=madre[i];
        }
        for(int i=0;i<padre.length&&(madre.length+i)<tamHijo;i++){
            hijo[madre.length+i]=padre[i];
        }       
    }
    
    
}
