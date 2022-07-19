package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardReadCmd implements BoardCmd{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String inputNum = request.getParameter("num");
		BoardDAO dao = new BoardDAO();
		BoardDTO writing = dao.boardRead(inputNum);
		
		request.setAttribute("boardRead", writing);
	}
	

}
