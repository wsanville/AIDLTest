package co.touchlab.aidltest.interfaces;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: William Sanville
 * Date: 10/12/12
 * Time: 11:33 AM
 * A simple data model to test passing complex types between processes.
 */
public class Category implements Parcelable
{
    private int id;
    private String name;

    public static final Creator<Category> CREATOR = new Creator<Category>()
    {
        @Override
        public Category createFromParcel(Parcel parcel)
        {
            return new Category(parcel);
        }

        @Override
        public Category[] newArray(int i)
        {
            return new Category[i];
        }
    };

    public Category(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Category(Parcel in)
    {
        readFromParcel(in);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
        parcel.writeString(name);
    }

    private void readFromParcel(Parcel parcel)
    {
        id = parcel.readInt();
        name = parcel.readString();
    }
}
