package co.touchlab.aidltest.services;

import co.touchlab.aidltest.interfaces.Article;
import co.touchlab.aidltest.interfaces.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 12:22 PM
 * A bogus data store for a simple data model.
 * <p/>
 * The getter methods are synchronized because according to the docs, an AIDL implementation should expect to be called
 * from a pool of unknown threads, and thus, be thread safe.  This example is obviously a bit contrived, but you know...
 */
public class MockDataStore
{
    private static final List<Category> CATEGORIES = new ArrayList<Category>();
    private static final List<Article> ARTICLES = new ArrayList<Article>();

    static
    {
        Category first = new Category(1, "First Category");
        CATEGORIES.add(first);
        Category second = new Category(2, "Second Category");
        CATEGORIES.add(second);

        ARTICLES.add(new Article(first, 1, "Body contents of Article 1", "Article 1"));
        ARTICLES.add(new Article(first, 2, "Body contents of Article 2", "Article 2"));
        ARTICLES.add(new Article(second, 3, "Body contents of Article 3", "Article 3"));
        ARTICLES.add(new Article(second, 4, "Body contents of Article 4", "Article 4"));
        ARTICLES.add(new Article(second, 5, "Body contents of Article 5", "Article 5"));
    }

    public synchronized static Article articleById(int id)
    {
        simulateWait();

        for (Article a : ARTICLES)
            if (a.getId() == id)
                return a;
        return null;
    }

    public synchronized static List<Article> articlesByCategory(Category c)
    {
        //simulateWait();

        int catId = c.getId();
        List<Article> results = new ArrayList<Article>();
        for (Article a : ARTICLES)
        {
            Category category = a.getCategory();
            if (category != null && category.getId() == catId)
                results.add(a);
        }
        return results;
    }

    private static void simulateWait()
    {
        try
        {
            Thread.sleep(8000);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
