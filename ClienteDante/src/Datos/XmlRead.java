    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.util.ArrayList;
/**
 *
 * @author leone
 */
public class XmlRead {
    
    private Configuracion Conf;
    private ArrayList<String> LIstaPuertos;
    private ArrayList<String> ListaAlias;
    
    public Configuracion Read(String URL){
        try {
            Conf=new Configuracion();
            LIstaPuertos=new ArrayList<>();
            ListaAlias=new ArrayList<>();
            
            File fXmlFile = new File(URL);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            
            doc.getDocumentElement().normalize();
            
            NodeList nodototal=doc.getElementsByTagName("CONFIGURACION");
            
            NodeList firstNameList = ((Element)nodototal.item(0)).getElementsByTagName("SERVIDOR");
            
            NodeList firstNameList2 = ((Element)firstNameList.item(0)).getElementsByTagName("MULTICAST");  //nodo multicast
            Node N0=firstNameList2.item(0);
            Element E0=(Element)N0;
            Conf.SetMultiCast(E0.getTextContent());  //establecer multicast
           
            firstNameList2 = ((Element)firstNameList.item(0)).getElementsByTagName("SERV");  
            Node N1=firstNameList2.item(0);
            Element E1=(Element)N1;
            Conf.SetServidor(E1.getTextContent());   //establecer servidor
          
            firstNameList2 = ((Element)firstNameList.item(0)).getElementsByTagName("ESC");
            Node N2=firstNameList2.item(0);
            Element E2=(Element)N2;
            Conf.SetPuerto(E2.getTextContent());   //establecer puerto de escucha
            
                                  
            firstNameList = ((Element)nodototal.item(0)).getElementsByTagName("ASIO"); 
            firstNameList2 = ((Element)firstNameList.item(0)).getElementsByTagName("CANAL");
             
             for(int i=0;i<firstNameList2.getLength();i++){
                 Node N=firstNameList2.item(i);
                 
                 Element E=(Element)N;
                 String descripcion=E.getAttribute("id");
               //  System.out.println(E.getAttribute("id"));
                 //System.out.println(E.getTextContent());
                 ListaAlias.add(descripcion);
                 LIstaPuertos.add(E.getTextContent());
             }
            Conf.SetLista(LIstaPuertos);
            Conf.SetAlias(ListaAlias);
            
            
            firstNameList2=((Element)firstNameList.item(0)).getElementsByTagName("FRECUENCIA");
            for(int i=0;i<firstNameList2.getLength();i++){
                 Node N=firstNameList2.item(i);
                 
                 Element E=(Element)N;
                 Conf.SetFrecuencia(Integer.parseInt(E.getTextContent()));
            }
            
             firstNameList2=((Element)firstNameList.item(0)).getElementsByTagName("MUESTRA");
            for(int i=0;i<firstNameList2.getLength();i++){
                 Node N=firstNameList2.item(i);
                 
                 Element E=(Element)N;
                 Conf.SetMuestra(Integer.parseInt(E.getTextContent()));
            }
 
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return Conf;
    }
    
}
