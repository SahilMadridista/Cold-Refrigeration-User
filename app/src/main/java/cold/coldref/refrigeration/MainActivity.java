package cold.coldref.refrigeration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import cold.coldref.refrigeration.Consts.SharedPrefConsts;

import com.coldref.refrigeration.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
      final int loginStatus = preferences.getInt("login", SharedPrefConsts.NO_LOGIN);


      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {

            if(loginStatus == SharedPrefConsts.USER_LOGIN){

               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
               assert firebaseAuth.getCurrentUser() != null;
               String id = firebaseAuth.getCurrentUser().getUid();

               firebaseFirestore.collection("Users").document(id)
                       .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {

                     assert documentSnapshot != null;
                     String email = documentSnapshot.getString("email");
                     String name = documentSnapshot.getString("name");
                     String phone = documentSnapshot.getString("phone");
                     String address = documentSnapshot.getString("address");

                     Intent intent = new Intent(getApplicationContext(),UserHomePageActivity.class);
                     intent.putExtra("name",name);
                     intent.putExtra("email",email);
                     intent.putExtra("address",address);
                     intent.putExtra("phone",phone);
                     finish();
                     startActivity(intent);

                  }
               });

            }

            else {

               new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {

                     startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                     finish();

                  }
               },0);

            }

         }
      },1000);



   }
}