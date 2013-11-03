package pl.us.wiinom.cameraman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.*;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

	private SpiceManager spiceManager = new SpiceManager(SimpleService.class);
	private Uri uri;
	private int PICTURE = 0;
	private Bitmap photo;
	private Camera camera;
	private Runnable cameraTask;
	private Handler handler;
	private long delay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.delay = 1000;
		this.camera = Camera.open();
		this.handler = new Handler();
		this.cameraTask = new Runnable() {
			@Override
			public void run()
			{
				runCamera();
				handler.postDelayed(this, delay);
			}
		};
		
		Button startBtn = (Button) findViewById(R.id.button1);
		
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.post(cameraTask);
			}
		});
		
	}
	
	private void runCamera()
	{
		try {
			//set camera parameters
			//camera.setParameters(parameters);
			camera.startPreview();
			camera.takePicture(null, null, mCall);
		} catch (RuntimeException e) {
			Log.d("CAMERA", e.getMessage());
		}
	}
	
	Camera.PictureCallback mCall = new Camera.PictureCallback()
    {

		@Override
		public void onPictureTaken(byte[] image, Camera camera) {
			Bitmap photo = BitmapFactory.decodeByteArray(image, 0, image.length);
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(photo);
			
			UploadRequest request = new UploadRequest(photo, getApplicationContext());
			UploadRequestListener requestListener = new UploadRequestListener();
			
			spiceManager.execute(request, requestListener);
		}
  
      
    };
	
	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		handler.removeCallbacks(cameraTask);
		super.onStop();
	}
	
	class UploadRequestListener implements RequestListener<String> {

		@Override
		public void onRequestFailure(SpiceException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSuccess(String arg0) {
			
		}
		
	}

}
