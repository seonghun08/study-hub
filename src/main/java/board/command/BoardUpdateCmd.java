package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardUpdateCmd implements BoardCmd{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		
		String inputNum = request.getParameter("num");
		String inputSubject = request.getParameter("subject");
		String inputContent = request.getParameter("content");
		String inputName = request.getParameter("name");
		
//		String inputPassword = request.getParameter("password");
		HttpSession password = request.getSession();
		String inputPassword = (String) password.getAttribute("PASSWORD");
		
		BoardDAO dao = new BoardDAO();
		dao.boardUpdate(inputNum, inputSubject, inputContent, inputName, inputPassword);
	}
}
