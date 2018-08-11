package id.co.okhome.consultant.lib.firestore_manager.chat.vholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;

/**
 * Created by jo on 2018-05-09.
 */

public class ChatRoomVHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.itemChatRoom_vContent)           public View vContent;
    @BindView(R.id.itemChatRoom_tvMessage)          public TextView tvMessage;
    @BindView(R.id.itemChatRoom_tvName)             public TextView tvName;
    @BindView(R.id.itemChatRoom_tvDateTime)         public TextView tvDateTime;
    @BindView(R.id.itemChatRoom_ivPhoto)            public ImageView ivPhoto;
    @BindView(R.id.itemChatRoom_tvCleaningDate)     public TextView tvCleaningDate;
    @BindView(R.id.itemChatRoom_tvThumbChar)        public TextView tvThumbChar;
    @BindView(R.id.itemChatRoom_vgThumb)            public ViewGroup vgThumb;
    @BindView(R.id.itemChatRoom_vgThumbPhoto)       public ViewGroup vgThumbPhoto;
    @BindView(R.id.itemChatRoom_vRedDot)            public View vRedDot;

    public ChatRoomVHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
