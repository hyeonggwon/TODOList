<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<jsp:useBean id="todoManager" class="todo.TODOManager" scope="application"/>

<%--
TODO를 삭제하는 페이지
todoManager에게 우선순위를 넘겨주고 todoManager가 처리한 뒤
main.jsp로 돌아감
--%>

<script>
alert("삭제가 완료되었습니다.");
</script>

<%
request.setCharacterEncoding("UTF-8");
todoManager.delete(Integer.parseInt(request.getParameter("priority")));
response.sendRedirect("main.jsp");
%>