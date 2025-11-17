package com.example.zakatgoldcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText editWeight, editValue;
    RadioGroup radioGroupType;
    RadioButton radioKeep, radioWear;
    Button btnCalculate, btnReset;
    TextView textTotalValue, textZakatPayable, textTotalZakat;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Zakat Gold Calculator");

        // Initialize widgets
        editWeight = findViewById(R.id.editWeight);
        editValue = findViewById(R.id.editValue);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioKeep = findViewById(R.id.radioKeep);
        radioWear = findViewById(R.id.radioWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        textTotalValue = findViewById(R.id.textTotalValue);
        textZakatPayable = findViewById(R.id.textZakatPayable);
        textTotalZakat = findViewById(R.id.textTotalZakat);

        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Calculate button
        btnCalculate.setOnClickListener(v -> {
            String weightStr = editWeight.getText().toString().trim();
            String valueStr = editValue.getText().toString().trim();

            if (weightStr.isEmpty() || valueStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight, valuePerGram;
            try {
                weight = Double.parseDouble(weightStr);
                valuePerGram = Double.parseDouble(valueStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine gold type
            double nisab;
            if (radioKeep.isChecked())
                nisab = 85;
            else if (radioWear.isChecked())
                nisab = 200;
            else {
                Toast.makeText(this, "Please select type of gold", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- Calculation---
            double totalValue = weight * valuePerGram;              // Total gold value
            double payableGrams = weight - nisab;                   // Payable grams
            if (payableGrams < 0) payableGrams = 0;
            double zakatPayableValue = payableGrams * valuePerGram; // Zakat payable value
            double totalZakat = zakatPayableValue * 0.025;         // 2.5% Zakat

            // Display results
            textTotalValue.setText("Total Gold Value: RM" + df.format(totalValue));
            textZakatPayable.setText("Gold Value Zakat Payable: RM" + df.format(zakatPayableValue));
            textTotalZakat.setText("Total Zakat (2.5%): RM" + df.format(totalZakat));
        });

        // Reset button
        btnReset.setOnClickListener(v -> {
            editWeight.setText("");
            editValue.setText("");
            radioKeep.setChecked(false);
            radioWear.setChecked(false);
            textTotalValue.setText("Total Gold Value: RM 0.00");
            textZakatPayable.setText("Gold Value Zakat Payable: RM 0.00");
            textTotalZakat.setText("Total Zakat (2.5%): RM 0.00");
        });
    }

    // Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuSetting) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuShare) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please use my application -https://github.com/wanhasyawadhihahwy/ZakatGoldCalculator");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
