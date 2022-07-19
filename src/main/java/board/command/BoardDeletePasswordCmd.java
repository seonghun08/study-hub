package board.command;

import javax.servlet.http.*;

public class BoardDeletePasswordCmd implements BoardCmd{
	
	public boolean password_check;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		
		String inputNum = request.getParameter("num");
		request.setAttribute("num", inputNum);
		
	}
}
