package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;
    public UserDaoJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    public void createUsersTable() throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS users (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "`name` VARCHAR(255), " +
            "lastName VARCHAR(255), " +
            "age TINYINT)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void dropUsersTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        }
    }

    public void removeUserById(long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age FROM users";
        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        String sql = "DELETE FROM users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
