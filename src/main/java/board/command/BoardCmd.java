package board.command;

import javax.servlet.http.*;

public interface BoardCmd {
	void execute(HttpServletRequest request, HttpServletResponse response);
}