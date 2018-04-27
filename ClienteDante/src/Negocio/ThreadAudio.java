/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import Datos.Configuracion;
import Datos.XmlRead;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import Interfaz.Panel;
import java.awt.Color;
import javax.swing.JSlider;
import java.math.*;
import java.net.SocketException;
import javax.swing.JButton;

/**
 *
 * @author leone
 */
public class ThreadAudio extends Thread{
    private AudioFormat format;
    private SourceDataLine sourceline;
    private Configuracion Conf;
    private XmlRead X;
    private String puerto;
    private String multicast;
    private InetAddress grupo;
    private MulticastSocket socket;
    private byte[] buffer=new byte[8192];
    private byte[] sonido;
    private DatagramPacket PaqueteCliente;
    private Panel P;
    private int Frecuencia;
    private int Muestra;
    private volatile boolean continuar=false;
    private int numerodecanal;
    private JSlider V;
    private JButton B;
    
    public ThreadAudio(String puerto,String multicast,Panel P,int Frecuencia,int muestra,int numerocanal,JSlider V,JButton B){
       
        format=new AudioFormat(Frecuencia, 16, 1, true, true);
        
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
        this.puerto=puerto;
        this.multicast=multicast;
        this.P=P;
        this.Frecuencia=Frecuencia;
        this.Muestra=muestra;
        this.numerodecanal=numerodecanal;
        this.V=V;
        this.numerodecanal=numerocanal;
        this.B=B;
          
        if (!AudioSystem.isLineSupported(info)){
                try {
                    System.out.println("Line matching " + info + " is not supported.");
                    P.SetLog("Error de tarjeta de audio ");
                    P.SetMSG("Error de Audio",true);
                    throw new LineUnavailableException();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(ThreadAudio.class.getName()).log(Level.SEVERE, null, ex);
                     P.SetLog("Error de tarjeta de audio ");
                     P.SetMSG("Error de Audio",true);
                     
                    //audio no soportado
                }
                B.setBackground(Color.red);
            }
        
        
         try {
             sourceline = (SourceDataLine)AudioSystem.getLine(info);
             sourceline.open(format);
             sourceline.start();
             
         } catch (LineUnavailableException ex) {
             Logger.getLogger(ThreadAudio.class.getName()).log(Level.SEVERE, null, ex);
             //error de audio
             B.setBackground(Color.red);

         }
                 
    }
    
    //audio ya instanciado
    
    @Override
    public void run(){
        try {
           // socket=new MulticastSocket(Integer.parseInt(puerto));
           socket=new MulticastSocket(Integer.parseInt(puerto));
            grupo=InetAddress.getByName(this.multicast);
          
            socket.joinGroup(grupo);
            socket.setSoTimeout(5000);
            
            int canal=selectorAudio(numerodecanal);
            sonido=new byte[Muestra*2];
            
            //cliclo while de recepcion de audio
            PaqueteCliente=new DatagramPacket(buffer, 0, buffer.length,grupo,Integer.parseInt(puerto));
             System.out.println("asignado el puerto  "+Integer.parseInt(puerto));
             P.SetLog("Asignado el puerto "+Integer.parseInt(puerto));          
             P.SetMSG("INICIO DE AUDIO", true);
             
             System.out.println(Muestra);
             System.out.println(canal);
             System.out.println(sonido.length);
            while(!continuar){
              
                socket.receive(PaqueteCliente);          
                
                for(int x=0;x<(Muestra*2);x++){
                    sonido[x]=(byte) (((byte)PaqueteCliente.getData()[x+canal]));
                }
                
              
                sourceline.write(sonido, 0,sonido.length);
               
            }
             System.out.println("hilo finalizado ");    
             P.SetLog("Audio Finalizado ");
             socket.disconnect();
             socket.close();
             P.SetMSG("FIN DE AUDIO", true);
        } catch (IOException ex) {
            Logger.getLogger(ThreadAudio.class.getName()).log(Level.SEVERE, null, ex);
            //error de RED
            System.out.println("tiempo de respuesta expirado");
            P.SetLog("Tiempo de respuesta expirado  ");
            P.SetMSG("FIN DE AUDIO, TIMEOUT alcanzado", true);
            P.TurnOnSend();
            P.TurnOffStop();
            B.setBackground(Color.red);

        }
        
        
          P.TurnOnSend();
            P.TurnOffStop();
        System.out.println("FINALIZADO");
    }
    
     public void detener(){
      System.out.println("hilo detenido ");
        P.SetLog("Audio detenido  ");
        P.SetMSG("FIN DE AUDIO", true);
        continuar=true;
        sourceline.flush();      
        socket.close();
      //    P.TurnOnSend();
      //    P.TurnOffStop();
 }
     
    public int selectorAudio(int canal){
        int cuenta=0;
        cuenta=canal*Muestra*2;
        return cuenta;
    }
    
}
