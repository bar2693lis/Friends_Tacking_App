package com.barlis.friendstacking;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_friend_activity);

        int friendPosition = getIntent().getIntExtra("friend", 0);

        final Friend friend = FriendManager.getInstance(this).getFriend(friendPosition);

        ImageView photoIv = findViewById(R.id.friend_photo_output);
        final TextView nameTv = findViewById(R.id.friend_name_output);
        TextView phoneTv = findViewById(R.id.friend_phone_output);
        TextView emailTv = findViewById(R.id.friend_email_output);
        TextView addressTv = findViewById(R.id.friend_address_output);
        TextView birthdayTv = findViewById(R.id.friend_birthday_output);
        TextView isBestTv = findViewById(R.id.is_best_friend_output);

        photoIv.setImageDrawable(Drawable.createFromPath(friend.getFile().getAbsolutePath()));
        nameTv.setText(friend.getName());
        birthdayTv.setText(friend.getBirthdayDate());

        if (friend.isBestFriend())
        {
            isBestTv.setText("Yes");
        }
        else
        {
            isBestTv.setText("No");
        }

        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + friend.getPhoneNumber()));
                startActivity(intent);
            }
        });

        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {friend.getEmail()});
                intent.setType("text/html");
                startActivity(intent);
            }
        });

        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("waze://q=" + friend.getAddress()));
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://id=com.waze"));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

