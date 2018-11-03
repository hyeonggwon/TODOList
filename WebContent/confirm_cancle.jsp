<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>

<%--
Entry : edit_TODO.jsp에서 취소 버튼 클릭
작성 취소 시 2페이지 전으로 돌아가고,
계속 작성할 시 1페이지 전으로 돌아감
--%>

<script>
if(confirm("작성을 취소하시겠습니까?")) {
	history.go(-2);
}
else {
	history.go(-1);
}
</script>