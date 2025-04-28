package org.example.diplom_fx;

import java.sql.*;

public class DB {
    //    устанавливаем соединение
    private final String HOST = "localhost";
    private final String PORT = "8889";
    private final String DB_NAME = "link_diplom";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConn() throws ClassNotFoundException, SQLException {
        String get_Str = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(get_Str, LOGIN, PASS);
        return dbConn;
    }
    //    проверка на подключение
    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConn();
        System.out.println(dbConn.isValid(1000));
    }
    public void addLink (String full_link, String short_link) {
        //        пишем sql команду, в values будут подставлены значения
        String sql = "INSERT INTO `link` (`full_link`, `short_link`) VALUES (?, ?)";
        try {
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
//            устанавливаем  в вопросы параметры
            prSt.setString(1, full_link);
            prSt.setString(2, short_link);
//            добавляем пользователя
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
//    метод поиска идентичных ссылок
    public boolean isExistsLink (String short_link) {
        String sql = "SELECT `id` FROM `link` WHERE `short_link` = ?";
        try {
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
            prSt.setString(1, short_link);

            ResultSet res = prSt.executeQuery();
            return res.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
//    получение из БД всех сокращенных ссылок
    public ResultSet getLinks() throws SQLException, ClassNotFoundException {
        String sql = "SELECT `short_link` FROM `link`";
        Statement statement = null;

        statement = getDbConn().createStatement();
        return statement.executeQuery(sql);
    }
}
