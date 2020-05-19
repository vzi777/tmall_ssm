<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>
    $(function(){
        //随机(概率为1/7)挑选一个产品作为推荐产品，高亮显示
        $("div.productsAsideCategorys div.row a").each(function(){
            var v = Math.round(Math.random() *6); //能够取到0 1 2 3 4 5 6 共计7个数
            if(v == 1)
                $(this).css("color","#87CEFA");
        });
    });

</script>
<c:forEach items="${cs}" var="c">
    <div cid="${c.id}" class="productsAsideCategorys">

        <c:forEach items="${c.productsByRow}" var="ps">
            <div class="row show1">
                <c:forEach items="${ps}" var="p">
                    <c:if test="${!empty p.subTitle}">
                        <a href="foreproduct?pid=${p.id}">
                            <%-- 显示副标题以空格分隔的第一个字符串 --%>
                            <c:forEach items="${fn:split(p.subTitle, ' ')}" var="title" varStatus="st">
                                <c:if test="${st.index==0}">
                                    ${title}
                                </c:if>
                            </c:forEach>
                        </a>
                    </c:if>
                </c:forEach>
                <div class="seperator"></div>
            </div>
        </c:forEach>
    </div>
</c:forEach>
