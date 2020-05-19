<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>
    var deleteOrderItem = false;
    var deleteOrderItemid = 0;
    $(function(){
        //点击删除按钮，弹出删除确认框
        $("a.deleteOrderItem").click(function(){
            deleteOrderItem = false;
            var oiid = $(this).attr("oiid");
            deleteOrderItemid = oiid;
            $("#deleteConfirmModal").modal('show');
        });
        //点击是否删除按钮后关闭确认模态
        $("button.deleteConfirmButton").click(function(){
            deleteOrderItem = true;
            $("#deleteConfirmModal").modal('hide');
        });
        //点击确认删除后，发送ajax请求删除订单项
        $('#deleteConfirmModal').on('hidden.bs.modal', function (e) {
            if(deleteOrderItem){
                var page="foredeleteOrderItem";
                $.post(
                    page,
                    {"oiid":deleteOrderItemid},
                    function(result){
                        if("success"==result){
                            //移除需删除的订单项栏
                            $("tr.cartProductItemTR[oiid="+deleteOrderItemid+"]").remove();
                            //重新计算总价格
                            calcCartSumPriceAndNumber();
                        }
                        else{
                            //删除失败，跳转到登陆页面
                            location.href="loginPage";
                        }
                    }
                );
            }
        })
        //点击选中某产品后，同步更新背景选择高亮、选择框状态、结算按钮可用、计算总数和总价
        $("img.cartProductItemIfSelected").click(function(){
            var selectit = $(this).attr("selectit");
            if("selectit"==selectit){
                $(this).attr("src","img/site/cartNotSelected.png");
                $(this).attr("selectit","false");
                $(this).parents("tr.cartProductItemTR").css("background-color","#fff");
            }
            else{
                $(this).attr("src","img/site/cartSelected.png");
                $(this).attr("selectit","selectit");
                $(this).parents("tr.cartProductItemTR").css("background-color","#FFF8E1");
            }
            syncSelect();
            syncCreateOrderButton();
            calcCartSumPriceAndNumber();
        });
        //点击全选按钮，全部选中和取消全部选中，并使结算按钮可用、更新总数和总计
        $("img.selectAllItem").click(function(){
            var selectit = $(this).attr("selectit");
            if("selectit"==selectit){
                //全选
                $("img.selectAllItem").attr("src","img/site/cartNotSelected.png");
                $("img.selectAllItem").attr("selectit","false");
                $(".cartProductItemIfSelected").each(function(){
                    $(this).attr("src","img/site/cartNotSelected.png");
                    $(this).attr("selectit","false");
                    $(this).parents("tr.cartProductItemTR").css("background-color","#fff");
                });
            }
            else{
                //全不选
                $("img.selectAllItem").attr("src","img/site/cartSelected.png");
                $("img.selectAllItem").attr("selectit","selectit")
                $(".cartProductItemIfSelected").each(function(){
                    $(this).attr("src","img/site/cartSelected.png");
                    $(this).attr("selectit","selectit");
                    $(this).parents("tr.cartProductItemTR").css("background-color","#FFF8E1");
                });
            }
            syncCreateOrderButton();
            calcCartSumPriceAndNumber();
        });
        //使用键盘输入设置产品数量
        $(".orderItemNumberSetting").keyup(function(){
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid="+pid+"]").text();
            var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
            var num = $(".orderItemNumberSetting[pid="+pid+"]").val();
            //对数量超过边界时处理
            num = parseInt(num);
            if(isNaN(num))
                num = 1;
            if(num<=0)
                num = 1;
            if(num>stock)
                num = stock;
            //同步单个产品总价
            syncPrice(pid,num,price);
        });
        //点击"+"时，数量加一，并同步更新单个产品总价
        $(".numberPlus").click(function(){

            var pid=$(this).attr("pid");
            var stock= $("span.orderItemStock[pid="+pid+"]").text();
            var price= $("span.orderItemPromotePrice[pid="+pid+"]").text();
            var num= $(".orderItemNumberSetting[pid="+pid+"]").val();

            num++;
            if(num>stock)
                num = stock;
            //同步单个订单项，同时会同步选中的订单项总数和总价
            syncPrice(pid,num,price);
        });
        //点击"-"时，数量减一，并同步更新单个产品总价
        $(".numberMinus").click(function(){
            var pid=$(this).attr("pid");
            var stock= $("span.orderItemStock[pid="+pid+"]").text();
            var price= $("span.orderItemPromotePrice[pid="+pid+"]").text();

            var num= $(".orderItemNumberSetting[pid="+pid+"]").val();
            --num;
            if(num<=0)
                num=1;
            //同步单个订单项，同时会同步选中的订单项总数和总价
            syncPrice(pid,num,price);
        });
        //点击结算按钮，跳转到结算页面
        $("button.createOrderButton").click(function(){
            var params = "";
            $(".cartProductItemIfSelected").each(function(){
                if("selectit"==$(this).attr("selectit")){
                    var oiid = $(this).attr("oiid");
                    params += "&oiid="+oiid;
                }
            });
            params = params.substring(1);
            //参数params是类似于：oiid=1&oiid=2&oiid=3 的所有订单项id,后台用数组接收
            location.href="forebuy?"+params;
        });
    })

    //当有产品选中时，结算功能可用，否则不可用
    function syncCreateOrderButton(){
        var selectAny = false;
        $(".cartProductItemIfSelected").each(function(){
            if("selectit"==$(this).attr("selectit")){
                selectAny = true;
            }
        });

        if(selectAny){
            //至少有一个订单项被选中
            $("button.createOrderButton").css("background-color","#C40000");
            $("button.createOrderButton").removeAttr("disabled");
        }
        else{
            //没有一个订单项被选中
            $("button.createOrderButton").css("background-color","#AAAAAA");
            $("button.createOrderButton").attr("disabled","disabled");
        }
    }

    //设置全选与全不选功能,以图片显示
    function syncSelect(){
        var selectAll = true;
        $(".cartProductItemIfSelected").each(function(){
            if("false"==$(this).attr("selectit")){
                selectAll = false;
            }
        });

        if(selectAll)
            $("img.selectAllItem").attr("src","img/site/cartSelected.png");
        else
            $("img.selectAllItem").attr("src","img/site/cartNotSelected.png");
    }

    //计算选中的订单项总数和总价
    function calcCartSumPriceAndNumber(){
        var sum = 0;
        var totalNumber = 0;
        //取所有被选中的订单项
        $("img.cartProductItemIfSelected[selectit='selectit']").each(function(){
            var oiid = $(this).attr("oiid");
            var price = $(".cartProductItemSmallSumPrice[oiid="+oiid+"]").text();
            //正则表达式去除其他符号取数据
            price = price.replace(/,/g, "");
            price = price.replace(/￥/g, "");
            sum += new Number(price);
            //获取所有订单总数
            var num = $(".orderItemNumberSetting[oiid="+oiid+"]").val();
            totalNumber += new Number(num);
        });
        //设置订单总数，总价
        $("span.cartSumPrice").html("￥"+formatMoney(sum));
        $("span.cartTitlePrice").html("￥"+formatMoney(sum));
        $("span.cartSumNumber").html(totalNumber);
    }

    //发送Ajax请求同步更新单个产品数量
    function syncPrice(pid,num,price){
        $(".orderItemNumberSetting[pid="+pid+"]").val(num);
        var cartProductItemSmallSumPrice = formatMoney(num*price);
        $(".cartProductItemSmallSumPrice[pid="+pid+"]").html("￥"+cartProductItemSmallSumPrice);
        //同步更新购物总数和总价
        calcCartSumPriceAndNumber();

        var page = "forechangeOrderItem";
        $.post(
            page,
            {"pid":pid,"number":num},
            function(result){
                if("success"!=result){
                    location.href="login.jsp";
                }
            }
        );
    }
