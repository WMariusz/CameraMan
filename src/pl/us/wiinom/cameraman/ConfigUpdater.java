package pl.us.wiinom.cameraman;

import java.lang.*;

public class ConfigUpdater implements Runnable {
		
	@Override
	public void run()
	{
		getConfigFromFile(); // 1.
		// 2. Pobieranie pliku konfiguracyjnego z ftp i nadpisanie w pamieci telefonu
		// 3. Powtarzanie 1. 2. w petli z opoznieniem zgodnym z parametrem pobranym z configa
	}
	
	public static synchronized void getConfigFromFile()
	{
		synchronized(MainActivity.monitor)
		{
			// 1. a) Odczytanie pliku konfiguracyjnego i ustawienie zmiennych (bez intervalu)
		}
		synchronized(MainActivity.monitor2)
		{
			// 1. b) Odczytanie pliku konfiguracyjnego i ustawienie intervalu aparatu
		}
	}
}
