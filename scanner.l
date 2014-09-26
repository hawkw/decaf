/* File:  scanner.l
 * ----------------
 * Lex input file to generate the scanner for the compiler.
 */

%{

#include <string.h>
#include "scanner.h"
#include "utility.h" // for PrintDebug()
#include "errors.h"
#include "parser.h" // for token codes, yylval
#include <vector>
using namespace std;

#define TAB_SIZE 8

/* Global variables
 * ----------------
 * (For shame!) But we need a few to keep track of things that are
 * preserved between calls to yylex or used outside the scanner.
 */
static int curLineNum, curColNum;
vector<const char*> savedLines;

static void DoBeforeEachAction(); 
#define YY_USER_ACTION DoBeforeEachAction();

%}

/* States
 * ------
 * A little wrinkle on states is the COPY exclusive state which
 * I added to first match each line and copy it ot the list of lines
 * read before re-processing it. This allows us to print the entire
 * line later to provide context on errors.
 */
%s N
%x COPY COMM
%option stack

/* Definitions
 * -----------
 * To make our rules more readable, we establish some definitions here.
 */
DIGIT             ([0-9])
HEX_DIGIT         ([0-9a-fA-F])
HEX_INTEGER       (0[Xx]{HEX_DIGIT}+)
INTEGER           ({DIGIT}+)
EXPONENT          ([Ee][-+]?{INTEGER})
DOUBLE            ({INTEGER}"."{DIGIT}*{EXPONENT}?)
BEG_STRING        (\"[^"\n]*)
STRING            ({BEG_STRING}\")
IDENTIFIER        ([a-zA-Z][a-zA-Z_0-9]*)
OPERATOR          ([-+/*%=.,;!<>()[\]{}])
BEG_COMMENT       ("/*")
END_COMMENT       ("*/")
SINGLE_COMMENT    ("//"[^\n]*)

%%             /* BEGIN RULES SECTION */

<COPY>.*               { char curLine[512];
                         //strncpy(curLine, yytext, sizeof(curLine));
                         savedLines.push_back(strdup(yytext));
                         curColNum = 1; yy_pop_state(); yyless(0); }
<COPY><<EOF>>          { yy_pop_state(); }
<*>\n                  { curLineNum++; curColNum = 1;
                         if (YYSTATE == COPY) savedLines.push_back("");
                         else yy_push_state(COPY); }

[ ]+                   { /* ignore all spaces */  }
<*>[\t]                { curColNum += TAB_SIZE - curColNum%TAB_SIZE + 1; }

 /* -------------------- Comments ----------------------------- */
{BEG_COMMENT}          { BEGIN(COMM); }
<COMM>{END_COMMENT}    { BEGIN(N); }
<COMM><<EOF>>          { ReportError::UntermComment();
                         return 0; }
<COMM>.                { /* ignore everything else that doesn't match */ }
{SINGLE_COMMENT}       { /* skip to end of line for // comment */ }


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
{OPERATOR}          { return yytext[0];     }

 /* -------------------- Constants ------------------------------ */
"true"|"false"      { yylval.boolConstant = (yytext[0] == 't');
                         return T_BoolConstant; }
{INTEGER}           { yylval.integerConstant = strtol(yytext, NULL, 10);
                         return T_IntConstant; }
{HEX_INTEGER}       { yylval.integerConstant = strtol(yytext, NULL, 16);
                         return T_IntConstant; }
{DOUBLE}            { yylval.doubleConstant = atof(yytext);
                         return T_DoubleConstant; }
{STRING}            { yylval.stringConstant = strdup(yytext); 
                         return T_StringConstant; }
{BEG_STRING}        { ReportError::UntermString(&yylloc, yytext); }


 /* -------------------- Identifiers --------------------------- */
{IDENTIFIER}        { if (strlen(yytext) > MaxIdentLen)
                         ReportError::LongIdentifier(&yylloc, yytext);
                       strncpy(yylval.identifier, yytext, MaxIdentLen);
                       yylval.identifier[MaxIdentLen] = '\0';
                       return T_Identifier; }


 /* -------------------- Default rule (error) -------------------- */
.                   { ReportError::UnrecogChar(&yylloc, yytext[0]); }

%%


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
    BEGIN(N);
    yy_push_state(COPY); // copy first line at start
    curLineNum = 1;
    curColNum = 1;
}


/* Function: DoBeforeEachAction()
 * ------------------------------
 * This function is installed as the YY_USER_ACTION. This is a place
 * to group code common to all actions.
 * On each match, we fill in the fields to record its location and
 * update our column counter.
 */
static void DoBeforeEachAction()
{
   yylloc.first_line = curLineNum;
   yylloc.first_column = curColNum;
   yylloc.last_column = curColNum + yyleng - 1;
   curColNum += yyleng;
}

/* Function: GetLineNumbered()
 * ---------------------------
 * Returns string with contents of line numbered n or NULL if the
 * contents of that line are not available.  Our scanner copies
 * each line scanned and appends each to a list so we can later
 * retrieve them to report the context for errors.
 */
const char *GetLineNumbered(int num) {
   if (num <= 0 || num > savedLines.size()) return NULL;
   return savedLines[num-1]; 
}


