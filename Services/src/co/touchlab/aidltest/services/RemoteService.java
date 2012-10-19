package co.touchlab.aidltest.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 12:35 PM
 * The actual Service class for other apps to bind to.
 */
public class RemoteService extends Service
{
    private IBinder binder = new RemoteServiceBinder();

    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public void onCreate()
    {
        Log.d("MyActivity", "RemoteService onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Log.d("MyActivity", "RemoteService onDestroy()");
        super.onDestroy();
    }
}
