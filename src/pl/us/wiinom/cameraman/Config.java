package pl.us.wiinom.cameraman;

public class Config {

	final public static String CONFIG_URL ="";//url do pliku konfiguracyjnego

	public volatile long interval;
	
	public volatile int quality;
	
	public Config() {
		this.interval = 1000;
	}
}
