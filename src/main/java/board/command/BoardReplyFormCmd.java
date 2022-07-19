package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardReplyFormCmd implements BoardCmd{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		
		String inputNum = request.getParameter("num");
		BoardDAO dao = new BoardDAO();
		
		BoardDTO writing = dao.boardReplyForm(inputNum);
		request.setAttribute("boardReplyForm", writing);
	}

}
