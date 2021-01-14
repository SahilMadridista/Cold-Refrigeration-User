package com.example.coldrefrigerationuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coldrefrigerationuser.Consts.SharedPrefConsts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHomePageActivity extends AppCompatActivity {

   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;

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

   }
}