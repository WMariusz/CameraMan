package pl.us.wiinom.cameraman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

	private SpiceManager spiceManager = new SpiceManager(SpiceService.class);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button startBtn = (Button) findViewById(R.id.button1);
		
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//SpiceRequest request = new SpiceRequest();
				//UploadRequestListener requestListener = new UploadRequestListener();
				
				//spiceManager.execute(request, requestListener);
			}
			
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}
	
	class UploadRequestListener implements RequestListener<String> {

		@Override
		public void onRequestFailure(SpiceException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSuccess(String arg0) {
			TextView tv = (TextView) findViewById(R.id.hello);
			
			tv.setText(arg0);
		}
		
	}

}
