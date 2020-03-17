package edu.demo.savemytrip.injections;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.demo.savemytrip.database.SaveMyTripDatabase;
import edu.demo.savemytrip.repositories.ItemDataRepository;
import edu.demo.savemytrip.repositories.UserDataRepository;

public class Injection {

    public static ItemDataRepository provideItemDataSource(Context context){
        SaveMyTripDatabase database = SaveMyTripDatabase.getInstance(context);
        return new ItemDataRepository(database.itemDao());
    }

    public static UserDataRepository provideUserDataSource(Context context){
        SaveMyTripDatabase database = SaveMyTripDatabase.getInstance(context);
        return new UserDataRepository(database.userDao());
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context){
        ItemDataRepository dataSourceItem = provideItemDataSource(context);
        UserDataRepository userSourceItem = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceItem, userSourceItem, executor);
    }

}
