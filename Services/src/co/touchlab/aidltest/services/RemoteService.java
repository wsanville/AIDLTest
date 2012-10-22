package co.touchlab.aidltest.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 12:35 PM
 * The actual Service class for other apps to bind to.
 */
public class RemoteService extends Service
{
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private IBinder binder = new RemoteServiceBinder(this);

    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    void submit(Runnable runnable)
    {
        executor.submit(runnable);
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
        executor.shutdownNow();
        super.onDestroy();
    }
}
