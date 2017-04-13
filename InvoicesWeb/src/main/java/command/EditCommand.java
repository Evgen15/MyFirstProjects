package command;

import logic.Invoice;
import logic.ManagementModel;
import logic.Seller;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, реализующий обработку запроса на создание и/или редактирование счетов
 */
public class EditCommand implements Command {
    /**
     * Метод обрабатывает запрос на создание нового счета и/или редактирование существующего счета по его id
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idInvoice = request.getParameter("idInvoice");
            if (idInvoice != null) {
                int id = Integer.parseInt(idInvoice);
                Invoice invoice = ManagementModel.loadInvoiceById(id);
                request.setAttribute("invoice", invoice);
            }

            List<Seller> sellers = ManagementModel.getAllSellers();
            request.setAttribute("listSellers", sellers);

            request.getRequestDispatcher("webcontent/pages/edit.jsp").forward(request, response);
        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
            throw new ServletException(e);
        }
    }
}
