package com.cold.coldrefrigerationuser.BottomSheets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cold.coldrefrigerationuser.Model.User;
import com.cold.coldrefrigerationuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpBottomSheet extends BottomSheetDialogFragment {


   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      final View v = inflater.inflate(R.layout.sign_up_bottom_sheet, container, false);
      final Context context = v.getContext();

      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      EditText EmailET = v.findViewById(R.id.email_et);
      EditText PasswordET = v.findViewById(R.id.password_et);
      EditText NameET = v.findViewById(R.id.name_et);
      EditText PhoneET = v.findViewById(R.id.phone_et);
      CheckBox ShowPassCheck = v.findViewById(R.id.show_pass_check);
      TextView SignUpBtn = v.findViewById(R.id.sign_up_btn);

      ProgressDialog progressDialog = new ProgressDialog(context);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Creating account...");

      SignUpBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            if(NameET.getText().toString().isEmpty()){
               String snack = "Please provide a name.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(PhoneET.getText().toString().isEmpty()){
               String snack = "Please provide a phone number.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(PhoneET.getText().toString().length() != 10){
               String snack = "Please provide a 10 digit mobile number.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(EmailET.getText().toString().isEmpty()){
               String snack = "Email address can't be empty.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(EmailET.getText().toString().trim()).matches()){
               String snack = "Please enter a valid email address";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(PasswordET.getText().toString().isEmpty()){
               String snack = "Please provide a password.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            if(PasswordET.getText().toString().length() < 6){
               String snack = "Please provide a password of more than or equal to 6 characters.";
               Toast.makeText(context,snack,Toast.LENGTH_LONG).show();
               return;
            }

            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(EmailET.getText().toString().trim()
                    ,PasswordET.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                  if(task.isSuccessful()){

                     Objects.requireNonNull(firebaseAuth.getCurrentUser())
                             .sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){

                              User user = new User();
                              user.name = NameET.getText().toString().trim();
                              user.email = EmailET.getText().toString().trim();
                              user.phone = PhoneET.getText().toString().trim();

                              DocumentReference documentReference = firebaseFirestore.collection("Users")
                                      .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

                              documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {

                                    progressDialog.dismiss();
                                    Toast.makeText(context,"A verification link has been sent to your email." +
                                            " Please verify your account to login.",Toast.LENGTH_SHORT).show();

                                 }
                              }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                                 }
                              });

                           }

                           else {
                              Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                           }

                        }
                     });



                  }
                  else {
                     progressDialog.dismiss();
                     Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                  }

               }
            });


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



      return v;

   }


}
