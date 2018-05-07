package id.co.okhome.consultant.lib.chat;

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

    @BindView(R.id.itemChat_vgHeaderDate)               ViewGroup vgHeaderDate;
    @BindView(R.id.itemChat_tvHeaderDate)               TextView tvHeaderDate;
    @BindView(R.id.itemChat_vBottomPadding)             View vBottomPadding;

    @BindView(R.id.itemChat_vgChatMine)                 ViewGroup vgChatMine;
    @BindView(R.id.itemChat_vgChatOpposite)             ViewGroup vgChatOpposite;


    @BindView(R.id.itemChatMine_ivPhoto)                ImageView ivMyChatPhoto;
    @BindView(R.id.itemChatMine_tvChat)                 TextView tvMyChat;
    @BindView(R.id.itemChatMine_tvTime)                 TextView tvMyTime;
    @BindView(R.id.itemChatMine_vgContents)             ViewGroup vgMyContents;
    @BindView(R.id.itemChatMine_vgPhotoContent)         ViewGroup vgMyPhotoContent;
    @BindView(R.id.itemChatMine_vTopPadding)            View vMyTopPadding;
    @BindView(R.id.itemChatMine_vgTextContent)          ViewGroup vgMyTextContent;

    @BindView(R.id.itemChatOpposite_ivPhoto)            ImageView ivOppositeChatPhoto;
    @BindView(R.id.itemChatOpposite_tvChat)             TextView tvOppositeChat;
    @BindView(R.id.itemChatOpposite_tvTime)             TextView tvOppositeTime;
    @BindView(R.id.itemChatOpposite_vgContents)         ViewGroup vgOppositeContents;
    @BindView(R.id.itemChatOpposite_vgPhotoContent)     ViewGroup vgOppositePhotoContent;
    @BindView(R.id.itemChatOpposite_vTopPadding)        View vOppositeTopPadding;
    @BindView(R.id.itemChatOpposite_vgTextContent)      ViewGroup vgOppositeTextContent;


    public ChatVHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}