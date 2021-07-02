package com.example.publictalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    TextView userName,userEmail;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;



    private FirebaseAuth mAuth;

    private FirebaseFirestore fStore;
    private String userID;

    private EditText search_txt_profile;

    private RelativeLayout badge,edit_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_profile);

//        firebase

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        badge = findViewById(R.id.badge);
        edit_profile= findViewById(R.id.edit_profile);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                finish();
            }
        });


//        search_txt_profile = findViewById(R.id.search_edit_txt_profile);
//
//        search_txt_profile.requestFocus();




        userName = findViewById(R.id.user_name);
        profileImage = findViewById(R.id.profileImage);
        userEmail = findViewById(R.id.user_email);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
//            userId.setText(account.getId());

            final String userName_string = userName.getText().toString().trim();
            final String userEmail_string = userEmail.getText().toString().trim();
            final String userPhoto_string = account.getPhotoUrl().toString().trim();


            userID = mAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("user").document(userID);
            Map<String,Object> userData = new HashMap<>();
            userData.put("userName",userName_string);
            userData.put("userEmail",userEmail_string);
            userData.put("userPhoto",userPhoto_string);

            documentReference.set(userData);

            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);

            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoMainActivity();
        }
    }
    private void gotoMainActivity(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}