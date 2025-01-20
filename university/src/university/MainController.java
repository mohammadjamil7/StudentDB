package university;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class MainController {
    @FXML
    private TextField nameField, ageField, majorField;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> colName;
    @FXML
    private TableColumn<Student, Integer> colAge;
    @FXML
    private TableColumn<Student, String> colMajor;

    private ObservableList<Student> studentList;

    private Connection connection;

    private Student selectedStudent;

    public void initialize() {

        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colAge.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        colMajor.setCellValueFactory(cellData -> cellData.getValue().majorProperty());

        studentList = FXCollections.observableArrayList();
        studentTable.setItems(studentList);

        initDatabase();
    }

    private void initDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:university.db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS students (name TEXT, age INTEGER, major TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        handleViewStudents();
    }

    @FXML
    private void handleAddStudent() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String major = majorField.getText();

        try {
            String sql = "INSERT INTO students (name, age, major) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, major);
            pstmt.executeUpdate();

            studentList.add(new Student(name, age, major));
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateStudent() {
        if (selectedStudent != null) {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String major = majorField.getText();

            try {
                String sql = "UPDATE students SET name = ?, age = ?, major = ? WHERE name = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setString(3, major);
                pstmt.setString(4, selectedStudent.getName());
                pstmt.executeUpdate();

                selectedStudent.setName(name);
                selectedStudent.setAge(age);
                selectedStudent.setMajor(major);
                studentTable.refresh();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteStudent() {
        if (selectedStudent != null) {
            try {
                String sql = "DELETE FROM students WHERE name = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, selectedStudent.getName());
                pstmt.executeUpdate();

                studentList.remove(selectedStudent);
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleViewStudents() {
        studentList.clear();
        try {
            String sql = "SELECT * FROM students";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                studentList.add(new Student(rs.getString("name"), rs.getInt("age"), rs.getString("major")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRowSelect() {
        selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            nameField.setText(selectedStudent.getName());
            ageField.setText(String.valueOf(selectedStudent.getAge()));
            majorField.setText(selectedStudent.getMajor());
        }
    }

    private void clearFields() {
        nameField.clear();
        ageField.clear();
        majorField.clear();
        selectedStudent = null;
    }
}
