package com.project.lexicalphase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abdullah on 02-Apr-16.
 */
public class ActivityTokens extends Activity {

 myScanner scanner = new myScanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tokens);

//        Toast.makeText(getApplicationContext(),"In Activity_Tokens",Toast.LENGTH_LONG).show();
//        Serializable b = getIntent().getSerializableExtra("TOKENS");

//
//        LinkedList<TokenPair> tokenPairs  = Parcels.unwrap(getIntent().getParcelableExtra("TOKENS"));
//        Log.d("TAG","Size: "+tokenPairs.size());

    }

    private void printinlog(ArrayList<TokenPair> toks) {

        while (!toks.isEmpty()) {
            TokenPair tp = scanner.getTokenPair();
            String os = String.format("%25s%10s%20s","ClassPart: "+tp.getToken(),"Line: "+tp.getLine(),"ValuePart: "+tp.getLexeme()+"\n");
            Log.i("TAG Activity Tokens", os);
        }
    }
}
