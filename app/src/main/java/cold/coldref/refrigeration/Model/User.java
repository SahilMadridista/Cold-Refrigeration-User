package cold.coldref.refrigeration.Model;

public class User {

   public String email;
   public String name;
   public String phone;

   public User(){

   }

   public User(String email, String name, String phone) {
      this.email = email;
      this.name = name;
      this.phone = phone;
   }

   public String getEmail() {
      return email;
   }


   public String getName() {
      return name;
   }

   public String getPhone() {
      return phone;
   }
}
