/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;
import com.sun.jndi.ldap.LdapName;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import pruebas.PruebaXML.*;
import xml.analizador.dom.*;
import xml.analizador.dom.modelo.Tag;
import xml.analizador.dom.modelo.Atributo;
import xml.analizador.dom.JespXML;

/**
 *
 * @author leone
 */
 

public class XmlWrite {
    
  
    public XmlWrite(){
    }
    
    public boolean write(Configuracion Conf,String file){
      
        try {
            // TODO code application logic here
             ArrayList<String> canalesConf=Conf.GetLista();  //lista de puertos de canales
             ArrayList<String> Alias=Conf.GetAlias();
            
            JespXML J=new JespXML("configuracion.xml");
            Tag raiz=new Tag("CONFIGURACION");
            Tag servidor=new Tag("SERVIDOR");
            Tag multicast=new Tag("MULTICAST");
            Tag serv=new Tag("SERV");
            Tag escape=new Tag("ESC");
            Tag Asio=new Tag("ASIO");
            Tag Frecuencia=new Tag("FRECUENCIA");
            Tag Muestra=new Tag("MUESTRA");
            ArrayList<Tag> canales=new ArrayList<Tag>();
           
            
            multicast.addContenido(Conf.GetMultiCast());
            serv.addContenido(Conf.GetServidor());         
            escape.addContenido(Conf.GetPuerto());
            
            for(int a=0;a<32;a++){
                canales.add(new Tag("CANAL"));
                canales.get(a).addAtributo("id",Alias.get(a));
                canales.get(a).addContenido(canalesConf.get(a));
                Asio.addTagHijo(canales.get(a));
            }
            
            Frecuencia.addContenido(Integer.toString(Conf.GetFrecuencia()));
            Muestra.addContenido(Integer.toString(Conf.GetMuestra()));
            Asio.addTagHijo(Frecuencia);
            Asio.addTagHijo(Muestra);
            
            raiz.addTagHijo(servidor);
            raiz.addTagHijo(Asio);
            
            servidor.addTagHijo(multicast);
            servidor.addTagHijo(serv);
            servidor.addTagHijo(escape);
            
            
            J.escribirXML(raiz);
            System.out.println("Escrito correctamente");
            return true;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlWrite.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlWrite.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (TransformerException ex) {
            Logger.getLogger(XmlWrite.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
      
    }
    
}
