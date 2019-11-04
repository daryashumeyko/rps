package com.enforcer.DAO;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySQLPetDAOTest {

    @Test
    public void create() throws SQLException, IOException {
        DAO dao = new DAO();
        Pet petresult;
        Pet pet = new Pet();
        pet.setName("petcat");
        pet.setBirthDate("03.10.2014");
        pet.setType("cat");
        pet.setPetOwnerId(2);
        //создание питомца и проверка результата на null
        try (Connection connection = dao.getConnection()){
            PetDAO petDAO = dao.getPetDao(connection);
            petresult=petDAO.create(pet);
        }
        Assert.assertNotNull(petresult);
    }

    @Test
    public void read() throws SQLException {
        DAO dao = new DAO();
        Pet pet;
        try (Connection connection = dao.getConnection()) {
            PetDAO petDAO = dao.getPetDao(connection);
            pet = petDAO.read(1);
        }
        //считывание записи с id=1 и проверка на null
        Assert.assertNotNull(pet);
    }

    @Test
    public void update() throws SQLException {
        DAO dao = new DAO();
        Pet pet = new Pet();
        pet.setName("petcat");
        pet.setBirthDate("03.10.2014");
        pet.setType("cat");
        pet.setPetOwnerId(2);
        try (Connection connection = dao.getConnection()) {
            PetDAO petDAO = dao.getPetDao(connection);
            petDAO.update(pet, 1);
        }
      //обновление записи и проверка ее успешности(сравнение введенного имени с записанным)
        Connection connection = dao.getConnection();
        PetDAO petDAO = dao.getPetDao(connection);
        Assert.assertEquals(pet.getName(), petDAO.read(1).getName());
    }

    @Test
    public void delete() throws SQLException {
        DAO dao = new DAO();
        Pet pet;
        try (Connection connection = dao.getConnection()){
            PetDAO petDAO = dao.getPetDao(connection);
            pet = petDAO.read((int) (petDAO.getAll().get(petDAO.getAll().size() - 1)).getId());
            petDAO.delete(pet);
        }
        //удаление последней записи
        Assert.assertNotNull("Pet not found.", pet);
    }

    @Test
    public void getAll() throws SQLException {
        DAO dao = new DAO();
        List<Pet> list;
        try (Connection connection = dao.getConnection()){
            PetDAO petDAO = dao.getPetDao(connection);
            list = petDAO.getAll();
        }
        //проверка что список на пустой
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }
}