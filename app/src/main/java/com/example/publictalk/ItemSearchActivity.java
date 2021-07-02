package com.example.publictalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static android.icu.util.ULocale.getKeywords;
import static android.icu.util.ULocale.minimizeSubtags;

public class  ItemSearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchEditText;

    private ImageView back_button;

    //database

    DatabaseReference reference;

    private GridView SearchGridView;

    //firebase


    GridView postGridView;
    ArrayList<PostModel> dataModelArrayList;
    private FirebaseAuth mAuth;

    String userID;

    FirebaseFirestore fStore;

    public static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        //database search section


        searchEditText = findViewById(R.id.txtSearch);
       // show soft keyboard
        searchEditText.requestFocus();

        SearchGridView = findViewById(R.id.post_search_grid);

        back_button = findViewById(R.id.back_item_search);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemSearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        postGridView = findViewById(R.id.post_home_recycler);
        dataModelArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // here we are calling a method
        // to load data in our list view.
//        loadDatainSearchGridView();

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO){

                    loadSaerchDataInGridView();
                    return true;
                }
                return false;
            }
        });






    }

    private void loadSaerchDataInGridView() {


    }
}














