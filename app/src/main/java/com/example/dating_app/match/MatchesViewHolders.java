package com.example.dating_app.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dating_app.R;
import com.example.dating_app.chat.ChatActivity;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId, mMatchName, mLastTimeStamp, mLastMessage, mNeed, mGive, mBudget, mProfile;
    public ImageView mNotificationDot;
    public ImageView mMatchImage;

    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId=(TextView) itemView.findViewById(R.id.MatchId);
        mMatchName=itemView.findViewById(R.id.MatchName);
        mLastMessage=itemView.findViewById(R.id.lastMessage);
        mLastTimeStamp=itemView.findViewById(R.id.lastTimeStamp);

        mNeed=itemView.findViewById(R.id.needId);
        mGive=itemView.findViewById(R.id.giveId);
        mBudget=itemView.findViewById(R.id.budgetId);
        mMatchImage=itemView.findViewById(R.id.MatchImage);
        mProfile=itemView.findViewById(R.id.profileId);
        mNotificationDot=itemView.findViewById(R.id.notification_dot);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(v.getContext(), ChatActivity.class);
        Bundle b=new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        b.putString("matchName", mMatchName.getText().toString());
        b.putString("lastMessage", mLastMessage.getText().toString());
        b.putString("lastTimeStamp", mLastTimeStamp.getText().toString());
        b.putString("budget", mBudget.getText().toString());
        b.putString("need", mNeed.getText().toString());
        b.putString("give", mGive.getText().toString());
        b.putString("profile", mProfile.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
