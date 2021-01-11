package com.example.coldrefrigerationuser.Model;

public class User {

   public String email;
   public String address;
   public String name;
   public String phone;

   public User(){

   }

   public User(String email, String address, String name, String phone) {
      this.email = email;
      this.address = address;
      this.name = name;
      this.phone = phone;
   }

   public String getEmail() {
      return email;
   }

   public String getAddress() {
      return address;
   }

   public String getName() {
      return name;
   }

   public String getPhone() {
      return phone;
   }
}
