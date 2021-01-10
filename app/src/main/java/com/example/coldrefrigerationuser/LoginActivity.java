package com.example.coldrefrigerationuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

   EditText EmailET, PasswordET;
   TextView LoginBtn;
   CheckBox ShowPassCheck;
   private FirebaseAuth firebaseAuth;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      firebaseAuth = FirebaseAuth.getInstance();

      EmailET = findViewById(R.id.email_et);
      PasswordET = findViewById(R.id.password_et);

      LoginBtn = findViewById(R.id.login_btn);

      LoginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            checkEditTexts(EmailET.getText().toString().trim(),PasswordET.getText().toString().trim());

         }
      });

   }

   private void checkEditTexts(String email, String password) {

      if(email.isEmpty()){
         String snack = "Please provide an email address.";
         showSnack(snack);
         return;
      }

      if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
         String snack = "Please provide a valid email address.";
         showSnack(snack);
         return;
      }

      if(password.isEmpty()){
         String snack = "Please provide a password.";
         showSnack(snack);
         return;
      }

      loginUser(email,password);

   }

   private void loginUser(String email, String password) {

      ProgressDialog loadingDialog = new ProgressDialog(this);
      loadingDialog.setMessage("Logging in...");
      loadingDialog.setCancelable(false);

      firebaseAuth.signInWithEmailAndPassword(email,password)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                       assert firebaseAuth.getCurrentUser() != null;
                       String id = firebaseAuth.getCurrentUser().getUid();


                       // TODO - Code for login



                    }else {
                       Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                               Toast.LENGTH_LONG).show();
                       loadingDialog.dismiss();

                    }
                 }
              });

   }

   private void showSnack(String snack) {

      RelativeLayout Parent = findViewById(R.id.parent);
      Snackbar snackbar = Snackbar.make(Parent,snack,Snackbar.LENGTH_LONG);
      snackbar.show();

   }
}