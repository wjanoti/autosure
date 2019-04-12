package no.uio.ifi.autosure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimMessage;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<ClaimMessage> mMessageList;

    MessageListAdapter(List<ClaimMessage> messageList) {
        mMessageList = messageList;
    }

    void setMessageList(List<ClaimMessage> claimMessageList) {
        this.mMessageList = claimMessageList;
    }

    @Override
    public int getItemCount() {
        if (mMessageList != null) {
            return mMessageList.size();
        }
        return 0;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ClaimMessage message = mMessageList.get(position);

        if (message.getSender().equals("AutoInSure")) {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ClaimMessage message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(ClaimMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getDate());
        }
    }

    private class ReceivedMessageHolder extends SentMessageHolder {
        TextView nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_message_name);
        }

        void bind(ClaimMessage message) {
            super.bind(message);
            nameText.setText(message.getSender());
        }
    }

}