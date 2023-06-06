# Salon_Management_System

💠 Developed this JavaFX application as the first semester's final project for the GDSE program at IJSE.

## 💻 Features 
<ul>
      <li>Generating payment reciept.</li>
      <li>Send payment reciept to customer throught Gmail.</li>
      <li>Send appointment details to customer throught Gmail.</li>
      <li>Employee Attendance marked by scanning QR code.</li>
</ul>

## 🛠️ How to Setup This Project
<ul>
      <li>Create a MySQL database and execute queries in src/main/java/lk/ijse/gdse/db/script.sql using database client.</li>
      <li>Setup Database Connection on 13 line of src/main/java/lk/ijse/gdse/db/DBConnection.java</li>
      <li>Reload the pom.xml file.</li>
      <li>ADD this VM option for run configurations.</li>
      
      --module-path
      "PATH_TO_YOUR_SDK"
      --add-modules
      javafx.controls,javafx.fxml
      --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
      --add-exports=javafx.base/com.sun.javafx.binding=ALL-UNNAMED
      --add-exports=javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
      --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED
      
</ul>

Happy coding! 😊


