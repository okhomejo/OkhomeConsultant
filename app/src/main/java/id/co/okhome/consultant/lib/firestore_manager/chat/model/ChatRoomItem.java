package id.co.okhome.consultant.lib.firestore_manager.chat.model;

import java.util.Map;

/**
 * Created by jo on 2018-05-09.
 */

public class ChatRoomItem {
    public String id;

    public String roomId;
    public String roomType;
    public String roomTitle;
    public String roomImgUrl;

    public Map<String, ChatItem> lastMessages; //아이디 + chatitem
    public Map<String, String> members;
    public Map<String, String> extra;
    public CleaningInfoInChatModel cleaningInfo;

    public ChatRoomItem() {
    }

    /**젤빠른 쳇 썸네일 불러옴*/
    public ChatItem latestChatThumbItem(String myId){
        ChatItem chatItemAll = lastMessages.get("ALL");
        ChatItem chatItemForMine = lastMessages.get(myId);

        if(chatItemForMine == null){
            return chatItemAll;
        }else{
            if(chatItemAll.getTimestamp().getTime() > chatItemForMine.getTimestamp().getTime()){
                return chatItemAll;
            }else{
                return chatItemForMine;
            }
        }


    }
}
