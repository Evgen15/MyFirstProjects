package command;

import logic.Invoice;
import logic.LineInvoice;
import logic.ManagementModel;
import logic.Seller;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, реализующий обработку запроса на сохранение и/или обновление счетов
 */
public class SaveCommand implements Command {
    /**
     * Метод обрабатывает запрос на сохранение нового счета и/или обновление существующего счета в хранилище
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int numberInvoice = Integer.parseInt(request.getParameter("numberInvoice"));
            Date dateInvoice = new SimpleDateFormat("dd.MM.yyyy").parse(request.getParameter("dateInvoice"));
            int sumInvoice = Integer.parseInt(request.getParameter("sumInvoice"));

            int idSeller = Integer.parseInt(request.getParameter("idSeller"));
            Seller seller = new Seller();
            seller.setId(idSeller);

            List<LineInvoice> listLines = new ArrayList<>();
            String[] descriptionLine = request.getParameterValues("descriptionLine");
            String[] amountLine = request.getParameterValues("amountLine");
            String[] priceLine = request.getParameterValues("priceLine");
            String[] sumLine = request.getParameterValues("sumLine");

            for(int i = 0; i < descriptionLine.length; i++) {
                LineInvoice line = new LineInvoice();
                line.setDescription(descriptionLine[i]);
                line.setAmount(Integer.parseInt(amountLine[i]));
                line.setPrice(Integer.parseInt(priceLine[i]));
                line.setSum(Integer.parseInt(sumLine[i]));
                listLines.add(line);
            }

            Invoice invoice = new Invoice();
            invoice.setNumber(numberInvoice);
            invoice.setDate(dateInvoice);
            invoice.setSum(sumInvoice);
            invoice.setSeller(seller);
            invoice.setListLines(listLines);

            String idInvoice = request.getParameter("idInvoice");

            if (idInvoice.isEmpty()) {
                ManagementModel.saveNewInvoice(invoice);
                response.sendRedirect("/invoices?add=ok");
            } else {
                invoice.setId(Integer.parseInt(idInvoice));
                ManagementModel.updateInvoice(invoice);
                response.sendRedirect("/invoices?edit=ok");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
            throw new ServletException(e);
        }
    }
}
