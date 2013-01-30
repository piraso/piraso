<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://piraso.org/taglibs/scripts" prefix="scripts" %>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a id="piraso_logo" class="brand" href="http://piraso.org/"><fmt:message key="app.logo.title"/></a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<header class="jumbotron subhead" id="overview">
    <div class="container">
        <h1><i class="icon-magic"></i> <fmt:message key="${subTitle}"/></h1>
        <p class="lead"><fmt:message key="${subTitle}.desc"/></p>
    </div>
</header>

