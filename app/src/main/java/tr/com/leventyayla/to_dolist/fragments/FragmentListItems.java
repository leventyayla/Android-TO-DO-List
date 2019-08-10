package tr.com.leventyayla.to_dolist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.util.Date;

import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.activities.MainActivity;
import tr.com.leventyayla.to_dolist.adapters.ItemAdapter;
import tr.com.leventyayla.to_dolist.models.TODOItem;
import tr.com.leventyayla.to_dolist.models.TODOList;

public class FragmentListItems extends Fragment implements ItemAdapter.ItemClickListener {

    private MainActivity mainActivity = null;
    private TODOList currentList;
    private ItemAdapter itemAdapter;
    private RecyclerView listItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        return inflater.inflate(R.layout.fragment_list_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null){
            Toast.makeText(getContext(), this.getClass().getSimpleName() + " cannot loaded because of mainActivity error!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        assert getArguments() != null;
        int id = getArguments().getInt("id");
        currentList = mainActivity.user.getList().get(id);

        assert mainActivity.getSupportActionBar() != null;
        mainActivity.getSupportActionBar().setTitle(currentList.getName());
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setMenuItemsVisibility();

        itemAdapter = new ItemAdapter(mainActivity.realm, currentList.getItems(), this);
        listItems = view.findViewById(R.id.list_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listItems.getContext(),
                layoutManager.getOrientation());
        listItems.addItemDecoration(dividerItemDecoration);
        listItems.setLayoutManager(layoutManager);
        listItems.setAdapter(itemAdapter);

        view.findViewById(R.id.filter).setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mainActivity);
            View dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });
    }

    @Override
    public void onRowClick(View view, int position, TODOItem data) {
        Snackbar.make(view, "Deadline: " + data.getDeadline(), Snackbar.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_list_item:
                createListItem();
                return true;
            case R.id.export:
                sendEmail();
                return true;
        }
        return false;
    }

    @SuppressLint("InflateParams")
    private void createListItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(mainActivity.getResources().getString(R.string.create_todo_list_item));
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_create_list_item, null, false);
        EditText et_name = view.findViewById(R.id.name);
        EditText et_description = view.findViewById(R.id.description);
        SingleDateAndTimePicker deadline_selector = view.findViewById(R.id.deadline);
        final Date[] deadline = {new Date()};
        deadline_selector.addOnDateChangedListener((displayed, date) -> deadline[0] = date);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String name = et_name.getText().toString();
            String description = et_description.getText().toString();
            if (!name.isEmpty() && !description.isEmpty()){
                TODOItem todoList = new TODOItem(name, description, deadline[0], false);
                itemAdapter.addItem(todoList);
            } else {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendEmail(){
        String fileContents = new Gson().toJson(mainActivity.realm.copyFromRealm(currentList.getItems()));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , fileContents);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TO-DO Items Export");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    private void setMenuItemsVisibility(){
        if (mainActivity.threeDotMenu != null){
            mainActivity.threeDotMenu.getItem(0).setVisible(false);
            mainActivity.threeDotMenu.getItem(1).setVisible(true);
            mainActivity.threeDotMenu.getItem(2).setVisible(true);
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
