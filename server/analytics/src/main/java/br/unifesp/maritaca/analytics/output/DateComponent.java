package br.unifesp.maritaca.analytics.output;

import java.util.Date;

import br.unifesp.maritaca.analytics.components.GeneralManipulation;
import br.unifesp.maritaca.analytics.components.SimpleOperators;
import br.unifesp.maritaca.analytics.enums.TransformationType;

public class DateComponent extends PolyTransformation{
	
	public String createDate(String cassandraDate) {
		if (cassandraDate==null || cassandraDate.compareTo("")==0) {
			return null;
		}		
		String month = cassandraDate.substring(4, 7);
		String day = cassandraDate.substring(8,10);
		String year = cassandraDate.substring(24);
		String result = month + " " + day + ", " + year;
		return result;
	}
	

	@Override
	public void createTransformation() {
		/* Sort */
		if (transformation.getType().equals(TransformationType.SORT.getDescription())) {
			Integer op = 1;
			if (transformation.getSpecificData().getValues().get(0).compareTo("desc")==0) {
				op = -1;
			} 			
			nextOperation = GeneralManipulation.getSorting(id, op);
		}	
		
		/* Equal == */
		else if (transformation.getType().equals(TransformationType.EQUAL.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getEqual(id, date);
		}
		
		/* Not Equal != */
		else if (transformation.getType().equals(TransformationType.NOTEQUAL.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getNE(id, date);			
		}	
		
		/* Greater > */		
		else if (transformation.getType().equals(TransformationType.GREATER.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getGT(id, date);		
		}
		
		/* Greater or Equal >= */
		else if (transformation.getType().equals(TransformationType.GREATER_EQUAL.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getGTE(id, date);		
		}
		
		/* Less < */
		else if (transformation.getType().equals(TransformationType.LESS.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getLT(id, date);		
		}
		
		/* Less or Equal <= */
		else if (transformation.getType().equals(TransformationType.LESS_EQUAL.getDescription())) {
			String result = createDate(transformation.getSpecificData().getValues().get(0));
			Date date = new Date(result);
			nextOperation = SimpleOperators.getLTE(id, date);		
		}		
		
		/*
		 * Limit and Skip
		 */
		else if (transformation.getType().equals(TransformationType.NFIRSTS.getDescription())) {
			nextOperation = GeneralManipulation.getLimit(Integer.parseInt(transformation.getSpecificData().getValues().get(0)));
		}
		
		else if (transformation.getType().equals(TransformationType.NLASTS.getDescription())) {
			nextOperation = GeneralManipulation.getSkip(Integer.parseInt(transformation.getSpecificData().getValues().get(0)));
		}
		
		/* 
		 * Insert the data whether an operation is created 
		 */
		if (nextOperation != null) {
			pipelineAggregation.addOperation(nextOperation);
		}		
	}
}