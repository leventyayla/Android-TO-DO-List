package tr.com.leventyayla.to_dolist.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.models.TODOItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Realm realm;
    private RealmList<TODOItem> list;
    private ItemClickListener mClickListener;

    public ItemAdapter(Realm realm, RealmList<TODOItem> list, ItemClickListener itemClickListener) {
        this.realm = realm;
        this.list = list;
        this.mClickListener = itemClickListener;
        list.addChangeListener(todoLists -> Log.d(this.getClass().getSimpleName(), "todoListItem changed on db."));
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView description;
        CheckBox name;
        ImageView delete;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
            description = view.findViewById(R.id.description);
            view.setOnClickListener(this);
            delete.setOnClickListener(v -> deleteItem(getAdapterPosition()));
            name.setOnCheckedChangeListener((buttonView, isChecked) -> {
                realm.executeTransaction(realm -> {
                    Objects.requireNonNull(list.get(getAdapterPosition())).setCompleted(isChecked);
                });
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onRowClick(view, getAdapterPosition(), list.get(getAdapterPosition()));
            }
        }
    }

    public interface ItemClickListener {
        void onRowClick(View view, int position, TODOItem data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todo_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TODOItem temp = list.get(position);

        holder.name.setText(temp != null ? temp.getName() : "");
        holder.name.setChecked(temp != null && temp.isCompleted());
        holder.description.setText(temp != null ? temp.getDescription() : "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(TODOItem item){
        realm.executeTransaction(realm -> {
            list.add(item);
            notifyItemInserted(list.size() - 1);
        });
    }

    private void deleteItem(int pos){
        realm.executeTransaction(realm -> {
            try{
                list.deleteFromRealm(pos);
                notifyItemRemoved(pos);
            } catch (ArrayIndexOutOfBoundsException ex){
                Log.e(this.getClass().getSimpleName(), "deleteItem function triggered with wrong position!");
            }
        });
    }
}