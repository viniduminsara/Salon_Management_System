package lk.ijse.moods_salon.controller;

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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lk.ijse.moods_salon.bo.custom.EmployeeBO;
import lk.ijse.moods_salon.bo.factory.BOFactory;
import lk.ijse.moods_salon.bo.factory.BOTypes;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.AttendanceDTO;
import lk.ijse.moods_salon.dto.EmployeeDTO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.dto.tm.EmployeeTM;
import lk.ijse.moods_salon.qr.QRGenerator;
import lk.ijse.moods_salon.util.RegExPatterns;
import lk.ijse.moods_salon.util.SystemAlert;
import lk.ijse.moods_salon.util.TxtColours;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


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
    private TableView<AttendanceTM> tblMarkAttendence;

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

    @FXML
    private Label lblError;

    ObservableList<EmployeeTM> data = FXCollections.observableArrayList();

    ObservableList<AttendanceTM> employees = FXCollections.observableArrayList();

    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBO(BOTypes.EMPLOYEE);

    @FXML
    void addBtnOnAction(ActionEvent event) {
        boolean isExists = false;
        try {
            isExists = employeeBO.existsEmployee(txtEmployeeId.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }

        if (!(txtEmployeeId.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty() || txtJobRole.getText().isEmpty() || txtSalary.getText().isEmpty())) {
            if (RegExPatterns.getEmployeeId().matcher(txtEmployeeId.getText()).matches()) {
                TxtColours.setDefaultColours(txtEmployeeId);
                if (RegExPatterns.getNamePattern().matcher(txtName.getText()).matches()) {
                    TxtColours.setDefaultColours(txtName);
                    if (RegExPatterns.getContactPattern().matcher(txtContact.getText()).matches()) {
                        TxtColours.setDefaultColours(txtContact);
                        TxtColours.setDefaultColours(txtAddress);
                        TxtColours.setDefaultColours(txtJobRole);
                        if (RegExPatterns.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                            TxtColours.setDefaultColours(txtSalary);
                            if (!isExists) {
                                TxtColours.setDefaultColours(txtEmployeeId);
                                lblError.setText("");

                                String id = txtEmployeeId.getText();
                                String name = txtName.getText();
                                String address = txtAddress.getText();
                                String contact = txtContact.getText();
                                String jobRole = txtJobRole.getText();
                                Double salary = Double.valueOf(txtSalary.getText());

                                try {
                                    boolean isSaved = employeeBO.addEmployee(new EmployeeDTO(id, name, address, contact, jobRole, salary));
                                    if (isSaved) {
                                        new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Employee saved!",ButtonType.OK).show();
                                        clearTextFields();
                                        populateEmployeeTable();
                                        searchFilter();
                                    } else {
                                        new SystemAlert(Alert.AlertType.WARNING,"Warning","Employee not saved!",ButtonType.OK).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                                }
                            }else{
                                lblError.setText("Employee Id is already exists!");
                                TxtColours.setErrorColours(txtEmployeeId);
                            }
                        }else {
                            lblError.setText("Please enter double value for salary.");
                            TxtColours.setErrorColours(txtSalary);
                        }
                    }else {
                        lblError.setText("Invalid contact number");
                        TxtColours.setErrorColours(txtContact);
                    }
                }else {
                    lblError.setText("Invalid name.");
                    TxtColours.setErrorColours(txtName);
                }
            }else {
                lblError.setText("Invalid employeeId.");
                TxtColours.setErrorColours(txtEmployeeId);
            }
        }else {
            lblError.setText("Please fill all the details.");
            if (txtEmployeeId.getText().isEmpty()){
                TxtColours.setErrorColours(txtEmployeeId);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtContact.getText().isEmpty()){
                TxtColours.setErrorColours(txtContact);
            }
            if (txtAddress.getText().isEmpty()){
                TxtColours.setErrorColours(txtAddress);
            }
            if (txtJobRole.getText().isEmpty()){
                TxtColours.setErrorColours(txtJobRole);
            }
            if (txtSalary.getText().isEmpty()){
                TxtColours.setErrorColours(txtSalary);
            }
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
            if (RegExPatterns.getEmployeeId().matcher(txtEmployeeId.getText()).matches()) {
                boolean isExists = false;
                try {
                    isExists = employeeBO.existsEmployee(txtEmployeeId.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
                }
                if (isExists) {
                    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new SystemAlert(Alert.AlertType.INFORMATION, "Information", "Are you sure to delete ?", yes, no).showAndWait();

                    String id = txtEmployeeId.getText();

                    if (result.orElse(no) == yes) {
                        TxtColours.setDefaultColours(txtEmployeeId);
                        lblError.setText("");

                        try {
                            boolean isDeleted = employeeBO.deleteEmployee(id);
                            if (isDeleted) {
                                new SystemAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Employee has deleted!", ButtonType.OK).show();
                                clearTextFields();
                                populateEmployeeTable();
                                searchFilter();
                            } else {
                                new SystemAlert(Alert.AlertType.WARNING, "Warning", "Employee not deleted!", ButtonType.OK).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            new SystemAlert(Alert.AlertType.ERROR, "Error", "Something went wrong!", ButtonType.OK).show();
                        }
                    }
                }else {
                    lblError.setText("No Employee found.");
                    TxtColours.setErrorColours(txtEmployeeId);
                }
            }else {
                lblError.setText("Invalid employee Id");
                TxtColours.setErrorColours(txtEmployeeId);
            }
        }else {
            lblError.setText("Please enter employeeId.");
            TxtColours.setErrorColours(txtEmployeeId);
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
                        TxtColours.setDefaultColours(txtAddress);
                        TxtColours.setDefaultColours(txtJobRole);
                        if (RegExPatterns.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                            TxtColours.setDefaultColours(txtSalary);
                            lblError.setText("");

                            String id = txtEmployeeId.getText();
                            String name = txtName.getText();
                            String address = txtAddress.getText();
                            String contact = txtContact.getText();
                            String jobRole = txtJobRole.getText();
                            Double salary = Double.valueOf(txtSalary.getText());

                            try {
                                boolean isUpdated = employeeBO.updateEmployee(new EmployeeDTO(id, name, address, contact, jobRole, salary));
                                if (isUpdated) {
                                    new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Employee has updated!",ButtonType.OK).show();
                                    clearTextFields();
                                    populateEmployeeTable();
                                    searchFilter();
                                } else {
                                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Employee not updated!",ButtonType.OK).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                            }
                        }else {
                            lblError.setText("Please enter double value for salary.");
                            TxtColours.setErrorColours(txtSalary);
                        }
                    }else {
                        lblError.setText("Invalid contact number");
                        TxtColours.setErrorColours(txtContact);
                    }
                }else {
                    lblError.setText("Invalid name.");
                    TxtColours.setErrorColours(txtName);
                }
            }else {
                lblError.setText("Invalid employeeId.");
                TxtColours.setErrorColours(txtEmployeeId);
            }
        }else {
            lblError.setText("Please fill all the details.");
            if (txtEmployeeId.getText().isEmpty()){
                TxtColours.setErrorColours(txtEmployeeId);
            }
            if (txtName.getText().isEmpty()){
                TxtColours.setErrorColours(txtName);
            }
            if (txtContact.getText().isEmpty()){
                TxtColours.setErrorColours(txtContact);
            }
            if (txtAddress.getText().isEmpty()){
                TxtColours.setErrorColours(txtAddress);
            }
            if (txtJobRole.getText().isEmpty()){
                TxtColours.setErrorColours(txtJobRole);
            }
            if (txtSalary.getText().isEmpty()){
                TxtColours.setErrorColours(txtSalary);
            }
        }
    }

    @FXML
    void markAttendenceOnAction(ActionEvent event) {
        boolean isMarked = false;
        try {
            isMarked = employeeBO.existsAttendance(LocalDate.now());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        if (!isMarked) {
            ArrayList<AttendanceDTO> attendances = new ArrayList<>();
            for (int i = 0; i < tblMarkAttendence.getItems().size(); i++) {
                String data = colStatus.getCellData(i);
                String date = lblDate.getText();
                String employeeId = colEmpId.getCellData(i);
                try {
                    String attendanceId = employeeBO.generateAttendanceId();
                    if (data.equals("Present")) {
                        attendances.add(new AttendanceDTO(attendanceId, date, "Present", employeeId));
                    } else {
                        attendances.add(new AttendanceDTO(attendanceId, date, "Absent", employeeId));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
                }
            }

            try {
                boolean isSaved = employeeBO.saveAttendance(attendances);
                if (isSaved) {
                    new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Attendance saved successfully!",ButtonType.OK).show();
                    populateMarkAttendenceTable();
                } else {
                    new SystemAlert(Alert.AlertType.WARNING,"Warning","Attendence not saved!",ButtonType.OK).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new SystemAlert(Alert.AlertType.ERROR,"Error","Something went wrong!",ButtonType.OK).show();
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Attendance already marked!",ButtonType.OK).show();
        }
    }

    @FXML
    void reportBtnOnAction(ActionEvent event) {
        String name = cmbEmployee.getSelectionModel().getSelectedItem();
        if (name != null) {
            new Thread(() -> {
                try {
                    String id = employeeBO.getEmployeeId(name);
                    JasperDesign design = JRXmlLoader.load("src/main/java/lk/ijse/moods_salon/report/AttendanceReport.jrxml");
                    JasperReport report = JasperCompileManager.compileReport(design);
                    HashMap<String, Object> map = new HashMap();
                    map.put("EmployeeId", id);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }).start();
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select an employee",ButtonType.OK).show();
        }
    }

    @FXML
    void DatePickerOnAction(ActionEvent event) {
        String date = DatePicker.getValue().toString();
        try {
            ObservableList<AttendanceTM> attendance = employeeBO.getAttendanceOfDay(date);
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
                String employeeId = employeeBO.getEmployeeId(name);
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select a folder");
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File selectedDirectory = directoryChooser.showDialog(cmbQr.getScene().getWindow());
                if (selectedDirectory != null) {
                    String filepath = selectedDirectory.getAbsolutePath()+"\\"+ employeeId + " - " + name + ".png";
                    boolean isGenerated = QRGenerator.generateQrCode(employeeId, 1250, 1250, filepath);
                    if (isGenerated){
                        new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Successfully saved as '"+filepath+"'.", ButtonType.OK).show();
                    }
                }

            } catch (SQLException | WriterException e) {
                e.printStackTrace();
            }
        }else {
            new SystemAlert(Alert.AlertType.WARNING,"Warning","Please select employee to generate QR.",ButtonType.OK).show();
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
        stage.getIcons().add(new Image("img/logo.png"));
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
            name = employeeBO.getEmployeeName(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
        return name;
    }

    public void markAttendence(String id){
        for (AttendanceTM attends : employees) {
            if (attends.getEmployeeId().equals(id)){
                attends.setAttendance("Present");
            }
        }
        tblMarkAttendence.refresh();
    }

    private void setComboBox() {
        try {
            ObservableList<String> employees = employeeBO.getAllEmployeeNames();
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
            boolean isMarked = employeeBO.existsAttendance(LocalDate.now());
            if (!isMarked) {
                ArrayList<EmployeeDTO> employeeDTOS = employeeBO.getAllEmployees();
                ObservableList<AttendanceTM> temp = FXCollections.observableArrayList();
                for (EmployeeDTO dto : employeeDTOS) {
                    temp.add(new AttendanceTM(dto.getEmployeeId(),dto.getName(),dto.getJobRole(),"Not Marked"));
                }
                employees = temp;
                tblMarkAttendence.setItems(employees);
            }else {
                ObservableList<AttendanceTM> marked = employeeBO.getAttendanceOfDay(String.valueOf(LocalDate.now()));
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
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colJob.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("attendance"));

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
            ArrayList<EmployeeDTO> allEmployees = employeeBO.getAllEmployees();
            data.clear();
            for (EmployeeDTO dto : allEmployees){
                JFXButton button = new JFXButton("edit",new ImageView("img/edit-97@30x.png"));
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.getStyleClass().add("infoBtn");
                setEditbtnOnAction(button);
                data.add(new EmployeeTM(dto.getEmployeeId(),dto.getName(),dto.getJobRole(),dto.getSalary(),button,dto.getContact(),dto.getAddress()));
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
