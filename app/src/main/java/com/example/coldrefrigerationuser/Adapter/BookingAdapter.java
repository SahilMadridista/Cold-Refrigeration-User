package com.example.coldrefrigerationuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coldrefrigerationuser.Model.Service;
import com.example.coldrefrigerationuser.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookingAdapter extends FirestoreRecyclerAdapter<Service, BookingAdapter.BookingViewHolder> {

   Context context;

   public BookingAdapter(@NonNull FirestoreRecyclerOptions<Service> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Service model) {

      holder.ServiceName.setText(model.getService_name());
      holder.ServicePrice.setText(model.getService_cost());
      holder.BookingDate.setText(model.getBooking_date());

   }

   @NonNull
   @Override
   public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_booking_item,
              parent, false);
      context = v.getContext();
      return new BookingViewHolder(v);
   }

   static class BookingViewHolder extends RecyclerView.ViewHolder{

      TextView ServiceName, ServicePrice, BookingDate;

      public BookingViewHolder(@NonNull View itemView) {
         super(itemView);

         ServiceName = itemView.findViewById(R.id.service_name_text);
         ServicePrice = itemView.findViewById(R.id.service_price_text);
         BookingDate = itemView.findViewById(R.id.service_booking_date_text);


      }
   }

}
