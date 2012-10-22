package co.touchlab.aidltest.services;

import android.os.RemoteException;
import android.util.Log;
import co.touchlab.aidltest.interfaces.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 12:21 PM
 * A test implementation of a service that does IPC with AIDL.
 */
public class RemoteServiceBinder extends IRemoteService.Stub
{
    private RemoteService service;

    public RemoteServiceBinder(RemoteService service)
    {
        this.service = service;
    }

    @Override
    public void loadArticle(final int id, final ArticleCallback callback) throws RemoteException
    {
        service.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    callback.onSuccess(MockDataStore.articleById(id));
                }
                catch (RemoteException e)
                {
                    try
                    {
                        callback.onFailure(Constants.ARTICLE_FAILED_WTF, e.getLocalizedMessage());
                    }
                    catch (RemoteException e1)
                    {
                        Log.e(getClass().getSimpleName(), "Unable to call failure callback!", e1);
                    }
                }
            }
        });
    }

    @Override
    public List<Article> loadMany(Category category) throws RemoteException
    {
        return MockDataStore.articlesByCategory(category);
    }
}
