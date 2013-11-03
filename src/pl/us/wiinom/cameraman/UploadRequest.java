package pl.us.wiinom.cameraman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.octo.android.robospice.request.SpiceRequest;

public class UploadRequest extends SpiceRequest<String> {

	
	
	private static final String TAG = "UploadRequest";
	private Context context;
	private Bitmap bitmap;

	public UploadRequest(Bitmap bitmap, Context context) {
		super(String.class);
		this.context = context;
		this.bitmap = bitmap;
	}

	@Override
	public String loadDataFromNetwork() throws Exception {
		// TODO:
		// - wysy³anie na FTP zdjêcia
		// - pobieranie z FTP konfiguracji
		// - zapisywanie w pamiêci
		
		String fileName = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		
		save(bitmap, fileName, context);
		
		return "test";
	}
	
	public static void save(Bitmap source, String fileName, Context context) {
        String tmpImg = fileName + ".jpg";
        OutputStream os = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory(), 
            		context.getResources().getText(R.string.app_name).toString());
            File file = new File(path, tmpImg);
            path.mkdirs();
            if(!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            source.compress(CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        }
        catch (IOException e) {
            Log.d("save", e.getMessage());
        }
    }


}
