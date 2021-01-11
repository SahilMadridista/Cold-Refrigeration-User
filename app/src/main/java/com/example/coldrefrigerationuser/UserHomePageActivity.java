package com.example.coldrefrigerationuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

      Button SignOut = findViewById(R.id.sign_out);
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

   }
}