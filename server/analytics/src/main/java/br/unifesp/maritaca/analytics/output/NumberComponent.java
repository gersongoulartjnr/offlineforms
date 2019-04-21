package br.unifesp.maritaca.analytics.output;

import br.unifesp.maritaca.analytics.components.GeneralManipulation;
import br.unifesp.maritaca.analytics.components.MathManipulation;
import br.unifesp.maritaca.analytics.components.SimpleOperators;
import br.unifesp.maritaca.analytics.enums.TransformationType;

public class NumberComponent extends PolyTransformation {
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
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getEqual(id, myNumber);
		}
		
		/* Not Equal != */
		else if (transformation.getType().equals(TransformationType.NOTEQUAL.getDescription())) {
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getNE(id, myNumber);			
		}	
		
		/* Greater > */		
		else if (transformation.getType().equals(TransformationType.GREATER.getDescription())) {
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getGT(id, myNumber);		
		}
		
		/* Greater or Equal >= */
		else if (transformation.getType().equals(TransformationType.GREATER_EQUAL.getDescription())) {
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getGTE(id, myNumber);		
		}
		
		/* Less < */
		else if (transformation.getType().equals(TransformationType.LESS.getDescription())) {
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getLT(id, myNumber);		
		}
		
		/* Less or Equal <= */
		else if (transformation.getType().equals(TransformationType.LESS_EQUAL.getDescription())) {
			Integer myNumber = Integer.parseInt(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getLTE(id, myNumber);		
		}
		
		/* Sum */
		else if(transformation.getType().equals(TransformationType.SUM.getDescription())){
			nextOperation = MathManipulation.getSummation(id);
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
		
		if (nextOperation != null) {
			pipelineAggregation.addOperation(nextOperation);
		}
	}
}