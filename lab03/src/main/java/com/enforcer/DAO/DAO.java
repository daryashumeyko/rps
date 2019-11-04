package com.enforcer.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    private String user = "user";//Логин пользователя
    private String password = "12345678";//Пароль пользователя
    private String url = "jdbc:mysql://localhost:3306/lab02";//URL адрес
    private String driver = "com.mysql.jdbc.Driver";//Имя драйвера

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public OwnerDAO getOwnerDao(Connection connection) {
        return new MySQLOwnerDAO(connection);
    }  //объект доступа к таблице owner

    public PetDAO getPetDao(Connection connection) {
        return new MySQLPetDAO(connection);
    }
    //объект доступа к таблице pet

    public static void connectionClose(Connection connection) throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    public DAO() {
        try {
            Class.forName(driver);  //Регистрируем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
