package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        // реализуйте алгоритм здесь
        Connection connection = Util.getConnection();
        UserService userService = new UserServiceImpl((UserDao) connection);
        userService.createUsersTable();

        userService.saveUser("Ivan", "Ivanov", (byte) 32);
        userService.saveUser("Petr", "Petrov", (byte) 18);
        userService.saveUser("Serg", "Olov", (byte) 17);
        userService.saveUser("Karmen", "Ivanova", (byte) 43);
        userService.saveUser("Ivanna", "Petrova", (byte) 34);
        userService.saveUser("Alex", "Alexeev", (byte) 16);
        userService.saveUser("Igor", "Livanov", (byte) 55);

        userService.removeUserById(3);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();




    }
}
