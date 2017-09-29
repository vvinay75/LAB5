package com.vinay.imageloader;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.vinay.imageloader.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class CustomDialogSearchClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
   public List<Store> cc;
    public CustomDialogSearchClass(Activity a, List<Store> cc) {
        super(a);
        // TODO Auto-generated constructor stub
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

        ArrayList<TextView> textViewList =new ArrayList<TextView>();
        textViewList.add((TextView)findViewById(R.id.res_1));
        textViewList.add((TextView)findViewById(R.id.res_2));
        textViewList.add((TextView)findViewById(R.id.res_3));
        textViewList.add((TextView)findViewById(R.id.res_4));
        textViewList.add((TextView)findViewById(R.id.res_5));
        textViewList.get(0).setText("StoreName"+"\t"+"Address");
        int i=1;
        Collections.shuffle(cc);
        for(Store concept:cc){
            if(i>4)break;
            textViewList.get(i).setText(concept.getName()+"\t"+"--"+concept.getAddress());

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