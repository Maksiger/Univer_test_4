package university; // Или auth

import java.util.Objects;

public class User {
    private String username;
    private String passwordHash;
    private String salt; // Соль для хеширования пароля

    // Конструктор используется сервисом аутентификации
    public User(String username, String passwordHash, String salt) {
        this.username = Objects.requireNonNull(username);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.salt = Objects.requireNonNull(salt);
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    // Переопределяем equals/hashCode для корректной работы, например, в Map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username); // Пользователи уникальны по username
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}