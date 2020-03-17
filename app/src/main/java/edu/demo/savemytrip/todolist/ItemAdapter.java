package edu.demo.savemytrip.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.demo.savemytrip.R;
import edu.demo.savemytrip.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    // CALLBACK
    public interface Listener {
        void onClickDeleteButton(int position);
    }
    private final Listener callback;

    // FOR DATA
    private List<Item> items;

    // CONSTRUCTOR
    public ItemAdapter(Listener callback) {
        items = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_todo_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.updateWithItem(items.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Item getItem(int position){
        return items.get(position);
    }

    public void updateData(List<Item> pItems){
        items = pItems;
        notifyDataSetChanged();
    }


}
