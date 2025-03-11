/* I Ahmed Nakhuda, 000878456 certify that this material is my original work.
   No other person's work has been used without due acknowledgement. */

package ca.mohawk.nakhuda;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class used to switch to the currency the user wants to select
 */
public class MainActivity2 extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    /** Displays the conversion rate amount  */
    private TextView conversionRateTextView;

    /** Spinner to select the currency */
    private Spinner spinner;

    /** Keep track of country selected, default is USA  */
    private String flag = "USA";

    /** Conversion rate for the country  */
    private double conversionRate;

    /** Name of the SharedPreferences file */
    public static final String SP_File = "saved_configuration";

    /** Store the conversion rate  */
    public static final String Conversion_State = "conversion_state";

    /** Store the country */
    public static final String Country_State = "country_state";

    /** Used to debug */
    private static final String tag = "==MainActivity TWO==";


    /**
     * Initializes the UI components, retrieves intent data, and updates the UI
     * @param savedInstanceState holds data about the previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        conversionRateTextView = findViewById(R.id.conversionRateTextView);
        spinner = findViewById(R.id.spinner);

        // USA conversion rate
        conversionRateTextView.setText("0.7");

        Intent intent = getIntent();
        if (intent != null) {
            String receivedRate = intent.getStringExtra("CONVERSION_RATE");
            flag = intent.getStringExtra("FLAG");

            if (flag.equals("USA")) {
                spinner.setSelection(0);
            } else if (flag.equals("Japan")) {
                spinner.setSelection(1);
            } else if (flag.equals("Cuba")) {
                spinner.setSelection(2);
            } else if (flag.equals("Kuwait")) {
                spinner.setSelection(3);
            } else if (flag.equals("Saudi Arabia")) {
                spinner.setSelection(4);
            }

            if (receivedRate != null) {
                conversionRate = Double.parseDouble(receivedRate);
                conversionRateTextView.setText(String.format("%.2f", conversionRate));
            }
        }
    }

    /**
     * OnClick method to save the flag and conversion rate,
     * send the flag and conversion rate values back to MainActivity,
     * and save the shared preferences.
     * @param view (unused)
     */
    public void save(View view) {
        Log.d(tag, "onClickSave");

        // Shared Preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(SP_File,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Conversion_State, String.valueOf(conversionRate));
        editor.putString(Country_State, String.valueOf(lastPosition));
        editor.apply();
        Log.d(tag, "Saving SharedPreference: " + conversionRate + " spinner = " + lastPosition);


        // Intent
        String selectedOption = spinner.getSelectedItem().toString();
        String conversionRate = "";

        // Conversion rates from Google currency converter
        if (selectedOption.equals("US Dollar")) {
            conversionRate = "0.7";
            flag = "USA";
        } else if (selectedOption.equals("Japanese Yen")) {
            conversionRate = "107.65";
            flag = "Japan";
        } else if (selectedOption.equals("Cuban Peso")) {
            conversionRate = "16.94";
            flag = "Cuba";
        } else if (selectedOption.equals("Kuwaiti Dinar")) {
            conversionRate = "0.22";
            flag = "Kuwait";
        } else if (selectedOption.equals("Saudi Riyal")) {
            conversionRate = "2.64";
            flag = "Saudi Arabia";
        }

        conversionRateTextView.setText(conversionRate);
        Log.d(tag, "Selected Item: " + selectedOption);

        Intent intent = new Intent();
        intent.putExtra("CONVERSION_RATE", conversionRate);
        intent.putExtra("FLAG", flag);

        setResult(RESULT_OK, intent);

        finish();
    }


    /** The last position of the spinner */
    private int lastPosition = 0;

    /**
     * Method to get the last position of the spinner
     * @param parent (unused)
     * @param view (unused)
     * @param position the position of the spinner
     * @param id (unused)
     */
    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View view, int position, long id) {
        // Load string array from resources
        String[] countries = getResources().getStringArray(R.array.spinner_options);
        lastPosition = position;
        Log.d(tag, countries[position]);
    }


    /**
     * Method invoked when no item is selected in the spinner
     * @param adapterView (unused)
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(tag, "No item selected in the spinner");
    }
}