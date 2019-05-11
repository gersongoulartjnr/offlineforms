package br.unifesp.offlineforms.mobile.model.components.form;

import org.simpleframework.xml.Root;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;

@Root(name = "location")
public class GeoLocationQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private TextView txtLatitude;
	private TextView txtLongitude;

	private Location location;
	private Double latitude;
	private Double longitude;
	private boolean isNetworkEnabled;
	private boolean isGpsEnabled;
	private static final long MIN_DISTANCE = 1;
	private static final long MIN_TIME = 60000;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.GEOLOCATION;
	}

	private void checkProviders(final ControllerActivity activity) {
		activity.setLocationManager((LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE));
		isNetworkEnabled = activity.getLocationManager().isProviderEnabled(
				LocationManager.NETWORK_PROVIDER);
		isGpsEnabled = activity.getLocationManager().isProviderEnabled(
				LocationManager.GPS_PROVIDER);
	}

	private void getInfo(ControllerActivity activity) {
		activity.setLocationListener(new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {}
			@Override
			public void onProviderDisabled(String provider) {}
			@Override
			public void onProviderEnabled(String provider) {	}
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		});
		activity.getLocationManager().requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
				activity.getLocationListener());
		if (isNetworkEnabled) {
			location = activity.getLocationManager().getLastKnownLocation(
					LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else if (isGpsEnabled) {
			location = activity.getLocationManager().getLastKnownLocation(
					LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		}
	}

	@Override
	public Integer getNext() {
		if (getComparisons() == null || getComparisons().size() < 1)
			return next;

		for (Comparison comp : getComparisons()) {
			String value = this.getValue();
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public String getValue() {
		return value != null ? value.toString() : "";
	}

	private void fillData() {
		String val = getValue();
		String[] tokens = val.split(";");
		if (tokens.length == 2) {
			latitude = Double.parseDouble(tokens[0]);
			longitude = Double.parseDouble(tokens[1]);
		}
	}

	@Override
	public View getLayout(final ControllerActivity activity) {
		this.checkProviders(activity);
		if (isNetworkEnabled && isGpsEnabled) {
			this.fillData();
			RelativeLayout layout = new RelativeLayout(activity);
			TableLayout tableLayout = new TableLayout(activity);

			//Margin params:
					TableLayout.LayoutParams params = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(20, 5, 0, 0);
				TableRow firstRow = new TableRow(activity);
				TextView lblLatitude = new TextView(activity);
				lblLatitude.setText(R.string.label_latitude);
				txtLatitude = new TextView(activity);
				txtLatitude.setText(latitude!= null?String.valueOf(latitude):"");
			
				TableRow secondRow = new TableRow(activity);
				TextView lblLongitude = new TextView(activity);
				lblLongitude.setText(R.string.label_longitude);
				txtLongitude = new TextView(activity);
				txtLongitude.setText(longitude!=null?String.valueOf(longitude):"");
				
				TableRow thirdRow = new TableRow(activity);
				Button btnGps = new Button(activity);
				btnGps.setText(R.string.label_get_location);
				btnGps.setOnClickListener(new View.OnClickListener() {				
					@Override
					public void onClick(View v) {
						getInfo(activity);
						txtLatitude.setText(""+getLatitude(activity));
						txtLongitude.setText(""+getLongitude(activity));
					}
				});
			
			firstRow.addView(lblLatitude);
			firstRow.addView(txtLatitude);
			secondRow.addView(lblLongitude);
			secondRow.addView(txtLongitude);
			thirdRow.addView(btnGps);
			tableLayout.addView(firstRow, params);
			tableLayout.addView(secondRow, params);
			tableLayout.addView(thirdRow, params);
			layout.addView(tableLayout);
			return layout;
		} else {
			TextView textView = new TextView(activity);
			textView.setText(R.string.location_disabled);
			textView.setPadding(20, 0, 0, 0);
			return textView;
		}
	}

	@Override
	public boolean validate() {
		if(required) {
			if (!"".equals(getValue()))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		if (txtLatitude != null && txtLatitude.getText() != null
				&& txtLongitude != null && txtLongitude.getText() != null) {
			value = txtLatitude.getText() + ";" + txtLongitude.getText();
		}
	}

	public double getLatitude(final ControllerActivity activity) {
		if (location != null)
			latitude = location.getLatitude();
		return latitude;
	}

	public double getLongitude(final ControllerActivity activity) {
		if (location != null)
			longitude = location.getLongitude();
		return longitude;
	}
}