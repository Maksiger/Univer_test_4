package university; // Или auth

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {

    // Простое хранилище в памяти (для примера!)
    // В реальном приложении здесь будет работа с файлом или БД
    private final Map<String, User> userStore = new HashMap<>();

    public AuthService() {
        // Можно добавить тестового пользователя при старте
        // register("admin", "password");
    }

    /**
     * Регистрирует нового пользователя.
     * @param username Имя пользователя.
     * @param password Пароль в открытом виде.
     * @return Созданный объект User.
     * @throws IllegalArgumentException если имя пользователя пустое или null.
     * @throws DuplicateUserException если пользователь с таким именем уже существует.
     * @throws RuntimeException если произошла ошибка хеширования.
     */
    public User register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым.");
        }
        if (userStore.containsKey(username.toLowerCase())) {
            throw new DuplicateUserException("Пользователь '" + username + "' уже существует.");
        }

        String salt = generateSalt();
        String passwordHash = hashPassword(password, salt);

        User newUser = new User(username.toLowerCase(), passwordHash, salt); // Храним username в нижнем регистре
        userStore.put(newUser.getUsername(), newUser);
        System.out.println("Пользователь '" + username + "' зарегистрирован."); // Для отладки
        return newUser;
    }

    /**
     * Проверяет логин и пароль.
     * @param username Имя пользователя.
     * @param password Пароль в открытом виде.
     * @return Optional с объектом User при успехе, иначе Optional.empty().
     */
    public Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        User storedUser = userStore.get(username.toLowerCase());
        if (storedUser == null) {
            return Optional.empty(); // Пользователь не найден
        }

        // Проверяем пароль
        if (verifyPassword(password, storedUser.getPasswordHash(), storedUser.getSalt())) {
            return Optional.of(storedUser); // Успешный вход
        } else {
            return Optional.empty(); // Неверный пароль
        }
    }

    // --- Хеширование пароля (SHA-256 с солью) ---

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 16 байт соли
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Добавляем соль к паролю перед хешированием
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Это не должно происходить с SHA-256
            throw new RuntimeException("Ошибка хеширования пароля", e);
        }
    }

    private boolean verifyPassword(String providedPassword, String storedHash, String salt) {
        String newHash = hashPassword(providedPassword, salt);
        return newHash.equals(storedHash);
    }

    // --- Кастомное исключение для дубликата ---
    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String message) {
            super(message);
        }
    }
}