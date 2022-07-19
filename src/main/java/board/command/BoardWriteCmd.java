package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardWriteCmd implements BoardCmd{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		HttpSession pw = request.getSession();
		String password = (String) pw.getAttribute("PASSWORD");
		
		BoardDAO dao = new BoardDAO();
		dao.boardWrite(name, subject, content, password);
		
	}

}