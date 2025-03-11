/* I Ahmed Nakhuda, 000878456 certify that this material is my original work.
   No other person's work has been used without due acknowledgement. */

package ca.mohawk.nakhuda;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class used to convert Canadian dollars to the
 * selected currency of the user's choice.
 */
public class MainActivity extends AppCompatActivity {

    /** Used to start MainActivity2 and receive a result when the activity finishes. */
    private ActivityResultLauncher<Intent> Launch4Result;

    /** Button to calculate conversion rate */
    private Button conversionRateButton;

    /** Show the total amount after conversion rate */
    private TextView totalTextView;

    /** Enter Canadian dollar amount to convert */
    private EditText amountEditText;

    /** Change the flag based on the selected country */
    private ImageView flagImage;

    /** Keep track of country selected, default is USA */
    private String flag = "USA";

    /** Default conversion rate for USA */
    private double conversionRate = 0.7;

    /** SharedPreferences file name */
    private static final String SP_File = "saved_configuration";

    /** Store the conversion rate */
    private static final String Conversion_State = "conversion_state";

    /** Store the country */
    private static final String Country_State = "country_state";

    /** Used to debug */
    private static final String tag = "==MainActivity==";




    /**
     * Initializes the UI components, loads saved preferences, and set registerForActivityResult
     * @param savedInstanceState holds data about the previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conversionRateButton = findViewById(R.id.conversionRateButton);
        totalTextView = findViewById(R.id.totalTextView);
        amountEditText = findViewById(R.id.amountEditText);
        flagImage = findViewById(R.id.flag);

        conversionRateButton.setText("@ " + conversionRate);

        loadPreferences();

        Launch4Result = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult
        );
    }


    /**
     * Loads the saved preferences for the conversion rate and flag and updates the UI
     */
    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_File, Context.MODE_PRIVATE);

        conversionRate = Double.parseDouble(sharedPreferences.getString(Conversion_State, "0.7"));
        flag = sharedPreferences.getString(Country_State, "USA");

        conversionRateButton.setText("@ " + conversionRate);
        updateFlagImage();

        Log.d(tag, "Loaded Preferences: Conversion Rate = " + conversionRate + ", Flag = " + flag);
    }


    /**
     * Saves the conversion rate and flag to SharedPreferences.
     */
    private void savePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_File, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Conversion_State, String.valueOf(conversionRate));
        editor.putString(Country_State, flag);
        editor.apply();
        Log.d(tag, "Saved Preferences: Conversion Rate = " + conversionRate + ", Country = " + flag);
    }


    /**
     * Handles the result from MainActivity2.
     * Extracts the conversion rate and flag data, and updates the UI.
     * Saves the preference.
     * @param result The result returned from MainActivity2
     */
    private void handleResult(ActivityResult result) {
        int resultCode = result.getResultCode();

        if (resultCode == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                String newConversionRate = data.getStringExtra("CONVERSION_RATE");
                String newFlag = data.getStringExtra("FLAG");
                Log.d(tag, "RESULT_OK. Conversion Rate Received: " + newConversionRate);

                try {
                    conversionRate = Double.parseDouble(newConversionRate);
                    flag = newFlag;
                    conversionRateButton.setText("@" + conversionRate);
                    updateFlagImage();
                    savePreferences();
                } catch (NumberFormatException e) {
                    Log.e(tag, "Invalid conversion rate");
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.d(tag, "RESULT_CANCELED.");
        } else {
            Log.d(tag, "Unexpected Error XXX, resultCode = " + resultCode);
        }
    }


    /**
     * Updates the flag image based on the selected country.
     */
    private void updateFlagImage() {
        switch (flag) {
            case "USA":
                flagImage.setImageResource(R.drawable.usa);
                break;
            case "Japan":
                flagImage.setImageResource(R.drawable.japan);
                break;
            case "Cuba":
                flagImage.setImageResource(R.drawable.cuba);
                break;
            case "Kuwait":
                flagImage.setImageResource(R.drawable.kuwait);
                break;
            case "Saudi Arabia":
                flagImage.setImageResource(R.drawable.saudi);
                break;
        }
    }


    /**
     * Switch to MainActivity2 and send updated values.
     * @param view (unused)
     */
    public void nextActivity(View view) {
        Intent switchToActivity2 = new Intent(MainActivity.this, MainActivity2.class);
        switchToActivity2.putExtra("CONVERSION_RATE", String.valueOf(conversionRate));
        switchToActivity2.putExtra("FLAG", flag);
        Launch4Result.launch(switchToActivity2);
    }


    /**
     * OnClick method to update the totalTextView based on the entered amount
     * multiplied by the conversion rate.
     * @param view (unused)
     */
    public void updateTotal(View view) {
        try {
            String amountText = amountEditText.getText().toString();
            double amount = Double.parseDouble(amountText);

            // Prevent negative values
            if (amount < 0) {
                amount = 1;  // Set amount to 1
                amountEditText.setText("1");
            }

            double total = amount * conversionRate;
            totalTextView.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            amountEditText.setText("1");
            totalTextView.setText(String.format("%.2f", 1 * conversionRate));
        }
    }


    /**
     * Update the value of the totalTextView when the app resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            String amountText = amountEditText.getText().toString();
            double amount = Double.parseDouble(amountText);
            // Prevent negative values
            if (amount < 0) {
                amount = 1;  // Set amount to 1
                amountEditText.setText("1");
            }
            double total = amount * conversionRate;
            totalTextView.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            amountEditText.setText("1");
            totalTextView.setText(String.format("%.2f", 1 * conversionRate));
        }
    }
}
