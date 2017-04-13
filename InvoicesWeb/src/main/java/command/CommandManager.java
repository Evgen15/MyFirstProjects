package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, управляющий запуском соответствующих команд для выполнения запросов
 */
public class CommandManager {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    /**
     * В стат блоке заполняем карту необходимыми командами
     * */
    static {
        COMMANDS.put("search", new SearchCommand());
        COMMANDS.put("edit", new EditCommand());
        COMMANDS.put("delete", new DeleteCommand());
        COMMANDS.put("save", new SaveCommand());
    }

    private CommandManager() {}

    /**
     * Метод запуска соответствующей команды для обработки запроса
     *
     * @param commandName имя конкретной команды
     * @param request HTTP запрос
     * @param response HTTP ответ
     * */
    public static void process(String commandName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        COMMANDS.get(commandName).execute(request, response);
    }
}
