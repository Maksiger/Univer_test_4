package com.example.univer_test_4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid; // Используем Solid для согласованности

import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

// Импорты для системы университета и аутентификации
import university.*; // Импортируем все, включая User, AuthService, Exceptions

import java.util.*;
import java.util.stream.Collectors;

public class UniversityAppJavaFX extends Application {

    private Stage primaryStage; // Сохраняем ссылку на основное окно
    private AuthService authService = new AuthService(); // Сервис аутентификации
    private User loggedInUser = null; // Текущий вошедший пользователь

    // --- Компоненты основного окна ---
    private University university = new University("Мой JavaFX Универ v4 (с Auth)");
    private ObservableList<Student> studentObjectList = FXCollections.observableArrayList();
    private ObservableList<Professor> professorObjectList = FXCollections.observableArrayList();
    private ObservableList<Course> courseObjectList = FXCollections.observableArrayList();
    private ListView<Student> studentListView;
    private ListView<Professor> professorListView;
    private ListView<Course> courseListView;
    private TextArea detailsTextArea;
    private Button setGradesBtn;

    // --- Иконки ---
    private final FontIcon userIcon = new FontIcon(FontAwesomeSolid.USER);
    private final FontIcon lockIcon = new FontIcon(FontAwesomeSolid.LOCK);
    private final FontIcon loginIcon = new FontIcon(FontAwesomeSolid.SIGN_IN_ALT);
    private final FontIcon registerIcon = new FontIcon(FontAwesomeSolid.USER_PLUS);
    private final FontIcon backIcon = new FontIcon(FontAwesomeSolid.ARROW_LEFT);
    // Иконки для основного окна
    private final FontIcon addStudentIcon = new FontIcon(FontAwesomeSolid.USER_PLUS);
    private final FontIcon addProfessorIcon = new FontIcon(FontAwesomeSolid.CHALKBOARD_TEACHER);
    private final FontIcon addCourseIcon = new FontIcon(FontAwesomeSolid.BOOK);
    private final FontIcon setGradesIcon = new FontIcon(FontAwesomeSolid.EDIT);


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Система Университета"); // Общий заголовок

        // Запускаем с экрана входа
        showLoginScreen();

