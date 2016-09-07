package com.example.casopratico2;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Gravador extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private MediaRecorder gravador;
	private TextView proximidade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gravador);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		proximidade = (TextView) findViewById(R.id.proximidade);
		
		// obter sensormanager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		gravador = new MediaRecorder();
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values[0] == 0) {
			// se aconteceu uma alteracao no sensor,
			// altera o layout e comeca a gravar
			setContentView(R.layout.agravar);
			Button botaofechar = (Button) findViewById(R.id.botaofechar);

			botaofechar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			startRecording();
		} 
		else {
			// se o sensor nao dispara vai para a activity reproduzir e para de
			// gravar
			stopRecording();
			Intent intent = new Intent(this, Reprodutor.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
				Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onStop() {
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(
				Sensor.TYPE_PROXIMITY));
		super.onStop();
	}
	
	private void startRecording() {
		gravador = new MediaRecorder();
		gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
		gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		gravador.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
		gravador.setOutputFile(filename);
		try {
			gravador.prepare();
			gravador.start();
		} catch (Exception e) {
			Log.d("start recording", e.toString());
			Toast.makeText(getBaseContext(), "Ocorreu um erro na gravacao.", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void stopRecording() {
		gravador.stop();
		gravador.reset();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}