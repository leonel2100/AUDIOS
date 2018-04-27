/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;
import Interfaz.Panel;
import Interfaz.PanelMixer;
 import Interfaz.PanelAjustes;

import Datos.XmlRead;
import Datos.Configuracion;
/**
 *
 * @author leone
 */
public class ClienteDante {

    /**
     * @param args the command line arguments
     */
    static Panel P;
   static ThreadStart start;
 
    public static void main(String[] args) {
        // TODO code application logic here
     
       P=new Panel();
       start=new ThreadStart(P);
       start.start();
     
       P.setVisible(true);
       
        
    }
    
}