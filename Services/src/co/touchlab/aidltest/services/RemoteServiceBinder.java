package co.touchlab.aidltest.services;

import android.os.RemoteException;
import co.touchlab.aidltest.interfaces.Article;
import co.touchlab.aidltest.interfaces.Category;
import co.touchlab.aidltest.interfaces.IRemoteService;

import java.util.List;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 12:21 PM
 * A test implementation of a service that does IPC with AIDL.
 */
public class RemoteServiceBinder extends IRemoteService.Stub
{
    @Override
    public Article loadArticle(int id) throws RemoteException
    {
        return MockDataStore.articleById(id);
    }

    @Override
    public List<Article> loadMany(Category category) throws RemoteException
    {
        return MockDataStore.articlesByCategory(category);
    }
}
