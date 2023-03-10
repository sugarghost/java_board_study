package com.board.servlet.yoony;

import com.board.servlet.yoony.article.ArticleDeleteActionCommand;
import com.board.servlet.yoony.article.ArticleListCommand;
import com.board.servlet.yoony.article.ArticleModifyActionCommand;
import com.board.servlet.yoony.article.ArticleModifyCommand;
import com.board.servlet.yoony.article.ArticleViewCommand;
import com.board.servlet.yoony.article.ArticleWriteActionCommand;
import com.board.servlet.yoony.article.ArticleWriteCommand;
import com.board.servlet.yoony.comment.CommentWriteActionCommand;
import com.board.servlet.yoony.file.FileDownloadActionCommand;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 커맨드 패턴에서 action에 대한 판별을 Servlet으로부터 분리하기 위해 만든 클래스 action에 대한 판별을 CommandHelper에서 하기 때문에
 * MainServlet에서는 CommandHelper를 통해 요청을 처리함 뭔가 큰 장점이 있는가에 대해서는 잘 모르겠음
 *
 * @author yoony
 * @version 1.0
 * @see MainServlet
 * @see MainCommand
 * @since 2023. 02. 14.
 */
public class MainCommandHelper {

  private static final Map<String, MainCommand> commands = new HashMap<>();

  // command String과 Command를 매핑하기 위한 commands 초기화 블록
  static {
    commands.put("articleList", new ArticleListCommand());
    commands.put("articleWrite", new ArticleWriteCommand());
    commands.put("articleWriteAction", new ArticleWriteActionCommand());
    commands.put("articleView", new ArticleViewCommand());
    commands.put("commentWriteAction", new CommentWriteActionCommand());
    commands.put("fileDownloadAction", new FileDownloadActionCommand());
    commands.put("articleModify", new ArticleModifyCommand());
    commands.put("articleModifyAction", new ArticleModifyActionCommand());
    commands.put("articleDeleteAction", new ArticleDeleteActionCommand());
  }

  /**
   * request 요청을 확인해 command 파라미터를 통해 요청을 처리할 Command를 가져옴 만약 command 파라미터가 없거나, command에 해당하는
   * Command가 없으면 UnknownCommand를 반환함
   *
   * @param request HttpServletRequest command 파라미터를 통해 요청을 처리할 Command를 가져오기 위해 사용
   * @return command 파라미터에 해당하는 Command 없으면 UnknownCommand
   * @aothor yoony
   * @version 1.0
   * @see UnknownMainCommand
   * @see MainCommand
   * @since 2023. 02. 14.
   */
  public static MainCommand getCommand(HttpServletRequest request) {
    // 초기에 클라이언트가 전송하는 Parameter를 사용했으니, 엔드포인트와 command를 같이 고려해야해서 복잡해짐
    // 일단 servlet에서 설정해주는 attribute를 사용하도록 변경
    String commandType = (String) request.getAttribute("command");

    MainCommand mainCommand = commands.get(commandType);
    if (mainCommand == null) {
      mainCommand = new UnknownMainCommand();
    }
    return mainCommand;
  }

}
