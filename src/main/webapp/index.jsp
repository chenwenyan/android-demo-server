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
    <script type="text/javascript" src="/static/jQuery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/bootstrap/css/bootstrap.css">
</head>
<body>
<div style="width:50%;margin-left:25%">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">语言Le的抽象机状态转换动态演示</h3>
        </div>
        <div class="panel-body">
            <form name="expressionForm" id="expressionForm" role="form">
                <div class="form-group">
                    <label for="expressionInput">表达式：</label>
                    <input class="form-control" type="text" name="expressionInput" id="expressionInput"
                           value="ge(add(var(x),mul(cons(2),var(y))),var(z))"/>
                </div>
                <div class="form-group">
                    <label for="DEnvInput">
                        初始化动态环境：
                    </label>
                    <input class="form-control" type="text" name="initDenvInput" id="initDenvInput"
                           value="x->34, y->7, z->50"/>
                </div>
            </form>
        </div>
        <div class="panel-footer">
            <button class="btn btn-default" type="reset">重置</button>
            <button class="btn btn-primary" name="submitExpression" id="submitExpression">提交</button>
        </div>
    </div>
    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">结果</h3>
        </div>
        <div class="panel-body">
            <form name="comForm" id="comForm" role="form">
                <div class="form-group">
                    <div name="rule" id="rule" class="">
                        <label for="controlInput">使用规则：</label>
                        <div><textarea class="form-control" rows="2" id="ruleInput" name="ruleInput"></textarea></div>
                    </div>
                    <div name="control" id="control" class="">
                        <label for="controlInput">控制区：</label>
                        <div><textarea class="form-control" rows="2" id="controlInput" name="controlInput"></textarea></div>
                    </div>
                    <div name="stack" id="stack" class="">
                        <label for="stackInput">栈：</label>
                        <div><textarea class="form-control" rows="2" id="stackInput" name="stackInput"></textarea></div>
                    </div>
                    <div name="DEnv" id="DEnv" class="">
                        <label for="DEnvInput">动态环境：</label>
                        <div><textarea class="form-control" rows="2" id="DEnvInput" name="DEnvInput"></textarea></div>
                    </div>
                </div>

            </form>
        </div>
        <div class="panel-footer">
            <button class="btn btn-default" type="reset">重置</button>
            <button class="btn btn-success" name="computeBtn" id="computeBtn">下一步</button>
        </div>
    </div>

    <div name="msg" id="msg" class="alert alert-block">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>提示：</strong>
        <p id="msgDiv" name="msgDiv"></p>
    </div>

</div>

<script type="text/javascript">
    $(function () {
        $("#submitExpression").click(function (e) {
            var expression = $("#expressionInput").val().trim();
            var initDenv = $("#initDenvInput").val().trim();
            if (expression == "" || expression == null) {
                alert("输入表达式不能为空！");
            } else {
                $.ajax({
                    type: "POST",
                    url: "/start",
                    data: {expressionInput: expression, initDenvInput: initDenv},
                    success: function (res) {
                        console.log(res);
                        if (res.code == 1) {
                            console.log(res.data);
                            var data = res.data;
                            $("#ruleInput").val();
                            $("#controlInput").val("");
                            $("#controlInput").val(data.initControlField);
                            $("#stackInput").val("");
                            $("#stackInput").val(data.initStackField);
                            $("#DEnvInput").val("");
                            $("#DEnvInput").val(data.initDEnvField);
                        } else {
                            $("#msgDiv").innerHTML = res.msg;
                        }
                    },
                    error: function (res) {
                        alert("请求出错！");
                    },
                    dataType: "json"
                });
            }
        });

        $("#computeBtn").click(function (e) {
            var control = $("#controlInput").val();
            var stack = $("#stackInput").val();
            var DEnv = $("#DEnvInput").val();
            $.ajax({
                type: "POST",
                url: "/next",
                data: {control: control, stack:stack, DEnv: DEnv},
                success: function (res) {
                    console.log(res);
                    if (res.code == 1) {
                        console.log(res.data);
                        var obj = res.data;
                        console.log(obj);
                        $("#ruleInput").val("");
                        $("#ruleInput").val(obj.rule);
                        $("#controlInput").val("");
                        $("#controlInput").val(obj.control);
                        $("#stackInput").val("");
                        $("#stackInput").val(obj.stack);
                        $("#DEnvInput").val("");
                        $("#DEnvInput").val(obj.DEnv);
//                        setTimeout(setValue,3000);
//                        function setValue(){
//                            for(var obj in data) {
//                                $("#ruleInput").val("");
//                                $("#ruleInput").val(obj.rule);
//                                $("#controlInput").val("");
//                                $("#controlInput").val(obj.control);
//                                $("#stackInput").val("");
//                                $("#stackInput").val(obj.stack);
//                                $("#DEnvInput").val("");
//                                $("#DEnvInput").val(obj.DEnv);
//                            }
//                        }
                    }
                },
                error: function (res) {
                    alert("请求出错！");
                },
                dataType: "json"
            });
        });

    });
</script>
</body>
</html>
