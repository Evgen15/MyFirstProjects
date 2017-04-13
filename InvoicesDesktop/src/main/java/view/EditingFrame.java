package view;

import control.ControllerOfEditingFrame;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс дополнительного окна "Добавления/Редактирования счета"
 */
public class EditingFrame extends JDialog {
    private Object[] sellers;
    private JTextField numberField;
    private JTextField dateField;
    private JComboBox nameCombobox;
    private JTextField addressField;
    private JTable linesTable;
    private JTextField totalField;
    private JTextField titleField;
    private JTextField amountField;
    private JTextField priceField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton OkButton;
    private JButton cancelButton;

    public EditingFrame(Object[] sellers) {
        this.sellers = sellers;
        setFrameOptions();
    }

    /**
     * Дополнительные настройки фрейма
     * */
    private void setFrameOptions() {
        add(createNumberDatePanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createOkCancelPanel(), BorderLayout.SOUTH);
        setBounds(100, 100, 500, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int) d.getWidth() - getWidth()) / 2, ((int)d.getHeight() - getHeight()) / 2);
        setResizable(false);
        // Делаем данное окно приоритетным
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Вспомогательный метод для настройки отдельных параметров представления таблицы.
     * Вызывается при создании новой модели таблицы.
     * */
    private void setTableOptions() {
        linesTable.getColumn("Название").setMinWidth(200);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        linesTable.getColumn("Количество").setCellRenderer(right);
        linesTable.getColumn("Цена").setCellRenderer(right);
        linesTable.getColumn("Сумма").setCellRenderer(right);
    }

    /**
     * Создание основной панели, на которой размещаются все элементы ГПИ
     *
     * @return новая созданная панель
     * */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(createSellerPanel());
        mainPanel.add(createLinesPanel());
        mainPanel.add(createTotalPanel());
        mainPanel.add(createThreeButtonsPanel());
        mainPanel.add(createModifyingPanel());
        return mainPanel;
    }

    /**
     * Создание панели с номером и датой счета
     *
     * @return новая созданная панель
     * */
    private JPanel createNumberDatePanel() {
        JLabel numberLabel = new JLabel("Номер ");
        numberField = new JTextField(5);
        numberField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        numberField.setPreferredSize(new Dimension(0, 25));
        JLabel dateLabel = new JLabel("Дата ");
        dateField = new JTextField(7);
        dateField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dateField.setPreferredSize(new Dimension(0, 25));
        JPanel numberDatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        numberDatePanel.add(numberLabel);
        numberDatePanel.add(numberField);
        numberDatePanel.add(dateLabel);
        numberDatePanel.add(dateField);
        return numberDatePanel;
    }

    /**
     * Создание панели с информацией о продавце
     *
     * @return новая созданная панель
     * */
    private JPanel createSellerPanel() {
        JLabel nameLabel = new JLabel("Наименование ");
        nameCombobox = new JComboBox();
        nameCombobox.setBackground(Color.WHITE);
        nameCombobox.setModel(new DefaultComboBoxModel(sellers));
        nameCombobox.setName("SELLER");
        JLabel addressLabel = new JLabel("Адрес ");
        addressField = new JTextField();
        addressField.setEditable(false);
        addressField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addressField.setBackground(Color.WHITE);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
        namePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        namePanel.add(nameLabel);
        namePanel.add(nameCombobox);
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.LINE_AXIS));
        addressPanel.add(Box.createRigidArea(new Dimension(59, 25)));
        addressPanel.add(addressLabel);
        addressPanel.add(addressField);
        JPanel sellerPanel = new JPanel(new BorderLayout(0, 3));
        sellerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Продавец"));
        sellerPanel.add(namePanel, BorderLayout.NORTH);
        sellerPanel.add(addressPanel, BorderLayout.SOUTH);
        return sellerPanel;
    }

    /**
     * Создание панели с информацией о строках в счете (таблица)
     *
     * @return новая созданная панель
     * */
    private JPanel createLinesPanel() {
        linesTable = new JTable();
        linesTable.setModel(new EditingTableModel());
        setTableOptions();
        JPanel linesPanel = new JPanel(new BorderLayout());
        linesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Позиции в счете"));
        linesPanel.add(new JScrollPane(linesTable));
        return linesPanel;
    }

    /**
     * Создание панели с информацией об итоговой сумме счета
     *
     * @return новая созданная панель
     * */
    private JPanel createTotalPanel() {
        JLabel totalLabel = new JLabel("Итого ");
        totalField = new JTextField(10);
        totalField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        totalField.setBackground(Color.WHITE);
        totalField.setPreferredSize(new Dimension(0, 25));
        totalField.setHorizontalAlignment(SwingConstants.RIGHT);
        totalField.setEditable(false);
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);
        totalPanel.add(totalField);
        return totalPanel;
    }

    /**
     * Создание панели с тремя кнопками управления строками в счете
     *
     * @return новая созданная панель
     * */
    private JPanel createThreeButtonsPanel() {
        addButton = new JButton("Добавить позицию");
        addButton.setName("ADD");
        updateButton = new JButton("Обновить позицию");
        updateButton.setName("UPDATE");
        deleteButton = new JButton("Удалить позицию");
        deleteButton.setName("DELETE");
        JPanel threeButtonsPanel = new JPanel();
        threeButtonsPanel.add(addButton);
        threeButtonsPanel.add(updateButton);
        threeButtonsPanel.add(deleteButton);
        return threeButtonsPanel;
    }

    /**
     * Создание панели модицикации строк в счете
     *
     * @return новая созданная панель
     * */
    private JPanel createModifyingPanel() {
        JLabel titleLabel = new JLabel("Название ");
        titleField = new JTextField(38);
        titleField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleField.setPreferredSize(new Dimension(0, 25));
        JLabel priceLabel = new JLabel("Цена ");
        priceField = new JTextField(10);
        priceField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        priceField.setPreferredSize(new Dimension(0, 25));
        JLabel amountLabel = new JLabel("Количество ");
        amountField = new JTextField(10);
        amountField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        amountField.setPreferredSize(new Dimension(0, 25));
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);
        JPanel Panel2 = new JPanel();
        Panel2.add(amountLabel);
        Panel2.add(amountField);
        Panel2.add(priceLabel);
        Panel2.add(priceField);
        JPanel modifyingPanel = new JPanel(new BorderLayout(0, 5));
        modifyingPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        modifyingPanel.add(titlePanel, BorderLayout.NORTH);
        modifyingPanel.add(Panel2, BorderLayout.SOUTH);
        return modifyingPanel;
    }

    /**
     * Создание панели с кнопками "ОК" и "Отмена"
     *
     * @return новая созданная панель
     * */
    private JPanel createOkCancelPanel() {
        OkButton = new JButton("ОК");
        OkButton.setName("OK");
        cancelButton = new JButton("Отмена");
        cancelButton.setName("CANCEL");
        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okCancelPanel.add(OkButton);
        okCancelPanel.add(cancelButton);
        return okCancelPanel;
    }

    /**
     * Метод регистрирует слушателя событий для всех необходимых компонентов
     *
     * @param controller слушатель событий
     * */
    public void addController(ControllerOfEditingFrame controller) {
        nameCombobox.addActionListener(controller);
        linesTable.addMouseListener(controller);
        addButton.addActionListener(controller);
        updateButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
        OkButton.addActionListener(controller);
        cancelButton.addActionListener(controller);
    }

    /**
     * Метод возвращает экземпляр продавца, выбранного в выпадающем списке
     *
     * @return выбранный элемент типа Object, представляющий экземпляр продавца, из выпадающего списка
     * */
    public Object getSelectedSeller() {
        return nameCombobox.getSelectedItem();
    }

    /**
     * Метод устанавливает новый экземпляр продавца в выпадающем списке
     *
     * @param newSeller новый элемент типа Object, представляющий экземпляр продавца
     * */
    public void setSeller(Object newSeller) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                nameCombobox.setSelectedItem(newSeller);
            }
        });
    }

    /**
     * Метод устанавливает новый адрес продавца в соответствующем поле
     *
     * @param newAddress новый адрес продавца
     * */
    public void setAddressSeller(String newAddress) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                addressField.setText(newAddress);
            }
        });
    }

    /**
     * Возвращает информацию о выбранной строке в таблице
     *
     * @return массив типа String с информацией о строке
     * */
    public String[] getInfoSelectedLine() {
        String[] array = new String[3];
        array[0] = titleField.getText();
        array[1] = amountField.getText();
        array[2] = priceField.getText();
        return array;
    }

    /**
     * Метод устанавливает информацию о выбранной строке в виде значений в соответствующие поля
     *
     * @param indexRow индекс выбранной строки
     * */
    public void setInfoSelectedLine(int indexRow) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                titleField.setText(linesTable.getValueAt(indexRow, 0).toString());
                amountField.setText(linesTable.getValueAt(indexRow, 1).toString().replaceAll("\\D",""));
                priceField.setText(linesTable.getValueAt(indexRow, 2).toString().replaceAll("\\D",""));
            }
        });
    }

    /**
     * Метод возвращает текущую модель таблицы
     *
     * @return текущая модель таблицы
     * */
    public EditingTableModel getTableModel() {
        return (EditingTableModel) linesTable.getModel();
    }

    /**
     * Метод устанавливает новую модель таблицы
     *
     * @param newTableModel новая модель таблицы
     * */
    public void setTableModel(EditingTableModel newTableModel) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                linesTable.setModel(newTableModel);
                setTableOptions();
            }
        });
    }

    /**
     * Метод обновляет значение в соответствующем поле "Итого" в формате поразрядного целого числа
     *
     * @param newValue новое значение
     * */
    public void updateTotal(int newValue) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                totalField.setText(String.format("%,d", newValue));
            }
        });
    }

    /**
     * Метод возвращает индекс выбранной строки в таблице
     *
     * @return индекс выбранной строки
     * */
    public int getIndexSelectedRow() {
        return linesTable.getSelectedRow();
    }

    /**
     * Метод возвращает номер счета
     *
     * @return номер счета
     * */
    public String getNumberInvoice() {
        return numberField.getText();
    }

    /**
     * Метод устанавливает номер счета в соответствующее поле в формате поразрядного целого числа
     *
     * @param newNumber новый номер счета
     * */
    public void setNumberInvoice(int newNumber) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                numberField.setText(String.format("%d", newNumber));
            }
        });
    }

    /**
     * Метод возвращает дату счета
     *
     * @return дата счета
     * */
    public String getDateInvoice() {
        return dateField.getText();
    }

    /**
     * Метод устанавливает дату счета в соответствующее поле в формате день месяц год (31.12.2017)
     *
     * @param newDate новая дата счета
     * */
    public void setDateInvoice(Date newDate) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                dateField.setText(new SimpleDateFormat("dd.MM.yyyy").format(newDate));
            }
        });
    }
}
