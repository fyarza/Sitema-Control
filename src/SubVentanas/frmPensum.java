/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SubVentanas;

import Clases.FooterPiePaginaiText;
import Clases.HeaderPiePaginaitext;
import Clases.PDF_modalidad;
import Clases.PDF_pensum;
import Clases.ProgramaObj;
import Clases.ValidarLetras;
import Conexion.Clase_Conexion;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.glass.events.KeyEvent;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

 

/**
 *
 * @author Gabriela
 */
public class frmPensum extends javax.swing.JFrame {

    /**
     * Creates new form Pensum
     */
    
 
    
    DefaultTableModel modelopensum;
    ValidarLetras letra;
    File folder;
    ProgramaObj programa;
    String rutaportada;
    public frmPensum() {
        initComponents();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.modelopensum=((DefaultTableModel)this.tabla_materias.getModel());
         inicio();
          rutaportada="C:\\firmaconstancia\\portadas\\matematicaycomputacion.pdf";
        cargarprograma();
    }
    
    
    private void limpiar(){
        this.txt_nombre.setText(null);
        this.txt_anograduacion.setText(null);
        this.txt_peridoinicio.setText(null);
        this.txt_periodoculminacion.setText(null);
    }
    
    
      private boolean cargarestudiante(String cedula)
  {
    boolean resul = false;
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2) as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa != 24 and rp.status!=500";
      //JOptionPane.showMessageDialog(this, sql);
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
      ResultSetMetaData rsmd = rset.getMetaData();
      
      
      while (rset.next())
      {
        resul = true;
        this.txt_nombre.setText(rset.getString("nombre"));
        this.cb_Progra.addItem(rset.getString("nprograma"));
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
      
      
      private void regimen_permanencia(int tipo){
       int ano,periodo,a,p=0;
       String[] arraycohorte = txt_peridoinicio.getText().split("-");
            a=Integer.parseInt(arraycohorte[1]);
            p=Integer.parseInt(arraycohorte[0]);
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
                   txt_periodoculminacion.setText("01-"+ano);
                   break;
               case 2:
                   txt_periodoculminacion.setText("02-"+ano);
                   break; 
               default:
                   txt_periodoculminacion.setText("03-"+ano);
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
                   txt_periodoculminacion.setText("01-"+ano);
                   break;
               case 2:
                   txt_periodoculminacion.setText("02-"+ano);
                   break; 
               default:
                   txt_periodoculminacion.setText("03-"+ano);
                   break;
           }
       }
     }
      
