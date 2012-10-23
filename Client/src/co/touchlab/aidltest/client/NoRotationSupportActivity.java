package co.touchlab.aidltest.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import co.touchlab.aidltest.interfaces.Article;
import co.touchlab.aidltest.interfaces.ArticleCallback;
import co.touchlab.aidltest.interfaces.IRemoteService;

/**
 * User: William Sanville
 * Date: 10/23/12
 * Time: 3:53 PM
 * Test to demonstrate what happens if you ignore config changes during a bound service. Basically, in-progress calls
 * will be recalled.
 */
public class NoRotationSupportActivity extends Activity
{
    private ServiceConnection serviceConnection;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        serviceConnection = new ActivityServiceConnection();
        Intent intent = new Intent(IRemoteService.class.getName());
        boolean success = getApplicationContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Log.d(NoRotationSupportActivity.class.getSimpleName(), String.format("bindService() returned %b.", success));
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if (serviceConnection != null)
            tryUnbind();
    }

    private void tryUnbind()
    {
        try
        {
            getApplicationContext().unbindService(serviceConnection);
            Log.d(NoRotationSupportActivity.class.getSimpleName(), "Service unbound successfully");
        }
        catch (IllegalArgumentException e)
        {
            Log.d(NoRotationSupportActivity.class.getSimpleName(), "Service was already unbound.", e);
        }
    }

    private void onBackgroundArticleLoaded(final Article article)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                String value = String.format("Got Article, Title = %s, Category = %s", article.getTitle(), article.getCategory().getName());
                Log.d(NoRotationSupportActivity.class.getSimpleName(), value);
                TextView text = (TextView)findViewById(R.id.text);
                text.setText(value);
            }
        });
    }

    private void onBackgroundLoadFailed()
    {
        //show a message to the user or something
    }

    class ActivityServiceConnection implements ServiceConnection
    {
        private IRemoteService remoteService;

        private ArticleCallback callback = new ArticleCallback.Stub()
        {
            @Override
            public void onFailure(int errorCode, String message) throws RemoteException
            {
                NoRotationSupportActivity.this.onBackgroundLoadFailed();
            }

            @Override
            public void onSuccess(Article result) throws RemoteException
            {
                Log.d("NoRotationSupportActivity", "Got callback, Activity instance: " + NoRotationSupportActivity.this.toString());
                NoRotationSupportActivity.this.onBackgroundArticleLoaded(result);
            }
        };

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            Log.d(getClass().getSimpleName(), "onServiceConnected");
            remoteService = IRemoteService.Stub.asInterface(iBinder);

            try
            {
                /* This gets called in the UI thread. It will return immediately from the service, and we'll get a
                 * callback from a NON-UI THREAD!!!! */
                remoteService.loadArticle(3, callback);
            }
            catch (RemoteException e)
            {
                /* This signifies a dead service connection, which only happens when the remote process dies
                 * unexpectedly. In such a case, onServiceDisconnected() will be called, per the Android documentation. */
                Log.e(NoRotationSupportActivity.class.getSimpleName(), "Error calling remote method.", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            /* This is called when the connection with the service has been unexpectedly disconnected -- that is, its process crashed. */
            remoteService = null;
            Log.d(NoRotationSupportActivity.class.getSimpleName(), "onServiceDisconnected");
        }
    }
}