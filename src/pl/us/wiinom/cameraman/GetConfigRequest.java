package pl.us.wiinom.cameraman;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

public class GetConfigRequest extends SpiceRequest< Config > {

	public GetConfigRequest() {
		super(Config.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Config loadDataFromNetwork() throws Exception {
		
		String url = Config.CONFIG_URL;
		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		
		Log.d("GetConfigRequest", "GET Request: \nURL: "+url);

		HttpGet httpGet = new HttpGet(url);

		httpResponse = httpClient.execute(httpGet);

		String response = IOUtils.toString(httpResponse.getEntity()
				.getContent(), "utf-8");
        
        return new Gson().fromJson(response, this.getResultType());
	}
}
