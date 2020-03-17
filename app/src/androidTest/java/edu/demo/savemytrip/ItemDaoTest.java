package edu.demo.savemytrip;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.demo.savemytrip.database.SaveMyTripDatabase;
import edu.demo.savemytrip.models.Item;
import edu.demo.savemytrip.models.User;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ItemDaoTest {

    // FOR DATA
    private SaveMyTripDatabase database;

    // DATA SET FOR TEST
    private static long USER_ID = 1;
    private static User USER_DEMO = new User(USER_ID, "Philippe", "https://www.google.com, ");
    private static Item NEW_ITEM_PLACE_TO_VISIT = new Item("Visite cet endroit de rêve !", 0, USER_ID);
    private static Item NEW_ITEM_IDEA = new Item("On pourrait faire du chien de traineau ?", 1, USER_ID);
    private static Item NEW_ITEM_RESTAURANTS = new Item("Ce restaurant à l'air sympa", 2, USER_ID);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception{
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                SaveMyTripDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception{
        database.close();
    }


    @Test
    public void insertAndGetUser() throws InterruptedException{
        // BEFORE : Adding a new user
        database.userDao().createUser(USER_DEMO);
        // TEST
        User user = LiveDataTestUtil.getValue(database.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }

    @Test
    public void getItemWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<Item> items = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }

    @Test
    public void insertAndGetItems() throws InterruptedException{
        // BEFORE : Adding demo user & demo items
        database.userDao().createUser(USER_DEMO);
        database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        database.itemDao().insertItem(NEW_ITEM_IDEA);
        database.itemDao().insertItem(NEW_ITEM_RESTAURANTS);

        // TEST
        List<Item> items = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException{
        // BEFORE : Adding demo user & demo item. Next, update item added & re-save it
        database.userDao().createUser(USER_DEMO);
        database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID)).get(0);
        itemAdded.setSelected(true);
        database.itemDao().updateItem(itemAdded);
        // TEST
        List<Item> items = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 1 && items.get(0).getSelected());
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException{
        // BEFORE : Adding demo user & demo item. Next, get added item & delete it
        database.userDao().createUser(USER_DEMO);
        database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID)).get(0);
        database.itemDao().deleteItem(itemAdded.getId());

        // TEST
        List<Item> items = LiveDataTestUtil.getValue(database.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }
}
