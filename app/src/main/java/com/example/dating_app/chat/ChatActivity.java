package com.example.dating_app.chat;

import static java.security.AccessController.getContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dating_app.R;
import com.example.dating_app.match.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;

    private RecyclerView.LayoutManager fiChatLayoutManager;

    private EditText mSendEditText;
    private ImageButton mBack;

    private ImageButton mSendButton;
    private String notification;

    private String currentUserID, matchId, chatId;
    private String matchName, matchGive, matchNeed, matchBudget, matchProfile;

    private String lastMessage, lastTimeStamp;
    private String message, createdByUser, isSeen, messageId, currentUserName;

    private Boolean currentUserBoolean;
    ValueEventListener seenListener;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        matchId = getIntent().getStringExtra("matchId");
        matchName = getIntent().getStringExtra("matchName");
        matchGive = getIntent().getStringExtra("matchGive");
        matchNeed = getIntent().getStringExtra("matchNeed");
        matchBudget = getIntent().getStringExtra("matchBudget");
        matchProfile = getIntent().getStringExtra("matchProfile");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chats");

        getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mBack = findViewById(R.id.back);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(i3 < i7) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        }
                    },100);
                }
            }
        });
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(ChatActivity.this, MatchesActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference( "Users").child(currentUserID);
        Map onChat = new HashMap<>();

        onChat.put("onChat", matchId);
        reference.updateChildren(onChat);

        DatabaseReference current = FirebaseDatabase.getInstance().getReference("Users")
            .child(matchId).child("connections").child("matches").child(currentUserID);

        Map lastSeen = new HashMap<>();
        lastSeen.put("lastSeen", "false");

        current.updateChildren(lastSeen);

    }

    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                if (dataSnapshot.exists()) {
                    messageId = null;
                    message = null;
                    createdByUser = null;
                    isSeen = null;
                    if (dataSnapshot.child("text").getValue() != null) {
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if (dataSnapshot.child("createdByUser").getValue() != null) {
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if (dataSnapshot.child("seen").getValue() != null) {
                        isSeen = dataSnapshot.child("seen").getValue().toString();
                    } else {
                        isSeen = "true";
                    }
                    messageId = dataSnapshot.getKey().toString();
                    if (message != null && createdByUser != null) {
                        currentUserBoolean = false;
                        if (createdByUser.equals(currentUserID)) {
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = null;
                        if (isSeen.equals("false")) {
                            if (!currentUserBoolean) {
                                isSeen = "true";
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat")
                                        .child(chatId).child(messageId);
                                Map seenInfo = new HashMap<>();
                                seenInfo.put("seen", "true");
                                reference.updateChildren(seenInfo);

                                newMessage = new ChatObject(message, currentUserBoolean, true);
                            } else {
                                newMessage = new ChatObject(message, currentUserBoolean, false);
                            }
                        }
                        else {
                            newMessage = new ChatObject(message, currentUserBoolean, true);
                            DatabaseReference usersInChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(matchId);
                            resultsChat.add(newMessage);
                            mChatAdapter.notifyDataSetChanged();
                            if(mRecyclerView.getAdapter().getItemCount() > 0)
                                mRecyclerView.smoothScrollToPosition(resultsChat.size() - 1);
                            else
                                Toast.makeText(ChatActivity.this, "Chat Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        Map onchat = new HashMap();

        onchat.put("onChat", "None");
        reference.updateChildren (onchat);
        super.onPause();
    }

    @Override
    protected void onStop() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        Map onchat = new HashMap();

        onchat.put("onChat", "None");
        reference.updateChildren (onchat);
        super.onStop();
    }

    private void seenMessage (final String text){
        DatabaseReference someMessage = FirebaseDatabase.getInstance().getReference("Users").child(matchId);
        someMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("onChat").exists()) {
                        if (dataSnapshot.child("notificationKey").exists())
                            notification = dataSnapshot.child("notificationKey").getValue().toString();
                        else
                            notification = "";
                        if (!dataSnapshot.child("onChat").getValue().toString().equals(currentUserID)) {
                            new SendNotification(text, "New message from: " + currentUserName, notification,
                                    "activityToBeOpened", "MatchesActivity");
                        }
                        else {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(currentUserID).child("connections").child("matches").child(matchId);
                            Map seenInfo = new HashMap();
                            seenInfo.put("lastSend", "false");
                            reference.updateChildren(seenInfo);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        TextView mMatchNameTextView = (TextView) findViewById(R.id.chatToolbar);
        mMatchNameTextView.setText(matchName);
        return true;
    }

    public void showProfile(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.item_profile, null);
        TextView name = (TextView) popupView.findViewById(R.id.name);
        ImageView image = (ImageView) popupView.findViewById(R.id.image);
        TextView budget = (TextView) popupView.findViewById(R.id.budget);
        ImageView mNeedImage = (ImageView) popupView.findViewById(R.id.needImage);
        ImageView mGiveImage = (ImageView) popupView.findViewById(R.id.giveImage);
        name.setText(matchName);
        budget.setText(matchBudget);

        //need Image
        if (matchNeed.equals("Netflix"))
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        else if (matchNeed.equals("Hulu"))
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        else if (matchNeed.equals("Vudu"))
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
        else if (matchNeed.equals("HBO Now"))
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        else if (matchNeed.equals("Youtube Originals"))
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        else
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));

        // Give Image
        if (matchNeed.equals("Netflix"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        else if (matchNeed.equals("Amazon Prime"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.amazon));
        else if (matchNeed.equals("Hulu"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        else if (matchNeed.equals("Vudu"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
        else if (matchNeed.equals("HBO Now"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        else if (matchNeed.equals("Youtube Originals"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        else
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));

        switch (matchProfile) {
            case "default":
                Glide.with(popupView.getContext()).load(R.drawable.profile).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(popupView.getContext()).load(matchProfile).into(image);
                break;
        }
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        hideSoftKeyBoard();

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
       /* popupWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });*/



    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.unMatch) {
            new AlertDialog.Builder(ChatActivity.this)
                    .setTitle("Unmatch")
                    .setMessage("Are you sure you want to unmatch?")
                    .setPositiveButton("Unmatch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMatch(matchId);
                            Intent intent = new Intent(ChatActivity.this, MatchesActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ChatActivity.this, "Unmatch successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Dismiss", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (item.getItemId() == R.id.viewProfile) {
            showProfile(findViewById(R.id.content));
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMatch(String matchId) {
        DatabaseReference matchId_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("matches").child(matchId);

        DatabaseReference userId_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId).child("connections").child("matches").child(currentUserID);

        DatabaseReference yeps_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId).child("connections").child("yeps").child(currentUserID);

        DatabaseReference yeps_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("yeps").child(matchId);

        DatabaseReference matchId_chat_dbReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatId);

        matchId_chat_dbReference.removeValue();
        matchId_in_UserId_dbReference.removeValue();
        userId_in_matchId_dbReference.removeValue();
        yeps_in_matchId_dbReference.removeValue();
        yeps_in_UserId_dbReference.removeValue();
    }

    private void sendMessage() {
        final String sendMessageText = mSendEditText.getText().toString();
        long now = System.currentTimeMillis();
        String timeStamp = Long.toString(now);

        if (!sendMessageText.isEmpty()) {
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map<String, String> newMessage = new HashMap<>();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessage.put("timeStamp", timeStamp);
            newMessage.put("seen", "false");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        dataSnapshot.child("name").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
                        lastMessage = sendMessageText;
                        lastTimeStamp = timeStamp;
                        updateLastMessage();
                        seenMessage(sendMessageText);
                        newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void updateLastMessage() {
        DatabaseReference currUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID)
                .child("connections").child("matches").child(matchId);

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId)
                .child("connections").child("matches").child(currentUserID);

        Map lastMessageMap = new HashMap<>();
        lastMessageMap.put("lastMessage", lastMessage);

        Map lastTimestampMap = new HashMap<>();
        lastTimestampMap.put("lastTimeStamp", lastTimeStamp);

        Map lastSeen = new HashMap<>();
        lastSeen.put("lastSeen", "true");

        currUserDb.updateChildren(lastSeen);
        currUserDb.updateChildren(lastMessageMap);
        currUserDb.updateChildren(lastTimestampMap);

        matchDb.updateChildren(lastMessageMap);
        matchDb.updateChildren(lastTimestampMap);
    }

    private List<ChatObject> resultsChat = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}
