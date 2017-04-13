import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.*;
import java.util.ArrayList;

/**
 * Программа "Вывода названий и размеров файлов, находящихся в zip-архиве"
 *
 * @author Герасенков Евгений
 * @version 1.0
 */
public class InfoZipFiles {
    private JFrame frame;
    private JButton buttonChoose;
    private JButton buttonShowInfo;
    private JTable table;
    private JProgressBar progressBar;
    private File file;
    private java.util.List<FileInfo> list;

    public static void main(String[] args) {
        new InfoZipFiles().buildGui();
    }

    /**
     * Метод создания основного ГПИ
     * */
    public void buildGui() {
        frame = new JFrame("Информация о файлах из zip-архива");

        createPanel();
        addListeners();

        frame.setBounds(400, 200, 500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Метод создания компонентов ГПИ
     * */
    private void createPanel() {
        progressBar = new JProgressBar();
        buttonChoose = new JButton();
        buttonChoose.setText("Выберите zip-файл");
        buttonShowInfo = new JButton();
        buttonShowInfo.setText("Показать информацию");
        buttonShowInfo.setEnabled(false);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BorderLayout());
        panelButton.add(buttonChoose, BorderLayout.WEST);
        panelButton.add(buttonShowInfo, BorderLayout.EAST);
        panelButton.add(progressBar, BorderLayout.CENTER);

        table = new JTable();
        table.setModel(new MyTableModel());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panelButton, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel);
    }

    /**
     * Метод определения действий кнопок
     * */
    private void addListeners() {
        buttonChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Zip архив (.zip)", "zip"));
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.showOpenDialog(frame);
                file = fileChooser.getSelectedFile();
                buttonShowInfo.setEnabled(true);
            }
        });

        buttonShowInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar.setStringPainted(true);
                progressBar.setString("чтение информации...");
                progressBar.setIndeterminate(true);
                buttonChoose.setEnabled(false);
                buttonShowInfo.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();
                    }
                }).start();
            }
        });
    }

    /**
     * Метод вывода полученной информации о файлах из zip архива в таблицу ГПИ
     * Явно указана кодировка для корректной работы с именами файлов в системе Windows
     * */
    private void showInfo() {
        try(ZipInputStream zipInput =
                    new ZipInputStream(new FileInputStream(file), Charset.forName("cp866"))) {
            list = new ArrayList<FileInfo>();
            ZipEntry zipEntry;
            while((zipEntry = zipInput.getNextEntry()) != null) {
                String name = zipEntry.getName();
                String size = formatSize(zipEntry.getSize());
                list.add(new FileInfo(name, size));
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        table.setModel(new MyTableModel(list));
                    }
                });
                zipInput.closeEntry();
            }
            setDefault();
        } catch(IOException e) {
            setDefault();
            JOptionPane.showMessageDialog(frame,
                    String.format("%s%n%s", e.getClass().getSimpleName(), e.getMessage()),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Метод, формирующий строку с размером файла в байтах, килобайтах, мегабайтах, гигибайтах
     * */
    private String formatSize(double value) {
        if(value < 1000) return String.format("%.0f b", value);
        double valueInKb = value/1024;
        if((valueInKb / 1024) >= 1000) return String.format("%.2f Gb", valueInKb / (1024 * 1024));
        else if(valueInKb >= 1000) return String.format("%.2f Mb", valueInKb / 1024);
        else return String.format("%.2f Kb", valueInKb);
    }

    /**
     * Метод устанавливает настройки кнопок по умолчанию
     * и отключает прогресс бар
     * */
    private void setDefault() {
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(false);
        buttonChoose.setEnabled(true);
    }

    /**
     * Модель таблицы, которая служит для отображения и обновления полученных данных в ГПИ
     * */
    private class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"Имя файла", "Размер файла"};
        private java.util.List<FileInfo> list;

        MyTableModel() {
            list = new ArrayList<FileInfo>();
        }

        MyTableModel(java.util.List<FileInfo> list) {
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
            FileInfo tmp = list.get(rowIndex);
            switch(columnIndex) {
                case 0: return tmp.getName();
                case 1: return tmp.getSize();
            }
            return "";
        }
    }

    /**
     * Класс, представляющий сущность - информация об имени и размере файла
     * */
    private class FileInfo {
        private String name;
        private String size;

        FileInfo(String name, String size) {
            this.name = name;
            this.size = size;
        }

        String getName() {
            return name;
        }

        String getSize() {
            return size;
        }
    }
}
