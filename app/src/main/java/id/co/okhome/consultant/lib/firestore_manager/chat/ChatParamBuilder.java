package id.co.okhome.consultant.lib.firestore_manager.chat;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2018-05-09.
 */

public class ChatParamBuilder {
    //채팅 메세지 만들기
    public static Map<String, Object> makeChatMessage(String writerType, String writerId, String msgType, String msg, String[] targetMembers){
        Map<String, Object> map = new HashMap<>();
        map.put("writerType", writerType);
        map.put("writerId", writerId);
        map.put("msgType", msgType);
        map.put("msg", msg);
        map.put("timestamp", FieldValue.serverTimestamp());

        Map<String, Object> memberMap = new HashMap<>();
        for(String member : targetMembers){
            memberMap.put(member, FieldValue.serverTimestamp());
        }
        map.put("members", memberMap);
        return map;
    }

    //가장 마지막에 작성한 채팅 내용
    public static Map<String, Object> makeLastMessageInChatRoom(String writerType, String writerId, String msgType, String msg){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapLastMessageTargetId = new HashMap<>();
        Map<String, Object> mapLastMessage = new HashMap<>();
        map.put("lastMessages", mapLastMessageTargetId);
        mapLastMessageTargetId.put("ALL", mapLastMessage);

        mapLastMessage.put("writerType", writerType);
        mapLastMessage.put("writerId", writerId);
        mapLastMessage.put("msgType", msgType);
        mapLastMessage.put("msg", msg);
        mapLastMessage.put("timestamp", FieldValue.serverTimestamp());
        return map;
    }
}
