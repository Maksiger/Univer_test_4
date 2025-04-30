package university;

import java.util.Objects;

public class Enrollment {
    private final Student student;
    private final Course course;
    private String grade; // Оценка (например, "A", "B+", "95", "Pass", "Не аттестован")

    public Enrollment(Student student, Course course) {
        // Проверка на null добавлена для надежности
        this.student = Objects.requireNonNull(student, "Студент не может быть null в Enrollment");
        this.course = Objects.requireNonNull(course, "Курс не может быть null в Enrollment");
        this.grade = null; // Оценка не выставлена по умолчанию
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getGrade() {
        return grade == null ? "N/A" : grade; // Возвращаем "N/A" если оценки нет
    }

    public void setGrade(String grade) {
        // Обработка пустой строки или null как отсутствия оценки
        this.grade = (grade == null || grade.trim().isEmpty()) ? null : grade.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        // Запись уникальна по студенту и курсу
        return student.equals(that.student) && course.equals(that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "student=" + student.getName() +
                ", course=" + course.getCourseCode() +
                ", grade='" + getGrade() + '\'' +
                '}';
    }
}