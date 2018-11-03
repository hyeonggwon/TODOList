<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>

<%--
main.jsp에서 완료 상태인 TODO의 재실행 버튼을 눌렀을 때 실행되는 페이지
todoManager로 우선순위를 넘겨  해당 우선순위의 TODO에 대한 재실행 처리를 한 뒤
main.jsp로 돌아감
--%>
<%
request.setCharacterEncoding("UTF-8");
int priority = Integer.parseInt(request.getParameter("priority"));
todoManager.restart(priority);
%>

<script>
location.replace("main.jsp");
</script>