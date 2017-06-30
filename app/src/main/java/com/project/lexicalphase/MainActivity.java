package com.project.lexicalphase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends Activity {

    public static String code = null;
    LinkedList<TokenPair> Tok = null;
    myScanner scanner = new myScanner();
    public StringBuffer dataBuffer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        LinearLayout ll_genTokens = new LinearLayout(this);
        ll_genTokens = (LinearLayout) findViewById(R.id.LL_genTokens);




        ll_genTokens.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Log.d("TAG","Calling LoadData");
                EditText editText = new EditText(getApplicationContext());
                editText = (EditText) findViewById(R.id.etCode);
                dataBuffer = new StringBuffer();

                code = editText.getText().toString();

                if(!code.equals(""))
                {
                    Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                    dataBuffer.append(code);
                    scanner.loadData(dataBuffer);
                    scanner.loadData(dataBuffer);
                    scanner.dispatcher();
                    Tok = scanner.getQueue();

                  //  writetoks(Tok);

                    setContentView(R.layout.activity_tokens);
                    EditText et = new EditText(getApplicationContext());
                    et= (EditText) findViewById(R.id.etOutTokens);
                    StringBuffer sb = new StringBuffer();

                    for(TokenPair tp : Tok)
                    {
                        sb.append(tp.getToken()+ " ");
                        sb.append(tp.getLexeme()+ " ");
                        sb.append(tp.getLine());
                        sb.append('\n');
                    }
                    et.setText(sb);

//                    Intent intent = new Intent(getApplicationContext(),ActivityTokens.class);
//                   // ArrayList<TokenPair> list = new ArrayList<TokenPair>(Tok);
//                   // intent.putExtra("TOKENS", Tok);
//                    Bundle b = new Bundle();
//                    b.putParcelable("TOKENS", Parcels.wrap(Tok.toArray()));
//                    startActivity(intent, b);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Write some Code.",Toast.LENGTH_LONG).show();
                }
            }
        });

}

    private void writetoks(LinkedList<TokenPair> toks)
    {
        while (!toks.isEmpty())
        {
            TokenPair tp = scanner.getTokenPair();
            String os = String.format("%25s%10s%20s","ClassPart: "+tp.getToken(),"Line: "+tp.getLine(),"ValuePart: "+tp.getLexeme()+"\n");
            Log.i("TAG", os);
        }
    }

    public Context getContextofApp(){
        return this.getApplicationContext();
    }
}
