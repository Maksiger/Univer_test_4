package university;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class University {
    private String name;
    private List<Student> students;
    private List<Professor> professors;
    private List<Course> courses;
    // !!! Поле для записей и оценок !!!
    private List<Enrollment> enrollments;

    public University(String name) {
        this.name = Objects.requireNonNull(name, "Название университета не может быть null");
        this.students = new ArrayList<>();
        this.professors = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.enrollments = new ArrayList<>(); // Инициализация
    }

    // --- Методы добавления основных сущностей ---
    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }
    public void addProfessor(Professor professor) {
        if (professor != null && !professors.contains(professor)) {
            professors.add(professor);
        }
    }
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    // --- МЕТОДЫ ДЛЯ РАБОТЫ С ЗАПИСЯМИ И ОЦЕНКАМИ ---

    public boolean enrollStudent(Student student, Course course) {
        if (student == null || course == null) { System.err.println("Ошибка записи: Студент/Курс null."); return false; }
        if (!this.students.contains(student) || !this.courses.contains(course)) { System.err.println("Ошибка записи: Студент " + student.getName() + " или Курс " + course.getCourseName() + " не найдены."); return false; }
        Enrollment newEnrollment = new Enrollment(student, course);
        if (!enrollments.contains(newEnrollment)) { enrollments.add(newEnrollment); return true; }
        else { return false; } // Уже записан
    }

    public boolean setGrade(Student student, Course course, String grade) {
        Optional<Enrollment> enrollmentOpt = findEnrollment(student, course);
        if (enrollmentOpt.isPresent()) {
            enrollmentOpt.get().setGrade(grade); return true;
        } else { System.err.println("Ошибка оценки: Студент " + student.getName() + " не найден на курсе " + course.getCourseName()); return false; }
    }

    public Optional<Enrollment> findEnrollment(Student student, Course course) {
        if (student == null || course == null) return Optional.empty();
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst();
    }

    public List<Enrollment> getEnrollmentsForStudent(Student student) {
        if (student == null) return Collections.emptyList();
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForCourse(Course course) {
        if (course == null) return Collections.emptyList();
        return enrollments.stream()
                .filter(e -> e.getCourse().equals(course))
                .collect(Collectors.toList());
    }

    // --- Геттеры ---
    public String getName() { return name; }
    public List<Student> getStudents() { return new ArrayList<>(students); }
    public List<Professor> getProfessors() { return new ArrayList<>(professors); }
    public List<Course> getCourses() { return new ArrayList<>(courses); }
    public List<Enrollment> getAllEnrollments() { return new ArrayList<>(enrollments); }
}