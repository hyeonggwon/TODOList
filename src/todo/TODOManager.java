package todo;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * TODOlist를 관리하는 클래스
 */
public class TODOManager {
	/**TODOlist*/
	ArrayList<TODOBean> todoBeanList;

	/**
	 * TODOlist에 TODO를 추가하고, DB를 갱신하는 함수
	 * @param todoBean : 리스트에 추가할 todo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void insert(TODOBean todoBean) throws ClassNotFoundException, SQLException {
		int priority = todoBean.getPriority();
		int size;
		
		todoBeanList.add(priority-1, todoBean);
		size = todoBeanList.size();
		for(int i = priority; i < size; i++)
			todoBeanList.get(i).setPriority(i+1);
		
		insertToDB(todoBean);
	}
	
	/**
	 * TODOlist에 있는 TODO의 내용을 변경하고, DB를 갱신하는 함수
	 * @param todoBean : 변경할 todo
	 * @param pre_priority : 변경할 TODO의  변경 전 우선순위
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void update(TODOBean todoBean, int pre_priority) throws ClassNotFoundException, SQLException {
		int priority = todoBean.getPriority();
		
		todoBeanList.remove(pre_priority-1);
		todoBeanList.add(priority-1, todoBean);
		
		if(priority != pre_priority) {
			if(priority < pre_priority) {	
				for(int i = priority; i < pre_priority; i++)
					todoBeanList.get(i).setPriority(i+1);
			}
			else {
				for(int i = pre_priority-1; i < priority-1; i++)
					todoBeanList.get(i).setPriority(i+1);
			}
		}
		
		updateToDB(todoBean,pre_priority);
	}
	
	/**
	 * TODOlist에서 입력받은 우선순위를 가지고 있는 TODO를 삭제하고, DB를 갱신하는 함수 
	 * @param priority : 삭제할 TODO가 가지고 있는 우선순위
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void delete(int priority) throws ClassNotFoundException, SQLException {
		int size;
		
		todoBeanList.remove(priority-1);
		size = todoBeanList.size();
		for(int i = priority; i < size; i++)
			todoBeanList.get(i).setPriority(i+1);
		
		deleteFromDB(priority);
	}
	
	/**
	 * TODOlist에서 입력받은 우선순위를 가지고 있는 TODO에 대해 완료 처리를 하고, DB를 갱신하는 함수
	 * 우선순위를 맨 꼴등으로 정하고, isComplete을 1로 세팅한 뒤 리스트를 정렬
	 * @param priority : 완료 처리를 할 TODO의 우선순위
	 * @throws SQLException
	 */
	public void complete(int priority) throws SQLException {
		int size = todoBeanList.size();
		TODOBean todoBean = todoBeanList.get(priority-1);
		todoBean.setIsComplete(1);
		todoBean.setPriority(size);
		
		todoBeanList.remove(priority-1);
		todoBeanList.add(todoBean);
		
		for(int i = priority-1; i < size-1; i++)
			todoBeanList.get(i).setPriority(i+1);
		
		setCompleteToDB(todoBean, priority);
	}
	
