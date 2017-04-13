package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Интерфейс для обработки и выполнения запросов пользователей
 */
interface Command {
    /**
     * Метод выполнения запросов пользователей
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
