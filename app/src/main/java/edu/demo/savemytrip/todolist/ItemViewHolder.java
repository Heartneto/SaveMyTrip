package edu.demo.savemytrip.todolist;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.demo.savemytrip.R;
import edu.demo.savemytrip.models.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.activity_todo_list_item_text)
    TextView textView;
    @BindView(R.id.activity_todo_list_item_image)
    ImageView imageView;
    @BindView(R.id.activity_todo_list_item_remove)
    ImageButton imageButton;

    // FOR DATA
    private WeakReference<ItemAdapter.Listener> callbackWeakRef;

    public ItemViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithItem(Item item, ItemAdapter.Listener callback){
        callbackWeakRef = new WeakReference<ItemAdapter.Listener>(callback);
        textView.setText(item.getText());
        imageButton.setOnClickListener(this);
        switch (item.getCategory()){
            case 0:
                imageView.setBackgroundResource(R.drawable.ic_room_black_24px);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.ic_lightbulb_outline_black_24px);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.ic_local_cafe_black_24px);
                break;
        }
        if (item.getSelected()){
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public void onClick(View v) {
        ItemAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
