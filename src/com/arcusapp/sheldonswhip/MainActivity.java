package com.arcusapp.sheldonswhip;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnSeekBarChangeListener, OnCheckedChangeListener {

	MediaPlayer mediaPlayer;
	Button btn;
	TextView txt;
	SeekBar bar;
	RadioGroup radgroup;
	int sensitivity;
	int defaultSens = 4;
	boolean longwhip = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//inicializo reproductor
		mediaPlayer = MediaPlayer.create(this, R.raw.whipshort);
		
		txt =(TextView)findViewById(R.id.textView1);
		
		
		//linkeo los listeners
		btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(this);
		
		radgroup = (RadioGroup)findViewById(R.id.radioGroup1);
		radgroup.setOnCheckedChangeListener(this);
		
		//inicializo accelerometro
		/* do this in onCreate */
	    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    
	    //inicializo la barra
	    bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        bar.setProgress(defaultSens);
        
        sensitivity = defaultSens;
        

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		mediaPlayer.start();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	}

	@Override
    protected void onDestroy() {
		super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
	
	/* put this into your activity class */
	  private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity

	  private final SensorEventListener mSensorListener = new SensorEventListener() {

	    public void onSensorChanged(SensorEvent se) {
	    	try
	    	{
    		  float x = se.values[0];
	  	      float y = se.values[1];
	  	      float z = se.values[2];
	  	      mAccelLast = mAccelCurrent;
	  	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	  	      float delta = mAccelCurrent - mAccelLast;
	  	      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	  	      
	  	      if(mAccel > sensitivity)
	  	    		mediaPlayer.start();
	    	}catch(Exception ex)
	    	{
	    	
	    	}
	      
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    	
	    }

	  };

	  @Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
		  super.onPause();
		  mSensorManager.unregisterListener(mSensorListener);
	  }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(progress == 0)
		{
			if(bar!=null)
				bar.setProgress(1);
			sensitivity = 1;
		}
		else
		{
			sensitivity = progress;
		}
		txt.setText("Sensitivity: "+sensitivity);
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if(checkedId == R.id.radio0)
			mediaPlayer = MediaPlayer.create(this, R.raw.whipshort);
		else
			mediaPlayer = MediaPlayer.create(this, R.raw.whiplong);
	}


}
