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

import java.util.Date;
import java.util.Objects;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.models.FilterSettings;
import tr.com.leventyayla.to_dolist.models.TODOItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Realm realm;
    private RealmList<TODOItem> list;
    private RealmList<TODOItem> filteredList;
    private ItemClickListener mClickListener;
    private FilterSettings filterSettings = null;

    public ItemAdapter(Realm realm, RealmList<TODOItem> list, ItemClickListener itemClickListener) {
        this.realm = realm;
        this.list = list;
        this.filteredList = this.list;
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
                    Objects.requireNonNull(filteredList.get(getAdapterPosition())).setCompleted(isChecked);
                });
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onRowClick(view, getAdapterPosition(), filteredList.get(getAdapterPosition()));
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
        TODOItem temp = filteredList.get(position);

        holder.name.setText(temp != null ? temp.getName() : "");
        holder.name.setChecked(temp != null && temp.isCompleted());
        holder.description.setText(temp != null ? temp.getDescription() : "");
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public FilterSettings getFilterSettings(){
        return filterSettings;
    }

    public void addItem(TODOItem item){
        realm.executeTransaction(realm -> {
            filteredList.add(item);
            notifyItemInserted(filteredList.size() - 1);
        });
    }

    public void filter(String name, boolean isCompleted, boolean isExpired){
        RealmQuery<TODOItem> query = list.where().equalTo("isCompleted", isCompleted);
        if (isExpired){
            query = query.lessThan("deadline", new Date());
        } else {
            query = query.greaterThan("deadline", new Date());
        }
        if (name != null && !name.isEmpty()){
            query = query.contains("name", name, Case.INSENSITIVE);
            filterSettings = new FilterSettings(name, isCompleted, isExpired);
        } else {
            filterSettings = new FilterSettings(null, isCompleted, isExpired);
        }

        RealmResults<TODOItem> results = query.findAll();
        RealmList<TODOItem> filteredList = new RealmList<>();
        filteredList.addAll(results.subList(0, results.size()));
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public void clearFilter(){
        filteredList = list;
        filterSettings = null;
        notifyDataSetChanged();
    }

    private void deleteItem(int pos){
        realm.executeTransaction(realm -> {
            try{
                filteredList.deleteFromRealm(pos);
                notifyItemRemoved(pos);
            } catch (ArrayIndexOutOfBoundsException ex){
                Log.e(this.getClass().getSimpleName(), "deleteItem function triggered with wrong position!");
            }
        });
    }
}