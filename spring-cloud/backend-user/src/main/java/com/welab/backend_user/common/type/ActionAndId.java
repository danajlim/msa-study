package com.welab.backend_user.common.type;

import lombok.Getter;
import lombok.Setter;

//kafka 메세지를 자동으로 발행하기 위한 이벤트 정보 담는 용도의 DTO
//이 사람이 어떤 행동을 했는지(action) + 누구(id)에 대해 했는지 담아서 넘겨줌
@Getter @Setter
public class ActionAndId {

    private String action;
    private Long id;

    //간단하게 객체 생성할 수 있는 팩토리 메서드 -> ActionAndId.of("Create", 3L) 이렇게 호출
    public static ActionAndId of(String action, Long id) {
        ActionAndId actionAndId = new ActionAndId();

        actionAndId.action = action;
        actionAndId.id = id;

        return actionAndId;
    }

}
