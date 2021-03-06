package id.co.okhome.consultant.lib.firestore_manager.chat.model;

import com.google.firebase.firestore.ServerTimestamp;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by jo on 2018-05-06.
 */

public class ChatItem {
    public String localUniqKey;
    private String id;
    public String writerId, writerType;
    public String msgType, msg;

    private DateTime dateTime;

    @ServerTimestamp
    private Date timestamp = null;

    private boolean fromLocal = false;

    public ChatItem(String writerId, String writerType, String msgType, String msg) {
        this.localUniqKey = System.currentTimeMillis()+"";
        this.writerId = writerId;
        this.writerType = writerType;
        this.msgType = msgType;
        this.msg = msg;
        this.fromLocal = true;
    }

    public Date getTimestamp() {
        if(timestamp == null){
            return new Timestamp(System.currentTimeMillis());
        }else{
            return timestamp;
        }

    }

    public void setId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public ChatItem() {
    }

    public boolean fromLocal() {
        return fromLocal;
    }


    public DateTime dateTime() {
        if(dateTime == null){
            dateTime = new DateTime(getTimestamp());
        }
        return dateTime;
    }
}
