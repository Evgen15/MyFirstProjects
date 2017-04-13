import control.ControllerOfMainFrame;
import control.ExceptionsHandler;
import view.MainFrame;

/**
 * Программы "Invoices Desktop Edition"
 *
 * @author Герасенков Евгений
 * @version 1.0
 */
public class Invoices {
    public static void main(String[] args) {
        MainFrame view = null;
        ControllerOfMainFrame controller = null;
        try {
            view = new MainFrame();
            controller = new ControllerOfMainFrame(view);
            view.setVisible(true);
        } catch (Exception e) {
            ExceptionsHandler.showMessageAboutGeneralExceptions(e);
            if (controller != null) controller.exit();
            System.exit(0);
        }
    }
}
