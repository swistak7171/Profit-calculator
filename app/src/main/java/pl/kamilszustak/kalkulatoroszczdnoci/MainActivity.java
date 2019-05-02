package pl.kamilszustak.kalkulatoroszczdnoci;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {

    private EditText balanceEditText;
    private EditText periodOfTimeEditText;
    private EditText monthlyPaymentEditText;
    private EditText annualInterestRateEditText;
    private EditText profitTaxEditText;
    private Spinner periodOfTimeSpinner;
    private Spinner interestCapitalizationSpinner;
    private Switch monthlyPaymentSwitch;
    private Switch profitTaxSwitch;
    private Button calculateButton;
    private MaterialCardView balanceCardView;
    private MaterialCardView periodOfTimeCardView;
    private MaterialCardView monthlyPaymentCardView;
    private MaterialCardView annualInterestRateCardView;
    private MaterialCardView interestCapitalizationCardView;
    private MaterialCardView profitTaxCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        customiseControls();
        setupViewsListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_item:
                resetValues();
                return true;

            case R.id.changelog_item:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.whats_new)
                        .setMessage(R.string.changelog_dialog)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton("OK", (changelogDialog, which) -> changelogDialog.cancel())
                        .show();
                return true;

            case R.id.rate_app_item:
                try {
                    // Trzeba dodać te flagi, żeby stos aplikacji Google Play został wyczyszczony
                    // i żebyśmy nie musieli przechodzić przez niego przy wciskaniu przycisku Wstecz
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()))
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return true;

            case R.id.privacy_policy_item:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacypolicies.com/privacy/view/ba90adf39b342bd625e371007e90b477"));
                startActivity(browserIntent);
                return true;

            case R.id.info_item:
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.info)
                        .setMessage(R.string.info_dialog)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(R.string.ok, (infoDialog, which) -> infoDialog.cancel())
                        .setNeutralButton(R.string.contact, (infoDialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_SEND)
                                    .setType("message/rfc822")
                                    .putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.email)})
                                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            try {
                                startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.this, R.string.no_email_client, Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                dialog.show();
                // Link jest otwierany w przeglądarce
                ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews() {
        balanceEditText = findViewById(R.id.balanceEditText);
        periodOfTimeEditText = findViewById(R.id.periodOfTimeEditText);
        monthlyPaymentEditText = findViewById(R.id.monthlyPaymentEditText);
        annualInterestRateEditText = findViewById(R.id.annualInterestRateEditText);
        profitTaxEditText = findViewById(R.id.profitTaxEditText);
        periodOfTimeSpinner = findViewById(R.id.periodOfTimeSpinner);
        interestCapitalizationSpinner = findViewById(R.id.interestCapitalizationSpinner);
        monthlyPaymentSwitch = findViewById(R.id.monthlyPaymentSwitch);
        profitTaxSwitch = findViewById(R.id.profitTaxSwitch);
        calculateButton = findViewById(R.id.calculateButton);

        balanceCardView = findViewById(R.id.balanceCardView);
        periodOfTimeCardView = findViewById(R.id.periodOfTimeCardView);
        monthlyPaymentCardView = findViewById(R.id.monthlyPaymentCardView);
        annualInterestRateCardView = findViewById(R.id.annualInterestRateCardView);
        interestCapitalizationCardView = findViewById(R.id.interestCapitalizationCardView);
        profitTaxCardView = findViewById(R.id.profitTaxCardView);
    }

    private void setupViewsListeners() {
        setupButtonsListeners();
        setupEditTextsListeners();
        setupSwitchesListeners();
        setupCardViewsListeners();
    }

    private void setupButtonsListeners() {
        calculateButton.setOnClickListener(view -> {
            if(!areFieldsEmpty()) {
                new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle)
                        .setTitle(R.string.result)
                        .setMessage(calculateProfitAndBuildStringResult())
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton(R.string.ok, (dialog, which) -> dialog.cancel())
                        .show();
            }
            else
                Toast.makeText(MainActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT)
                        .show();
        });
    }

    private void setupCardViewsListeners() {
        balanceCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.balance_toast));
            return false;
        });

        periodOfTimeCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.period_of_time_toast));
            return false;
        });

        monthlyPaymentCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.monthly_payment_toast));
            return false;
        });

        annualInterestRateCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.annual_interest_rate_toast));
            return false;
        });

        interestCapitalizationCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.interest_capitalization_toast));
            return false;
        });

        profitTaxCardView.setOnLongClickListener(view -> {
            longClickAction(getString(R.string.profit_tax_toast));
            return false;
        });
    }

    private void setupEditTextsListeners() {
        periodOfTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Zapobiega wpisaniu 0 jako okresu
                if(s.toString().startsWith("0"))
                    s.clear();
            }
        });
    }

    private void setupSwitchesListeners() {
        monthlyPaymentSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                monthlyPaymentEditText.setEnabled(isChecked));

        profitTaxSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                profitTaxEditText.setEnabled(isChecked));
    }

    private void longClickAction(String text) {
        vibrateOnLongClick();
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT)
                .show();
    }

    private void customiseControls() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.period_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_view);
        periodOfTimeSpinner.setAdapter(adapter);
        periodOfTimeSpinner.setSelection(0);

        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.capitalization_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_view);
        interestCapitalizationSpinner.setAdapter(adapter);
        interestCapitalizationSpinner.setSelection(0);
    }

    private void vibrateOnLongClick() {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator())
            vibrator.vibrate(35);
    }

    private String calculateProfitAndBuildStringResult() {
        int months = Integer.parseInt(periodOfTimeEditText.getText().toString());
        // Sprawdza, czy zaznaczona jest opcja "lat". Jeśli tak, to przelicza wartość Period na miesiące
        if(periodOfTimeSpinner.getSelectedItemPosition() == 1)
            months *= 12;


        // Na ile części trzeba podzielić procent roczny (x)?
        // item 0 - miesięczna (12)
        // item 1 - kwartalna (4)
        // item 2 - roczna (1)
        // item 3 - na koniec okresu (długość okresu)
        int capitalization = interestCapitalizationSpinner.getSelectedItemPosition();
        boolean capitalizationAtEndOfPeriod = false;
        switch(capitalization) {
            case 0:
                capitalization = 12;
                break;
            case 1:
                capitalization = 4;
                break;
            case 2:
                capitalization = 1;
                break;
            case 3:
                capitalizationAtEndOfPeriod = true;
                break;
        }
        Calculator calculator = new Calculator(Double.parseDouble(balanceEditText.getText().toString()), months, (monthlyPaymentSwitch.isChecked() ? Double.parseDouble(monthlyPaymentEditText.getText().toString()) : 0), Double.parseDouble(annualInterestRateEditText.getText().toString()), capitalization, capitalizationAtEndOfPeriod,(profitTaxSwitch.isChecked() ? Double.parseDouble(profitTaxEditText.getText().toString()) : 0));

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.balance)).append(": ").append(balanceEditText.getText().toString()).append("\n");
        builder.append(getString(R.string.period_of_time)).append(": ").append(periodOfTimeEditText.getText().toString()).append(" ").append(periodOfTimeSpinner.getSelectedItem().toString()).append("\n");
        if(monthlyPaymentSwitch.isChecked())
            builder.append(getString(R.string.monthly_payment)).append(": ").append(monthlyPaymentEditText.getText().toString()).append("\n");
        builder.append(getString(R.string.annual_interest_rate)).append(": ").append(annualInterestRateEditText.getText().toString()).append("\n");
        builder.append(getString(R.string.interest_capitalization)).append(": ").append(interestCapitalizationSpinner.getSelectedItem().toString()).append("\n");
        if(profitTaxSwitch.isChecked())
            builder.append(getString(R.string.profit_tax)).append(": ").append(profitTaxEditText.getText().toString()).append("%").append("\n");
        builder.append("\n").append(getString(R.string.result)).append(": ").append(calculator.getActualBalance()).append(" (").append(getString(R.string.profit)).append(": ").append(((double)Math.round(((calculator.getProfit())*100))/100)).append(")");

        return builder.toString();
    }

    private boolean areFieldsEmpty() {
        return (balanceEditText.getText().toString().equals("") || periodOfTimeEditText.getText().toString().equals("") || (monthlyPaymentSwitch.isChecked() && monthlyPaymentEditText.getText().toString().equals("")) || annualInterestRateEditText.getText().toString().equals("") || (profitTaxSwitch.isChecked() && profitTaxEditText.getText().toString().equals("")));
    }

    private void resetValues() {
        balanceEditText.setText("");
        periodOfTimeEditText.setText("");
        periodOfTimeSpinner.setSelection(0);
        monthlyPaymentEditText.setText("");
        annualInterestRateEditText.setText("");
        interestCapitalizationSpinner.setSelection(0);
        profitTaxEditText.setText(R.string.default_tax);

        Toast.makeText(MainActivity.this, R.string.cleared_values, Toast.LENGTH_SHORT)
                .show();
    }
}