	/**
	 * TODOlist에서 입력받은 우선순위를 가지고 있는 TODO에 대해 재실행 처리를 하고, DB를 갱신하는 함수
	 * 리스트를 미리 정렬한 뒤 우선순위를 현재 진행 중인 TODO중 맨 꼴등으로 정하고, isComplete를 0으로 세팅
	 * @param priority : 재실행 처리를 할 TODO의 우선순위
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void restart(int priority) throws SQLException, ClassNotFoundException {
		int size = todoBeanList.size();
		int insert_index = 0;
		TODOBean todoBean = todoBeanList.get(priority-1);
		
		for(int i = 0; i < size; i++) {
			if(todoBeanList.get(i).isComplete == 1) {
				insert_index = i;
				break;
			}
		}
		
		todoBean.setIsComplete(0);
		todoBean.setPriority(insert_index+1);
		
		update(todoBean, priority);
	}
	
	/**
	 * DB에 새 TODO를 추가하는 함수
	 * @param todoBean : 추가할 todo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void insertToDB(TODOBean todoBean) throws ClassNotFoundException, SQLException {
		String myUrl = "jdbc:mysql://localhost/TODOlist?serverTimezone=UTC";
		Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
		String query = "Update TODO\n"
				+ "set priority=priority+1\n"
				+ "where priority>=" + todoBean.getPriority();
		PreparedStatement pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();
		
		query = "Insert into TODO\n"
				+ "values(" + todoBean.getPriority() + ","
				+ "\'" + todoBean.getTitle() + "\'" + ","
				+ "\'" + todoBean.getContent() + "\'" + ","
				+ todoBean.getGoalDateForQuery() + ","
				+ todoBean.getIsComplete() + ")";
		pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();

		pStmt.close();
		conn.close();
	}
	
	/**
	 * DB에서 pre_priority를 갖고 있는 TODO의 내용을 변경하는 함수 
	 * @param todoBean : 새로 변경될 todo
	 * @param pre_priority : 변경될 TODO의 변경 전 우선순위
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void updateToDB(TODOBean todoBean, int pre_priority) throws ClassNotFoundException, SQLException {
		int priority = todoBean.getPriority();
		String myUrl = "jdbc:mysql://localhost/TODOlist?serverTimezone=UTC";
		Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
		String query;
		PreparedStatement pStmt;
		
		if(priority == pre_priority) {
			query = "Update TODO\n"
					+ "set title = '" + todoBean.getTitle() + "', "
					+ "content = '" + todoBean.getContent() + "', "
					+ "goaldate = " + todoBean.getGoalDateForQuery() + ", " 
					+ "isComplete = " + todoBean.getIsComplete() + "\n"
					+ "where priority = " + todoBean.getPriority();
			pStmt = conn.prepareStatement(query);
			pStmt.executeUpdate();
		}
		else {
			query = "Update TODO\n"
					+ "set priority=0\n"
					+ "where priority="+pre_priority;
			pStmt = conn.prepareStatement(query);
			pStmt.executeUpdate();
			
			if(priority < pre_priority) {
				query = "Update TODO\n"
						+ "set priority=priority+1\n"
						+ "where priority>=" + priority 
						+ " and priority<" + pre_priority;
				pStmt = conn.prepareStatement(query);
				pStmt.executeUpdate();
			}
			else {
				query = "Update TODO\n"
						+ "set priority=priority-1\n"
						+ "where priority>" + pre_priority 
						+ " and priority<=" + priority;
				pStmt = conn.prepareStatement(query);
				pStmt.executeUpdate();
			}
			
			query = "Update TODO\n"
					+ "set priority = " + priority + ", "
					+ "title = '" + todoBean.getTitle() + "', "
					+ "content = '" + todoBean.getContent() + "', "
					+ "goaldate = " + todoBean.getGoalDateForQuery() + ", " 
					+ "isComplete = " + todoBean.getIsComplete() + "\n"
					+ "where priority=0";
			pStmt = conn.prepareStatement(query);
			pStmt.executeUpdate();
		}
		
		pStmt.close();
		conn.close();
	}
	
	/**
	 * DB에서 해당 우선순위를 갖고 있는 TODO를 삭제하는 함수
	 * @param priority : 삭제할 TODO의 우선순위
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void deleteFromDB(int priority) throws ClassNotFoundException, SQLException {
		String myUrl = "jdbc:mysql://localhost/TODOlist?serverTimezone=UTC";
		Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
		String query = "Delete from TODO\n"
				+ "where priority=" + priority;
		PreparedStatement pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();
		
		query = "Update TODO\n"
				+ "set priority=priority-1\n"
				+ "where priority>" + priority;
		pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();

		pStmt.close();
		conn.close();
	}
	
	/**
	 * 완료 처리된 TODO를 DB에 적용하는 함수
	 * @param todoBean : 완료 처리된 todo
	 * @param pre_priority : 완료 처리된 TODO의 처리 전 우선순위
	 * @throws SQLException
	 */
	private void setCompleteToDB(TODOBean todoBean, int pre_priority) throws SQLException {
		String myUrl = "jdbc:mysql://localhost/TODOlist?serverTimezone=UTC";
		Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
		
		String query = "Update TODO\n"
				+ "set priority=0\n"
				+ "where priority=" + pre_priority;
		PreparedStatement pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();
		
		query = "Update TODO\n"
			+ "set priority=priority-1\n"
			+ "where priority > " + pre_priority + " and priority <=" + todoBean.getPriority();
		pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();

		query = "Update TODO\n"
				+ "set priority=" + todoBean.getPriority() + ", isComplete=1\n"
				+ "where priority=0";
		pStmt = conn.prepareStatement(query);
		pStmt.executeUpdate();
		
		pStmt.close();
		conn.close();
	}
	
