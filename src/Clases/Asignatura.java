/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Conexion.Clase_Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriela
 */
public class Asignatura {
    String codigo,nombre,nivel,creditos,horas,prelaciones,tipo;
    
    public Asignatura(){
        this.codigo = null;
        this.nombre = null;
        this.nivel = null;
        this.creditos = null;
        this.horas = null;
        this.prelaciones = null;
        this.tipo = null;
    }

    public Asignatura(String codigo, String nombre, String nivel, String creditos, String horas, String prelaciones, String tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nivel = nivel;
        this.creditos = creditos;
        this.horas = horas;
        this.prelaciones = prelaciones;
        this.tipo = tipo;
    }
    
    public boolean es_Vacio(){
        return (this.codigo == null&&
                this.creditos == null&&this.horas == null
                &&this.nivel == null&&this.nombre == null
                &&this.nombre == null&&this.prelaciones == null
                &&this.tipo == null);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getPrelaciones() {
        return prelaciones;
    }

    public void setPrelaciones(String prelaciones) {
        this.prelaciones = prelaciones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    //Asignaturas filtradas por tipo y programa
     public  ArrayList<Asignatura> List_Asignaturas(String programa,String tipo){
        ArrayList<Asignatura> listado= new ArrayList<>();
        Asignatura asignatura;
     try
        {
         Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
            try (Connection conexion = con.getConexion()) {
                String sql = "SELECT b.codigo,b.nombre,b.nivel,b.creditos,b.horas,b.prelaciones,b.tipo "
                        + "from programas a left join asignaturas b on (a.id=b.programa) "
                        + "where a.id='"+programa+"' and b.tipo='"+tipo+"' order by b.nivel";
                //JOptionPane.showMessageDialog(null, sql);
                
                PreparedStatement pstm = conexion.prepareCall(sql);
             try (ResultSet rset = pstm.executeQuery()) {
                 while(rset.next())
                 {
                     asignatura=new Asignatura(rset.getString("codigo"),rset.getString("nombre"),
                             rset.getString("nivel"),rset.getString("creditos"),rset.getString("horas"),rset.getString("prelaciones"),rset.getString("tipo"));
                     listado.add(asignatura);
                 }
             }
            }
            //JOptionPane.showMessageDialog(null, sql);
            //JOptionPane.showMessageDialog(null, sql);
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
        return listado;
    }
}
