package lk.ijse.gdse.controller;

import com.google.zxing.WriterException;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.Attendance;
import lk.ijse.gdse.dto.Employee;
import lk.ijse.gdse.dto.tm.AttendanceTM;
import lk.ijse.gdse.dto.tm.EmployeeTM;
import lk.ijse.gdse.dto.tm.InventoryTM;
import lk.ijse.gdse.dto.tm.MarkAttendanceTM;
import lk.ijse.gdse.model.AttendanceModel;
import lk.ijse.gdse.model.EmployeeModel;
import lk.ijse.gdse.qr.QRGenerator;
import lk.ijse.gdse.util.RegExPatterns;
import lk.ijse.gdse.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;


public class EmployeeFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton searchCancelBtn;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtEmployeeId;

    @FXML
    private JFXTextField txtJobRole;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colJobRole;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableView<MarkAttendanceTM> tblMarkAttendence;

    @FXML
    private TableColumn<?, String> colEmpId;

    @FXML
    private TableColumn<?, ?> colEmpName;

    @FXML
    private TableColumn<?, ?> colJob;

    @FXML
    private TableColumn<?, String> colStatus;

    @FXML
    private Label lblDate;

    @FXML
    private TableView<AttendanceTM> tblAttendance;

    @FXML
    private TableColumn<?, ?> employeeCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> jobCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private JFXDatePicker DatePicker;

    @FXML
    private JFXComboBox<String> cmbEmployee;

    @FXML
    private JFXComboBox<String> cmbQr;

    @FXML
    private JFXButton btnMarkAttendance;

    @FXML
    private JFXButton btnScanQR;

    ObservableList<EmployeeTM> data = FXCollections.observableArrayList();

    ObservableList<MarkAttendanceTM> employees = FXCollections.observableArrayList();

    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = EmployeeModel.findIfExists(txtEmployeeId.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
        if (!isExists) {
            TxtColours.setDefaultColours(txtEmployeeId);
            if (!(txtEmployeeId.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty() || txtJobRole.getText().isEmpty() || txtSalary.getText().isEmpty())) {
                if (RegExPatterns.getEmployeeId().matcher(txtEmployeeId.getText()).matches()) {
                    TxtColours.setDefaultColours(txtEmployeeId);
                    if (RegExPatterns.getNamePattern().matcher(txtName.getText()).matches()) {
                        TxtColours.setDefaultColours(txtName);
                        if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                            TxtColours.setDefaultColours(txtContact);
                            if (RegExPatterns.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                                TxtColours.setDefaultColours(txtSalary);

                                String id = txtEmployeeId.getText();
                                String name = txtName.getText();
                                String address = txtAddress.getText();
                                String contact = txtContact.getText();
                                String jobRole = txtJobRole.getText();
                                Double salary = Double.valueOf(txtSalary.getText());

                                try {
                                    boolean isSaved = EmployeeModel.addEmployee(new Employee(id, name, address, contact, jobRole, salary));
                                    if (isSaved) {
                                        new Alert(Alert.AlertType.CONFIRMATION, "Employee saved!").show();
                                        clearTextFields();
                                        populateEmployeeTable();
                                        searchFilter();
                                    } else {
                                        new Alert(Alert.AlertType.WARNING, "Employee not saved!").show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                                }
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Please enter double value for salary.").show();
                                TxtColours.setErrorColours(txtSalary);
                            }
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Invalid contact number").show();
                            TxtColours.setErrorColours(txtContact);
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Invalid name.").show();
                        TxtColours.setErrorColours(txtName);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid employeeId.").show();
                    TxtColours.setErrorColours(txtEmployeeId);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Please fill all the details.").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Employee Id is already exists!").show();
            TxtColours.setErrorColours(txtEmployeeId);
        }
    }

    private void clearTextFields() {
        txtEmployeeId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtSalary.setText("");
        txtJobRole.setText("");
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {

        if (!txtEmployeeId.getText().isEmpty()) {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                String id = txtEmployeeId.getText();

                try {
                    boolean isDeleted = EmployeeModel.deleteEmployee(id);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Employee has deleted!").show();
                        clearTextFields();
                        populateEmployeeTable();
                        searchFilter();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Employee has not deleted!").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please enter employeeId.").show();
        }
    }

    @FXML
    void searchCancelBtnOnAction(ActionEvent event) {
        txtSearch.setText("");
        searchCancelBtn.setVisible(false);
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {

        if (!(txtEmployeeId.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty() || txtJobRole.getText().isEmpty() || txtSalary.getText().isEmpty())) {
            if (RegExPatterns.getEmployeeId().matcher(txtEmployeeId.getText()).matches()) {
                TxtColours.setDefaultColours(txtEmployeeId);
                if (RegExPatterns.getNamePattern().matcher(txtName.getText()).matches()) {
                    TxtColours.setDefaultColours(txtName);
                    if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                        TxtColours.setDefaultColours(txtContact);
                        if (RegExPatterns.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                            TxtColours.setDefaultColours(txtSalary);

                            String id = txtEmployeeId.getText();
                            String name = txtName.getText();
                            String address = txtAddress.getText();
                            String contact = txtContact.getText();
                            String jobRole = txtJobRole.getText();
                            Double salary = Double.valueOf(txtSalary.getText());

                            try {
                                boolean isUpdated = EmployeeModel.updateEmployee(new Employee(id, name, address, contact, jobRole, salary));
                                if (isUpdated) {
                                    new Alert(Alert.AlertType.CONFIRMATION, "Employee details updated!").show();
                                    clearTextFields();
                                    populateEmployeeTable();
                                    searchFilter();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Employee details not updated!").show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                            }
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Please enter double value for salary.").show();
                            TxtColours.setErrorColours(txtSalary);
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Invalid contact number").show();
                        TxtColours.setErrorColours(txtContact);
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid name.").show();
                    TxtColours.setErrorColours(txtName);
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid employeeId.").show();
                TxtColours.setErrorColours(txtEmployeeId);
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please fill all the details.").show();
        }
    }

    @FXML
    void markAttendenceOnAction(ActionEvent event) {
        boolean isMarked = false;
        try {
            isMarked = AttendanceModel.findExists(LocalDate.now());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (!isMarked) {
            ObservableList<Attendance> attendances = FXCollections.observableArrayList();
            for (int i = 0; i < tblMarkAttendence.getItems().size(); i++) {
                String data = colStatus.getCellData(i);
                String date = lblDate.getText();
                String employeeId = colEmpId.getCellData(i);
                try {
                    String attendanceId = AttendanceModel.generateId();
                    if (data.equals("Present")) {
                        attendances.add(new Attendance(attendanceId, date, "Present", employeeId));
                    } else {
                        attendances.add(new Attendance(attendanceId, date, "Absent", employeeId));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }

            try {
                boolean isSaved = AttendanceModel.saveAttendance(attendances);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Attendance saved successfully!").show();
                    populateMarkAttendenceTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Attendence not saved!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Attendance already marked!").show();
        }
    }

    @FXML
    void reportBtnOnAction(ActionEvent event) {
        String name = cmbEmployee.getSelectionModel().getSelectedItem();
        if (name != null) {
            try {
                String id = EmployeeModel.getEmployeeId(name);
                JasperDesign design = JRXmlLoader.load("F:\\1st semester final project\\moods salon\\src\\main\\java\\lk\\ijse\\gdse\\report\\AttendanceReport.jrxml");
                JasperReport report = JasperCompileManager.compileReport(design);
                HashMap<String, Object> map = new HashMap();
                map.put("EmployeeId", id);
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (SQLException | JRException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select an employee").show();
        }
    }

    @FXML
    void DatePickerOnAction(ActionEvent event) {
        String date = DatePicker.getValue().toString();
        try {
            ObservableList<AttendanceTM> attendance = AttendanceModel.getAttendance(date);
            tblAttendance.setItems(attendance);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    @FXML
    void getQRBtnOnAction(ActionEvent event) {
        if (cmbQr.getSelectionModel().getSelectedItem() != null) {
            String name = cmbQr.getSelectionModel().getSelectedItem();
            try {
                String employeeId = EmployeeModel.getEmployeeId(name);
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select a folder");
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File selectedDirectory = directoryChooser.showDialog(cmbQr.getScene().getWindow());
                if (selectedDirectory != null) {
                    String filepath = selectedDirectory.getAbsolutePath()+"\\"+ employeeId + " - " + name + ".png";
                    QRGenerator.generateQrCode(employeeId, 1250, 1250, filepath);
                }

            } catch (SQLException | WriterException e) {
                e.printStackTrace();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select employee to generate QR.").show();
        }
    }

    @FXML
    void scanQRBtnOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QRScanForm.fxml"));
        stage.setScene(new Scene(loader.load()));
        QRScanFormController controller = loader.getController();
        controller.setEmployeeController(this);
        stage.setTitle("QR Scan");
        stage.getIcons().add(new Image("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\logo.png"));
        stage.centerOnScreen();
        stage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                controller.getService().cancel();
                btnScanQR.setDisable(false);
            }
        });
        stage.show();
        btnScanQR.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValueFactory();
        populateEmployeeTable();
        searchFilter();
        populateMarkAttendenceTable();
        loadDate();
        setComboBox();
    }

    public String findEmployee(String id){
        String name = null;
        try {
            name = EmployeeModel.findEmployee(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (name != null){
            return name;
        }
        return "Invalid QR code";
    }

    public void markAttendence(String id){
        for (MarkAttendanceTM attends : employees) {
            if (attends.getEmpId().equals(id)){
                attends.setStatus("Present");
            }
        }
        tblMarkAttendence.refresh();
    }

    private void setComboBox() {
        try {
            ObservableList<String> employees = EmployeeModel.getEmployeeNames();
            cmbEmployee.setItems(employees);
            cmbQr.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void loadDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(format.format(date));
    }

    private void populateMarkAttendenceTable() {
        try {
            boolean isMarked = AttendanceModel.findExists(LocalDate.now());
            if (!isMarked) {
                employees = EmployeeModel.getEmployees();
                tblMarkAttendence.setItems(employees);
            }else {
                ObservableList<MarkAttendanceTM> marked = AttendanceModel.getMarkedAttendence(LocalDate.now());
                tblMarkAttendence.setItems(marked);
                btnMarkAttendance.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setValueFactory() {
        //Employee details table
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));

        //Mark attendence table
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //view Attendance table
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        jobCol.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("attendance"));
    }

    private void searchFilter() {
        FilteredList<EmployeeTM> filteredData = new FilteredList<>(data, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(EmployeeTM -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    searchCancelBtn.setVisible(false);
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (EmployeeTM.getEmployeeId().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else if(EmployeeTM.getName().toLowerCase().contains(searchKeyword)){
                    searchCancelBtn.setVisible(true);
                    return true;
                }else
                    searchCancelBtn.setVisible(true);
                return false;

            });
        });

        SortedList<EmployeeTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblEmployee.comparatorProperty());
        tblEmployee.setItems(sortedData);
    }

    private void populateEmployeeTable() {
        try {
            ResultSet rs = EmployeeModel.getAllEmployees();
            data.clear();
            while (rs.next()){
                JFXButton button = new JFXButton("edit",new ImageView("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditbtnOnAction(button);
                data.add(new EmployeeTM(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),button,rs.getString(5),rs.getString(6)));
            }
            tblEmployee.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setEditbtnOnAction(JFXButton button) {
        button.setOnAction((e) -> {
            int index = -1;
            for (int i=0; i<tblEmployee.getItems().size(); i++){
                if (colAction.getCellData(i).equals(button)){
                    index = i;
                }
            }
            EmployeeTM employee = tblEmployee.getItems().get(index);
            txtEmployeeId.setText(employee.getEmployeeId());
            txtName.setText(employee.getName());
            txtJobRole.setText(employee.getJobRole());
            txtContact.setText(employee.getContact());
            txtSalary.setText(String.valueOf(employee.getSalary()));
            txtAddress.setText(employee.getAddress());
        });
    }
}
