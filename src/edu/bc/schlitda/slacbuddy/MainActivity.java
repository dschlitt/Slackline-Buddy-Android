package edu.bc.schlitda.slacbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private MediaPlayer mediaPlayer = null;
	private float sensitivity = .3f;
	private float x, savedX;
	private float y, savedY;
	private float z, savedZ;
	private boolean calibration;
	private SoundThread myChild = null;
	private Drawable calibratedImage;
	private Drawable uncalibratedImage;
	private View background;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// creating accelerometer monitor
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		uncalibratedImage = getResources().getDrawable(R.drawable.unselected);
		calibratedImage = getResources().getDrawable(R.drawable.selected);
		background = findViewById(R.id.main_layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		// restore calibration data if it exists

	}

	public void onPause() { // something in here is bad.
		super.onPause();
		// if (mediaPlayer != null) {
		// mediaPlayer.release(); //could this cause a race condition that leads
		// to crashing?
		// mediaPlayer = null;
		// }
		sensorManager.unregisterListener(this); // Very important, avoids
												// battery drain and useless cpu
												// consumption
		// maybe I should de-calibrate onPause?
		// save calibration data
		if (myChild != null)
			myChild.flagForDeath();
		calibration = false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("MenuItem", item.toString());
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.howTo:
			Intent intent = new Intent(this, HowToLandingPage.class);
			startActivity(intent);
			break;
		case R.id.videoLink:
			Intent vidIntent = new Intent(Intent.ACTION_VIEW);
			vidIntent.setData(Uri.parse("http://youtu.be/K7GrfqPKTYg"));
			startActivity(vidIntent);
			
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float G = SensorManager.GRAVITY_EARTH;
		x = event.values[0] / G; // in G's, not m/s^2
		y = event.values[1] / G;
		z = event.values[2] / G;
	}

	public void calibrate(View e) {
		if (!calibration) {
			setSavedX(x);
			setSavedY(y);
			setSavedZ(z);
			System.out.println("Calibrated");
			// add toast here? Place phone in pocket in same position
			// (Monitoring begins in 5 seconds)
			// sleep atleast 5 seconds or something to give use time to put it
			// in their pocket.
			// change button text to indicate calibration
			new SoundThread(this).start();
			calibration = true;
			background.setBackground(calibratedImage);
		} else {
			if (myChild != null)
				myChild.flagForDeath();
			calibration = false;

			background.setBackground(uncalibratedImage);

		}

	}

	// public void updateSensitivity(View e){
	// String sensitivityString =
	// ((EditText)findViewById(R.id.sensitivityField)).getText().toString();
	// System.out.println(sensitivityString);
	// if (sensitivityString.length() != 0){
	// float reading = Float.parseFloat(sensitivityString);
	// if (reading > 0 && reading < 1){
	// setSensitivity(reading);
	// }
	// }
	// }

	public float getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mplyr) {
		this.mediaPlayer = mplyr;
	}

	public float getSavedX() {
		return savedX;
	}

	public void setSavedX(float savedX) {
		this.savedX = savedX;
	}

	public float getSavedY() {
		return savedY;
	}

	public void setSavedY(float savedY) {
		this.savedY = savedY;
	}

	public float getSavedZ() {
		return savedZ;
	}

	public void setSavedZ(float savedZ) {
		this.savedZ = savedZ;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return savedY;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public boolean isCalibrated() {
		return calibration;
	}

}
