package com.example.rodrigoespinoza.fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rodrigoespinoza.fragmentos.fragments.LoginFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.RegistroFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

import java.lang.reflect.Array;


public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, RegistroFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener{

    Button btnDinamico, btnEstatico, btnNavegacion;
    Intent intent;


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    private GoogleApiClient googleApiClient;
    RegistroFragment registroFragment;
    LoginFragment loginFragment;
    MenuActivity menuActivity;
    Button btnOpenLoginFragment, btnOpenSigInFragment;
    SignInButton sign_in_button;

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se crean variables del mismo tipo BUTTON que tomaran el control de accion
        this.btnOpenLoginFragment = findViewById(R.id.btnOpenLoginFragment);
        this.btnOpenSigInFragment = findViewById(R.id.btnOpenSingInFragment);
        this.sign_in_button = findViewById(R.id.sign_in_button);

        // Estas variables actuaran una ves que sean oprimidas
        this.btnOpenLoginFragment.setOnClickListener(this);
        this.btnOpenSigInFragment.setOnClickListener(this);

        //Se importan los fragmentos o vistas
        this.registroFragment = new RegistroFragment();
        this.loginFragment = new LoginFragment();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso )
                .build();

        mAuth = FirebaseAuth.getInstance();
        // [END config_signin]
        this.sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //goMainScreen();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.com_facebook_smart_login_confirmation_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });

        fireAuthStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    goMainScreen();
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() == null){
            //goLoginScreen();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.containerFragment, this.loginFragment).commit();

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(fireAuthStateListener);
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 777){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResultGoogle(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResultGoogle(GoogleSignInResult result) {
        if(result.isSuccess()){
            //holi
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            String url = account.getPhotoUrl().toString();
            intent = new Intent(this, MenuActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("email",email);
            intent.putExtra("url", url);
            // FALTA PASAR ID USUARIO BIEN POR QUE DICE QUE NO ES UN INT :O
            startActivity(intent);


        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.btnOpenLoginFragment:
                fragmentTransaction.replace(R.id.containerFragment, this.loginFragment).commit();
                break;

            case R.id.btnOpenSingInFragment:
                fragmentTransaction.replace(R.id.containerFragment, this.registroFragment).commit();
                break;

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
