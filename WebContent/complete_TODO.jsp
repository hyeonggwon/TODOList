<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>

<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>

<%--
Entry : main.jsp의 TODO 옆 "완료" 버튼 클릭 시 인자로 우선순위를 넘겨받음
todoManager가 완료작업을 처리한 뒤 main.jsp로 복귀
--%>

<%
request.setCharacterEncoding("UTF-8");
int priority = Integer.parseInt(request.getParameter("priority"));
todoManager.complete(priority);
%>

<script>
location.replace("main.jsp");
</script>
