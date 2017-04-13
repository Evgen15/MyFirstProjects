package view;

import control.ControllerOfMainFrame;
import control.ExceptionsHandler;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Класс главного окна программы
 */
public class MainFrame extends JFrame {
    private JProgressBar progressBar;
    private JTable table;
    private JLabel quantityInvoices;
    private JButton buttonPeriod;
    private JButton buttonRefresh;
    private JButton buttonAdd;
    private JButton buttonEdit;
    private JButton buttonRemove;
    private JMenuItem exit;

    /**
     * В конструкторе происходит формирование графического интерфейса программы
     * */
    public MainFrame() {
        JPanel panelTable = new JPanel();
        table = new JTable(new MainTableModel());
        panelTable.add(new JScrollPane(table));
        setTableOptions();

        JPanel panelQuantity = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityInvoices = new JLabel("Количество счетов: 0");
        panelQuantity.add(quantityInvoices);

        progressBar = new JProgressBar();
        progressBar.setString("обработка данных...");
        progressBar.setVisible(false);

        buttonPeriod = new JButton("Указать период");
        buttonPeriod.setName("PERIOD");
        buttonRefresh = new JButton("Обновить");
        buttonRefresh.setName("REFRESH");
        buttonAdd = new JButton("Добавить счет");
        buttonAdd.setName("ADD");
        buttonEdit = new JButton("Редактировать счет");
        buttonEdit.setName("EDIT");
        buttonRemove = new JButton("Удалить счет");
        buttonRemove.setName("REMOVE");

        JPanel panelTopButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        panelTopButtons.setBorder(BorderFactory.createEmptyBorder(40, 0, 60, 5));
        panelTopButtons.add(buttonPeriod);
        panelTopButtons.add(buttonRefresh);
        panelTopButtons.add(progressBar);

        JPanel panelLowerButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        panelLowerButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 5));
        panelLowerButtons.add(buttonAdd);
        panelLowerButtons.add(buttonEdit);
        panelLowerButtons.add(buttonRemove);

        JPanel panelAllButtons= new JPanel(new GridLayout(2, 1));
        panelAllButtons.add(panelTopButtons);
        panelAllButtons.add(panelLowerButtons);

        add(panelTable, BorderLayout.CENTER);
        add(panelAllButtons, BorderLayout.EAST);
        add(panelQuantity, BorderLayout.SOUTH);
        setTitle("Счета");
        createMenu();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        // Получаем размеры экрана пользователя
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // Размещаем окно программы по центру, вычисляя координаты на основе полученной информации
        setLocation(((int) d.getWidth() - getWidth()) / 2, ((int)d.getHeight() - getHeight()) / 2);
        setResizable(false);
    }

    /**
     * Вспомогательный метод для настройки отдельных параметров представления таблицы.
     * Вызывается при создании новой модели таблицы.
     * */
    private void setTableOptions() {
        // минимальные размеры столбов
        table.getColumn("Номер").setMinWidth(50);
        table.getColumn("Дата").setMinWidth(80);
        table.getColumn("Продавец").setMinWidth(150);
        // выравнивание столбца "Сумма" по правому краю
        DefaultTableCellRenderer aligRight = new DefaultTableCellRenderer();
        aligRight.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumn("Сумма").setCellRenderer(aligRight);
    }

    /**
     * Метод создает и добавляет меню для корректного выхода из программы
     * */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Выход / Exit");
        exit = new JMenuItem("Выйти / Exit");
        exit.setName("EXIT");
        menu.add(exit);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    /**
     * Метод регистрирует слушателя событий для всех необходимых компонентов
     *
     * @param controller слушатель событий
     * */
    public void addController(ControllerOfMainFrame controller) {
        buttonPeriod.addActionListener(controller);
        buttonRefresh.addActionListener(controller);
        buttonAdd.addActionListener(controller);
        buttonEdit.addActionListener(controller);
        buttonRemove.addActionListener(controller);
        exit.addActionListener(controller);
    }

    /**
     * Метод "включает" прогресс-бар
     */
    public void setProgressON() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisible(true);
                progressBar.setStringPainted(true);
                progressBar.setIndeterminate(true);
            }
        });
    }

    /**
     * Метод "выключает" прогресс-бар
     */
    public void setProgressOFF() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setStringPainted(false);
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
            }
        });
    }

    /**
     * Метод возвращает индекс выбранной строки в таблице
     *
     * @return индекс выбранной строки
     * */
    public int getIndexSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Метод возвращает текущую модель таблицы
     *
     * @return текущая модель таблицы
     * */
    public MainTableModel getTableModel() {
        return (MainTableModel) table.getModel();
    }

    /**
     * Метод устанавливает новую модель таблицы
     *
     * @param newTableModel новая модель таблицы
     * */
    public void setTableModel(MainTableModel newTableModel) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                table.setModel(newTableModel);
                setTableOptions();
            }
        });
    }

    /**
     * Метод устанавливает новое значение в соответствующем поле "Количество счетов"
     *
     * @param newValue новое значение
     * */
    public void setQuantityInvoices(int newValue) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                quantityInvoices.setText("Количество счетов: " + newValue);
            }
        });
    }

    /**
     * Метод устанавливает в заголовке временной промежуток, за который отображаются счета в таблице
     *
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * */
    public void setTitle(Date startDate, Date endDate) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setTitle("Счета за период: "
                        + new SimpleDateFormat("dd.MM.yyyy").format(startDate)
                        + " - "
                        + new SimpleDateFormat("dd.MM.yyyy").format(endDate));
            }
        });
    }

    /**
     * Метод запрашивает ввод даты, начиная с которой необходимо отобразить счета
     *
     * @return начальная дата
     * */
    public String showInputDialogStartDate() {
        return JOptionPane.showInputDialog(null,
                "Введите НАЧАЛЬНУЮ дату \n в формате ДД.ММ.ГГГГ (например: 31.12.2017)",
                "Введите начальную дату", JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Метод запрашивает ввод даты, заканчивая которой необходимо отобразить счета
     *
     * @return конечная дата
     * */
    public String showInputDialogSEndDate() {
        return JOptionPane.showInputDialog(null,
                "Введите КОНЕЧНУЮ дату \n в формате ДД.ММ.ГГГГ (например: 31.12.2017)",
                "Введите конечную дату", JOptionPane.QUESTION_MESSAGE);
    }
}
