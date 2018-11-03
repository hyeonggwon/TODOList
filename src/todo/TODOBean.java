package todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 하나의 TODO에 해당하는 클래스
 */
public class TODOBean {
	
	/**우선순위 */
	int priority;
	
	/**제목*/
	String title;
	
	/**내용*/
	String content;
	
	/**완료 또는 진행중 여부를 저장하는 변수(완료시 1, 진행중일시 0)*/ 
	int isComplete;
	
	/**마감기한*/
	Date goalDate = null;

	
	//멤버변수들의 get,set 함수들
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}
	
	public Date getGoalDate() {
		return goalDate;
	}
	public void setGoalDate(Date goalDate) {
		this.goalDate = goalDate;
	}
	
	/**
	 * String형의 마감기한을 가져와 Date형으로 변환한 뒤 멤버변수 goalDate에 저장하는 함수
	 * @param sqlGoalDateStr : 마감기한의 String형
	 */
	public void setGoalDate(String sqlGoalDateStr) {
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(sqlGoalDateStr != null) {
			try {
				goalDate = form.parse(sqlGoalDateStr);
			} catch (ParseException e) {
				goalDate = null;
			}
		}
	}
	
	/**
	 * Date형 멤버변수 goalDate를 Mysql Query에서 사용할 수 있는 형태의 문자열로 변환 후 리턴하는 함수
	 * @return goalDate의 Mysql Query에서 사용할 수 있는 형태의 문자열
	 */
	public String getGoalDateForQuery() {
		if(goalDate == null)
			return null;
		else {
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "'" + form.format(goalDate) + "'";
		}
	}
	
	/**
	 * Date형 멤버변수 goalDate를 yyyy-MM-dd HH:mm 형태의 String으로 변환 후 리턴하는 함수
	 * @return goalDate를 yyyy-MM-dd HH:mm 형태로 변환한 String
	 */
	public String getGoalDateStr() {
		if(goalDate == null)
			return null;
		else {
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return form.format(goalDate);
		}
	}
	
	/**
	 * 입력받은 문자열을 Date형으로 파싱하여 변환 후 결과를 멤버변수 goalDate에 저장하는 함수
	 * 날짜가 유효하지 않거나, 입력 포맷을 벗어나면 저장하지 않는다.
	 * @param input : yyyy-MM-dd HH:mm 형태의 입력받은 문자열
	 * @param currentDate : Date형 현재 시간 객체
	 */
	public void setGoalDate(String input, Date currentDate) {
		try {
			if(input == null) {
				throw new Exception();
			}
			
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			form.setLenient(false);
			Date tempDate = form.parse(input);
			
			if(tempDate.getTime() < currentDate.getTime()) {
				throw new Exception();
			}
			
			this.goalDate = (Date) tempDate.clone();
		}
		catch(Exception e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * TODO의 마감기한이 지났는지 확인하는 함수 
	 * @param currentDate : 현재 시간 객체
	 * @return 마감기한이 지났으면 true, 그렇지 않으면 false
	 */
	public boolean isTimeOut(Date currentDate) {
		if(goalDate == null)
			return false;
		
		if(goalDate.getTime() - currentDate.getTime() <= 0) 
			return true;
		else
			return false;
	}
	
	/**
	 * 마감까지 남은 시간을 알려주는 함수
	 * @param currentDate : 현재 시간 객체
	 * @return 마감기한이 설정되어있지 않으면 공백, 마감기한을 지났으면 "마감", 그렇지 않으면 마감까지 남은 시간의 출력 문자열
	 */
	public String getRemainingTimeStr(Date currentDate) {
		if(goalDate == null) {
			return " ";
		}
		else {
			long remainingTime = goalDate.getTime() - currentDate.getTime();
			
			if(remainingTime <= 0) {
				return "마감";
			}
			else {
				long hour = remainingTime / (1000*60*60);
				long min = (remainingTime - hour*1000*60*60) / (1000 * 60);
				
				return hour + "시간 " + min + "분";
			}
		}
	}
}
