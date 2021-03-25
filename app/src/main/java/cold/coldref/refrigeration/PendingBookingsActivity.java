package cold.coldref.refrigeration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cold.coldref.refrigeration.Adapter.BookingAdapter;
import cold.coldref.refrigeration.Model.Service;

import com.coldref.refrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PendingBookingsActivity extends AppCompatActivity {

   com.airbnb.lottie.LottieAnimationView Loading;
   TextView Empty;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pending_bookings);

      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      Loading = findViewById(R.id.loading_animation);
      Empty = findViewById(R.id.empty_text);

      Intent i = getIntent();
      String email = i.getStringExtra("email");

      CollectionReference collectionReference = firebaseFirestore.collection("Bookings");

      Query query = collectionReference
              .whereEqualTo("customer_email",email)
              .whereEqualTo("work_status","pending");

      FirestoreRecyclerOptions<Service> options = new FirestoreRecyclerOptions.Builder<Service>()
              .setQuery(query, Service.class)
              .build();

      query.addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {

            if (error != null) {
               Log.d("TAG", error.getMessage());
               return;
            }

            assert querySnapshot != null;

            List<Service> list = querySnapshot.toObjects(Service.class);

            if(list.size() == 0){
               Loading.setVisibility(View.GONE);
               Empty.setVisibility(View.VISIBLE);
            }

            if(list.size()!=0){
               Loading.setVisibility(View.GONE);
               Empty.setVisibility(View.GONE);
            }

         }
      });

      RecyclerView recyclerView = findViewById(R.id.pending_r_view);
      BookingAdapter bookingAdapter = new BookingAdapter(options);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(bookingAdapter);
      bookingAdapter.startListening();


   }
}