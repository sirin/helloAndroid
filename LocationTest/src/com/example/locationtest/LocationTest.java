package com.example.locationtest;

import java.util.List;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class LocationTest extends Activity implements LocationListener {
	private LocationManager mgr;
	private TextView output;
	private String best;
	private static final String[] A = {"invalid", "n/a", "fine", "coarse"};
	private static final String[] P = {"invalid", "n/a", "low", "medium", "high"};
	private static final String[] S = {"out of service", "temporarily unavailable", "available"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_test);
		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		output = (TextView) findViewById(R.id.output);
		log("Location provides:");
		dumpProviders();
		
		Criteria criteria = new Criteria();
		best = mgr.getBestProvider(criteria, true);
		log("\nBest provider is: "+best);
		log("\nLocations (starting wit last known):");
		Location location = mgr.getLastKnownLocation(best);
		dumpLocation(location);
	}

	protected void onResume() {
		super.onResume();
		mgr.requestLocationUpdates(best, 15000, 1, this);
	}
	
	protected void onPause() {
		super.onPause();
		mgr.removeUpdates(this);
	}
	
	public void onLocationChanged(Location location) {
		dumpLocation(location);
	}
	
	public void onProviderDisabled(String provider) {
		log("\nProvider disabled: "+provider);
	}
	
	public void onProviderEnabled(String provider) {
		log("\nProvider enabled: "+provider);
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		log("\nProvider status changed: "+provider+ ", status="+S[status]+ ", extras="+extras);
	}
	
	private void log(String string) {
		output.append(string + "\n");
	}
	
	private void dumpProviders() {
		List<String> providers = mgr.getAllProviders();
		for (String provider : providers) {
			dumpProvider(provider);
		}
	}
	
	private void dumpProvider(String provider) {
		LocationProvider info = mgr.getProvider(provider);
		StringBuilder builder = new StringBuilder();
		builder.append("LocationProvider[")
			.append("name=")
			.append(info.getName())
			.append(",enabled=")
			.append(mgr.isProviderEnabled(provider))
			.append(",getAccuracy=")
			.append(A[info.getAccuracy()+1])
			.append(",getPowerRequirement=")
			.append(P[info.getPowerRequirement()+1])
			.append(",hasMonetaryCost=")
			.append(info.hasMonetaryCost())
			.append(",requiresCell=")
			.append(info.requiresCell())
			.append(",requiresNetwork=")
			.append(info.requiresNetwork())
			.append(",requiresSatellite=")
			.append(info.requiresSatellite())
			.append(",supportsAltitude=")
			.append(info.supportsAltitude())
			.append(",supportsBearing=")
			.append(info.supportsBearing())
			.append(",supportsSpeed=")
			.append(info.supportsSpeed())
			.append("]");
		log(builder.toString());
	}
	
	private void dumpLocation(Location location) {
		if (location == null)
			log("\nLocation[unknown]");
		else
			log("\n"+ location.toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_test, menu);
		return true;
	}

}
