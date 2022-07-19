package board.command;

import javax.servlet.http.*;

import board.model.*;

public class BoardUpdateFormCmd implements BoardCmd{
	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String inputNum = request.getParameter("num");
		BoardDAO dao = new BoardDAO();
		BoardDTO writing = dao.boardUpdateForm(inputNum);
		
		request.setAttribute("boardUpdateForm", writing);
	}
	
	
}
