package board.command;

import javax.servlet.http.*;

public class BoardUpdatePasswordCmd implements BoardCmd{
	
	public boolean password_check;
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		// TODO Auto-generated method stub
		String inputNum = request.getParameter("num");
		request.setAttribute("num", inputNum);
	}

}
