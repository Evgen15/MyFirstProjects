package control;

import logic.*;
import view.EditingFrame;
import view.EditingTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс контролирует действия пользователя в дополнительном окне "Добавления/Редактирования счета"
 */
public class ControllerOfEditingFrame extends MouseAdapter implements ActionListener {
    private EditingFrame view;
    private Invoice invoice;

    public ControllerOfEditingFrame(EditingFrame view) {
        this.view = view;
        view.addController(this);
        view.setAddressSeller(((Seller) view.getSelectedSeller()).getAddress());
    }

    public ControllerOfEditingFrame(EditingFrame view, Invoice invoice) {
        this(view);
        this.invoice = invoice;
        loadInvoiceOptions();
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
            case "SELLER": changeAddressSeller(); break;
            case "ADD": addLineInvoice(); break;
            case "UPDATE": updateLineInvoice(); break;
            case "DELETE": deleteLineInvoice(); break;
            case "OK": save(); break;
            case "CANCEL": exit(); break;
        }

    }

    /**
     * Метод заполняет соответствующие поля для дальнейшего редактирования выделенной строки счета
     *
     * @param e ссылка на объект "Событие"
     * */
    @Override
    public void mouseClicked(MouseEvent e) {
        int indexRow = view.getIndexSelectedRow();
        if(indexRow >= 0 ) {
            view.setInfoSelectedLine(indexRow);
        }
    }

    /**
     * Метод загружает информацию о счете в соответствующие поля окна в режиме редактирования счета
     * */
    private void loadInvoiceOptions() {
        view.setNumberInvoice(invoice.getNumber());
        view.setDateInvoice(invoice.getDate());
        view.setSeller(invoice.getSeller());
        view.setAddressSeller(invoice.getSeller().getAddress());
        view.updateTotal(invoice.getSum());
        view.setTableModel(new EditingTableModel(invoice.getListLines()));
    }

    /**
     * Метод меняет адрес продавца в соответствующем поле представления в зависимости от выбранного продавца
     * */
    private void changeAddressSeller() {
        Seller seller = (Seller) view.getSelectedSeller();
        view.setAddressSeller(seller.getAddress());
    }

    /**
     * Метод добавляет новую строку в таблицу и обновляет поле "Итого"
     * */
    private void addLineInvoice() {
        try {
            String description = view.getInfoSelectedLine()[0];
            if(description.equals("")) throw new RuntimeException();
            int amount = Integer.parseInt(view.getInfoSelectedLine()[1]);
            int price = Integer.parseInt(view.getInfoSelectedLine()[2]);
            int sum = amount * price;
            EditingTableModel tableModel = view.getTableModel();
            LineInvoice tmpLine = new LineInvoice();
            tmpLine.setDescription(description);
            tmpLine.setAmount(amount);
            tmpLine.setPrice(price);
            tmpLine.setSum(sum);
            tableModel.addNewRow(tmpLine);
            view.updateTotal(tableModel.getSumAllLinesInvoice());
        } catch (NumberFormatException e) {
            ExceptionsHandler.showMessageAboutInvalidAmountOrPrice();
        } catch (RuntimeException e) {
            ExceptionsHandler.showMessageAboutInvalidDescriptionItem();
        }
    }

    /**
     * Метод обновляет выделенную строку в таблице и обновляет поле "Итого"
     * */
    private void updateLineInvoice() {
        int indexRow = view.getIndexSelectedRow();
        if(indexRow >= 0) {
            try{
                String description = view.getInfoSelectedLine()[0];
                if(description.equals("")) throw new RuntimeException();
                int amount = Integer.parseInt(view.getInfoSelectedLine()[1]);
                int price = Integer.parseInt(view.getInfoSelectedLine()[2]);
                int sum = amount * price;
                EditingTableModel tableModel = view.getTableModel();
                tableModel.setValueAt(description, indexRow, 0);
                tableModel.setValueAt(amount, indexRow, 1);
                tableModel.setValueAt(price, indexRow, 2);
                tableModel.setValueAt(sum, indexRow, 3);
                view.updateTotal(tableModel.getSumAllLinesInvoice());
            } catch (NumberFormatException e) {
                ExceptionsHandler.showMessageAboutInvalidAmountOrPrice();
            } catch (RuntimeException e) {
                ExceptionsHandler.showMessageAboutInvalidDescriptionItem();
            }
        }
    }

    /**
     * Метод удаляет выделенную строку в таблице и обновляет поле "Итого"
     * */
    private void deleteLineInvoice() {
        int indexRow = view.getIndexSelectedRow();
        if(indexRow >= 0) {
            EditingTableModel tableModel = view.getTableModel();
            tableModel.deleteRow(indexRow);
            view.updateTotal(tableModel.getSumAllLinesInvoice());
        }
    }

    /**
     * Метод сохраняет изменения в базу данных и закрывает окно "Добавления/Редактирования счета"
     * */
    private void save() {
        new Thread() {
            @Override
            public void run() {
                try {
                    int number = Integer.parseInt(view.getNumberInvoice());
                    Date date =  new SimpleDateFormat("dd.MM.yyyy").parse(view.getDateInvoice());
                    Seller seller = (Seller) view.getSelectedSeller();
                    EditingTableModel tableModel = view.getTableModel();
                    int sum = tableModel.getSumAllLinesInvoice();
                    List<LineInvoice> listLines = tableModel.getListLinesInvoice();
                    if(invoice == null) {
                        invoice = new Invoice();
                        setInvoiceOptions(number, date, seller, sum, listLines);
                        ManagementModel.saveNewInvoice(invoice);
                    } else {
                        setInvoiceOptions(number, date, seller, sum, listLines);
                        ManagementModel.updateInvoice(invoice);
                    }
                    exit();
                } catch (NumberFormatException e) {
                    ExceptionsHandler.showMessageAboutInvalidNumber();
                } catch (ParseException e) {
                    ExceptionsHandler.showMessageAboutInvalidDate();
                } catch (Exception e) {
                    ExceptionsHandler.showMessageAboutGeneralExceptions(e);
                }
            }
        }.start();
    }

    /**
     * Метод устанавливает переданные значения в соответствующие поля счета
     *
     * @param number номер счета
     * @param date дата счета
     * @param seller продавец
     * @param sum сумма счета
     * @param listLines строки в счете
     * */
    private void setInvoiceOptions(int number, Date date, Seller seller, int sum, List<LineInvoice> listLines) {
        invoice.setNumber(number);
        invoice.setDate(date);
        invoice.setSum(sum);
        invoice.setSeller(seller);
        invoice.setListLines(listLines);
    }

    /**
     * Метод закрывает окно представления
     *
     *  <p><b>
     * Примечание:
     * </b></p>
     * если вызывается при нажатии кнопки "cancel", то окно закрывается без сохранения изменений
     *
     * */
    private void exit() {
        view.dispose();
    }
}
