package edu.demo.savemytrip;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.demo.savemytrip.database.SaveMyTripDatabase;
import edu.demo.savemytrip.models.Item;
import edu.demo.savemytrip.provider.ItemContentProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class ItemProviderTest {

    // FOR DATA
    private ContentResolver contentResolver;

    // DATA SET FOR TEST
    private static long USER_ID = 1;

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                SaveMyTripDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted(){
        final Cursor cursor = contentResolver
                .query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID),
                        null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetItem(){
        final Uri userUri = contentResolver
                .insert(ItemContentProvider.URI_ITEM, generateItem());
        final Cursor cursor = contentResolver
                .query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID),
                        null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("text")),
                is("Visite cet endroit de rêve !"));
        cursor.close();
    }

    private ContentValues generateItem(){
        final ContentValues values = new ContentValues();
        values.put("text", "Visite cet endroit de rêve !");
        values.put("category", "0");
        values.put("isSelected", "false");
        values.put("userId", "1");
        return values;
    }
}
