package pl.us.wiinom.cameraman;

public class Config {

	public final static String CONFIG_URL = "http://tmp.lukasz.az.pl/config.json";
	public final static String config_location = "/web/PUM/";
	public final static String config_file = "config.json";
	public final static String upload_location = "/web/PUM/";

	public static volatile long config_interval = 1000 * 1;
	public static volatile long interval = 1000 * 1;
	public static volatile int quality = 90;
	public static volatile int pixel_threshold = 50;
	public static volatile float threshold = 0.5F;

}
