<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<body>
<h2>Hello World123!</h2>

springmvc上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springmvc上传文件"/>

</form>

springmvc上传文件富文本
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本文件上传"/>

</form>
</body>
</html>
