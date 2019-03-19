package no.uio.ifi.autosure;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import no.uio.ifi.autosure.tasks.PlatesTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class NewClaimActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText inputOcurrenceDate;
    Spinner dropdownPlates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_claim);
        Intent intent = this.getIntent();
        int sessionId = intent.getExtras().getInt("sessionId");
        inputOcurrenceDate = findViewById(R.id.inputOcurrenceDate);
        dropdownPlates = findViewById(R.id.dropdownPlates);
        fetchPlates(sessionId);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String myFormat = "dd-MM-yy hh:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            inputOcurrenceDate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    public void openDatePicker(View view) {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void fetchPlates(int sessionId) {
        TaskListener fetchPlatesCallback = new TaskListener<List<String>>() {
            @Override
            public void onFinished(List<String> result) {
                String[] resultArr = new String[result.size() + 1];
                result.add(0, "Choose a plate...");
                resultArr = result.toArray(resultArr);

                ArrayAdapter<String> plateAdapter = new ArrayAdapter<>(
                    NewClaimActivity.this,
                    R.layout.support_simple_spinner_dropdown_item,
                    resultArr
                );

                dropdownPlates.setSelection(0);
                dropdownPlates.setAdapter(plateAdapter);
            }
        };
        new PlatesTask(fetchPlatesCallback, sessionId).execute();
    }
}
