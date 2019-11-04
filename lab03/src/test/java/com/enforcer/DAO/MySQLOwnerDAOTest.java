package com.enforcer.DAO;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySQLOwnerDAOTest {

    @Test
    public void getAll() throws SQLException {
        DAO dao = new DAO();
        List<Owner> list;
        try (Connection connection = dao.getConnection()) {
            OwnerDAO ownerDAO = dao.getOwnerDao(connection);
            list = ownerDAO.getAll();
        }
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void create() throws SQLException, IOException {
        DAO dao = new DAO();
        Owner ownerresult;
        Owner owner = new Owner();
        owner.setName("NewName");
        owner.setAddress("NewAddress");
       //создаем новую запись, анализируем результат в зависимости от того что вернется
        try (Connection connection = dao.getConnection()) {
            OwnerDAO ownerDAO = dao.getOwnerDao(connection);
            ownerresult = ownerDAO.create(owner);
        }
        Assert.assertNotNull(ownerresult);
    }

    @Test
    public void read() throws SQLException {
        DAO dao = new DAO();
        Owner owner;
        try (Connection connection = dao.getConnection()){
            OwnerDAO ownerDAO = dao.getOwnerDao(connection);
            owner = ownerDAO.read(1);
        }
        //считываем владельца с id 1, проверяем пусто или считалось
        Assert.assertNotNull(owner);
    }

    @Test
    public void update() throws SQLException {
        DAO dao = new DAO();
        Owner owner = new Owner();
        owner.setName("NewName");
        owner.setAddress("NewAddress");
        try (Connection connection = dao.getConnection()) {
            OwnerDAO ownerDAO = dao.getOwnerDao(connection);
            ownerDAO.update(owner, 1);
        }
    //Для владельца с id =1 вводим новое имя и адрес, после для проверки сравниваем имя у id=1 с введенным
        Connection connection = dao.getConnection();
        OwnerDAO ownerDAO = dao.getOwnerDao(connection);
        Assert.assertEquals(owner.getName(), ownerDAO.read(1).getName());
    }

    @Test
    public void delete() throws SQLException {
        DAO dao = new DAO();
        Owner owner;
        try (Connection connection = dao.getConnection()){
            OwnerDAO ownerDAO = dao.getOwnerDao(connection);
            //выбираем последнюю запись и удаляем ее
            owner = ownerDAO.read((int) (ownerDAO.getAll().get(ownerDAO.getAll().size() - 1)).getId());
            ownerDAO.delete(owner);
        }
        Assert.assertNotNull("Owner not found.", owner);
    }
}