package tr.com.leventyayla.to_dolist.utils;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import tr.com.leventyayla.to_dolist.fragments.FragmentListItems;
import tr.com.leventyayla.to_dolist.fragments.FragmentUserLists;

public class FragmentChanger {

    private FragmentManager fragmentManager;
    private int containerId;

    public FragmentChanger(FragmentManager fragmentManager, int containerId){
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        init();
    }

    private void init(){
        if (fragmentManager.getFragments().size() == 0)
            fragmentManager.beginTransaction().add(containerId, new FragmentUserLists(), "fragmentUserLists").commit();
    }

    public boolean isLastFragment(){
        return fragmentManager.getFragments().size() == 1;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment currentFragment = fragments.get(fragments.size() - 1);
        return currentFragment.onOptionsItemSelected(item);
    }

    public void popBackStack(boolean clearAll){
        if (clearAll){
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else {
            fragmentManager.popBackStack();
        }
    }

    public void setFragmentListItems(int index){
        Bundle bundle = new Bundle();
        bundle.putInt("id", index);
        FragmentListItems fragObj = new FragmentListItems();
        fragObj.setArguments(bundle);

        FragmentTransaction ft = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(containerId, fragObj,"fragmentListItems").addToBackStack("fragmentUserLists").commit();
    }
}
