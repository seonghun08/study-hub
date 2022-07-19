package board.command;

import javax.servlet.http.*;
import board.model.*;

public class BoardDeleteCmd implements BoardCmd{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		// TODO Auto-generated method stub
		
		String inputNum = request.getParameter("num");
		
		BoardDAO dao = new BoardDAO();
		dao.boardDelete(inputNum);
	}

}
