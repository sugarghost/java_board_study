
# Java_board_JSP

간단한 게시판 프로젝트를 JSP, Servlet에 model2 + command 패턴 방식으로 구현한 프로젝트 입니다.  
학습을 위해 Spring을 사용하지 않았습니다.   
이전 프로젝트인 Java_board_JSP에 피드백을 반영하고, 새롭게 피드백을 받았습니다.   
피드백은 다음 프로젝트인 Java_board_Spring에 반영 될 것입니다.

# 피드백 목차
* [이전 Java_board_JSP 피드백](#---java-board-jsp----)
    + [meta와 같은 공통 요소의 처리](#meta--------------)
    + [Util 활용하기](#util-----)
    + [equals 사용시 비교 Text를 기준으로 하기](#equals--------text---------)
    + [Session 사용 피하기](#session-------)
    + [System.out.println() 사용 피하기](#systemoutprintln---------)
    + [파일 저장시 UUID 활용하기](#-------uuid-----)
    + [Java에서 javascript 호출하지 말기](#java---javascript--------)
    + [필드별, 서비스별 유효성 검증을 구분하기](#----------------------)
    + [Vector 대신 ArrayList 사용하기](#vector----arraylist-----)
    + [return을 Finally에 사용하지 말기](#return--finally---------)
    + [Database를 설정할 땐 Class loader를 주의하기](#database--------class-loader------)
    + [Query 잘 짜기](#query-----)
    + [공통적으로 쓰는 설정은 properties 파일로 관리하기](#-------------properties---------)
    + [사용자에게 서버와 관련된 데이터를 보여주지 말기](#--------------------------)
* [신규 Java_board_Servlet 피드백](#---java-board-servlet----)
    + [log4j2대신 logback 사용하기](#log4j2---logback-----)
    + [에러 핸드링 방식 변경](#------------)
    + [url을 가져올 때 @PathParam을 사용고려](#url---------pathparam------)
    + [if else 활용하기](#if-else-----)
    + [if else 치워버리기](#if-else------)
    + [request를 매개변수로 던지지 말기](#request--------------)
    + [이전 페이지로 돌아간다고 referer를 사용하지 말기](#--------------referer---------)
    + [util 클래스를 만든다면 이름은 간결하게](#util-------------------)
    + [null 체크시 "null" 문자열은 별도의 의미를 가질 수 있음](#null------null----------------------)
    + [이름을 지을 때 Wrapper와 Manager를 구분하기](#---------wrapper--manager------)
    + [MyBatis에서 sqlSessionFactory를 외부에 노출하지 않기](#mybatis---sqlsessionfactory-------------)
    + [SqlSession을 사용한다면 각 처리에서 SqlSession 하나만 사용하기](#sqlsession---------------sqlsession---------)
    + [rollback 등의 마지막 처리는 Finally에서 하기](#rollback------------finally-----)
    + [resultMap 대신 resultType 사용하기](#resultmap----resulttype-----)
    + [쿼리문 예쁘게 정리하기](#------------)
    + [properties 파일 활용하기](#properties--------)
    + [DTO에서 boolean에 경우 get을 붙이지 말기](#dto---boolean-----get--------)
    + [한줄짜리 주석도 javaDoc 주석을 활용하기](#---------javadoc---------)
    + [코드 줄이기](#------)

## 이전 Java_board_JSP 피드백

### meta와 같은 공통 요소의 처리

encoding, meta와 같은 공통 요소들은 따로 상위에 빼서 처리하는 것이 좋습니다.   
예시: List.jsp

```java
    Map<String, Object> param=new HashMap<String, Object>();
    // 인코딩은 따로 상위에서 처리하는걸 추천
    request.setCharacterEncoding("utf-8");
    String searchWord=request.getParameter("searchWord");
```

피드백 반영:   
```java
    // 공통적인 인코딩 설정은 모든 요청을 받아 처리하는 MainServlet에서 공통 처리   
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    
    // JSP에서 공통적인 처리는 별도 jsp 파일을 만들고 include로 처리 
    <head>
    <jsp:include page="/common/encode.jsp"></jsp:include>
    <title>자유게시판 - 목록</title>
    <jsp:include page="/common/bootstrap.jsp"></jsp:include>
    </head>
```

### Util 활용하기

공통적인 Null 체크 등의 기능은 따로 util 클래스를 만들어 사용하는 것이 좋습니다.   
예시: List.jsp

```java
    // null 체크는 따로 util 클래스를 만들어 사용하는 것이 좋음
    if(searchWord!=null&&!searchWord.equals("")){
        param.put("searchWord",searchWord);
    }
```

또 다른 예시:

```java
// 모든 처리 부분은 가급적이면 유틸로 처리하기
// 게시글 제목이 80자 이상이면 80자까지만 출력하는 코드
    if(articleDTO.getTitle().length()>80){
        out.print(articleDTO.getTitle().substring(0,80)+"...");
        }else{
        out.print(articleDTO.getTitle());
        }
```

피드백 반영:   

```java
    // Util 클래스 생성
    public class ValidationChecker {
      public static boolean CheckStringIsNullOrEmpty(String targetString) {
        return targetString == null || "".equals(targetString) || targetString.isEmpty()
            || "null".equals(targetString);
      }
      
      public static String SubStringWithSkipMark(String targetString, int StringLength) {
        if (targetString.length() > StringLength) {
          targetString = targetString.substring(0, StringLength) + "...";
        }
        return targetString;
      }
    }
```
### equals 사용시 비교 Text를 기준으로 하기

변수를 기준으로 equals를 사용하는 것은 좋지 않습니다.  
변수의 예기치 않은 데이터가 들어있을 수 있습니다.   
예시: List.jsp

```java
    // 변수를 기준으로 equals를 사용하는 것은 좋지 않음
    if(searchWord!=null&&!searchWord.equals("")){
        param.put("searchWord",searchWord);
    }
```

피드백 반영:  

```java
    // 예시로 searchWord를 검사하는 경우 ""이라는 String을 기준으로 equals 사용
    if("".equals(searchWord)){
        param.put("searchWord",searchWord);
    }
```

### Session 사용 피하기

세션은 동일한 브라우저에서 페이지를 새로 켜도 유지되는 것이 특징입니다.   
검색 조건등에 세션을 사용하면 여러 페이지를 동시에 켜면 검색 조건이 다같이 적용되는 문제가 생깁니다.   
또한 주소를 복사해서 다른 브라우저에 전달하면 검색 조건을 전달 할 수 없습니다.   
예시: List.jsp

```java
    // session 사용 피하기!
    HttpSession session=request.getSession();
    String searchWord=(String)session.getAttribute("searchWord");
```

피드백 반영:  

```java
    // MainServlet.java:
    // SearchManager 라는 검색 조건을 관리하는 클래스를 만들어서 사용
    SearchManager searchManager = new SearchManager(request);
    request.setAttribute("searchManager", searchManager);

    // List.jsp:
    // Servlet에서 전달된 SearchManager를 받아서 사용
    <input type="button" class="btn btn-primary" value="글쓰기"
        onclick="location.href='write.do?${searchManager.getSearchParamsQuery()}'">

    // JSP에서 이동하는 모든 경로에 파라미터로 검색 조건을 던져주고, Servlet에서는 모든 요청에 대해서 SearchManager를 통해 검색 조건을 처리
```

### System.out.println() 사용 피하기

가능한 Log 라이브러리를 사용하는 것이 좋습니다.   
또한 JSP에서 Sytem.out.println()을 사용하는건 좋지 않습니다.   
예시:

```java
    // System.out.println() 사용 피하기
    System.out.println("searchWord : "+searchWord);
```

피드백 반영: 모든 소스에서 System.out.println()을 제거하고 Log4j를 사용하도록 변경   

### 파일 저장시 UUID 활용하기

파일 저장시 SimpleDateFormat을 활용해 파일명을 만들면 동일한 파일명이 저장될 수 있습니다.  
UUID를 활용하면 동일한 파일명이 저장되는 문제를 해결할 수 있습니다.  
예시:

```java
    // 파일을 밀리세컨으로 처리하는 경우 중복이 발생할 수 있음으로 추천하지 않는 방식임
    // UUID 랜덤을 활용하는 방안을 고려
    String nowDate=new SimpleDateFormat("yyyyMMdd_HmsS").format(System.currentTimeMillis());
    String newFileName=nowDate+"."+ext;
```

피드백 반영:   
```java
    // UUID를 활용한 파일명 생성
    String newFileName = UUID.randomUUID() + "." + ext;
```

### Java에서 javascript 호출하지 말기

Java에서 javascript를 호출하는 것은 좋지 않습니다.  
예시: WriteAction.jsp

```java
    if(fileInsertResult==0){
        // 자바쪽에서 스크립트 호출 방식은 안좋은 방식
        out.println("<script>alert('"+fileName+" 파일 등록 실패!')</script>");
    }
```

```java
    // 자바쪽에서 스크립트 호출 방식은 제거함
    // command에서 error 코드를 jsp 페이지에 보내면 jsp 페이지가 받아 처리하도록 변경
    request.setAttribute("error", "3");

    // MainServlet.java: 각 페이지 별로 사용할 에러 메시지에 대한 처리를 추가
    if ("/write.do".equals(servletPath)) {
        viewPage = "/boards/free/write.jsp";
        request.setAttribute("command", "articleWrite");
        errorMessages.put("1", "입력값 오류!");
        errorMessages.put("2", "게시물 등록 실패!");
        errorMessages.put("3", "파일 등록 실패!");
    }
        
    // WriteAction.jsp: 각 JSP에 errorMessage를 처리하는 JSP를 include
    <jsp:include page="/common/message_handler.jsp">
      <jsp:param name="errorMessages" value="${errorMessages}"/>
    </jsp:include>

    // message_handler.jsp: 에러 메시지를 처리하는 JSP
    <script>
        <c:forEach items="${errorMessages}" var="errorMessage">
            <c:if test="${errorMessage.key eq param.error}">
                alert('${errorMessage.value}')
            </c:if>
        </c:forEach>
    </script>
    
    // 참고로 좋은 방법이 아니란 피드백을 받았기에 변경 예정
```

### 필드별, 서비스별 유효성 검증을 구분하기

각각 개별의 유효성 검증을 하는건 좋지만, 자칫 잘못하면 코드의 가독성을 망칩니다.  
때로는 필드별로, 때로는 서비스별로 유효성 검증을 구분하는 것이 좋습니다.  
예시: ArticleDAO.java

```java
    // 필드별 유효성 검증이 많아 가독성을 해침
  // insert 서비스를 기준으로 insertValid() 도입을 고려
    if(!articleDTO.isCategoryValid()||!articleDTO.isWriterValid()
        ||!articleDTO.isPasswordValid()||!articleDTO.isTitleValid()
        ||!articleDTO.isContentValid()){
        logger.debug("insertArticle() : invalid data");
        return 0;
    }
```

피드백 반영: 기존 DTO에 개별 valid 들을 합쳐서 새롭게 서비스별 valid를 생성
```java
  // insert 서비스를 기준으로 insert 전 isInserArticleValid()를 호출하여 유효성 검증
  public boolean isInsertArticleValid() {
    return isWriterValid()
        && isPasswordValid()
        && isTitleValid()
        && isContentValid();
  }
```

### Vector 대신 ArrayList 사용하기

Vector는 동기화를 지원하기 위해 만들어진 클래스입니다.  
ArrayList보다 크고 무겁기 때문에 Tread Safe를 고려해 사용하는게 좋습니다.

```java
    // Vector 대신 ArrayList 사용하기
    List<CategoryDTO> categoryList=new Vector<CategoryDTO>();
```

```java
    // categoryDAO: 
    public List<CategoryDTO> selectCategoryList();
    
    // category를 가져다쓰는 command: 
    // Vector 대신 ArrayList 사용
    List<CategoryDTO> categoryList = categoryDAO.selectCategoryList();
```

### return을 Finally에 사용하지 말기

return을 finally에 사용하는 것은 좋지 않습니다.  
finally는 예외가 발생하더라도 무조건 실행되기 때문에, 오류가 발생해도 return이 되버립니다.

```java
    try {
        // 작업
    } catch(Exception e) {
        logger.error("getCategoryList() ERROR : "+e.getMessage());
        e.printStackTrace();
    } finally {
        MyDatabase.closeConnection(con,pstmt,rs);
        // 오류가 발생해도 return은 무조건 실행됨
        return categoryList;
    }
```

피드백 반영:  return 구문은 try-catch-finally 구문을 벗어나 마지막에 실행하도록 변경  

### Database를 설정할 땐 Class loader를 주의하기

jdbc를 이용해 Database에 접속할 때, Class loader를 반복해서 실행하는 것은 좋지 않습니다.  
Class loader는 연산이 많이 드는 작업이기 때문에, Class loader는 한번만 실행하고, 그 결과를 재사용하는 것이 좋습니다.
예시: MyDatabase.java

```java
    public static Connection getConnection()throws Exception{
        Connection con=null;
    
        // DAO에서 connection을 호출할 때마다 Class Loader가 계속 호출되면서 낭비가됨
        // 싱글톤으로 한다고 하면 인스턴스를 한번 생성할때만 올려주는 방식으로 가야함.
        Class.forName(DRIVER_NAME);
        con=DriverManager.getConnection(URL,USER,PASSWORD);
        return con;
    }
```

피드백 반영: 기존 java_board_jsp에 MyDatabase.java에 반영됨  
```java
  // 생성자에서 Class.forName을 호출하지만 싱글톤으로 private화 시킴
  private MyDatabase() {
    try {
      Class.forName(DRIVER_NAME);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  // 싱글톤 패턴으로 getInstance()를 통해 인스턴스를 생성
  // 기존 instance가 존재하면 기존 instance를 반환하고, 없으면 새로 생성하기 때문에 한번만 생성
  public static MyDatabase getInstance() {
    if(instance==null) {
      instance = new MyDatabase();
    }
    return instance;
  }
```

### Query 잘 짜기

Query를 짤때는 가독성을 고려해야 합니다.  
가능한 코드를 줄이고, 이름은 명확하게 만들어야 합니다.  
예시: ArticleDAO.java

```java
  /*
    // 쿼리 예시
    SELECT Tb.* FROM (
      SELECT *, @ROWNUM:=@ROWNUM+1 AS row_num
      FROM article, (SELECT @ROWNUM:=0) AS R
      WHERE 1=1 ORDER BY article_id DESC
    ) Tb
    WHERE row_num BETWEEN ? AND ?
    // 쿼리 구조가 복잡함, 좀더 효율적인 방법을 고려
    // 테이블 별칭 바꾸기(너무 축약적)
  */
```

피드백 반영: 기존 쿼리를 단순화 시킴  

```sql
    // 기존 ROWNUM을 이용한 이유는 페이지와 게시글 넘버링에 따라 게시글을 가져오기 위함
    // LIMIT와 OFFSET을 사용해 단순화
    SELECT
        article_id, category_id, writer, PASSWORD, 
        title, content, view_count, created_date, modified_date,
        (
        SELECT EXISTS(
            SELECT 1
            FROM article_file
            WHERE article_id = article.article_id
            )
        ) AS is_file_exist
    FROM article
    WHERE 1=1
    ORDER BY article_id DESC
    LIMIT 5 OFFSET 10
)
```

### 공통적으로 쓰는 설정은 properties 파일로 관리하기

공통적으로 쓰는 설정은 properties 파일로 관리하는 것이 좋습니다.    
때로는 환경마다 설정이 다를 수 있기 때문에, properties 파일로 관리하는 것이 좋습니다.  
예시: Download.jsp

```java
    // 디렉토리등 공통적으로 쓰는 경우는 프로퍼티로 따로 빼는게 좋음
    String saveDirectory="C:\\tempUploads";
```

피드백 반영: 까먹고 안함 java_board_Spring에 반영 예정  

### 사용자에게 서버와 관련된 데이터를 보여주지 말기

파일을 DB에 저장해 관리할 때, 사용자에게 서버와 관련된 데이터를 보여주지 말아야 합니다.  
예시: Download.jsp

```java
    // fileSaveName과 같이 서버에 저장된 파일명을 사용자가 볼 수 있도록 하는건 안좋음
    // fileId를 기준으로 검색해 사용하는 것을 추천
    String fileSaveName=request.getParameter("fileSaveName");
    String fileOriginName=request.getParameter("fileOriginName");
```

피드백 반영:  fileSaveName, filePath 등 서버와 관련된 정보를 지우고, fileId를 사용하고 서버에서 검색하는 방식으로 변경  

## 신규 Java_board_Servlet 피드백

### log4j2대신 logback 사용하기
log4j2는 이전에 보안문제로 인한 결함이 있었습니다.  
가능하면 logback을 사용하는 것이 좋습니다.  

### 에러 핸드링 방식 변경
현재 에러를 핸들링 하기 위해 MainServlet에서 요청 별로 errorMessage Map을 만들고 JSP에 전달하는 방식을 사용중입니다.  
이 방식은 에러가 추가될 떄마다 MainServlet에 코드를 추가해야해 가독성을 망칩니다.  
또한 에러 정의를 Command에서 진행하지만 단순 code를 setAttribute하기 때문에 에러에 대해 이해하기가 어렵습니다.  

예시: MainServlet.java  

```java
    // 에러 메시지 Map을 선언
    Map<String, String> errorMessages = new HashMap<>();
    // 요청 별로 에러 메시지를 정의
    if ("/write.do".equals(servletPath)) {
        viewPage = "/boards/free/write.jsp";
        request.setAttribute("command", "articleWrite");
        // 각 요청 별로 정의되기 때문에 가독성을 망침!
        errorMessages.put("1", "입력값 오류!");
        errorMessages.put("2", "게시물 등록 실패!");
        errorMessages.put("3", "파일 등록 실패!");
    }
```

예시: ArticleWriteCommand.java  

```java
    // 에러 메시지를 정의하지만 어떤 에러인지 알기 어려움 
    request.setAttribute("error", "2");
```

에러 핸들링은 별도의 클래스를 만들어 에러를 정의하고, 핸들링하도록 하고,     
에러 발생시 호출해 핸들링 하는 방식으로 변경하면 가독성이 좋아질 것 같습니다.  
본인만의 Exception를 만드는 방안도 고려해보면 좋을 것 같습니다  

### url을 가져올 때 @PathParam을 사용고려  
command 방식 사용시 요청 url을 파악하기 위해 다양한 방법을 사용 가능합니다.   
아래는 subString을 활용한 예시입니다.  
```java
    String uri = request.getRequestURI();
    String conPath = request.getContextPath();
    String command = uri.substring(conPath.length());
```
URL을 가져오고 ContextPath를 가져오는 조금 복잡한 과정을 거칩니다.   
추천하는 방식으로는 @PathParam을 사용하는 것입니다.  
추가로 자신만의 Annotation을 만들어 보는 것을 추천합니다.  
method 실행 전 개입하여 데코레이터처럼 동작하는 AOP 개념의 방식을 사용해보는 것을 추천합니다.  

### if else 활용하기
if를 연속적으로 사용하는 것은 성능을 저하시킬 수 있습니다.  
만약 각 if문에 return이 정의되있다면 return 이후의 if는 실행되지 않기 때문에 괜찮습니다.  
하지만 if문에 return이 없다면 조건에 맞는 if문이 실행되어도 다음 if문을 검사하기 때문에 성능이 저하됩니다.  
예시: MainServlet.java  

```java
    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/list.jsp";
    }
    // /list.do 조건이 true여도 아래 조건을 다 검사함  
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/write.jsp";
    }
    if ("/writeAction.do".equals(servletPath)) {
      viewPage = "/list.do";
    }
```

### if else 치워버리기
이전 피드백에 if else를 소개했지만 사실 if else를 사용하는 것은 가독성을 떨어트립니다.  
Map과 같은 다양한 방법을 사용해 if else를 치워버리는 것을 추천합니다.  

```java
    // 규모가 점점 늘어난다고 생각하면, 가독성이 매우 안좋아 질 것입니다.
    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/list.jsp";
    }
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/write.jsp";
    }
    if ("/writeAction.do".equals(servletPath)) {
      viewPage = "/list.do";
    }
```

### request를 매개변수로 던지지 말기
request를 매개변수로 던지는 것은 확장성을 떨어트립니다.  
request는 우리가 정의할 수 없습니다. 또한 안에 무었이 들었는지 판단하기가 어렵습니다.  
특정 인터페이스(WAS)에서 만들어주는 데이터는 가능하면 파라미터로 사용하지 않는걸 추천합니다.  
우리가 util을 만든다면 request등과 같이 특정 인터페이스에 종속되는게 아닌 어떤 클래스에서 호출해도 명확하게 이해하고 사용 가능하도록 해야합니다.  
예시: MainServlet.java  
```java
    // request에 무엇이 들어가는지 알 수 없고 제어하기 어려움
    // pojo형태(내가 만든 자바 DTO 또는 Map 등) 등으로 전달하는 방식으로 처리하는게 나음
    // 예시 1
    MainCommand mainCommand = MainCommandHelper.getCommand(request);
    mainCommand.execute(request, response);
    // 예시 2
    SearchManager searchManager = new SearchManager(request);
```

### 이전 페이지로 돌아간다고 referer를 사용하지 말기
referer는 사용자가 요청을 보낼때 접근 전 이전 페이지의 url을 가져옵니다.  
즉 List.jsp에서 write.jsp로 이동할 때 write.jsp 요청의 referer는 List.jsp가 됩니다.  
하지만 사용자가 직접 url을 입력하여 접근할 경우 referer는 null이 됩니다.  
referer는 목적 자체가 통계 데이터 수집용으로 설계되었습니다.(사용자가 어디로부터 오는가)  
따라서 referer를 사용하여 이전 페이지로 돌아간다는 기능을 구현하는 것은 좋지 않습니다.  

예시: MainServlet.java  

```java
    // 이전 페이지가 의도한 페이지가 아닐 수 있음 
    String referer = request.getHeader("referer");
    response.sendRedirect(referer);
```

### util 클래스를 만든다면 이름은 간결하게
util 클래스를 호출해 사용할 때 이름이 길어지면 가독성이 떨어지기 때문에 이름을 간결하게 만드는 것을 추천합니다.  
해당 Class가 어떤 동작을 하는지 설명하기 위해 이름을 짓는건 좋은 일이지만, 가독성 또한 고려해야합니다.  
기능을 명확하게 설명하면서도 간결한 이름을 고려해야합니다.  

예시: ValidationChecker.java

```java
    public class ValidationChecker {
      // 메소드 이름은 소문자로 시작해야함
      // boolean을 리턴하는 메소드는 is로 시작하는게 좋음
      // equals, null체크 등 다양한 방법으로 체크를 하지만 그걸 이름에 다 명시할 필요는 없음
      // 명확하고 간결하게 isEmpty로 바꿔도 됨
      public static boolean CheckStringIsNullOrEmpty(String targetString) {
        return targetString == null || "".equals(targetString) || targetString.isEmpty()
            || "null".equals(targetString);
      }
    }
```

### null 체크시 "null" 문자열은 별도의 의미를 가질 수 있음
null을 체크한다고 "null".equals를 사용하는 경우가 있습니다.  
이 경우 애초에 "null"이라는 문자열이 들어오지 않는 것이 보장되어야 합니다.  
"null"은 발생하기 어려운 경우입니다.  
만약 "null"이라는 문자열이 들어올 수 있다면 별도의 의도를 가지고 의도적으로 정의한 경우일 수 있습니다.  

예시: ValidationChecker.java  

```java
    public class ValidationChecker {
      // null체크시 "null" 문자열은 별도의 의미를 가질 수 있음
      public static boolean CheckStringIsNullOrEmpty(String targetString) {
        return targetString == null || "".equals(targetString) || targetString.isEmpty()
            || "null".equals(targetString);
      }
    }
```

### 이름을 지을 때 Wrapper와 Manager를 구분하기
Wrapper는 특정 클래스(또는 메소드)를 감싸는 클래스(또는 메소드)에 대해 사용할 수 있습니다.  
즉, 해당 클래스가 특정 클래스에 종속적일 때 사용합니다.  
Manager는 여러 기능을 관리하는 클래스(또는 메소드)에 대해 사용할 수 있습니다.  
즉, 해당 클래스가 특정 목적에 연관된 여러 기능을 가지고 있을 때 사용합니다.  
예시로 기본 데이터 형인 int, boolean 등은 Integer, Boolean 등의 Wrapper 클래스가 있습니다.  
본 프로젝트에서 search 데이터를 관리하기 위해 SearchManager 클래스를 만들었습니다.  

추가로 MyBatis에서 SqlSession와 직접적으로 관련된 메소드를 만든다고 가정할 경우 sqlSessionMapper 등의 네이밍을 고려할 수 있습니다. 

### MyBatis에서 sqlSessionFactory를 외부에 노출하지 않기
MyBatis에서 SqlSessionFactory를 외부에 노출하지 않는 것이 좋습니다.  
SqlSessionFactory는 자체적으로 SqlSession을 생성하고 관리합니다.  
SqlSessionFactory를 외부에 노출하면 SqlSession을 관리하는데 의도치 않은 조작을 할 수 있습니다.  
따라서 SqlSessionFactory를 반환하는 대신 SqlSession을 반환하는 걸 추천드립니다.  
예시: MyBatisConfig.java  

```java
    // sqlSessionFactory를 리턴하는 대신 SqlSession을 생성해서 리턴하는게 좋음
    // SqlSession에 대한 관리를 MyBatis가 자체적으로 해줌(이전에는 직접 Finally에서 close를 해줘야 했음)
  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }
```

### SqlSession을 사용한다면 각 처리에서 SqlSession 하나만 사용하기
SqlSession은 요청에 대한 트랜잭션을 관리할 수 있습니다.  
즉 SqlSession을 사용하는 동안에는 트랜잭션을 커밋하거나 롤백할 수 있습니다.  
게시글을 insert 하고 File insert에 실패한 경우 모든 처리를 롤백할 수 있습니다.  
하지만 SqlSession을 게시글, File 별로 나눠서 생성하면 트랜잭션을 관리할 수 없습니다.  

예시: 
```java
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    // session을 DAO별로 따로 생성하면 트랜잭션 관리를 할 수 없음
    try (
        SqlSession articleSqlSession = myBatisConfig.getSqlSessionFactory().openSession();
        SqlSession fileSqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      // 기타 처리...
    }
```
### rollback 등의 마지막 처리는 Finally에서 하기
try 내부에서 error가 발생한 경우 catch에서 rollback을 처리하는 경우가 있습니다.  
만일 catch가 여러개 존재할 경우 rollback 구문이 catch마다 반복되며 가독성에 좋지 않습니다.  
처리 중 error를 별도로 구분하며 처리 결과에 따라 Finally에서 rollback을 처리하는 것이 좋습니다.  

### resultMap 대신 resultType 사용하기
resultMap은 보통 우리의 DTO를 MyBatis Mapper에서 별도로 정의하고 쿼리 결과를 매핑할 때 사용합니다.  
resultType은 MyBatis Mapper에서 별도로 정의하지 않고 쿼리 결과를 매핑할 때 사용합니다.  
예시: CategoryMapper.xml  

```xml
    <mapper namespace="com.board.servlet.yoony.category.CategoryDAO">
      <parameterMap id="categoryParameterMap" type="com.board.servlet.yoony.category.CategoryDTO"></parameterMap>
      <resultMap id="categoryResultMap" type="com.board.servlet.yoony.category.CategoryDTO">
        <id property="categoryId" column="category_id"/>
        <result property="name" column="name"/>
      </resultMap>
      <!-- resultMap을 이용해 기존의 정의된 categoryResultMap을 매핑 -->
      <select id="selectCategoryList" resultMap="categoryResultMap">
        SELECT category_id, name
        FROM category
        ORDER BY category_id ASC
      </select>
      <!-- resultType을 사용해 DTO 자체를 매핑-->
      <select id="selectCategoryList" resultType="com.board.servlet.yoony.category.CategoryDTO">
        SELECT category_id, name
        FROM category
        ORDER BY category_id ASC
      </select>
    </mapper>
```

resultMap 방식은 컬럼과 DTO의 변수명을 자유롭게 매칭 가능합니다.  
주로 복잡한 쿼리문 결과를 매핑할 때 사용합니다.  
resultType 방식은 컬럼과 DTO의 변수명을 자동으로 매칭 하지만, 정해진 규격을 벗어나면 매칭이 어렵습니다.  
resultType을 추천드리는 이유는 런타임에서 Query와 매핑된 DTO를 검사해서 오류가 있는 경우 즉각적으로 알려주는 장점이 있습니다.  
즉각적인 오류 검사를 위해 resultType을 추천드립니다.  

### 쿼리문 예쁘게 정리하기
Select를 작성할때 반환 컬럼명이 많은 경우 3,4개를 기준으로 줄바꿈을 하면서 가독성을 고려하는 것을 추천드립니다.  
효율적인 쿼리 구성 말고도 단순 가독성 변경또한 신경써야합니다.  

### properties 파일 활용하기
설정 등의 정적 자원에 대해서 properties 파일을 활용하는 것이 좋습니다.  
배포 환경, 개발 환경 등 다양한 환경에서 정적 자원에 각각 차이가 있을 수 있습니다.  
서로 다른 환경에 대해서 properties 파일을 활용하여 관리할 수 있습니다.  
다음은 properties 파일을 활용 가능한 예시들 입니다.   
```java
    // MyBatis에서 설정 xml을 읽어올 때 properties를 활용 가능합니다.  
    // 추가로 xml 파일은 소문자로 시작해야합니다.    
    InputStream configuration = Resources.getResourceAsStream("Mybatis.xml");

    // 파일에 대한 저장 설정 시 properties를 활용해 각 서버 환경마다 다른 설정이 가능합니다.  
    String saveDirectory = "C:\\tempUploads";
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";
```

또한 properties를 활용한다면 국제화를 해보는 것도 좋습니다.  
다국어 지원을 위해 properties 파일을 locale에 따라 다르게 설정할 수 있습니다.  

### DTO에서 boolean에 경우 get을 붙이지 말기
DTO에서 boolean에 대해서 get을 붙이지 않는 것이 좋습니다.  
자동으로 Generated된 boolean getter는 is를 붙여줍니다.  
별도로 따로 boolean에 대한 메소드 정의시 is를 활용하는 것이 좋습니다.  

### 한줄짜리 주석도 javaDoc 주석을 활용하기
DTO에 변수명에 주석을 거는 경우 한줄짜리의 주석을 활용하는 경우가 있습니다.  
추후 문서화를 고려하는 경우 javaDoc 주석을 활용하는 것이 좋습니다.  
예시: PageDTO.java
```java
  // //으로 정의되는 주석대신 /** */을 활용하는 것이 좋습니다.
  // 페이지 번호
  private int pageNum;
  // 페이지당 보여줄 게시물 수
  private int pageSize;
```

### 코드 줄이기
공통적으로 가능한 코드를 줄이고 가독성을 확보하는 것이 좋습니다.  
리팩토링 중심 코드의 코드를 늘 고려하는게 좋습니다.  
Try catch만 줄여도 코드가 줄어듭니다.  
에러를 그냥 던져버리거나, 핸들러를 마련해서 핸들러가 처리를 해도 코드가 줄어듭니다.  