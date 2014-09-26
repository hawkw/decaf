/* File:  scanner.l
 * ----------------
 * Lex input file to generate the scanner for the compiler.
 */

%{

/* The text within this first region delimited by %{ and %} is assumed to
 * be C/C++ code and will be copied verbatim to the lex.yy.c file ahead
 * of the definitions of the yylex() function. Add other header file inclusions
 * or C++ variable declarations/prototypes that are needed by your code here.
 */

#include <string.h>
#include "scanner.h"
#include "utility.h" // for PrintDebug()
#include "errors.h"
#include "parser.h"

/* Macro: YY_USER_ACTION
 * ---------------------
 * This flex built-in macro can be defined to provide an action which is
 * always executed prior to any matched rule's action. Basically, it is
 * a way of having a piece of code common to all actions factored out to
 * this routine.  We already defined it for you and left the empty
 * function DoBeforeEachAction ready for your use as needed. It will
 * be called once for each pattern scanned from the file, before
 * executing its action.
 */
static void DoBeforeEachAction();
#define YY_USER_ACTION DoBeforeEachAction();

%}

 /* The section before the first %% is the Definitions section of the lex
  * input file. Here is where you set options for the scanner, define lex
  * states, and can set up definitions to give names to regular expressions
  * as a simple substitution mechanism that allows for more readable
  * entries in the Rules section later.
  */

/* Comments */
LINECOMMENT   \/\/.*$
OPBK          \/\*
CLBK          \*\/
NOTSTAR       [^\*\n]
NOTFWSL       [^\/\n]
NOTSTAROR     [^\*\/\n]
LINE          (({NOTSTAROR}*|{NOTSTAR}{NOTFWSL})+)
%x            BLOCKCOMMENT


