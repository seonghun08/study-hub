package board.model;

import java.sql.*;
import java.util.ArrayList;


public class BoardDAO {


	public static final int WRITING_PER_PAGE = 10;
	
	public BoardDAO() {}

	public static Connection getConnection(){
		try{
			String uri = "jdbc:mysql://localhost:3306/jspbook?characterEncoding=UTF-8&serverTimezone=UTC";
			String id = "root";
			String pw = "1234";
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(uri, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 게시판 목록 조회 기능 수행
	public ArrayList<BoardDTO> boardList(String curPage) {
		
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		String sql = null;
		
		try {
			conn = getConnection();
			
			sql = "SELECT num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt"
				+ "  FROM jspbook.board"
				+ " ORDER BY ref desc, step asc"
				+ " LIMIT ?, ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, WRITING_PER_PAGE * (Integer.parseInt(curPage)-1));
			pstmt.setInt(2, WRITING_PER_PAGE);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int num = rs.getInt("num");
				String name  = rs.getString("name");
				String password = rs.getString("password");
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Time witeTime = rs.getTime("write_time");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				BoardDTO writing = new BoardDTO();
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(witeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
				
				list.add(writing);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return list;
	}

	// 게시글 페이징 처리를 위한 기능 수행
	public int boardPageCnt() {
		
		int pageCnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			
			sql = "SELECT COUNT(*) AS num"
				+ "  FROM jspbook.board";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				pageCnt = rs.getInt("num") / WRITING_PER_PAGE + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return pageCnt;
	}
	
	
	// 게시글 등록 기능 수행
	@SuppressWarnings("resource")
	public void boardWrite (String name, String subject, String content, String password) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int num = 1;
		
		try {
			conn = getConnection();
			sql = "SELECT IFNULL(MAX(num), 0)+1 AS NUM"
				+ "  FROM jspbook.board";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				num = rs.getInt(num); 
			}
			
			sql = "INSERT INTO jspbook.board (num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt)"
				+ " VALUES (?, ?, ?, ?, ?, curdate(), curtime(), ?, 0, 0, 0, 0)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, subject);
			pstmt.setString(5, content);
			pstmt.setInt(6, num);
			
			pstmt.executeUpdate();
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
	}
	
	// 게시글 열람 기능 수행
	@SuppressWarnings("resource")
	public BoardDTO boardRead(String inputNum) {
		
		BoardDTO writing = new BoardDTO();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "UPDATE jspbook.board SET read_cnt = read_cnt + 1"
				+ " WHERE NUM = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			pstmt.executeUpdate();
			
			sql = "SELECT num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt"
				+ "  FROM jspbook.board"
				+ " WHERE num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Time writeTime = rs.getTime("write_time");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(writeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return writing;
	}
	
	// 게시글 수정 기능 수행
	public void boardUpdate(String inputNum,
							String inputSubject,
							String inputContent,
							String inputName,
							String inputPassword) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "UPDATE jspbook.board"
				+ " SET subject=?, content=?, name=?, password=?"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, inputSubject);
			pstmt.setString(2, inputContent);
			pstmt.setString(3,  inputName);
			pstmt.setString(4, inputPassword);
			pstmt.setInt(5, Integer.parseInt(inputNum));
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 try {
				 if (pstmt != null) pstmt.close();
				 if (conn != null) conn.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 } 
		 }
	 }
	
	// 게시글 수정 및 삭제를 위한 비밀번호 확인 기능 조회
	public boolean boardPasswordCheck(String inputNum, String inputPassword, String inputName) {
		
		boolean passwordOk = false;
		int passwordCheck = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT COUNT(*) AS password_check"
				+ "  FROM jspbook.board"
				+ " WHERE num=? AND password=? AND name=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(inputNum));
			pstmt.setString(2, inputPassword);
			pstmt.setString(3, inputName);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) passwordCheck = rs.getInt("password_check");
			if (passwordCheck > 0) passwordOk = true;
			
		} catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 try {
				 if (rs != null) rs.close();
				 if (pstmt != null) pstmt.close();
				 if (conn != null) conn.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return passwordOk;
	}
	
