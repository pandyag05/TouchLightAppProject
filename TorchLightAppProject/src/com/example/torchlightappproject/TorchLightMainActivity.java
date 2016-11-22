package com.example.torchlightappproject;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;
import android.content.res.Resources;


public class TorchLightMainActivity extends Activity {
	//variable for Image Button and textview
	private ImageButton btnOnImage;
	private TextView descriptionText;
	//variables for camera to start the light
	private Camera mCamera;
	private Camera.Parameters mParameters;
	// if it is true, then turn light on.
	private boolean isLightOn;
	private boolean makeCameraOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch_light_main);
		//restore two components from the layout.
		btnOnImage = (ImageButton) findViewById(R.id.on_light_btn);
		descriptionText = (TextView)findViewById(R.id.descTextView);
		
		//make sure that camera is working properly.
		makeCameraOn = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		
		// If error occuredd inside camera, it stops.
		if(!makeCameraOn){
			AlertDialog errorTorchBuilder = new AlertDialog.Builder(TorchLightMainActivity.this).create();
			errorTorchBuilder.setTitle("Error");
			errorTorchBuilder.setMessage("Your device is not supported by this app");
			errorTorchBuilder.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			errorTorchBuilder.show();
			return;	
		}
		getCamera();
		changeButtonImage();
		//set button for light
		btnOnImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//if Light is on, turn Light off otherwise turn Light on
				if(isLightOn){
					turnOffTorch();
					//String turnOff = getResources().getString(R.string.turn_off);
					//descriptionText.setText(turnOff);
				}else{
					turnOnTorch();
					//String turnOn = getResources().getString(R.string.turn_on);
					//descriptionText.setText(turnOn);
				}
			}
		});
		
		
	}
	private void getCamera(){
	 	if(mCamera == null){
	 		//set the camera features to open camera. 
	 		try{
	 			mCamera = mCamera.open();
	 			mParameters = mCamera.getParameters();
	 		}catch(RuntimeException rte){
	 			Toast makeToast = Toast.makeText(getApplicationContext(), "Sorry, there was an error", Toast.LENGTH_SHORT);
	 			makeToast.show();
	 		}
	 	}
	}
	//change the image into button 
	private void changeButtonImage(){
	
		if(isLightOn){
			//set button_ON image from drawable.
			btnOnImage.setBackgroundResource(R.drawable.ic_button_on);
		}else{
			//set button_OFF image from drawable.
			btnOnImage.setBackgroundResource(R.drawable.ic_button_off);
		}
	}
	private void turnOnTorch(){
		if(!isLightOn){
			//if camera is not enable to function, then return to error.
			if(mCamera == null || mParameters == null){
				return;
			}
			
			//set the camera parameters to turn light on.
			mParameters = mCamera.getParameters();
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(mParameters);
			mCamera.startPreview();
			//turn light on is true
			isLightOn = true;
			changeButtonImage();
			//String turnOn = getResources().getString(R.string.turn_on);
			//descriptionText.setText(turnOn);
			
			// set the textview to show on screen
			String turnOff = getResources().getString(R.string.turn_off);
			descriptionText.setText(turnOff);
		}
	}
	private void turnOffTorch(){
		if(isLightOn){
			//if camera is not enable to function, then return to error.
			if(mCamera == null || mParameters == null){
				return;
			}
			
			//set the camera parameters to turn light off.
			mParameters = mCamera.getParameters();
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(mParameters);
			mCamera.stopPreview();
			
			//turn light on is not true, then turn light off.
			isLightOn = false;
			changeButtonImage();
			//String turnOff = getResources().getString(R.string.turn_off);
			//descriptionText.setText(turnOff);
			
			// set the textview to show on screen
			String turnOn = getResources().getString(R.string.turn_on);
			descriptionText.setText(turnOn);
		}
	}

	
}
