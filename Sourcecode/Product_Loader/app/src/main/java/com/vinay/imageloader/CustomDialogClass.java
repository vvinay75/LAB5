package com.vinay.imageloader;
//Coded By Vinay
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.vinay.imageloader.R;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.prediction.Concept;



public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
   public List<Concept> cc;
    public CustomDialogClass(Activity a, List<Concept> cc) {
        super(a);
        this.c = a;
        this.cc=cc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_result_dialog);
        yes = (Button) findViewById(R.id.button_yes);

        yes.setOnClickListener(this);

        ArrayList<TextView> tl =new ArrayList<TextView>();
        tl.add((TextView)findViewById(R.id.res_1));
        tl.add((TextView)findViewById(R.id.res_2));
        tl.add((TextView)findViewById(R.id.res_3));
        tl.add((TextView)findViewById(R.id.res_4));
        tl.add((TextView)findViewById(R.id.res_5));
        int i=0;
        for(Concept concept:cc){
            if(i>4)break;
            tl.get(i).setText(concept.name()+"\t"+concept.value());

            i++;
        }
       // no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_yes:
                this.dismiss();
                break;

            default:
                break;
        }

    }
}