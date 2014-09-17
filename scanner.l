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

/* Global variable: yylval
 * -----------------------
 * This global variable is how we get attribute information about the token
 * just scanned to the client. The scanner sets the global variable
 * appropriately and since it's global the client can just read it.  In the
 * future, this variable will be declared for us in the y.tab.c file
 * produced by Yacc, but for now, we declare it manually.
 */
YYSTYPE yylval;  // manually declared for pp1, later Yacc provides

/* Global variable: yylloc
 * -----------------------
 * This global variable is how we get position information about the token
 * just scanned to the client. (Operates similarly to yylval above)
 */
struct yyltype yylloc; // manually dclared for pp1, later Yacc provides

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
BLOCKCOMMENT  {OPBK}.*{CLBK}|{OPBK}\**({LINE}\n{0,1})+\**{CLBK}

STRTERM       [\"\']
NOTSTRTERM    [^\"\'\n]
STRINGLIT     {STRTERM}({NOTSTRTERM}|\n)*{STRTERM}

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

/* Keywords "*/
VOID                    void
INT                     int
DOUBLE                  double
BOOL                    bool
STRING                  string
CLASS                   class
INTERFACE               interface
NULL                    null


%%             /* BEGIN RULES SECTION */
 /* All patterns and actions should be placed between the start and stop
  * %% markers which delimit the Rules section.
  */

{LINECOMMENT}
{BLOCKCOMMENT} %{
    int j;
    for (j = 0; j < strlen(yytext); j++){
        if(yytext[j] == '\n')  {
            yylloc.first_line += 1;
            yylloc.last_line = yylloc.first_line;
        }
    }
%}

{STRINGLIT} %{
    yylval.stringConstant = strdup(yytext);
    yylloc.last_column += (strlen(yytext)-1);
    if (yytext[strlen(yytext)] != '\'' && yytext[strlen(yytext)] != '"')
        ReportError::UntermString(yylloc, yytext);
    // TODO: add support for multiple line string literals
    return T_StringConstant;
%}

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

void                    %{
    yylloc.last_column += 3;
    return T_Void;
%}
int                     %{
    yylloc.last_column += 2;
    return T_Int;
%}
double                  %{
    yylloc.last_column += 5;
    return T_Double;
%}
\(                      %{
    return (int)'(';
%}
\)                      %{
    return (int)')';
%}
\;                      %{
    return (int)';';
%}
\=                      return (int)'=';
\*                      return (int)'*';
\<                      return (int)'<';
\>                      return (int)'>';
\{                      %{
    return (int)'{';
%}
\}                      return (int)'}';
-                       return (int)'-';
\+                      return (int)'+';
\!                      return (int)'!';
bool                    return T_Bool;
string                  return T_String;
class                   return T_Class;
interface               return T_Interface;
null                    return T_Null;
\[\]                    return T_Dims;
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
{INT_CONST} %{
    sscanf(yytext, "%i", &yylval.integerConstant);
    return T_IntConstant;
%}

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

static void error(char* string) {
    printf("*** Error line %d\n", yylloc.last_line);
    printf("*** %s", string);
}