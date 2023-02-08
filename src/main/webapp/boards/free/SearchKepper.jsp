<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-07
  Time: 오후 6:55
  To change this template use File | Settings | File Templates.
--%>
<script>
  function backToList() {
    let searchFilter = "${searchFilter}";
    let category = "${category}";
    let searchWord = "${searchWord}";
    let startDate = "${startDate}";
    let endDate = "${endDate}";
    let pageNum = "${pageNum}";
    if (pageNum == null || pageNum == 0) {
      pageNum = 1;
    }
    let url = "/List.jsp?searchFilter=" + searchFilter + "&category=" + category + "&searchWord="
        + searchWord + "&startDate=" + startDate + "&endDate=" + endDate + "&pageNum=" + pageNum;
    alert(url);
    location.href = url;
  }
</script>