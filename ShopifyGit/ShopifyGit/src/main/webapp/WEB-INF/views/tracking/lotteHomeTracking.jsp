<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
	<script type="text/javascript">
	    $(document).ready(function(){
	        setTimeout(function() {
	            var form = document.tracking;
	            form.submit();          
	        }, 100);
	    });
	</script>
</head>

<body>
    <form name="tracking" method="post" action="https://www.lotteglogis.com/home/reservation/tracking/linkView">
        <input type="hidden" name="InvNo" value="${invNo}">
    </form>
</body>
</html>