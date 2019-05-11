package br.unifesp.offlineforms.mobile.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import android.view.View;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.model.comparators.Equal;
import br.unifesp.offlineforms.mobile.model.comparators.Greater;
import br.unifesp.offlineforms.mobile.model.comparators.GreaterEqual;
import br.unifesp.offlineforms.mobile.model.comparators.Less;
import br.unifesp.offlineforms.mobile.model.comparators.LessEqual;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;

/**
 * 
 * @author Bruno G. Santos
 * @version 1.0.1
 * 
 */
@Root(name = "question")
public abstract class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(required = true)
	protected Integer id;

	@Attribute(required = false)
	protected Integer next;

	@Attribute(required = false)
	protected Integer previous;

	@Element(required = false)
	protected String help;

	@Element(name = "default", required = false)
	protected String _default;

	@Attribute(required = false)
	protected Boolean required;

	@Element
	protected String label;

	protected Object value;

	@ElementListUnion({
			@ElementList(entry = "equal", inline = true, type = Equal.class, required = false),
			@ElementList(entry = "less", inline = true, type = Less.class, required = false),
			@ElementList(entry = "greater", inline = true, type = Greater.class, required = false),
			@ElementList(entry = "lessequal", inline = true, type = LessEqual.class, required = false),
			@ElementList(entry = "greaterequal", inline = true, type = GreaterEqual.class, required = false) })
	protected List<Comparison> comparisons;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}

	public Integer getPrevious() {
		return previous;
	}

	public void setPrevious(Integer previous) {
		this.previous = previous;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getDefault() {
		return _default;
	}

	public void setDefault(String _default) {
		this._default = _default;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/* Abstract methods */
	public abstract ComponentType getComponentType();

	public abstract Object getValue();

	public abstract View getLayout(ControllerActivity activity);

	public abstract boolean validate();

	public abstract void save(View answer);

	public List<Comparison> getComparisons() {
		return comparisons;
	}

	public void setComparisons(List<Comparison> comparisons) {
		this.comparisons = comparisons;
	}
}