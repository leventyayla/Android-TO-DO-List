package tr.com.leventyayla.to_dolist.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmList;
import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.models.TODOList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private Realm realm;
    private RealmList<TODOList> list;
    private ItemClickListener mClickListener;

    public ListAdapter(Realm realm, RealmList<TODOList> list, ItemClickListener itemClickListener) {
        this.realm = realm;
        this.list = list;
        this.mClickListener = itemClickListener;
        list.addChangeListener(todoLists -> Log.d(this.getClass().getSimpleName(), "todoList changed on db."));
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView delete;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
            view.setOnClickListener(this);
            delete.setOnClickListener(v -> deleteItem(getAdapterPosition()));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onRowClick(view, getAdapterPosition(), list.get(getAdapterPosition()));
            }
        }
    }

    public interface ItemClickListener {
        void onRowClick(View view, int position, TODOList data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todo_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TODOList temp = list.get(position);

        holder.name.setText(temp != null ? temp.getName() : "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(TODOList item){
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