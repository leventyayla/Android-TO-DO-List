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

public class MainActivity extends AppCompatActivity {

    Realm realm;
    User user;

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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        realm.beginTransaction();
        user.setLoggedIn(false);
        realm.commitTransaction();
        startActivity(new Intent(this, StartUpActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
