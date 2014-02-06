package pl.us.wiinom.cameraman;

public class FtpParams {
	
	//public static final FtpParams Ftp = new FtpParams();
	
	public FtpParams() {
		server = "ftp.strefa.pl";
		port = 21;
		user = "admin+tmp1389983364.strefa.pl";
		password = "sAserebeHuswu4aw";
	}
	
	private static volatile String server = "ftp.strefa.pl";
	private static volatile int port = 21;
	private static volatile String user = "admin+tmp1389983364.strefa.pl";
	private static volatile String password = "sAserebeHuswu4aw";

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
