package university;

import javafx.beans.property.*;

public class Student {
    private final StringProperty name;
    private final IntegerProperty age;
    private final StringProperty major;

    public Student(String name, int age, String major) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.major = new SimpleStringProperty(major);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getMajor() {
        return major.get();
    }

    public void setMajor(String major) {
        this.major.set(major);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public StringProperty majorProperty() {
        return major;
    }
}
