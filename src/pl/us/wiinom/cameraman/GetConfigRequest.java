package pl.us.wiinom.cameraman;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

public class GetConfigRequest extends SpiceRequest< String > {

	public GetConfigRequest() {
		super(String.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String loadDataFromNetwork() throws Exception {
		
		String url = Config.CONFIG_URL;
		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		
		

		HttpGet httpGet = new HttpGet(url);

		httpResponse = httpClient.execute(httpGet);

		String response = IOUtils.toString(httpResponse.getEntity()
				.getContent(), "utf-8");
		
		Log.d("MyDebugApp", "GET Request: \nURL: "+url+"\nResult:"+response);
        return response;
	}
}
