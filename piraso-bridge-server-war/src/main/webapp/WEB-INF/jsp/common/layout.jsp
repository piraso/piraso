<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://piraso.org/taglibs/scripts" prefix="scripts" %>

<tiles:importAttribute scope="request" />

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><c:if test="${not empty title}"><fmt:message key="${title}"/> · </c:if><fmt:message key="app.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <c:forEach var="style" items="${styles}">
        <link href="<c:url value='${style}'/>" rel="stylesheet" type="text/css"/>
    </c:forEach>

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <link rel="shortcut icon" href="<c:url value='/assets/ico/favicon.ico'/>">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value='/assets/ico/apple-touch-icon-144-precomposed.png'/>">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value='/assets/ico/apple-touch-icon-114-precomposed.png'/>">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value='/assets/ico/apple-touch-icon-72-precomposed.png'/>">
    <link rel="apple-touch-icon-precomposed" href="<c:url value='/assets/ico/apple-touch-icon-57-precomposed.png'/>">
</head>
<body<c:if test="${not empty scrollspy}"> data-spy="scroll" data-target="${scrollspy}"</c:if>>

<c:if test="${not empty header}">
    <tiles:insertAttribute name="header" />
</c:if>

<div class="container">
<c:choose>
    <c:when test="${fn:endsWith(body, '.jsp')}">
        <tiles:insertAttribute name="body" />
    </c:when>
    <c:otherwise>
        <c:import url="${body}"/>
    </c:otherwise>
</c:choose>

<c:if test="${not empty footer}">
    <tiles:insertAttribute name="footer" />
</c:if>
</div>

<c:forEach var="script" items="${scripts}">
    <script src="<c:url value='${script}'/>" type="text/javascript"></script>
</c:forEach>
<scripts:out/>
</body>
</html>