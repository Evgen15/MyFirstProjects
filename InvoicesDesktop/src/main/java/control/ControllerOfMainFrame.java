package control;

import logic.*;
import view.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.*;
import java.util.Date;
import java.util.List;

/**
 * Класс контролирует действия пользователя в главном окне программы
 */
public class ControllerOfMainFrame implements ActionListener {
    private MainFrame view;
    private Period period;

    public ControllerOfMainFrame(MainFrame view) throws SQLException, ClassNotFoundException, IOException {
        this.view = view;
        period = new Period();
        view.addController(this);
    }

    /**
     * Метод принмиает ссылку на объект "События", далее получает имя объекта, который вызвал это событие
     * и в зависимости от имени вызывается соответсвующий метод
     *
     * @param e ссылка на объект "Событие"
     * */
    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        switch (c.getName()){
            case "PERIOD": insertPeriod(); break;
            case "REFRESH": updateTable(); break;
            case "ADD": addInvoice(); break;
            case "EDIT": editInvoice(); break;
            case "REMOVE": removeLine(); break;
            case "EXIT": exit(); break;
        }
    }

    /**
     * Метод предлагает пользователю ввести даты, за которые необходимо показать счета
     * */
    private void insertPeriod() {
        String start = view.showInputDialogStartDate();
        if(start == null) return;
        String end = view.showInputDialogSEndDate();
        if(end == null) return;
        try {
            Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse(start);
            Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse(end);
            // Устанавливаем даты непосредственно в объекте сущности "Период"
            period.setStartDate(startDate);
            period.setEndDate(endDate);
        } catch (ParseException e) {
            ExceptionsHandler.showMessageAboutInvalidDate();
        }
    }

    /**
     * Метод обновляет данные в таблице в зависимости от заданного периода.
     * В методе также предусмотрена проверка на обязательное наличие начальной и конечной дат желаемого периода.
     * */
    private void updateTable() {
        if(period.getStartDate() != null && period.getEndDate() != null) {
            view.setProgressON();
            new Thread(){
                @Override
                public void run() {
                    try {
                        List<Invoice> list = ManagementModel.findInvoicesByPeriod(period);
                        view.setTableModel(new MainTableModel(list));
                        view.setTitle(period.getStartDate(), period.getEndDate());
                        view.setQuantityInvoices(list.size());
                        view.setProgressOFF();
                    } catch (Exception e) {
                        view.setProgressOFF();
                        ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                    }
                }
            }.start();
        }
    }

    /**
     * Метод открывает окно добавления нового счета
     * */
    private void addInvoice(){
            new Thread() {
                @Override
                public void run() {
                    try {
                        List<Seller> sellers = ManagementModel.getAllSellers();
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                EditingFrame view = new EditingFrame(sellers.toArray());
                                ControllerOfEditingFrame controller = new ControllerOfEditingFrame(view);
                                view.setTitle("Добавление нового счета");
                                view.setVisible(true);
                            }
                        });
                    } catch (Exception e) {
                        ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                    }
                }
            }.start();
    }

    /**
     * Метод открывает окно редактированния выбранного счета,
     * предварительно загрузив всю необходимую информацию о нем
     * */
    private void editInvoice() {
        int indexRow = view.getIndexSelectedRow();
        if(indexRow >= 0) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        MainTableModel tableModel = view.getTableModel();
                        Invoice invoice = ManagementModel.loadInvoiceById(
                                tableModel.getSelectedInvoice(indexRow).getId());
                        List<Seller> sellers = ManagementModel.getAllSellers();
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                EditingFrame view = new EditingFrame(sellers.toArray());
                                ControllerOfEditingFrame controller =
                                        new ControllerOfEditingFrame(view, invoice);
                                view.setTitle("Редактирование счета");
                                view.setVisible(true);
                            }
                        });
                    } catch (Exception e) {
                        ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                    }
                }
            }.start();
        }
    }

    /**
     * Метод удаляет выделенную строку в моделе таблицы и в базе данных,
     * обновляет информацию о количестве счетов
     * */
    private void removeLine() {
        int indexRow = view.getIndexSelectedRow();
        if(indexRow >= 0) {
            view.setProgressON();
            new Thread(){
                @Override
                public void run() {
                    try {
                        MainTableModel tableModel = view.getTableModel();
                        ManagementModel.deleteInvoice(tableModel.getSelectedInvoice(indexRow));
                        tableModel.removeRow(indexRow);
                        view.setQuantityInvoices(tableModel.getRowCount());
                        view.setProgressOFF();
                    } catch (Exception e) {
                        view.setProgressOFF();
                        ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                    }
                }
            }.start();
        }
    }

    /**
     * Метод закрывает программу
     * */
    public void exit() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ManagementModel.close();
                    System.exit(0);
                } catch (SQLException e) {
                    ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                    System.exit(0);
                }
            }
        }.start();
    }
}
