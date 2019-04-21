package br.unifesp.maritaca.analytics.input;

import java.sql.Date;
import br.unifesp.maritaca.analytics.util.ConstantsPipeline;
import br.unifesp.maritaca.analytics.util.MongoDriver;

import com.mongodb.BasicDBObject;

public class AnswerJSON {

	private MongoDriver driver;
	private final BasicDBObject document;

	public AnswerJSON(
			MongoDriver driver, 
			String key,
			String formKey,
			String author, 
			Long creationDate, 
			String url, 
			String userKey) {
		this.driver = driver;
		document = new BasicDBObject();
		document.put(ConstantsPipeline._ID, key);
		document.put(ConstantsPipeline.FORM_KEY, formKey);
		document.put(ConstantsPipeline.AUTHOR, author);
		document.put(ConstantsPipeline.CREATION_DATE, new Date(creationDate));
		document.put(ConstantsPipeline.URL, url);
	}

	public MongoDriver getDriver() {
		return driver;
	}

	public void setDriver(MongoDriver driver) {
		this.driver = driver;
	}

	public BasicDBObject getDocument() {
		return document;
	}

	public void addAnswer(NextAnswers answer) {
		document.put(answer.getId(), answer.getValue());
	}

	public void saveInDB() {
		driver.getCollection().save(document);
	}	
}