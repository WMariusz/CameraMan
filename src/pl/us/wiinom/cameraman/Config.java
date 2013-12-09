package pl.us.wiinom.cameraman;

public class Config {

	public final static String CONFIG_URL = "";// url do pliku konfiguracyjnego

	public static volatile FtpParams ftpParams;
	public static volatile long interval = 1000;
	public static volatile int quality = 90;
	public static volatile int pixel_threshold = 50;
	public static volatile float threshold = 0.5F;

	public class FtpParams {
		public String server;
		public int port;
		public String user;
		public String password;
	}
}
