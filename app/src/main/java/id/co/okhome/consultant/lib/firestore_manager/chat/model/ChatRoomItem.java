package id.co.okhome.consultant.lib.firestore_manager.chat.model;

import java.util.Map;

/**
 * Created by jo on 2018-05-09.
 */

public class ChatRoomItem {
    public String id;
    public String lastWriterName;
    public String lastWriterPhotoUrl;
    public String lastWriterId;

    public ChatItem lastMessage;
    public Map<String, String> members;
    public Map<String, String> extra;

    public ChatRoomItem() {
    }
}
