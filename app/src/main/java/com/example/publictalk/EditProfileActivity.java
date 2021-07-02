package com.example.publictalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    public static final String TAG = "TAG";


    TextView userName,userEmail;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;


    private FirebaseAuth mAuth;

    private FirebaseFirestore fStore;
    private String userID;

    private EditText public_name;

    private RelativeLayout msave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        public_name = findViewById(R.id.user_public_name);
        userName = findViewById(R.id.first_name_txt);
        profileImage = findViewById(R.id.profile_image);
        userEmail = findViewById(R.id.user_email);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        msave = findViewById(R.id.top_layout_edit_profile);

        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                GoogleSignInResult result=opr.get();
                handleSignInResult(result);
                GoogleSignInAccount account=result.getSignInAccount();

                final String userName_string = userName.getText().toString().trim();
                final String userEmail_string = userEmail.getText().toString().trim();
                final String userPhoto_string = account.getPhotoUrl().toString().trim();
                final String userPublic_name = public_name.getText().toString().trim();

                if (TextUtils.isDigitsOnly(userPublic_name)){
                    public_name.setError("Please add your user name");

                }else {

                    CollectionReference userref = fStore.collection("user");

                    Query query = userref.whereEqualTo("userPublicName",userPublic_name);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){

                                for (DocumentSnapshot documentSnapshot: task.getResult()){
                                    String user = documentSnapshot.getString("userPublicName");

                                    if (user.equals(userPublic_name)){
                                        public_name.setError("This user name is not available");
                                    }
                                }
                            }
                            if (task.getResult().size()==0){

                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("user").document(userID);
                                Map<String,Object> userData = new HashMap<>();
                                userData.put("userName",userName_string);
                                userData.put("userEmail",userEmail_string);
                                userData.put("userPhoto",userPhoto_string);
                                userData.put("userPublicName",userPublic_name);

                                documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Finish Up "+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Something wrong try again " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }
                        }
                    });
                }
            }
        });


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

    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
//            userId.setText(account.getId());

            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);

            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoMainActivity();
        }
    }

    private void gotoMainActivity() {

        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}