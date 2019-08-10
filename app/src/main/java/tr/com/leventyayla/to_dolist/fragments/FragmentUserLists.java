package tr.com.leventyayla.to_dolist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.activities.MainActivity;
import tr.com.leventyayla.to_dolist.adapters.ListAdapter;
import tr.com.leventyayla.to_dolist.models.TODOList;

public class FragmentUserLists extends Fragment implements ListAdapter.ItemClickListener {

    private MainActivity mainActivity = null;
    private ListAdapter listAdapter;
    private RecyclerView user_lists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        return inflater.inflate(R.layout.fragment_user_lists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null){
            Toast.makeText(getContext(), this.getClass().getSimpleName() + " cannot loaded because of mainActivity error!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        assert mainActivity.getSupportActionBar() != null;
        mainActivity.getSupportActionBar().setTitle("Lists for " + mainActivity.user.getEmail());
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setMenuItemsVisibility();

        listAdapter = new ListAdapter(mainActivity.realm, mainActivity.user.getList(), this);
        user_lists = view.findViewById(R.id.user_lists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(user_lists.getContext(),
                layoutManager.getOrientation());
        user_lists.addItemDecoration(dividerItemDecoration);
        user_lists.setLayoutManager(layoutManager);
        user_lists.setAdapter(listAdapter);

        new Handler().postDelayed(() -> {
            TODOList todoList = new TODOList("Kedi");
            listAdapter.addItem(todoList);
            //istAdapter.deleteItem(0);
        }, 1500);
    }

    @Override
    public void onRowClick(View view, int position, TODOList data) {
        mainActivity.fragmentChanger.setFragmentListItems(position);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.create_list){
            Toast.makeText(getContext(), "Create list", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setMenuItemsVisibility(){
        if (mainActivity.threeDotMenu != null){
            mainActivity.threeDotMenu.getItem(0).setVisible(true);
            mainActivity.threeDotMenu.getItem(1).setVisible(false);
            mainActivity.threeDotMenu.getItem(2).setVisible(false);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        } else {
            Log.e(this.getClass().getSimpleName(), "Error occurred when getting activity!");
        }
    }
}
