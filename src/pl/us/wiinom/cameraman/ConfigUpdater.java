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

	private Context context;
	private FTPClient ftpClient;
	private Handler handler;

	public ConfigUpdater(Context context) {
		this.context = context;
		this.ftpClient = new FTPClient();
		this.handler = new Handler();
	}

	@Override
	public void run() {
		getConfigFromFile(); // 1.

		// new Gson().fromJson(response, Config.class);

		/*
		 * 2. Pobieranie pliku konfiguracyjnego z ftp i nadpisanie w pamieci
		 * telefonu
		 */
		try {
			this.ftpClient.connect(FtpParams.Ftp.getServer(),
					FtpParams.Ftp.getPort());
			if (this.ftpClient.isConnected()) {
				this.ftpClient.login(FtpParams.Ftp.getUser(),
						FtpParams.Ftp.getPassword());
				this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				this.ftpClient.enterLocalPassiveMode();
				this.ftpClient.changeWorkingDirectory("/web/PUM/");
				BufferedOutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(new File(
								Environment.getExternalStorageDirectory()
										+ "/"
										+ context.getResources().getText(
												R.string.app_name),
								"config.json")));
				this.ftpClient.retrieveFile("config.json", outputStream);
				outputStream.close();
				this.ftpClient.noop();
				this.ftpClient.logout();
				this.ftpClient.disconnect();
			}
		} catch (Exception e) {
		}

		// 3. Powtarzanie 1. 2. w petli z opoznieniem zgodnym z parametrem
		// pobranym z configa
		handler.postDelayed(this, Config.config_interval);
	}

	public static synchronized void getConfigFromFile() {
		synchronized (MainActivity.monitor) {
			// 1. a) Odczytanie pliku konfiguracyjnego i ustawienie zmiennych
			// klasy Config (!!! bez Config.interval !!!)
		}
		synchronized (MainActivity.monitor2) {
			// 1. b) Odczytanie pliku konfiguracyjnego i ustawienie intervalu
			// aparatu (Config.interval)
		}
	}
}
