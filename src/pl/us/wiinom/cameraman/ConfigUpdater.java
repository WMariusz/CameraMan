package pl.us.wiinom.cameraman;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.content.Context;

import com.google.gson.Gson;

public class ConfigUpdater implements Runnable {

	private static Object fileMonitor;
	private static Context context;
	private FTPClient ftpClient;
	private Handler handler;
	private long configInterval;

	public ConfigUpdater(Context context) {
		ConfigUpdater.context = context;
		this.handler = new Handler();
		ConfigUpdater.fileMonitor = new Object();
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

		try {
			this.ftpClient = new FTPClient();
			this.ftpClient.connect(FtpParams.getServer(),
					FtpParams.getPort());
			if (this.ftpClient.isConnected()) {
				this.ftpClient.login(FtpParams.getUser(),
						FtpParams.getPassword());
				this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				this.ftpClient.enterLocalPassiveMode();
				this.ftpClient.changeWorkingDirectory(Config.config_location);

				synchronized (ConfigUpdater.fileMonitor) {
					BufferedOutputStream outputStream = new BufferedOutputStream(
							new FileOutputStream(new File(
									Environment.getExternalStorageDirectory()
											+ "/"
											+ ConfigUpdater.context
													.getResources().getText(
															R.string.app_name),
									"config.json")));
					this.ftpClient.retrieveFile(Config.config_file,
							outputStream);
					outputStream.close();
				}

				this.ftpClient.noop();
				this.ftpClient.logout();
				this.ftpClient.disconnect();
			}
		} catch (Exception e) {
		}

		// 3. Powtarzanie 1. 2. w petli z opoznieniem zgodnym z parametrem
		// pobranym z configa
		handler.postDelayed(this, this.configInterval);
	}

	public static synchronized void getConfigFromFile() {
		synchronized (ConfigUpdater.fileMonitor) {
			try {
				Config responseConfig = new Gson().fromJson(
						new BufferedReader(new FileReader(new File(Environment
								.getExternalStorageDirectory()
								+ "/"
								+ ConfigUpdater.context
										.getText(R.string.app_name),
								"config.json"))), Config.class);

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
}
