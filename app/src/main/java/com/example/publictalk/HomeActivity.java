package com.example.publictalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.agrawalsuneet.dotsloader.loaders.SlidingLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView search_home, home_logo_black, add_idea_icon;

    RecyclerView postRecyclerView;
    ArrayList<PostModel> postArrayList;
    FirebaseFirestore firebaseFirestore;


    PostAdapter postAdapter;

    private FirebaseAuth mAuth;

    String userID;

    FirebaseFirestore fStore;

    public static final String TAG = "TAG";

    private RelativeLayout searchLayout;

    private SlidingLoader slidingLoader;



    // slider part

    // creating variables for our adapter, array list,
    // firebase firestore and our sliderview.
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderClass> sliderDataArrayList;
    private SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // creating a new array list fr our array list.
        sliderDataArrayList = new ArrayList<>();

        // initializing our slider view and
        // firebase firestore instance.
        sliderView = findViewById(R.id.slider);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // calling our method to load images.
        loadImages();


        //start search view section

        searchLayout = findViewById(R.id.search_layout);

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ItemSearchActivity.class);
                startActivity(intent);
            }
        });



        //end search view section


        slidingLoader = findViewById(R.id.progress_bar);

        add_idea_icon = findViewById(R.id.add_idea_icon);

        add_idea_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });

        home_logo_black = findViewById(R.id.home_profile_image);

        home_logo_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });



        Typeface tf = Typeface.createFromAsset(getAssets(), "font/FredokaOne-Regular.ttf");
        TextView wordlogo = findViewById(R.id.wordlogo);
        wordlogo.setTypeface(tf);


        postRecyclerView = findViewById(R.id.post_home_recycler);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        postArrayList = new ArrayList<PostModel>();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        postAdapter = new PostAdapter(HomeActivity.this,postArrayList);


        // here we are calling a method 
        // to load data in our list view.
//        loadDatainGridView();
    }

    private void loadImages() {

        firebaseFirestore.collection("slider").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                    // after we get the data we are passing inside our object class.
                    SliderClass sliderData = documentSnapshot.toObject(SliderClass.class);
                    SliderClass model = new SliderClass();

                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImgUrl(sliderData.getImgUrl());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    sliderAdapter = new SliderAdapter(HomeActivity.this, sliderDataArrayList);

                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(sliderAdapter);

                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(3);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);

                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(HomeActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadDatainGridView() {

        // below line is use to get data from Firebase
        // firestore using collection in android.



        firebaseFirestore.collection("post").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            Log.e("Firestore err",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType()== DocumentChange.Type.ADDED){

                                postArrayList.add(dc.getDocument().toObject(PostModel.class));
                            }

                            postAdapter.notifyDataSetChanged();
                            slidingLoader.setVisibility(View.GONE);
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
//        postRecyclerView.setAdapter(postAdapter);

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = firebaseFirestore.collection("user").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    String userImageHome = documentSnapshot.getString("userPhoto");
                    Glide.with(getApplicationContext()).load(userImageHome).into(home_logo_black);

                }else {
                    Log.d(TAG, "No such document");
                }

            }
        });
    }
}