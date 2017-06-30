package com.project.lexicalphase;


import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Abdullah on 22-Mar-16.
 */
public class myScanner {
    private StringBuffer inFilebuffer;
    private int filePointer;
    private int line;


    private LinkedList<TokenPair> tokenQueue;

    String token = "";
    StringBuffer lexeme;

    public myScanner()
    {
        inFilebuffer = new StringBuffer();
        filePointer = 0;
        line = 1;
        tokenQueue = new LinkedList<TokenPair>();
    }

    public void loadData(StringBuffer buffer)
    {
        inFilebuffer = buffer ;
    }

    protected LinkedList<TokenPair> getQueue()
    {
        return  tokenQueue;
    }

    protected TokenPair getTokenPair()
    {
       if(tokenQueue.size() == 0)
       {
           return  null;
       }
        else{
           return tokenQueue.removeFirst();
       }

    }

    private void addTokenQueue(TokenPair tp)
    {
        tokenQueue.add(tp);
    }

    public void dispatcher()
    {
        while (filePointer < inFilebuffer.length())
        {
            char inChar = inFilebuffer.charAt(filePointer);
            int prevPointer = filePointer;

            if((int)inChar > 127)
            {
                TokenPair tp =  new TokenPair("CP_ERROR","Not ASCII",line);
                addTokenQueue(tp);
                filePointer++;
                continue;
            }

            if(inChar=='\n')
            {
                line ++;
                filePointer++;
                continue;

            }
            if(inChar == '\r')
            {
                filePointer++;
                continue;
            }

            if(inChar == '-')
            {
                skipComment();
            }
            else if (isLetter(inChar) || inChar == '_')
            {
                try
                {
                fsaLetter();
                }
                 catch (IndexOutOfBoundsException e)
                {
                Log.d("TAG",e.toString());


                }


            }

            else if (isDigit(inChar))
            {
                fsaDigit();
            }
            else if(inChar ==  '\'')
            {
                fsaStringLit();
            }
            else if (isWhiteSpace(inChar))
            {
                filePointer++;
                continue;
            }
            else if (inChar == '>')
            {
                fsaGThan();
            }
            else if (inChar == '<')
            {
                fsaLThan();
            }
            else if (inChar == ':')
            {
                fsaColon();
            }
            else if (inChar == '.')
            {
                //DotTerminator
                TokenPair tp = new TokenPair(".",".",line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == ';')
            {
                TokenPair tp = new TokenPair(";", ";", line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '(')
            {
                TokenPair tp = new TokenPair("(", "(", line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == ')')
            {
                TokenPair tp = new TokenPair(")", ")", line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '=')
            {
                TokenPair tp = new TokenPair("=", "=", line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '+' )
            {
                TokenPair tp = new TokenPair("+","+",line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '-' )
            {
                TokenPair tp = new TokenPair("-","-",line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '*' )
            {
                TokenPair tp = new TokenPair("*","*",line);
                addTokenQueue(tp);
                filePointer++;
            }
            else if (inChar == '/' )
            {
                TokenPair tp = new TokenPair("/","/",line);
                addTokenQueue(tp);
                filePointer++;
            }
            else
            {
                TokenPair tp = new TokenPair("MP_ERROR",inChar+"",line);
                addTokenQueue(tp);
                filePointer++;
            }

         }
     }

    private void oldfsaLetter() throws IndexOutOfBoundsException
    {
        StringBuffer lexeme = new StringBuffer();
        String str;
        boolean immediateUndescore = false;
        boolean finished = false;
        char c = inFilebuffer.charAt(filePointer);
        lexeme.append(c);

        if(c == '_')
        {
            immediateUndescore = true;
        }

        while(!finished)
        {
            filePointer++;
         //   if(inFilebuffer.charAt(filePointer)!= '\u0000') {
                c = inFilebuffer.charAt(filePointer);
//}
//            else
//            {
//                Log.d("TAG","weired whitespace char");
//                finished = true;
//                break;
//            }
//            if (isWhiteSpace(c) || c == '.'|| c == '+'||
//                    c == '-' || c == '/' || c == '*' ||
//                    c == '(' || c == ')' || c == '\n')
            if (isWhiteSpace(c) || c == '.')
            {

                if (immediateUndescore) {
                    Log.d("TAG","Error While _ is inputed !!");
                    finished = true;
                    break;
                }

                if (c == '.')
                {
                    //check if next char is a number or not.
                    int nextPointer = filePointer;
                    nextPointer ++;
                    char d = inFilebuffer.charAt(nextPointer);
                    if(isDigit(d))
                    {
                        lexeme.append(c);
                        immediateUndescore  = false;

                        Log.d("TAG","Reading Floating Points");
                        continue;
                    }

                    else{
                        finished = true;
                        break;
                    }

                }

            }
            else {
                if (isLetter(c)) {
                    lexeme.append(c);
                    immediateUndescore = false;
                    continue;
                }
                else if (isDigit(c)) {
                    lexeme.append(c);
                    immediateUndescore = false;
                    continue;
                }
                else if (c == '_' && !immediateUndescore) {
                    lexeme.append(c);
                    immediateUndescore = true;
                    continue;
                }
                else if (c == '_')
                {
                    System.out.println("Error : Two underscore in input File");
                    finished = true;
                    break;
                }
                else
                {
                    break;
                }

            }
        }

        str = lexeme.toString();

        if(str.equals("and"))
        {
            TokenPair tp = new TokenPair("AND","and",line);
            addTokenQueue(tp);
        }
        else if(str.equals("start"))
        {
            TokenPair tp = new TokenPair("START","start",line);
            addTokenQueue(tp);
        }
        else if(str.equals("divide"))
        {
            TokenPair tp = new TokenPair("DIVIDE","divide",line);
            addTokenQueue(tp);
        }
        else if(str.equals("do"))
        {
            TokenPair tp = new TokenPair("DO","do",line);
            addTokenQueue(tp);
        }
        else if(str.equals("downto"))
        {
            TokenPair tp = new TokenPair("DOWNTO","downto",line);
            addTokenQueue(tp);
        }
        else if(str.equals("else"))
        {
            TokenPair tp = new TokenPair("ELSE","else",line);
            addTokenQueue(tp);
        }
        else if(str.equals("end"))
        {
            TokenPair tp = new TokenPair("END","end",line);
            addTokenQueue(tp);
        }
        else if(str.equals("fixed"))
        {
            TokenPair tp = new TokenPair("FIXED","fixed",line);
            addTokenQueue(tp);
        }
        else if(str.equals("real"))
        {
            TokenPair tp = new TokenPair("REAL","real",line);
            addTokenQueue(tp);
        }
        else if(str.equals("for"))
        {
            TokenPair tp = new TokenPair("FOR","for",line);
            addTokenQueue(tp);
        }
        else if(str.equals("function"))
        {
            TokenPair tp = new TokenPair("FUNCION","function",line);
            addTokenQueue(tp);
        }
        else if(str.equals("if"))
        {
            TokenPair tp = new TokenPair("IF","if",line);
            addTokenQueue(tp);
        }
        else if(str.equals("integer"))
        {
            TokenPair tp = new TokenPair("INTEGER","integer",line);
            addTokenQueue(tp);
        }
        else if(str.equals("mod"))
        {
            TokenPair tp = new TokenPair("MOD","mod",line);
            addTokenQueue(tp);
        }
        else if(str.equals("not"))
        {
            TokenPair tp = new TokenPair("MP_NOT","not",line);
            addTokenQueue(tp);
        }
        else if(str.equals("or"))
        {
            TokenPair tp = new TokenPair("OR","OR",line);
            addTokenQueue(tp);
        }
        else if(str.equals("procedure"))
        {
            TokenPair tp = new TokenPair("MP_PROCEDURE","procedure",line);
            addTokenQueue(tp);
        }
        else if(str.equals("program"))
        {
            TokenPair tp = new TokenPair("PROGRAM","program",line);
            addTokenQueue(tp);
        }
        else if(str.equals("read"))
        {
            TokenPair tp = new TokenPair("READ","read",line);
            addTokenQueue(tp);
        }
        else if(str.equals("repeat"))
        {
            TokenPair tp = new TokenPair("REPEAT","repeat",line);
            addTokenQueue(tp);
        }
        else if(str.equals("then"))
        {
            TokenPair tp = new TokenPair("THEN","then",line);
            addTokenQueue(tp);
        }
        else if(str.equals("to"))
        {
            TokenPair tp = new TokenPair("TO","to",line);
            addTokenQueue(tp);
        }
        else if(str.equals("until"))
        {
            TokenPair tp = new TokenPair("UNTIL","until",line);
            addTokenQueue(tp);
        }
        else if(str.equals("var"))
        {
            TokenPair tp = new TokenPair("VAR","var",line);
            addTokenQueue(tp);
        }
        else if(str.equals("while"))
        {
            TokenPair tp = new TokenPair("WHILE","while",line);
            addTokenQueue(tp);
        }
        else if(str.equals("write"))
        {
            TokenPair tp = new TokenPair("WRITE","write",line);
            addTokenQueue(tp);
        }

        else if(str.equals("put"))
        {
            TokenPair tp = new TokenPair("PUT","put",line);
            addTokenQueue(tp);
        }
        else
        {
            TokenPair tp = new TokenPair("IDENTIFIER",str+"",line);
            addTokenQueue(tp);
        }

    }

    private void fsaDigit()
    {
        StringBuffer lexeme= new StringBuffer();
        boolean finished = false;
        int state = 0;

        while(!finished)
        {
            char c = inFilebuffer.charAt(filePointer);

            if (isWhiteSpace(c) || c == '+'||
                    c == '-' || c == '/' || c == '*' ||
                    c == '(' || c == ')' || c == '\n')
            {
                finished = true;
                break;
            }

            filePointer++;


            switch (state)
            {
                case 0:
                    if(c >= '0' && c <= '9')
                    {
                        lexeme.append(c);;
                        state = 1;
                        break;
                    }
                    else if(c == '.')
                    {
                        lexeme.append(c);
                        state = 2;
                        break;
                    }
//                    else if (c== 'e' || c == 'E')
//                    {
//                        lexeme.append(c);
//                        state = 3;
//                        break;
//                    }
                    else
                    {
                        finished = true;
                        break;
                    }

                case 1:
                    if(c >= '0' && c <= '9')
                    {
                        lexeme.append(c);
                        state = 1;
                        break;
                    }
                    else if(c== '.')
                    {
                        lexeme.append(c);
                        state = 2;
                        break;
                    }
//                    else if(c == 'e' || c == 'E')
//                    {
//                        lexeme.append(c);
//                        state = 3;
//                        break;
//                    }
                    else
                    {
                        finished = true;
                        break;
                    }

                case 2:
                    if(c >= '0' && c <= '9')
                    {
                        lexeme.append(c);
                        state = 2;
                        break;
                    }
//                    else if(c == 'e' || c == 'E')
//                    {
//                        lexeme.append(c);
//                        state = 3;
//                        break;
//                    }
                    else
                    {
                        finished = true;

                        break;
                    }

//                case 3:
//                    if(c >= '0' && c >= '9' || c == '+' || c == '-')
//                    {
//                        lexeme.append(c);
//                        state = 4;
//                        break;
//                    }
//                    else
//                    {
//                        finished = true;
//                        break;
//                    }
//
//                case 4 :
//                    if(c >= '0' && c >= '9')
//                    {
//                        lexeme.append(c);
//                        state = 4;
//                        break;
//                    }
//                    else
//                    {
//                        finished = true;
//                        break;
//                    }
            }
        }

        if (state == 0 || state == 3)
        {
            TokenPair tp = new TokenPair("MP_ERROR",lexeme.toString(),line);
            addTokenQueue(tp);
        }

        else if (state == 1)
        {
            TokenPair tp = new TokenPair("MP_INTEGER",lexeme.toString(),line);
            addTokenQueue(tp);
        }

        else if (state == 2)
        {
            TokenPair tp = new TokenPair("MP_FIXED",lexeme.toString(),line);
            addTokenQueue(tp);
        }
        if (state == 4)
        {
            TokenPair tp = new TokenPair("MP_REAL",lexeme.toString(),line);
            addTokenQueue(tp);
        }
    }

    private void fsaStringLit()
    {
        StringBuffer lexeme = new StringBuffer();
        boolean finished = false;
        int state = 0;

        while(!finished)
        {
            char c = inFilebuffer.charAt(filePointer);
            filePointer++;

            switch (state)
            {
                case 0:
                    if(c=='\'')
                    {
                        lexeme.append(c);
                        state = 1;
                    }
                    else
                    {
                        System.out.println("Debug: fsaStringLit : Unexpected start character.");
                        moveBackChar();
                        finished = true;

                    }
                    break;

                case 1:
                    if(c== '\'')
                    {
                        lexeme.append(c);
                        state = 2;
                    }
                    else if ((int)c == 10 || (int)c == 12 )
                    {
                        System.out.println("Debug: fsaStringLit : New Line found in String.");
                        moveBackChar();
                        finished = true;
                    }
                    else
                    {
                        lexeme.append(c);
                        state = 1;
                    }
                    break;

                case 2:
                    if(c == '\'' )
                    {
                        lexeme.append(c);
                        state =1;
                    }
                    else if(Character.isWhitespace(c))
                    {
                        finished = true;
                    }
                    else
                    {
                        lexeme.append(c);
                        state = 1;
                    }
                    break;

                case 3:
                    if (c == '\u00a0')
                    {
                        System.out.println("weired whitespace char");
                        finished = true;
                    }
            }

        }

        if (state == 1 || state == 0)
        {
            TokenPair tp = new TokenPair("MP_ERROR",lexeme.toString(),line);
            addTokenQueue(tp);
        }
        else if(state == 2)
        {
            TokenPair tp = new TokenPair("MP_STRING_LIT",lexeme.toString(),line);
            addTokenQueue(tp);
        }

    }

    private void fsaLThan() {

        StringBuffer lexeme = new StringBuffer();
        lexeme.append('<');

        filePointer++;
        char c = inFilebuffer.charAt(filePointer);
        if(c == '=')
        {
            lexeme.append('=');
            String toReturn = lexeme.toString();;
            TokenPair tp = new TokenPair("MP_LEQUAL",toReturn,line);
            addTokenQueue(tp);
            filePointer++;
        }
        else if(c == '>')
        {
            lexeme.append('>');
            TokenPair tp = new TokenPair("MP_NEQUAL",lexeme.toString(),line);
            addTokenQueue(tp);
            filePointer++;
        }
        else
        {
            TokenPair tp = new TokenPair("MP_LTHAN",lexeme.toString(),line);
            addTokenQueue(tp);
        }
    }

    private void fsaGThan() {
        StringBuffer lexeme= new StringBuffer();
        lexeme.append('>');
        filePointer++;

        char c = inFilebuffer.charAt(filePointer);
        if (c == '=')
        {
            lexeme.append('=');
            TokenPair tp =  new TokenPair("MP_GEQUAL",lexeme.toString(),line);
            addTokenQueue(tp);
            filePointer++;
        }
        else
        {
            TokenPair tp = new TokenPair("MP_GTHAN",lexeme.toString(),line);
            addTokenQueue(tp);
        }

    }

    private void fsaColon() {

        StringBuffer lexeme = new StringBuffer();
        boolean finsihed = false;
        int state = 0;

        while(!finsihed)
        {
            char c = inFilebuffer.charAt(filePointer);
            filePointer++;

            switch(state)
            {
                case 0:
                    if(c == ':')
                    {
                        lexeme.append(c);
                        state = 1;
                    }
                    else
                    {
                        System.out.println("Debug: fasColon unexpected Start Chartacter");
                        moveBackChar();
                        finsihed = true;
                    }
                    break;

                case 1:
                    if(c == '=')
                    {
                        lexeme.append(c);
                        state = 2;
                    }
                    else
                    {
                        moveBackChar();
                    }
                    finsihed = true;
                    break;
            }
        }
            if(state == 1)
            {
                TokenPair tp = new TokenPair("MP_COLON",":",line);
                addTokenQueue(tp);
            }
            else if(state == 2)
            {
                TokenPair tp = new TokenPair("MP_ASSIGN",":=",line);
                addTokenQueue(tp);
            }

    }

    private void skipComment() {
     boolean finished = false;
        filePointer++;

        while(!finished &&!(filePointer >= inFilebuffer.length()))
        {
         char inChar = inFilebuffer.charAt(filePointer);
            if(inChar == '\r')
            {
              if((filePointer) == inFilebuffer.length()
                        || inFilebuffer.charAt(filePointer) != '\n')
                {
                    line ++;
                    filePointer++;
                }
                else if(inChar == '\n')
                {
                    line++;;
                    filePointer++;
                }
                else
                {
                    if(inChar == '}')
                    {
                        finished = true;
                    }
                }
            }
        }
        if(!finished)
        {
            TokenPair tp = new TokenPair("COMMENT","",line);
            addTokenQueue(tp);
        }

    }

    private void moveBackChar() {
        filePointer--;
    }

    private boolean isWhiteSpace(char inChar) {
        return Character.isWhitespace(inChar);
    }

    private boolean isDigit(char inChar) {
        return Character.isDigit(inChar);
    }

    private boolean isLetter(char inChar)
    {
        return Character.isLetter(inChar);
    }

    private void fsaLetter()throws IndexOutOfBoundsException { // Derek

		StringBuffer lexeme = new StringBuffer();
		String str;
		boolean immediateUnderscore = false;
		boolean finished = false;
        char c = inFilebuffer.charAt(filePointer);

		lexeme.append(c);

		if (c == '_') {
			immediateUnderscore = true;
		}

		while (!finished) {
			filePointer++;
			c = inFilebuffer.charAt(filePointer);
			if (isWhiteSpace(c)) {
				// done, and break -- return pointer pointing at whitespace char
				if (immediateUnderscore) {
					System.out.println("Error: Ending an id in an underscore. Debug fsaletter()");
					finished = true;
					break;
				}
                finished = true;
                break;
            ///error Generating <code>
                //</code>
			}
            else if(c == '.')
            {
                if (immediateUnderscore) {
                    System.out.println("Error: Ending an id in an underscore. Debug fsaletter()");
                    finished = true;
                    break;
                }
                //check if next char is a number or not.
                    int nextPointer = filePointer;
                    nextPointer++;
                if(nextPointer < inFilebuffer.length()){
                    char d = inFilebuffer.charAt(nextPointer);
                    if (isDigit(d)) {
                        lexeme.append(c);
                        immediateUnderscore = false;

                        Log.d("TAG", "Reading Floating Points");
                        continue;
                    }
                }

                finished = true;
                break;
             }
            else
            { // not whitespace
				if (isLetter(c)) {
					lexeme.append(c);
					immediateUnderscore = false;
					continue;
				} else if (isDigit(c)) {
					lexeme.append(c);
					immediateUnderscore = false;
					continue;
				} else if (c == '_' && !immediateUnderscore) {
					lexeme.append(c);
					immediateUnderscore = true;
					continue;
				} else if (c == '_') { // if c is an underscore, and there was
										// an underscore before
					System.out.println("Error: Two underscores in input File");
					finished = true;
					break;
				} else {
				//end of token
					break;
				}
			}
		}
		// done, and our identifier string should be set up!
		// now to categorize it!!

		str = lexeme.toString();
        if(str.equals("and"))
        {
            TokenPair tp = new TokenPair("AND","and",line);
            addTokenQueue(tp);
        }
        else if(str.equals("start"))
        {
            TokenPair tp = new TokenPair("START","start",line);
            addTokenQueue(tp);
        }
        else if(str.equals("divide"))
        {
            TokenPair tp = new TokenPair("DIVIDE","divide",line);
            addTokenQueue(tp);
        }
        else if(str.equals("do"))
        {
            TokenPair tp = new TokenPair("DO","do",line);
            addTokenQueue(tp);
        }
        else if(str.equals("downto"))
        {
            TokenPair tp = new TokenPair("DOWNTO","downto",line);
            addTokenQueue(tp);
        }
        else if(str.equals("else"))
        {
            TokenPair tp = new TokenPair("ELSE","else",line);
            addTokenQueue(tp);
        }
        else if(str.equals("end"))
        {
            TokenPair tp = new TokenPair("END","end",line);
            addTokenQueue(tp);
        }
        else if(str.equals("fixed"))
        {
            TokenPair tp = new TokenPair("FIXED","fixed",line);
            addTokenQueue(tp);
        }
        else if(str.equals("real"))
        {
            TokenPair tp = new TokenPair("REAL","real",line);
            addTokenQueue(tp);
        }
        else if(str.equals("for"))
        {
            TokenPair tp = new TokenPair("FOR","for",line);
            addTokenQueue(tp);
        }
        else if(str.equals("function"))
        {
            TokenPair tp = new TokenPair("FUNCION","function",line);
            addTokenQueue(tp);
        }
        else if(str.equals("if"))
        {
            TokenPair tp = new TokenPair("IF","if",line);
            addTokenQueue(tp);
        }
        else if(str.equals("integer"))
        {
            TokenPair tp = new TokenPair("INTEGER","integer",line);
            addTokenQueue(tp);
        }
        else if(str.equals("mod"))
        {
            TokenPair tp = new TokenPair("MOD","mod",line);
            addTokenQueue(tp);
        }
        else if(str.equals("not"))
        {
            TokenPair tp = new TokenPair("MP_NOT","not",line);
            addTokenQueue(tp);
        }
        else if(str.equals("or"))
        {
            TokenPair tp = new TokenPair("OR","OR",line);
            addTokenQueue(tp);
        }
        else if(str.equals("procedure"))
        {
            TokenPair tp = new TokenPair("MP_PROCEDURE","procedure",line);
            addTokenQueue(tp);
        }
        else if(str.equals("program"))
        {
            TokenPair tp = new TokenPair("PROGRAM","program",line);
            addTokenQueue(tp);
        }
        else if(str.equals("read"))
        {
            TokenPair tp = new TokenPair("READ","read",line);
            addTokenQueue(tp);
        }
        else if(str.equals("repeat"))
        {
            TokenPair tp = new TokenPair("REPEAT","repeat",line);
            addTokenQueue(tp);
        }
        else if(str.equals("then"))
        {
            TokenPair tp = new TokenPair("THEN","then",line);
            addTokenQueue(tp);
        }
        else if(str.equals("to"))
        {
            TokenPair tp = new TokenPair("TO","to",line);
            addTokenQueue(tp);
        }
        else if(str.equals("until"))
        {
            TokenPair tp = new TokenPair("UNTIL","until",line);
            addTokenQueue(tp);
        }
        else if(str.equals("var"))
        {
            TokenPair tp = new TokenPair("VAR","var",line);
            addTokenQueue(tp);
        }
        else if(str.equals("while"))
        {
            TokenPair tp = new TokenPair("WHILE","while",line);
            addTokenQueue(tp);
        }
        else if(str.equals("write"))
        {
            TokenPair tp = new TokenPair("WRITE","write",line);
            addTokenQueue(tp);
        }

        else if(str.equals("put"))
        {
            TokenPair tp = new TokenPair("PUT","put",line);
            addTokenQueue(tp);
        }
        else
        {
            TokenPair tp = new TokenPair("IDENTIFIER",str+"",line);
            addTokenQueue(tp);
        }


    }

}
