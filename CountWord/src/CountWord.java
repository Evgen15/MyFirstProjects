import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Программа "Подсчет слова в тексте файла"
 *
 * @author Герасенков Евгений
 * @version 1.0
 * */
public class CountWord {
    private JFrame frame;
    private JButton buttonChooseFile;
    private JButton buttonCount;
    private JTextField word;
    private JTextArea textCountWord;
    private JProgressBar progress;
    private int count;
    private File file;

    public static void main(String[] args) {
        new CountWord().buildGui();
    }

    /**
     * Метод создает основной ГПИ.
     * */
    private void buildGui() {
        frame = new JFrame("Подсчет слова");

        createPanel();
        createMenu();

        frame.pack();
        frame.setLocation(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Метод создает основую панель:
     * - слева создаются всмпомогательные надписи;
     * - создается кнопка выбора файла;
     * - создается поле для ввода слова, которое необходимо посчитать в файле;
     * - создается прогресс бар для индикации выполнения программы.
     * */
    private void createPanel() {
        word = new JTextField();
        textCountWord = new JTextArea();
        progress = new JProgressBar();

        JPanel panel = new JPanel(new GridLayout(3, 3, 4, 4));
        buttonChooseFile = new JButton("Нажмите для выбора");
        buttonChooseFile.addActionListener(new ChooseFile());
        buttonCount = new JButton("Посчитать");
        buttonCount.addActionListener(new Count());
        panel.add(new JLabel("Выберите текстовый файл c кодировкой UTF-8"));
        panel.add(new JLabel("Введите слово, которое нужно посчитать в файле"));
        panel.add(buttonChooseFile);
        panel.add(word);
        panel.add(buttonCount);
        panel.add(textCountWord);
        panel.setBackground(Color.WHITE);
        frame.add(panel);

        frame.add(new JPanel().add(progress), BorderLayout.SOUTH);
    }

    /**
     * Метод создает и добавляет меню "Справка - О программе".
     * */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Справка");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new About(frame).setVisible(true);
            }});
        menu.add(about);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Меню "Справка - О программе"
     * */
    private class About extends JDialog {
        public About (JFrame frame) {
            super(frame, "О программе", true);
            add(new JLabel("<html><h2>Программа для подсчета встречаемости слова в тексте файла.</h2>Программа работает с текстовыми файлами в кодировке UTF-8.<hr>"+
                    "<p align=\"right\"><i>автор Евгений Герасенков</i></p></html>"));
            setResizable(false);
            pack();
            setBounds(400, 200 , 350, 170);
        }
    }

    /**
     * Класс служит для выбора файла.
     * */
    private class ChooseFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            //Установка фильтра только на текстовые файлы с раширением .txt
            fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовый файл (.txt)", "txt"));
            //Отключение в окне chooser`a возможности выбора "all files".
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.showOpenDialog(frame);
            file = fileChooser.getSelectedFile();
        }
    }

    /**
     * Класс служит для запуска подсчета встречаемости слова и вывода результата.
     * */
    private class Count implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (file != null) {
                progress.setIndeterminate(true);
                buttonChooseFile.setEnabled(false);
                buttonCount.setEnabled(false);
                new Thread(){
                    @Override
                    public void run() {
                        loadfile(file);
                        textCountWord.setFont(new Font("Arial", Font.BOLD, 13));
                        textCountWord.setText(String.format("Слово \"%s\" встречается в тексте файла %,d раз(а).", word.getText(), count));
                        frame.pack();
                        count = 0;
                        progress.setIndeterminate(false);
                        buttonChooseFile.setEnabled(true);
                        buttonCount.setEnabled(true);
                    }
                }.start();
            }
        }
    }

    /**
     * Метод подсчета, в котором непосредственно происходит посчет встречаемости слова.
     * Слово пользователя считывается и приводится в нижний регистр.
     * В полученной (в аргументе) строке с помощью RegEx производится замена символов, неотносящихся к словам, на пробелы.
     * И также весь текст обрататывается в нижний регистр.
     * В цикле разбиваем большую строку на отдельные слова по пробелам и производим подсчет при наличии совпадений.
     * */
    private void countWord(String line) {
        String wordUser = word.getText().toLowerCase();
        line = line.replaceAll("\\s|[^\\wА-Яа-я]", " ").toLowerCase();
        for (String tmp : line.split(" +")) {
            if (tmp.equals(wordUser)) count++;
        }
    }

    /**
     * Метод читает построчно файл и передает строку в метод подсчета.
     * */
    private void loadfile(File file) {
        try(Scanner scanner = new Scanner(file,"UTF-8")) {
            while(scanner.hasNextLine()) {
                countWord(scanner.nextLine());
            }
        } catch(IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Не удалось загрузить файл", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
