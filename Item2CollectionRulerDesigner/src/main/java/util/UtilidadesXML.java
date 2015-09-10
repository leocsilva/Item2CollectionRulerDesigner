package util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.unesp.repositorio.base.xmlschema.item2collectionruler.ObjectFactory;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.University;


public class UtilidadesXML {
	
	static JAXBContext context;
	static Unmarshaller unmarshaller;
	static Marshaller marshaller;
	
	static{
		try {
			context = JAXBContext.newInstance(ObjectFactory.class);
			unmarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (JAXBException e) {
			
		}
		
	}
	
	@SuppressWarnings("restriction")
	public static University carregaXML(String string) throws JAXBException {
		 
		@SuppressWarnings("unchecked")
		JAXBElement<University> element = (JAXBElement<University>) unmarshaller.unmarshal(new File(string));
		University university = element.getValue();
		return university;
	}
	
	public static String toXML(University university) throws JAXBException{
		StringWriter xml = new StringWriter();
		marshaller.marshal(university, xml);
		return xml.toString();
	}
	
	public static void toXML(University university,File file) throws JAXBException{
		StringWriter xml = new StringWriter();
		marshaller.marshal(university, file);
		
	}
	
	
}
