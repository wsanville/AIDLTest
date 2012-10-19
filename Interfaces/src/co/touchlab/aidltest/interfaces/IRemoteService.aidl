package co.touchlab.aidltest.interfaces;

import co.touchlab.aidltest.interfaces.Article;
import co.touchlab.aidltest.interfaces.Category;

interface IRemoteService
{
    Article loadArticle(int id);
    List<Article> loadMany(in Category category);
}