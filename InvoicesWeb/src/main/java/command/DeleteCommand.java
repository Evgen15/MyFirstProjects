package command;

import logic.Invoice;
import logic.ManagementModel;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, реализующий обработку запроса на удаление счетов
 */
public class DeleteCommand implements Command {
    /**
     * Метод обрабатывает запрос на удаление счета по его id.
     * Для дальнейшего отображения пользователю измененной информации запрос и ответ транслируются в команду "Search".
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idInvoice = Integer.parseInt(request.getParameter("idInvoice"));
            Invoice invoice = new Invoice();
            invoice.setId(idInvoice);
            ManagementModel.deleteInvoice(invoice);
            CommandManager.process("search", request, response);
        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
            throw new ServletException(e);
        }
    }
}
