/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import SubVentanas.frmPensum;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeaderPiePaginaitext extends PdfPageEventHelper {
    
    @Override
 public void onStartPage(PdfWriter writer, Document document) {
    
     Image  headerImage,headerImage2;
             try {
                 headerImage = Image.getInstance(getClass().getResource("/Icono/logouc.png"));
                  headerImage.scalePercent((float) 27.5);
                  headerImage2 = Image.getInstance(getClass().getResource("/Icono/POSTGRADO.png"));
                  headerImage2.scalePercent((float) 40);
  
                Chunk ck =new Chunk(headerImage,0,-75);
                Chunk ce =new Chunk(headerImage2,document.right()-165,-75);
                Paragraph menbrete =new Paragraph("Universidad de Carabobo" +
                                                "\nFacultad de Ingeniería" +
                                                    "\nDirección de Estudios para Graduados",
                     FontFactory.getFont(FontFactory.HELVETICA,14,
                            Font.BOLD, new BaseColor(0, 0, 0)));
            menbrete.setAlignment(Element.ALIGN_CENTER);
            document.add(ck);
            document.add(ce);
            document.add(menbrete);
             } catch (BadElementException | IOException ex) {
                 Logger.getLogger(frmPensum.class.getName()).log(Level.SEVERE, null, ex);
             } catch (DocumentException ex) {
            Logger.getLogger(HeaderPiePaginaitext.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
}
