
package com.project.lexicalphase;

import android.util.Log;

/**
 * Created by Abdullah on 06-May-16.
 */
class SyntaxAnalyzer {

    protected myScanner lexer;	// lexical analyzer
    protected TokenPair token;	// current token

    public SyntaxAnalyzer() {
        lexer = new myScanner();
        getToken ();
    }

    private void getToken (){
        token = lexer.getTokenPair();
    }

    public void constDeclaration (){
        if (token.getToken().toString().equals("IDENTIFIER")) {
            getToken();
            if (token.getToken().toString().equals("equals")
                    || token.getToken().toString().equals("=") )
            {
                getToken();
                if (token.getToken().toString().equals("INTEGER"))
                    getToken ();
                else
                    Log.d("TAG", token.getLine() + "Integer expected");
            }
            else  Log.d("TAG", token.getLine()+"= expected");
        }
        else  Log.d("TAG", token.getLine()+"id expected");
    }

    public void varDeclaration () {
        if (token.getToken().toString().equals("IDENTIFIER"))
            getToken ();
        else
            Log.d("TAG", token.getLine() + "ID EXPECTED");
    }

    public void factor () {
        if (token.getToken().toString().equals("IDENTIFIER"))
            getToken ();
        else if (token.getToken().toString().equals("INTEGER"))
            getToken ();
        else if (token.getToken().toString().equals("(")) {
            getToken ();
            expression ();
            if (token.getToken().toString().equals(")"))
                getToken ();
            else
                Log.d("TAG", token.getLine() + "MISSING )");
        }
        else  Log.d("TAG", token.getLine() + "UNRECOGNIZABLE SYMBOL");
    }

    public void term (){
        factor ();
        while (token.getToken().toString().equals("*") ||
                token.getToken().toString().equals("/")) {
            getToken ();
            factor ();
        }
    }

    public void expression (){
        if (token.getToken().toString().equals("+")  ||
                token.getToken().toString().equals("-")) {
            getToken ();
            term ();
        }
        else
            term ();
        while (token.getToken().toString().equals("+")  ||
                token.getToken().toString().equals("-"))  {
            getToken ();
            term ();
        }
    }

    public void condition (){
        if (token.getToken().toString().equals("CONDITION")) {
            getToken ();
            expression ();
        }
        else {
            expression ();
            switch (token.getToken().toString()) {
                case "equals to" :
                case "less than" :
                case "greater than" :
                case "not equals to"  :
                case "less than equals to" :
                case "greater than equals to":
                case "==" :
                case "<" :
                case ">" :
                case "!="  :
                case "<=" :
                case ">=":

                    getToken ();
                    expression ();
                    break;
                default :
                    Log.d("TAG", token.getLine() + "RELATIONAL OPERATOR EXPECTED");
            }
        }
    }

    public void statement () {
        switch (token.getToken().toString()) {
            case "IDENTIFIER" :
                getToken();
                if (token.getToken().toString().equals("ASSIGN"))
                    getToken ();
                else
                    Log.d("TAG", token.getLine() + ":= EXPECTED");
                expression ();
                break;
            case "FUNCTION" :
                getToken ();
                if (token.getToken().toString().equals("IDENTIIER"))
                    Log.d("TAG", token.getLine() + "ID EXPECTED");
                else {
                    getToken ();
                    break;
                }
            case "IF" :
                getToken ();
                condition ();
                if (token .getToken().toString().equals("THEN"))
                    getToken ();
                else
                    Log.d("TAG", "THEN EXPECTED");
                statement ();
                break;
            case "START" :
                getToken ();
                statement();
                while (token .getToken().toString().equals(".")) {
                    getToken ();
                    statement ();
                }
                if (token.getToken().toString().equals("END"))
                    getToken ();
                else
                    Log.d("TAG", token.getLine() + "END OR . EXPECTED");
                break;
            case "REPEAT" :
                getToken ();
                condition ();
                if (token.getToken().toString().equals("FOREVER"))
                    getToken ();
                else
                    Log.d("TAG", token.getLine() + "DO EXPECTED");
                statement ();
                break;
        }
    }

    public void block ()  {
        if (token.getToken().toString().equals("CONSTANT")) {
            getToken ();
            constDeclaration ();
            while (token.getToken().toString().equals(",")) {
                getToken ();
                constDeclaration ();
            }
            if (token.getToken().toString().equals("."))
                getToken ();
            else
            Log.d("TAG", token.getLine() + ", OR . EXPECTED");
        }
        if (token.getToken().toString().equals("DATATYPE")) {
            getToken ();
            varDeclaration ();
            while (token.getToken().toString().equals(",")) {
                getToken ();
                varDeclaration ();
            }
            if (token.getToken().toString().equals("."))
                getToken ();
            else
                Log.d("TAG", token.getLine() + ", OR . EXPECTED");
        }
        while (token.getToken().toString().equals("FUNCTION")) {
            getToken();
            if (token.getToken().toString().equals("IDENTIFIER"))
                getToken ();
            else
                Log.d("TAG", token.getLine() + "ID EXPECTED");
            if (token.getToken().toString().equals("."))
                getToken ();
            else
                Log.d("TAG", token.getLine() + ". EXPECTED");
            block ();
            if (token.getToken().toString().equals("."))
                getToken ();
            else
                Log.d("TAG", token.getLine() + ". EXPECTED");
        }
        statement ();
    }

    public void program (){
        block();
        if (token.getToken().toString().equals("."))
            Log.d("TAG", token.getLine() + ". EXPECTED");
    }

}