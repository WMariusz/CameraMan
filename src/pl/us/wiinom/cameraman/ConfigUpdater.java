package pl.us.wiinom.cameraman;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ConfigUpdater implements Runnable {

	private static Object fileMonitor;
	private static Context context;
	private Handler handler;
	private long configInterval;
	private SpiceManager spiceManager;

	public ConfigUpdater(Context context, SpiceManager spiceManager) {
		ConfigUpdater.context = context;
		this.handler = new Handler();
		ConfigUpdater.fileMonitor = new Object();
		this.spiceManager = spiceManager;
	}

	@Override
	public void run() {
		getConfigFromFile(); // 1.

		// new Gson().fromJson(response, Config.class);

		/*
		 * 2. Pobieranie pliku konfiguracyjnego z ftp i nadpisanie w pamieci
		 * telefonu
		 */
		synchronized (MainActivity.monitor3) {
			this.configInterval = Config.config_interval;
		}
		
		spiceManager.execute(new GetConfigRequest(), new GetConfigRequestListener());
		// 3. Powtarzanie 1. 2. w petli z opoznieniem zgodnym z parametrem
		// pobranym z configa
		handler.postDelayed(this, this.configInterval);
	}

	public static synchronized void getConfigFromFile() {
		synchronized (ConfigUpdater.fileMonitor) {
			try {
				BufferedReader rd = new BufferedReader(new FileReader(new File(Environment
						.getExternalStorageDirectory()
						+ "/"
						+ ConfigUpdater.context
								.getText(R.string.app_name),
						"config.json")));
				String file = org.apache.commons.io.IOUtils.toString(rd);
				ConfigFromJson responseConfig = new Gson().fromJson(file, ConfigFromJson.class);

				synchronized (MainActivity.monitor) {
					// 1. a) Odczytanie pliku konfiguracyjnego i ustawienie
					// zmiennych
					// klasy Config (!!! bez Config.interval !!!)
					Config.quality = responseConfig.quality;
					Config.pixel_threshold = responseConfig.pixel_threshold;
					Config.threshold = responseConfig.threshold;
				}

				synchronized (MainActivity.monitor2) {
					// 1. b) Odczytanie pliku konfiguracyjnego i ustawienie
					// intervalu
					// aparatu (Config.interval)
					Config.interval = responseConfig.interval;
				}

				synchronized (MainActivity.monitor3) {
					Config.config_interval = responseConfig.config_interval;
				}
			} catch (Exception e) {
			}
		}
	}
	
	private class GetConfigRequestListener implements RequestListener<String> {

		@Override
		public void onRequestFailure(SpiceException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSuccess(String response) {
			synchronized (ConfigUpdater.fileMonitor) {
				
				try {
					PrintWriter pw;
					pw = new PrintWriter(new File(
									Environment.getExternalStorageDirectory()
											+ "/"
											+ ConfigUpdater.context
													.getResources().getText(
															R.string.app_name),
									"config.json"));

					pw.write(response);
					pw.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
