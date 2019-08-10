package tr.com.leventyayla.to_dolist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.activities.MainActivity;
import tr.com.leventyayla.to_dolist.models.TODOList;

public class FragmentListItems extends Fragment {

    private MainActivity mainActivity = null;
    private TODOList currentList;

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
