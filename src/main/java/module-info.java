module com.example.univer_test_4 {
    requires javafx.controls;
    requires javafx.fxml; // Оставляем, если вы планируете использовать FXML

    requires org.kordamp.bootstrapfx.core; // Для BootstrapFX

    // --- ДОБАВЛЕНО для Ikonli ---
    requires org.kordamp.ikonli.javafx;     // Основной модуль Ikonli для JavaFX
    requires org.kordamp.ikonli.fontawesome5; // Модуль для иконок FontAwesome 5 (мы использовали FontAwesomeSolid)
    // --- КОНЕЦ ДОБАВЛЕННОГО ---

    // Оставляем java.desktop на всякий случай, т.к. некоторые зависимости могут его неявно использовать,
    // но если вы уверены, что он не нужен, можно попробовать удалить.
    requires java.desktop;

    // Открываем ваш пакет для JavaFX FXML (если используете FXML) и рефлексии
    opens com.example.univer_test_4 to javafx.fxml;
    // Экспортируем ваш пакет, чтобы JavaFX мог запустить приложение
    exports com.example.univer_test_4;
    exports university;
    opens university to javafx.fxml;

    // Если ваши классы логики (Student, Professor, etc.) в другом пакете,
    // например, university, то нужно экспортировать и/или открыть и его:
    // exports university;
    // opens university to javafx.base; // Если используете properties в логике
}