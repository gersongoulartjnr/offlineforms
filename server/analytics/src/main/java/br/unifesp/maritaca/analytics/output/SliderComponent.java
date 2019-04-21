package br.unifesp.maritaca.analytics.output;

import br.unifesp.maritaca.analytics.components.GeneralManipulation;
import br.unifesp.maritaca.analytics.components.SimpleOperators;
import br.unifesp.maritaca.analytics.enums.TransformationType;

public class SliderComponent extends PolyTransformation {
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
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getEqual(id, decimal);
		}
		
		/* Not Equal != */
		else if (transformation.getType().equals(TransformationType.NOTEQUAL.getDescription())) {
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getNE(id, decimal);			
		}	
		
		/* Greater > */		
		else if (transformation.getType().equals(TransformationType.GREATER.getDescription())) {
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getGT(id, decimal);		
		}
		
		/* Greater or Equal >= */
		else if (transformation.getType().equals(TransformationType.GREATER_EQUAL.getDescription())) {
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getGTE(id, decimal);		
		}
		
		/* Less < */
		else if (transformation.getType().equals(TransformationType.LESS.getDescription())) {
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getLT(id, decimal);		
		}
		
		/* Less or Equal <= */
		else if (transformation.getType().equals(TransformationType.LESS_EQUAL.getDescription())) {
			Double decimal = Double.parseDouble(transformation.getSpecificData().getValues().get(0));
			nextOperation = SimpleOperators.getLTE(id, decimal);		
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