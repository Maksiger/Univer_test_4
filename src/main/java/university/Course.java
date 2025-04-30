package university;

import java.util.Objects;

public class Course {
    private String courseName;
    private String courseCode;
    private Professor professor;

    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.professor = null;
    }

    public void setProfessor(Professor professor) {
        if (this.professor == null || !this.professor.equals(professor)) {
            // Удаляем курс у предыдущего профессора, если он был
            if (this.professor != null) {

            }
            this.professor = professor;
            // Сообщаем новому профессору (если он не null)
            if (professor != null) {
                // Используем метод assignToCourse
                professor.assignToCourse(this);
            }
        }
    }

    public String getBaseInfo() {
        return "Курс: " + courseName + " (" + courseCode + ")"
                + "\nПреподаватель: " + (professor != null ? professor.getName() : "(не назначен)");
    }

    // Геттеры для основных полей
    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Professor getProfessor() {
        return professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseCode.equals(course.courseCode); // Сравнение по коду курса
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }

    @Override
    public String toString() {
        return courseName + " (" + courseCode + ")"; // Для отображения
    }
}