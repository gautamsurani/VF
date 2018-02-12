package com.vedicfashions.shopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by welcome on 16-11-2016.
 */
public class TestClass extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        for (int i=1;i<=5;i++){


            for(int j=1;j<=5;j++){
              /*  if(5%j){

                }*/
                System.out.print(" ");
            }

        }
    }
}
