package ro.pub.cs.systems.eim.practicaltest01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

public class PracticalTest01MainActivity extends AppCompatActivity {
    EditText nextTerm;
    Button addButton;
    Button computeButton;
    EditText allTerms;
    private int savedSum = 0;
    private String lastTerms = "";

    static class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
        }
    }

    private final MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private final IntentFilter intentFilter = new IntentFilter();

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == addButton) {
                String nextTermText = nextTerm.getText().toString();
                String allTermsText = allTerms.getText().toString();
                if (nextTermText.isEmpty())
                    return;
                if (allTermsText.isEmpty()) {
                    allTerms.setText(nextTermText);
                } else {
                    allTerms.setText(allTermsText + " + " + nextTermText);
                }
            } else if (view == computeButton) {
                String allTermsText = allTerms.getText().toString();
                if (allTermsText.isEmpty())
                    return;
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                intent.putExtra(Constants.ALL_TERMS, allTermsText);
                startActivityForResult(intent, Constants.COMPUTE_TERMS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.COMPUTE_TERMS && resultCode == RESULT_OK) {
            int intExtra = data.getIntExtra(Constants.SUM, 0);
            savedSum = intExtra;
            lastTerms = allTerms.getText().toString();
            Toast.makeText(this, "The sum is " + intExtra, Toast.LENGTH_SHORT).show();
            if (intExtra > 10) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.SUM, intExtra);
                getApplicationContext().startService(intent);
            }
        } else {
            Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.SAVED_SUM, savedSum);
        if (allTerms.getText().toString().equals(lastTerms))
            outState.putString(Constants.LAST_TERMS, lastTerms);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(Constants.SAVED_SUM) && savedInstanceState.containsKey(Constants.LAST_TERMS)) {
            allTerms.setText(String.valueOf(savedInstanceState.getInt(Constants.SAVED_SUM)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        nextTerm = (EditText)findViewById(R.id.nextTerm);
        addButton = (Button)findViewById(R.id.addButton);
        computeButton = (Button)findViewById(R.id.computeButton);
        allTerms = (EditText)findViewById(R.id.allTerms);

        Arrays.asList(addButton, computeButton).forEach(button -> button.setOnClickListener(new ButtonClickListener()));
        intentFilter.addAction(Constants.SUM_BROADCAST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplicationContext().stopService(new Intent(getApplicationContext(), PracticalTest01Service.class));
    }
}