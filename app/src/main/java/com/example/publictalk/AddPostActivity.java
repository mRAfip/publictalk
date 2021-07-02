package com.example.publictalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fevziomurtekin.customprogress.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karacce.buttom.Buttom;

import java.util.HashMap;
import java.util.Map;

import static com.fevziomurtekin.customprogress.Type.FLICKR;

public class AddPostActivity extends AppCompatActivity {


    public static final String TAG = "TAG";

    private EditText category_topic,post_heading, post_detail;

    private ProgressBar progressBar;

    private Buttom post_button;

    private FirebaseAuth mAuth;

    private TextView user_name_add_post,photo_url_addpost_dummy;
    private ImageView user_image_add_post;

    FirebaseFirestore fStore;
    String userID;

    String timestamp;




    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        user_name_add_post = findViewById(R.id.user_name_add_post);
        photo_url_addpost_dummy = findViewById(R.id.photo_url_addpost_dummy);
        user_image_add_post = findViewById(R.id.profile_image_add_post);

        category_topic = findViewById(R.id.category_topic);
        post_heading = findViewById(R.id.post_heading_txt);
        post_detail = findViewById(R.id.post_deatil_txt);

        post_button = findViewById(R.id.post_button);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

      // show soft keyboard
        category_topic.requestFocus();


        Typeface add_post_page_head = Typeface.createFromAsset(getAssets(), "font/Poppins-Regular.ttf");
        TextView add_post_page_head_tv = findViewById(R.id.add_post_page_head);
        add_post_page_head_tv.setTypeface(add_post_page_head);

//        Typeface idea_head = Typeface.createFromAsset(getAssets(), "font/Poppins-Regular.ttf");
//        TextView idea_head_tv = findViewById(R.id.idea_head);
//        idea_head_tv.setTypeface(idea_head);

        Typeface category_heading = Typeface.createFromAsset(getAssets(), "font/Poppins-Regular.ttf");
        TextView category_heading_tv = findViewById(R.id.category_heading);
        category_heading_tv.setTypeface(category_heading);


        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String topicName = category_topic.getText().toString().trim();
                final String topicHead = post_heading.getText().toString().trim();
                final String topicDetail = post_detail.getText().toString().trim();
                final String userNameAddPost = user_name_add_post.getText().toString().trim();
                final String userImageUrlAddPost = photo_url_addpost_dummy.getText().toString().trim();


                if (TextUtils.isDigitsOnly(topicName)){
                    category_topic.setError("What is the category of your posts, like social media or product like chocolate.");
                    return;
                }

                if (TextUtils.isDigitsOnly(topicHead)){
                    category_topic.setError("Add your post heading.");
                    return;
                }

                if (topicHead.length() >75 ){

                    post_heading.setError("You Are exceeding your limit heading must be below 50 characters.");
                }

               String timestamp = Timestamp.now().toString();

                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference1 = fStore.collection("post").document(timestamp);
                Map<String,Object> data = new HashMap<>();
                data.put("topicName",topicName);
                data.put("postHead",topicHead);
                data.put("postDetail",topicDetail);
                data.put("userPublicName",userNameAddPost);
                data.put("userPhoto",userImageUrlAddPost);

                documentReference1.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = fStore.collection("user").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){


                            String userPostName = documentSnapshot.getString("userPublicName");
                            String userImageAddPost = documentSnapshot.getString("userPhoto");
                            String userImageUrlDummy = documentSnapshot.getString("userPhoto");

                            user_name_add_post.setText(userPostName);
                            photo_url_addpost_dummy.setText(userImageUrlDummy);
                            Glide.with(getApplicationContext()).load(userImageAddPost).into(user_image_add_post);
                }else {
                    Log.d(TAG, "No such document");
                }

            }
        });
    }
}