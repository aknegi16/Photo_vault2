package com.example.akashn.photo_vault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends AppCompatActivity implements View.OnClickListener {

    Button Button9,Button8,Button7,Button6,Button5,Button4,Button3,Button2,Button1,Button0,Buttondeci,Buttonplus,Buttonmin,Buttondiv,Buttonequal,Buttonadd,Buttondel;
    TextView CONTAINER;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);




        CONTAINER=(TextView) findViewById(R.id.CONTAIN);
        Button0=(Button) findViewById(R.id.button0);
        Button1=(Button)findViewById(R.id.button1);
        Button2=(Button) findViewById(R.id.button2);
        Button3=(Button) findViewById(R.id.button3);
        Button4=(Button)findViewById(R.id.button4);
        Button5=(Button) findViewById(R.id.button5);
        Button6=(Button) findViewById(R.id.button6);
        Button7=(Button)findViewById(R.id.button7);
        Button8=(Button) findViewById(R.id.button8);
        Button9=(Button) findViewById(R.id.button9);
        Buttondeci=(Button) findViewById(R.id.buttondeci);
        Buttonplus=(Button) findViewById(R.id.buttonplus);
        Buttondiv=(Button) findViewById(R.id.buttondiv);
        Buttondel=(Button) findViewById(R.id.buttondel);
        Buttonequal=(Button) findViewById(R.id.buttonfinal);
        Buttonmin=(Button) findViewById(R.id.buttonmin);



        Buttonequal.setOnClickListener(this);

        Button0.setOnClickListener(this);

        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button3.setOnClickListener(this);
        Button4.setOnClickListener(this);
        Button5.setOnClickListener(this);
        Buttondel.setOnClickListener(Calculator.this);
        Buttonplus.setOnClickListener(Calculator.this);



    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttondel:
                int length=CONTAINER.getText().toString().length();
                if(length == 1){
                    CONTAINER.setText("0");
                }
                else{
                    String original = CONTAINER.getText().toString();

                    original=original.substring(0,length-1);
                    CONTAINER.setText(original);
                }
                break;
            case R.id.button1:
                if(CONTAINER.getText().toString().equals("0")){
                    CONTAINER.setText("1");
                } else{
                    CONTAINER.setText(CONTAINER.getText()+"1");
                }
                break;
            case R.id.button2:
                if(CONTAINER.getText().toString().equals("0")){
                    CONTAINER.setText("2");
                } else{
                    CONTAINER.setText(CONTAINER.getText() + "2");
                }
                break;
            case R.id.button3:
                if(CONTAINER.getText().toString().equals("0")){
                    CONTAINER.setText("3");
                }else{
                    CONTAINER.setText(CONTAINER.getText().toString() + "3");
                }
                break;
            case R.id.button4:
                if(CONTAINER.getText().toString().equals("0")){
                    CONTAINER.setText("4");
                }else{
                    CONTAINER.setText(CONTAINER.getText().toString() + "4");
                }
                break;
            case R.id.buttonplus:
                if(CONTAINER.getText().toString().equals("0")){
                    CONTAINER.setText("0");
                }
                else {
                    CONTAINER.setText("" + CONTAINER.getText() + "+");
                }
                break;
            case R.id.buttonfinal:


                if(CONTAINER.getText().toString().equals("123"))
                    startActivity(new Intent(Calculator.this,login.class));


        }
    }
}
