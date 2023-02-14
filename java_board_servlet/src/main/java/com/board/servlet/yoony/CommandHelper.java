package com.board.servlet.yoony;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 커맨드 패턴에서 action에 대한 판별을 Servlet으로부터 분리하기 위해 만든 클래스
 * action에 대한 판별을 CommandHelper에서 하기 때문에 MainServlet에서는 CommandHelper를 통해 요청을 처리함
 * 뭔가 큰 장점이 있는가에 대해서는 잘 모르겠음
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 * @see MainServlet
 * @see Command
 */
public class CommandHelper {
  private static final Map<String, Command> commands = new HashMap<>();

  // command String과 Command를 매핑하기 위한 commands 초기화 블록
  static {
    // 이후 커맨드가 추가되면 등록
  }

  /**
   * request 요청을 확인해 command 파라미터를 통해 요청을 처리할 Command를 가져옴
   * 만약 command 파라미터가 없거나, command에 해당하는 Command가 없으면 UnknownCommand를 반환함
   * @aothor yoony
   * @param request HttpServletRequest
   *                command 파라미터를 통해 요청을 처리할 Command를 가져오기 위해 사용
   * @see UnknownCommand
   * @see Command
   * @return command 파라미터에 해당하는 Command
   *        없으면 UnknownCommand
   */
  public static Command getCommand(HttpServletRequest request) {
    String commandType = request.getParameter("command");
    Command command = commands.get(commandType);
    if (command == null) {
      command = new UnknownCommand();
    }
    return command;
  }

}
