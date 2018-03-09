/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriela
 */
public class PDF_modalidad {
    
    public PDF_modalidad(){
        
    }
     public String otraFecha() 
     {
       SimpleDateFormat formateador = new SimpleDateFormat("EEEEEEEEE dd 'de' MMMMM 'de' yyyy");
      Calendar cal = Calendar.getInstance();
      cal.setTimeZone(TimeZone.getDefault());
      return formateador.format((new Date()));
     // System.out.println(formateador.format(new Date()));
    }
    
    public  void crearPDF(ProgramaObj programa,String ruta,
        String nombre,
        String cedula) throws DocumentException, FileNotFoundException{
    //Declaramos un documento como un objecto Document. 
    Document documento =new Document(PageSize.A4,60,60, 60, 60);
    //writer es declarado como el método utilizado para escribir en el archivo.
    try {
        
             PdfWriter p=PdfWriter.getInstance(documento,new FileOutputStream(ruta+"V_modalidad.pdf"));
             HeaderPiePaginaitext header =new HeaderPiePaginaitext();
             FooterPiePaginaiText footer = new FooterPiePaginaiText();
             p.setPageEvent(footer); 
             p.setPageEvent(header);
             documento.open();
             
            Paragraph titulo =new Paragraph("\nCONSTANCIA",FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             
             titulo.setAlignment(Element.ALIGN_CENTER);
             
             
             Paragraph saltolinea=new Paragraph("\n");
             
              Paragraph parrafo2 =new Paragraph("\n\n\nQuien suscribe, La Directora de Estudios para Graduados "
                     + "de la Facultad de Ingeniería de Universidad de Carabobo, Dra. Marianna Barrios hace constar que el "
                     + "el ciudadano(a) "+nombre+" titular de la cedula de identidad Nº "+cedula+", curso estudios en el Programa de "+programa.getNombre()+" "
                     + "cuya modalidad es de tipo PRESENCIAL, el cual está autorizado por el Consejo Nacional Consultivo de Postgrado segun Gaceta "+programa.getGaceta()+" "
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
             
             documento.add(saltolinea);
             documento.add(titulo);
             documento.add(parrafo2);
             documento.add(parrafo3);
             documento.add(director);
             documento.close();
            p.close();
         } catch (DocumentException | FileNotFoundException e) {
             Logger.getLogger(PDF_pensum.class.getName()).log(Level.SEVERE, null, e);
         
             }
}
}
    

