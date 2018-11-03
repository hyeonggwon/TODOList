package todo;

import java.util.*;

/**
 * 현재 시간을 객체로 관리하고 갱신하는 클래스
 */
public class TimeManager {
	Date currentDate;
	
	public void renewCurrentDate() {
		this.currentDate = new Date();
	}
	
	public Date getCurrentDate() {
		return currentDate;
	}
}
