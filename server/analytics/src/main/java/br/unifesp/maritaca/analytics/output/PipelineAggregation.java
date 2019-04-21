package br.unifesp.maritaca.analytics.output;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;
import br.unifesp.maritaca.analytics.util.MongoDriver;

import com.mongodb.BasicDBObject;

public class PipelineAggregation {

	private MongoDriver driver;
	private ArrayList<BasicDBObject> pipeline;
	private BasicDBObject cmdBody;

	public PipelineAggregation(MongoDriver driver) {
		this.driver = driver;
		this.pipeline = new ArrayList<BasicDBObject>();
		this.cmdBody = new BasicDBObject(ConstantsPipeline.AGGREGATE,
				this.driver.getCollection().getName());
	}

	public MongoDriver getDriver() {
		return driver;
	}
	public void setDriver(MongoDriver driver) {
		this.driver = driver;
	}
	public ArrayList<BasicDBObject> getPipeline() {
		return pipeline;
	}
	public void setPipeline(ArrayList<BasicDBObject> pipeline) {
		this.pipeline = pipeline;
	}
	public BasicDBObject getCmdBody() {
		return cmdBody;
	}
	public void setCmdBody(BasicDBObject cmdBody) {
		this.cmdBody = cmdBody;
	}

	public void addOperation(BasicDBObject dbObject) {
		if (!(dbObject == null))
			pipeline.add(dbObject);
	}

	public BasicDBObject executeOperations() {
		// No operations in pipeline model
		if (pipeline.size() <= 0) {
			return null;
		} else {
			cmdBody.put("pipeline", pipeline);
			BasicDBObject x = (BasicDBObject) driver.getDatabase().command(cmdBody);			
			x.remove("serverUsed");
			return x;
		}
	}
}