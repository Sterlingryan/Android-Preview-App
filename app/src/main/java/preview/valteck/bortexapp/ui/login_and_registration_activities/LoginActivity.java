package preview.valteck.bortexapp.ui.login_and_registration_activities;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final int GOOGLE_SIGN_IN_RC = 123;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallBackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create google sign in request
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        showSnackBar("Failed to Connect " + connectionResult.getErrorCode());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Facebook login build
        mCallBackManager = CallbackManager.Factory.create();

        // Get Firebase instance
        mAuth = FirebaseAuth.getInstance();

        // Get views references
        final AutoCompleteTextView emailAddressTextView = (AutoCompleteTextView) findViewById(R.id.email);
        final EditText passwordEditTextView = (EditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button registerButton = (Button) findViewById(R.id.register_button);
        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        LoginButton facebookSignInButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // Set views functionality
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailAddressTextView.getText().toString();
                String password = passwordEditTextView.getText().toString();

                if(emailAddress.isEmpty()|| password.isEmpty()){
                    showSnackBar("Email or Password is incorrect");
                }
                else{
                    signInWithEmailAndPassword(emailAddress, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        facebookSignInButton.setReadPermissions("email", "public_profile");
        facebookSignInButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showSnackBar("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                showSnackBar("Error signing in " + error.getMessage());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result from launching GoogleSignInAPI
        if(requestCode == GOOGLE_SIGN_IN_RC){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                // Google sign in was successful authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showSnackBar("Sign in failed");
            }
        }
        else {
            // Pass the result to Facebook SDK
            mCallBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Show a message
     * @param message
     */
    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void signInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Sign in successful
                            showSnackBar("Sign in successful");
                            goToMainActivity();
                        }
                        else {
                            // Sign in unsuccessful
                            showSnackBar("Sign in unsuccessful");
                        }
                    }
                });
    }

    private void signInWithGoogle(){
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN_RC);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            goToMainActivity();
                        } else {
                            showSnackBar("Authentication unsuccessful");
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            goToMainActivity();
                        }
                        else {
                            showSnackBar("Firebase Authentication Failed");
                        }
                    }
                });
    }

    /**
     *  Go to the main application
     */
    private void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

