<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>

<body>
<div align="center">
<h1>MongoDB-GridFS Document Storage</h1>

<form:form method="POST" action="${pageContext.request.contextPath}/upload" modelAttribute="uploadForm" enctype="multipart/form-data">

    <strong>Select file:</strong> <input type="file" name="files" /><br/><br/>
    <input type="submit" value="Submit" />

</form:form>
<br/>
Note : We have file size limit of 150 MB (Configurable)<br/>
<a href="${pageContext.request.contextPath}/home">GO TO HOME</a>
</div>
</body>
</html>