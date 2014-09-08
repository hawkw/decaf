/**
 * File: windows.cc
 * ---------------
 * Extra functions needed for Windows compatibility
 */

/* This function is used to determine whether or not more files exist
 * to scan after one has been read.  This function says that no extra files
 * will be used.
 */
extern "C" int yywrap() {
	return 0;
}

/* This function from <unistd.h> is supposed to say whether a file descriptor
 * corresponds to the terminal or not.  We're going to say that everything
 * represents the terminal, which is a Totally Awful Hack but works for our
 * purposes.
 */
int isatty(int) {
	return 1;
}