        primaryStage.show();
    }

    // --- ЭКРАН ВХОДА ---
    private void showLoginScreen() {
        primaryStage.setTitle("Вход в систему");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("Авторизация");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.getStyleClass().add("h4");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label userNameLabel = new Label("Имя пользователя:");
        grid.add(userNameLabel, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setPromptText("Username");
        userTextField.setPrefWidth(200);
        userTextField.getStyleClass().add("form-control");
        grid.add(userTextField, 1, 1);

        Label pwLabel = new Label("Пароль:");
        grid.add(pwLabel, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Password");
        pwBox.getStyleClass().add("form-control");
        grid.add(pwBox, 1, 2);

        Button loginBtn = new Button("Войти", loginIcon);
        loginBtn.getStyleClass().addAll("btn", "btn-primary");
        loginBtn.setDefaultButton(true);

        Button registerBtn = new Button("Регистрация", registerIcon);
        registerBtn.getStyleClass().addAll("btn", "btn-secondary");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(registerBtn, loginBtn);
        grid.add(hbBtn, 1, 4);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.FIREBRICK);
        messageLabel.getStyleClass().add("text-danger");
        grid.add(messageLabel, 1, 5);

        loginBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            Optional<User> userOpt = authService.login(username, password);

            if (userOpt.isPresent()) {
                this.loggedInUser = userOpt.get();
                System.out.println("Успешный вход: " + loggedInUser.getUsername());
                showMainWindow(); // Переключаемся на основной экран
            } else {
                messageLabel.setText("Неверное имя пользователя или пароль.");
            }
        });

        registerBtn.setOnAction(e -> showRegistrationScreen());
        pwBox.setOnAction(e -> loginBtn.fire());

        Scene scene = new Scene(grid, 400, 300);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    // --- ЭКРАН РЕГИСТРАЦИИ ---
    private void showRegistrationScreen() {
        primaryStage.setTitle("Регистрация");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("Регистрация нового пользователя");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.getStyleClass().add("h4");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label userNameLabel = new Label("Имя пользователя:");
        grid.add(userNameLabel, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setPromptText("Username");
        userTextField.getStyleClass().add("form-control");
        grid.add(userTextField, 1, 1);

        Label pwLabel = new Label("Пароль:");
        grid.add(pwLabel, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Password");
        pwBox.getStyleClass().add("form-control");
        grid.add(pwBox, 1, 2);

        Label confirmPwLabel = new Label("Подтвердите пароль:");
        grid.add(confirmPwLabel, 0, 3);
        PasswordField confirmPwBox = new PasswordField();
        confirmPwBox.setPromptText("Confirm Password");
        confirmPwBox.getStyleClass().add("form-control");
        grid.add(confirmPwBox, 1, 3);

        Button registerBtn = new Button("Зарегистрироваться", registerIcon);
        registerBtn.getStyleClass().addAll("btn", "btn-success");

        Button backBtn = new Button("Назад", backIcon);
        backBtn.getStyleClass().addAll("btn", "btn-default");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(backBtn, registerBtn);
        grid.add(hbBtn, 1, 5);

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        grid.add(messageLabel, 1, 6);

        registerBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String confirmPassword = confirmPwBox.getText();
            messageLabel.setText("");

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Пароли не совпадают!");
                messageLabel.setTextFill(Color.FIREBRICK); messageLabel.getStyleClass().setAll("text-danger"); return;
            }
            if (password.length() < 4) {
                messageLabel.setText("Пароль слишком короткий (мин. 4 символа)!");
                messageLabel.setTextFill(Color.FIREBRICK); messageLabel.getStyleClass().setAll("text-danger"); return;
            }

            try {
                authService.register(username, password); // AuthService бросает исключения
                messageLabel.setText("Пользователь '" + username + "' успешно зарегистрирован!");
                messageLabel.setTextFill(Color.GREEN); messageLabel.getStyleClass().setAll("text-success");
                new Timer().schedule(new TimerTask() {
                    @Override public void run() { Platform.runLater(() -> showLoginScreen()); }
                }, 2000); // Задержка
            } catch (AuthService.DuplicateUserException | IllegalArgumentException ex) { // Ловим кастомные и стандартные
                messageLabel.setText("Ошибка: " + ex.getMessage());
                messageLabel.setTextFill(Color.FIREBRICK); messageLabel.getStyleClass().setAll("text-danger");
            } catch (RuntimeException ex) { // Ловим ошибку хеширования
                messageLabel.setText("Внутренняя ошибка регистрации.");
                messageLabel.setTextFill(Color.FIREBRICK); messageLabel.getStyleClass().setAll("text-danger");
                ex.printStackTrace();
            }
        });

        backBtn.setOnAction(e -> showLoginScreen());

        Scene scene = new Scene(grid, 450, 400);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    // --- ОСНОВНОЕ ОКНО ПРИЛОЖЕНИЯ ---
    private void showMainWindow() {
        primaryStage.setTitle("Университет: " + university.getName() + " (Пользователь: " + loggedInUser.getUsername() + ")");
        BorderPane root = new BorderPane(); root.setPadding(new Insets(15));

        // --- Панель кнопок ---
        HBox buttonBar = new HBox(10); buttonBar.setPadding(new Insets(0, 0, 15, 0)); buttonBar.setAlignment(Pos.CENTER);
        Button addStudentBtn = new Button("Добавить студента", addStudentIcon); Button addProfessorBtn = new Button("Добавить преподавателя", addProfessorIcon); Button addCourseBtn = new Button("Добавить курс", addCourseIcon);
        addStudentBtn.getStyleClass().addAll("btn", "btn-success"); addProfessorBtn.getStyleClass().addAll("btn", "btn-primary"); addCourseBtn.getStyleClass().addAll("btn", "btn-info");
        addStudentBtn.setOnAction(e -> handleAddStudent()); addProfessorBtn.setOnAction(e -> handleAddProfessor()); addCourseBtn.setOnAction(e -> handleAddCourse());
        buttonBar.getChildren().addAll(addStudentBtn, addProfessorBtn, addCourseBtn); root.setTop(buttonBar);

        // --- Панель списков ---
        GridPane listsPane = new GridPane(); listsPane.setHgap(15); listsPane.setVgap(10);
        ColumnConstraints colConstraint = new ColumnConstraints(); colConstraint.setPercentWidth(100.0 / 3.0); listsPane.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint);
        studentListView = new ListView<>(studentObjectList); professorListView = new ListView<>(professorObjectList); courseListView = new ListView<>(courseObjectList);
        studentListView.setCellFactory(listView -> new StudentListCell()); professorListView.setCellFactory(listView -> new ProfessorListCell()); courseListView.setCellFactory(listView -> new CourseListCell(this.university));
        Panel studentsPanel = createBootstrapPanel("Студенты", studentListView); Panel professorsPanel = createBootstrapPanel("Преподаватели", professorListView); Panel coursesPanel = createBootstrapPanel("Курсы", courseListView);
        listsPane.add(studentsPanel, 0, 0); listsPane.add(professorsPanel, 1, 0); listsPane.add(coursesPanel, 2, 0);
        GridPane.setVgrow(studentsPanel, Priority.ALWAYS); GridPane.setVgrow(professorsPanel, Priority.ALWAYS); GridPane.setVgrow(coursesPanel, Priority.ALWAYS); root.setCenter(listsPane);

        // --- Область деталей и кнопка оценок ---
        detailsTextArea = new TextArea(); detailsTextArea.setEditable(false); detailsTextArea.setWrapText(true); detailsTextArea.setPrefRowCount(10);
        Panel detailsPanel = new Panel("Детали"); detailsPanel.getStyleClass().add("panel-info"); detailsPanel.setBody(new VBox(detailsTextArea)); VBox.setVgrow(detailsTextArea, Priority.ALWAYS); detailsPanel.setMaxHeight(Double.MAX_VALUE);
        setGradesBtn = new Button("Выставить оценки", setGradesIcon); setGradesBtn.getStyleClass().addAll("btn", "btn-warning"); setGradesBtn.setDisable(true);
        setGradesBtn.setOnAction(e -> { Course selectedCourse = courseListView.getSelectionModel().getSelectedItem(); if (selectedCourse != null) { showSetGradesDialog(selectedCourse); } });
        HBox detailsButtonBox = new HBox(10); detailsButtonBox.setAlignment(Pos.CENTER_RIGHT); detailsButtonBox.getChildren().add(setGradesBtn); detailsButtonBox.setPadding(new Insets(5,0,0,0));
        VBox bottomContainer = new VBox(10); bottomContainer.getChildren().addAll(detailsPanel, detailsButtonBox); BorderPane.setMargin(bottomContainer, new Insets(15, 0, 0, 0)); root.setBottom(bottomContainer);

        // --- Слушатели выбора в списках ---
        studentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showDetails(newVal));
        professorListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showDetails(newVal));
        courseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showDetails(newVal));

        // --- Загрузка начальных данных ---
        if (studentObjectList.isEmpty() && professorObjectList.isEmpty() && courseObjectList.isEmpty()) { addInitialData(); }

        Scene scene = new Scene(root, 1050, 800);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene); primaryStage.centerOnScreen(); showDetails(null);
    }


    // --- Вспомогательные методы и диалоги ---

    // Вспомогательный метод для создания Panel BootstrapFX
    private Panel createBootstrapPanel(String title, ListView<?> listView) {
        Panel panel = new Panel(title); panel.getStyleClass().add("panel-primary");
        VBox content = new VBox(listView); VBox.setVgrow(listView, Priority.ALWAYS);
        panel.setBody(content); panel.setMaxHeight(Double.MAX_VALUE); panel.setPrefHeight(400);
        return panel;
    }

    // Обновленный метод showDetails для отображения оценок
    private void showDetails(Object selectedObject) {
        boolean isCourseSelected = selectedObject instanceof Course;
        if (setGradesBtn != null) { setGradesBtn.setDisable(!isCourseSelected); }
        if (selectedObject == null) { detailsTextArea.setText("Выберите элемент из списка для просмотра деталей."); clearOtherSelections(null); return; }
        StringBuilder info = new StringBuilder(); clearOtherSelections(selectedObject);
        if (selectedObject instanceof Student) {
            Student student = (Student) selectedObject; info.append("--- Студент ---\n"); info.append("Имя: ").append(student.getName()).append("\n"); info.append("ID: ").append(student.getStudentId()).append("\n\n"); info.append("Записан на курсы:\n");
            List<Enrollment> studentEnrollments = university.getEnrollmentsForStudent(student);
            if (studentEnrollments.isEmpty()) { info.append("  (нет записей)\n"); }
            else { studentEnrollments.sort(Comparator.comparing(e -> e.getCourse().getCourseName())); for (Enrollment enr : studentEnrollments) { info.append("  - ").append(enr.getCourse().getCourseName()).append(" (").append(enr.getCourse().getCourseCode()).append(") - Оценка: ").append(enr.getGrade()).append("\n"); } }
            info.append("---------------");
        } else if (selectedObject instanceof Professor) {
            Professor prof = (Professor) selectedObject; info.append("--- Преподаватель ---\n"); info.append("Имя: ").append(prof.getName()).append("\n"); info.append("ID: ").append(prof.getProfessorId()).append("\n"); info.append("Кафедра: ").append(prof.getDepartment()).append("\n\n"); info.append("Ведет курсы:\n");
            List<Course> teachingCourses = courseObjectList.stream().filter(c -> prof.equals(c.getProfessor())).sorted(Comparator.comparing(Course::getCourseName)).collect(Collectors.toList());
            if(teachingCourses.isEmpty()){ info.append("  (не назначен)\n"); } else { for(Course c : teachingCourses){ info.append("  - ").append(c.getCourseName()).append(" (").append(c.getCourseCode()).append(")\n"); } }
            info.append("--------------------");
        } else if (selectedObject instanceof Course) {
            Course course = (Course) selectedObject; info.append("--- Курс ---\n"); info.append("Название: ").append(course.getCourseName()).append("\n"); info.append("Код: ").append(course.getCourseCode()).append("\n"); info.append("Преподаватель: ").append(course.getProfessor() != null ? course.getProfessor().getName() : "(не назначен)").append("\n\n"); info.append("Записанные студенты:\n");
            List<Enrollment> courseEnrollments = university.getEnrollmentsForCourse(course);
            if (courseEnrollments.isEmpty()) { info.append("  (нет студентов)\n"); }
            else { courseEnrollments.sort(Comparator.comparing(e -> e.getStudent().getName())); for (Enrollment enr : courseEnrollments) { info.append("  - ").append(enr.getStudent().getName()).append(" (ID: ").append(enr.getStudent().getStudentId()).append(") - Оценка: ").append(enr.getGrade()).append("\n"); } }
            info.append("------------");
        }
        detailsTextArea.setText(info.toString());
    }

    // Сброс выбора в других списках
    private void clearOtherSelections(Object selectedObject) {
        if (!(selectedObject instanceof Student) && studentListView != null) studentListView.getSelectionModel().clearSelection();
        if (!(selectedObject instanceof Professor) && professorListView != null) professorListView.getSelectionModel().clearSelection();
        if (!(selectedObject instanceof Course) && courseListView != null) courseListView.getSelectionModel().clearSelection();
    }

    // --- ОБРАБОТЧИКИ КНОПОК С TRY-CATCH ---
    private void handleAddStudent() {
        Optional<Student> result = showAddStudentDialog();
        result.ifPresent(student -> {
            try {
                university.addStudent(student); // Бросает DuplicateEntityException
                studentObjectList.add(student);
                detailsTextArea.setText("Добавлен студент:\n" + getStudentInfo(student));
                studentListView.scrollTo(student);
                studentListView.getSelectionModel().select(student);
            } catch (RuntimeException ex) {
                showError("Ошибка добавления студента: " + ex.getMessage());
            }
        });
    }

    private void handleAddProfessor() {
        Optional<Professor> result = showAddProfessorDialog();
        result.ifPresent(prof -> {
            try {
                university.addProfessor(prof); // Бросает DuplicateEntityException
                professorObjectList.add(prof);
                detailsTextArea.setText("Добавлен преподаватель:\n" + getProfessorInfo(prof));
                professorListView.scrollTo(prof);
                professorListView.getSelectionModel().select(prof);
            } catch (RuntimeException ex) {
                showError("Ошибка добавления преподавателя: " + ex.getMessage());
            }
        });
    }

    private void handleAddCourse() {
        Optional<Course> result = showAddCourseDialog();
        result.ifPresent(course -> {
            try {
                university.addCourse(course); // Бросает DuplicateEntityException
                courseObjectList.add(course);
                // Опционально: открыть диалог назначения профессора
                detailsTextArea.setText("Добавлен курс:\n" + getCourseInfo(course));
                courseListView.scrollTo(course);
                courseListView.getSelectionModel().select(course);
            } catch (RuntimeException ex) {
                showError("Ошибка добавления курса: " + ex.getMessage());
            }
        });
    }

    // --- Диалоги для добавления (без проверки дубликатов) ---
    private Optional<Student> showAddStudentDialog() {
        Dialog<Student> dialog = new Dialog<>(); dialog.setTitle("Добавить нового студента"); dialog.setHeaderText("Введите данные студента");
        DialogPane dialogPane = dialog.getDialogPane(); dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ButtonType okButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE); dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        Node okButton = dialogPane.lookupButton(okButtonType); okButton.getStyleClass().addAll("btn", "btn-primary");
        Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL); cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField(); nameField.setPromptText("Имя"); nameField.getStyleClass().add("form-control");
        TextField idField = new TextField(); idField.setPromptText("ID (число)"); idField.getStyleClass().add("form-control");
        grid.add(new Label("Имя:"), 0, 0); grid.add(nameField, 1, 0); grid.add(new Label("ID:"), 0, 1); grid.add(idField, 1, 1);
        dialogPane.setContent(grid);
        Node okButtonNode = dialogPane.lookupButton(okButtonType); okButtonNode.setDisable(true);
        Runnable validateInput = () -> { boolean nameOk = !nameField.getText().trim().isEmpty(); boolean idOk = idField.getText().trim().matches("\\d+"); okButtonNode.setDisable(!(nameOk && idOk)); };
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run()); idField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) { String name = nameField.getText().trim(); String idStr = idField.getText().trim();
                if (name.isEmpty() || idStr.isEmpty()) { showError("Имя и ID не могут быть пустыми."); return null; }
                try { int id = Integer.parseInt(idStr); return new Student(name, id); }
                catch (NumberFormatException ex) { showError("ID должен быть целым числом!"); return null; }
            } return null;
        });
        return dialog.showAndWait();
    }

    private Optional<Professor> showAddProfessorDialog() {
        Dialog<Professor> dialog = new Dialog<>(); dialog.setTitle("Добавить нового преподавателя"); dialog.setHeaderText("Введите данные преподавателя");
        DialogPane dialogPane = dialog.getDialogPane(); dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ButtonType okButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE); dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        Node okButton = dialogPane.lookupButton(okButtonType); okButton.getStyleClass().addAll("btn", "btn-primary");
        Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL); cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField(); nameField.setPromptText("Имя"); nameField.getStyleClass().add("form-control");
        TextField idField = new TextField(); idField.setPromptText("ID (число)"); idField.getStyleClass().add("form-control");
        TextField deptField = new TextField(); deptField.setPromptText("Кафедра"); deptField.getStyleClass().add("form-control");
        grid.add(new Label("Имя:"), 0, 0); grid.add(nameField, 1, 0); grid.add(new Label("ID:"), 0, 1); grid.add(idField, 1, 1); grid.add(new Label("Кафедра:"), 0, 2); grid.add(deptField, 1, 2);
        dialogPane.setContent(grid);
        Node okButtonNode = dialogPane.lookupButton(okButtonType); okButtonNode.setDisable(true);
        Runnable validateInput = () -> { boolean nameOk = !nameField.getText().trim().isEmpty(); boolean idOk = idField.getText().trim().matches("\\d+"); boolean deptOk = !deptField.getText().trim().isEmpty(); okButtonNode.setDisable(!(nameOk && idOk && deptOk)); };
        nameField.textProperty().addListener((obs, old, newVal) -> validateInput.run()); idField.textProperty().addListener((obs, old, newVal) -> validateInput.run()); deptField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) { String name = nameField.getText().trim(); String idStr = idField.getText().trim(); String dept = deptField.getText().trim();
                if (name.isEmpty() || idStr.isEmpty() || dept.isEmpty()) { showError("Все поля должны быть заполнены."); return null; }
                try { int id = Integer.parseInt(idStr); return new Professor(name, id, dept); }
                catch (NumberFormatException ex) { showError("ID должен быть целым числом!"); return null; }
            } return null;
        });
        return dialog.showAndWait();
    }

    private Optional<Course> showAddCourseDialog() {
        Dialog<Course> dialog = new Dialog<>(); dialog.setTitle("Добавить новый курс"); dialog.setHeaderText("Введите данные курса");
        DialogPane dialogPane = dialog.getDialogPane(); dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ButtonType okButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE); dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        Node okButton = dialogPane.lookupButton(okButtonType); okButton.getStyleClass().addAll("btn", "btn-primary");
        Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL); cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField(); nameField.setPromptText("Название курса"); nameField.getStyleClass().add("form-control");
        TextField codeField = new TextField(); codeField.setPromptText("Код курса (напр. CS101)"); codeField.getStyleClass().add("form-control");
        grid.add(new Label("Название:"), 0, 0); grid.add(nameField, 1, 0); grid.add(new Label("Код:"), 0, 1); grid.add(codeField, 1, 1);
        dialogPane.setContent(grid);
        Node okButtonNode = dialogPane.lookupButton(okButtonType); okButtonNode.setDisable(true);
        Runnable validateInput = () -> { boolean nameOk = !nameField.getText().trim().isEmpty(); boolean codeOk = !codeField.getText().trim().isEmpty(); okButtonNode.setDisable(!(nameOk && codeOk)); };
        nameField.textProperty().addListener((obs, old, newVal) -> validateInput.run()); codeField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) { String name = nameField.getText().trim(); String code = codeField.getText().trim().toUpperCase();
                if (name.isEmpty() || code.isEmpty()) { showError("Название и код не могут быть пустыми."); return null; }
                return new Course(name, code);
            } return null;
        });
        return dialog.showAndWait();
    }

    // --- Диалог для выставления оценок (с try-catch) ---
    private void showSetGradesDialog(Course course) {
        Dialog<Boolean> dialog = new Dialog<>(); dialog.setTitle("Оценки: " + course.getCourseName()); dialog.setHeaderText("Введите оценки для студентов (пусто = N/A)");
        DialogPane dialogPane = dialog.getDialogPane(); dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet()); dialogPane.setMinWidth(500);
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE); dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        Node saveButton = dialogPane.lookupButton(saveButtonType); saveButton.getStyleClass().addAll("btn", "btn-success");
        Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL); cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        List<Enrollment> enrollments = university.getEnrollmentsForCourse(course); enrollments.sort(Comparator.comparing(e -> e.getStudent().getName()));
        Map<Student, TextField> gradeFields = new LinkedHashMap<>(); Node contentNode;
        if (enrollments.isEmpty()) { contentNode = new Label("  На этот курс не записан ни один студент."); saveButton.setDisable(true); }
        else {
            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 20, 10, 10));
            Label nameHeader = new Label("Студент"); nameHeader.setFont(Font.font("System", FontWeight.BOLD, 13)); Label gradeHeader = new Label("Оценка"); gradeHeader.setFont(Font.font("System", FontWeight.BOLD, 13));
            grid.add(nameHeader, 0, 0); grid.add(gradeHeader, 1, 0); int rowIndex = 1;
            for (Enrollment enr : enrollments) {
                Student student = enr.getStudent(); Label studentLabel = new Label(student.getName() + " (ID: " + student.getStudentId() + ")");
                TextField gradeField = new TextField(enr.getGrade().equals("N/A") ? "" : enr.getGrade());
                gradeField.setPromptText("N/A"); gradeField.getStyleClass().add("form-control"); gradeField.setPrefWidth(100);
                grid.add(studentLabel, 0, rowIndex); grid.add(gradeField, 1, rowIndex); gradeFields.put(student, gradeField); rowIndex++;
            }
            ScrollPane scrollPane = new ScrollPane(grid); scrollPane.setFitToWidth(true); scrollPane.setPrefHeight(300); contentNode = scrollPane;
        }
        dialogPane.setContent(contentNode);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType && !enrollments.isEmpty()) {
                try { gradeFields.forEach((student, textField) -> { String grade = textField.getText().trim(); university.setGrade(student, course, grade); }); return true; }
                catch (RuntimeException ex) { showError("Ошибка сохранения оценок: " + ex.getMessage()); return false; }
            } return false;
        });
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) { showDetails(course); }
    }

    // Добавление начальных данных (с try-catch)
    private void addInitialData() {
        try {
            Professor profSmith = new Professor("Доктор Смит", 101, "Информатика"); Professor profJones = new Professor("Профессор Джонс", 102, "Математика");
            university.addProfessor(profSmith); university.addProfessor(profJones);
            Course cs101 = new Course("Введение в программирование", "CS101"); Course math202 = new Course("Линейная алгебра", "MA202");
            university.addCourse(cs101); university.addCourse(math202);
            cs101.setProfessor(profSmith); math202.setProfessor(profJones);
            Student alice = new Student("Алиса", 2023001); Student bob = new Student("Боб", 2023002); Student charlie = new Student("Чарли", 2023003);
            university.addStudent(alice); university.addStudent(bob); university.addStudent(charlie);
            university.enrollStudent(alice, cs101); university.enrollStudent(alice, math202); university.enrollStudent(bob, cs101); university.enrollStudent(charlie, math202);
            university.setGrade(alice, cs101, "A"); university.setGrade(bob, cs101, "B+"); university.setGrade(alice, math202, "A-");
        } catch (RuntimeException ex) {
            System.err.println("Ошибка при добавлении начальных данных: " + ex.getMessage());
            Platform.runLater(() -> showError("Критическая ошибка при инициализации данных: " + ex.getMessage()));
        } finally {
            studentObjectList.setAll(university.getStudents()); professorObjectList.setAll(university.getProfessors()); courseObjectList.setAll(university.getCourses());
        }
    }

    // Вспомогательный метод для показа ошибок
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Ошибка"); alert.setHeaderText(null); alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane(); dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet()); dialogPane.getStyleClass().add("alert"); dialogPane.getStyleClass().add("alert-danger");
        alert.showAndWait();
    }

    // Точка входа
    public static void main(String[] args) {
        launch(args);
    }

    // --- Вспомогательные методы для получения информации ---
    private String getStudentInfo(Student student) { if (student == null) return ""; StringBuilder info = new StringBuilder(); info.append("--- Студент ---\n").append("Имя: ").append(student.getName()).append("\n").append("ID: ").append(student.getStudentId()).append("\n\n").append("Записан на курсы:\n"); List<Enrollment> enrollments = university.getEnrollmentsForStudent(student); if (enrollments.isEmpty()) { info.append("  (нет записей)\n"); } else { enrollments.sort(Comparator.comparing(e -> e.getCourse().getCourseName())); for (Enrollment enr : enrollments) { info.append("  - ").append(enr.getCourse().getCourseName()).append(" (").append(enr.getCourse().getCourseCode()).append(") - Оценка: ").append(enr.getGrade()).append("\n"); } } info.append("---------------"); return info.toString(); }
    private String getProfessorInfo(Professor prof) { if (prof == null) return ""; StringBuilder info = new StringBuilder(); info.append("--- Преподаватель ---\n").append("Имя: ").append(prof.getName()).append("\n").append("ID: ").append(prof.getProfessorId()).append("\n").append("Кафедра: ").append(prof.getDepartment()).append("\n\n").append("Ведет курсы:\n"); List<Course> teaching = courseObjectList.stream().filter(c -> prof.equals(c.getProfessor())).sorted(Comparator.comparing(Course::getCourseName)).collect(Collectors.toList()); if (teaching.isEmpty()) { info.append("  (не назначен)\n"); } else { for (Course c : teaching) { info.append("  - ").append(c.getCourseName()).append(" (").append(c.getCourseCode()).append(")\n"); } } info.append("--------------------"); return info.toString(); }
    private String getCourseInfo(Course course) { if (course == null) return ""; StringBuilder info = new StringBuilder(); info.append("--- Курс ---\n").append("Название: ").append(course.getCourseName()).append("\n").append("Код: ").append(course.getCourseCode()).append("\n").append("Преподаватель: ").append(course.getProfessor() != null ? course.getProfessor().getName() : "(не назначен)").append("\n\n").append("Записанные студенты:\n"); List<Enrollment> enrollments = university.getEnrollmentsForCourse(course); if (enrollments.isEmpty()) { info.append("  (нет студентов)\n"); } else { enrollments.sort(Comparator.comparing(e -> e.getStudent().getName())); for (Enrollment enr : enrollments) { info.append("  - ").append(enr.getStudent().getName()).append(" (ID: ").append(enr.getStudent().getStudentId()).append(") - Оценка: ").append(enr.getGrade()).append("\n"); } } info.append("------------"); return info.toString(); }

    // --- Внутренние классы для кастомных ячеек ListView ---
    private static class StudentListCell extends ListCell<Student> { private HBox contentBox=new HBox(10); private FontIcon icon=new FontIcon(FontAwesomeSolid.USER_GRADUATE); private Label nameLabel=new Label(); private Label idLabel=new Label(); private VBox textVBox=new VBox(2); public StudentListCell(){super();icon.setIconSize(18);icon.setIconColor(Color.SLATEGRAY);nameLabel.setFont(Font.font("System",FontWeight.BOLD,13));idLabel.setFont(Font.font("System",11));idLabel.setTextFill(Color.DARKSLATEGRAY);textVBox.getChildren().addAll(nameLabel,idLabel);contentBox.getChildren().addAll(icon,textVBox);contentBox.setAlignment(Pos.CENTER_LEFT);setText(null);} @Override protected void updateItem(Student item,boolean empty){super.updateItem(item,empty);if(empty||item==null){setGraphic(null);}else{nameLabel.setText(item.getName());idLabel.setText("ID: "+item.getStudentId());setGraphic(contentBox);}}}
    private static class ProfessorListCell extends ListCell<Professor> { private HBox contentBox=new HBox(10); private FontIcon icon=new FontIcon(FontAwesomeSolid.USER_TIE); private Label nameLabel=new Label(); private Label deptLabel=new Label(); private VBox textVBox=new VBox(2); public ProfessorListCell(){super();icon.setIconSize(18);icon.setIconColor(Color.DARKSLATEBLUE);nameLabel.setFont(Font.font("System",FontWeight.BOLD,13));deptLabel.setFont(Font.font("System",11));deptLabel.setTextFill(Color.DARKSLATEGRAY);textVBox.getChildren().addAll(nameLabel,deptLabel);contentBox.getChildren().addAll(icon,textVBox);contentBox.setAlignment(Pos.CENTER_LEFT);setText(null);} @Override protected void updateItem(Professor item,boolean empty){super.updateItem(item,empty);if(empty||item==null){setGraphic(null);}else{nameLabel.setText(item.getName());deptLabel.setText(item.getDepartment());setGraphic(contentBox);}}}
    private static class CourseListCell extends ListCell<Course> { private final University university; private HBox contentBox=new HBox(10); private FontIcon icon=new FontIcon(FontAwesomeSolid.LAPTOP_CODE); private Label nameLabel=new Label(); private Label codeLabel=new Label(); private VBox textVBox=new VBox(2); private Label studentCountLabel=new Label(); private HBox badgeBox=new HBox(); private Region spacer=new Region(); public CourseListCell(University university){super();this.university=Objects.requireNonNull(university);icon.setIconSize(18);icon.setIconColor(Color.TEAL);nameLabel.setFont(Font.font("System",FontWeight.BOLD,13));codeLabel.setFont(Font.font("System",11));codeLabel.setTextFill(Color.DARKSLATEGRAY);studentCountLabel.setFont(Font.font("System",FontWeight.BOLD,10));studentCountLabel.setTextFill(Color.WHITE);studentCountLabel.setPadding(new Insets(1,5,1,5));badgeBox.getChildren().add(studentCountLabel);badgeBox.getStyleClass().addAll("badge","bg-info");badgeBox.setAlignment(Pos.CENTER);HBox.setHgrow(spacer,Priority.ALWAYS);textVBox.getChildren().addAll(nameLabel,codeLabel);contentBox.getChildren().addAll(icon,textVBox,spacer,badgeBox);contentBox.setAlignment(Pos.CENTER_LEFT);setText(null);} @Override protected void updateItem(Course item,boolean empty){super.updateItem(item,empty);if(empty||item==null){setGraphic(null);}else{nameLabel.setText(item.getCourseName());codeLabel.setText(item.getCourseCode());int count=university.getEnrollmentsForCourse(item).size();studentCountLabel.setText(String.valueOf(count));badgeBox.setVisible(count > 0);setGraphic(contentBox);}}}

}