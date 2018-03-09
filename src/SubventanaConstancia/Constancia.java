/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SubventanaConstancia;
import Clases.Observado;
import Clases.Observador;
import Conexion.Clase_Conexion;
import Reportes.CamposConstancia;
import com.toedter.calendar.JDateChooser;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Administrador
 */
public class Constancia extends javax.swing.JInternalFrame {

    /**
     * Creates new form Constancia
     */
     DefaultTableModel modeloconstancia,modeloconvalidacion;
    
     String periodo_actual="";
     float ind=0;
    public Constancia() {
        initComponents();
         this.modeloconstancia = ((DefaultTableModel)this.tablamaterias.getModel());
         this.modeloconvalidacion=((DefaultTableModel)this.tabla_convalidacion.getModel());
     this.txt_cedula.requestFocus();
     this.btn_agregar.setVisible(false);
     this.btn_quitar.setVisible(false);
     this.btn_editarcreditos.setVisible(false);
    }
    
     public static void reiniciarJTable(JTable Tabla)
  {
    DefaultTableModel modelo = (DefaultTableModel)Tabla.getModel();
    while (modelo.getRowCount() > 0) {
      modelo.removeRow(0);
    }

  }
     
     private String obt_fecha(JDateChooser fecha)
  {
    String fnacim = "";
    try
    {
      String formato = fecha.getDateFormatString();
      Date date = fecha.getDate();
      SimpleDateFormat sdf = new SimpleDateFormat(formato);
      fnacim = String.valueOf(sdf.format(date));
    }
    catch (Exception localException) {}
    return fnacim;
  }
     
     private void cargartablaconstancia(String rhp)
  {
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      Connection conexion = con.getConexion();
      reiniciarJTable(this.tablamaterias);
      //this.modeloconstancia.setColumnCount(0);
      this.modeloconstancia.setRowCount(0);
      
      String sql = "SELECT x.codigo,x.nasignatura,x.creditos,x.nperiodo,x.nota\nFROM (SELECT periodos.nombre AS nperiodo,\n\t\tprogramas.codigo as cod_programa,\n          programas.nombre AS nprograma,\n          a.nombre AS nasignatura,\n             lpad(o.id,5,0) AS oferta,\n             o.id AS o_id,\n             a.codigo,\n             a.creditos,\n             iss.id AS iss_id,\n             lpad(ip.id,5,0) AS planilla,\n             ip.id AS ip_id,\n             iss.retirada,\n             o.shorario,\n             o.seccion,\n             inscripciones.nombre AS ninscripcion,\n             rhp.id AS rhp,\n             n.nota,\n             n.requisito_idiomas,\n             '' AS observaciones,\n             periodos.id AS periodo,\n             o.acta_nota_status AS nota_status,\n             n.cargar_nota,\n             periodos.anno,\n             periodos.num\n   FROM registro_historico_programas AS rhp\n   LEFT JOIN registro_programas AS rp ON rp.rhp=rhp.id\n   LEFT JOIN inscripcion_sel AS iss ON iss.rp=rp.id\n   LEFT JOIN inscripcion_planillas AS ip ON ip.id=iss.planilla\n   LEFT JOIN inscripciones ON iss.inscripcion=inscripciones.id\n   LEFT JOIN ofertas AS o ON iss.oferta=o.id\n   LEFT JOIN programaciones ON o.programacion=programaciones.id\n   LEFT JOIN programas ON programaciones.programa=programas.id\n   LEFT JOIN periodos ON programaciones.periodo=periodos.id\n   LEFT JOIN asignaturas AS a ON o.asignatura=a.id\n   LEFT JOIN notas AS n ON n.ro=iss.id\n   AND o.acta_nota_status IN ('D',\n                              'R')  \n   WHERE rhp.id='" + rhp + "'\n     AND ip.ratificada=1\n     AND iss.retirada=0\n     AND a.creditos > 0\n   UNION SELECT periodos.nombre AS nperiodo,\n                concat(programas.codigo,' ',programas.nombre) AS nprograma,\n                a.nombre AS nasignatura,\n                a.nombre AS nasignatura_com,\n                lpad(0,5,0) AS oferta,\n                0 AS o_id,\n                a.codigo,\n                a.creditos,\n                0 AS iss_id,\n                '' AS planilla,\n                0 AS ip_id,\n                0 AS retirada,\n                '' AS shorario,\n                '' AS seccion,\n                '' AS ninscripcion,\n                rhp.id AS rhp,\n                notas.nota,\n                notas.requisito_idiomas,\n                IF(cargar_notas.oficio='',\n                   cargar_nota_status.label,\n                   concat(cargar_nota_status.label,' ofc:',cargar_notas.oficio,'')) AS observaciones,\n                  periodos.id AS periodo,\n                  'R' AS nota_status,\n                  notas.cargar_nota,\n                  periodos.anno,\n                  periodos.num\n   FROM notas\n   LEFT JOIN cargar_notas ON notas.cargar_nota=cargar_notas.id\n   LEFT JOIN cargar_nota_status ON cargar_notas.status=cargar_nota_status.id\n   LEFT JOIN periodos ON cargar_notas.periodo=periodos.id\n   LEFT JOIN registro_historico_programas AS rhp ON cargar_notas.rhp=rhp.id\n   LEFT JOIN asignaturas AS a ON cargar_notas.asignatura=a.id\n   LEFT JOIN programas ON a.programa=programas.id\n   WHERE rhp.id='" + rhp + "') AS x\nLEFT JOIN periodos ON x.periodo=periodos.id\nORDER BY periodos.anno ASC,\n         periodos.num ASC,\n         x.codigo";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      ResultSetMetaData rsmd = rset.getMetaData();
      
      int col = rsmd.getColumnCount();
//      for (int i = 1; i <= col; i++) {
//        this.modeloconstancia.addColumn(rsmd.getColumnLabel(i));
//      }
      while (rset.next())
      {
        String[] filas = new String[col];
        for (int j = 0; j < col; j++) {
          if (rset.getString(j + 1) != null) {
            filas[j] = rset.getString(j + 1);
          } else {
            filas[j] = " ";
          }
        }
        this.modeloconstancia.addRow(filas);
      }
      this.tablamaterias.setModel(this.modeloconstancia);
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
  }
     private void limpiar()
  {
    this.lb_nombre.setText(null);
    this.lb_cohorte.setText(null);
    this.lb_creditos.setText(null);
    this.lb_indice.setText(null);
    this.lb_ingles.setText("");
    this.cb_programas.removeAllItems();
    lb_culminacion.setText(null);
    lb_tesis.setText(null);
    lb_notaingles.setText("");
    lb_inglesperiodo.setText("");
    lb_nacionalidad.setText("");
    lb_infoidioma.setText("");
    reiniciarJTable(tabla_convalidacion);
  }
     private static  boolean isNumeric(String cadena){
         try {
             Integer.parseInt(cadena);
             return true;
         } catch (Exception e) {
             return false;
         }
     }
     
