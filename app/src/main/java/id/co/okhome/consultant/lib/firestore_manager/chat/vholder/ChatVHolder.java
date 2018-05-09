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
 * Created by jo on 2018-05-06.
 */

public class ChatVHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.itemChat_vgHeaderDate)               public ViewGroup vgHeaderDate;
    @BindView(R.id.itemChat_tvHeaderDate)               public TextView tvHeaderDate;
    @BindView(R.id.itemChat_vBottomPadding)             public View vBottomPadding;

    @BindView(R.id.itemChat_vgChatMine)                 public ViewGroup vgChatMine;
    @BindView(R.id.itemChat_vgChatOpposite)             public ViewGroup vgChatOpposite;


    @BindView(R.id.itemChatMine_ivPhoto)                public ImageView ivMyChatPhoto;
    @BindView(R.id.itemChatMine_tvChat)                 public TextView tvMyChat;
    @BindView(R.id.itemChatMine_tvTime)                 public TextView tvMyTime;
    @BindView(R.id.itemChatMine_vgContents)             public ViewGroup vgMyContents;
    @BindView(R.id.itemChatMine_vgPhotoContent)         public ViewGroup vgMyPhotoContent;
    @BindView(R.id.itemChatMine_vTopPadding)            public View vMyTopPadding;
    @BindView(R.id.itemChatMine_vgTextContent)          public ViewGroup vgMyTextContent;

    @BindView(R.id.itemChatOpposite_ivPhoto)            public ImageView ivOppositeChatPhoto;
    @BindView(R.id.itemChatOpposite_tvChat)             public TextView tvOppositeChat;
    @BindView(R.id.itemChatOpposite_tvTime)             public TextView tvOppositeTime;
    @BindView(R.id.itemChatOpposite_vgContents)         public ViewGroup vgOppositeContents;
    @BindView(R.id.itemChatOpposite_vgPhotoContent)     public ViewGroup vgOppositePhotoContent;
    @BindView(R.id.itemChatOpposite_vTopPadding)        public View vOppositeTopPadding;
    @BindView(R.id.itemChatOpposite_vgTextContent)      public ViewGroup vgOppositeTextContent;


    public ChatVHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}