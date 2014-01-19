package pl.us.wiinom.cameraman;

public class Config {

	public final static String CACHE_KEY = "Config";
	
	public final static String CONFIG_URL = "http://tmp.lukasz.az.pl/config.json";// url do pliku konfiguracyjnego
	
	public volatile long interval = 1000;
	public volatile int quality = 90;
	public volatile int pixel_threshold = 50;
	public volatile float threshold = 0.5F;

}
