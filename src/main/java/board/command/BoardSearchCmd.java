package board.command;

import java.util.ArrayList;
import javax.servlet.http.*;
import board.model.*;

public class BoardSearchCmd implements BoardCmd{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse reponse) {
		
		BoardDAO dao = new BoardDAO();
		String searchOption = request.getParameter("searchOption");
		String searchWord = request.getParameter("searchWord");
		
		ArrayList<BoardDTO> list = dao.boardSearch(searchOption, searchWord);
		request.setAttribute("boardList", list);
	}
}
