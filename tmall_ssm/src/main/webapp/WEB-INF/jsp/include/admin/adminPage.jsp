<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

	
<script>
    $(function(){
        $("ul.pagination li.disabled a").click(function(){
            return false;
        });
    });


</script>


<nav>
  <ul class="pagination">
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
        <%-- 分页超链接总是相对于当前页面 --%>
      <a  href="?start=0${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>

    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?start=${page.start-page.count}${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&lsaquo;</span>
      </a>
    </li>

      <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
          <%-- status.index为当前页减1，status.count为当前页 --%>
          <%-- 分页总数大于5页 --%>
          <c:if test="${page.totalPage >= 5}">
              <%-- 页码小于3时，补齐左边不足 --%>
              <c:if test="${page.start < 15}">
                  <c:if test="${status.count*page.count <= 25}">
                      <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
                          <a href="?start=${status.index*page.count}${page.param}"
                             <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
                          >${status.count}</a>
                      </li>
                  </c:if>
              </c:if>
              <%-- 页码较大时，补齐右边不足 --%>
              <c:if test="${(page.start/5+1) > (page.totalPage-2)}">
                  <c:if test="${status.count > page.totalPage-5}">
                      <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
                          <a href="?start=${status.index*page.count}${page.param}"
                             <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
                          >${status.count}</a>
                      </li>
                  </c:if>
              </c:if>
              <%-- 页码合适时，前2页后2页 --%>
              <c:if test="${page.start >= 15 && (page.start/5+1) <= (page.totalPage-2)}">
                  <c:if test="${status.count*page.count-page.start <= 15 && status.count*page.count-page.start >= -5}">
                      <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
                          <a href="?start=${status.index*page.count}${page.param}"
                             <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
                          >${status.count}</a>
                      </li>
                  </c:if>
              </c:if>
          </c:if>
          <%-- 分页不足5页时，直接显示所有 --%>
          <c:if test="${page.totalPage < 5}">
              <c:if test="${status.count >= 1 && status.count <= page.totalPage}">
                  <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
                      <a href="?start=${status.index*page.count}${page.param}"
                         <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
                      >${status.count}</a>
                  </li>
              </c:if>
          </c:if>

      </c:forEach>


    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.start+page.count}${page.param}" aria-label="Next">
        <span aria-hidden="true">&rsaquo;</span>
      </a>
    </li>
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.last}${page.param}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