	// 글 수정 화면에 필요한 원글 데이터 조회 기능
	public BoardDTO boardUpdateForm(String inputNum) {
		BoardDTO writing = new BoardDTO();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT num, name, password, subject, content, write_date,"
				+ "       write_time, ref, step, lev, read_cnt, child_cnt"
				+ "  FROM jspbook.board"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Time writeTime = rs.getTime("write_Time");
				
				int ref = rs.getInt("ref");
	            int step = rs.getInt("step");
	            int lev = rs.getInt("lev");
	            int readCnt = rs.getInt("read_cnt");
	            int childCnt = rs.getInt("child_cnt"); 
	            
	            writing.setNum(num);
	            writing.setName(name);
	            writing.setPassword(password);
	            writing.setSubject(subject);
	            writing.setContent(content);
	            writing.setWriteDate(writeDate);
	            writing.setWriteTime(writeTime);
	            writing.setRef(ref);
	            writing.setStep(step);
	            writing.setLev(lev);
	            writing.setReadCnt(readCnt);
	            writing.setChildCnt(childCnt);
			}
		} catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 try {
				 if (rs != null) rs.close();
				 if (pstmt != null) pstmt.close();
				 if (conn != null) conn.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return writing;
	 }
	
	// 게시글 삭제 기능 수행
	@SuppressWarnings("resource")
	public void boardDelete(String inputNum) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT ref, lev, step"
				+ "  FROM jspbook.board"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int ref = rs.getInt(1);
				int lev = rs.getInt(2);
				int step = rs.getInt(3);
				boardDeleteChildCntUpdate(ref, lev, step);
			}
			
			sql = "DELETE"
				+ "  FROM jspbook.board"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			pstmt.executeUpdate();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
	}

	// 삭제 대상인 게시글에 답글의 존재 유무를 검사
	public boolean boardReplyCheck(String inputNum) {
		
		boolean replyCheck = false;
		int replyCnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT child_cnt as reply_check"
				+ "  FROM jspbook.board"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) replyCnt = rs.getInt("reply_check");
			if (replyCnt == 0) replyCheck = true;
			
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			 try {
				 if (rs != null) rs.close();
				 if (pstmt != null) pstmt.close();
				 if (conn != null) conn.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		}
		return replyCheck;
	}	


	// 게시글 답글일 경우, 원글들의 답글 개수를 줄여주는 기능을 수행
	@SuppressWarnings("resource")
	public void boardDeleteChildCntUpdate(int ref, int lev, int step) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			
			for (int updateLev = lev - 1; updateLev >= 0; updateLev--) {
				sql = "SELECT MAX(step)"
					+ "  FROM jspbook.board"
					+ " WHERE ref=? AND lev=? AND step < ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref); 
				pstmt.setInt(2, updateLev); 
				pstmt.setInt(3, step);
				
				rs = pstmt.executeQuery();
				
				int maxStep = 0;
				if (rs.next()) maxStep = rs.getInt(1);
				
				sql = "UPDATE jspbook.board"
					+ " SET child_cnt = child_cnt - 1"
					+ " WHERE ref=? AND lev=? AND step=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, updateLev); 
				pstmt.setInt(3, maxStep); 
				pstmt.executeUpdate();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }
	 }
	
	
	// 검색 기능 수행
	public ArrayList<BoardDTO> boardSearch(String searchOption, String searchWord) {
		
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt"
				+ "  FROM jspbook.board";
			
			if (searchOption.equals("subject")) {
				sql += " WHERE subject LIKE ?";
				sql	+= " ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if (searchOption.equals("content")) {
				sql += " WHERE content LIKE ?";
				sql	+= " ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if (searchOption.equals("name")) {
				sql += " WHERE name LIKE ?";
				sql += " ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if (searchOption.equals("both")) {
				sql += " WHERE subject LIKE ? OR content LIKE ?";
				sql += " ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				pstmt.setString(2, "%" + searchWord + "%");
			}
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Time writeTime = rs.getTime("write_time");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				BoardDTO writing = new BoardDTO();
				
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);				 
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(writeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
				
				list.add(writing);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return list;	
	}
	
	
	// 답글 적성에 필요한 원글 데이터 조회 기능
	public BoardDTO boardReplyForm(String inputNum) {
		
		BoardDTO writing = new BoardDTO();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = getConnection();
			sql = "SELECT num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt"
				+ "  FROM jspbook.board"
				+ " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputNum);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String subject = "RE: " + rs.getString("subject");
				Date writeDate = rs.getDate("write_date");
				Time writeTime = rs.getTime("write_time");
				
				String content = "[원문: " + writeDate + " " + writeTime + "작성됨]\n" + rs.getString("content");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(writeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return writing;
	}
	
	// 답글 등록 기능 수행
	@SuppressWarnings("resource")
	public void boardReply(String num, String name, String subject, String content,
						   String password, String ref, String step, String lev) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		int replyNum = 0;
		int replyStep = 0;
		
		try {
			conn = getConnection();
			
//			답글의 step 위치 값을 가져옴
			replyStep = boardReplyStep(ref, lev, step);
			
			if (replyStep > 0) {
				sql = "UPDATE jspbook.board"
					+ " SET step = step + 1"
					+ " WHERE ref=? AND step >= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				pstmt.setInt(2, replyStep);
				pstmt.executeUpdate();
			
			} else {
				sql = "SELECT MAX(step)"
					+ "  FROM jspbook.board"
					+ " WHERE ref=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				rs = pstmt.executeQuery();
				if (rs.next()) replyStep = rs.getInt(1) + 1;
			}
			
			sql = "SELECT MAX(num) + 1 AS num"
				+ "  FROM jspbook.board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) replyNum = rs.getInt("num");
			
			sql = "INSERT INTO jspbook.board"
				+ " (num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt)"
				+ " VALUES(?, ?, ?, ?, ?, curdate(), curtime(), ?, ?, ?, 0, 0)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, replyNum);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, replyNum);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, subject);
			pstmt.setString(5, content);
			pstmt.setInt(6, Integer.parseInt(ref));
			pstmt.setInt(7, replyStep);
			pstmt.setInt(8, Integer.parseInt(lev) + 1);
			pstmt.executeUpdate();
			 
			boardReplyChildCntUpdate(ref, lev, replyStep);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
	}

	public int boardReplyStep(String ref, String lev, String step) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		int replyStep = 0;
		
		try {
			conn = getConnection();
			sql = "SELECT IFNULL (MIN(step), 0)"
				+ "  FROM jspbook.board"
				+ " WHERE ref=? AND lev<=? AND step>?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(ref));
			pstmt.setInt(2, Integer.parseInt(lev));
			pstmt.setInt(3, Integer.parseInt(step));
			rs = pstmt.executeQuery();
			
			if (rs.next()) replyStep = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return replyStep;
	 }
	
	// 답글 작성 후 원글들이 답글 개수를 늘려주는 기능 수행
	@SuppressWarnings("resource")
	public void boardReplyChildCntUpdate(String ref, String lev, int replyStep) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		int maxStep = 0;
		
		try {
			conn = getConnection();
			
			for (int updateLev = Integer.parseInt(lev); updateLev>=0; updateLev--) {
				sql = "SELECT MAX(step)"
					+ "  FROM jspbook.board"
					+ " WHERE ref=? AND lev=? AND step<?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, replyStep);
				
				rs = pstmt.executeQuery();
				
				if (rs.next()) maxStep = rs.getInt(1);
				
				sql = "UPDATE jspbook.board SET child_cnt = child_cnt + 1"
					+ " WHERE ref = ? AND lev = ? AND step = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, maxStep);
				 
				pstmt.executeUpdate();
				
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}










