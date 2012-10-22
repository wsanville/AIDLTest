package co.touchlab.aidltest.interfaces;

import co.touchlab.aidltest.interfaces.Article;

interface ArticleCallback
{
    void onSuccess(in Article result);
    void onFailure(int errorCode, String message);
}