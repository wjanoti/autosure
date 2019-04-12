package no.uio.ifi.autosure;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import no.uio.ifi.autosure.tasks.NewClaimTask;
import no.uio.ifi.autosure.tasks.PlatesTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class NewClaimActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText inputOcurrenceDate;
    EditText inputClaimTitle;
    EditText inputClaimDescription;
    Spinner dropdownPlates;
    int sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_claim);
        setTitle("New Claim");

        // fetch plates list
        Intent intent = this.getIntent();
        sessionId = intent.getExtras().getInt("sessionId");

        fetchPlates(sessionId);

        inputClaimTitle = findViewById(R.id.inputClaimTitle);
        inputClaimDescription = findViewById(R.id.inputClaimDescription);
        inputOcurrenceDate = findViewById(R.id.inputOcurrenceDate);
        dropdownPlates = findViewById(R.id.dropdownPlates);
    }

    // set up calendar
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String myFormat = "dd-MM-yy hh:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            inputOcurrenceDate.setText(sdf.format(myCalendar.getTime()));
            inputOcurrenceDate.setError(null);
        }
    };

    // on click handler of the date field
    public void openDatePicker(View view) {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void fetchPlates(int sessionId) {
        TaskListener fetchPlatesCallback = new TaskListener<List<String>>() {
            @Override
            public void onFinished(List<String> result) {
                String[] resultArr;
                if (result != null) {
                    resultArr = new String[result.size() + 1];
                    result.add(0, "Choose a plate...");
                    resultArr = result.toArray(resultArr);
                } else {
                    resultArr = new String[1];
                    resultArr[0] = "Choose a plate...";
                }

                ArrayAdapter<String> plateAdapter = new ArrayAdapter<>(
                    NewClaimActivity.this,
                    R.layout.support_simple_spinner_dropdown_item,
                    resultArr
                );

                dropdownPlates.setAdapter(plateAdapter);
                dropdownPlates.setSelection(0);
            }
        };
        new PlatesTask(fetchPlatesCallback, sessionId).execute();
    }

    public void newClaim(View view) {
        // validate fields
        String claimTitle = inputClaimTitle.getText().toString();
        String occurrenceDate = inputOcurrenceDate.getText().toString();
        String plate = null;
        if (dropdownPlates.getSelectedItem() != null) {
            plate = dropdownPlates.getSelectedItem().toString();
        };
        String claimDescription = inputClaimDescription.getText().toString();
        if (claimTitle.isEmpty()) {
            inputClaimTitle.setError("Claim title is required");
            return;
        } else if (occurrenceDate.isEmpty()) {
            inputOcurrenceDate.setError("Occurrence date is required");
            return;
        } else if (plate == null || plate.equals("Choose a plate...")) {
            ((TextView) dropdownPlates.getSelectedView()).setError("Plate is required");
            return;
        } else if (claimDescription.isEmpty()) {
            inputClaimDescription.setError("Description is required");
            return;
        }

        TaskListener newClaimCallback = new TaskListener<Boolean>() {
            @Override
            public void onFinished(Boolean successful) {
                if (successful) {
                    Toast.makeText(NewClaimActivity.this, "Claim submitted successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(NewClaimActivity.this, "Failed creating a new claim", Toast.LENGTH_LONG).show();
                }
            }
        };

        new NewClaimTask(newClaimCallback, sessionId, claimTitle, occurrenceDate,
                plate, claimDescription).execute();
    }
}

