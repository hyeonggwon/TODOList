<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="todo.*" errorPage="error.jsp"%>
<!DOCTYPE html>

<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>

<%--
TODO 수정화면

<Entry1> - main.jsp의 작업추가 버튼 클릭 시, 인자로 priority가 들어오지 않으므로
		 priorityStr는 null
<Entry2> - printTODO.jsp의 수정 버튼 클릭 시, 인자로 priority가 들어오므로
		 priorityStr는 priority의 String 변환 값
		 
Entry1을 통해 들어온 경우 insert로 action을 정하고,
Entry2를 통해 들어온 경우 update로 action을 정한다.
--%>
<% 
request.setCharacterEncoding("UTF-8");
String action;
String priorityStr = request.getParameter("priority");
TODOBean todoBean = null;
int pre_priority = 0;

if(priorityStr != null) {
	action = "update";
	todoBean = todoManager.getTODOBean(Integer.parseInt(priorityStr));
	pre_priority = todoBean.getPriority();
}
else {
	action = "insert";
}
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>작업편집/추가</title>
	</head>
	<body>
		<div align="center">
		<br><br>
		<form method="POST" action="add_TODO.jsp?action=<%=action%>&pre_priority=<%=pre_priority%>">
			<table border="1">
				<tr>
					<td>제목</td>
					<td><input type="text" name="title" size="50" maxlength="49" 
					<%
					if(todoBean != null)
						out.print("value = " + todoBean.getTitle());
					%>
					></td>
				</tr>
			</table>
			<br><p>내용</p>
			<textarea name="content" cols="60" rows="30"><%
			if(todoBean != null)
				out.print(todoBean.getContent());
			%></textarea>
			<br><br>
			<table border="1">
				<tr>
					<td align="center">우선순위 </td>
					<td align="center">
						<select name="priority">
							<%--
							insert 작업일 경우 입력받을 우선순위 개수는 (TODOlist 사이즈 + 1)개
							update 작업일 경우 입력받을 우선순위 개수는 (TODOlist 사이즈)개
							--%>
							<%
							int num = todoManager.getListSize();
							if(action.equals("insert"))
								num++;
							
							for(int i = 0; i < num; i++) {
								if(i+1 == pre_priority)
									out.println("\t\t\t\t\t\t\t\t<option selected>" + (i+1) + "</option>");
								else
									out.println("\t\t\t\t\t\t\t\t<option>" + (i+1) + "</option>");
							}
							%>
						</select>
					</td>
				</tr>
			</table>
			<br>
			<table border="1">
				<tr>
					<td align="center">마감기한<br>
					<font size=1>(필요시 입력, yyyy-MM-dd HH:mm 형식으로 작성, y=년 M=월 d=일 H=시간(00~24) m=분)</font></td>
				</tr>
				<tr>
				<%--
				마감기한 입력부분
				마감기한이 존재하면 입력부분에 출력, 존재하지 않으면 출력하지 않음
				--%>
					<td align="center"><input type="text" name="goalDate" placeholder="yyyy-mm-dd hh:mm" maxlength="16"<%
					if(todoBean != null) {
						String str = todoBean.getGoalDateStr();
						if(str != null)
							out.print(" value = \"" + str + "\"");
					}
					%>/></td>
				</tr>
			</table>
			<table>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="완료"></td>
					<td colspan="2" align="center"><button type="button" onclick="window.location.href='confirm_cancle.jsp'">취소</button></td>
				</tr>
			</table>
		</form>
		</div>
	</body>
</html>