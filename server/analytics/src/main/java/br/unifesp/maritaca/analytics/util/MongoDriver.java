package br.unifesp.maritaca.analytics.util;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoDriver {

	private static Logger logger = Logger.getLogger(MongoDriver.class);	
	
	private MongoClient client;
	private DB database;
	private DBCollection collection;

	public MongoDriver(String host, String port, String timeout, String dbName, String collectionName) {
		
		try {
			MongoClientOptions m = new MongoClientOptions.Builder().socketKeepAlive(true).socketTimeout(Integer.parseInt(timeout)).build();
			this.client = new MongoClient(new ServerAddress(host, Integer.parseInt(port)), m);		
			this.database = client.getDB(dbName);
			this.collection = database.getCollection(collectionName);
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
	}

	public MongoClient getClient() {
		return client;
	}
	public void setClient(MongoClient client) {
		this.client = client;
	}
	public DB getDatabase() {
		return database;
	}
	public void setDatabase(DB database) {
		this.database = database;
	}
	public DBCollection getCollection() {
		return collection;
	}
	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
}