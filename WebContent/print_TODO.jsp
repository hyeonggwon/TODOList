<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="todo.*" errorPage="error.jsp"%>
<!DOCTYPE html>

<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>

<%--
요청으로 부터 우선순위를 받아 해당 우선순위를 갖고 있는 TODO를 출력하는 페이지
isYes 객체는 TODO 삭제를 클릭할 시 재확인하는 페이지(confirm_TODO.jsp)에서 "취소"를 클릭 했을 시 생성되는 객체
--%>
<%
request.setCharacterEncoding("UTF-8");
int priority = Integer.parseInt(request.getParameter("priority"));
TODOBean todoBean = todoManager.getTODOBean(priority);
Boolean isYes = (Boolean) session.getAttribute("isYes");
if(isYes != null) {
	session.removeAttribute("isYes");
}
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=todoBean.getTitle()%></title>
	</head>
	<body>
		<div align="center">
		<br><br>
		<table border="1" width="500">
			<tr>
				<td width="10%">제목 : </td>
				<td width="90%">
				<%
				if(todoBean != null)
					out.print(todoBean.getTitle());
				%></td>
			</tr>
		</table>
		<br>내용
		<table border="1">
			<tr>
				<td width="500">
				<%
				if(todoBean != null)
					out.print(todoBean.getContent());
				%></td>
			</tr>
		</table>
		<%
		String str = todoBean.getGoalDateStr();
		if(str != null) {
		%>
		<br>
		<table border="1">
			<tr>
				<td>마감기한 </td>
				<td><%=str%></td>
			</tr>
		</table>
		<%
		}
		%>
		<br>
		<table>
			<tr>
				<%--목록버튼 -> main.jsp --%>
				<td><button type="button" onclick="window.location.href='main.jsp'">목록</button></td>
				<%--수정버튼 -> edit.jsp , param : priority --%>
				<td><button type="button" onclick="window.location.href='edit_TODO.jsp?priority=<%=priority%>'">수정</button></td>
				<%--삭제버튼 -> confirm_delete.jsp , param : priority --%>
				<td><button type="button" onclick="window.location.href='confirm_delete.jsp?priority=<%=priority%>'">삭제</button></td>
			</tr>
		</table>
		</div>
	</body>
</html>