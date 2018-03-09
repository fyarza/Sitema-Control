/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

/**
 *
 * @author Administrador
 */
public class CamposConstancia
{
  String codigo;
  String asignatura;
  String uc;
  String periodo;
  String nota;
  
  public CamposConstancia(String codigo, String asignatura, String uc, String periodo, String nota)
  {
    this.codigo = codigo;
    this.asignatura = asignatura;
    this.uc = uc;
    this.periodo = periodo;
    this.nota = nota;
  }
  
  public String getcodigo()
  {
    return this.codigo;
  }
  
  public void setcodigo(String codigo)
  {
    this.codigo = codigo;
  }
  
  public String getasignatura()
  {
    return this.asignatura;
  }
  
  public void setasignatura(String asignatura)
  {
    this.asignatura = asignatura;
  }
  
  public String getuc()
  {
    return this.uc;
  }
  
  public void setuc(String uc)
  {
    this.uc = uc;
  }
  
  public String getperiodo()
  {
    return this.periodo;
  }
  
  public void setperiodo(String periodo)
  {
    this.periodo = periodo;
  }
  
  public String getnota()
  {
    return this.nota;
  }
  
  public void setnota(String nota)
  {
    this.nota = nota;
  }
}
