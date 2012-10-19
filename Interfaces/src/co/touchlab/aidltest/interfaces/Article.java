package co.touchlab.aidltest.interfaces;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 11:33 AM
 * A simple data model to test passing complex types between processes.
 */
public class Article implements Parcelable
{
    private int id;
    private String title, text;
    private Category category;

    public static final Creator<Article> CREATOR = new Creator<Article>()
    {
        @Override
        public Article createFromParcel(Parcel parcel)
        {
            return new Article(parcel);
        }

        @Override
        public Article[] newArray(int i)
        {
            return new Article[i];
        }
    };

    public Article(Category category, int id, String text, String title)
    {
        this.category = category;
        this.id = id;
        this.text = text;
        this.title = title;
    }

    public Article(Parcel in)
    {
        readFromParcel(in);
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeParcelable(category, flags);
    }

    private void readFromParcel(Parcel in)
    {
        id = in.readInt();
        title = in.readString();
        text = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }
}
