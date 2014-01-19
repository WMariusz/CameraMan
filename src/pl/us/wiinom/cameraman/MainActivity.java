package pl.us.wiinom.cameraman;

import java.io.IOException;
import java.lang.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private final static String TAG = "MainActivity";

	private SpiceManager spiceManager;
	
	private Camera camera;
	private Runnable cameraTask;
	private Handler handler;
	private Bitmap prevPhoto;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	
	public static Object monitor = new Object();
	public static Object monitor2 = new Object();
	
	private static boolean flag = false;// czy petla dzia³a
	private Config config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		spiceManager = new SpiceManager(SimpleService.class);
		config = new Config();
		synchronized (monitor) {
			ConfigUpdater.getConfigFromFile();
		}
		new Thread(new ConfigUpdater()).start();
		// camera.startPreview();
		this.handler = new Handler();
		this.cameraTask = new Runnable() {
			@Override
			public void run() {
				flag = true;
				runCamera();
				long inter;
				synchronized (monitor2) {
					inter = config.interval;
				}
				handler.postDelayed(this, inter);
			}
		};

		Button startBtn = (Button) findViewById(R.id.button1);
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					handler.removeCallbacks(cameraTask);
					flag = false;
				} else
					handler.post(cameraTask);
			}
		});

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GetConfigRequest request = new GetConfigRequest();
		spiceManager.execute(request, Config.CACHE_KEY, config.interval, new GetConfigListener());
		
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(int id) {
		Camera c = null;
		try {
			c = Camera.open(id); // attempt to get a Camera instance
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return c; // returns null if camera is unavailable
	}

	private void runCamera() {
		try {
			camera.takePicture(null, null, cameraCallback);
			camera.startPreview();
		} catch (RuntimeException e) {
			Log.e("CAMERA", e.getMessage());
		}
	}

	Camera.PictureCallback cameraCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] image, Camera camera) {
			Bitmap photo = BitmapFactory
					.decodeByteArray(image, 0, image.length);

			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(photo);

			UploadRequest request = new UploadRequest(photo, prevPhoto,
					getApplicationContext(), config);
			UploadRequestListener requestListener = new UploadRequestListener();

			spiceManager.execute(request, requestListener);
			
			prevPhoto = Bitmap.createScaledBitmap(photo, photo.getWidth(),
					photo.getHeight(), false);
		}
	};
	private boolean mPreviewRunning;

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		handler.removeCallbacks(cameraTask);
		flag = false;
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// camera.release();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if (mPreviewRunning)
			camera.stopPreview();

		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}

		camera.startPreview();
		mPreviewRunning = true;

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = getCameraInstance(0);
		camera.setDisplayOrientation(90);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		mPreviewRunning = false;
		camera.release();
	}

	class UploadRequestListener implements RequestListener<Integer> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.e(TAG, arg0.getMessage());
			Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Integer result) {
			switch(result) {
			case 0:
				GetConfigRequest request = new GetConfigRequest();
				spiceManager.execute(request, Config.CACHE_KEY, config.interval, new GetConfigListener());
				Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				//zdjecie nie mia³o ró¿nic
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
	
	class GetConfigListener implements RequestListener<Config> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.e(TAG, arg0.getMessage());
			Toast.makeText(getApplicationContext(), "GetConfig Failed", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Config result) {
			config = result;
			Toast.makeText(getApplicationContext(), "GetConfig Success", Toast.LENGTH_SHORT).show();
		}
	}
}