	/**
	 * 마감기한이 지난 TODO들의 우선순위 리스트를 만들어 리턴하는 함수
	 * @param currentDate : 현재 시간 객체
	 * @return 마감기한이 지난 TODO들의 우선순위 리스트
	 */
	public ArrayList<Integer> getPriListOfTimeOut(Date currentDate) {
		ArrayList<Integer> priorityList = new ArrayList<Integer>();
		int size = todoBeanList.size();
		
		for(int i = 0; i < size; i++)
			if(todoBeanList.get(i).isTimeOut(currentDate))
				priorityList.add(i+1);
		
		return priorityList;
	}
	
	/**
	 * TODOlist를 리턴하는 함수
	 * @return TODOlist
	 */
	public ArrayList<TODOBean> getTODOList() {
		return todoBeanList;
	}
	
	/**
	 * 입력받은 우선순위에 해당하는 TODO를 리턴하는 함수
	 * @param priority : 받을 TODO의 우선순위
	 * @return 입력받은 우선순위에 해당하는 TODO
	 */
	public TODOBean getTODOBean(int priority) {
		int size = todoBeanList.size();
		for(int i = 0; i < size; i++) {
			if(todoBeanList.get(i).getPriority() == priority)
				return todoBeanList.get(i);
		}
		
		return null;
	}
	
	/**
	 * 현재 TODOlist에 저장된 TODO의 개수를 리턴하는 함수
	 * @return 현재 TODOlist에 저장된 TODO의 개수
	 */
	public int getListSize() {
		return todoBeanList.size();
	}
	
	/**
	 * DB에 연결하여 모든 TODO를 가져와 TODOlist에 추가하는 함수
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void loadData() throws SQLException, ClassNotFoundException {
		
		todoBeanList = new ArrayList<TODOBean>();
		String myUrl;
		Connection conn;
		String query;
		PreparedStatement pStmt;
		
		Class.forName("com.mysql.jdbc.Driver");

		try{
			myUrl = "jdbc:mysql://localhost/TODOlist?serverTimezone=UTC";
			conn = DriverManager.getConnection(myUrl, "root", "1234");
		}
		catch(SQLException e) {
			myUrl = "jdbc:mysql://localhost?serverTimezone=UTC";
			conn = DriverManager.getConnection(myUrl, "root", "1234");
			
			query = "create database TODOlist";
			pStmt = conn.prepareStatement(query);
			pStmt.execute();
			
			query = "use TODOlist";
			pStmt = conn.prepareStatement(query);
			pStmt.execute();
			
			query = "create table TODO(\n"
					+ "priority 	int not null,\n"
					+ "title		varchar(50) not null,\n"
					+ "content		text,\n"
					+ "goaldate 	datetime,\n"
					+ "isComplete	int)";
			pStmt = conn.prepareStatement(query);
			pStmt.executeUpdate();
		}

		query = "select * from TODO order by priority";
		pStmt = conn.prepareStatement(query);

		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			TODOBean todoBean = new TODOBean();
			todoBean.setPriority(rs.getInt(1));
			todoBean.setTitle(rs.getString(2));
			todoBean.setContent(rs.getString(3));
			todoBean.setGoalDate(rs.getString(4));
			todoBean.setIsComplete(rs.getInt(5));
			
			todoBeanList.add(todoBean);
		}

		rs.close();
		pStmt.close();
		conn.close();
	}
}