STRTERM       [\"\']
NOTSTRTERM    [^\"\'\n]
/* STRINGLIT     {STRTERM}({NOTSTRTERM}|\n)*{STRTERM} // This is disabled because it's too good*/
STRINGLIT      {STRTERM}{NOTSTRTERM}*{STRTERM}?

/* Chartypes */
DIGITS                  [0-9]+
HEX_DIGITS              [0-9a-fA-F]+
IDENTIFIER              [a-zA-Z][a-zA-Z0-9\_]*

/* Constants */
FLOAT                   {DIGITS}\.{DIGITS}?
DOUBLE_CONST            {FLOAT}|{FLOAT}E\+{DIGITS}
INT_CONST               {DIGITS}|0[xX]{HEX_DIGITS}
STRING_CONST            \"[^\n\"]\"
CONSTANT                {INT_CONST}|{DOUBLE_CONST}|{BOOL_CONST}|{STRING_CONST}|null
TYPE_LITERALS           int|double|string|ident
TYPE                    {TYPE_LITERALS}|{TYPE_LITERALS}\[\]


%%             /* BEGIN RULES SECTION */
 /* All patterns and actions should be placed between the start and stop
  * %% markers which delimit the Rules section.
  */

 /* -------------------- Comments ----------------------------- */

{LINECOMMENT}
{OPBK}                              BEGIN(BLOCKCOMMENT);
<BLOCKCOMMENT>{CLBK}                BEGIN(INITIAL);
<BLOCKCOMMENT><<EOF>>               ReportError::UntermComment();
<BLOCKCOMMENT>\n|\r\n                     %{
    yylloc.first_column = 0;
    yylloc.last_column = 0;
    yylloc.first_line += 1;
    yylloc.last_line = yylloc.first_line;
%}
 /* -------------------- Whitespace ----------------------------- */


\n|\r\n                     %{
    yylloc.first_column = 0;
    yylloc.last_column = 0;
    yylloc.first_line += 1;
    yylloc.last_line = yylloc.first_line;
%}
[\ ]                    %{
    //printf("space %i\n", yylloc.first_column);
%}

     /* Tab length is 8, right? */
[\t]                    %{ yylloc.last_column += 8; %}

 /* --------------------- Keywords ------------------------------- */
"void"              { return T_Void;        }
"int"               { return T_Int;         }
"double"            { return T_Double;      }
"bool"              { return T_Bool;        }
"string"            { return T_String;      }
"null"              { return T_Null;        }
"class"             { return T_Class;       }
"extends"           { return T_Extends;     }
"this"              { return T_This;        }
"interface"         { return T_Interface;   }
"implements"        { return T_Implements;  }
"while"             { return T_While;       }
"for"               { return T_For;         }
"if"                { return T_If;          }
"else"              { return T_Else;        }
"return"            { return T_Return;      }
"break"             { return T_Break;       }
"new"               { return T_New;         }
"NewArray"          { return T_NewArray;    }
"Print"             { return T_Print;       }
"ReadInteger"       { return T_ReadInteger; }
"ReadLine"          { return T_ReadLine;    }

 /* -------------------- Operators ----------------------------- */
"<="                { return T_LessEqual;   }
">="                { return T_GreaterEqual;}
"=="                { return T_Equal;       }
"!="                { return T_NotEqual;    }
"&&"                { return T_And;         }
"||"                { return T_Or;          }
"[]"                { return T_Dims;        }

\(                      return (int)'(';
\)                      return (int)')';
\;                      return (int)';';
\=                      return (int)'=';
\*                      return (int)'*';
\<                      return (int)'<';
\>                      return (int)'>';
\{                      return (int)'{';
\}                      return (int)'}';
-                       return (int)'-';
\+                      return (int)'+';
\!                      return (int)'!';
\/                      return (int)'/';
\.                      return (int)'.';

 /* -------------------- Constants ------------------------------ */
"true"|"false"      { yylval.boolConstant = (yytext[0] == 't');
                         return T_BoolConstant; }
{INT_CONST} %{
    sscanf(yytext, "%i", &yylval.integerConstant);
    return T_IntConstant;
%}
{DOUBLE_CONST} %{
    sscanf(yytext, "%d", &yylval.integerConstant);
    return T_IntConstant;
}%

{STRINGLIT} %{
    yylval.stringConstant = yytext;
    yylloc.last_column += (yyleng);

    if (yytext[yyleng-2] =='\'' || yytext[yyleng-2] == '"')  {
        return T_StringConstant;
    } else if (yytext[yyleng-1] =='\'' || yytext[yyleng-1] == '"') {
        return T_StringConstant;
    } else { ReportError::UntermString(&yylloc, yytext);}

%}

 /* -------------------- Identifiers --------------------------- */
{IDENTIFIER} %{
    int i;
    // Check to see if the identifer is too long, if so, raise a warning
    if (strlen(yytext) > MaxIdentLen) {
        ReportError::LongIdentifier(&yylloc, yytext);
    }
    // Automatically truncate yytext if it is too long
    for(i = 0; i < MaxIdentLen; i++) {
        if(yytext[i] == '\0') break;
        yylval.identifier[i] = yytext[i];
    }
    yylval.identifier[i] = '\0';
    yylloc.last_column += i - 1;
    return T_Identifier;
%}

 /* -------------------- Unrecognized character --------------- */
.           %{
    ReportError::UnrecogChar(&yylloc, yytext[0]);
%}

%%
/* The closing %% above marks the end of the Rules section and the beginning
 * of the User Subroutines section. All text from here to the end of the
 * file is copied verbatim to the end of the generated lex.yy.c file.
 * This section is where you put definitions of helper functions.
 */


/* Function: InitScanner
 * ---------------------
 * This function will be called before any calls to yylex().  It is designed
 * to give you an opportunity to do anything that must be done to initialize
 * the scanner (set global variables, configure starting state, etc.). One
 * thing it already does for you is assign the value of the global variable
 * yy_flex_debug that controls whether flex prints debugging information
 * about each token and what rule was matched. If set to false, no information
 * is printed. Setting it to true will give you a running trail that might
 * be helpful when debugging your scanner. Please be sure the variable is
 * set to false when submitting your final version.
 */
void InitScanner()
{
    PrintDebug("lex", "Initializing scanner");
    yy_flex_debug = false;
    yylloc.first_line = 1;
    yylloc.first_column = 0;
    yylloc.last_line = 1;
    yylloc.last_column = 0;
}


/* Function: DoBeforeEachAction()
 * ------------------------------
 * This function is installed as the YY_USER_ACTION. This is a place
 * to group code common to all actions.
 */
static void DoBeforeEachAction()
{
    yylloc.first_column = yylloc.last_column;
    yylloc.first_line = yylloc.last_line;
    yylloc.first_column += 1;
    yylloc.last_column += 1;
}

/* Function: GetLineNumbered()
 * ---------------------------
 * Returns string with contents of line numbered n or NULL if the
 * contents of that line are not available.  The scanner that we will
 * release later on will have this implemented for you; for now, feel
 * free to use this stub implementation.
 */
const char *GetLineNumbered(int num) {
  return NULL;
}

static void error(char* string) {
    printf("*** Error line %d\n", yylloc.last_line);
    printf("*** %s", string);
}