     private void calcularindice(JTable tabla)
  {
    int filas = tabla.getRowCount();
    int suma = 0;int suma2 = 0;int sumadoc=0;
    float resul = 0.0F;
    DecimalFormat formateador = new DecimalFormat("00.00");
    for (int i = 0; i < filas; i++)
    {
        
      String[] arraycreditos = tabla.getValueAt(i, 2).toString().split(".00");
      sumadoc += Integer.parseInt(arraycreditos[0]);
      if ((!" ".equals(tabla.getValueAt(i, 4).toString()))&&(isNumeric(tabla.getValueAt(i, 4).toString())))
      {
        suma += Integer.parseInt(arraycreditos[0]);
        suma2 += Integer.parseInt(arraycreditos[0]) * Integer.parseInt(tabla.getValueAt(i, 4).toString());
      }
    }
    if(doctorado()){
        this.lb_creditos.setText(sumadoc + "");
    }else{
        this.lb_creditos.setText(suma + "");
    }
    if (suma != 0) {
      resul = Float.parseFloat(suma2 + "") / Float.parseFloat(suma + "");
      ind=Float.parseFloat(suma2 + "") / Float.parseFloat(suma + "");
    }
    this.lb_indice.setText(formateador.format(resul));
  }
     private boolean calculo_estatus(int p,int a,int tipo)
  {
    boolean resul = false;
    try
    {
         
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      

      String sql = "SELECT p.nombre from postgrado.periodos_actuales h "
              + "left join postgrado.inscripciones t on (h.inscripcion=t.id) "
              + "left join  postgrado.periodos p on (t.iperiodo=p.id)";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      if (rset.next())
      {
          int ano,periodo,ano_inicio,periodo_inicio;
          periodo_actual=rset.getString("nombre");
          String[] arraycursos = periodo_actual.split("-");
          ano=Integer.parseInt(arraycursos[0]);
          periodo=Integer.parseInt(arraycursos[1]);
        
         if(tipo==1){ 
         if (periodo==3){
             periodo_inicio=1;
             ano_inicio=ano-3;
         }else{
             periodo_inicio=periodo+1;
             ano_inicio=ano-4;
         }
          //JOptionPane.showMessageDialog(null, periodo_inicio +"/"+ ano_inicio);
         // JOptionPane.showMessageDialog(null, p+"-"+a);
          if((a>=ano_inicio)){
              resul=true;  
          }
         }else{
             if (periodo==3){
             periodo_inicio=1;
             ano_inicio=ano-4;
         }else{
             periodo_inicio=periodo+1;
             ano_inicio=ano-5;
         }
          //JOptionPane.showMessageDialog(null, periodo_inicio +"/"+ ano_inicio);
          if((a>=ano_inicio)){
              resul=true;
          }
         }
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  } 
   
     private boolean cargarestudiante(String cedula)
  {
    boolean resul = false;
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa != 24 and rp.status!=500";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      ResultSetMetaData rsmd = rset.getMetaData();
      
      this.cb_programas.removeAllItems();
      while (rset.next())
      {
        resul = true;
        this.lb_nombre.setText(rset.getString("nombre"));
        this.cb_programas.addItem(rset.getString("nprograma"));
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
     private String buscarrhp(String cedula, String codigo)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa != 24 and rp.status != 500 and programas.codigo='" + codigo + "'";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
    
      if (rset.next())
      {
        resul = rset.getString("rhp");
        this.lb_cohorte.setText(rset.getString("ncohorte"));
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
      private String buscaralumno(String cedula, String codigo)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa != 24 and rp.status != 500 and programas.codigo='" + codigo + "'";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      
      if (rset.next())
      {
        resul = rset.getString("alumno");
        
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
       private String buscarrhpitaliano(String cedula)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa = 28 and rp.status!=500";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      ResultSetMetaData rsmd = rset.getMetaData();
      while (rset.next()) {
        resul = rset.getString("rhp");
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
  
     private String buscarrhpingles(String cedula)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa = 24 and rp.status!=500";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      ResultSetMetaData rsmd = rset.getMetaData();
      while (rset.next()) {
        resul = rset.getString("rhp");
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
     private String AprobadoIngles(String rhp)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      Connection conexion = con.getConexion();
      
      String sql = "SELECT x.codigo,x.nasignatura,x.creditos,x.nperiodo,x.nota "
              + "FROM (SELECT periodos.nombre AS nperiodo,programas.codigo as cod_programa,"
              + " programas.nombre AS nprograma,a.nombre AS nasignatura,lpad(o.id,5,0) AS oferta,"
              + " o.id AS o_id,a.codigo,a.creditos,iss.id AS iss_id,lpad(ip.id,5,0) AS planilla,ip.id AS ip_id,"
              + " iss.retirada,o.shorario,o.seccion,inscripciones.nombre AS ninscripcion,rhp.id AS rhp,n.nota,"
              + "n.requisito_idiomas,'' AS observaciones,periodos.id AS periodo,o.acta_nota_status AS nota_status,"
              + "n.cargar_nota,periodos.anno,periodos.num FROM registro_historico_programas AS rhp LEFT JOIN registro_programas AS rp ON rp.rhp=rhp.id"
              + " LEFT JOIN inscripcion_sel AS iss ON iss.rp=rp.id"
              + " LEFT JOIN inscripcion_planillas AS ip ON ip.id=iss.planilla"
              + " LEFT JOIN inscripciones ON iss.inscripcion=inscripciones.id"
              + " LEFT JOIN ofertas AS o ON iss.oferta=o.id"
              + " LEFT JOIN programaciones ON o.programacion=programaciones.id"
              + " LEFT JOIN programas ON programaciones.programa=programas.id"
              + " LEFT JOIN periodos ON programaciones.periodo=periodos.id"
              + " LEFT JOIN asignaturas AS a ON o.asignatura=a.id"
              + " LEFT JOIN notas AS n ON n.ro=iss.id"
              + " AND o.acta_nota_status IN ('D','R') WHERE rhp.id='" + rhp + "' AND ip.ratificada=1 AND iss.retirada=0 AND n.nota > 10"
              + " UNION SELECT periodos.nombre AS nperiodo,concat(programas.codigo,' ',programas.nombre) AS nprograma,"
              + " a.nombre AS nasignatura,a.nombre AS nasignatura_com,lpad(0,5,0) AS oferta, 0 AS o_id,a.codigo,"
              + "a.creditos,0 AS iss_id,'' AS planilla,0 AS ip_id,0 AS retirada,'' AS shorario,'' AS seccion,"
              + "'' AS ninscripcion,rhp.id AS rhp,notas.nota,notas.requisito_idiomas,IF(cargar_notas.oficio='',"
              + "cargar_nota_status.label,concat(cargar_nota_status.label,' ofc:',cargar_notas.oficio,'')) AS observaciones,"
              + "periodos.id AS periodo,'R' AS nota_status,notas.cargar_nota,periodos.anno,periodos.num"
              + " FROM notas LEFT JOIN cargar_notas ON notas.cargar_nota=cargar_notas.id"
              + " LEFT JOIN cargar_nota_status ON cargar_notas.status=cargar_nota_status.id"
              + " LEFT JOIN periodos ON cargar_notas.periodo=periodos.id"
              + " LEFT JOIN registro_historico_programas AS rhp ON cargar_notas.rhp=rhp.id"
              + " LEFT JOIN asignaturas AS a ON cargar_notas.asignatura=a.id"
              + " LEFT JOIN programas ON a.programa=programas.id"
              + " WHERE rhp.id='" + rhp + "') AS x "
              + "LEFT JOIN periodos ON x.periodo=periodos.id ORDER BY periodos.anno ASC, periodos.num ASC, x.codigo";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     
      if (rset.next()) {
          //JOptionPane.showMessageDialog(null, rset.getString("nota"));
        if (rset.getString("nota") != null)
        {
          int nota = 0;
          if (isNumeric(rset.getString("nota"))){
          nota = Integer.parseInt(rset.getString("nota"));
           if (nota > 10) {
            resul = rset.getString("nperiodo");
            notaingles(nota+"");
          }
          }else if ((rset.getString("nota").equals("AP"))||(rset.getString("nota").equals("ap"))){
               resul = rset.getString("nperiodo");
            notaingles("AP");
          }
         
        }
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    return resul;
  }
     private boolean doctorado(){
         boolean resul=false;
         if(cb_programas.getItemCount()>0){
         String cadena = cb_programas.getSelectedItem().toString();
        int resultado = cadena.indexOf("DOCTORADO");
        if(resultado != -1) {
              resul=true;
        }
       }
         return resul;
     }
     
     private void regimen_permanencia(int tipo){
       int ano,periodo,a,p=0;
       String[] arraycohorte = lb_cohorte.getText().split("-");
            a=Integer.parseInt(arraycohorte[0]);
            p=Integer.parseInt(arraycohorte[1]);
       if(tipo==1){
         if (p==1)
         {
             ano=a+3;
             periodo=p+2;
         }else{
             ano=a+4;
             periodo=p-1;
         }
           switch (periodo) {
               case 1:
                   lb_culminacion.setText("Abril del "+ano);
                   break;
               case 2:
                   lb_culminacion.setText("Agosto del "+ano);
                   break; 
               default:
                   lb_culminacion.setText("Diciembre del "+ano);
                   break;
           }
       }else{
           if (p==1)
         {
             ano=a+4;
             periodo=p+2;
         }else{
             ano=a+5;
             periodo=p-1;
         }
           switch (periodo) {
               case 1:
                   lb_culminacion.setText("Abril del "+ano);
                   break;
               case 2:
                   lb_culminacion.setText("Agosto del "+ano);
                   break; 
               default:
                   lb_culminacion.setText("Diciembre del "+ano);
                   break;
           }
       }
     }
     

   private void cargarprograma()
  {
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT id,nombre FROM postgrado.programas";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      this.cb_Progra.removeAllItems();
      this.cb_Progra1.removeAllItems();
     while (rset.next())
      {
        this.cb_Progra.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        this.cb_Progra1.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    
  }
   
     private void cargarmaterias(String codigo)
  {
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT id,nombre  FROM postgrado.asignaturas where programa='" + codigo+ "' order by nombre";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      this.cb_asigna.removeAllItems();
      this.cb_asigna1.removeAllItems();
     while (rset.next())
      {
        this.cb_asigna.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        this.cb_asigna1.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    
  }
     
      private void cargarcreditos(String codigo)
  {
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT codigo,creditos  FROM postgrado.asignaturas where id='" +codigo+ "'";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     if (rset.next())
      {
        this.lb_codi.setText(rset.getString("codigo"));
        this.lb_cred.setText(rset.getString("creditos"));
        this.lb_codi1.setText(rset.getString("codigo"));
        this.lb_cred1.setText(rset.getString("creditos"));
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    
  }
      
      private String titulo(){
          String resul="";
         if(cb_programas.getItemCount()>0){
         String cadena = cb_programas.getSelectedItem().toString();
        int resultado = cadena.indexOf("DOCTORADO");
        int resultado1 = cadena.indexOf("MAESTRÍA");
        int resultado2 = cadena.indexOf("ESPECIALIZACIÓN");
        if(resultado != -1) {
              resul="DOCTOR EN INGENIERÍA";
        }else if (resultado1 !=-1){
            String[] arraycursos = this.cb_programas.getSelectedItem().toString().split("MAESTRÍA");
            resul="MAGISTER"+arraycursos[1];
        }else if (resultado2 !=-1){
            String[] arraycursos = this.cb_programas.getSelectedItem().toString().split("ESPECIALIZACIÓN TÉCNICA");
            resul="TÉCNICO SUPERIOR ESPECIALISTA"+arraycursos[1];
             resul=resul.replaceAll("TRIGAL-VALENCIA", "");
             resul=resul.replaceAll("CARACAS", "");
             resul=resul.substring(0,resul.length()-3);
             
        }
       }
          return resul;
      }
/**
     * Permite convertir un String en fecha (Date).
     * @param fecha Cadena de fecha dd/MM/yyyy
     * @return Objeto Date
     */
    public static Date ParseFecha(String fecha)
    {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
        return fechaDate;
    }
    
   
     
      private void fecha_tesis(String alumno)
  {
    try
    {
        
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
       String sql = "SELECT date_format(fecha_presentacion,' Aprobado el %d/%m/%Y') as fecha FROM postgrado.SeccionGrado p left join postgrado.registro_programas h on (p.rp=h.id) where h.alumno='" +alumno+ "'";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     if (rset.next())
      {  
        lb_tesis.setText(rset.getString("fecha"));
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    
  }
      private String coordinador(String codigo){
          String resul="";
          try
    {
        
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql="SELECT concat(t.apellido,' ',t.nombrep) as nombre FROM postgrado.programas h left join postgrado.profesores p on (h.coordinador=p.id) left join postgrado.personas t on (p.persona=t.id) where h.codigo='" +codigo+ "'";
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     if (rset.next())
      {  
          resul=rset.getString("nombre");
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
     return resul;
   }
      
       private String nac(String cedula) {
          String resul="";
     try
    {
        
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql="SELECT nac FROM postgrado.personas where cedula='" +cedula+ "'";
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
     if (rset.next())
      {  
          resul=rset.getString("nac");
           if (resul.equals("V")){
             lb_nacionalidad.setText("VENEZOLANO");
            }else if (resul.equals("E")){
            lb_nacionalidad.setText("EXTRANJERO");
            }else{
             lb_nacionalidad.setText("PASAPORTE");
      }
          
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
     return resul;
   }
       
       
      
      private void notaingles(String nota){
         switch (nota) {
             case 10+"":
                 lb_notaingles.setText("Diez (10) puntos");
                 break;
             case 11+"":
                 lb_notaingles.setText("Once (11) puntos");
                 break;
             case 12+"":
                 lb_notaingles.setText("Doce (12) puntos");
                 break;
             case 13+"":
                 lb_notaingles.setText("Trece (13) puntos");
                 break;
             case 14+"":
                 lb_notaingles.setText("Catorce (14) puntos");
                 break;
             case 15+"":
                 lb_notaingles.setText("Quince (15) puntos");
                 break;
             case 16+"":
                 lb_notaingles.setText("Dieciseis (16) puntos");
                 break;
             case 17+"":                    
                 lb_notaingles.setText("Diecisiete (17) puntos");
                 break;
             case 18+"":
                 lb_notaingles.setText("Dieciocho (18) puntos");
                 break;
             case 19+"":
                 lb_notaingles.setText("Diecinueve (19) puntos");
                 break;
             case 20+"":
                 lb_notaingles.setText("Veinte (20) puntos");
                 break;
                 case "AP":
                 lb_notaingles.setText("Reconocido");
                 break;
             default:
                 break;
         }
          
      }
      
      private void periodoingles(String periodo){
          if(!periodo.equals("")){
               String[] arrayperiodo = periodo.split("-");
                switch (arrayperiodo[1]) {
             case "01":
                 lb_inglesperiodo.setText("Enero-Abril "+arrayperiodo[0]);
                 break;
             case "02":
                  lb_inglesperiodo.setText("Mayo-Agosto "+arrayperiodo[0]);
                 break;
             case "03":
                  lb_inglesperiodo.setText("Octubre-Diciembre "+arrayperiodo[0]);
                 break;
             default:
                 break;
         }
       }
          
      }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        convalidacion = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_convalidacion = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        btn_agregarC = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();
        btn_cargamanual = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cb_Progra1 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cb_asigna1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        lb_codi1 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lb_cred1 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txt_periodo1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txt_nota1 = new javax.swing.JTextField();
        panelmateria = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cb_Progra = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cb_asigna = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        lb_codi = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lb_cred = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_periodo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_nota = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btn_materia = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        carga_manual = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_codigoC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_asignaturaC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_creditosC = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txt_periodoC = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txt_notaC = new javax.swing.JTextField();
        btn_AGREGAMANUAL = new javax.swing.JButton();
        Prorroga = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txt_numero = new javax.swing.JSpinner();
        jLabel27 = new javax.swing.JLabel();
        txtfecha = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        txtdispone = new javax.swing.JTextField();
        btnimprimirP = new javax.swing.JButton();
        btn_Sol = new javax.swing.JButton();
        ingles = new javax.swing.JDialog();
        dateingles = new com.toedter.calendar.JDateChooser();
        btn_imprimiringles = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_cedula = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        lb_nacionalidad = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lb_nombre = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cb_programas = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        lb_creditos = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lb_indice = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lb_ingles = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lb_cohorte = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablamaterias = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btn_notas = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lb_culminacion = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_agregar = new javax.swing.JButton();
        btn_solvencia = new javax.swing.JButton();
        btn_dices = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        lb_tesis = new javax.swing.JLabel();
        btn_convalidacion = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btn_certificadoingles = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        lb_inglesperiodo = new javax.swing.JLabel();
        lb_notaingles = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lb_infoidioma = new javax.swing.JLabel();
        btn_constanciainlngles = new javax.swing.JButton();
        btn_quitar = new javax.swing.JButton();
        btn_editarcreditos = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        convalidacion.setAlwaysOnTop(true);

        tabla_convalidacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tabla_convalidacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Asignatura", "U.C", "Período", "Nota"
            }
        ));
        jScrollPane3.setViewportView(tabla_convalidacion);
        if (tabla_convalidacion.getColumnModel().getColumnCount() > 0) {
            tabla_convalidacion.getColumnModel().getColumn(0).setPreferredWidth(70);
            tabla_convalidacion.getColumnModel().getColumn(0).setMaxWidth(70);
            tabla_convalidacion.getColumnModel().getColumn(2).setMinWidth(50);
            tabla_convalidacion.getColumnModel().getColumn(2).setPreferredWidth(50);
            tabla_convalidacion.getColumnModel().getColumn(2).setMaxWidth(50);
            tabla_convalidacion.getColumnModel().getColumn(3).setPreferredWidth(60);
            tabla_convalidacion.getColumnModel().getColumn(3).setMaxWidth(60);
            tabla_convalidacion.getColumnModel().getColumn(4).setMinWidth(40);
            tabla_convalidacion.getColumnModel().getColumn(4).setPreferredWidth(40);
            tabla_convalidacion.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btn_agregarC.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_agregarC.setText("Agregar");
        btn_agregarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarCActionPerformed(evt);
            }
        });

        btn_borrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_borrar.setText("Borrar");
        btn_borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_borrarActionPerformed(evt);
            }
        });

        btn_imprimir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_imprimir.setText("Imprimir");
        btn_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimirActionPerformed(evt);
            }
        });

        btn_cargamanual.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_cargamanual.setText("Carga Manual");
        btn_cargamanual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cargamanualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(btn_agregarC)
                .addGap(18, 18, 18)
                .addComponent(btn_cargamanual)
                .addGap(18, 18, 18)
                .addComponent(btn_borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_imprimir)
                .addGap(100, 100, 100))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_agregarC)
                    .addComponent(btn_borrar)
                    .addComponent(btn_imprimir)
                    .addComponent(btn_cargamanual))
                .addGap(20, 20, 20))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Materia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N
        jPanel8.setLayout(new java.awt.GridLayout(0, 2));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Programa:");
        jPanel8.add(jLabel18);

        cb_Progra1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_Progra1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_Progra1ItemStateChanged(evt);
            }
        });
        jPanel8.add(cb_Progra1);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Asignatura: ");
        jPanel8.add(jLabel20);

        cb_asigna1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_asigna1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_asigna1ItemStateChanged(evt);
            }
        });
        jPanel8.add(cb_asigna1);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Codigo:");
        jPanel8.add(jLabel21);

        lb_codi1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel8.add(lb_codi1);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("Creditos:");
        jPanel8.add(jLabel22);

        lb_cred1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel8.add(lb_cred1);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Periodo:");
        jPanel8.add(jLabel23);

        txt_periodo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_periodo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_periodo1ActionPerformed(evt);
            }
        });
        txt_periodo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_periodo1KeyTyped(evt);
            }
        });
        jPanel8.add(txt_periodo1);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Nota:");
        jPanel8.add(jLabel24);

        txt_nota1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_nota1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nota1KeyTyped(evt);
            }
        });
        jPanel8.add(txt_nota1);

        javax.swing.GroupLayout convalidacionLayout = new javax.swing.GroupLayout(convalidacion.getContentPane());
        convalidacion.getContentPane().setLayout(convalidacionLayout);
        convalidacionLayout.setHorizontalGroup(
            convalidacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(convalidacionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(convalidacionLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        convalidacionLayout.setVerticalGroup(
            convalidacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, convalidacionLayout.createSequentialGroup()
                .addGap(0, 18, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Materia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N
        jPanel5.setLayout(new java.awt.GridLayout(0, 2));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Programa:");
        jPanel5.add(jLabel11);

        cb_Progra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_Progra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_PrograItemStateChanged(evt);
            }
        });
        jPanel5.add(cb_Progra);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Asignatura: ");
        jPanel5.add(jLabel13);

        cb_asigna.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_asigna.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_asignaItemStateChanged(evt);
            }
        });
        jPanel5.add(cb_asigna);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Codigo:");
        jPanel5.add(jLabel14);

        lb_codi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel5.add(lb_codi);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Creditos:");
        jPanel5.add(jLabel17);

        lb_cred.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel5.add(lb_cred);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Periodo:");
        jPanel5.add(jLabel19);

        txt_periodo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_periodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_periodoActionPerformed(evt);
            }
        });
        txt_periodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_periodoKeyTyped(evt);
            }
        });
        jPanel5.add(txt_periodo);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Nota:");
        jPanel5.add(jLabel16);

        txt_nota.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel5.add(txt_nota);

        btn_materia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_materia.setText("Agregar");
        btn_materia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_materiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_materia)
                .addGap(325, 325, 325))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(btn_materia)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout panelmateriaLayout = new javax.swing.GroupLayout(panelmateria.getContentPane());
        panelmateria.getContentPane().setLayout(panelmateriaLayout);
        panelmateriaLayout.setHorizontalGroup(
            panelmateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmateriaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelmateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelmateriaLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelmateriaLayout.setVerticalGroup(
            panelmateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmateriaLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jPanel9.setLayout(new java.awt.GridLayout(0, 2));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Codigo:");
        jPanel9.add(jLabel8);

        txt_codigoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_codigoCActionPerformed(evt);
            }
        });
        jPanel9.add(txt_codigoC);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Asignatura:");
        jPanel9.add(jLabel9);

        txt_asignaturaC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_asignaturaCActionPerformed(evt);
            }
        });
        jPanel9.add(txt_asignaturaC);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Creditos:");
        jPanel9.add(jLabel10);
        jPanel9.add(txt_creditosC);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Periodo:");
        jPanel9.add(jLabel25);

        txt_periodoC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_periodoCActionPerformed(evt);
            }
        });
        jPanel9.add(txt_periodoC);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Nota:");
        jPanel9.add(jLabel26);

        txt_notaC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_notaCActionPerformed(evt);
            }
        });
        jPanel9.add(txt_notaC);

        btn_AGREGAMANUAL.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_AGREGAMANUAL.setText("Agregar");
        btn_AGREGAMANUAL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AGREGAMANUALActionPerformed(evt);
            }
        });
        jPanel9.add(btn_AGREGAMANUAL);

        javax.swing.GroupLayout carga_manualLayout = new javax.swing.GroupLayout(carga_manual.getContentPane());
        carga_manual.getContentPane().setLayout(carga_manualLayout);
        carga_manualLayout.setHorizontalGroup(
            carga_manualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        );
        carga_manualLayout.setVerticalGroup(
            carga_manualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
        );

        jPanel10.setLayout(new java.awt.GridLayout(0, 2));

        jLabel29.setText("Nª");
        jPanel10.add(jLabel29);

        txt_numero.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        jPanel10.add(txt_numero);

        jLabel27.setText("Fecha");
        jPanel10.add(jLabel27);

        txtfecha.setDateFormatString("dd-MM-yyyy");
        jPanel10.add(txtfecha);

        jLabel28.setText("Dispone:");
        jPanel10.add(jLabel28);
        jPanel10.add(txtdispone);

        btnimprimirP.setText("Culminacion");
        btnimprimirP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirPActionPerformed(evt);
            }
        });
        jPanel10.add(btnimprimirP);

        btn_Sol.setText("Solvencia");
        btn_Sol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SolActionPerformed(evt);
            }
        });
        jPanel10.add(btn_Sol);

        javax.swing.GroupLayout ProrrogaLayout = new javax.swing.GroupLayout(Prorroga.getContentPane());
        Prorroga.getContentPane().setLayout(ProrrogaLayout);
        ProrrogaLayout.setHorizontalGroup(
            ProrrogaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );
        ProrrogaLayout.setVerticalGroup(
            ProrrogaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
        );

        btn_imprimiringles.setText("Imprimir");
        btn_imprimiringles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimiringlesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inglesLayout = new javax.swing.GroupLayout(ingles.getContentPane());
        ingles.getContentPane().setLayout(inglesLayout);
        inglesLayout.setHorizontalGroup(
            inglesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inglesLayout.createSequentialGroup()
                .addComponent(dateingles, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_imprimiringles))
        );
        inglesLayout.setVerticalGroup(
            inglesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dateingles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btn_imprimiringles)
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 160, 75)), "Datos del Alumno", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 111, 55))); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(0, 2));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel1.setText("Cedula: ");
        jPanel1.add(jLabel1);

        txt_cedula.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cedulaActionPerformed(evt);
            }
        });
        txt_cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cedulaKeyTyped(evt);
            }
        });
        jPanel1.add(txt_cedula);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Nacionalidad:");
        jPanel1.add(jLabel32);

        lb_nacionalidad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(lb_nacionalidad);

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel2.setText("Nombre: ");
        jPanel1.add(jLabel2);

        lb_nombre.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanel1.add(lb_nombre);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel3.setText("Lista de Programas:");
        jPanel1.add(jLabel3);

        cb_programas.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        cb_programas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_programasItemStateChanged(evt);
            }
        });
        jPanel1.add(cb_programas);

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel5.setText("Total de Creditos Aprobados: ");
        jPanel1.add(jLabel5);

        lb_creditos.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanel1.add(lb_creditos);

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel6.setText("índice Académico:");
        jPanel1.add(jLabel6);

        lb_indice.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanel1.add(lb_indice);

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel7.setText("Idioma Aprobado: ");
        jPanel1.add(jLabel7);

        lb_ingles.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanel1.add(lb_ingles);

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel4.setText("Cohorte: ");
        jPanel1.add(jLabel4);

        lb_cohorte.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanel1.add(lb_cohorte);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 160, 75)), "Listado de Materias Cursadas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12), new java.awt.Color(0, 111, 55))); // NOI18N

        tablamaterias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Asignatura", "Crédito", "Periodo", "Nota"
            }
        ));
        tablamaterias.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tablamaterias);
        tablamaterias.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tablamaterias.getColumnModel().getColumnCount() > 0) {
            tablamaterias.getColumnModel().getColumn(0).setMinWidth(60);
            tablamaterias.getColumnModel().getColumn(0).setPreferredWidth(60);
            tablamaterias.getColumnModel().getColumn(0).setMaxWidth(60);
            tablamaterias.getColumnModel().getColumn(1).setResizable(false);
            tablamaterias.getColumnModel().getColumn(1).setPreferredWidth(600);
            tablamaterias.getColumnModel().getColumn(2).setMinWidth(50);
            tablamaterias.getColumnModel().getColumn(2).setPreferredWidth(50);
            tablamaterias.getColumnModel().getColumn(2).setMaxWidth(50);
            tablamaterias.getColumnModel().getColumn(3).setMinWidth(70);
            tablamaterias.getColumnModel().getColumn(3).setPreferredWidth(70);
            tablamaterias.getColumnModel().getColumn(3).setMaxWidth(70);
            tablamaterias.getColumnModel().getColumn(4).setResizable(false);
            tablamaterias.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 111, 55)));

        btn_notas.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        btn_notas.setText("Constancia de Notas");
        btn_notas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_notasActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton2.setText("Constancia de Estudios");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton3.setText("Constancia de Culminacion");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lb_culminacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_culminacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_culminacion.setText(" ");

        jLabel12.setText("Fecha de Culminacion:");

        btn_agregar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btn_agregar.setText("Agregar Materia");
        btn_agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarActionPerformed(evt);
            }
        });

        btn_solvencia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_solvencia.setText("Solvencia");
        btn_solvencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_solvenciaActionPerformed(evt);
            }
        });

        btn_dices.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_dices.setText("Constancia DICES");
        btn_dices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dicesActionPerformed(evt);
            }
        });

        jLabel15.setText("Fecha de Presentacion de Tesis: ");

        lb_tesis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btn_convalidacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_convalidacion.setText("Convalidacion");
        btn_convalidacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_convalidacionActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Prorroga");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_certificadoingles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_certificadoingles.setText("Certificado de Ingles");
        btn_certificadoingles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_certificadoinglesActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 111, 55), 2), "Detalles del Idioma", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 111, 55))); // NOI18N

        lb_inglesperiodo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lb_notaingles.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel30.setText("Nota:");

        jLabel31.setText("Periodo:");

        jLabel33.setText("Tipo: ");

        lb_infoidioma.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4)
                                .addComponent(lb_notaingles, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(lb_inglesperiodo, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(69, 69, 69))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_infoidioma, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_infoidioma, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_notaingles, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_inglesperiodo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btn_constanciainlngles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_constanciainlngles.setText("Contancia de Ingles");
        btn_constanciainlngles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_constanciainlnglesActionPerformed(evt);
            }
        });

        btn_quitar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_quitar.setText("Quitar");
        btn_quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_quitarActionPerformed(evt);
            }
        });

        btn_editarcreditos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_editarcreditos.setText("Editar creditos");
        btn_editarcreditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarcreditosActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setText("Certificado de Italiano");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_certificadoingles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_notas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_constanciainlngles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jButton4)))
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(6, 6, 6)
                        .addComponent(btn_solvencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btn_dices))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_editarcreditos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_quitar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_agregar)
                        .addGap(88, 88, 88)
                        .addComponent(btn_convalidacion))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_culminacion, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_tesis, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_notas)
                            .addComponent(jButton2)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(lb_culminacion, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_tesis, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel15))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(btn_solvencia)
                            .addComponent(jButton1)
                            .addComponent(btn_dices))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_agregar)
                            .addComponent(btn_quitar)
                            .addComponent(btn_editarcreditos)
                            .addComponent(btn_convalidacion))
                        .addGap(15, 15, 15))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_certificadoingles)
                            .addComponent(btn_constanciainlngles))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cedulaActionPerformed

    private void txt_cedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cedulaKeyTyped
      char c= evt.getKeyChar();
      if (c==KeyEvent.VK_ENTER){
           if (cargarestudiante(this.txt_cedula.getText()))
            {
              String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
              cargartablaconstancia(buscarrhp(this.txt_cedula.getText(), arraycursos[0]));         
                fecha_tesis(buscaralumno(this.txt_cedula.getText(), arraycursos[0]));
              calcularindice(this.tablamaterias);
              nac(this.txt_cedula.getText());
              String rhp = buscarrhpingles(this.txt_cedula.getText());
              String rhpitaliano=buscarrhpitaliano(this.txt_cedula.getText());
              //JOptionPane.showMessageDialog(null, rhp);
              if(!"".equals(rhpitaliano)){
                 this.lb_ingles.setText(AprobadoIngles(rhpitaliano));
                   periodoingles(lb_ingles.getText());
                   this.lb_infoidioma.setText("ITALIANO");
              }else{
                 if (!"".equals(rhp)) {
                this.lb_infoidioma.setText("INGLES");
                this.lb_ingles.setText(AprobadoIngles(rhp));
                periodoingles(lb_ingles.getText());
              }  
              }
             
            int t;
            
            if(doctorado()){
                t=2;
                System.out.println("Doctorado");
            }else{
                t=1;
            }
              regimen_permanencia(t);
              this.btn_agregar.setVisible(true);
              this.btn_quitar.setVisible(true);
              this.btn_editarcreditos.setVisible(true);
            }
            else
            {
              JOptionPane.showMessageDialog(null, "Alumno no tiene Materias en SIGMA", "Error", 0);
            }
      }
    if ((c == KeyEvent.VK_DELETE))
    {
        reiniciarJTable(this.tablamaterias);
         limpiar();
         this.btn_agregar.setVisible(false);
         this.btn_quitar.setVisible(false);
         this.btn_editarcreditos.setVisible(false);
      
    }
    if ((c == KeyEvent.VK_BACK_SPACE))
    {
        reiniciarJTable(this.tablamaterias);
         limpiar();
         this.btn_agregar.setVisible(false);
         this.btn_quitar.setVisible(false);
         this.btn_editarcreditos.setVisible(false);
      
    }
     
    }//GEN-LAST:event_txt_cedulaKeyTyped

    private void btn_notasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notasActionPerformed
      if (this.tablamaterias.getRowCount() != 0)
    {
      List Resultados = new ArrayList();
      
      SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
      
      Date fechaDate = new Date();
      String fecha = formateador.format(fechaDate);
      
      Resultados.clear();
      for (int fila = 0; fila < this.tablamaterias.getRowCount(); fila++)
      {
        CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
        Resultados.add(tipo);
      }
      Map map = new HashMap();
      String nac=" ";
      JDialog reporte = new JDialog();
      reporte.setSize(900, 700);
      reporte.setLocationRelativeTo(null);
      reporte.setTitle("Constancia de Notas");
      String resul="";
      String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
       resul=arraycursos[1];
       int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
       resul=resul.replaceAll("TRIGAL-VALENCIA", "");
       resul=resul.replaceAll("CARACAS", "");
       resul=resul.substring(0,resul.length()-3);
            }
      map.put("fecha", fecha);
      map.put("cedula", this.txt_cedula.getText());
      map.put("nombre", this.lb_nombre.getText());
      map.put("programa", resul);
      map.put("cohorte", this.lb_cohorte.getText());
      map.put("creditosaprobados", this.lb_creditos.getText());
      map.put("indice", this.lb_indice.getText());
      if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
      if (this.lb_ingles.getText() != "") {
          if(this.lb_notaingles.getText().equals("Reconocido")){
              map.put("idioma", "Reconocimiento " + this.lb_ingles.getText());
          }else{
            map.put("idioma", "Aprobado " + this.lb_ingles.getText());  
          }
        
      } else {
        if (resultado2 !=-1){
              map.put("idioma","Exonerado");
          }else{
            map.put("idioma", "No Aprobado");  
          }
      }
       ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
      try
      {
        JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Constanciasinmenbrete.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reportes, map, new JRBeanCollectionDataSource(Resultados));
        
        JasperViewer jviewer = new JasperViewer(print, false);
        jviewer.setTitle("Constancia de Notas");
        jviewer.setVisible(true);
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "Tabla de Materias Vacia", "Error", 0);
    }
    }//GEN-LAST:event_btn_notasActionPerformed

    private void cb_programasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_programasItemStateChanged
        if(cb_programas.getItemCount()>0){
        String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
        cargartablaconstancia(buscarrhp(this.txt_cedula.getText(), arraycursos[0]));
        calcularindice(this.tablamaterias);
        String rhp = buscarrhpingles(this.txt_cedula.getText());
        if (!"".equals(rhp)) {
          this.lb_ingles.setText(AprobadoIngles(rhp));
        }
         int t;
            
         if(doctorado()){
                t=2;
                System.out.println("Doctorado");
            }else{
                t=1;
            }
              regimen_permanencia(t);
        }
        
    }//GEN-LAST:event_cb_programasItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     if (this.tablamaterias.getRowCount() != 0)
    {
        if(!lb_cohorte.getText().equals("")){
            int ano,periodo;
            int t;
            String[] arraycohorte = lb_cohorte.getText().split("-");
            ano=Integer.parseInt(arraycohorte[0]);
            periodo=Integer.parseInt(arraycohorte[1]);
            if(doctorado()){
                t=2;
                System.out.println("Doctorado");
            }else{
                t=1;
            }
            if(calculo_estatus(periodo,ano,t)){
    
            List Resultados = new ArrayList();

            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");

            Date fechaDate = new Date();
            String fecha = formateador.format(fechaDate);

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
              Resultados.add(tipo);
            }
            Map map = new HashMap();
            String nac="";

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Constancia de Notas");
            String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
             String resul="";
      
            resul=arraycursos[1];
            int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
            resul=resul.replaceAll("TRIGAL-VALENCIA", "");
            resul=resul.replaceAll("CARACAS", "");
            resul=resul.substring(0,resul.length()-3);
            }
             map.put("fecha", fecha);
            map.put("cedula", this.txt_cedula.getText());
            map.put("nombre", this.lb_nombre.getText());
            map.put("programa", resul);
            map.put("cohorte", periodo_actual);
            if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            try
            {
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Constanciaestudios.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Constancia de Estudios");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
          }else{
                JOptionPane.showMessageDialog(null, "Alumno NO REGULAR");
            }  
    }
    }else{
             JOptionPane.showMessageDialog(null, "Alumno debe tener Inscrito\nal menos una Materia","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         if (this.tablamaterias.getRowCount() != 0)
    {
      List Resultados = new ArrayList();
      
      SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
      
      Date fechaDate = new Date();
      String fecha = formateador.format(fechaDate);
      String menbrete= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa y dispone hasta "+lb_culminacion.getText()+" para "
              + "la presentación y aprobación del Trabajo de Grado:";
      String menbretedoctorado= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa y dispone hasta "+lb_culminacion.getText()+" para "
              + "la presentación y aprobación del Trabajo Doctoral de Grado:";
      
      Resultados.clear();
      for (int fila = 0; fila < this.tablamaterias.getRowCount(); fila++)
      {
        CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
        Resultados.add(tipo);
      }
      Map map = new HashMap();
      String nac="";
      
      JDialog reporte = new JDialog();
      reporte.setSize(900, 700);
      reporte.setLocationRelativeTo(null);
      reporte.setTitle("Constancia de Culminacion");
      String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
       String resul="";
       resul=arraycursos[1];
       int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
       resul=resul.replaceAll("TRIGAL-VALENCIA", "");
       resul=resul.replaceAll("CARACAS", "");
       resul=resul.substring(0,resul.length()-3);
            }
      map.put("fecha", fecha);
      map.put("cedula", this.txt_cedula.getText());
      map.put("nombre", this.lb_nombre.getText());
      map.put("programa", resul);
      map.put("cohorte", this.lb_cohorte.getText());
      map.put("creditosaprobados", this.lb_creditos.getText());
      map.put("indice", this.lb_indice.getText());
      if(doctorado()){
      map.put("menbrete", menbretedoctorado);
      }else{
          map.put("menbrete", menbrete);
      }
      
      if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
      if (this.lb_ingles.getText() != "") {
        if(this.lb_notaingles.getText().equals("Reconocido")){
              map.put("idioma", "Reconocimiento " + this.lb_ingles.getText());
          }else{
            map.put("idioma", "Aprobado " + this.lb_ingles.getText());  
          }
      } else {
        if (resultado2 !=-1){
              map.put("idioma","Exonerado");
          }else{
            map.put("idioma", "No Aprobado");  
          }
      }
       ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
      try
      {
        JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Constanciadeculminacion.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reportes, map, new JRBeanCollectionDataSource(Resultados));
        
        JasperViewer jviewer = new JasperViewer(print, false);
        jviewer.setTitle("Constancia de Culminacion");
        jviewer.setVisible(true);
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "Tabla de Materias Vacia", "Error", 0);
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarActionPerformed
    this.panelmateria.setSize(760, 304);
    this.panelmateria.setLocationRelativeTo(null);
     cargarprograma();
     if(cb_Progra.getItemCount()>0){
     String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
     cargarmaterias(arrayprograma[0]);
     }
     if(cb_asigna.getItemCount()>0){
      String[] arraymateria = cb_asigna.getSelectedItem().toString().split(" -");
     cargarcreditos(arraymateria[0]);
     }
    this.panelmateria.setModal(true);
    this.panelmateria.setVisible(true);
   
    }//GEN-LAST:event_btn_agregarActionPerformed

    private void txt_periodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_periodoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_periodoActionPerformed

    private void cb_PrograItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_PrograItemStateChanged
        if(cb_Progra.getItemCount()>0){
            String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
            cargarmaterias(arrayprograma[0]);
        }
    }//GEN-LAST:event_cb_PrograItemStateChanged

    private void cb_asignaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_asignaItemStateChanged
        if(cb_asigna.getItemCount()>0){
            String[] arraymateria = cb_asigna.getSelectedItem().toString().split(" -");
            cargarcreditos(arraymateria[0]);
        }
    }//GEN-LAST:event_cb_asignaItemStateChanged

    private void btn_materiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_materiaActionPerformed
        try {

            if(!this.txt_nota.getText().equals("")&&(!this.txt_periodo.getText().equals(""))){
                String[] filas = new String[5];
                filas[0] = this.lb_codi.getText();
                if(cb_asigna.getItemCount()>0){
                    String[] arraymateria = cb_asigna.getSelectedItem().toString().split(" - ");
                    filas[1] = arraymateria[1];
                }
                filas[2] = this.lb_cred.getText();
                filas[3] = this.txt_periodo.getText();
                filas[4] = this.txt_nota.getText();
                this.modeloconstancia.addRow(filas);
                this.tablamaterias.setModel(this.modeloconstancia);
                this.tablamaterias.repaint();
                calcularindice(tablamaterias);
                JOptionPane.showMessageDialog(null, "Materia Incluida Exitosamente","Agregado",JOptionPane.INFORMATION_MESSAGE);
                this.txt_nota.setText(null);
                this.txt_periodo.setText(null);
            }else{
                JOptionPane.showMessageDialog(null, "Los Campos de Nota y Periodo no pueden ser Vacios","Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btn_materiaActionPerformed

    private void btn_solvenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_solvenciaActionPerformed
        if (this.tablamaterias.getRowCount() != 0)
    {
        if(!lb_cohorte.getText().equals("")){
           
            int t;
            
            if(doctorado()){
                t=2;
                System.out.println("Doctorado");
            }else{
                t=1;
            }
            String menbrete= "Cumplió con todos los créditos exigidos por el programa "
                    + "y dispone hasta "+lb_culminacion.getText()+",  para la entrega  de los tomos del Trabajo de Grado y obtener el título de:";
             
            String menbretedoctorado= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa y dispone hasta "+lb_culminacion.getText()+" para "
              + "la presentación y aprobación del Trabajo Doctoral de Grado:";
            List Resultados = new ArrayList();

            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");

            Date fechaDate = new Date();
            String fecha = formateador.format(fechaDate);

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
              Resultados.add(tipo);
            }
            Map map = new HashMap();
            String nac="";

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Solvencia Academica");
            String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
             String resul="";
            resul=arraycursos[1];
            int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
            resul=resul.replaceAll("TRIGAL-VALENCIA", "");
            resul=resul.replaceAll("CARACAS", "");
            resul=resul.substring(0,resul.length()-3);
            }
            map.put("fecha", fecha);
            map.put("cedula", this.txt_cedula.getText());
            map.put("nombre", this.lb_nombre.getText());
            map.put("programa", resul);
            map.put("cohorte", lb_cohorte.getText());
            map.put("titulo", titulo());
            map.put("menbrete", menbrete);
             if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            try
            {
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/solvencia.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Solvencia Academica");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
          
    }
    }else{
             JOptionPane.showMessageDialog(null, "Alumno debe tener Inscrito\nal menos una Materia","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btn_solvenciaActionPerformed

    private void btn_dicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dicesActionPerformed
          if (this.tablamaterias.getRowCount() != 0)
    {
      List Resultados = new ArrayList();
      
      SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
      
      Date fechaDate = new Date();
      String fecha = formateador.format(fechaDate);
      String menbrete= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa, presento y aprobó  "
              + "el Trabajo de Grado para optar al titulo Magister:";
      String menbretedoctorado= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa, presento y aprobó  "
              + "el Trabajo Doctoral de Grado para optar al titulo de Doctor:";
       String menbreteespecializacion= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa, presento y aprobó  "
              + "el Trabajo de Grado para optar al titulo de Técnico Especialista:";
      Resultados.clear();
      for (int fila = 0; fila < this.tablamaterias.getRowCount(); fila++)
      {
        CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
        Resultados.add(tipo);
      }
      Map map = new HashMap();
      String nac="";
      
      JDialog reporte = new JDialog();
      reporte.setSize(900, 700);
      reporte.setLocationRelativeTo(null);
      reporte.setTitle("Constancia de Culminacion");
      String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
       String resul="";
       resul=arraycursos[1];
       int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
       resul=resul.replaceAll("TRIGAL-VALENCIA", "");
       resul=resul.replaceAll("CARACAS", "");
       resul=resul.substring(0,resul.length()-3);
            }
      map.put("fecha",lb_tesis.getText());
      map.put("cedula", this.txt_cedula.getText());
      map.put("nombre", this.lb_nombre.getText());
      map.put("programa", resul);
      map.put("cohorte", this.lb_cohorte.getText());
      map.put("creditosaprobados", this.lb_creditos.getText());
      map.put("indice", this.lb_indice.getText());
       if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
      if(doctorado()){
      map.put("menbrete", menbretedoctorado);
      }else if (resultado2 !=-1){
          map.put("menbrete", menbreteespecializacion);
      }else{
          map.put("menbrete", menbrete);
      }
      if (this.lb_ingles.getText() != ""){
        if(this.lb_notaingles.getText().equals("Reconocido")){
              map.put("idioma", "Reconocimiento " + this.lb_ingles.getText());
          }else{
            map.put("idioma", "Aprobado " + this.lb_ingles.getText());  
          }
      } else {
          if (resultado2 !=-1){
              map.put("idioma","Exonerado");
          }else{
            map.put("idioma", "No Aprobado");  
          }
            
      }
       ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
      try
      {
        JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/ConstanciadeculminacionDICES.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reportes, map, new JRBeanCollectionDataSource(Resultados));
        
        JasperViewer jviewer = new JasperViewer(print, false);
        jviewer.setTitle("Constancia de Culminacion");
        jviewer.setVisible(true);
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "Tabla de Materias Vacia", "Error", 0);
    }
    }//GEN-LAST:event_btn_dicesActionPerformed

    private void btn_convalidacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_convalidacionActionPerformed
      this.convalidacion.setSize(938, 570);
    this.convalidacion.setLocationRelativeTo(null);
     cargarprograma();
     if(cb_Progra1.getItemCount()>0){
     String[] arrayprograma = cb_Progra1.getSelectedItem().toString().split(" -");
     cargarmaterias(arrayprograma[0]);
     }
     if(cb_asigna1.getItemCount()>0){
      String[] arraymateria = cb_asigna1.getSelectedItem().toString().split(" -");
     cargarcreditos(arraymateria[0]);
     }
    this.convalidacion.setModal(true);
    this.convalidacion.setVisible(true);
    }//GEN-LAST:event_btn_convalidacionActionPerformed

    private void cb_Progra1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_Progra1ItemStateChanged
      if(cb_Progra1.getItemCount()>0){
            String[] arrayprograma = cb_Progra1.getSelectedItem().toString().split(" -");
            cargarmaterias(arrayprograma[0]);
        }
    }//GEN-LAST:event_cb_Progra1ItemStateChanged

    private void cb_asigna1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_asigna1ItemStateChanged
         if(cb_asigna1.getItemCount()>0){
            String[] arraymateria = cb_asigna1.getSelectedItem().toString().split(" -");
            cargarcreditos(arraymateria[0]);
        }
    }//GEN-LAST:event_cb_asigna1ItemStateChanged

    private void txt_periodo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_periodo1ActionPerformed
        
    }//GEN-LAST:event_txt_periodo1ActionPerformed

    private void txt_codigoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_codigoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_codigoCActionPerformed

    private void txt_asignaturaCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_asignaturaCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_asignaturaCActionPerformed

    private void txt_periodoCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_periodoCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_periodoCActionPerformed

    private void txt_notaCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_notaCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_notaCActionPerformed

    private void btn_cargamanualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cargamanualActionPerformed
        this.carga_manual.setSize(760, 170);
    this.carga_manual.setLocationRelativeTo(null);
    this.carga_manual.setModal(true);
    this.carga_manual.setVisible(true);
    }//GEN-LAST:event_btn_cargamanualActionPerformed

    private void btn_AGREGAMANUALActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AGREGAMANUALActionPerformed
         try {

            if(!this.txt_notaC.getText().equals("")&&(!this.txt_periodoC.getText().equals(""))){
                String[] filas = new String[5];
                filas[0] = this.txt_codigoC.getText();
                filas[1] = this.txt_asignaturaC.getText();
                filas[2] = this.txt_creditosC.getText()+".00";
                filas[3] = this.txt_periodoC.getText();
                filas[4] = this.txt_notaC.getText();
                this.modeloconvalidacion.addRow(filas);
                this.tabla_convalidacion.setModel(this.modeloconvalidacion);
                this.tabla_convalidacion.repaint();
                JOptionPane.showMessageDialog(null, "Materia Incluida Exitosamente","Agregado",JOptionPane.INFORMATION_MESSAGE);
                this.txt_notaC.setText(null);
                this.txt_periodoC.setText(null);
            }else{
                JOptionPane.showMessageDialog(null, "Los Campos de Nota y Periodo no pueden ser Vacios","Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btn_AGREGAMANUALActionPerformed

    private void btn_agregarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarCActionPerformed
        try {

            if(!this.txt_nota1.getText().equals("")&&(!this.txt_periodo1.getText().equals(""))){
                String[] filas = new String[5];
                filas[0] = this.lb_codi1.getText();
                if(cb_asigna1.getItemCount()>0){
                    String[] arraymateria = cb_asigna1.getSelectedItem().toString().split(" - ");
                    filas[1] = arraymateria[1];
                }
                filas[2] = this.lb_cred1.getText();
                filas[3] = this.txt_periodo1.getText();
                filas[4] = this.txt_nota1.getText();
                this.modeloconvalidacion.addRow(filas);
                this.tabla_convalidacion.setModel(this.modeloconvalidacion);
                this.tabla_convalidacion.repaint();
                //JOptionPane.showMessageDialog(null, "Materia Incluida Exitosamente","Agregado",JOptionPane.INFORMATION_MESSAGE);
                this.txt_nota1.setText("");
                this.txt_periodo1.setText("");
            }else{
                JOptionPane.showMessageDialog(null, "Los Campos de Nota y Periodo no pueden ser Vacios","Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btn_agregarCActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        if (this.tabla_convalidacion.getRowCount() != 0)
    {
      List Resultados = new ArrayList();
      
      SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
      
      Date fechaDate = new Date();
      String fecha = formateador.format(fechaDate);
      String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
      String menbrete= "Ciudadano (a)\nProf. (a) "+ coordinador(arraycursos[0])
              + "\nCoordinador(a) de la "+arraycursos[1]
              + "\nPresente.-";
      String menbrete1= "   Para su información fines y consiguientes, anexo comunicación suscrita por el Ing. "+lb_nombre.getText()
              + ", de CI: "+txt_cedula.getText()
              + " donde solicita la CONVALIDACION de las siguientes asignaturas:";
      
      Resultados.clear();
      for (int fila = 0; fila < this.tabla_convalidacion.getRowCount(); fila++)
      {
        CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tabla_convalidacion.getValueAt(fila, 0)), 
                String.valueOf(this.tabla_convalidacion.getValueAt(fila, 1)), String.valueOf(this.tabla_convalidacion.getValueAt(fila, 2)), 
                String.valueOf(this.tabla_convalidacion.getValueAt(fila, 3)), String.valueOf(this.tabla_convalidacion.getValueAt(fila, 4)));
        Resultados.add(tipo);
      }
      Map map = new HashMap();
      
      JDialog reporte = new JDialog();
      reporte.setSize(900, 700);
      reporte.setLocationRelativeTo(null);
      reporte.setTitle("Constancia de Convalidacion");
     
       String resul="";
       resul=arraycursos[1];
       int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
       resul=resul.replaceAll("TRIGAL-VALENCIA", "");
       resul=resul.replaceAll("CARACAS", "");
       resul=resul.substring(0,resul.length()-3);
            }
      map.put("fecha",fecha);
      map.put("cedula", this.txt_cedula.getText());
      map.put("nombre", this.lb_nombre.getText());
      map.put("menbrete", menbrete);
      map.put("menbrete1", menbrete1);
      
      ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
      try
      {
       convalidacion.hide();
        JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/constanciaconvalidacion.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reportes, map, new JRBeanCollectionDataSource(Resultados));
        
        JasperViewer jviewer = new JasperViewer(print, false);
        jviewer.setTitle("Constancia de Convalidacion");
        jviewer.setVisible(true);
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "Tabla de Materias Vacia", "Error", 0);
    }
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.Prorroga.setSize(486, 170);
    this.Prorroga.setLocationRelativeTo(null);
    this.Prorroga.setModal(true);
    this.Prorroga.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnimprimirPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirPActionPerformed
         if (this.tablamaterias.getRowCount() != 0)
    {
      List Resultados = new ArrayList();
      
      SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
      
      Date fechaDate = new Date();
      String fecha = formateador.format(fechaDate);
      String menbrete= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa"
              + ", por Resolucion del Consejo de Postgrado en Sesion Ordinaria Nª "+txt_numero.getValue()+" de fecha "+obt_fecha(txtfecha)
              +  " dispone hasta "+txtdispone.getText() +" para la presentación y aprobación del Trabajo de Grado:";
      String menbretedoctorado= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa"
              + ", por Resolucion del Consejo de Postgrado en Sesion Ordinaria Nª "+txt_numero.getValue()+" de fecha "+obt_fecha(txtfecha)
              +  " dispone hasta "+txtdispone.getText() +" para "
              + "la presentación y aprobación del Trabajo Doctoral de Grado:";
      
      Resultados.clear();
      for (int fila = 0; fila < this.tablamaterias.getRowCount(); fila++)
      {
        CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
        Resultados.add(tipo);
      }
      Map map = new HashMap();
      String nac="";
      
      JDialog reporte = new JDialog();
      reporte.setSize(900, 700);
      reporte.setLocationRelativeTo(null);
      reporte.setTitle("Constancia de Culminacion");
      String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
       String resul="";
       resul=arraycursos[1];
       int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
       resul=resul.replaceAll("TRIGAL-VALENCIA", "");
       resul=resul.replaceAll("CARACAS", "");
       resul=resul.substring(0,resul.length()-3);
            }
      map.put("fecha", fecha);
      map.put("cedula", this.txt_cedula.getText());
      map.put("nombre", this.lb_nombre.getText());
      map.put("programa", resul);
      map.put("cohorte", this.lb_cohorte.getText());
      map.put("creditosaprobados", this.lb_creditos.getText());
      map.put("indice", this.lb_indice.getText());
       if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
      if(doctorado()){
      map.put("menbrete", menbretedoctorado);
      }else{
          map.put("menbrete", menbrete);
      }
      if (this.lb_ingles.getText() != "") {
         if(this.lb_notaingles.getText().equals("Reconocido")){
              map.put("idioma", "Reconocimiento " + this.lb_ingles.getText());
          }else{
            map.put("idioma", "Aprobado " + this.lb_ingles.getText());  
          }
      } else {
        map.put("idioma", "No Aprobado");
      }
       ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
      try
      {
          Prorroga.hide();
        JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Constanciadeculminacion.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reportes, map, new JRBeanCollectionDataSource(Resultados));
        
        JasperViewer jviewer = new JasperViewer(print, false);
        jviewer.setTitle("Constancia de Culminacion");
        jviewer.setVisible(true);
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "Tabla de Materias Vacia", "Error", 0);
    }
    }//GEN-LAST:event_btnimprimirPActionPerformed

    private void txt_periodoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_periodoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_periodoKeyTyped

    private void txt_periodo1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_periodo1KeyTyped
       char c= evt.getKeyChar();
      if (c==KeyEvent.VK_ENTER){
          txt_nota1.requestFocus();
      }
      
          
    }//GEN-LAST:event_txt_periodo1KeyTyped

    private void txt_nota1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nota1KeyTyped
      char c= evt.getKeyChar();
      if (c==KeyEvent.VK_ENTER){
          btn_agregarC.requestFocus();
      }
    }//GEN-LAST:event_txt_nota1KeyTyped

    private void btn_SolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SolActionPerformed
         if (this.tablamaterias.getRowCount() != 0)
    {
        if(!lb_cohorte.getText().equals("")){
           
            int t;
            
            if(doctorado()){
                t=2;
                System.out.println("Doctorado");
            }else{
                t=1;
            }
            String menbrete= "Cumplió con todos los créditos exigidos por el programa "
                    + ", por Resolucion del Consejo de Postgrado en Sesion Ordinaria Nª "+txt_numero.getValue()+" de fecha "+obt_fecha(txtfecha)
              +  " dispone hasta "+txtdispone.getText() +" para la presentación y aprobación del Trabajo de Grado para obtener el Titulo de:";
             
            String menbretedoctorado= "El Director de  Control de Estudios de la  Facultad de Ingeniería de la Universidad de Carabobo, "
              + "hace constar que el (la) ciudadano(a) mencionado(a) a continuación cumplió con todos los créditos exigidos por el programa y dispone hasta "+lb_culminacion.getText()+" para "
              + "la presentación y aprobación del Trabajo Doctoral de Grado:";
            List Resultados = new ArrayList();

            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");

            Date fechaDate = new Date();
            String fecha = formateador.format(fechaDate);

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia(String.valueOf(this.tablamaterias.getValueAt(fila, 0)), String.valueOf(this.tablamaterias.getValueAt(fila, 1)), String.valueOf(this.tablamaterias.getValueAt(fila, 2)), String.valueOf(this.tablamaterias.getValueAt(fila, 3)), String.valueOf(this.tablamaterias.getValueAt(fila, 4)));
              Resultados.add(tipo);
            }
            Map map = new HashMap();
            String nac="";

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Solvencia Academica");
            String[] arraycursos = this.cb_programas.getSelectedItem().toString().split(" -");
             String resul="";
            resul=arraycursos[1];
            int resultado2 = resul.indexOf("ESPECIALIZACIÓN");
            if (resultado2 !=-1){
            resul=resul.replaceAll("TRIGAL-VALENCIA", "");
            resul=resul.replaceAll("CARACAS", "");
            resul=resul.substring(0,resul.length()-3);
            }
            map.put("fecha", fecha);
            map.put("cedula", this.txt_cedula.getText());
            map.put("nombre", this.lb_nombre.getText());
            map.put("programa", resul);
            map.put("cohorte", lb_cohorte.getText());
            map.put("titulo", titulo());
            map.put("menbrete", menbrete);
             if (lb_nacionalidad.getText().equals("VENEZOLANO")){
          nac="V-";
      }else if (lb_nacionalidad.getText().equals("EXTRANJERO")){
          nac="E-";
      }else{
          nac="P-";
      }
      map.put("nac", nac);
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            try
            {
                Prorroga.hide();
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/solvencia.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Solvencia Academica");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
          
    }
    }else{
             JOptionPane.showMessageDialog(null, "Alumno debe tener Inscrito\nal menos una Materia","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btn_SolActionPerformed

    private void btn_certificadoinglesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_certificadoinglesActionPerformed
    if ((!lb_ingles.getText().equals(""))&&(!lb_infoidioma.getText().equals("ITALIANO")))
    {
            List Resultados = new ArrayList();

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia("", "", "", "", "");
              Resultados.add(tipo);
            }
            Map map = new HashMap();

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Certificado de Ingles");
            
            map.put("nombre", this.lb_nombre.getText());
            map.put("cedula", this.txt_cedula.getText());
            map.put("nota", lb_notaingles.getText());
            map.put("periodo", lb_inglesperiodo.getText());
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            try
            {
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Certificadoingles.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Certificado de Ingles");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
         
    
    }else{
             JOptionPane.showMessageDialog(null, "Alumno no tiene Ingles Aprobado","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btn_certificadoinglesActionPerformed

    private void btn_imprimiringlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimiringlesActionPerformed
       if ((!lb_ingles.getText().equals(""))&&(!lb_infoidioma.getText().equals("ITALIANO")))
    {
            List Resultados = new ArrayList();

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia("", "", "", "", "");
              Resultados.add(tipo);
            }
            Map map = new HashMap();

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Constancia  de Ingles");
            
            map.put("nombre", this.lb_nombre.getText());
            map.put("cedula", this.txt_cedula.getText());
            map.put("fecha", obt_fecha(dateingles));
            map.put("nota", lb_notaingles.getText());
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            this.ingles.hide();
            try
            {
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/constanciaingles.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Constancia de Ingles");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
         
    
    }else{
             JOptionPane.showMessageDialog(null, "Alumno no tiene Ingles Aprobado","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btn_imprimiringlesActionPerformed

    private void btn_constanciainlnglesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_constanciainlnglesActionPerformed
      this.ingles.setSize(250, 60);
      this.ingles.setTitle("Introduzca FECHA DE APROBACION");
    this.ingles.setLocationRelativeTo(null);
    this.ingles.setModal(true);
    this.ingles.setVisible(true);
    }//GEN-LAST:event_btn_constanciainlnglesActionPerformed

    private void btn_quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_quitarActionPerformed
   
    
    int fsel = this.tablamaterias.getSelectedRow();
    if (fsel == -1)
    {
      JOptionPane.showMessageDialog(null, "Debe selecionar la materia a eliminar", "Advertencia", 0);
    }
    else
    {
      int resp = JOptionPane.showConfirmDialog(null, "Estas Seguro de Eliminar esta materia", "Eliminar", 0);
      if (resp == 0)
      {
          
        this.modeloconstancia = ((DefaultTableModel)this.tablamaterias.getModel());
        this.modeloconstancia.removeRow(fsel);
         calcularindice(this.tablamaterias);
      }
    }
    }//GEN-LAST:event_btn_quitarActionPerformed

    private void btn_editarcreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarcreditosActionPerformed
       lb_creditos.setText(JOptionPane.showInputDialog("Ingrese los Creditos"));
    }//GEN-LAST:event_btn_editarcreditosActionPerformed

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
        int fsel = this.tabla_convalidacion.getSelectedRow();
    if (fsel == -1)
    {
      JOptionPane.showMessageDialog(null, "Debe selecionar la materia a eliminar", "Advertencia", 0);
    }
    else
    {
      int resp = JOptionPane.showConfirmDialog(null, "Estas Seguro de Eliminar esta materia", "Eliminar", 0);
      if (resp == 0)
      {
          
        this.modeloconvalidacion = ((DefaultTableModel)this.tabla_convalidacion.getModel());
        this.modeloconvalidacion.removeRow(fsel);
      }
    }
    }//GEN-LAST:event_btn_borrarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       if ((!lb_ingles.getText().equals(""))&&(!lb_infoidioma.getText().equals("INGLES")))
    {
            List Resultados = new ArrayList();

            Resultados.clear();
            for (int fila = 0; fila < 1; fila++)
            {
              CamposConstancia tipo = new CamposConstancia("", "", "", "", "");
              Resultados.add(tipo);
            }
            Map map = new HashMap();

            JDialog reporte = new JDialog();
            reporte.setSize(900, 700);
            reporte.setLocationRelativeTo(null);
            reporte.setTitle("Certificado de Italiano");
            
            map.put("nombre", this.lb_nombre.getText());
            map.put("cedula", this.txt_cedula.getText());
            map.put("nota", lb_notaingles.getText());
            map.put("periodo", lb_inglesperiodo.getText());
            ImageIcon icono  = new ImageIcon ("C:\\firmaconstancia\\loading.gif");
            JOptionPane pane =new JOptionPane("Generando Reporte Espere...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, icono);
            final JDialog dialog = pane.createDialog(null,"Generando");
            dialog.setModal(true);
            Timer timer =new Timer(1*1000, (ActionEvent ev) -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
            dialog.show();
            timer.stop();
            
            try
            {
              JasperReport reportes = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("Reportes/Certificadoitaliano.jrxml"));
              JasperPrint print = JasperFillManager.fillReport(reportes, map,new JRBeanCollectionDataSource(Resultados));

              JasperViewer jviewer = new JasperViewer(print, false);
              jviewer.setTitle("Certificado de Italiano");
              jviewer.setVisible(true);
            }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error", 0);
            }
         
    
    }else{
             JOptionPane.showMessageDialog(null, "Alumno no tiene ITALIANO Aprobado","Mensaje",JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_jButton4ActionPerformed

        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog Prorroga;
    private javax.swing.JButton btn_AGREGAMANUAL;
    private javax.swing.JButton btn_Sol;
    private javax.swing.JButton btn_agregar;
    private javax.swing.JButton btn_agregarC;
    private javax.swing.JButton btn_borrar;
    private javax.swing.JButton btn_cargamanual;
    private javax.swing.JButton btn_certificadoingles;
    private javax.swing.JButton btn_constanciainlngles;
    private javax.swing.JButton btn_convalidacion;
    private javax.swing.JButton btn_dices;
    private javax.swing.JButton btn_editarcreditos;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_imprimiringles;
    private javax.swing.JButton btn_materia;
    private javax.swing.JButton btn_notas;
    private javax.swing.JButton btn_quitar;
    private javax.swing.JButton btn_solvencia;
    private javax.swing.JButton btnimprimirP;
    private javax.swing.JDialog carga_manual;
    private javax.swing.JComboBox<String> cb_Progra;
    private javax.swing.JComboBox<String> cb_Progra1;
    private javax.swing.JComboBox<String> cb_asigna;
    private javax.swing.JComboBox<String> cb_asigna1;
    private javax.swing.JComboBox<String> cb_programas;
    private javax.swing.JDialog convalidacion;
    private com.toedter.calendar.JDateChooser dateingles;
    private javax.swing.JDialog ingles;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lb_codi;
    private javax.swing.JLabel lb_codi1;
    private javax.swing.JLabel lb_cohorte;
    private javax.swing.JLabel lb_cred;
    private javax.swing.JLabel lb_cred1;
    private javax.swing.JLabel lb_creditos;
    private javax.swing.JLabel lb_culminacion;
    private javax.swing.JLabel lb_indice;
    private javax.swing.JLabel lb_infoidioma;
    private javax.swing.JLabel lb_ingles;
    private javax.swing.JLabel lb_inglesperiodo;
    private javax.swing.JLabel lb_nacionalidad;
    private javax.swing.JLabel lb_nombre;
    private javax.swing.JLabel lb_notaingles;
    private javax.swing.JLabel lb_tesis;
    private javax.swing.JDialog panelmateria;
    private javax.swing.JTable tabla_convalidacion;
    private javax.swing.JTable tablamaterias;
    private javax.swing.JTextField txt_asignaturaC;
    private javax.swing.JTextField txt_cedula;
    private javax.swing.JTextField txt_codigoC;
    private javax.swing.JTextField txt_creditosC;
    private javax.swing.JTextField txt_nota;
    private javax.swing.JTextField txt_nota1;
    private javax.swing.JTextField txt_notaC;
    private javax.swing.JSpinner txt_numero;
    private javax.swing.JTextField txt_periodo;
    private javax.swing.JTextField txt_periodo1;
    private javax.swing.JTextField txt_periodoC;
    private javax.swing.JTextField txtdispone;
    private com.toedter.calendar.JDateChooser txtfecha;
    // End of variables declaration//GEN-END:variables
}
