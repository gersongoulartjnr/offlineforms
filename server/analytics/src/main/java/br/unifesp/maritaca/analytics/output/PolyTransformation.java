package br.unifesp.maritaca.analytics.output;

import com.mongodb.BasicDBObject;

import br.unifesp.maritaca.analytics.json.ATransformation;

public abstract class PolyTransformation {
	
	protected PipelineAggregation pipelineAggregation;
	protected ATransformation transformation;
	protected BasicDBObject nextOperation = null;
	protected String id;
	
	public abstract void createTransformation();

	public PipelineAggregation getPipelineAggregation() {
		return pipelineAggregation;
	}

	public void setPipelineAggregation(PipelineAggregation pipelineAggregation) {
		this.pipelineAggregation = pipelineAggregation;
	}

	public ATransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(ATransformation transformation) {
		this.transformation = transformation;
	}

	public BasicDBObject getNextOperation() {
		return nextOperation;
	}

	public void setNextOperation(BasicDBObject nextOperation) {
		this.nextOperation = nextOperation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}