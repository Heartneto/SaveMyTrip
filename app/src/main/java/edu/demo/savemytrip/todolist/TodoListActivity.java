package edu.demo.savemytrip.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import edu.demo.savemytrip.R;
import edu.demo.savemytrip.base.BaseActivity;
import edu.demo.savemytrip.injections.Injection;
import edu.demo.savemytrip.injections.ViewModelFactory;
import edu.demo.savemytrip.models.Item;
import edu.demo.savemytrip.models.User;
import edu.demo.savemytrip.utils.ItemClickSupport;

public class TodoListActivity extends BaseActivity implements ItemAdapter.Listener {

    // FOR DESIGN
    @BindView(R.id.todo_list_activity_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.todo_list_activity_spinner)
    Spinner spinner;
    @BindView(R.id.todo_list_activity_edit_text)
    EditText editText;
    @BindView(R.id.todo_list_activity_header_profile_image)
    ImageView profileImage;
    @BindView(R.id.todo_list_activity_header_profile_text)
    TextView profileText;

    // FOR DATA
    private ItemViewModel itemViewModel;
    private ItemAdapter adapter;
    private static int USER_ID = 1;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_todo_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureToolbar();
        configureSpinner();

        configureRecyclerView();
        configureViewModel();

        getCurrentUser(USER_ID);
        getItems(USER_ID);
    }

    // -------------------
    // ACTIONS
    // -------------------

    @OnClick(R.id.todo_list_activity_button_add)
    public void onClickAddButton() {
        /*TODO*/
        createItem();
    }

    @Override
    public void onClickDeleteButton(int position){
        deleteItem(adapter.getItem(position));
    }


    // -----------------
    // DATA
    // -----------------
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        itemViewModel = new ViewModelProvider(this, mViewModelFactory).get(ItemViewModel.class);
        itemViewModel.init(USER_ID);
    }

    private void getCurrentUser(int userId){
        itemViewModel.getUser(userId).observe(this, this::updateHeader);
    }

    private void getItems(int userId){
        itemViewModel.getItems(userId).observe(this, this::updateItemsList);
    }

    private void createItem(){
        Item item = new Item(editText.getText().toString(),
                spinner.getSelectedItemPosition(), USER_ID);
        editText.setText("");
        itemViewModel.createItem(item);
    }

    private void deleteItem(Item item){
        itemViewModel.deleteItem(item.getId());
    }

    private void updateItem(Item item){
        item.setSelected(!item.getSelected());
        itemViewModel.updateItem(item);
    }


    // -------------------
    // UI
    // -------------------

    private void configureSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void configureRecyclerView(){
        adapter = new ItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(recyclerView, R.layout.activity_todo_list_item)
                .setOnItemClickListener((recyclerView1, position, v) -> updateItem(adapter.getItem(position)));
    }

    private void updateHeader(User user){
        profileText.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(this.profileImage);
    }

    private void updateItemsList(List<Item> items){
        adapter.updateData(items);
    }
}
