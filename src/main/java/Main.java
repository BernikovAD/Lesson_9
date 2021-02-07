import java.sql.*;

public class Main {
    /*    Создать класс кота
        Создать в бд таблицу с котами
        Написать метод извлечения котов
        Метод добавления котов
        Метод удаления котов
        Метод изменения котов*/
    static String url = "jdbc:sqlite:lesson.db";
    static String sqlCommand = "CREATE TABLE if not exists CatTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT NOT NULL);";
    private static String updateStr = "insert into CatTable(Name) values ('Маркиз'),('Кира');";
    static String preparedStr = "insert into CatTable (Name) values (?)";
    private static Connection connection;
    static Statement statement;
    private static PreparedStatement ps;

    public static void main(String[] args) {
        connect ();
        try {
            statement = connection.createStatement ();
            statement.execute (sqlCommand);
            System.out.println ("Таблица создана!");
            statement.executeUpdate(updateStr);
        } catch (SQLException throwables) {
            throwables.printStackTrace ();

        }
        Cat cat1 = new Cat ("Мурзик");
        Cat cat2 = new Cat ("Пушок");
        addCat (cat1);
        addCat (cat2);
        deleteCat ("Кира");
        writeCat ();
        updateCat("Пушок","Муська");
        close ();

    }

    private static void addCat(Cat cat) {
        try {
            ps = connection.prepareStatement (preparedStr);
            ps.setString (1, cat.name);
            ps.executeUpdate ();
            System.out.printf ("Добавлен кот %s\n\r", cat.name);
        } catch (SQLException throwables) {
            throwables.printStackTrace ();
        }
    }

    private static void writeCat() {
        try {
            ResultSet rs = statement.executeQuery ("select * from CatTable;");
            System.out.print ("Все коты: ");
            while (rs.next ()) {
                System.out.print (rs.getString ("Name") + ", ");
            }
            System.out.println ();
            statement.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ();
        }
    }

    private static void deleteCat(String name) {
        try (PreparedStatement statement = connection.prepareStatement ("DELETE FROM CatTable WHERE Name = ?")) {
            statement.setObject (1, name);
            statement.executeUpdate ();
            statement.close ();
            System.out.printf ("Котик %s удален\n\r", name);
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    private static void updateCat(String name, String newName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement ("UPDATE CatTable SET name = ?")){
            preparedStatement.setString (1, newName);
            preparedStatement.executeUpdate ();
            preparedStatement.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ();
        }
    }

    private static void connect() {
        try {
            Class.forName ("org.sqlite.JDBC");
            connection = DriverManager.getConnection (url);
            System.out.println ("Подключение к БД прошло успешно!");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace ();
        }
    }

    private static void close() {
        try {
            connection.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ();
        }
    }
}
