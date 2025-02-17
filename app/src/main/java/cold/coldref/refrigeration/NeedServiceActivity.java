package cold.coldref.refrigeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import cold.coldref.refrigeration.Model.Service;

import com.coldref.refrigeration.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NeedServiceActivity extends AppCompatActivity {

   Spinner ServiceSpinner;
   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   TextView ServiceCost;
   EditText Address;
   final int UPI_PAYMENT = 0;
   String name, email, phone;
   String cost, worker_cost;
   int advance_amount;
   long time;
   ProgressDialog progressDialog;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_need_service);

      firebaseAuth = FirebaseAuth.getInstance();
      firebaseFirestore = FirebaseFirestore.getInstance();
      ServiceCost = findViewById(R.id.service_cost_text);
      Address = findViewById(R.id.address_et);
      Button PayAndBook = findViewById(R.id.pay_and_book);

      Intent i = getIntent();
      name = i.getStringExtra("name");
      phone = i.getStringExtra("phone");
      email = i.getStringExtra("email");

      progressDialog = new ProgressDialog(NeedServiceActivity.this);
      progressDialog.setCancelable(false);
      progressDialog.setTitle("Loading");
      progressDialog.setMessage("Just a moment...");

      ServiceSpinner = findViewById(R.id.service_spinner);
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
              R.array.loading, R.layout.spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      ServiceSpinner.setAdapter(adapter);

      firebaseFirestore.collection("Services").orderBy("service_name",
              Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {

            ArrayList<String> service_list = new ArrayList<>();

            for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
               service_list.add(documentSnapshot.getString("service_name"));
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item_without_padding, service_list);
            adapter.setDropDownViewResource( R.layout.spinner_item);
            ServiceSpinner.setAdapter(adapter);


         }
      });

      ServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            ServiceCost.setText(R.string.loading);
            String service = adapterView.getItemAtPosition(i).toString().trim();

            firebaseFirestore.collection("Services").whereEqualTo("service_name",service)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                  if (task.isSuccessful()) {

                     for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        cost = String.valueOf(Objects.requireNonNull(document.get("total_cost")));
                        worker_cost = String.valueOf(Objects.requireNonNull(document.get("worker_cost")));

                        assert cost != null;
                        ServiceCost.setText(cost);

                     }

                  } else {

                     Toast.makeText(getApplicationContext(),
                             Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();

                  }
               }
            });


         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
      });

      PayAndBook.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            // payUsingUPI(name,email,phone);
            storeInfoOfBooking();


         }
      });

   }

   /*private void payUsingUPI(String name, String email, String phone) {

      if(ServiceCost.getText().toString().trim().equals("Loading...")){
         Toast.makeText(getApplicationContext(),"Please wait for amount to load.",Toast.LENGTH_SHORT).show();
         return;
      }

      if(Address.getText().toString().trim().isEmpty()){
         Toast.makeText(getApplicationContext(),"Please enter your address.",Toast.LENGTH_SHORT).show();
         return;
      }

      Uri uri = Uri.parse("upi://pay").buildUpon()
              .appendQueryParameter("pa", "sahil91098singh-1@okaxis")
              .appendQueryParameter("pn", name)
              .appendQueryParameter("tn", name+phone)
              .appendQueryParameter("am", "a")
              .appendQueryParameter("cu", "INR")
              .build();

      Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
      upiPayIntent.setData(uri);

      Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

      if(null != chooser.resolveActivity(getPackageManager())) {
         startActivityForResult(chooser, UPI_PAYMENT);
      } else {
         Toast.makeText(getApplicationContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
      }

   }*/

   /*public static boolean isConnectionAvailable(Context context) {
      ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivityManager != null) {
         NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
         if (netInfo != null && netInfo.isConnected()
                 && netInfo.isConnectedOrConnecting()
                 && netInfo.isAvailable()) {
            return true;
         }
      }
      return false;
   }*/

   /*@Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      switch (requestCode) {
         case UPI_PAYMENT:
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
               if (data != null) {
                  String trxt = data.getStringExtra("response");
                  Log.d("UPI", "onActivityResult: " + trxt);
                  ArrayList<String> dataList = new ArrayList<>();
                  dataList.add(trxt);
                  upiPaymentDataOperation(dataList);
               } else {
                  Log.d("UPI", "onActivityResult: " + "Return data is null");
                  ArrayList<String> dataList = new ArrayList<>();
                  dataList.add("nothing");
                  upiPaymentDataOperation(dataList);
               }
            } else {
               Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
               ArrayList<String> dataList = new ArrayList<>();
               dataList.add("nothing");
               upiPaymentDataOperation(dataList);
            }
            break;
      }
   }*/

   /*private void upiPaymentDataOperation(ArrayList<String> dataList) {

      if (isConnectionAvailable(NeedServiceActivity.this)) {
         String str = dataList.get(0);
         Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
         String paymentCancel = "";
         if(str == null) str = "discard";
         String status = "";
         String approvalRefNo = "";
         String[] response = str.split("&");
         for (int i = 0; i < response.length; i++) {
            String[] equalStr = response[i].split("=");
            if(equalStr.length >= 2) {
               if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                  status = equalStr[1].toLowerCase();
               }
               else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                  approvalRefNo = equalStr[1];
               }
            }
            else {
               paymentCancel = "Payment cancelled by user.";
            }
         }

         if (status.equals("success")) {

            Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();

            storeInfoOfBooking();

         }
         else if("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(getApplicationContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
         }
         else {
            Toast.makeText(getApplicationContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
         }
      } else {
         Toast.makeText(getApplicationContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
      }
   }*/

   private void storeInfoOfBooking() {

      Calendar c = Calendar.getInstance();
      @SuppressLint("SimpleDateFormat")
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

      String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
      String date1 = sdf.format(c.getTime());

      Date d = null;
      try {
         d = sdf.parse(date1);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      assert d != null;
      time = d.getTime()/10000000;

      String address = Address.getText().toString().trim();

      Service service = new Service();

      service.service_name = ServiceSpinner.getSelectedItem().toString().trim();
      service.service_cost = cost;
      service.service_worker_cost = worker_cost;
      service.customer_name = name;
      service.customer_email = email;
      service.customer_phone = phone;
      service.customer_address = address;
      service.booking_date = date;
      service.booking_time = Integer.parseInt(String.valueOf(time));
      service.advance_amount = String.valueOf(advance_amount);
      service.work_status = "pending";
      service.worker_allotment_status = "no";
      service.worker_name = "";
      service.worker_email = "";
      service.worker_phone = "";

      progressDialog.show();

      DocumentReference documentReference = firebaseFirestore.collection("Bookings")
              .document(String.valueOf(System.currentTimeMillis())+email);

      documentReference.set(service).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {

            String message = getResources().getString(R.string.message);
            try {
               sendSMS(message,phone);
            } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
            }

         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

         }
      });


   }

   private void sendSMS(String message, String phone) throws UnsupportedEncodingException {

      String apiKey = "vnOaGc8URfFmKbpwdYuEBe76ijNVs3HrMqyk2zI1QX4TJWAtZ5E2I6Wb5HaBomxrCXZ9QTv4qKM3lcRt";
      String sendId = "FSTSMS";

      //important step...
      message = URLEncoder.encode(message, "UTF-8");
      String language = "english";

      String route = "p";

      String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId + "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + phone;

      StringRequest requestSms = new StringRequest(myUrl, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {

            JSONObject object = null;

            try {

               object = new JSONObject(response);
               String ret = object.getString("return");
               String reqId = object.getString("request_id");
               JSONArray dataArray = object.getJSONArray("message");
               String res = dataArray.getString(0);

               sendMsgToAdmin();


            } catch (JSONException e) {
               e.printStackTrace();

            }

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError volleyError) {

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();

         }
      });

      RequestQueue rQueue = Volley.newRequestQueue(NeedServiceActivity.this);
      rQueue.add(requestSms);


   }

   private void sendMsgToAdmin() {

      firebaseFirestore.collection("Members").whereEqualTo("designation",
              "ADMIN").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {

            ArrayList<String> admin_contacts = new ArrayList<>();

            for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
               admin_contacts.add(documentSnapshot.getString("phone"));
            }

            String apiKey = "vnOaGc8URfFmKbpwdYuEBe76ijNVs3HrMqyk2zI1QX4TJWAtZ5E2I6Wb5HaBomxrCXZ9QTv4qKM3lcRt";
            String sendId = "FSTSMS";

            //important step...

            String message = getResources().getString(R.string.admin_msg);

            try {
               message = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
            }

            String language = "english";
            String route = "p";

            for(int i = 0;i<admin_contacts.size();i++){

               String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId + "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + admin_contacts.get(i);

               StringRequest requestSms = new StringRequest(myUrl, new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {

                     JSONObject object = null;

                     try {

                        object = new JSONObject(response);
                        String ret = object.getString("return");
                        String reqId = object.getString("request_id");
                        JSONArray dataArray = object.getJSONArray("message");
                        String res = dataArray.getString(0);

                        Toast.makeText(getApplicationContext(), "Message: " + res, Toast.LENGTH_SHORT).show();

                        Toast.makeText(getApplicationContext(),"Booking successful. A worker will reach you soon.",Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();

                        NeedServiceActivity.super.onBackPressed();


                     } catch (JSONException e) {
                        e.printStackTrace();

                     }

                  }
               }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError volleyError) {

                     progressDialog.dismiss();
                     Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                  }
               });

               RequestQueue rQueue = Volley.newRequestQueue(NeedServiceActivity.this);
               rQueue.add(requestSms);

            }


         }
      });


   }


}