package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    EditText SearchEdit;
    ImageView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        SearchEdit = findViewById(R.id.SearchEdit);
        closeBtn = findViewById(R.id.closeBtn);
        SearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() >0)
                {
                    closeBtn.setVisibility(View.VISIBLE);
                    findViewById(R.id.sideLine).setVisibility(View.VISIBLE);
                }
                else
                {
                    closeBtn.setVisibility(View.GONE);
                    findViewById(R.id.sideLine).setVisibility(View.GONE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>0)
                {

                }else
                {

                }

            }
        });
    }

    public void closeBtn(View view)
    {
        SearchEdit.setText("");
    }

    public void goBack(View view)
    {
        SearchEdit.setText("");
        super.onBackPressed();
    }

}