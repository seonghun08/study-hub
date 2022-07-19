package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardDeleteCheckCmd implements BoardCmd{
	
	public boolean password_check;
	public boolean reply_check;
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		String inputNum = request.getParameter("num");
		String inputPassword = request.getParameter("password");
		String inputName = request.getParameter("name");
		
		request.setAttribute("num", inputNum);
		
		BoardDAO dao = new BoardDAO();
		
		password_check = dao.boardPasswordCheck(inputNum, inputPassword, inputName);
		reply_check = dao.boardReplyCheck(inputNum);
		
	}

}
