package preview.valteck.bortexapp.ui.login_and_registration_activities;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 8/16/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();

        // Get view references
        final EditText emailAddressEditText = (EditText) findViewById(R.id.email);
        EditText nameAndSurnameEditText = (EditText) findViewById(R.id.name);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        Button registerButton = (Button) findViewById(R.id.button_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        //set view functionality
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailAddressEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(emailAddress.isEmpty()|| password.isEmpty()){
                    showSnackBar("Email or Password is incorrect");
                }
                else{
                    registerWithEmailAndPassword(emailAddress, password);
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void registerWithEmailAndPassword(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Sign in success therefore sign in
                            showSnackBar("Sign in successful");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            showSnackBar("Sign in unsuccessful");
                        }
                    }
                });
    }

    /**
     * Show a message
     * @param message
     */
    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
