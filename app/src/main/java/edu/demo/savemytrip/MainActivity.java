package edu.demo.savemytrip;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import edu.demo.savemytrip.base.BaseActivity;
import edu.demo.savemytrip.todolist.TodoListActivity;
import edu.demo.savemytrip.tripbook.TripBookActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_main;
    }

    // -------------
    // ACTIONS
    // -------------
    @OnClick(R.id.main_activity_button_trip_book)
    public void onClickTripBookButton(){
        Intent intent = new Intent(this, TripBookActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_button_todo_list)
    public void onClickTodoListButton(){
        Intent intent = new Intent(this, TodoListActivity.class);
        startActivity(intent);
    }
}
