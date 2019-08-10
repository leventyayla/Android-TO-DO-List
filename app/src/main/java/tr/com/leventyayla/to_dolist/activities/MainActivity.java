package tr.com.leventyayla.to_dolist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.models.User;
import tr.com.leventyayla.to_dolist.utils.FragmentChanger;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public User user;
    public FragmentChanger fragmentChanger;
    public Menu threeDotMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        RealmResults<User> users = realm.where(User.class).equalTo("isLoggedIn", true).findAll();
        if (users.size() != 1){
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                realm.beginTransaction();
                assert user != null;
                user.setLoggedIn(false);
                realm.commitTransaction();
            }
            startActivity(new Intent(this, StartUpActivity.class));
            finish();
            return;
        }
        user = users.get(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        threeDotMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, threeDotMenu);
        fragmentChanger = new FragmentChanger(getSupportFragmentManager(), R.id.fragment_container);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
            return true;
        } else {
            return fragmentChanger.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        realm.beginTransaction();
        user.setLoggedIn(false);
        realm.commitTransaction();
        startActivity(new Intent(this, StartUpActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (fragmentChanger.isLastFragment()){
            super.onBackPressed();
        } else {
            fragmentChanger.popBackStack(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        if(realm != null) {
            realm.close();
        }
        super.onDestroy();
    }
}
