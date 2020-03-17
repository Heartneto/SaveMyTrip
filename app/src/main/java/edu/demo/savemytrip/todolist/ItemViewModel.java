package edu.demo.savemytrip.todolist;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import edu.demo.savemytrip.models.Item;
import edu.demo.savemytrip.models.User;
import edu.demo.savemytrip.repositories.ItemDataRepository;
import edu.demo.savemytrip.repositories.UserDataRepository;

public class ItemViewModel extends ViewModel {

    // REPOSITORIES
    private final ItemDataRepository itemDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<User> currentUser;

    public ItemViewModel(ItemDataRepository itemDataSource, UserDataRepository userDataSource,
                         Executor executor) {
        this.itemDataSource = itemDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    public void init(long userId){
        if (currentUser != null){
            return;
        }
        currentUser = userDataSource.getUser(userId);
    }

    // --------------
    // FOR USER
    // --------------
    public LiveData<User> getUser(long userId){
        return currentUser;
    }

    // --------------
    // FOR ITEM
    // --------------
    public LiveData<List<Item>> getItems(long userId){
        return itemDataSource.getItems(userId);
    }

    public void createItem(Item item){
        executor.execute(() ->{
            itemDataSource.createItem(item);
        });
    }

    public void deleteItem(long itemId){
        executor.execute(() -> itemDataSource.deleteItem(itemId));
    }

    public void updateItem(Item item){
        executor.execute(() -> itemDataSource.updateItem(item));
    }
}
