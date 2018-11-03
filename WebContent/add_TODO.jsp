<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>

<%--
Entry : edit_TODO의 "완료"버튼 클릭 시 인자로 TODO의 멤버 값과 action 문자열을 넘겨받음(action이 update일 시 우선순위도 넘겨받음)

TODO의 멤버 값중 마감기한은 String형이므로 todoBean객체에 String을 넘겨주어 알아서 변환하도록 함
action이 insert인 경우 todoManager로 insert작업을 처리
action이 update인 경우 todoManager로 update작업을 처리
모든 작업을 처리하고 main.jsp로 복귀
--%>

<%
request.setCharacterEncoding("UTF-8");
String action = request.getParameter("action");
%>

<jsp:useBean id="timeManager" class="todo.TimeManager" scope="application"/>
<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>
<jsp:useBean id="todoBean" class="todo.TODOBean" scope="page"/>
<jsp:setProperty name="todoBean" property="priority"/>
<jsp:setProperty name="todoBean" property="title"/>
<jsp:setProperty name="todoBean" property="content"/>

<%
todoBean.setGoalDate(request.getParameter("goalDate"), timeManager.getCurrentDate());

if(action.equals("insert"))
	todoManager.insert(todoBean);
else if(action.equals("update"))
	todoManager.update(todoBean, Integer.parseInt(request.getParameter("pre_priority")));

response.sendRedirect("main.jsp");
%>