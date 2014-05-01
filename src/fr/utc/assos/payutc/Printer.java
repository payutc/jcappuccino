package fr.utc.assos.payutc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;

public class Printer {
	public static void print(String data) {
		try {
			System.out.println("Impression depuis Printer");
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

		    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		    aset.add(new MediaPrintableArea(100, 400, 210, 160, Size2DSyntax.MM));

		    InputStream is = new ByteArrayInputStream(data.toString().getBytes());
		    
		    Doc mydoc = new SimpleDoc(is, flavor, null);

		    PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

		    // print using default
		    DocPrintJob job = defaultService.createPrintJob();
		    job.print(mydoc, aset);    	
		}
		catch(Exception e) {
			System.out.println("Échec de l'impression");
			e.printStackTrace();
		}
	}
}
