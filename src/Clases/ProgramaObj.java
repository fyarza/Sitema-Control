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
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriela
 */
public class ProgramaObj {
    
    String id,nombre,
            nivel,grado,estado,
            gaceta,coordinador,codigo,
            autorizado,codigo_OPSU,nombre_OPSU,total_creditos,duracion;

    public ProgramaObj(String id, String nombre, String nivel, String grado, String estado, String gaceta, String coordinador, String codigo, String autorizado, String codigo_OPSU, String nombre_OPSU
    ,String total_creditos,String duracion) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.grado = grado;
        this.estado = estado;
        this.gaceta = gaceta;
        this.coordinador = coordinador;
        this.codigo = codigo;
        this.autorizado = autorizado;
        this.codigo_OPSU = codigo_OPSU;
        this.nombre_OPSU = nombre_OPSU;
        this.total_creditos=total_creditos;
        this.duracion=duracion;
    }
    public ProgramaObj() {
        this.id = null;
        this.nombre = null;
        this.nivel = null;
        this.grado = null;
        this.estado = null;
        this.gaceta = null;
        this.coordinador = null;
        this.codigo = null;
        this.autorizado = null;
        this.codigo_OPSU = null;
        this.nombre_OPSU = null;
        this.total_creditos=null;
        this.duracion=null;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTotal_creditos() {
        return total_creditos;
    }

    public void setTotal_creditos(String total_creditos) {
        this.total_creditos = total_creditos;
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

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGaceta() {
        return gaceta;
    }

    public void setGaceta(String gaceta) {
        this.gaceta = gaceta;
    }

    public String getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(String coordinador) {
        this.coordinador = coordinador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getCodigo_OPSU() {
        return codigo_OPSU;
    }

    public void setCodigo_OPSU(String codigo_OPSU) {
        this.codigo_OPSU = codigo_OPSU;
    }

    public String getNombre_OPSU() {
        return nombre_OPSU;
    }

    public void setNombre_OPSU(String nombre_OPSU) {
        this.nombre_OPSU = nombre_OPSU;
    }
    
    public boolean es_Vacio(){  
        return (this.id == null&&
                this.nombre == null&&this.nivel == null
                &&this.grado == null&&this.estado == null
                &&this.gaceta == null&&this.coordinador == null
                &&this.codigo == null&&this.autorizado == null
                &&this.codigo_OPSU == null&&this.nombre_OPSU == null
                &&this.total_creditos == null&&this.duracion==null);
    }
    public ArrayList<ProgramaObj> List_programa(){
        ArrayList<ProgramaObj> programas=new ArrayList<> ();
        ProgramaObj programa=new ProgramaObj();
     try
        {
         Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
            try (Connection conexion = con.getConexion()) {
                String sql = "SELECT * FROM programas";
                
                PreparedStatement pstm = conexion.prepareCall(sql);
             try (ResultSet rset = pstm.executeQuery()) {
                 if (rset.next())
                 {
                     programa.setId(rset.getString("id"));
                     programa.setNombre(rset.getString("nombre"));
                     programa.setNivel(rset.getString("nivel"));
                     programa.setGrado(rset.getString("grado"));
                     programa.setEstado(rset.getString("estado"));
                     programa.setGaceta(rset.getString("gaceta"));
                     programa.setCoordinador(rset.getString("coordinador"));
                     programa.setCodigo(rset.getString("codigo"));
                     programa.setAutorizado(rset.getString("autorizado"));
                     programa.setTotal_creditos(rset.getString("total_creditos"));
                     programa.setCodigo_OPSU(rset.getString("codigo_OPSU"));
                     programa.setNombre_OPSU(rset.getString("nombre_OPSU"));
                     programa.setDuracion(rset.getString("duracion"));
                     programas.add(programa);
                 }
             }
            }
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
        
        return programas;
    }
    
    
    
    public ProgramaObj copia(){
        ProgramaObj programa=new ProgramaObj();
        programa.setId("12");
        programa.setNombre("Ejemplo");
        return programa;
    }

 public ProgramaObj programa_filtrado(String id){
         ProgramaObj programa=new ProgramaObj();
     try
        {
         Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
        Connection conexion = con.getConexion();
      String sql = "SELECT * FROM programas where id="+id+"";
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     if (rset.next())
      {
          programa.setId(rset.getString("id"));
          programa.setNombre(rset.getString("nombre"));
          programa.setNivel(rset.getString("nivel"));
          programa.setGrado(rset.getString("grado"));
          programa.setEstado(rset.getString("estado"));
          programa.setGaceta(rset.getString("gaceta"));
          programa.setCoordinador(rset.getString("coordinador"));
          programa.setCodigo(rset.getString("codigo"));
          programa.setAutorizado(rset.getString("autorizado"));
          programa.setTotal_creditos(rset.getString("total_creditos"));
          programa.setCodigo_OPSU(rset.getString("codigo_OPSU"));
          programa.setNombre_OPSU(rset.getString("nombre_OPSU"));
          programa.setDuracion(rset.getString("duracion"));
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
        
        return programa;
    }
}
