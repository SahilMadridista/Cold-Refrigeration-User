package com.example.coldrefrigerationuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PendingBookingsActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pending_bookings);

      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      Intent i = getIntent();
      String email = i.getStringExtra("email");



   }
}