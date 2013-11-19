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
	private Camera camera;
	private Runnable cameraTask;
	private Handler handler;
	private Bitmap prevPhoto;
	public static volatile long interval = 1000;
	public static volatile int quality;
	public static volatile FtpParams ftpParams;
	public static Object monitor = new Object();
	public static Object monitor2 = new Object();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		synchronized(monitor)
		{
			ConfigUpdater.getConfigFromFile();
		}
		new Thread(new ConfigUpdater()).start();

		this.camera = Camera.open();
		this.handler = new Handler();
		this.cameraTask = new Runnable() {
			@Override
			public void run() {
				runCamera();
				long inter;
				synchronized(monitor2)
				{
					inter = interval;
				}
				handler.postDelayed(this, inter);
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

	private void runCamera() {
		try {
			// camera.setParameters(parameters);
			camera.startPreview();
			camera.takePicture(null, null, cameraCallback);
		} catch (RuntimeException e) {
			Log.d("CAMERA", e.getMessage());
		}
	}

	Camera.PictureCallback cameraCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] image, Camera camera) {
			Bitmap photo = BitmapFactory
					.decodeByteArray(image, 0, image.length);
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(photo);

			UploadRequest request = new UploadRequest(photo, quality, prevPhoto, ftpParams,
					getApplicationContext());
			UploadRequestListener requestListener = new UploadRequestListener();

			spiceManager.execute(request, requestListener);

			prevPhoto = Bitmap.createScaledBitmap(photo, photo.getWidth(),
					photo.getHeight(), false);
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
	
	public class FtpParams
	{
		public String server;
		public int port;
		public String user;
		public String password;
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
