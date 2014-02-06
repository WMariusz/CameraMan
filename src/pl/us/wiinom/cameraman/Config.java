package pl.us.wiinom.cameraman;

public class Config {

	public final static String CONFIG_URL = "http://www.tmp1389983364.strefa.pl/config.json";
	//public final static String config_location = "/web/PUM/";
	public final static String config_file = "config.json";
	public final static String upload_location = "/web/PUM/";

	public static volatile long config_interval = 1000 * 1;
	public static volatile long interval = 1000 * 1;
	public static volatile int quality = 90;
	public static volatile float pixel_threshold = 0.1F;
	public static volatile float threshold = 0.2F;

}
