package view;

import logic.LineInvoice;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель таблицы для отображения информации о строках счета в окне "Добавления/Редактирования счета"
 */
public class EditingTableModel extends AbstractTableModel {
    private String[] columnsNames = {"Название", "Количество", "Цена", "Сумма"};
    private List<LineInvoice> listLinesInvoice;
    private int sumAllLinesInvoice;

    public EditingTableModel() {
        this(new ArrayList<>());
    }

    public EditingTableModel(List<LineInvoice> listLinesInvoice) {
        this.listLinesInvoice = listLinesInvoice;
        calculateSumAllLinesInvoice();
    }

    @Override
    public int getRowCount() {
        return listLinesInvoice.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= 0 && columnIndex >= 0) {
            LineInvoice tmp = listLinesInvoice.get(rowIndex);
            switch (columnIndex) {
                case 0: return tmp.getDescription();
                case 1: return String.format("%,d", tmp.getAmount());
                case 2: return String.format("%,d", tmp.getPrice());
                case 3: return String.format("%,d", tmp.getSum());
            }
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex >= 0 && columnIndex >= 0) {
            LineInvoice tmp = listLinesInvoice.get(rowIndex);
            int oldSumm = tmp.getSum();
            switch (columnIndex) {
                case 0: tmp.setDescription((String) aValue); break;
                case 1: tmp.setAmount((Integer) aValue); break;
                case 2: tmp.setPrice((Integer) aValue); break;
                case 3: tmp.setSum((Integer) aValue); break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
            sumAllLinesInvoice = getSumAllLinesInvoice() - oldSumm + tmp.getSum();
        }
    }

    /**
     * Метод подсчитывает общую сумму всех строк в счете
     * */
    private void calculateSumAllLinesInvoice() {
        int tmpSum = 0;
        if(getRowCount() > 0) {
            for(LineInvoice tmpLineInvoice : listLinesInvoice) {
                tmpSum += tmpLineInvoice.getSum();
            }
            sumAllLinesInvoice = tmpSum;
        }
    }

    /**
     * Метод возвращает общую сумму всех строк в счете
     *
     * @return сумма всех строк
     * */
    public int getSumAllLinesInvoice() {
        return sumAllLinesInvoice;
    }

    /**
     * Метод добавляет новую строку с данными в модель таблицы
     * обновляет саму модель и увеличивает общую сумму всех строк в счете
     *
     * @param newLineInvoice новая добавляемая строка
     * */
    public void addNewRow(LineInvoice newLineInvoice) {
        listLinesInvoice.add(newLineInvoice);
        fireTableRowsInserted(getRowCount(), getRowCount());
        sumAllLinesInvoice = getSumAllLinesInvoice() + newLineInvoice.getSum();
    }

    /**
     * Метод удаляет строку с данными из списка модели таблицы
     * обновляет саму модель и уменьшает общую сумму всех строк в счете
     *
     * @param indexRow индекс удаляемой строки
     * */
    public void deleteRow(int indexRow) {
        if(listLinesInvoice.size() > 0 && indexRow >= 0) {
            LineInvoice deletingLineInvoice = listLinesInvoice.remove(indexRow);
            fireTableRowsDeleted(indexRow, indexRow);
            sumAllLinesInvoice = getSumAllLinesInvoice() - deletingLineInvoice.getSum();
        }
    }

    /**
     * Метод возвращает список строк в счете
     *
     * @return список всех строк
     * */
    public List<LineInvoice> getListLinesInvoice() {
        return listLinesInvoice;
    }
}
