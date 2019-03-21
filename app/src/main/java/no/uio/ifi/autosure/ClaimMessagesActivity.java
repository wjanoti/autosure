package no.uio.ifi.autosure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimMessage;
import no.uio.ifi.autosure.tasks.ListClaimMessagesTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimMessagesActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<ClaimMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_messages);
        setTitle("Claim Messages");
        //TODO pass proper params
        fetchClaimMessages(2, 1);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    private void fetchClaimMessages(int sessionId, int claimId) {
        TaskListener fetchClaimMessagesCallback = new TaskListener<List<ClaimMessage>>() {
            @Override
            public void onFinished(List<ClaimMessage> result) {
                if (result.size() > 0) {
                    messageList = result;
                } else {
                    Toast.makeText(ClaimMessagesActivity.this, "Error fetching claims", Toast.LENGTH_SHORT).show();
                }
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
