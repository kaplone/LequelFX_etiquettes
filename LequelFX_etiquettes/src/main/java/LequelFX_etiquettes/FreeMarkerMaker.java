package LequelFX_etiquettes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.scene.image.Image;


import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class FreeMarkerMaker {
	
	static String string = "";
	
	protected static String cutX (String s){
  	  return  s.length() > 16 ? s.substring(0, 16) : s;
    }

	public static Map<Integer, ArrayList<String>> odt2pdf(Path p, Map<Integer, ArrayList<String>> listes) {

		try {
		      // 1) Load Docx file by filling Velocity template engine and cache it to the registry

			  InputStream in = new FileInputStream(new File("src/main/java/LequelFX_etiquettes/modele_etiquette_test_reprise.odt"));

		      IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,TemplateEngineKind.Freemarker);
		      
		      int index_courant = 0; 
		      
		      for (int i=0; i < 10; i++){
					if (listes.get(i).isEmpty()){
						index_courant = i;
						break;
				  };
			  }
		      
		      // ajout du nom du disque
		      listes.get(index_courant).add(p.getFileName().toString());
		      
		      String [] dirlist = p.toFile().list();
		      System.out.println(dirlist);
		      
		      
		      
              listes.get(index_courant).addAll(Arrays.asList(dirlist).stream()
            		                                                 .sorted()
            		                                                 .filter(x -> !x.startsWith(".") && !x.startsWith("@") && !x.equals("Thumbs.db"))
            		                                                 .map( x -> cutX(x))
            		                                                 .limit(9)
            		                                                 .collect(Collectors.toList()));
              
		      FieldsMetadata metadata = report.createFieldsMetadata();      
		      metadata.addFieldAsList("alt");
		      report.setFieldsMetadata(metadata);

		      IContext context = report.createContext();
		      
		      for (int i=0; i < 10; i++){
		    	  
		    	  string = "";
		    	  
		    	  if(! listes.get(i).isEmpty()){
	                  try {
		            	  string = String.format("%03d_%s\n %s_%s", Integer.parseInt(listes.get(i).get(0).split("_")[0]),
		            			                                 listes.get(i).get(0).split("_")[1],
		            			                                 listes.get(i).get(0).split("_")[2],
		            			                                 listes.get(i).get(0).split("_")[3]);
	                  }
	                  catch (ArrayIndexOutOfBoundsException | NumberFormatException nfe){
	                	  string = listes.get(i).get(0);
	                  }
	                  context.put(String.format("alt%d", i), listes.get(i).subList(1, listes.get(i).size() > 9 ? 9 : listes.get(i).size()));
		    	  }
		    	  else {
		    		  context.put(String.format("alt%d", i), listes.get(i));
		    	  }
                  
		    	  String bourrage = "";
		    	  for (int k=listes.get(i).size(); k < 9 ; k++){
		    		  bourrage += "\n";
		    	  }
		    	  
		    	  context.put(String.format("b%d", i), bourrage);
		    	  
		    	  context.put(String.format("s%d", i), string); 
		    	  
		      }

		      // 3) Generate report by merging Java model with the Docx


		      OutputStream out = new FileOutputStream(Paths.get(System.getProperty("user.home")).resolve("étiquettes_disques.odt").toString());

		      report.process(context, out);

//		      OutputStream out2 = new FileOutputStream("/mnt/nfs_public/pour David/MIRROR/modele_etiquette_test_reprise_out.pdf");
//              // 1) Create options ODT 2 PDF to select well converter form the registry
//              Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);
//
//              // 2) Get the converter from the registry
//              IConverter converter = ConverterRegistry.getRegistry().getConverter(options);
//   
//              //report.convert(context, options, out2);

		    } catch (IOException e) {
		      e.printStackTrace();
		    } catch (XDocReportException e) {
		      e.printStackTrace();
		    }
		
		return listes;

	}

}
