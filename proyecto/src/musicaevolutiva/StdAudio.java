/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicaevolutiva;

/*************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *  
 *  Simple library for reading, writing, and manipulating .wav files.

 *
 *  Limitations
 *  -----------
 *    - Does not seem to work properly when reading .wav files from a .jar file.
 *    - Assumes the audio is monaural, with sampling rate of 44,100.
 *
 *************************************************************************/

import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.sound.sampled.*;

/**
 *  <i>Standard audio</i>. This class provides a basic capability for
 *  creating, reading, and saving audio. 
 *  <p>
 *  The audio format uses a sampling rate of 44,100 (CD quality audio), 16-bit, monaural.
 *
 *  <p>
 *  For additional documentation, see <a href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public final class StdAudio {

    /**
     *  The sample rate - 44,100 Hz for CD quality audio.
     */
    //public static final int SAMPLE_RATE = 44100;
    public static final int SAMPLE_RATE = 22000;

    private static final int BYTES_PER_SAMPLE = 1;                // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;    // number of samples currently in internal buffer

    // do not instantiate
    private StdAudio() { }

   
    // static initializer
    static { init(); }
    // open up an audio stream
    private static void init() {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            
            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets divided because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE/3];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // no sound gets made before this call
        line.start();
    }
    /**
     * Close standard audio.
     */
    public static void close() {
        line.drain();
        line.stop();
    }
    /**
     * Write one sample (between -1.0 and +1.0) to standard audio. If the sample
     * is outside the range, it will be clipped.
     */
    public static void play(double in) {

        // clip if outside [-1, +1]
        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little Endian

        // send to sound card if buffer is full        
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }
    /**
     * Write an array of samples (between -1.0 and +1.0) to standard audio. If a sample
     * is outside the range, it will be clipped.
     */
    public static void play(double[] input) {
        for (int i = 0; i < input.length; i++) {
            play(input[i]);
        }
    }
    /**
     * Read audio samples from a file (in .wav or .au format) and return them as a double array
     * with values between -1.0 and +1.0.
     */
    public static double[] read(String filename) {
        byte[] data = readByte(filename);
        int N = data.length;
        double[] d = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            d[i] = ((short) (((data[2*i+1] & 0xFF) << 8) + (data[2*i] & 0xFF))) / ((double) MAX_16_BIT);
        }
        return d;
    }
    /**
     * Play a sound file (in .wav, .mid, or .au format) in a background thread.
     */
    public static void play(String filename) {
        URL url = null;
        try {
            File file = new File(filename);
            if (file.canRead()) url = file.toURI().toURL();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        // URL url = StdAudio.class.getResource(filename);
        if (url == null) throw new RuntimeException("audio " + filename + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }
    /**
     * Loop a sound file (in .wav, .mid, or .au format) in a background thread.
     */
    public static void loop(String filename) {
        URL url = null;
        try {
            File file = new File(filename);
            if (file.canRead()) url = file.toURI().toURL();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        // URL url = StdAudio.class.getResource(filename);
        if (url == null) throw new RuntimeException("audio " + filename + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.loop();
    }
    // return data as a byte array
    private static byte[] readByte(String filename) {
        byte[] data = null;
        AudioInputStream ais = null;
        try {

            // try to read from file
            File file = new File(filename);
            if (file.exists()) {
                ais = AudioSystem.getAudioInputStream(file);
                data = new byte[ais.available()];
                ais.read(data);
            }

            // try to read from URL
            else {
                URL url = StdAudio.class.getResource(filename);
                ais = AudioSystem.getAudioInputStream(url);
                data = new byte[ais.available()];
                ais.read(data);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not read " + filename);
        }

        return data;
    }
    /**
     * Save the double array as a sound file (using .wav or .au format).
     */
    public static void save(String filename, double[] input) {

        // assumes 44,100 samples per second
        // use 16-bit audio, mono, signed PCM, little Endian
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        byte[] data = new byte[2 * input.length];
        for (int i = 0; i < input.length; i++) {
            int temp = (short) (input[i] * MAX_16_BIT);
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);
        }

        // now save the file
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, input.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else {
                throw new RuntimeException("File format not supported: " + filename);
            }
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
/*
 
 De aqui en adelante todo es nuevo
 
 */
    public static double[] Senosoidales(double frecuence, int duration, double amplitude){
        int N = duration;
        double[] wave=new double[N+1];
        
        for (int i=0;i <N;i++){ 
           wave[i]=amplitude* Math.sin(2*Math.PI*i*frecuence/StdAudio.SAMPLE_RATE);
        }
        return wave;                
     }
    
    public static double[] Triangulares(double frecuence, int duration, double amplitude){
        int N = duration;

        double[] wave=new double[N+1];
        double pendiente=(amplitude*frecuence)/(2*StdAudio.SAMPLE_RATE);        
        double y=-amplitude;
        for(int i=0;i<N;i++){
            y+=pendiente;
            wave[i]=y;        
            if(y+pendiente>amplitude){
                pendiente=-pendiente;
            }else if(y+pendiente<-amplitude){
                    pendiente=-pendiente;
            }
        }
        
        return wave;
    }
    
    public static double[] Cuadradas(double frecuence, int duration, double amplitude){       
        int N = duration;
        double[] wave=new double[N+1];
        double sentido=1;
        int acum=0;
        for(int i=0;i<=N;i++){
            if(acum>(2*StdAudio.SAMPLE_RATE)/frecuence){
                acum=0;
                sentido*=-1;
            }
            acum++;
            wave[i]=(byte)(sentido*amplitude);
        }       
        return wave;   
    }
    
    public static double[] DienteSierra(double frecuence,int duration, double amplitude){
        int N = duration;
        double[] wave=new double[N+1];
        double pendiente=(amplitude*frecuence)/StdAudio.SAMPLE_RATE;
        double y=-amplitude;
        for(int i=0;i<=N;i++){
            y+=pendiente;
            wave[i]=y;        
            if(y+pendiente>amplitude){
                y=-amplitude;
            }
        }
        return wave;
    }  
    
    public static double[] RuidoBlanco(int duration, double amplitude){
        int N = duration;
        double[] wave=new double[N+1];
        for(int i=0; i<=N;i++){
           double aleatorio1=Math.random();
           if(aleatorio1>0.5){
              wave[i]=(Math.random()*amplitude); 
           }
           else{
              wave[i]=(Math.random()*-amplitude);
           }
        }
        return wave;
    }
    
    public static double[] RuidoRosa(int duration, double amplitude){
        int N = duration;
        double[] wave=new double[N+1];
        double alt1,alt2,alt3;
        alt1=(Math.random()*amplitude)-(amplitude/2);
        alt2=(Math.random()*amplitude)-(amplitude/2);
        alt3=(Math.random()*amplitude)-(amplitude/2);
        
        for(int i=0; i<=N;i++){
            wave[i]=((alt1+alt2+alt3)/3);
            alt3=alt2;
            alt2=alt1;
            alt1=(Math.random()*amplitude)-(amplitude/2);
        }
        return wave;
    }
    
    public static double[] Silencio(int duration){
        int N = duration;
        double[] wave=new double[N+1];
        for(int i=0;i<=N;i++){
            wave[i]=0;
        }
        return wave;
    }

    
//     public static void crearIniciales(){
//        
//        String directorio="./sonidos/";
//        File Dir = new File(directorio);
//        File[] lista_Archivos = Dir.listFiles();
//           
//        for(int i=0;i<lista_Archivos.length;i++){
//            int silencio=(int) Math.floor(Math.random()*SAMPLE_RATE);
//            System.out.println(lista_Archivos[i].getName());
//            double[] sonido=read(directorio+lista_Archivos[i].getName());
//            double[] sonidoRepetido=new double[440000];
//            for(int j=0;j<sonidoRepetido.length;j++){
//                for(int k=0;k<sonido.length && j<sonidoRepetido.length;k++, j++){
//                    sonidoRepetido[j]=sonido[k];
//                   
//                }
//                j+=silencio;
//            }
//            
//            save("./sonidos2/"+lista_Archivos[i].getName(),sonidoRepetido);
//            
//        }
//
//    }
     
//     
//     public static void main(String args[]){
//         crearIniciales();
//     }
    /**
     * Test client - play an A major scale to standard audio.
     */
}