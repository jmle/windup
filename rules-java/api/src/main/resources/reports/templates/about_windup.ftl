<!DOCTYPE html>
<html lang="en">

<#assign applicationReportIndexModel = reportModel.applicationReportIndexModel>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        <#if reportModel.projectModel??>
            ${reportModel.projectModel.name} - About
        <#else>
            About ${getWindupBrandName()}
        </#if>
    </title>
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/font-awesome.min.css" rel="stylesheet" />
    <link href="resources/css/windup.css" rel="stylesheet" media="screen">
    <link href="resources/css/windup.java.css" rel="stylesheet" media="screen">

    <#assign basePath="resources">
    <#include "include/favicon.ftl">

    <script src="resources/js/jquery-3.3.1.min.js"></script>
</head>
<body role="document">

    <!-- Navbar -->
    <div id="main-navbar" class="navbar navbar-inverse navbar-fixed-top">
        <div class="wu-navbar-header navbar-header">
            <#include "include/navheader.ftl">
        </div>
        <div class="navbar-collapse collapse navbar-responsive-collapse">
            <#include "include/navbar.ftl">
        </div>
    </div>
    <!-- / Navbar -->


    <div class="container-fluid" role="main">
        <div class="row">
            <div class="page-header page-header-no-border">
                <h1>
                    <div class="main">About</div>
                </h1>
            </div>
        </div>

        <div class="row">
            <div class="container-fluid theme-showcase" role="main">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">About ${getWindupBrandName()}</h3>
                    </div>

                    <div class="panel-body">
                        <#include "include/about_links.ftl">
                        <dl class="dl-horizontal">
                            <dt>GitHub Source</dt>
                            <dd><a href="https://github.com/windup/windup">https://github.com/windup/windup</a></dd>

                            <dt>GitHub Wiki</dt>
                            <dd><a href="https://github.com/windup/windup/wiki">https://github.com/windup/windup/wiki</a></dd>

                            <dt>Discussion Forum</dt>
                            <dd><a href="https://developer.jboss.org/en/windup?view=discussions">https://developer.jboss.org/en/windup</a></dd>

                            <dt>Mailing List</dt>
                            <dd><a href="https://lists.jboss.org/mailman/listinfo/windup-dev">https://lists.jboss.org/mailman/listinfo/windup-dev</a></dd>

                            <dt>Issues Tracking</dt>
                            <dd><a href="https://issues.jboss.org/browse/WINDUP">https://issues.jboss.org/browse/WINDUP</a></dd>

                        </dl>
                    </div>
                </div>

            </div>
        </div>

        <#include "include/timestamp.ftl">
    </div> <!-- /container -->



    <script src="resources/libraries/flot/jquery.flot.min.js"></script>
    <script src="resources/libraries/flot/jquery.flot.pie.min.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>

</body>
</html>
