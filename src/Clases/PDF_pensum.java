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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriela
 */
public class PDF_pensum {
    
 
    public PDF_pensum() {
    }
    
  

public  void crearPDF(ProgramaObj programa,String ruta,String inicio,String fin,String nombre,
        String nac, String cedula,String fecha) throws DocumentException, FileNotFoundException{
    //Declaramos un documento como un objecto Document. 
    Document documento =new Document(PageSize.A4,60,60, 60, 60);
    //writer es declarado como el método utilizado para escribir en el archivo.
    try {
        int tamano=8;
             PdfWriter p=PdfWriter.getInstance(documento,new FileOutputStream(ruta+"V_pensum.pdf"));
             HeaderPiePaginaitext header =new HeaderPiePaginaitext();
             FooterPiePaginaiText footer = new FooterPiePaginaiText();
             p.setPageEvent(footer);
             p.setPageEvent(header);
             documento.open();
             
            Paragraph titulo =new Paragraph("\nPENSUM DE ESTUDIOS",FontFactory.getFont(FontFactory.HELVETICA, 20,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             
             titulo.setAlignment(Element.ALIGN_CENTER);
             
             Paragraph Fecha =new Paragraph("\n"+fecha,FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             
             Fecha.setAlignment(Element.ALIGN_RIGHT);
             Paragraph saltolinea=new Paragraph("\n");
             
             Paragraph encabezado =new Paragraph("\n"+programa.getNombre()
                     ,FontFactory.getFont(FontFactory.HELVETICA, 13,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             encabezado.setAlignment(Element.ALIGN_LEFT);
            
             
             Paragraph datos =new Paragraph("Periodo: "+inicio+" al "+fin
                     +"\nNombre: "+nombre+"\nC.I."+nac+"",
                     FontFactory.getFont(FontFactory.HELVETICA,11,Font.PLAIN,new BaseColor(0,0,0)));
             datos.setAlignment(Element.ALIGN_LEFT);
             
             Paragraph obligatoria = new Paragraph("\nOBLIGATORIAS:\n\n",
                     FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             obligatoria.setAlignment(Element.ALIGN_LEFT);
             
             Paragraph electiva = new Paragraph("\nELECTIVAS:\n\n",
                     FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             electiva.setAlignment(Element.ALIGN_LEFT);
             
             Paragraph investigacion = new Paragraph("\nINVESTIGACION:\n\n",
                     FontFactory.getFont(FontFactory.HELVETICA, 12,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             investigacion.setAlignment(Element.ALIGN_LEFT);
             
           //Tabla Obligatorias 
           PdfPTable tabla_obligatorias = new PdfPTable(6); 
           //Datos de porcentaje a la tabla (tamaño ancho).
           tabla_obligatorias.setWidthPercentage(100);
           //Datos del ancho de cada columna.
           tabla_obligatorias.setWidths(new float[] {10, 30, 8, 8, 8, 10});
           //tabla de electivas
            //Añadimos una tabla de 6 columnas. 
           PdfPTable tabla_electivas = new PdfPTable(6); 
           //Datos de porcentaje a la tabla (tamaño ancho).
           tabla_electivas.setWidthPercentage(100);
           
           //Datos del ancho de cada columna.
           tabla_electivas.setWidths(new float[] {10, 30, 8, 8, 8, 10});
           PdfPTable tabla_electivas2 = new PdfPTable(6); 
           //Datos de porcentaje a la tabla (tamaño ancho).
           tabla_electivas2.setWidthPercentage(100);
           
           //Datos del ancho de cada columna.
           tabla_electivas2.setWidths(new float[] {10, 30, 8, 8, 8, 10});

           //tabla de investigacion
            //Añadimos una tabla de 6 columnas. 
           PdfPTable tabla_investigacion = new PdfPTable(6); 
           //Datos de porcentaje a la tabla (tamaño ancho).
           tabla_investigacion.setWidthPercentage(100);
           
           //Datos del ancho de cada columna.
           tabla_investigacion.setWidths(new float[] {10, 30, 8, 8, 8, 10});

           
           //Añadimos los títulos a la tabla. 
           Paragraph columna1 = new Paragraph("CODIGO");
           columna1.getFont().setStyle(Font.BOLD);
           columna1.getFont().setSize(9);
           
           Paragraph columna2 = new Paragraph("ASIGNATURA");
           columna2.getFont().setStyle(Font.BOLD);
           columna2.getFont().setSize(9);
           
           Paragraph columna3 = new Paragraph("NIVEL");
           columna3.getFont().setStyle(Font.BOLD);
           columna3.getFont().setSize(9);
           
           Paragraph columna4 = new Paragraph("CREDITO");
           columna4.getFont().setStyle(Font.BOLD);
           columna4.getFont().setSize(9);
          
           Paragraph columna5 = new Paragraph("HORAS");
           columna5.getFont().setStyle(Font.BOLD);
           columna5.getFont().setSize(9);
    
           Paragraph columna6 = new Paragraph("PRELACION");
           columna6.getFont().setStyle(Font.BOLD);
           columna6.getFont().setSize(9);
           
           
           tabla_obligatorias.addCell(columna1);
           tabla_obligatorias.addCell(columna2);
           tabla_obligatorias.addCell(columna3);
           tabla_obligatorias.addCell(columna4);
           tabla_obligatorias.addCell(columna5);
           tabla_obligatorias.addCell(columna6);
           
           tabla_electivas.addCell(columna1);
           tabla_electivas.addCell(columna2);
           tabla_electivas.addCell(columna3);
           tabla_electivas.addCell(columna4);
           tabla_electivas.addCell(columna5);
           tabla_electivas.addCell(columna6);
           
           tabla_electivas2.addCell(columna1);
           tabla_electivas2.addCell(columna2);
           tabla_electivas2.addCell(columna3);
           tabla_electivas2.addCell(columna4);
           tabla_electivas2.addCell(columna5);
           tabla_electivas2.addCell(columna6);
           
           tabla_investigacion.addCell(columna1);
           tabla_investigacion.addCell(columna2);
           tabla_investigacion.addCell(columna3);
           tabla_investigacion.addCell(columna4);
           tabla_investigacion.addCell(columna5);
           tabla_investigacion.addCell(columna6);
           
         ArrayList<Asignatura> listado_obligatoria;
         listado_obligatoria=new Asignatura().List_Asignaturas(programa.getId(), "B");
         ArrayList<Asignatura> listado_electivas;
         listado_electivas=new Asignatura().List_Asignaturas(programa.getId(), "E");
         ArrayList<Asignatura> listado_investigacion;
         listado_investigacion=new Asignatura().List_Asignaturas(programa.getId(), "INV");
         
          
           //Recorremos cada arrayList e imprimimos los resultados.
          
        for (int i = 0; i<listado_obligatoria.size(); i++){
            
            columna1= new Paragraph(listado_obligatoria.get(i).getCodigo());
            columna1.getFont().setSize(tamano);
            tabla_obligatorias.addCell(columna1);

           columna2 = new Paragraph(listado_obligatoria.get(i).getNombre());
           columna2.getFont().setSize(tamano);
           tabla_obligatorias.addCell(columna2);
           
            columna3 = new Paragraph(listado_obligatoria.get(i).getNivel());
           columna3.getFont().setSize(tamano);
           tabla_obligatorias.addCell(columna3);
        
           columna4 = new Paragraph(listado_obligatoria.get(i).getCreditos());
           columna4.getFont().setSize(tamano);
           tabla_obligatorias.addCell(columna4);
           
           columna5 = new Paragraph(listado_obligatoria.get(i).getHoras());
           columna5.getFont().setSize(tamano);
           tabla_obligatorias.addCell(columna5);
           
           columna6 = new Paragraph(listado_obligatoria.get(i).getPrelaciones());
           columna6.getFont().setSize(tamano);
           tabla_obligatorias.addCell(columna6);
        } 
       
        int t;
        if(listado_obligatoria.size()>4){
            t=listado_electivas.size()/3;
        }else
            t=listado_electivas.size()/2;
        //Recorremos cada arrayList e imprimimos los resultados.
         
        for (int i = 0; i<t; i++){
            
            columna1= new Paragraph(listado_electivas.get(i).getCodigo());
            columna1.getFont().setSize(tamano);
            tabla_electivas.addCell(columna1);

           columna2 = new Paragraph(listado_electivas.get(i).getNombre());
           columna2.getFont().setSize(tamano);
           tabla_electivas.addCell(columna2);
           
            columna3 = new Paragraph(listado_electivas.get(i).getNivel());
           columna3.getFont().setSize(tamano);
           tabla_electivas.addCell(columna3);
        
           columna4 = new Paragraph(listado_electivas.get(i).getCreditos());
           columna4.getFont().setSize(tamano);
           tabla_electivas.addCell(columna4);
           
           columna5 = new Paragraph(listado_electivas.get(i).getHoras());
           columna5.getFont().setSize(tamano);
           tabla_electivas.addCell(columna5);
           
           columna6 = new Paragraph(listado_electivas.get(i).getPrelaciones());
           columna6.getFont().setSize(tamano);
           tabla_electivas.addCell(columna6);
        }
        
        for (int i = t; i<listado_electivas.size(); i++){
            
            columna1= new Paragraph(listado_electivas.get(i).getCodigo());
            columna1.getFont().setSize(tamano);
            tabla_electivas2.addCell(columna1);

           columna2 = new Paragraph(listado_electivas.get(i).getNombre());
           columna2.getFont().setSize(tamano);
           tabla_electivas2.addCell(columna2);
           
            columna3 = new Paragraph(listado_electivas.get(i).getNivel());
           columna3.getFont().setSize(tamano);
           tabla_electivas2.addCell(columna3);
        
           columna4 = new Paragraph(listado_electivas.get(i).getCreditos());
           columna4.getFont().setSize(tamano);
           tabla_electivas2.addCell(columna4);
           
           columna5 = new Paragraph(listado_electivas.get(i).getHoras());
           columna5.getFont().setSize(tamano);
           tabla_electivas2.addCell(columna5);
           
           columna6 = new Paragraph(listado_electivas.get(i).getPrelaciones());
           columna6.getFont().setSize(tamano);
           tabla_electivas2.addCell(columna6);
        }
        
        
        
          //Recorremos cada arrayList e imprimimos los resultados.
         
        for (int i = 0; i<listado_investigacion.size(); i++){
            
            columna1= new Paragraph(listado_investigacion.get(i).getCodigo());
            columna1.getFont().setSize(tamano);
            tabla_investigacion.addCell(columna1);

           columna2 = new Paragraph(listado_investigacion.get(i).getNombre());
           columna2.getFont().setSize(tamano);
           tabla_investigacion.addCell(columna2);
           
            columna3 = new Paragraph(listado_investigacion.get(i).getNivel());
           columna3.getFont().setSize(tamano);
           tabla_investigacion.addCell(columna3);
        
           columna4 = new Paragraph(listado_investigacion.get(i).getCreditos());
           columna4.getFont().setSize(tamano);
           tabla_investigacion.addCell(columna4);
           
           columna5 = new Paragraph(listado_investigacion.get(i).getHoras());
           columna5.getFont().setSize(tamano);
           tabla_investigacion.addCell(columna5);
           
           columna6 = new Paragraph(listado_investigacion.get(i).getPrelaciones());
           columna6.getFont().setSize(tamano);
           tabla_investigacion.addCell(columna6);
        }
            Paragraph parrafo1 = new Paragraph("\nEl Programa de "+programa.getNombre()+" Funciona bajo un régimen de créditos, divididos en ("+programa.getDuracion()+") periodos lectivos que a su vez consta 12 semanas de clases presenciales.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
             
                       
             Paragraph parrafo2 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: dos (2) materia de nivelación, (7) materias obligatorias,  (3) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo3= new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: dos (2) materias obligatorias,  (7) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo4 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: una (1) materia de nivelación, (8) materias obligatorias,  (2) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo5 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: dos (2) materia de nivelación, (8) materias obligatorias,  (2) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo6 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: dos (2) materia de nivelación, (5) materias obligatorias,  (5) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo7 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: cinco (5) materias obligatorias,  (4) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo9 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar un mínimo de veintiuno (21) unidades de créditos en seminarios: básicos y electivos. Un máximo de veinticuatro (24) unidades de créditos en seminarios de investigación. "
                     + "Los seminarios de investigación estas compuestos por un máximo de nueve (09) unidades de créditos en seminarios tutoriales de investigación y quince (15) créditos en seminarios complementarios. "
                     + "Los seminarios complementarios son actividades de aprendizaje asociadas a la investigación que realiza el estudiante, bajo la orientación de un asesor, quien lo evaluará trimestralmente. "
                     + "De igual manera debe demostrar el dominio instrumental del idioma inglés y aprobar la Tesis Doctoral.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafo8 = new Paragraph("\nPara Obtener el Titulo de "+programa.getGrado()+" se debe aprobar: dos (2) materia de nivelación, (8) materias obligatorias,  (2) electivas y (2) materias de Investigación de igual manera debe demostrar el dominio "
                     + "instrumental del idioma inglés y aprobar el Trabajo de Grado.",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.PLAIN, new BaseColor(0, 0, 0)));
             parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
             
             Paragraph parrafogeneral = new Paragraph("\nCréditos obligatorios para la obtención del grado: "+programa.getTotal_creditos()+"\n"
                     + "Duración de la carrera: "+programa.getDuracion()+" periodos lectivos",FontFactory.getFont(FontFactory.HELVETICA, 9,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             parrafogeneral.setAlignment(Element.ALIGN_JUSTIFIED);
             Paragraph director = new Paragraph("\n\n\nProf(a). Marianna Barrios, Dra.\nDirectora.",FontFactory.getFont(FontFactory.HELVETICA, 10,
                            Font.BOLD, new BaseColor(0, 0, 0)));
             director.setAlignment(Element.ALIGN_CENTER);
             
             documento.add(titulo);
             documento.add(Fecha);
             documento.add(encabezado);
             documento.add(datos);
             documento.add(obligatoria);
             documento.add(tabla_obligatorias);
             documento.add(investigacion);
             documento.add(tabla_investigacion);
             documento.add(electiva);
             documento.add(tabla_electivas);
             documento.newPage();
             documento.add(saltolinea);
             documento.add(electiva);
             documento.add(tabla_electivas2);
             documento.add(parrafo1);
             
              switch(programa.codigo){
               case "91501":
                   
                   documento.add(parrafo2);
                break;
               case "91512":
                   
                   documento.add(parrafo3);
                   break;
               case "91561":
                   
                   documento.add(parrafo4);
                   break;
               case "91571":
                  
                   documento.add(parrafo5);
                   break;
                case "91581":
                    
                    documento.add(parrafo6);
                break;
                case "91591":
                    
                    documento.add(parrafo7);
                   break;
                  case "91801":
                     
                      documento.add(parrafo8);
                   break;
               default:
                   
                   documento.add(parrafo9);
                   break;
         
           }
             
             documento.add(parrafogeneral);
             documento.add(director);
             
             
             
             //documento.add(director);
            documento.close();
            p.close();
         } catch (DocumentException | FileNotFoundException e) {
             Logger.getLogger(PDF_pensum.class.getName()).log(Level.SEVERE, null, e);
         
             }
}
}

