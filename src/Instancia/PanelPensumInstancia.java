/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instancia;

import SubVentanas.frmPensum;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Gabriela
 */
public class PanelPensumInstancia {
    private static JFrame jf;
     public PanelPensumInstancia(){
        
    }
     private static void ubicacionVentana(JFrame jf,int tamanox,int tamanoy) { 
            int tamanioX = jf.getWidth(); 
            int tamanioY = jf.getHeight(); 
            int maxX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(); 
            int maxY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
             System.out.println(maxX+" "+maxY);
             System.out.println(tamanioX+" "+tamanioY);
            // ubicacion de la ventana 
            //JOptionPane.showMessageDialog(this, "Este el Valor de maxX= "+ maxY+"\n"+" Este es el valor maxY= "+ maxY+"\n"+"tamanio X= "+tamanioX+"\n"+"tamanio Y= "+tamanioY);
            jf.setLocation((maxX - tamanioX)-tamanox/2, ((maxY - tamanioY)-tamanoy)/2); 
        } 
    
    public static JFrame getInstance(int tamanox,int tamanoy){
        
        if(jf==null){
            jf=new frmPensum();
           PanelPensumInstancia.ubicacionVentana(jf,tamanox,tamanoy);
       }else{
           jf.dispose();
          jf=new frmPensum();
          PanelPensumInstancia.ubicacionVentana(jf,tamanox,tamanoy);
        }
            
        return jf;
    }
}
