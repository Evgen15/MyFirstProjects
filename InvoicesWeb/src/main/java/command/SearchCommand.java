package command;

import logic.Invoice;
import logic.ManagementModel;
import logic.Period;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, реализующий обработку запроса по поиску счетов
 */
class SearchCommand implements Command {
    /**
     * Метод обрабатывает запрос по поиску счетов.
     * Проверка startDate на null необходима, чтобы выявить запросы, транслированные через другие команды,
     * то есть в случаях, когда клиент не указывал новый период отображения счетов.
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Period period = null;
            String startDate = request.getParameter("startDate");

            if (startDate != null) {
                String endDate = request.getParameter("endDate");
                period = new Period();
                period.setStartDate(new SimpleDateFormat("dd.MM.yyyy").parse(startDate));
                period.setEndDate(new SimpleDateFormat("dd.MM.yyyy").parse(endDate));
                session.setAttribute("startDate", startDate);
                session.setAttribute("endDate", endDate);
                session.setAttribute("period", period);
            } else
                period = (Period) session.getAttribute("period");

            List<Invoice> result = ManagementModel.findInvoicesByPeriod(period);

            request.setAttribute("invoicesList", result);
            request.getRequestDispatcher("webcontent/pages/result.jsp").forward(request, response);

        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
            throw new ServletException(e);
        }
    }
}
