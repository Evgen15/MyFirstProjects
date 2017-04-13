package logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс, предоставляющий соединение с базой данных
 */
public class Database {

    private Database() {}

    /**
     * Метод попытается зарегистрировать драйвер JDBC на основании необходимых параметров,
     * которые в свою очередь загружаются из XML файла, и установить соединение с базой данных.
     *
     * @return соединение с базой данных
     * */
    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties props = new Properties();
        props.loadFromXML(new FileInputStream("resources/ConfigDB.xml"));
        Class.forName(props.getProperty("driver"));
        String url = props.getProperty("url");
        return DriverManager.getConnection(url, props);
    }
}
