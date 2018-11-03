<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>

<%--
Entry : print_TODO의 삭제버튼 클릭 시 인자로 priority를 넘겨받음

TODO삭제를 하기 전 사용자로 부터 삭제작업에 대한 재확인을 하는 페이지
script 내에 JSP 구문을 추가할 수 없어 isYes 객체를 이용해 구현함
Firefox에서는 정상작동되지 않는다.("확인" 선택 시 무한 새로고침 발생)
--%>
<%
request.setCharacterEncoding("UTF-8");
int priority = Integer.parseInt(request.getParameter("priority"));

Boolean isYes = (Boolean)session.getAttribute("isYes");

if(isYes == null) {
	isYes = true;
	session.setAttribute("isYes", isYes);
}
else {
	session.removeAttribute("isYes");
%>
<jsp:forward page="delete_TODO.jsp">
	<jsp:param name="priority" value="<%=priority%>" />
</jsp:forward>
<%
}
%>

<script>
if(confirm("정말로 삭제하시겠습니까?"))
	history.go(0);
else
	location.replace("print_TODO.jsp?priority="+<%=priority%>);
</script>