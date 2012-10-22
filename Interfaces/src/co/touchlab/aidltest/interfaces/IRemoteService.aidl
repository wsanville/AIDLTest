package co.touchlab.aidltest.interfaces;

import co.touchlab.aidltest.interfaces.Article;
import co.touchlab.aidltest.interfaces.ArticleCallback;
import co.touchlab.aidltest.interfaces.Category;

interface IRemoteService
{
    void loadArticle(int id, in ArticleCallback callback);
    List<Article> loadMany(in Category category);
}