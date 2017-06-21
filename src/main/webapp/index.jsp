<%--
  Created by IntelliJ IDEA.
  User: chenwy
  Date: 2017/6/15
  Time: 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>抽象机表达式动态演示</title>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<div>
    <form name="expressionForm" id="expressionForm" action="/getExpression" method="post">
        请输入表达式：
        <input type="text" name="expressionInput" id="expressionInput"/>
        <br>
        <button type="reset">重置</button>
        <button name="submitExpression" id="submitExpression">提交</button>
    </form>
    <form name="comForm" id="comForm" action="/compute" method="post">
        <div name="control" id="control">
            控制区：
            <div><textarea id="controlInput" name="controlInput"></textarea></div>
        </div>
        <div name="stack" id="stack">
            栈：
            <div><textarea id="stackInput" name="stackInput">[]</textarea></div>
        </div>
        <div name="DEnv" id="DEnv">
            动态环境：
            <div><textarea id="DEnvInput" name="DEnvInput"></textarea></div>
        </div>
        <button type="reset">重置</button>
        <button name="computeBtn" id="computeBtn">下一步</button>
    </form>
    <div name="msg" id="msg">
        提示：
        <div id="msgDiv" name="msgDiv">${result.msg}</div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $("#submitExpression").onclick = function () {
            var expression = $("#expressionInput").value.trim();
            if (expression == "" || expression == null) {
                alert("输入表达式不能为空！");
            } else {
                $.ajax({
                    url: "/getExpression",
                    data: expression,
                    success: function (res) {
                        if (res.code == 1) {
                            $("#controlDiv").innerText = res.data;
                        } else {
                            $("#msgDiv").innerHTML = res.msg;
                        }
                    }
                });
            }
        }

        $("#submitExpression").onclick = function () {
            var control = $("#controlInput").value.trim();
            var DEnv = $("#DEnvInput").value.trim();
            if (control == "" || control == null) {
                alert("控制区不能为空！");
                return;
            }else if(DEnv == "" || DEnv == null){
                alert("动态环境变量不能为空！");
                return;
            } else {
                $.post( "/compute", {control:control,DEnv : DEnv}, function (res) {
                        $("#controlInput").innerText = res.control;
                        $("#stackInput").innerText = res.stack;
                        $("#DEnvInput").innerText = res.DEnv;
                });
            }
        }

    });
</script>
</body>
</html>
