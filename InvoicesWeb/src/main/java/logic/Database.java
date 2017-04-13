package logic;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, предоставляющий соединение с базой данных
 */
public class Database {
    private static DataSource source;

    /**
     * В статическом блоке происходит попытка установить соединение с базой данных
     * посредством JNDI, на основании параметров из web.xml и context.xml.
     */
    static {
        try {
            Context initCtx = new InitialContext();
            source = (DataSource) initCtx.lookup("java:comp/env/jdbc/InvoicesDB");
        } catch (NamingException e) {
            Logger.getLogger("Database").log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Database() {}

    /**
     * Метод возвращает соединение с базой данных
     *
     * @return соединение с базой данных
     * */
    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
