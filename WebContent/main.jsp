<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="todo.*, java.util.*" errorPage="error.jsp"%>
<!DOCTYPE html>

<%-- 맨 처음 접속 페이지 --%>

<%-- 
todoManager : TODOlist를 관리
timeManager : 내부에 저장된 현재 시간을 매 페이지 방문마다 갱신
--%>

<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>
<jsp:useBean id="timeManager" class="todo.TimeManager" scope="application"/>

<%--
todoManager가 DB로 부터 TODOlist를 읽어 로딩하고, 현재 시간을 갱신
priorityList는 마감기한이 지난 TODO의 우선순위 리스트
--%>
<%
application.log("loading data from mysql..");
todoManager.loadData();
application.log("loading data from mysql complete.");
timeManager.renewCurrentDate();
ArrayList<Integer> priorityList = todoManager.getPriListOfTimeOut(timeManager.getCurrentDate());
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>작업관리</title>
	</head>
	<body>
		<div align="center">
		<h2>작업관리</h2><hr>
		<button onclick="window.location.href='edit_TODO.jsp'">작업추가</button>
		<br><br>
		<table border="1" frame="boxs" width="800">
			<tr><th width="10%">우선순위</th><th width="40%">제목</th><th width="20%">남은시간</th><th width="10%">완료여부</th><th width="10%"> </th></tr>
			<%--
			TODOlist가 비어있을 때 아무것도 출력하지 않고,
			각 TODO의 완료/진행중 상태에 따라 출력을 다르게함
			--%>
			<%
			if(todoManager.getListSize() != 0) {
				for(TODOBean todoBean : todoManager.getTODOList()) {
					if(todoBean.getIsComplete() == 0) {
						out.println("\t\t\t<tr>" + "<td align=\"center\">" + todoBean.getPriority() + "</td>");
						out.println("\t\t\t\t<td align=\"center\">" + "<a href=\"print_TODO.jsp?priority=" + todoBean.getPriority() + "\">" + todoBean.getTitle() + "</a></td>");
						out.println("\t\t\t\t<td align=\"center\"><font size=\"2\">" + todoBean.getRemainingTimeStr(timeManager.getCurrentDate()) + "</font></td>");
						out.println("\t\t\t\t<td align=\"center\"> </td>");
						out.println("\t\t\t\t<td align=\"center\"><button onclick=\"window.location.href='complete_TODO.jsp?priority="+ todoBean.getPriority() + "'\">완료</td>");
					}
					else {
						out.println("\t\t\t<tr>" + "<td align=\"center\"> </td>");
						out.println("\t\t\t\t<td align=\"center\">" + "<a href=\"print_TODO.jsp?priority=" + todoBean.getPriority() + "\">" + todoBean.getTitle() + "</a></td>");
						out.println("\t\t\t\t<td align=\"center\"> </td>");
						out.println("\t\t\t\t<td align=\"center\">O</td>");
						out.println("\t\t\t\t<td align=\"center\"><button onclick=\"window.location.href='restart_TODO.jsp?priority="+ todoBean.getPriority() + "'\">재시작</td>");
					}
					out.println("\t\t\t</tr>");
				}
			}
			%>
		</table>
		<br>
		<hr>
		<%--
		마감기한이 지난 작업을 출력
		--%>
		<%
		if(!priorityList.isEmpty()) {
			out.println("<마감된작업><br><br>");
			int size = priorityList.size();
			for(int i = 0; i < size; i++)
				out.println(todoManager.getTODOBean(priorityList.get(i)).getTitle()+"<br>");
		}
		%>
		</div>
	</body>
</html>