/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Observable;

/**
 *
 * @author Administrador
 */
public class Observado extends Observable {
    String mensaje;
    
    public Observado(){
        mensaje="Objeto Observado Iniciado";
    }
    
    public void cambiarMensaje(String m){
        mensaje=m;
        //Marcamos el Objeto observable  como objeto que ha cambiado
        setChanged();
        //Notificamos a los Observadores y le enviamos el nuevo valor 
        notifyObservers(mensaje);
        //notifyObservers(): este mentodo solo notifica que hubo cambio en el objeto
    }
}
