package ro.pub.cs.systems.eim.practicaltest01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            String allTerms = intent.getStringExtra(Constants.ALL_TERMS);
            if (allTerms != null) {
                String[] terms = allTerms.split(" \\+ ");
                int sum = 0;
                for (String term : terms) {
                    sum += Integer.parseInt(term);
                }

                setResult(RESULT_OK, new Intent().putExtra(Constants.SUM, sum));
                finish();
            }
        }

        setResult(RESULT_CANCELED, new Intent());
        finish();
    }
}