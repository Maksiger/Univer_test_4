package university;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Professor {
    private String name;
    private int professorId;
    private String department;
    private List<Course> teachingCourses;

    public Professor(String name, int professorId, String department) {
        this.name = name;
        this.professorId = professorId;
        this.department = department;
        this.teachingCourses = new ArrayList<>();
    }

    // Метод назначения преподавателя на курс
    public void assignToCourse(Course course) {
        if (course != null && !teachingCourses.contains(course)) {
            teachingCourses.add(course);
            // Убедимся, что и курс знает об этом преподавателе
            if (!this.equals(course.getProfessor())) {
                course.setProfessor(this); // Вызываем сеттер курса
            }
            // System.out.println("Профессор " + name + " назначен на курс: " + course.getCourseName());
        }
    }

    // Внутренний метод для добавления курса (используется из Course.setProfessor)
    // Сделаем его package-private или protected, чтобы не вызывать снаружи напрямую
    protected void addTeachingCourseInternal(Course course) {
        if (course != null && !teachingCourses.contains(course)) {
            teachingCourses.add(course);
        }
    }

    // Метод для удаления курса
    protected void removeTeachingCourseInternal(Course course) {
        if (course != null) {
            teachingCourses.remove(course);
        }
    }

    public String getBaseInfo() {
        return "Преподаватель: " + name + " (ID: " + professorId + "), Кафедра: " + department;
    }

    // Геттеры
    public String getName() { return name; }
    public int getProfessorId() { return professorId; }
    public String getDepartment() { return department; }
    public List<Course> getTeachingCourses() { return new ArrayList<>(teachingCourses); } // Возвращаем копию

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return professorId == professor.professorId; // Сравнение по ID
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(professorId);
    }

    @Override
    public String toString() {
        return name + " (ID: " + professorId + ")"; // Для отображения
    }
}