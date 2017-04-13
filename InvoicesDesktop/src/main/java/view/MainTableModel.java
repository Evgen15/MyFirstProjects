package view;

import logic.Invoice;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель таблицы для отображения информации о счете-фактуре в главном окне
 */
public class MainTableModel extends AbstractTableModel {
    private String[] columnNames = {"Номер", "Дата", "Продавец", "Сумма"};
    private List<Invoice> list;

    public MainTableModel() {
        this(new ArrayList<>());
    }

    public MainTableModel(List<Invoice> list) {
        this.list = list;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= 0 && columnIndex >= 0) {
            Invoice invoice = list.get(rowIndex);
            switch (columnIndex) {
                case 0: return invoice.getNumber();
                case 1: return new SimpleDateFormat("dd.MM.yyyy").format(invoice.getDate());
                case 2: return invoice.getSeller().getName();
                case 3: return String.format("%,d", invoice.getSum());
            }
        }
        return "";
    }

    /**
     * Метод возвращает выделенный счет по индексу в списке
     *
     * @param index индекс объекта в списке модели таблицы
     * @return конкретный счет
     * */
    public Invoice getSelectedInvoice(int index) {
        return list.get(index);
    }

    /**
     * Метод удаляет строку из списка модели таблицы и обновляет саму модель
     *
     * @param indexRow индекс удаляемой строки
     * */
    public void removeRow(int indexRow) {
        if(list.size() > 0 && indexRow >= 0) {
            list.remove(indexRow);
            fireTableRowsDeleted(indexRow, indexRow);
        }
    }
}
