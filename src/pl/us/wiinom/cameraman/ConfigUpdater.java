package pl.us.wiinom.cameraman;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;

public class ConfigUpdater implements Runnable {

	@Override
	public void run() {
		getConfigFromFile(); // 1.

		/*
		 * 2. Pobieranie pliku konfiguracyjnego z ftp i nadpisanie w pamieci
		 * telefonu
		 * 
		 * InputStream source = Downloader.retrieveStream(Config.CONFIG_URL);
		 * Gson gson = new Gson();
		 * Reader reader = new InputStreamReader(source);
		 */

		// 3. Powtarzanie 1. 2. w petli z opoznieniem zgodnym z parametrem
		// pobranym z configa
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
