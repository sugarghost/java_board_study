# Java_board_JSP

간단한 게시판 프로젝트를 JSP에 model1 방식으로 구현한 프로젝트 입니다.  
Java와 JSP만을 학습하기 위해 Servlet과 Spring을 사용하지 않았습니다.   
1주일간의 개발 후 피드백을 받고, 하루간 리팩토링을 진행해 피드백을 반영하고, 일부 피드백은 다음 프로젝트인 Java_board_Servlet에 반영하였습니다.

# 피드백 목차
* [피드백](#피드백)
    + [meta와 같은 공통 요소의 처리](#meta와-같은-공통-요소의-처리)
    + [Util 활용하기](#util-활용하기)
    + [equals 사용시 비교 Text를 기준으로 하기](#equals-사용시-비교-text를-기준으로-하기)
    + [Session 사용 피하기](#session-사용-피하기)
    + [System.out.println() 사용 피하기](#systemoutprintln-사용-피하기)
    + [파일 저장시 UUID 활용하기](#파일-저장시-uuid-활용하기)
    + [Java에서 javascript 호출하지 말기](#java에서-javascript-호출하지-말기)
    + [필드별, 서비스별 유효성 검증을 구분하기](#필드별-서비스별-유효성-검증을-구분하기)
    + [Vector 대신 ArrayList 사용하기](#vector-대신-arraylist-사용하기)
    + [return을 Finally에 사용하지 말기](#return을-finally에-사용하지-말기)
    + [Database를 설정할 땐 Class loader를 주의하기](#database를-설정할-땐-class-loader를-주의하기)
    + [Query 잘 짜기](#query-잘-짜기)
    + [공통적으로 쓰는 설정은 properties 파일로 관리하기](#공통적으로-쓰는-설정은-properties-파일로-관리하기)
    + [사용자에게 서버와 관련된 데이터를 보여주지 말기](#사용자에게-서버와-관련된-데이터를-보여주지-말기)

## 피드백

### meta와 같은 공통 요소의 처리

encoding, meta와 같은 공통 요소들은 따로 상위에 빼서 처리하는 것이 좋습니다.   
예시: List.jsp

```java
    Map<String, Object> param=new HashMap<String, Object>();
    // 인코딩은 따로 상위에서 처리하는걸 추천
    request.setCharacterEncoding("utf-8");
    String searchWord=request.getParameter("searchWord");
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

### System.out.println() 사용 피하기

가능한 Log 라이브러리를 사용하는 것이 좋습니다.   
또한 JSP에서 Sytem.out.println()을 사용하는건 좋지 않습니다.   
예시:

```java
    // System.out.println() 사용 피하기
    System.out.println("searchWord : "+searchWord);
```

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

### Java에서 javascript 호출하지 말기

Java에서 javascript를 호출하는 것은 좋지 않습니다.  
예시: WriteAction.jsp

```java
    if(fileInsertResult==0){
        // 자바쪽에서 스크립트 호출 방식은 안좋은 방식
        out.println("<script>alert('"+fileName+" 파일 등록 실패!')</script>");
    }
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

### Vector 대신 ArrayList 사용하기

Vector는 동기화를 지원하기 위해 만들어진 클래스입니다.  
ArrayList보다 크고 무겁기 때문에 Tread Safe를 고려해 사용하는게 좋습니다.

```java
    // Vector 대신 ArrayList 사용하기
    List<CategoryDTO> categoryList=new Vector<CategoryDTO>();
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

### 공통적으로 쓰는 설정은 properties 파일로 관리하기

공통적으로 쓰는 설정은 properties 파일로 관리하는 것이 좋습니다.    
때로는 환경마다 설정이 다를 수 있기 때문에, properties 파일로 관리하는 것이 좋습니다.  
예시: Download.jsp

```java
    // 디렉토리등 공통적으로 쓰는 경우는 프로퍼티로 따로 빼는게 좋음
    String saveDirectory="C:\\tempUploads";
```

### 사용자에게 서버와 관련된 데이터를 보여주지 말기

파일을 DB에 저장해 관리할 때, 사용자에게 서버와 관련된 데이터를 보여주지 말아야 합니다.  
예시: Download.jsp

```java
    // fileSaveName과 같이 서버에 저장된 파일명을 사용자가 볼 수 있도록 하는건 안좋음
    // fileId를 기준으로 검색해 사용하는 것을 추천
    String fileSaveName=request.getParameter("fileSaveName");
    String fileOriginName=request.getParameter("fileOriginName");
```