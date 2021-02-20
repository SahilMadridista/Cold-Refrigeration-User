package com.cold.coldrefrigerationuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cold.coldrefrigerationuser.Consts.SharedPrefConsts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHomePageActivity extends AppCompatActivity {

   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   private static final int REQUEST_PHONE_CALL = 1;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_user_home_page);

      firebaseAuth = FirebaseAuth.getInstance();
      firebaseFirestore = FirebaseFirestore.getInstance();

      Intent i = getIntent();
      TextView NameText = findViewById(R.id.name_text);
      TextView PhoneText = findViewById(R.id.phone_text);
      TextView EmailText = findViewById(R.id.email_text);

      NameText.setText(i.getStringExtra("name"));
      PhoneText.setText(i.getStringExtra("phone"));
      EmailText.setText(i.getStringExtra("email"));



      ImageView SignOut = findViewById(R.id.sign_out);
      SignOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("login", SharedPrefConsts.NO_LOGIN);
            editor.apply();

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

         }
      });


      CardView NeedServiceCard = findViewById(R.id.need_service_card);
      NeedServiceCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),NeedServiceActivity.class);
            intent.putExtra("name",i.getStringExtra("name"));
            intent.putExtra("email",i.getStringExtra("email"));
            intent.putExtra("phone",i.getStringExtra("phone"));
            startActivity(intent);


         }
      });

      CardView PendingCard = findViewById(R.id.pending_card);
      PendingCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),PendingBookingsActivity.class);
            intent.putExtra("email",i.getStringExtra("email"));
            startActivity(intent);

         }
      });

      CardView PreviousCard = findViewById(R.id.previous_card);
      PreviousCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),PreviousBookingActivity.class);
            intent.putExtra("email",i.getStringExtra("email"));
            startActivity(intent);

         }
      });

      CardView ContactCard = findViewById(R.id.contact_card);
      ContactCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {


            String[] items = {"Call", "Email"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(UserHomePageActivity.this);
            dialog.setTitle("Select one");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int which) {
                  if (which == 0) {

                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9621898141"));
                     if (ContextCompat.checkSelfPermission(UserHomePageActivity.this, Manifest.permission.CALL_PHONE)
                             != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UserHomePageActivity
                                .this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                     }
                     else
                     {
                        startActivity(intent);
                     }

                  }

                  if (which == 1) {

                     Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                             "mailto","coldrefrigeration30@gmail.com", null));
                     startActivity(Intent.createChooser(emailIntent, "Send email..."));

                  }
               }
            });
            dialog.create().show();


         }
      });

   }
}