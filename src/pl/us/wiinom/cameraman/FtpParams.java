package pl.us.wiinom.cameraman;

public class FtpParams {
	
	public static final FtpParams Ftp = new FtpParams();
	
	public FtpParams() {
		server = "tmp.lukasz.az.pl";
		port = 21;
		user = "lukasz_tmp";
		password = "android123";
	}
	
	private static volatile String server;
	private static volatile int port;
	private static volatile String user;
	private static volatile String password;

	static String getServer() {
		return server;
	}
	static int getPort() {
		return port;
	}
	static String getUser() {
		return user;
	}
	static String getPassword() {
		return password;
	}
	
}
