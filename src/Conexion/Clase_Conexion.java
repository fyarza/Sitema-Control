/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Clase_Conexion
{
  private Connection conexion = null;
  private String servidor = "";
  private String database = "";
  private String usuario = "";
  private String password = "";
  private String url = "";
  
  public Clase_Conexion(String servidor, String database, String usuario, String password)
  {
    try
    {
      this.servidor = servidor;
      this.database = database;
      
      Class.forName("com.mysql.jdbc.Driver");
      
      this.url = ("jdbc:mysql://" + servidor + "/" + database);
      this.conexion = DriverManager.getConnection(this.url, usuario, password);
      System.out.println("Conexion a Base de Datos " + this.url + " . . . . .Ok");
    }
    catch (SQLException ex)
    {
      System.out.println(ex);
       System.out.println("No se pudo establecer la connecion");
              JOptionPane.showMessageDialog(null, "no se pudo conectar con el servidor "
                      + "jdbc:mysql://"+servidor+"/"+database, "Error de Conecion", JOptionPane.ERROR_MESSAGE);
              JOptionPane.showMessageDialog(null, "Fue imposible conectarse al servidor."
                  + "Porfavor siga los pasos para para ejecutar este programa."
                  + "\nPrimero: Inicie su servido de base de datos si no esta iniciado.\n"
                  + "Segundo: En los archivos de este proyecto existe una carpeta\n"
                  + "con el nombre 'basededato' dentro de esta carpeta existe el script sql"
                  + "\npara la base de datos importe este script desde su gestor de base\n"
                  + "de datos(mysql). si aun si tiene problemas\n"
                  + "debe crear una base de datos con el nombre 'bdsiap' y usando esta base de datos"
                  + "\nimporte el script indicado anteriormente."
                  + "\nTercero: configura cambie el nombre de usuario y la contraseña\n"
                  + "con el nombre de usuario y contraseña de su servidor de base de datos."
                  + "\n en el archivo 'conexion/ConexionBD.java'\n"
                  + "Vuelva a ejecutar este programa", servidor, JOptionPane.ERROR_MESSAGE);
              ex.printStackTrace();
               //System.exit(0);
             
    }
    catch (ClassNotFoundException ex)
    {
      System.out.println(ex);
    }
  }
  
  public Connection getConexion()
  {
    return this.conexion;
  }
  
  public Connection cerrarConexion()
  {
    try
    {
      this.conexion.close();
      System.out.println("Cerrando conexion a " + this.url + " . . . . . Ok");
    }
    catch (SQLException ex)
    {
      System.out.println(ex);
    }
    this.conexion = null;
    return this.conexion;
  }
}
