package control;

import command.CommandManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс сервлета, который выступает в роли первого лицевого контролера-обработчика запросов
 */
public class FrontControllerServlet extends HttpServlet {
    /**
     * Метод получает параметр (имя команды) и передает его
     * вместе с объектами запроса и ответа на дальнейшую обработку
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        CommandManager.process(request.getParameter("command"), request, response);
    }
}
