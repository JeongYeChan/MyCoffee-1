<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<link href="../../resources/css/bootstrap.css" rel="stylesheet" />
<link href="../../resources/css/driverorder.css" rel="stylesheet" />
<script src="https://code.jquery.com/jquery-3.4.1.js"
	integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
	crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>배달 목록</title>
</head>
<script type="text/javascript">
    $(document).ready(function () {

    	const itemForm = $("#itemForm")
    	
        $("div.card-header").on("click", function (e) {
            const cardIcon = $(this).find("img");
            const cardBody = $(this).next();
            console.log(cardIcon.attr("src"));
            if (cardBody.hasClass("show")) {
                cardBody.removeClass("show");
                cardIcon.attr("src", "/resources/img/togle2.png");
            } else {
                cardBody.addClass("show");
                cardIcon.attr("src", "/resources/img/togle.png");
            }
        });
        
    	$("#btnConfirm").on("click", function(e){
    		let uri = "/driver/member/driverOrder/delivery";
    		if($(this).is(${status==4})){
    			uri = "/driver/member/driverOrder/confirm";
    		}
    	});
    	
//         $("#btnConfirm").on("click", function(e){
// 			itemForm.attr("action", "/driver/member/driverOrder/confirm");
// 			itemForm.submit();
//         });

		

    });
</script>
<style>
    .driver-orders {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
    }

    .card-order {
        min-width: 450px;
        max-width: 600px;
    }
</style>
<body>
	<%@include file="../../driver/include/menu.jsp" %>   

	<form id="itemForm" action="#" method="post">
	<div class="container mt-3 driver-orders">
	<c:set var="newOrder" value="0"/>
	<c:forEach items="${order}" var="order" varStatus="status">
		<c:if test="${order.status==3}"><c:set var="newOrder" value="${newOrder + 1}"/></c:if>

        <div class="card m-2 shadow card-order">
            <div class="card-header">
                <div class="d-flex justify-content-between">
                    <div class="d-flex align-items-center">
                        <img src="/resources/img/togle2.png">
                        <div class="ml-3">
                            <h5>주문번호</h5>
                            <p>${order.oid}</p>
                        </div>
                    </div> 
                    <p class="${order.status==3?'text-danger':'text-primary'}">${order.status2}</p>
                </div>
            </div>
            <div class="card-body collapse">
                <div class="d-flex justify-content-between">
                    <h5>주문 내역</h5>
                    <c:if test="${order.status<5}">
                    <button type="button" id="btnConfirm" class="btn ${order.status==3?'btn-primary':'btn-warning'}">${order.status==3?'접수':'완료'}</button>
                    </c:if>
                </div>
                <p>${order.orderdetail}</p>
                <h5>주소</h5>
                <p>${order.address}</p>
            </div>
        </div>
	</c:forEach>
	<p>
	</div>
	</form>

</body>

</html>