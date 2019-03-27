package no.uio.ifi.autosure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimMessage;
import no.uio.ifi.autosure.tasks.ListClaimMessagesTask;
import no.uio.ifi.autosure.tasks.SendClaimMessageTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimMessagesActivity extends AppCompatActivity {

    private MessageListAdapter mMessageAdapter;
    private List<ClaimMessage> messageList;
    private int claimId;
    private int sessionId;
    private TextView txtClaimMessage;
    private RecyclerView mMessageRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_messages);
        setTitle("Claim Messages");

        Intent intent = this.getIntent();
        sessionId = intent.getExtras().getInt("sessionId");
        claimId = intent.getExtras().getInt("claimId");
        fetchClaimMessages(sessionId, claimId);

        txtClaimMessage = findViewById(R.id.txtClaimMessage);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    private void fetchClaimMessages(int sessionId, int claimId) {
        TaskListener fetchClaimMessagesCallback = new TaskListener<List<ClaimMessage>>() {
            @Override
            public void onFinished(List<ClaimMessage> result) {
                if (result != null) {
                    messageList = result;
                    mMessageAdapter.setMessageList(messageList);
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount() + 1);
                }
            }
        };
        new ListClaimMessagesTask(fetchClaimMessagesCallback, sessionId, claimId).execute();
    }

    public void sendMessage(View view) {
        String message = txtClaimMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(ClaimMessagesActivity.this, "Can't send an empty message", Toast.LENGTH_LONG).show();
            return;
        }

        TaskListener sendClaimMessageCallback = new TaskListener<Boolean>() {
            @Override
            public void onFinished(Boolean sendSuccessful) {
                if (sendSuccessful) {
                    fetchClaimMessages(sessionId, claimId);
                    txtClaimMessage.setText(null);
                    mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount() + 1);
                } else {
                    Toast.makeText(ClaimMessagesActivity.this, "Could not send message", Toast.LENGTH_LONG).show();
                }
            }
        };
        new SendClaimMessageTask(sendClaimMessageCallback, sessionId, claimId, message).execute();
    }
}
