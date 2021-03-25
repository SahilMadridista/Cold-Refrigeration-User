package cold.coldref.refrigeration.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cold.coldref.refrigeration.Model.Service;
import com.coldref.refrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PreviousBookingAdapter extends FirestoreRecyclerAdapter<Service, PreviousBookingAdapter.PreviousBookingViewHolder> {

   Context context;

   public PreviousBookingAdapter(@NonNull FirestoreRecyclerOptions<Service> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull PreviousBookingViewHolder holder, int position, @NonNull Service model) {

      holder.ServiceName.setText(model.getService_name());
      holder.ServicePrice.setText(model.getService_cost());
      holder.BookingDate.setText(model.getBooking_date());
      holder.WorkerName.setText(model.getWorker_name());
      holder.WorkerPhone.setText(model.getWorker_phone());

   }

   @NonNull
   @Override
   public PreviousBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_previous_item,
              parent, false);
      context = v.getContext();
      return new PreviousBookingViewHolder(v);
   }

   static class PreviousBookingViewHolder extends RecyclerView.ViewHolder{

      TextView ServiceName, ServicePrice, BookingDate, WorkerName, WorkerPhone;

      public PreviousBookingViewHolder(@NonNull View itemView) {
         super(itemView);

         ServiceName = itemView.findViewById(R.id.service_name_text);
         ServicePrice = itemView.findViewById(R.id.service_price_text);
         BookingDate = itemView.findViewById(R.id.service_booking_date_text);
         WorkerName = itemView.findViewById(R.id.worker_name_text);
         WorkerPhone = itemView.findViewById(R.id.worker_contact_text);


      }
   }

}
