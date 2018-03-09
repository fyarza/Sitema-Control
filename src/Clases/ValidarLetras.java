/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.event.KeyEvent;

/**
 *
 * @author federico
 */
public class ValidarLetras {
    Character s;
    public String sololetrasynumero(KeyEvent e){
        String resul="";
        s=e.getKeyChar();
        if(!Character.isLetter(s) && !Character.isDigit(s) && 
         s!=KeyEvent.VK_BACK_SPACE && s!=KeyEvent.VK_DELETE 
                && s!=KeyEvent.VK_SPACE){
            e.consume();
            resul="Solo Letras y numeros";
        }
        return resul;
    }
    
    public String solonumero(KeyEvent e){
        String resul="";
        s=e.getKeyChar();
        if(!Character.isDigit(s) && 
         s!=KeyEvent.VK_BACK_SPACE && s!=KeyEvent.VK_DELETE 
                ){
            e.consume();
            resul="Solo numeros";
        }
        return resul;
    }
    public String sololetras(KeyEvent e){
        String resul="";
        s=e.getKeyChar();
        if(!Character.isLetter(s) &&
         s!=KeyEvent.VK_BACK_SPACE && s!=KeyEvent.VK_DELETE 
                && s!=KeyEvent.VK_SPACE){
            e.consume();
            resul="Solo Letras";
        }
        return resul;
    }
    
    public String tipo_titulo(String cadena){
        char[] caracteres=cadena.toCharArray();
        caracteres[0]=Character.toUpperCase(caracteres[0]);
        for (int i = 0; i < cadena.length()-2 ; i++) {
            if(caracteres[i]==' '|| caracteres[i]=='.'||caracteres[i]==','){
                caracteres[i+1]=Character.toUpperCase(caracteres[i+1]);
            }
        }
        return new String(caracteres);
    }
    
    
    
}
