import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    static final String POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/postgres"; // URL для подключения к PostgreSQL
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/database_lr5"; // URL для подключения к созданной базе данных
    static final String JDBC_DRIVER = "org.postgresql.Driver"; // Драйвер для PostgreSQL

    String USER = "postgres"; // Логин
    String PASSWORD = "mysecretpassword"; // Пароль

    private Connection start_connection;
    private Connection connection;

    //конструктор класса, загружает драйвер и подключение создает
    public Database() {
        try {
            Class.forName(JDBC_DRIVER);
            this.start_connection = DriverManager.getConnection(POSTGRESQL_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    //выполняем скрипт init.sql и инициализируем БД
    public void initDatabase() {
        try {
            try (Statement statement = start_connection.createStatement()) {
                statement.execute("CREATE DATABASE database_lr5");
                System.out.println("База данных 'database_lr5' успешно создана.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("42P04")) {
                    System.out.println("База данных 'database_lr5' уже существует.");
                } else {
                    throw e;
                }
            }

            //подключаемся
            this.connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            //чтение + выполнение скрипта
            InputStream file = getClass().getClassLoader().getResourceAsStream("init.sql");
            if (file == null) {
                throw new FileNotFoundException("Отсутствует init.sql файл в папке src");
            }
            try (Statement statement = this.connection.createStatement();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sql.append(line).append("\n");
                }

                statement.execute(sql.toString());
                System.out.println("Процедуры успешно инициализированы.");
            } finally {
                this.connection.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    //подключение с нашим логином/паролем
    public boolean connectDatabase(String user, String password) {
        this.USER = user;
        this.PASSWORD = password;
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Object[][] getOrders() {
        CallableStatement callableStatement = null;
        List<Object[]> ordersList = new ArrayList<>();
        String sql = "{call public.get_clients()}"; 

        try {
            callableStatement = connection.prepareCall(sql);
            ResultSet resultSet = callableStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();
            //проходим по всем строкам результата + заполняем значения в массив
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                ordersList.add(row);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return new Object[0][0];
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return ordersList.toArray(new Object[0][]);
    }

    public Object[][] searchOrderClientId(int desired) {
        CallableStatement callableStatement = null;
        List<Object[]> ordersList = new ArrayList<>();
        String sql = "{call public.search_client_ID(?)}";

        try {
            callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, desired);

            ResultSet resultSet = callableStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                ordersList.add(row);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return new Object[0][0];
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return ordersList.toArray(new Object[0][]);
    }

    public Object[][] searchOrderFullName(String desired) {
        CallableStatement callableStatement = null;
        List<Object[]> ordersList = new ArrayList<>();
        String sql = "{call public.search_client_name(?)}";

        try {
            callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, desired);

            ResultSet resultSet = callableStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                ordersList.add(row);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return new Object[0][0];
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return ordersList.toArray(new Object[0][]);
    }

    public Object[][] searchOrderPhoneNumber(String desired) {
        CallableStatement callableStatement = null;
        List<Object[]> ordersList = new ArrayList<>();
        String sql = "{call public.search_client_phone(?)}";

        try {
            callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, desired);

            ResultSet resultSet = callableStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                ordersList.add(row);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return new Object[0][0];
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return ordersList.toArray(new Object[0][]);
    }

    public Object[][] searchOrderContactInfo(String desired) {
        CallableStatement callableStatement = null;
        List<Object[]> ordersList = new ArrayList<>();
        String sql = "{call public.search_contact_info(?)}";

        try {
            callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, desired);

            ResultSet resultSet = callableStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                ordersList.add(row);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return new Object[0][0];
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return ordersList.toArray(new Object[0][]);
    }

    public boolean addOrder(int client_id, String full_name, String phone_number, String contact_info) {
        String sql = "{call public.add_client(?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            //устанавливаем значения параметра для хранимой процедуры
            callableStatement.setInt(1, client_id);
            callableStatement.setString(2, full_name);
            callableStatement.setString(3, phone_number);
            callableStatement.setString(4, contact_info);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrder(int client_id, String full_name, String phone_number, String contact_info) {
        String sql = "{call public.update_client(?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, client_id);
            callableStatement.setString(2, full_name);
            callableStatement.setString(3, phone_number);
            callableStatement.setString(4, contact_info);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public boolean deleteOrderClientId(int desired) {
        String sql = "{call public.delete_client_id(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, desired);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderFullName(String desired) {
        String sql = "{call public.delete_client_name(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setString(1, desired);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderPhoneNumber(String desired) {
        String sql = "{call public.delete_client_number(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setString(1, desired);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderContactInfo(String desired) {
        String sql = "{call public.delete_contact_info(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setString(1, desired);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearTable() {
        String sql = "{call public.clear_clients()}"; 
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dropDatabase(String dbName) {
        Statement statement = null;
        try {
            connection.close();
            this.start_connection = DriverManager.getConnection(POSTGRESQL_URL, USER, PASSWORD);

            statement = start_connection.createStatement();
            String dropDbSQL = "DROP DATABASE " + dbName + ";";
            statement.executeUpdate(dropDbSQL);
            return true; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public boolean addUser(String user, String password, boolean isAdmin) {
        String sql;
        if (isAdmin){
            sql = "{call public.add_admin(?, ?)}";
        } else{
            sql = "{call public.add_user(?, ?)}";
        }
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setObject(1, user, Types.VARCHAR);
            callableStatement.setObject(2, password, Types.VARCHAR);

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exit(){
        try{
            this.connection.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
