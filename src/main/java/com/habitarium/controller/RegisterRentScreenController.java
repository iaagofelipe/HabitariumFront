package com.habitarium.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.dao.RentDAO;
import main.java.entity.Lessor;
import main.java.entity.Rent;
import main.java.enuns.Gender;

import java.net.URL;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterRentScreenController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private DatePicker datePBorn;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtRg;
    @FXML
    private TextField txtTel1;
    @FXML
    private TextField txtTel2;
    @FXML
    private ComboBox<Gender> cbGender;
    @FXML
    private ComboBox<String> cbProperty;
    @FXML
    private TextField txtRentValue;
    @FXML
    private Spinner<Integer> spPayDay;
    @FXML
    private DatePicker datePEntrance;
    @FXML
    private DatePicker datePExit;
    @FXML
    private DatePicker datePReadjustment;
    @FXML
    private Button btnSave;

    Rent rent;
    Lessor lessor;

    @FXML
    void save() {
        rent = new Rent();
        lessor = new Lessor();
        if (checkTxtPadding() && checkDateEnumPadding()) {
            if (false) {
                alertRgInvalid();
            } else if (false) {
                alertCpfInvalid();
            } else {
                lessor.setName(txtName.getText().trim());
                lessor.setCpf(txtCpf.getText().trim());
                lessor.setRg(txtRg.getText().trim());
                lessor.setTelOne(txtTel1.getText().trim());
                lessor.setTelTwo((txtTel2.getText().trim()));
                lessor.setGender(cbGender.getValue());

                Date entranceDate = Date.from(datePEntrance.getValue()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date exitDate = Date.from(datePExit.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date readjustmentDate = Date.from(datePReadjustment.getValue()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());

                rent.setValue(Float.parseFloat(txtRentValue.getText().trim()));
                rent.setEntranceDate(entranceDate);
                rent.setExitDate(exitDate);
                rent.setReadjustmentDate(readjustmentDate);
                rent.setPayDay(spPayDay.getValue());
                lessor.setRent(rent);
                rent.setLessor(lessor);

                RentDAO rentDAO = new RentDAO();
                rent = rentDAO.save(rent);

                saveSucess();
                Stage stage = (Stage) btnSave.getScene().getWindow();
                stage.close();
            }
        } else {
            alertPadding();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSpinner();
        setComboBox();
    }

    private void setSpinner() {
        int initialValue = 5;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(1, lastDayCurrentMonth(), initialValue);
        spPayDay.setValueFactory(valueFactory);
    }

    private void setComboBox() {
        ObservableList<Gender> list = FXCollections.observableArrayList(Gender.MALE, Gender.FEMALE, Gender.OTHERS);
        cbGender.setItems(FXCollections.observableList(list));
    }

    public boolean checkTxtPadding() {
        boolean registerLessor = !txtName.getText().trim().equals("") && !txtCpf.getText().trim().equals("")
                && !txtRg.getText().trim().equals("") && !txtTel1.getText().trim().equals("")
                && !txtTel2.getText().trim().equals("");
        boolean registerRent = !txtRentValue.getText().trim().equals("");
        return registerLessor && registerRent;
    }

    public boolean checkDateEnumPadding() {
        if (datePBorn.getValue() != null && datePEntrance.getValue() != null &&
                datePExit.getValue() != null && datePReadjustment.getValue() != null &&
                cbGender.getSelectionModel().getSelectedIndex() != -1) {
            return true;
        } else {
            return false;
        }
    }

    private void alertPadding() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Há campos em branco", ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText("Erro ao preencher");
        alert.show();
    }

    private void alertCpfInvalid() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Formato do CPF inválido", ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText("Erro de CPF");
        alert.show();
    }

    private void alertRgInvalid() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Formato do RG inválido",
                ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText("Erro de RG");
        alert.show();
    }

    public void saveSucess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "",
                ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText("salvo com sucesso!");
        alert.show();
    }

    public int lastDayCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private boolean isCpfValid(String cpf) {
        return cpf.matches("/^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$/");
    }

    private boolean isRgValid(String rg) {
        return rg.matches("[0-9](\\.[0-9]{3}){2}-[0-9]");
    }
}
