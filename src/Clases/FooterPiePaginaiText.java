/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Gabriela
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.JOptionPane;

public class FooterPiePaginaiText extends  PdfPageEventHelper {
 
 @Override
 public void onEndPage(PdfWriter writer, Document document) {
     try {
         
         com.itextpdf.text.Font ffont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.ITALIC);
         ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Pagina: http://postgrado.ing.uc.edu.ve/",ffont), (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() + 20, 0);
   ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Correo: maestriaingpostgradouc@gmail.com / "
           + "doctoradoingpostgradouc@gmail.com / controldeestudiospostgradoing@gmail.com",ffont), 
           (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() + 10, 0);
   ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Facultad de Ingeniería Naguanagua Sector Bárbula ",ffont), 
           (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() + 0, 0);
   ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Pagina "+ document.getPageNumber(),ffont), 
           (document.right() - document.left()) + document.leftMargin(),
                    document.bottom() + -13, 0);
     } catch (Exception e) {
         System.err.println(e.getMessage());
     }
     
  
 }
}
