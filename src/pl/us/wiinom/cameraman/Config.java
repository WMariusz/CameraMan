package pl.us.wiinom.cameraman;

public class Config {

	public final static String CACHE_KEY = "Config";
	
	public final static String CONFIG_URL = "http://tmp.lukasz.az.pl/PUM/config.json";// url do pliku konfiguracyjnego
	
	public static volatile long config_interval = 1000 * 60;
	public static volatile long interval = 1000;
	public static volatile int quality = 90;
	public static volatile int pixel_threshold = 50;
	public static volatile float threshold = 0.5F;

}
