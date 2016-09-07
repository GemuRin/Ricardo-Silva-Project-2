package com.example.casopratico2;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Reprodutor extends Activity implements SensorEventListener {
	private ShakeDetector mShaker;
	private SensorManager mSensorManager;
	private MediaPlayer reprodutor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reproduzir);

		mShaker.setOnShakeListener(new ShakeDetector.OnShakeListener() {
			public void onShake() {
				reproduzir();
			}
		});

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		Button botaoacabar = (Button) findViewById(R.id.buttonacabar);
		Button botaoreproduzir = (Button) findViewById(R.id.botaoreproduzir);

		botaoreproduzir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reproduzir();
			}
		});
		botaoacabar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				parar();
			}
		});

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResume() {
		super.onResume();
		mShaker.resume();
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onStop() {
		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		super.onStop();
	}

	protected void reproduzir() {
		reprodutor = new MediaPlayer();
		if (reprodutor == null) {
			Toast.makeText(Reprodutor.this, "Erro ao carregar o MediaPlayer",
					Toast.LENGTH_LONG).show();
		}
		try {
			String filename = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/test.3gp";
			reprodutor.setDataSource(filename);
			reprodutor.prepare();
			reprodutor.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void parar() {
		reprodutor.stop();
		reprodutor.reset();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mShaker.pause();
		// super.onPause();

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