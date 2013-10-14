package pl.us.wiinom.cameraman;

import com.octo.android.robospice.request.SpiceRequest;

public class UploadRequest extends SpiceRequest<String> {

	public UploadRequest() {
		super(String.class);
	}

	@Override
	public String loadDataFromNetwork() throws Exception {
		// TODO:
		// - wysy³anie na FTP zdjêcia
		// - pobieranie z FTP konfiguracji
		return "test";
	}

}
