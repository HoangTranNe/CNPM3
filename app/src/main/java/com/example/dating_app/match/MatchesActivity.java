package com.example.dating_app.match;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dating_app.MainActivity;
import com.example.dating_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private ImageButton mBack;
    private DatabaseReference current;
    private ValueEventListener listen;
    private HashMap<String, Integer> mList = new HashMap<>();
    private String currentUserId, mLastTimeStamp, mLastMessage, lastSeen;
    DatabaseReference mCurrentIdInsideMatchConnections, mCheckLastSeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matches);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBack=findViewById(R.id.matchesBack);
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecycleView=findViewById(R.id.recycleView);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setHasFixedSize(true);
        mMatchesLayoutManager=new LinearLayoutManager(MatchesActivity.this);
        mRecycleView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter= new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecycleView.setAdapter(mMatchesAdapter);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MatchesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        getUserMatchId();
        mLastMessage=mLastTimeStamp=lastSeen="";
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    private void getLastMessageInfo(DatabaseReference userDb){
        mCurrentIdInsideMatchConnections=userDb.child("connections").child("matches").child(currentUserId);

        mCurrentIdInsideMatchConnections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("lastMessage").getValue() != null && snapshot.child("lastTimeStamp")
                            .getValue() != null && snapshot.child("lastSeen").getValue() != null){
                        mLastMessage=snapshot.child("lastMessage").getValue().toString();
                        mLastTimeStamp=snapshot.child("lastTimeStamp").getValue().toString();
                        lastSeen=snapshot.child("lastSeen").getValue().toString();
                    } else {
                        mLastMessage = "Start Chatting one";
                        mLastTimeStamp= " ";
                        lastSeen= "true";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserMatchId(){
        Query sortMatchesByTimeStamp = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserId).child("matches")
                .orderByChild("lastTimeStamp");

        sortMatchesByTimeStamp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey(), match.child("Chatid").toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key, String chatid) {
        DatabaseReference userDb=FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        getLastMessageInfo(userDb);

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userId=snapshot.getKey();
                    String name="";
                    String profileImageUrl="";
                    String need="";
                    String give="";
                    String budget="";
                    final String lastMessage="";
                    String lastTimeStamp="";

                    if (snapshot.child("name").getValue() != null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if (snapshot.child("profileImageUrl").getValue() != null){
                        profileImageUrl=snapshot.child("profileImageUrl").getValue().toString();
                    }
                    if (snapshot.child("need").getValue() != null){
                        need=snapshot.child("need").getValue().toString();
                    }
                    if (snapshot.child("give").getValue() != null){
                        give=snapshot.child("give").getValue().toString();
                    }
                    if (snapshot.child("budget").getValue() != null){
                        budget=snapshot.child("budget").getValue().toString();
                    }

                    String milliSec= mLastTimeStamp;
                    Long now;

                    try {
                        now=Long.parseLong(milliSec);
                        lastTimeStamp=convertMilliToRelative(now);
                        String[] arrOfStr=lastTimeStamp.split(",");
                        mLastTimeStamp=arrOfStr[0];
                    } catch (Exception e){ }

                    MatchesObject obj=new MatchesObject(userId,name,profileImageUrl,need,give,budget
                    , mLastMessage, mLastTimeStamp, chatid, lastMessage);
                    if (mList.containsKey(chatid)){
                        int key=mList.get(chatid);
                        resultsMatches.set(resultsMatches.size() - key, obj);
                    } else {
                        resultsMatches.add(0, obj);
                        mList.put(chatid, resultsMatches.size());
                    }
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String convertMilliToRelative(Long now) {
        String time= DateUtils.getRelativeDateTimeString(this, now, DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        return time;
    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<>();
    private List<MatchesObject> getDataSetMatches(){
        return resultsMatches;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        return;
    }
}