package com.forager.meal.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.forager.meal.ui.R;

public class HomeScreen extends Activity implements View.OnClickListener{


    TextView mRegister;
    Button mSignIn;
    ImageButton mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initView();
    }

    private void initView() {
        mRegister = (TextView) findViewById(R.id.txt_register);
        mSignIn = (Button) findViewById(R.id.sign_btn);
        mTitle = (ImageButton) findViewById(R.id.title);
        mRegister.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mTitle.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.title:
                Intent mIntroIntent = new Intent(HomeScreen.this, IntroActivity.class);
                mIntroIntent.putExtra("checkShownCount", false);
                startActivity(mIntroIntent);
                break;
            case R.id.sign_btn:
                Intent mSignInIntent = new Intent(HomeScreen.this, SignInActivity.class);
                startActivity(mSignInIntent);
       finish();
                break;
            case R.id.txt_register:
                Intent mRegisterIntent = new Intent(HomeScreen.this, RegisterActivity.class);
                startActivity(mRegisterIntent);
                break;
            default:
                break;
        }


    }
}
