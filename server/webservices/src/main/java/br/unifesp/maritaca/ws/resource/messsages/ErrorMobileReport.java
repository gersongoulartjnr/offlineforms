package br.unifesp.maritaca.ws.resource.messsages;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="errorreport")
public class ErrorMobileReport {
	
	private String userName;
	
	private List<ErrorMobile> errors;
	
	@XmlElement
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElementWrapper(name="errors")
	@XmlElement(name="error")
	public List<ErrorMobile> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorMobile> errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString(){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ErrorMobileReport.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter(); 
			marshaller.marshal(this, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}				
	}
}