private String buscarrhp(String cedula, String codigo)
  {
    String resul = "";
    try
    {
      Clase_Conexion con = new Clase_Conexion("postgrado.ing.uc.edu.ve", "postgrado", "pguser", "pgu2014-3x");
      
      Connection conexion = con.getConexion();
      String sql = "SELECT concat(p.nombrep,' ',p.nombrep2,' ',p.apellido,' ',p.apellido2)as Nombre,rhp.id rhp, rhp.alumno, rhp.periodo      cohorte, periodos.nombre  ncohorte, rp.programa, concat(programas.codigo,' - ',programas.nombre) as  nprograma, s.nombre as estatus FROM   registro_historico_programas rhp LEFT JOIN registro_programas rp  ON rp.rhp = rhp.id LEFT JOIN personas p ON rhp.alumno = p.id LEFT JOIN programas ON rp.programa = programas.id  LEFT JOIN periodos  ON rhp.periodo = periodos.id LEFT JOIN alumno_status s ON rp.status=s.id  WHERE  p.cedula='" + cedula + "' and rhp.alumno = p.id  AND rp.activo = 1 and rp.programa != 24 and rp.status != 500 and programas.codigo='" + codigo + "'";
      
      PreparedStatement pstm = conexion.prepareCall(sql);
      ResultSet rset = pstm.executeQuery();
    
      if (rset.next())
      {
        resul = rset.getString("rhp");
        this.txt_peridoinicio.setText(rset.getString("ncohorte"));
        
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
    private void inicio(){
         java.util.Date fecha = new Date();
         lb_fecha.setText(fecha.toLocaleString());
         letra=new ValidarLetras();
         poputTable();
    }
    
    private void eliminarmateria(){
     int fsel = this.tabla_materias.getSelectedRow();
        if (fsel == -1)
        {
            JOptionPane.showMessageDialog(null, "Debe selecionar la materia a eliminar", "Advertencia", 0);
        }
        else
        {
            int resp = JOptionPane.showConfirmDialog(null, "Estas Seguro de Eliminar esta materia", "Eliminar", 0);
            if (resp == 0)
            {

                this.modelopensum = ((DefaultTableModel)this.tabla_materias.getModel());
                this.modelopensum.removeRow(fsel);
            }
        }
    
    }
    
    public void poputTable(){
        JPopupMenu popupMenu =new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Eliminar",new ImageIcon(getClass().getResource("/Icono/Borrar3_opt.png")));
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               eliminarmateria();
            }
        });
        popupMenu.add(menuItem1);
        tabla_materias.setComponentPopupMenu(popupMenu);
    }
    public static void reiniciarJTable(JTable Tabla)
  {
    DefaultTableModel modelo = (DefaultTableModel)Tabla.getModel();
    while (modelo.getRowCount() > 0) {
      modelo.removeRow(0);
    }
  }
    
    public static void estampaPDF(String fileNameOrigen, OutputStream outputStream,String nombre) {
    try {
     String archivo_entrada = fileNameOrigen;
 
     PdfReader pdfReader = new PdfReader(archivo_entrada);
     PdfStamper pdfStamper = new PdfStamper(pdfReader,outputStream);
 
     //Salida con clave de seguridad
    //pdfStamper.setEncryption(null,"password".getBytes(),(PdfWriter.ALLOW_PRINTING|PdfWriter.ALLOW_COPY|PdfWriter.ALLOW_SCREENREADERS|PdfWriter.ALLOW_MODIFY_ANNOTATIONS|PdfWriter.ALLOW_FILL_IN), PdfWriter.STRENGTH128BITS);
     float alto_pagina = 0;
     float ancho_pagina = 0;
     float angulo_marca_agua=0;
 
     //Por cada una de las páginas
     for(int i=1; i<= pdfReader.getNumberOfPages(); i++){
 
          PdfContentByte content = pdfStamper.getOverContent(i);
          PdfContentByte content_fondo = pdfStamper.getUnderContent(i);
 
          //Comprobamos si la página está en vertical o en horizontal
          if((pdfReader.getPageRotation(i)==0)||(pdfReader.getPageRotation(i)==180))
          {
               alto_pagina = pdfReader.getPageSize(i).getHeight();
               ancho_pagina = pdfReader.getPageSize(i).getWidth();
               angulo_marca_agua = 5;
          }
          else //Si no es vertical, será apaisada
          {
               alto_pagina = pdfReader.getPageSize(i).getWidth();
               ancho_pagina = pdfReader.getPageSize(i).getHeight();
               angulo_marca_agua = 5;
          }
        
         String texto_costadillo = nombre;
         
         //Marca de agua
         String marca_agua= nombre;
         String Copia=marca_agua;
         for(int g=0;g<5;g++){
             marca_agua=marca_agua+" "+Copia;
         }
       
         Phrase frase = new Phrase(texto_costadillo, FontFactory.getFont(BaseFont.HELVETICA, 10));
         ColumnText.showTextAligned(content, Element.ALIGN_CENTER, frase, 30, alto_pagina/2, 90);
         //Marca de agua en color gris, rotada y con relleno blanco
         Phrase frase_marca_agua= new Phrase(marca_agua , FontFactory.getFont(BaseFont.HELVETICA, 12, Font.PLAIN));
         
         content_fondo.setColorFill(new BaseColor(215,215,215));
         
         for(int l=30;l<=alto_pagina;l=l+50){
         for(int j=0;j<1;j++){
         //ColumnText.showTextAligned(content_fondo, Element.ALIGN_RIGHT, frase_marca_agua,100*j+20,alto_pagina-l, angulo_marca_agua);
         ColumnText.showTextAligned(content_fondo, Element.ALIGN_JUSTIFIED_ALL, frase_marca_agua,10+100*j ,alto_pagina-l, angulo_marca_agua);
         }
         }
      }

      pdfStamper.close();
        pdfReader.close();
 
      } catch (IOException e) {
          System.out.println(e.toString());
       
      } catch (DocumentException e) {
 
      System.out.println(e.toString());
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
     while (rset.next())
      {
        this.cb_Progra.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        
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
      
     while (rset.next())
      {
        this.cb_asigna.addItem(rset.getString("id")+" - "+rset.getString("nombre"));
        
        
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
        
      }
      rset.close();
      conexion.close();
    }
    catch (SQLException exc)
    {
      JOptionPane.showMessageDialog(null, "Error:" + exc.getMessage(), "error", 0);
    }
    
  }
     
private String generarcarpeta(String cedula, String nombre){
         String ruta="";
         try {
         nombre=nombre.replaceAll("\\s", "");
         ruta=System.getProperty("user.home")+"\\Desktop\\EntregaPensum\\"+cedula+"-"+nombre+"\\";
         folder= new File(ruta);
         folder.mkdirs(); // esto crea la carpeta java, independientemente que exista el path completo, si no existe crea toda la ruta necesaria
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
         return ruta;
     }
     
     
     private static void Eliminar(String pArchivo) throws Exception {
		try {
			File fichero = new File(pArchivo);
			if (!fichero.delete())
				throw new Exception("El fichero " + pArchivo
						+ " no puede ser borrado!");
		} catch (Exception e) {
			throw new Exception(e);
		} // end try
	} // end Eliminar
     
     
   private boolean Existe_fichero(String pArchivo) throws  Exception{
       boolean resul=false;
       try {	
            File fichero = new File(pArchivo);
        if (fichero.exists()){
            resul=true;
            System.out.println("El fichero " + pArchivo + " existe");
        }else
            System.out.println("Pues va a ser que no");
        
       } catch (Exception e) {
	throw new Exception(e);
	} // end try
       return resul;
   }
   
   
     private String generarcedula(){
        String resul="";
        if(this.cb_nacionalidad.getSelectedItem().toString().equals("Venezolano")){
            resul="V-"+txt_cedula.getText();
        }else if (this.cb_nacionalidad.getSelectedItem().toString().equals("Extranjero")){
            resul="E-"+txt_cedula.getText();
        }else
            resul ="P-"+txt_cedula.getText();
        return resul;
     }
    
     
     private boolean procesararchivos(String ruta,String tipo){
     boolean resul= false;
     try {
        
         switch(tipo){
               case "vigencia":
                OutputStream outputVC = new FileOutputStream(ruta+"Vigencia.pdf");
                estampaPDF(ruta+"VC.pdf", outputVC,generarcedula()+" "+txt_nombre.getText());
                resul=true; 
                break;
               case "pensum":
            OutputStream outputPS = new FileOutputStream(ruta+"Pensum.pdf");
            estampaPDF(ruta+"V_pensum.pdf", outputPS,generarcedula()+" "+txt_nombre.getText());
                resul=true;
                break;
               case "vigenciasing":
                OutputStream outputVS = new FileOutputStream(ruta+"Vigenciasingraduacion.pdf");
                estampaPDF(ruta+"VS.pdf", outputVS,generarcedula()+" "+txt_nombre.getText());
                resul=true;   
                break;
               case "modalidad":
               OutputStream outputMS = new FileOutputStream(ruta+"Modalidad.pdf");   
                estampaPDF(ruta+"V_modalidad.pdf", outputMS,generarcedula()+" "+txt_nombre.getText());
                resul=true;
                break;
               case "portada":
                OutputStream outputPOR = new FileOutputStream(ruta+"Portada.pdf");   
                estampaPDF(rutaportada, outputPOR,generarcedula()+" "+txt_nombre.getText());
                resul=true;
                
               break;
               default:
                   
                break;         
           }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
        }
     return resul;
     }
     
     
 // Algoritmo que Genera el pdf de la Vigencia sin Graduacion    
     private void generarvigencia(String ruta) {
         Document documento =new Document(PageSize.A4,60,60, 60, 60);
         
         try {
             /*JOptionPane.showMessageDialog(null, System.getProperty("user.home")+"\n"+System.getProperty("os.name")
                     +"\n"+System.getProperty("os.arch")+"\n"+System.getProperty("os.version") ,"informacion",JOptionPane.INFORMATION_MESSAGE);*/
             
             PdfWriter p=PdfWriter.getInstance(documento,new FileOutputStream(ruta+"VS.pdf"));
             HeaderPiePaginaitext header =new HeaderPiePaginaitext();
             FooterPiePaginaiText footer = new FooterPiePaginaiText();
             p.setPageEvent(footer);
             p.setPageEvent(header);
             documento.open();
             
            Paragraph titulo =new Paragraph("\n\nCONSTANCIA",FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             
             titulo.setAlignment(Element.ALIGN_CENTER);
             
             Paragraph parrafo2 =new Paragraph("\n\n\n          La Directora de Estudios para Graduados "
                     + "de la Facultad de Ingeniería de Universidad de Carabobo, certifica que "
                     + "el pensum y el contenido de los programas anexo al presente documento, firmado "
                     + "y sellado por la autoridad correspondiente, se encuentra vigente entre los períodos "
                     + "lectivos "+ txt_peridoinicio.getText()+" al "+txt_periodoculminacion.getText()+", cuyas asignaturas fueron cursadas y aprobadas por el "
                     + "ciudadano(a) "+ txt_nombre.getText()+" Titular de la cedula de identidad Nº "+generarcedula()
                     ,FontFactory.getFont(FontFactory.HELVETICA, 13,
                            Font.LAYOUT_LEFT_TO_RIGHT, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo3 = new Paragraph("\n\n          Constancia que se emite a petición de la parte interesada en Naguanagua, "
                        + "el "+otraFecha()+".",FontFactory.getFont(FontFactory.HELVETICA, 13,
                            Font.LAYOUT_LEFT_TO_RIGHT, new BaseColor(0, 0, 0)));
             parrafo3.setAlignment(Element.ALIGN_JUSTIFIED);
             Paragraph director = new Paragraph("\n\n\n\n\nProf(a). Marianna Barrios, Dra.\nDirectora.",FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             director.setAlignment(Element.ALIGN_CENTER);
             
             documento.add(titulo);
             documento.add(parrafo2);
             documento.add(parrafo3);
             documento.add(director);
            documento.close();
            p.close();
         } catch (DocumentException | FileNotFoundException e) {
             Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, e);
         
         }
     }
     
     
// Algoritmo que Genera el pdf de la Vigencia con Graduacion    
private void generarvigenciacongrad(String ruta, ProgramaObj programa) {
         Document documento =new Document(PageSize.A4,60,60, 60, 60);
         
         try {
             /*JOptionPane.showMessageDialog(null, System.getProperty("user.home")+"\n"+System.getProperty("os.name")
                     +"\n"+System.getProperty("os.arch")+"\n"+System.getProperty("os.version") ,"informacion",JOptionPane.INFORMATION_MESSAGE);*/
             
             PdfWriter p=PdfWriter.getInstance(documento,new FileOutputStream(ruta+"VC.pdf"));
             HeaderPiePaginaitext header =new HeaderPiePaginaitext();
             FooterPiePaginaiText footer = new FooterPiePaginaiText();
             p.setPageEvent(footer);
             p.setPageEvent(header);
             documento.open();
             
            Paragraph titulo =new Paragraph("\n\nCONSTANCIA",FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             
             titulo.setAlignment(Element.ALIGN_CENTER);
             
             Paragraph parrafo2 =new Paragraph("\n\n\n          La Directora de Estudios para Graduados "
                     + "de la Facultad de Ingeniería de Universidad de Carabobo, certifica que "
                     + "el pensum y el contenido de los programas anexo al presente documento, firmado "
                     + "y sellado por la autoridad correspondiente, se encuentra vigente entre los períodos "
                     + "lectivos "+ txt_peridoinicio.getText()+" al "+txt_periodoculminacion.getText()+", cuyas asignaturas fueron cursadas y aprobadas por el "
                     + "ciudadano(a) "+ txt_nombre.getText()+" Titular de la cedula de identidad Nº "+generarcedula()+" quien obtuvo el titulo"
                             + " de "+programa.getGrado()+" en el año "+txt_anograduacion.getText()+"."
                     ,FontFactory.getFont(FontFactory.HELVETICA, 13,
                            Font.LAYOUT_LEFT_TO_RIGHT, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo3 = new Paragraph("\n\n          Constancia que se emite a petición de la parte interesada en Naguanagua, "
                        + "el "+otraFecha()+".",FontFactory.getFont(FontFactory.HELVETICA, 13,
                            Font.LAYOUT_LEFT_TO_RIGHT, new BaseColor(0, 0, 0)));
             parrafo3.setAlignment(Element.ALIGN_JUSTIFIED);
             Paragraph director = new Paragraph("\n\n\n\n\nProf(a). Marianna Barrios, Dra.\nDirectora.",FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             director.setAlignment(Element.ALIGN_CENTER);
             
             documento.add(titulo);
             documento.add(parrafo2);
             documento.add(parrafo3);
             documento.add(director);
            documento.close();
            p.close();
         } catch (DocumentException | FileNotFoundException e) {
             Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, e);
         
         }
     }
     
     private boolean verificarenlatabla(String cod){
        boolean resul=true;
        int filas=tabla_materias.getRowCount();
        for (int i = 0; i < filas; i++) {
            if(cod.equals(tabla_materias.getValueAt(i,0).toString())){
                resul=false;
                i=filas;
            }
        }
        return resul;
    }
     private boolean verificarcampos(String Status){
         boolean resul=true;
         if(txt_cedula.getText().equals("")){
             JOptionPane.showMessageDialog(null,"Campo Cedula No Puede ser Vacia!!","Error",JOptionPane.ERROR_MESSAGE );
             resul=false;
         }else if (txt_nombre.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Campo Nombre No Puede ser Vacia!!","Error",JOptionPane.ERROR_MESSAGE );
             resul=false;
         }else if (txt_peridoinicio.getText().equals("")){
             JOptionPane.showMessageDialog(null,"Campo Periodo de Inicio No Puede ser Vacia!!","Error",JOptionPane.ERROR_MESSAGE );
             resul=false;
         }else if (txt_periodoculminacion.getText().equals("")){
             JOptionPane.showMessageDialog(null,"Campo Periodo de Culminacion No Puede ser Vacia!!","Error",JOptionPane.ERROR_MESSAGE );
             resul=false;
         }else if(txt_anograduacion.getText().equals("")&& Status.equals("vigencia")){
             JOptionPane.showMessageDialog(this, "Campo de Año de Graduacion Vacio\n"
                    + "Ingrese un valor para Generar la Constancia","Error",JOptionPane.ERROR_MESSAGE);
             resul=false;
         }
         return resul;
     }
      public String otraFecha() 
     {
       SimpleDateFormat formateador = new SimpleDateFormat("EEEEEEEEE dd 'de' MMMMM 'de' yyyy");
      Calendar cal = Calendar.getInstance();
      cal.setTimeZone(TimeZone.getDefault());
      return formateador.format((new Date()));
     // System.out.println(formateador.format(new Date()));
    }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cb_Progra = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cb_asigna = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        lb_codi = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lb_cred = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_materias = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_cedula = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cb_nacionalidad = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_peridoinicio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_periodoculminacion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_anograduacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cb_programas = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        btn_generarpdf = new javax.swing.JButton();
        btn_agregarmateria = new javax.swing.JButton();
        cpensum = new javax.swing.JCheckBox();
        cvigenciasing = new javax.swing.JCheckBox();
        cvigencia = new javax.swing.JCheckBox();
        cprogramasa = new javax.swing.JCheckBox();
        cmodalidade = new javax.swing.JCheckBox();
        cportada = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        lb_fecha = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información de Asignatura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel6.setLayout(new java.awt.GridLayout(4, 2, 0, 5));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Programa:");
        jPanel6.add(jLabel11);

        cb_Progra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_Progra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_PrograItemStateChanged(evt);
            }
        });
        jPanel6.add(cb_Progra);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Asignatura: ");
        jPanel6.add(jLabel13);

        cb_asigna.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_asigna.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_asignaItemStateChanged(evt);
            }
        });
        jPanel6.add(cb_asigna);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Codigo:");
        jPanel6.add(jLabel14);

        lb_codi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel6.add(lb_codi);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Creditos:");
        jPanel6.add(jLabel17);

        lb_cred.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel6.add(lb_cred);

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 950, 140));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Materias Agregadas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tabla_materias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre Asignatura"
            }
        ));
        jScrollPane1.setViewportView(tabla_materias);
        if (tabla_materias.getColumnModel().getColumnCount() > 0) {
            tabla_materias.getColumnModel().getColumn(0).setMinWidth(70);
            tabla_materias.getColumnModel().getColumn(0).setPreferredWidth(70);
            tabla_materias.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 950, 220));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información Estudiante", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel4.setLayout(new java.awt.GridLayout(7, 2));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Cedula:");
        jPanel4.add(jLabel2);

        txt_cedula.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_cedula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_cedula.setToolTipText("Ej. 7209091 \"solo Numero\"");
        txt_cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cedulaKeyTyped(evt);
            }
        });
        jPanel4.add(txt_cedula);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Nacionalidad:");
        jPanel4.add(jLabel7);

        cb_nacionalidad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_nacionalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Venezolano", "Extranjero", "Pasaporte" }));
        jPanel4.add(cb_nacionalidad);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Nombre:");
        jPanel4.add(jLabel1);

        txt_nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nombre.setToolTipText("Ej. Henrriquez Reveron Reyes (Solo Letras)");
        txt_nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_nombreFocusLost(evt);
            }
        });
        txt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombreKeyTyped(evt);
            }
        });
        jPanel4.add(txt_nombre);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Periodo de inicio:");
        jPanel4.add(jLabel3);

        txt_peridoinicio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_peridoinicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_peridoinicio.setToolTipText("Ej. 01-2017 \"Sin guion solo numero\"");
        txt_peridoinicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_peridoinicioKeyTyped(evt);
            }
        });
        jPanel4.add(txt_peridoinicio);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Periodo de Culminación:");
        jPanel4.add(jLabel4);

        txt_periodoculminacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_periodoculminacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_periodoculminacion.setToolTipText("Ej. 01-2017 \"Sin guion solo numero\"");
        txt_periodoculminacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_periodoculminacionKeyTyped(evt);
            }
        });
        jPanel4.add(txt_periodoculminacion);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Año de Graduación:");
        jPanel4.add(jLabel6);

        txt_anograduacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_anograduacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_anograduacion.setToolTipText("Ej 2006 (solo el Año)");
        txt_anograduacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_anograduacionKeyTyped(evt);
            }
        });
        jPanel4.add(txt_anograduacion);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Programa Principal:");
        jPanel4.add(jLabel8);

        cb_programas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel4.add(cb_programas);

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 950, 170));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btn_generarpdf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_generarpdf.setText("Generar Pdf");
        btn_generarpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generarpdfActionPerformed(evt);
            }
        });

        btn_agregarmateria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_agregarmateria.setText("Agregar");
        btn_agregarmateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarmateriaActionPerformed(evt);
            }
        });

        cpensum.setText("Pensum");

        cvigenciasing.setText("Vigencia sin Graduación");

        cvigencia.setText("Vigencia");

        cprogramasa.setText("Programas Analíticos");

        cmodalidade.setText("Modalidad de Estudios");

        cportada.setText("Portada");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(btn_agregarmateria, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cpensum)
                    .addComponent(cportada))
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cvigencia)
                    .addComponent(cvigenciasing))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cprogramasa)
                    .addComponent(cmodalidade))
                .addGap(119, 119, 119)
                .addComponent(btn_generarpdf, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpensum)
                            .addComponent(cvigencia)
                            .addComponent(cprogramasa))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cvigenciasing)
                            .addComponent(cmodalidade)
                            .addComponent(btn_generarpdf)
                            .addComponent(cportada)))
                    .addComponent(btn_agregarmateria, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 950, 70));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Fecha:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 50, 20));

        lb_fecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_fecha.setText(" ");
        jPanel2.add(lb_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 210, 20));

        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_generarpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generarpdfActionPerformed
        String status="";
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
        if(cvigencia.isSelected()){
            status="vigencia";
        }
        if(verificarcampos(status)){    
        String ruta=generarcarpeta(txt_cedula.getText(),txt_nombre.getText());
        if(cvigenciasing.isSelected()){
           generarvigencia(ruta);//Genera Vigencia sin Graduacion
           procesararchivos(ruta,"vigenciasing");
           try {
            Eliminar(ruta+"VS.pdf");
            } catch (Exception ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            if(cpensum.isSelected()){
            if(cb_Progra.getItemCount()>0){
            String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
            programa =(new ProgramaObj().programa_filtrado(arrayprograma[0]));
            if (programa.es_Vacio()){
                JOptionPane.showMessageDialog(this, "Programa Vacio Verifique el Campo e Intentelo Nuevamente");
            }else{
               PDF_pensum pensum =new PDF_pensum();
            try {
                pensum.crearPDF(programa,ruta,txt_peridoinicio.getText(),txt_periodoculminacion.getText(),txt_nombre.getText(),
                generarcedula(), txt_cedula.getText(),otraFecha());
                procesararchivos(ruta,"pensum");
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            } 
            }
            }
            try {
            Eliminar(ruta+"V_pensum.pdf");         
            } catch (Exception ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            if(cvigencia.isSelected()){
                if(cb_Progra.getItemCount()>0){
            String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
            programa =(new ProgramaObj().programa_filtrado(arrayprograma[0]));
            if (programa.es_Vacio()){
                JOptionPane.showMessageDialog(this, "Programa Vacio Verifique el Campo e Intentelo Nuevamente");
            }else{
                generarvigenciacongrad(ruta,programa);
                procesararchivos(ruta,"vigencia");
            }
            }
            try {
            Eliminar(ruta+"VC.pdf");         
            } catch (Exception ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
            if (cmodalidade.isSelected()){
                if(cb_Progra.getItemCount()>0){
                String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
                programa =(new ProgramaObj().programa_filtrado(arrayprograma[0]));
            if (programa.es_Vacio()){
                JOptionPane.showMessageDialog(this, "Programa Vacio Verifique el Campo e Intentelo Nuevamente");
            }else{
                PDF_modalidad modalidad=new PDF_modalidad();
            try {
                modalidad.crearPDF(programa, ruta, txt_nombre.getText(),generarcedula());
                procesararchivos(ruta,"modalidad");
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            } 
            }
            }
            try {
              Eliminar(ruta+"V_modalidad.pdf");
            } catch (Exception ex) {
                Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
            }   
            }
            
            
            if (cportada.isSelected()){
               if(cb_Progra.getItemCount()>0){
                String[] arrayprograma = cb_Progra.getSelectedItem().toString().split(" -");
                cargarmaterias(arrayprograma[0]);
                switch(arrayprograma[0]){
                    case"3"://Doctorado en Ambiente
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    case"4":// Doctorado en Quimica
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    case"5":// Doctorado en Bioingenieria
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    case"6":// Doctorado en Computo Aplicado
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    case"7": // Doctorado en Indstrial
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    case"8": // Doctorado en Electrica
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;                       
                    case "9":
                        rutaportada="C:\\firmaconstancia\\portadas\\mecanica.pdf";
                    break;
                    case "10":
                         rutaportada="C:\\firmaconstancia\\portadas\\ambiental.pdf";
                        break;
                    case "11":
                         rutaportada="C:\\firmaconstancia\\portadas\\procesos.pdf";
                    break;
                    case "12":
                         rutaportada="C:\\firmaconstancia\\portadas\\industrial.pdf";
                        break;
                    case "13":
                         rutaportada="C:\\firmaconstancia\\portadas\\electrica.pdf";
                        break;
                    case "14":
                         rutaportada="C:\\firmaconstancia\\portadas\\matematicaycomputacion.pdf";
                        break;
                    case "15":
                         rutaportada="C:\\firmaconstancia\\portadas\\gerenciaconstruccion.pdf";
                        break;
                    case "25"://Doctorado en Mecanica
                        JOptionPane.showMessageDialog(pane, "Portada No Encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    default:
                        rutaportada="C:\\firmaconstancia\\portadas\\mecanica.pdf";
                     break;
                        
                }
                procesararchivos(ruta,"portada");
             }
                
          }
            
            
            
            
          //Abre la Ruta de La Creacion de la Carpeta de Constancias Generadas  
          try {
              File path = new File (ruta);
             Desktop.getDesktop().open(path);
            }catch (IOException ex) {
            ex.printStackTrace();
            }
       }
    
    }//GEN-LAST:event_btn_generarpdfActionPerformed

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

    private void btn_agregarmateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarmateriaActionPerformed
        
        if (verificarenlatabla(this.lb_codi.getText())){
          try {
                String[] filas = new String[2];
                
                filas[0] = this.lb_codi.getText();
                if(cb_asigna.getItemCount()>0){
                    String[] arraymateria = cb_asigna.getSelectedItem().toString().split(" - ");
                    filas[1] = arraymateria[1];
                }
                this.modelopensum.addRow(filas);
                this.tabla_materias.setModel(this.modelopensum);
                this.tabla_materias.repaint();
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        }else{
            JOptionPane.showMessageDialog(null, "Materia ya agregada en la Tabla","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_agregarmateriaActionPerformed

    private void txt_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombreKeyTyped
       
       letra.sololetras(evt);
        if (evt.getKeyChar()==KeyEvent.VK_ENTER || evt.getKeyChar()==KeyEvent.VK_TAB){
           cb_nacionalidad.requestFocus();
       }
    }//GEN-LAST:event_txt_nombreKeyTyped

    private void txt_nombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_nombreFocusLost
        
        if(!"".equals(txt_nombre.getText())){
            txt_nombre.setText(txt_nombre.getText().toUpperCase());
             //txt_nombre.setText(letra.tipo_titulo(txt_nombre.getText()));
        }
    }//GEN-LAST:event_txt_nombreFocusLost

    private void txt_cedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cedulaKeyTyped
       letra.solonumero(evt);
       if (evt.getKeyChar()==KeyEvent.VK_ENTER || evt.getKeyChar()==KeyEvent.VK_TAB){
           txt_peridoinicio.requestFocus();
       }
       char c= evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_DELETE))
        {   
            limpiar();
        }
        if ((c == java.awt.event.KeyEvent.VK_BACK_SPACE))
        {
            limpiar();
            

        }
    }//GEN-LAST:event_txt_cedulaKeyTyped

    private void txt_periodoculminacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_periodoculminacionKeyTyped
        letra.solonumero(evt);
       if (evt.getKeyChar()==KeyEvent.VK_ENTER || evt.getKeyChar()==KeyEvent.VK_TAB){
           txt_anograduacion.requestFocus();
       }
       
       if ((txt_periodoculminacion.getText().length()==2)&&(evt.getKeyChar()!=KeyEvent.VK_BACKSPACE)&&(evt.getKeyChar()!=KeyEvent.VK_DELETE)){
            txt_periodoculminacion.setText(txt_periodoculminacion.getText()+"-");
         }
       if (txt_periodoculminacion.getText().length()> 6){
             evt.consume();
         }
       
       
    }//GEN-LAST:event_txt_periodoculminacionKeyTyped

    private void txt_anograduacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_anograduacionKeyTyped
        letra.solonumero(evt);
       if (evt.getKeyChar()==KeyEvent.VK_ENTER || evt.getKeyChar()==KeyEvent.VK_TAB){
           cb_Progra.requestFocus();
       }
        if (txt_anograduacion.getText().length()> 3){
             evt.consume();
         }
    }//GEN-LAST:event_txt_anograduacionKeyTyped

    private void txt_peridoinicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_peridoinicioKeyTyped
     letra.solonumero(evt);
       if (evt.getKeyChar()==KeyEvent.VK_ENTER || evt.getKeyChar()==KeyEvent.VK_TAB){
           txt_periodoculminacion.requestFocus();
       }
       
       if ((txt_peridoinicio.getText().length()==2)&&(evt.getKeyChar()!=KeyEvent.VK_BACKSPACE)&&(evt.getKeyChar()!=KeyEvent.VK_DELETE)){
            txt_peridoinicio.setText(txt_peridoinicio.getText()+"-");
         }
       if (txt_peridoinicio.getText().length()> 6){
             evt.consume();
         }
    }//GEN-LAST:event_txt_peridoinicioKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmPensum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPensum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPensum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPensum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPensum().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agregarmateria;
    private javax.swing.JButton btn_generarpdf;
    private javax.swing.JComboBox<String> cb_Progra;
    private javax.swing.JComboBox<String> cb_asigna;
    private javax.swing.JComboBox<String> cb_nacionalidad;
    private javax.swing.JComboBox<String> cb_programas;
    private javax.swing.JCheckBox cmodalidade;
    private javax.swing.JCheckBox cpensum;
    private javax.swing.JCheckBox cportada;
    private javax.swing.JCheckBox cprogramasa;
    private javax.swing.JCheckBox cvigencia;
    private javax.swing.JCheckBox cvigenciasing;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_codi;
    private javax.swing.JLabel lb_cred;
    private javax.swing.JLabel lb_fecha;
    private javax.swing.JTable tabla_materias;
    private javax.swing.JTextField txt_anograduacion;
    private javax.swing.JTextField txt_cedula;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_peridoinicio;
    private javax.swing.JTextField txt_periodoculminacion;
    // End of variables declaration//GEN-END:variables
}
