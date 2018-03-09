/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import static com.lowagie.text.Annotation.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class Observador implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Nueva Actualizacion: "+o+"-> "+arg);
        if(arg.equals("Generandor Reporte 1")){
        ImageIcon icono  = new ImageIcon ("src/imagenes/loading.gif");
        JOptionPane.showMessageDialog (null, "Generando ...", "GENERADOR", JOptionPane.INFORMATION_MESSAGE, icono);
       }
    }
}
