package com.example.rodrigoespinoza.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RegistroFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener{

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
    Button btnOpenLoginFragment, btnOpenSigInFragment;
    SignInButton sign_in_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        this.sign_in_button.setSize(SignInButton.SIZE_WIDE);
        this.sign_in_button.setColorScheme(SignInButton.COLOR_DARK);
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
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.com_facebook_smart_login_confirmation_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.com_facebook_smart_login_confirmation_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        fireAuthStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    createUserInDb(user);


                }
            }
        };

        getSupportFragmentManager().beginTransaction().add(R.id.containerFragment, this.loginFragment).commit();

    }

    private void createUserInDb(final FirebaseUser user) {
        String USER_TAG ="USER";
        final Map<String, String> params = new HashMap<>();
        params.put("name", user.getDisplayName());
        params.put("email", user.getEmail());
        params.put("id", user.getUid());
        params.put("url", user.getDisplayName());
        db.collection("user")
                .whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    final QuerySnapshot document = task.getResult();

                    if(document.getDocuments().size()==0){
                        db.collection("user").add(params).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Map<String, String> param_person = new HashMap<>();
                                param_person.put("user_id", documentReference.getId());
                                String person_id = db.collection("person").add(params).getResult().getId();
                                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("person_id", person_id);
                                editor.commit();
                                goMenuScreen(person_id);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });


                    }else{
                        db.collection("person").whereEqualTo("user_id", user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            if (task.isSuccessful()){
                                                String person_id = document.getDocuments().get(0).getId();
                                                goMenuScreen(person_id);
                                            }
                                        }
                                    }
                                });
                    }}
            }
        });


    }




    private void goLoginScreen() {
        // Funcion recursiva -> estas mandando al main de nuevo xd y no va a estar logeado...
        // y como no esta logeado al llegar al main voy a crear otro intent main y .. asi xd
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goMenuScreen(String person_id) {


        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("person_id", person_id);
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
            firebaseAuthWithGoogle(result.getSignInAccount());
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        }
        else{
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.com_facebook_smart_login_confirmation_cancel, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_firebase_auth, Toast.LENGTH_LONG).show();
                }
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fireAuthStateListener != null){
            mAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }
}