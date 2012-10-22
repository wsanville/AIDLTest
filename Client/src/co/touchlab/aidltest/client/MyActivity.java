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

public class MyActivity extends Activity
{
    private ActivityServiceConnection serviceConnection;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public Object onRetainNonConfigurationInstance()
    {
        serviceConnection.detach();
        return serviceConnection;
    }

    @Override
    protected void onStart()
    {
        Log.d(getClass().getSimpleName(), "onStart() called");
        super.onStart();

        //Let's try to bind to a service in a separate APK.
        Object last = getLastNonConfigurationInstance();
        if (last instanceof ActivityServiceConnection)
        {
            serviceConnection = (ActivityServiceConnection)last;
            serviceConnection.attach(this);
        }
        else
        {
            serviceConnection = new ActivityServiceConnection(this);
            Intent intent = new Intent(IRemoteService.class.getName());
            boolean success = getApplicationContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            Log.d(MyActivity.class.getSimpleName(), String.format("bindService() returned %b.", success));
        }
    }

    @Override
    protected void onStop()
    {
        Log.d(getClass().getSimpleName(), "onStop() called");
        super.onStop();

        if (!isChangingConfigurations() && serviceConnection != null)
        {
            Log.d(MyActivity.class.getSimpleName(), "calling unbindService()");
            getApplicationContext().unbindService(serviceConnection);
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
                Log.d(MyActivity.class.getSimpleName(), value);
                TextView text = (TextView)findViewById(R.id.text);
                text.setText(value);
            }
        });
    }

    private void onBackgroundLoadFailed()
    {
        //show a message to the user or something
    }

    static class ActivityServiceConnection implements ServiceConnection
    {
        private IRemoteService remoteService;
        private MyActivity activity;
        private Article article;

        private ArticleCallback callback = new ArticleCallback.Stub()
        {
            @Override
            public void onFailure(int errorCode, String message) throws RemoteException
            {
                callFailure();
            }

            @Override
            public void onSuccess(Article result) throws RemoteException
            {
                setAndNotify(result);
            }
        };

        ActivityServiceConnection(MyActivity activity)
        {
            this.activity = activity;
        }

        public synchronized void attach(MyActivity activity)
        {
            this.activity = activity;
            if (article != null && this.activity != null)
                this.activity.onBackgroundArticleLoaded(article);
        }

        public synchronized void detach()
        {
            this.activity = null;
        }

        synchronized void setAndNotify(Article article)
        {
            this.article = article;
            if (this.activity != null)
                this.activity.onBackgroundArticleLoaded(article);
        }

        synchronized void callFailure()
        {
            if (this.activity != null)
                this.activity.onBackgroundLoadFailed();
        }

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
                Log.e(MyActivity.class.getSimpleName(), "Error calling remote method.", e);
             }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            /* This is called when the connection with the service has been unexpectedly disconnected -- that is, its process crashed. */
            remoteService = null;
            Log.d(MyActivity.class.getSimpleName(), "onServiceDisconnected");
        }
    }
}
