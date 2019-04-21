package br.unifesp.maritaca.analytics.output;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.components.BooleanManipulation;
import br.unifesp.maritaca.analytics.components.GeneralManipulation;
import br.unifesp.maritaca.analytics.components.TextManipulation;
import br.unifesp.maritaca.analytics.enums.TransformationType;

public class TextComponent extends PolyTransformation {

	@Override
	public void createTransformation() {

		if (transformation.getType().equals(TransformationType.SORT.getDescription())) {
			Integer op = 1;
			if (transformation.getSpecificData().getValues().get(0).compareTo("desc")==0) {
				op = -1;
			} 
			
			nextOperation = GeneralManipulation.getSorting(id, op);
		}
		
		else if (transformation.getType().equals(TransformationType.EQUAL.getDescription())) {
			ArrayList<String> words = new ArrayList<String>();
			words.add(transformation.getSpecificData().getValues().get(0));
			nextOperation = TextManipulation.getIdenticText(id, words);
		}
		
		else if (transformation.getType().equals(TransformationType.NOTEQUAL.getDescription())) {
			ArrayList<Object> words = new ArrayList<Object>();
			words.add((String)transformation.getSpecificData().getValues().get(0));
			nextOperation = BooleanManipulation.getNOT(id, words);
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
		
		if (nextOperation!=null) {
			pipelineAggregation.addOperation(nextOperation);			
		}
	}
}