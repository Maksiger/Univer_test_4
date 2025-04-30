package university;

import java.util.Objects;

public class Student {
    private String name;
    private int studentId;
    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    public String getBaseInfo() {
        return "Студент: " + name + " (ID: " + studentId + ")";
    }


    // Геттеры для основных полей
    public String getName() {
        return name;
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId; // Сравнение по ID
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(studentId);
    }

    @Override
    public String toString() {
        return name + " (ID: " + studentId + ")"; // Для отображения в комбобоксах и т.п.
    }
}