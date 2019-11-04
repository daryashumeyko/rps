package ru.wirbel.lab02.myapp;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Application {
    private static final String DEFAULT_CONSOLE_ENCODING = "UTF-8";
    private static final String CONSOLE_ENCODING_PROPERTY = "consoleEncoding";
    private static final String PROPERTIES_FILENAME = "application.properties";
    private static final String DB_URL_PROPERTY = "db.url";
    private static final String DB_USERNAME_PROPERTY = "db.username";
    private static final String DB_PASSWORD_PROPERTY = "db.password";

    private Properties settings = new Properties();
    private Connection connection;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // write your code here
        Application app = new Application();
        app.run();
    }

    private void run() throws IOException {
        // установить кодировку консоли
        setConsoleEncoding();
        // загрузить найстроки подключения из файла
        loadProperties();
        // установить соединение
        establishConnection();
        // код приложения
        showMainMenu();
        // закрыть соединение
        closeConnection();

    }

    private void establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();     //ПОДКЛЮЧЕНИЕ ДРАЙВЕРА mysql
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("Произошла ошибка при загрузке MySQL драйвера.");
            System.exit(1);
        }

        try {
            this.connection = DriverManager.getConnection(      //создание соединения с бд
                    settings.getProperty(DB_URL_PROPERTY),
                    settings.getProperty(DB_USERNAME_PROPERTY),
                    settings.getProperty(DB_PASSWORD_PROPERTY)
            );
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при установке соединения с БД: ");
            System.out.println(sqle.getMessage());
            System.exit(1);
        }
    }

    /**
     * Закрытие соединения с БД
     */
    private void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при закрытии соединения:");
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * Загрузка параметров подключения к БД
     */
    private void loadProperties() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
            this.settings.load(inputStream);
        } catch (IOException ioe) {
            System.out.println("Произошла ошибка при загрузке параметров подключения к БД");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
    }

    private static void setConsoleEncoding() {
        // чтение системной переменной
        String consoleEncoding = System.getProperty(CONSOLE_ENCODING_PROPERTY, DEFAULT_CONSOLE_ENCODING);

        try {
            // установить кодировку стандартной консоли вывода
            System.setOut(new PrintStream(System.out, true, consoleEncoding));
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported encoding set for console: " + consoleEncoding);
        }
    }

    private void showMainMenu() throws IOException {
        clearConsole();
        System.out.println("Основное меню:");
        System.out.println("\t1) Управление владельцами");
        System.out.println("\t2) Управление питомцами");
        System.out.println("\t0) Закончить работу (выход)");

        loop:
        while (true) {
            System.out.print("Выберите один из вариантов [0-2]: ");
            String choice = reader.readLine();

            switch (choice) {
                case "1":
                    showUserMenu();
                    break;
                case "2":
                    showPetMenu();
                    break;
                case "0":
                    break;
                default:
                    continue loop;
            }
            break;
        }
    }

    private void showUserMenu() throws IOException {
        clearConsole();

        System.out.println("Управление владельцами:");
        System.out.println("\t1) Список всех владельцев");
        System.out.println("\t2) Добавить нового владельца");
        System.out.println("\t3) Удалить владельца");
        System.out.println("\t4) Изменить владельца");
        System.out.println("\t0) Вернуться в основное меню");

        loop:
        while (true) {
            System.out.print("Выберите один из вариантов [0-4]: ");
            String choice = reader.readLine();
            switch (choice) {
                case "0":
                    showMainMenu();
                    break;
                case "1":
                    listAllUsers();
                    break;
                case "2":
                    addUser();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    editUser();
                    break;
                default:
                    continue loop;
            }
            break;
        }
    }

    private void listAllUsers() throws IOException {
        clearConsole();

        String lineFormat = "%s. %s %s";
        String query = "select `name`, `address` from owner order by `name` desc";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.connection.prepareStatement(query);
            rs = stmt.executeQuery();

            int rowNum = 0;
            while (rs.next()) {
                System.out.println(String.format(lineFormat,
                        ++rowNum,
                        rs.getString("name"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showUserMenu();
    }

    private void addUser() throws IOException {
        clearConsole();

        String query =
                "insert into owner (name, address)" +
                        " values (?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Введите сведения о владельце");
        System.out.print("\tФИО: ");
        String fio = reader.readLine();
        System.out.print("\tАдрес: ");
        String address = reader.readLine();

        try {
            stmt = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, fio);
            stmt.setString(2, address);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("Пользователь успешно добавлен с ID = " + rs.getInt(1));
                }
            } else {
                System.out.println("Не удалось добавить владельца.");
            }
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showUserMenu();
    }

    private int getOwnerId(String name) {
        String query = "select id from owner where name = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            return 0;
        }
    }

    private void deleteUser() throws IOException {
        clearConsole();

        String query = "delete from owner where id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        System.out.println("Введите id владельца: ");
        int id = Integer.parseInt(reader.readLine());

        try {
            preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            System.out.println("Пользователь удален");
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showUserMenu();
    }

    private void editUser() throws IOException {
        clearConsole();

        String query = "update owner set name=?, address=? where id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Введите id ползователя: ");
            preparedStatement.setInt(3, Integer.parseInt(reader.readLine()));
            System.out.println("Введите новое имя: ");
            preparedStatement.setString(1, reader.readLine());
            System.out.println("Введите адрес: ");
            preparedStatement.setString(2, reader.readLine());
            preparedStatement.execute();
            System.out.println("Запись успешно изменена");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showUserMenu();
    }


    private void showPetMenu() throws IOException {
        clearConsole();

        System.out.println("Управление петомцами:");
        System.out.println("\t1) Список всех питомцев");
        System.out.println("\t2) Добавить нового питомца");
        System.out.println("\t3) Удалить питомца");
        System.out.println("\t4) Изменить питомца");
        System.out.println("\t0) Вернуться в основное меню");

        loop:
        while (true) {
            System.out.print("Выберите один из вариантов [0-4]: ");
            String choice = reader.readLine();
            switch (choice) {
                case "0":
                    showMainMenu();
                    break;
                case "1":
                    listAllPets();
                    break;
                case "2":
                    addPet();
                    break;
                case "3":
                    deletePet();
                    break;
                case "4":
                    editPet();
                    break;
                default:
                    continue loop;
            }
            break;
        }
    }

    private void listAllPets() throws IOException {
        clearConsole();

        String lineFormat = "%s. %s %s (%s) %s";
        String query = "select pet.name, pet.birthDate, pet.type, owner.name from pet join owner on pet.pet_owner_id = owner.id";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.connection.prepareStatement(query);
            rs = stmt.executeQuery();

            int rowNum = 0;
            while (rs.next()) {
                System.out.println(String.format(lineFormat,
                        ++rowNum,
                        rs.getString("name"),
                        rs.getDate("birthDate"),
                        rs.getString("type"),
                        rs.getString("owner.name")
                ));
            }
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showPetMenu();
    }

    private void addPet() throws IOException {
        clearConsole();

        String query =
                "insert into pet (name, birthDate, type, pet_owner_id)" +
                        " values (?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Введите сведения о петомце");
        System.out.print("\tКличка: ");
        String name = reader.readLine();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean isDatePresent = false;
        Date birthDate = null;
        do {
            System.out.print("\tДата рождения (yyyy-MM-dd): ");
            try {
                birthDate = sdf.parse(reader.readLine());
                isDatePresent = true;
            } catch (ParseException pe) {
                System.out.println("\tНекорректная дата, попробуйте снова.");
            }
        } while (!isDatePresent);

        System.out.print("\tВид: ");
        String type = reader.readLine();

        String ownerName;
        int ownerId = 0;
        boolean flag = false;
        do {
            System.out.print("\tХозяин: ");
            try {
                ownerName = reader.readLine();
                if (getOwnerId(ownerName) == 0) {
                    System.out.println("Хозяина с таким именем нет в базе");
                } else {
                    ownerId = getOwnerId(ownerName);
                    flag = true;
                }
            } catch (Exception ex) {
                //do nothing
            }
        } while (!flag);


        try {
            stmt = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setDate(2, new java.sql.Date(birthDate.getTime()));
            stmt.setString(3, type);
            stmt.setInt(4, ownerId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("Питомец успешно добавлен с ID = " + rs.getInt(1));
                }
            } else {
                System.out.println("Не удалось добавить питомца.");
            }
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showPetMenu();
    }

    private void deletePet() throws IOException {
        clearConsole();

        String query = "delete from pet where id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        System.out.println("Введите id питомца: ");
        int id = Integer.parseInt(reader.readLine());

        try {
            preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            System.out.println("Питомец удален");
        } catch (SQLException sqle) {
            System.out.println("Произошла ошибка при выполнении SQL запроса:");
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (Exception e) {
                // do nothing
            }
            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showPetMenu();
    }




    private void editPet() throws IOException {
        clearConsole();

        String query = "update pet set name = ?, birthDate = ?, pet_owner_id = ? where id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Введите id ползователя: ");
            preparedStatement.setInt(4, Integer.parseInt(reader.readLine()));
            System.out.println("Введите новую кличку: ");
            preparedStatement.setString(1, reader.readLine());
            System.out.println("Введите дату рождения: ");
            boolean isDatePresent = false;
            Date birthDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            do {
                System.out.print("Введите дату рождения (yyyy-MM-dd): ");
                try {
                    birthDate = sdf.parse(reader.readLine());
                    isDatePresent = true;
                } catch (ParseException pe) {
                    System.out.println("Некорректная дата, попробуйте снова.");
                }
            } while(!isDatePresent);
            preparedStatement.setDate(2, new java.sql.Date(birthDate.getTime()));
            System.out.println("Введите имя хозяина");
            preparedStatement.setInt(3, getOwnerId(reader.readLine()));
            preparedStatement.execute();
            System.out.println("Запись успешно изменена");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // Вернуться в меню
        System.out.print("\nНажмите Enter...");
        reader.readLine();
        showPetMenu();

    }

    private void clearConsole(){
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
