# Salon_Management_System

💠 Developed this JavaFX application as the first semester's final project for the GDSE program at IJSE.

## 💻 Features 
* Generating payment reciept.
* Send payment reciept to customer throught Gmail.
* Send appointment details to customer throught Gmail.
* Employee Attendance marked by scanning QR code.

## 🛠️ How to Setup This Project
* Create a MySQL database and execute queries in `src/main/java/lk/ijse/gdse/db/script.sql` using database client.
* Setup Database Connection on 13 line of `src/main/java/lk/ijse/gdse/db/DBConnection.java`
* Reload the `pom.xml` file.
* ADD this VM option for run configurations.
      
      --module-path
      "PATH_TO_YOUR_SDK"
      --add-modules
      javafx.controls,javafx.fxml
      --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
      --add-exports=javafx.base/com.sun.javafx.binding=ALL-UNNAMED
      --add-exports=javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
      --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED

Happy coding! 😊


