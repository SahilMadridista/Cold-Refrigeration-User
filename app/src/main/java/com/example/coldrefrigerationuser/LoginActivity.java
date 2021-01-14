package com.example.coldrefrigerationuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldrefrigerationuser.BottomSheets.SignUpBottomSheet;
import com.example.coldrefrigerationuser.Consts.SharedPrefConsts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

   EditText EmailET, PasswordET;
   TextView LoginBtn;
   CheckBox ShowPassCheck;
   private FirebaseAuth firebaseAuth;
   private FirebaseFirestore firebaseFirestore;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      /*System.out.println("------------------------------------------------");
      Calendar c = Calendar.getInstance();

      @SuppressLint("SimpleDateFormat")
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

      String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());

      System.out.println(date);*/

      firebaseAuth = FirebaseAuth.getInstance();
      firebaseFirestore = FirebaseFirestore.getInstance();
      ShowPassCheck = findViewById(R.id.show_pass_check);


      EmailET = findViewById(R.id.email_et);
      PasswordET = findViewById(R.id.password_et);

      LoginBtn = findViewById(R.id.login_btn);

      LoginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            checkEditTexts(EmailET.getText().toString().trim(),PasswordET.getText().toString().trim());

         }
      });

      ShowPassCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               PasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
               PasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
         }
      });

      TextView CreateAccText = findViewById(R.id.create_acc_text);
      CreateAccText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            SignUpBottomSheet signUpBottomSheet = new SignUpBottomSheet();
            signUpBottomSheet.show(getSupportFragmentManager(),"sign up bs");

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

      loadingDialog.show();

      firebaseAuth.signInWithEmailAndPassword(email,password)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                       assert firebaseAuth.getCurrentUser() != null;
                       String id = firebaseAuth.getCurrentUser().getUid();

                       if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){

                          firebaseFirestore.collection("Users").document(id)
                                  .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {

                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("login", SharedPrefConsts.USER_LOGIN);
                                editor.apply();

                                String email = documentSnapshot.getString("email");
                                String name = documentSnapshot.getString("name");
                                String phone = documentSnapshot.getString("phone");
                                String address = documentSnapshot.getString("address");

                                Intent intent = new Intent(getApplicationContext(),UserHomePageActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("email",email);
                                intent.putExtra("address",address);
                                intent.putExtra("phone",phone);
                                loadingDialog.dismiss();
                                finish();
                                startActivity(intent);


                             }
                          }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        e.getMessage(),Toast.LENGTH_LONG).show();
                             }
                          });

                       }

                       else{
                          Toast.makeText(LoginActivity.this,
                                  "Please verify your email first. You can login after verification.",
                                  Toast.LENGTH_LONG).show();
                          loadingDialog.dismiss();

                       }

                    }

                    else {
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