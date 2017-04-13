import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * Программа "Простая демонстрация многопоточности"
 *
 * @author Герасенков Евгений
 * @version 1.0
 * */
public class SimpleCurrents {
    private JFrame frame;
    private Square[] array;
    private boolean isCancel;

    public static void main(String[] args) {
        new SimpleCurrents().buildGui();
    }

    private void buildGui() {
        frame = new JFrame("Simple Currents");
        createDanceFloor();
        createButton();
        createMenu();
        frame.setBounds(400, 200, 250, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createDanceFloor() {
        JPanel panel = new JPanel(new GridLayout(12, 12, 4, 4));
        array = new Square[12*12];
        for(int i = 0; i < 12*12; i++) {
            Square square = new Square();
            panel.add(square);
            array[i] = square;
        }
        frame.add(panel);
    }

    private void createButton() {
        JPanel panel = new JPanel();
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancel = false;
                start.setEnabled(false);
                stop.setEnabled(true);
                for(Square square : array)
                    new Thread(square).start();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancel = true;
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });
        stop.setEnabled(false);
        panel.add(start);
        panel.add(stop);
        frame.add(panel, BorderLayout.SOUTH);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Справка");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Проcтой пример многопоточности Java.",
                        "Авторство: Герасенков Евгений", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(about);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private class Square extends JPanel implements Runnable{
        Random random = new Random();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(random.nextInt(0xFFFFFF)));
            g.fillRect(0, 0, 15, 15);
        }

        @Override
        public void run() {
            while(!isCancel) {
                repaint();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
