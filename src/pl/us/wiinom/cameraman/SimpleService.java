package pl.us.wiinom.cameraman;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

public class SimpleService extends SpiceService
{
	@Override
	public CacheManager createCacheManager(Application arg0) throws CacheCreationException
	{
		CacheManager cacheManager = new CacheManager();
		
		return cacheManager;
	}
}
