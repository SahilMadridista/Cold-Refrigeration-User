package com.example.coldrefrigerationuser.Model;

public class Service {

   public String service_name;
   public String service_cost;
   public String service_worker_cost;

   public String customer_name;
   public String customer_email;
   public String customer_phone;
   public String customer_address;

   public int booking_time;
   public String booking_date;

   public String advance_amount;

   public String worker_allotment_status;
   public String worker_name;
   public String worker_email;
   public String worker_phone;

   public Service(){

   }

   public Service(String service_name, String service_cost, String service_worker_cost, String customer_name,
                  String customer_email, String customer_phone, String customer_address,
                  int booking_time, String booking_date, String advance_amount,
                  String worker_allotment_status, String worker_name, String worker_email, String worker_phone) {
      this.service_name = service_name;
      this.service_cost = service_cost;
      this.service_worker_cost = service_worker_cost;
      this.customer_name = customer_name;
      this.customer_email = customer_email;
      this.customer_phone = customer_phone;
      this.customer_address = customer_address;
      this.booking_time = booking_time;
      this.booking_date = booking_date;
      this.advance_amount = advance_amount;
      this.worker_allotment_status = worker_allotment_status;
      this.worker_name = worker_name;
      this.worker_email = worker_email;
      this.worker_phone = worker_phone;
   }

   public String getService_name() {
      return service_name;
   }

   public String getService_cost() {
      return service_cost;
   }

   public String getService_worker_cost() {
      return service_worker_cost;
   }

   public String getCustomer_name() {
      return customer_name;
   }

   public String getCustomer_email() {
      return customer_email;
   }

   public String getCustomer_phone() {
      return customer_phone;
   }

   public String getCustomer_address() {
      return customer_address;
   }

   public int getBooking_time() {
      return booking_time;
   }

   public String getBooking_date() {
      return booking_date;
   }

   public String getAdvance_amount() {
      return advance_amount;
   }

   public String getWorker_allotment_status() {
      return worker_allotment_status;
   }

   public String getWorker_name() {
      return worker_name;
   }

   public String getWorker_email() {
      return worker_email;
   }

   public String getWorker_phone() {
      return worker_phone;
   }
}
