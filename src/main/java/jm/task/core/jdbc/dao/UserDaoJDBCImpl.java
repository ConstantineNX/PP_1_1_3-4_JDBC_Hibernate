package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    public UserDaoJDBCImpl() {
    }

    private void execInTransaction(Consumer<Connection> connect) {
        try {
            connection.setAutoCommit(false);
            connect.accept(connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "`name` VARCHAR(255), " +
                "lastName VARCHAR(255), " +
                "age TINYINT)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Таблица создана");
            } catch (SQLException e) {
                System.out.println("Ошибка при создании таблицы: " + e.getMessage());
            }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Таблица удалена");
            } catch (SQLException e) {
                System.out.println("Ошибка при удалении табилцы: " + e.getMessage());
            }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        execInTransaction(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, age);
                preparedStatement.executeUpdate();
                System.out.println("User добавлен");
            } catch (SQLException e) {
                System.out.println("Ошибка при добавлении User: " + e.getMessage());
            }
        });
    }


    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id=?";
        execInTransaction(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.println("User удален");
            } catch (SQLException e) {
                System.out.println("Ошибка при удалении User: " + e.getMessage());
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            System.out.println("Получение списка Users прошло успешно");
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка Users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        execInTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Таблица Users очищена");
            } catch (SQLException e) {
                System.out.println("Ошибка при очищении таблицы Users: " + e.getMessage());
            }
        });
    }
}
