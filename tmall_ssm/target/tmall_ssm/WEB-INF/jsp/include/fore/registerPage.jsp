<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>
    $(function(){
        //名称框失去焦点验证：输入用户名否，且存在否
        $("#name").blur(function (){
            nameOK();
        });
        //密码框失去焦点验证输入密码否
        $("#password").blur(function () {
            passwordOk();
        })
        //密码确认框失去焦点验证两次密码一致否
        $("#repeatpassword").blur(function () {
            var repeatpasseordOK = repeatpasswordOK();
            if(repeatpasseordOK){
                passwordAndRepeatOK();
            }
        })

        //表单提交时，验证信息输入否
        submitUser();
    });

    //判断用户名合法函数
    function nameOK() {
        var name = $("#name").val();
        if(name.trim().length != 0){
            $.post("foreisExist",{"name":name},function (result) {
                if(result == "exist"){
                    //用户名已经存在，给出提示信息
                    $("span.errorMessage").html("用户名已经被使用，请重新输入!");
                    $("div.registerErrorMessageDiv").css("visibility","visible");
                    return false;
                }else{
                    //用户名不存在，操作合法
                    $("span.errorMessage").html("");
                    $("div.registerErrorMessageDiv").css("visibility","hidden");
                    return true;
                }
            });
        }else{
            //未输入用户名
            $("span.errorMessage").html("请输入用户名");
            $("div.registerErrorMessageDiv").css("visibility","visible");
            return false;
        }
    }

    //判断密码合法函数
    function passwordOk() {
        if(0 == $("#password").val().length){
            //为输入密码
            $("span.errorMessage").html("请输入密码");
            $("div.registerErrorMessageDiv").css("visibility","visible");
            return false;
        }else{
            //已输入密码，操作合法
            $("span.errorMessage").html("");
            $("div.registerErrorMessageDiv").css("visibility","hidden");
            return true;
        }
    }

    //判断密码确认合法函数
    function repeatpasswordOK() {
        if(0 == $("#repeatpassword").val().length){
            //未输入确认密码
            $("span.errorMessage").html("请输入重复密码");
            $("div.registerErrorMessageDiv").css("visibility","visible");
            return false;
        }else {
            //已输入确认密码，验证两次密码一致性
            passwordAndRepeatOK();
        }
    }

    //验证两次密码一致性函数
    function passwordAndRepeatOK() {
        if($("#password").val() != $("#repeatpassword").val()){
            $("span.errorMessage").html("重复密码不一致");
            $("div.registerErrorMessageDiv").css("visibility","visible");
            return false;
        }else{
            $("span.errorMessage").html("");
            $("div.registerErrorMessageDiv").css("visibility","hidden");
            return true;
        }
    }

    //提交表单前验证输入信息，得到服务端反馈的函数
    function submitUser(){
        //得到服务端查询的用户名是否存在的结果信息
        <c:if test="${!empty msg}">
        $("span.errorMessage").html("${msg}");
        $("div.registerErrorMessageDiv").css("visibility","visible");
        </c:if>

        $(".registerForm").submit(function(){
            if(0==$("#name").val().length){
                $("span.errorMessage").html("请输入用户名");
                $("div.registerErrorMessageDiv").css("visibility","visible");
                return false;
            }
            if(0==$("#password").val().length){
                $("span.errorMessage").html("请输入密码");
                $("div.registerErrorMessageDiv").css("visibility","visible");
                return false;
            }
            if(0==$("#repeatpassword").val().length){
                $("span.errorMessage").html("请输入重复密码");
                $("div.registerErrorMessageDiv").css("visibility","visible");
                return false;
            }
            if($("#password").val() !=$("#repeatpassword").val()){
                $("span.errorMessage").html("重复密码不一致");
                $("div.registerErrorMessageDiv").css("visibility","visible");
                return false;
            }
            return true;
        });
    }
</script>

<form method="post" action="foreregister" class="registerForm">

    <div class="registerDiv">
        <div class="registerErrorMessageDiv">
            <div class="alert alert-danger" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
                <span class="errorMessage"></span>
            </div>
        </div>

        <table class="registerTable" align="center">
            <tr>
                <td  class="registerTip registerTableLeftTD">设置会员名</td>
                <td></td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">登陆名</td>
                <td  class="registerTableRightTD"><input id="name" name="name" placeholder="会员名一旦设置成功，无法修改" > </td>
            </tr>
            <tr>
                <td  class="registerTip registerTableLeftTD">设置登陆密码</td>
                <td  class="registerTableRightTD">登陆时验证，保护账号信息</td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">登陆密码</td>
                <td class="registerTableRightTD"><input id="password" name="password" type="password"  placeholder="设置你的登陆密码" > </td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">密码确认</td>
                <td class="registerTableRightTD"><input id="repeatpassword" type="password"   placeholder="请再次输入你的密码" > </td>
            </tr>

            <tr>
                <td colspan="2" class="registerButtonTD">
                    <%--在不同的浏览器下，button的反应不一样。有的会跳转有的并不会跳转。所以加个超链。--%>
                    <a href="registerSuccess.jsp"><button>提   交</button></a>
                </td>
            </tr>
        </table>
    </div>
</form>