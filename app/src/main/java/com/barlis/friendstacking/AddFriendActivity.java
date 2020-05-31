package com.barlis.friendstacking;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Calendar;

public class AddFriendActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bitmap;
    EditText nameEt, phoneEt, emailEt, addressEt;
    CheckBox isBestCb;
    TextView dateTv, takePhotoTv;

    final int CAMERA_REQUEST = 1;
    Button takePicBtn;
    final int WRITE_PERMISSION_REQUEST = 2;
    private final int READ_PERMISSION_REQUEST = 3;

    File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity);

        imageView = findViewById(R.id.friend_photo_result);

        nameEt = findViewById(R.id.friend_name_input);
        phoneEt = findViewById(R.id.friend_phone_input);
        emailEt = findViewById(R.id.friend_email_input);
        addressEt = findViewById(R.id.friend_address_input);

        isBestCb = findViewById(R.id.friend_checkbox);

        dateTv = findViewById(R.id.birth_date_output);
        takePhotoTv = findViewById(R.id.take_photo_output);

        takePicBtn = findViewById(R.id.pic_btn);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New photo");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
                file = new File(Environment.getExternalStorageDirectory(), phoneEt.getText().toString() + ".jpg");
                Uri fileUri = FileProvider.getUriForFile(v.getContext(), "com.example.whosmyfriend.provider", file);
                //Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_REQUEST);
            }
            if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        }

        Button dateBtn = findViewById(R.id.birth_date_btn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(AddFriendActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateTv.setText(dayOfMonth + " / " + (month+1) + " / " + year);
                    }
                },year,month,day);
                dpd.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_save:

                if (nameEt.getText().toString().matches("") || phoneEt.getText().toString().matches("") || addressEt.getText().toString().matches("") || emailEt.getText().toString().matches("") || dateTv.getText().toString().matches("") || file == null)
                {
                    Toast.makeText(AddFriendActivity.this, "Unable to save because some data is missing", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm").setMessage("Are you sure you want to save your friend?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Friend friend = new Friend(nameEt.getText().toString(), phoneEt.getText().toString(), addressEt.getText().toString(), emailEt.getText().toString(), dateTv.getText().toString(), isBestCb.isChecked(), file);
                            friend.setPhoto(bitmap);
                            FriendManager manager = FriendManager.getInstance(AddFriendActivity.this);
                            manager.addFriend(friend);

                            imageView.setImageBitmap(null);
                            nameEt.setText("");
                            phoneEt.setText("");
                            addressEt.setText("");
                            emailEt.setText("");
                            isBestCb.setChecked(false);
                            dateTv.setText("");
                            Toast.makeText(AddFriendActivity.this, "Your friend has been successfully saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AddFriendActivity.this, "Friend not saved", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                }
                break;

            case R.id.action_back:
                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == WRITE_PERMISSION_REQUEST)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Cant take picture", Toast.LENGTH_SHORT).show();
            }
            else
            {
                takePicBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            imageView.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
            bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getPath()), 200, 200);
            takePhotoTv.setText("Image taken successfully");
        }
    }
}

