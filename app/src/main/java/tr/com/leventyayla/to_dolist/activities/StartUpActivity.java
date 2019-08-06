package tr.com.leventyayla.to_dolist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import io.realm.Realm;
import tr.com.leventyayla.to_dolist.R;
import tr.com.leventyayla.to_dolist.models.User;
import tr.com.leventyayla.to_dolist.utils.EmailValidator;

public class StartUpActivity extends AppCompatActivity implements View.OnClickListener {

    Realm realm;
    TextInputEditText username, password;
    EmailValidator emailValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        realm = Realm.getDefaultInstance();

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_sing_up).setOnClickListener(this);
        emailValidator = new EmailValidator();
        username.addTextChangedListener(emailValidator);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();        break;
            case R.id.btn_sing_up:
                registerUser(); break;
        }
    }

    private void login(){
        String username = this.username.getText() == null ? "" : this.username.getText().toString();
        String password = this.password.getText() == null ? "" : this.password.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Snackbar.make(
                    this.password,
                    getResources().getString(R.string.invalid_inputs),
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        User user = realm.where(User.class)
                .equalTo("email", username)
                .equalTo("password", password)
                .findFirst();

        if (user != null){
            Snackbar.make(
                    this.password,
                    "User id: " + user.getId(),
                    Snackbar.LENGTH_LONG)
                    .show();
        } else {
            Snackbar.make(
                    this.password,
                    "User not found!",
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(){
        String username = this.username.getText() == null ? "" : this.username.getText().toString();
        String password = this.password.getText() == null ? "" : this.password.getText().toString();
        User user = realm.where(User.class).equalTo("email", username).findFirst();
        if (user != null){
            Snackbar.make(
                    this.password,
                    getResources().getString(R.string.already_registered),
                    Snackbar.LENGTH_LONG)
                    .show();
        } else if (emailValidator.isValid() && !password.isEmpty()){
            realm.executeTransactionAsync(
                    bgRealm -> {
                        Number maxId = bgRealm.where(User.class).max("id");
                        int nextId = (maxId == null) ? 0 : maxId.intValue() + 1;
                        final User createdUser = bgRealm.createObject(User.class, nextId);
                        createdUser.setEmail(username);
                        createdUser.setPassword(password);
                    },
                    () -> Snackbar.make(this.password, getResources().getString(R.string.created_user), Snackbar.LENGTH_LONG).show(),
                    error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Snackbar.make(
                    this.password,
                    getResources().getString(R.string.invalid_inputs),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
