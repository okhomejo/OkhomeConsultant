package id.co.okhome.consultant.lib.firestore_manager.chat;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2018-05-09.
 */

public class ChatParamBuilder {
    //채팅 메세지 만들기
    public static Map<String, Object> makeChatMessage(String writerType, String writerId, String msgType, String msg){
        Map<String, Object> map = new HashMap<>();
        map.put("writerType", writerType);
        map.put("writerId", writerId);
        map.put("msgType", msgType);
        map.put("msg", msg);
        map.put("timestamp", FieldValue.serverTimestamp());
        return map;
    }

    //가장 마지막에 작성한 채팅 내용
    public static Map<String, Object> makeLastMessageInChatRoom(String writerType, String writerId, String msgType, String msg){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapLastMessage = new HashMap<>();
        map.put("lastMessage", mapLastMessage);

        mapLastMessage.put("writerType", writerType);
        mapLastMessage.put("writerId", writerId);
        mapLastMessage.put("msgType", msgType);
        mapLastMessage.put("msg", msg);
        mapLastMessage.put("timestamp", FieldValue.serverTimestamp());
        return map;
    }
}
