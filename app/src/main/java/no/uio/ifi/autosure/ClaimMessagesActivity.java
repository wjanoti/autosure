package no.uio.ifi.autosure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimMessage;
import no.uio.ifi.autosure.tasks.ListClaimMessagesTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimMessagesActivity extends AppCompatActivity {

    private MessageListAdapter mMessageAdapter;
    private List<ClaimMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_messages);
        setTitle("Claim Messages");

        Intent intent = this.getIntent();
        int sessionId = intent.getExtras().getInt("sessionId");
        int claimId = intent.getExtras().getInt("claimId");
        fetchClaimMessages(sessionId, claimId);

        RecyclerView mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    private void fetchClaimMessages(int sessionId, int claimId) {
        TaskListener fetchClaimMessagesCallback = new TaskListener<List<ClaimMessage>>() {
            @Override
            public void onFinished(List<ClaimMessage> result) {
                messageList = result;
                mMessageAdapter.setMessageList(messageList);
                mMessageAdapter.notifyDataSetChanged();
            }
        };
        new ListClaimMessagesTask(fetchClaimMessagesCallback, sessionId, claimId).execute();
    }

    public void sendMessage(View view) {
        //TODO
    }
}