</script>

<title>购物车</title>
<div class="cartDiv">
    <div class="cartTitle pull-right">
        <span>已选商品  (不含运费)</span>
        <span class="cartTitlePrice">￥0.00</span>
        <button class="createOrderButton" disabled="disabled">结 算</button>
    </div>

    <div class="cartProductList">
        <table class="cartProductTable">
            <thead>
            <tr>
                <th class="selectAndImage">
                    <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
                    全选

                </th>
                <th>商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th width="120px">金额</th>
                <th class="operation">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${ois }" var="oi">
                <tr oiid="${oi.id}" class="cartProductItemTR">
                    <td>
                        <img selectit="false" oiid="${oi.id}" class="cartProductItemIfSelected" src="img/site/cartNotSelected.png">
                        <a style="display:none" href="#nowhere"><img src="img/site/cartSelected.png"></a>
                        <img class="cartProductImg"  src="img/productSingle_middle/${oi.product.firstProductImage.id}.jpg">
                    </td>
                    <td>
                        <div class="cartProductLinkOutDiv">
                            <a href="foreproduct?pid=${oi.product.id}" class="cartProductLink">${oi.product.name}</a>
                            <div class="cartProductLinkInnerDiv">
                                <img src="img/site/creditcard.png" title="支持信用卡支付">
                                <img src="img/site/7day.png" title="消费者保障服务,承诺7天退货">
                                <img src="img/site/promise.png" title="消费者保障服务,承诺如实描述">
                            </div>
                        </div>

                    </td>
                    <td>
                        <span class="cartProductItemOringalPrice">￥${oi.product.originalPrice}</span>
                        <span  class="cartProductItemPromotionPrice">￥${oi.product.promotePrice}</span>

                    </td>
                    <td>

                        <div class="cartProductChangeNumberDiv">
                            <span class="hidden orderItemStock " pid="${oi.product.id}">${oi.product.stock}</span>
                            <span class="hidden orderItemPromotePrice " pid="${oi.product.id}">${oi.product.promotePrice}</span>
                            <a  pid="${oi.product.id}" class="numberMinus" href="#nowhere">-</a>
                            <input pid="${oi.product.id}" oiid="${oi.id}" class="orderItemNumberSetting" autocomplete="off" value="${oi.number}">
                            <a  stock="${oi.product.stock}" pid="${oi.product.id}" class="numberPlus" href="#nowhere">+</a>
                        </div>

                    </td>
                    <td >
                            <span class="cartProductItemSmallSumPrice" oiid="${oi.id}" pid="${oi.product.id}" >
                            ￥<fmt:formatNumber type="number" value="${oi.product.promotePrice*oi.number}" minFractionDigits="2"/>
                            </span>

                    </td>
                    <td>
                        <a class="deleteOrderItem" oiid="${oi.id}"  href="#nowhere">删除</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>

    <div class="cartFoot">
        <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
        <span>全选</span>
        <!--         <a href="#">删除</a> -->

        <div class="pull-right">
            <span>已选商品 <span class="cartSumNumber" >0</span> 件</span>

            <span>合计 (不含运费): </span>
            <span class="cartSumPrice" >￥0.00</span>
            <button class="createOrderButton" disabled="disabled" >结  算</button>
        </div>

    </div>

</div>