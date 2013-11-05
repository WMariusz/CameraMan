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

public class UploadRequest extends SpiceRequest<String>
{
	private static final String TAG = "UploadRequest";
	private Context context;
	private Bitmap bitmap;
	private Bitmap prevBitmap;
	private int quality;

	public UploadRequest(Bitmap bitmap, int quality, Bitmap prevBitmap, Context context)
	{
		super(String.class);
		this.context = context;
		this.bitmap = bitmap;
		this.quality = quality;
		this.prevBitmap = prevBitmap;
	}

	@Override
	public String loadDataFromNetwork() throws Exception
	{
		// TODO:
		// - wysy³anie na FTP zdjêcia
		// - pobieranie z FTP konfiguracji
		// - zapisywanie w pamiêci
		
		String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		
		save(this.bitmap, fileName, this.quality, this.context);
		
		this.detectMotion(50, 0.5F); // jezeli wykryto ruch, wyslij fote na serwer
		
		return "";
	}
	
	public static void save(Bitmap source, String fileName, int quality, Context context)
	{
        String tmpImg = fileName + ".jpg";
        OutputStream os = null;
        
        try
        {
            File path = new File(Environment.getExternalStorageDirectory(), 
            		context.getResources().getText(R.string.app_name).toString());
            File file = new File(path, tmpImg);
            path.mkdirs();
            
            if(!file.exists())
                file.createNewFile();

            os = new FileOutputStream(file);
            source.compress(CompressFormat.JPEG, quality, os);
            os.flush();
            os.close();
        }
        catch (IOException e)
        {
            Log.d("save", e.getMessage());
        }
    }
	
	private boolean detectMotion(int pixel_threshold, float threshold)
	{      
		boolean motionDetected = false;
		
	    if(this.prevBitmap != null)
	    {
	    	int maxLuminance = 255;
	    	
	    	if(threshold >= 1)
	    		threshold = maxLuminance;
	    	else if(threshold <= 0)
	    		threshold = 0;
	    	else
	    		threshold = threshold * maxLuminance;
	    			
	    	int[] size = { 640, 480 };
	    	int[] pixelsPrev = new int[size[0] * size[1]];
	    	int[] pixels = new int[size[0] * size[1]];
	    	int differentPixels = 0;
	    	
	    	this.prevBitmap = Bitmap.createScaledBitmap(this.prevBitmap, size[0], size[1], false);
	    	Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.bitmap, size[0], size[1], false);
	        
	        this.prevBitmap.getPixels(pixelsPrev, 0, size[0], 0, 0, size[0], size[1]);
	        scaledBitmap.getPixels(pixels, 0, size[0], 0, 0, size[0], size[1]);
	        
	        for(int i = 0; i < pixelsPrev.length; i++)
	        {
	        	if(Math.abs(((pixelsPrev[i] >> 16) & 0xff) - ((pixels[i] >> 16) & 0xff)) >= threshold
	        		|| Math.abs(((pixelsPrev[i] >> 8) & 0xff) - ((pixels[i] >> 8) & 0xff)) >= threshold
	        		|| Math.abs((pixelsPrev[i] & 0xff) - (pixels[i] & 0xff)) >= threshold)
	        		differentPixels++;
	        }
	        
	        if(differentPixels >= pixel_threshold)
	        {
	        	motionDetected = true;
	        	
	        	Log.d("MotionDetection", "Detected! Pixels: " + differentPixels);
	        }
	    }
	    
	    return motionDetected;
	}
}
