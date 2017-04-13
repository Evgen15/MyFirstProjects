package logic;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Класс - модель управления основной логикой программы:
 * в данном случае работа с информацией в базе данных
 */
public class ManagementModel {

    private ManagementModel() {}

    /**
     * Метод ищет счета за указанный период, при этом загрузка строк в счетах не происходит
     *
     * @param period период, за который необходимо найти счета
     * @return список счетов
     */
    public static List<Invoice> findInvoicesByPeriod(Period period) throws SQLException {
        List<Invoice> result = new ArrayList<>();
        String sql = "SELECT inv_id, inv_number, inv_date, inv_sum, sel_id FROM invoices " +
                "WHERE inv_date BETWEEN ? AND ? " +
                "ORDER BY invoices.inv_date";
        try(Connection con = Database.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setDate(1, new java.sql.Date(period.getStartDate().getTime()));
            stm.setDate(2, new java.sql.Date(period.getEndDate().getTime()));
            try(ResultSet rs = stm.executeQuery()) {
                while(rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("inv_id"));
                    invoice.setNumber(rs.getInt("inv_number"));
                    invoice.setDate(rs.getDate("inv_date"));
                    invoice.setSum(rs.getInt("inv_sum"));
                    invoice.setSeller(loadSellerById(con, rs.getInt("sel_id")));
                    result.add(invoice);
                }
            }
        }
        return result;
    }

    /**
     * Метод ищет и возвращает конкретный счет по идентификатору этого счета (id)
     *
     * @param id идентификатор (id) искомого счета
     * @return конкретный счет
     */
    public static Invoice loadInvoiceById(int id) throws SQLException {
        String sql = "SELECT inv_number, inv_date, inv_sum, sel_id FROM invoices WHERE inv_id = ?";
        try(Connection con = Database.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            try(ResultSet rs = stm.executeQuery()) {
                rs.next();
                Invoice result = new Invoice();
                result.setId(id);
                result.setNumber(rs.getInt("inv_number"));
                result.setDate(rs.getDate("inv_date"));
                result.setSum(rs.getInt("inv_sum"));
                result.setSeller(loadSellerById(con, rs.getInt("sel_id")));
                result.setListLines(loadLinesInvoice(con, result));
                return result;
            }
        }
    }

    /**
     * Метод ищет и возвращает конкретного продавца по идентификатору искомого продавца (id)
     *
     * @param con соединение с базой данных
     * @param id идентификатор (id) искомого продавца
     * @return конкретный продавец
     */
    private static Seller loadSellerById(Connection con, int id) throws SQLException {
        String sql = "SELECT sel_name, sel_address FROM sellers WHERE sel_id = ?";
        try(PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            try(ResultSet rs = stm.executeQuery()) {
                rs.next();
                Seller result = new Seller();
                result.setId(id);
                result.setName(rs.getString("sel_name"));
                result.setAddress(rs.getString("sel_address"));
                return result;
            }
        }
    }

    /**
     * Метод возвращает всех продавов
     *
     * @return список всех продавцов
     */
    public static List<Seller> getAllSellers() throws SQLException {
        List<Seller> result = new ArrayList<>();
        String sql = "SELECT sel_id, sel_name, sel_address FROM sellers";
        try(Connection con = Database.getConnection(); Statement stm = con.createStatement()) {
            try(ResultSet rs = stm.executeQuery(sql)) {
                while(rs.next()) {
                    Seller seller = new Seller();
                    seller.setId(rs.getInt("sel_id"));
                    seller.setName(rs.getString("sel_name"));
                    seller.setAddress(rs.getString("sel_address"));
                    result.add(seller);
                }
                return result;
            }
        }
    }

    /**
     * Метод ищет и возвращает список строк в счете по идентификатору счета (id)
     *
     * @param con соединение с базой данных
     * @param invoice сущность счета, из которой берется идентификатор (id) счета, строки которого необходимо найти
     * @return список строк в счете
     * */
    private static List<LineInvoice> loadLinesInvoice(Connection con, Invoice invoice) throws SQLException {
        List<LineInvoice> result = new ArrayList<>();
        String sql = "SELECT g_id, g_description, g_amount, g_price, g_sum FROM goods WHERE inv_id = ?";
        try(PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, invoice.getId());
            try(ResultSet rs = stm.executeQuery()) {
                while(rs.next()) {
                    LineInvoice line = new LineInvoice();
                    line.setId(rs.getInt("g_id"));
                    line.setDescription(rs.getString("g_description"));
                    line.setAmount(rs.getInt("g_amount"));
                    line.setPrice(rs.getInt("g_price"));
                    line.setSum(rs.getInt("g_sum"));
                    result.add(line);
                }
            }
            return result;
        }
    }

    /**
     * Метод сохраняет новый счет
     *
     * @param newInvoice новый счет, который необходимо сохранить
     * */
    public static void saveNewInvoice(Invoice newInvoice) throws SQLException {
        String sql = "INSERT INTO invoices (inv_number, inv_date, inv_sum, sel_id) VALUES (?, ?, ?, ?)";
        try(Connection con = Database.getConnection(); PreparedStatement stm = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, newInvoice.getNumber());
            stm.setDate(2, new java.sql.Date(newInvoice.getDate().getTime()));
            stm.setInt(3, newInvoice.getSum());
            stm.setInt(4, newInvoice.getSeller().getId());
            stm.executeUpdate();
            try(ResultSet rs = stm.getGeneratedKeys()){
                if(rs.next()) {
                    int newId = rs.getInt(1);
                    newInvoice.setId(newId);
                    saveLinesInvoice(con, newInvoice);
                }
            }
        }
    }

    /**
     * Метод обновляет существующий счет, в том числе обновляются имеющиеся строки в счете
     *
     * @param invoice счет, который необходимо обновить
     * */
    public static void updateInvoice(Invoice invoice) throws SQLException {
        String sql = "UPDATE invoices SET inv_number = ?, inv_date = ?, inv_sum = ?, sel_id = ? WHERE inv_id = ?";
        try(Connection con = Database.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, invoice.getNumber());
            stm.setDate(2, new java.sql.Date(invoice.getDate().getTime()));
            stm.setInt(3, invoice.getSum());
            stm.setInt(4, invoice.getSeller().getId());
            stm.setInt(5, invoice.getId());
            stm.executeUpdate();
            deleteLinesInvoice(con, invoice);
            saveLinesInvoice(con, invoice);
        }
    }

    /**
     * Метод удаляет конкретный счет по идентификатору этого счета (id)
     *
     * @param invoice сущность счета, из которой берется идентификатор (id) удаляемого счета
     * */
    public static void deleteInvoice(Invoice invoice) throws SQLException {
        String sql = "DELETE FROM invoices WHERE inv_id = ?";
        try(Connection con = Database.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, invoice.getId());
            stm.executeUpdate();
        }
    }

    /**
     * Метод сохраняет строки конкретного счета
     *
     * @param con соединение с базой данных
     * @param invoice счет, строки которого необходимо сохранить
     * */
    private static void saveLinesInvoice(Connection con, Invoice invoice) throws SQLException {
        String sql = "INSERT INTO goods (g_description, g_amount, g_price, g_sum, inv_id) VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement stm = con.prepareStatement(sql)) {
            for(LineInvoice line : invoice.getListLines()) {
                stm.setString(1, line.getDescription());
                stm.setInt(2, line.getAmount());
                stm.setInt(3, line.getPrice());
                stm.setInt(4, line.getSum());
                stm.setInt(5, invoice.getId());
                stm.addBatch();
            }
            stm.executeBatch();
        }
    }

    /**
     * Метод удаляет строки в счете по идентификатору этого счета (id)
     *
     * @param con соединение с базой данных
     * @param invoice счет, строки которого необходимо удалить
     * */
    private static void deleteLinesInvoice(Connection con, Invoice invoice) throws SQLException {
        String sql = "DELETE FROM goods WHERE inv_id = ?";
        try(PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, invoice.getId());
            stm.executeUpdate();
        }
    }
}
