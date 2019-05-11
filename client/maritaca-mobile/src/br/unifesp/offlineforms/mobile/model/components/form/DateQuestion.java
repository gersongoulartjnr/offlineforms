package br.unifesp.offlineforms.mobile.model.components.form;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.view.View;
import android.widget.DatePicker;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.util.Constants;
import br.unifesp.offlineforms.mobile.util.Utils;

@Root(name = "date")
public class DateQuestion extends Question {

	private static final long serialVersionUID = 1L;

	@Element(name = "format", required = true)
	private String format;

	@Attribute(name = "min", required = false)
	private String min;

	@Attribute(name = "max", required = false)
	private String max;

	private Date maxDate;
	private Date minDate;

	private DatePicker field;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.DATE;
	}

	@Override
	public Integer getNext() {
		if (getComparisons() == null || getComparisons().size() < 1)
			return next;

		for (Comparison comp : getComparisons()) {
			Date value = this.getValue();
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public Date getValue() {
		return value != null ? (Date) value : new Date();
	}

	public View getLayout(ControllerActivity activity) {
		setFormatDate(format);
		setMinDate();
		setMaxDate();

		field = new DatePicker(activity);
		Date defaultDate = Utils.convertStringToDate(_default, format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(getValue());
		field.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
		field.setPadding(20, 0, 20, 0);
		return field;
	}

	@Override
	public boolean validate() {
		Date val = getValue();
		boolean flag = false;
		if (val != null) {
			if (minDate == null && maxDate == null) {
				flag = true;
			}
			if (maxDate != null && minDate != null) {
				flag = valueIsBefore(val, maxDate)
						&& valueIsAfter(val, minDate);
			} else if (maxDate != null) {
				flag = valueIsBefore(val, maxDate);
			} else if (minDate != null) {
				flag = valueIsAfter(val, minDate);
			}
		}
		if (!required)
			flag = true;
		return flag;
	}

	private boolean valueIsBefore(Date val, Date maxValue) {
		Calendar calValue = new GregorianCalendar();
		calValue.setTime(val);
		calValue.set(Calendar.MILLISECOND, 0);
		calValue.set(Calendar.SECOND, 0);
		calValue.set(Calendar.MINUTE, 0);
		calValue.set(Calendar.HOUR_OF_DAY, 0);
		Calendar calMaxValue = new GregorianCalendar();
		calMaxValue.setTime(maxValue);
		calMaxValue.set(Calendar.MILLISECOND, 0);
		calMaxValue.set(Calendar.SECOND, 0);
		calMaxValue.set(Calendar.MINUTE, 0);
		calMaxValue.set(Calendar.HOUR_OF_DAY, 0);

		if (calValue.before(calMaxValue))
			return true;
		return false;
	}

	private boolean valueIsAfter(Date val, Date minValue) {
		Calendar calValue = new GregorianCalendar();
		calValue.setTime(val);
		calValue.set(Calendar.MILLISECOND, 0);
		calValue.set(Calendar.SECOND, 0);
		calValue.set(Calendar.MINUTE, 0);
		calValue.set(Calendar.HOUR_OF_DAY, 0);
		Calendar calMinValue = new GregorianCalendar();
		calMinValue.setTime(minValue);
		calMinValue.set(Calendar.MILLISECOND, 0);
		calMinValue.set(Calendar.SECOND, 0);
		calMinValue.set(Calendar.MINUTE, 0);
		calMinValue.set(Calendar.HOUR_OF_DAY, 0);

		if (calValue.after(calMinValue))
			return true;
		return false;
	}

	@Override
	public void save(View answer) {
		String date = field.getYear() + "-" + (field.getMonth() + 1) + "-"
				+ field.getDayOfMonth();
		value = Utils.convertStringToDate(date,
				Constants.DATE_ISO8601FORMAT);
	}

	public void setFormatDate(String format) {
		this.format = Utils.getDateFormat(format);
	}

	public void setMaxDate() {
		this.maxDate = Utils.convertStringToDate(max, format);
	}

	public void setMinDate() {
		this.minDate = Utils.convertStringToDate(min, format);
	}
}