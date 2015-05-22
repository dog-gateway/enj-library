package it.polito.elite.enocean.examples.util;

import java.io.PrintStream;
import java.util.StringTokenizer;

/**  The sandmark.util.Options class parses command-line options according to
 *  the Unix standard (POSIX .2 section 2.10.2) utility syntax guidelines.
 *  While the end-user sees behavior consistent with the C getopt()
 *  function, the programming interface is completely different.
 * @author Gregg Townsend, University of Arizona
 * @version February, 2001
 * @since <i>Included since 20/11/2003</i>
 */

public class Options {

    private int nspecs;		// number of specs, equals size of arrays:
    private String[] key;	// specification keys
    private String[] var;	// variable names; null for non-valued 
    private String[] dflt;	// defaults; null if not specified
    private String[] descr;	// descriptions
    private String[] value;	// specified value

    private String header;	// header for usage message
    private String footer;	// footer for usage message.

    private StringBuffer seen;	// option letters seen during parsing
    private int firstpos;	// index of first positional argument



/**
 *  Constructs an Options object and parses a list of arguments.
 *  If a usage error is detected, the program is aborted with a message.
 *  After parsing, option values are found by calling {@link #getValue}
 *  and positional arguments are found by calling {@link #getIndex}.
 *  <P>
 *  @param speclist  parameter specifications (see {@link #Options(String[])})
 *  @param cmdname   command name, for usage message if needed
 *  @param arglist   argument list (see {@link #parse})
 */
public Options(String[] speclist, String cmdname, String[] arglist) {
    this(speclist);
    try {
	parse(arglist);
    } catch (Exception e) {
	String s = e.getMessage();
	if (s != null && ! s.endsWith(" -?")) {
	    System.err.println(s);
	}
	usage(System.err, cmdname);
	System.exit(1);
    }
}



/**  Constructs an Options object for parsing an argument list.
 *  The single argument is a list of strings, which are interpreted
 *  in pairs; thus the length of the argument array should be even.
 *  In each pair, the first string specifies argument and the second
 *  provides a very brief description for a usage message.
 *  There are three types of argument specifications.
 *
 *  <P> A command option that does not take an argument is specified
 *  by a two-character string, a hyphen followed by the option letter
 *  (or, rarely, other character).
 *
 *  <P> A command option that takes an argument is specified by a string
 *  consisting of: <UL>
 * 	<LI> a hyphen followed by the option letter and a space
 * 	<LI> a variable name characterizing the option value
 * 	<LI> optionally, another space and a default value
 *  </UL>
 *
 *  <P> A positional argument is specified by a variable name that
 *  does not begin with a hyphen.  Positional arguments are used
 *  only for generating usage messages, and are not interpreted.
 *  All positional arguments must follow all command options.
 *
 *  <P> Here is a somewhat contrived specification example:
 *  <PRE>
 *  Options o = new Options(new String[] {
 * 	"-d",		"delete afterwards",
 * 	"-n nproc 1",	"use N parallel processes",	// 1 is default
 * 	"-p",		"preserve dates and times",
 * 	"-v",		"provide verbose commentary",
 * 	"src",		"data source",
 * 	"...",		"",
 * 	"dst",		"destination",
 *  });
 *  </PRE>
 *
 *  <P>
 * @param speclist The list of supported options, in the POSIX format, "-option","option
 * description"
 */
public Options(String[] speclist) {

    if (speclist == null || speclist.length % 2 == 1) {
	throw new Error("invalid specification list");
    }
    nspecs = speclist.length / 2;
    key = new String[nspecs];
    var = new String[nspecs];
    dflt = new String[nspecs];
    descr = new String[nspecs];
    value = new String[nspecs];

    boolean inoptions = true;
    for (int i = 0; i < nspecs; i++) {
	StringTokenizer tk = new StringTokenizer(speclist[2*i]);
	String name = tk.nextToken();
	key[i] = name;
	if (name.startsWith("-")) {
	    if (! inoptions) {
		throw new Error("positional argument amidst options");
	    }
	    if (name.length() != 2) {
		throw new Error("multiple-character option " + name);
	    }
	    if (tk.hasMoreElements()) {
		var[i] = tk.nextToken();
	    }
	    if (tk.hasMoreElements()) {
		dflt[i] = tk.nextToken();
	    }
	} else {
	    inoptions = false;
	}
	descr[i] = speclist[2*i+1];
    }

    seen = new StringBuffer();
}



/**  Defines a header string to be output at the start of any usage message,
 *  such as a string announcing the full program name and version number.
 *  <P>
 * @see #usage
 * @param s The header string
 */
public void setHeader(String s) {
    header = s;
}



/** Defines a footer string to be output at the end of any usage message,
 *  such as a copyright notice.
 *
 *  <P>
 * @see #usage
 * @param s The footer string
 */
public void setFooter(String s) {
    footer = s;
}



/**  Synthesizes a usage message and writes it to the specified stream.
 *  The usage message is composed of:  <UL>
 * 	<LI> a header, if one was set by {@link #setHeader}
 * 	<LI> "usage:  cmdname [options] positionals"
 * 	<LI> a list of options, omitting any having no description
 * 	<LI> a footer, if one was set by {@link #setFooter}
 *  </UL>
 * @param p PrintStream to which send the output message
 * @param cmdname Name of the class using this utility (command issued by the user, in a console)
 */
public void usage(PrintStream p, String cmdname) {

    // print header
    if (header != null) {
	p.println(header);			// header
    }

    // print synopsis
    p.print("usage:  " + cmdname);		// usage: cmdname
    if (nspecs > 0 && key[0].startsWith("-")) {
	p.print(" [options]");			// [options]
    }

    for (int i = 0; i < nspecs; i++) {
	if (! key[i].startsWith("-")) {
	    p.print(" " + key[i]);		// positional parameters
	}
    }
    p.println();				// terminate synopsis

    // calculate width needed for first column of option list
    int w = 0;
    for (int i = 0; i < nspecs; i++) {
	int kwid = key[i].length();
	if (var[i] != null) {
	    kwid += 1 + var[i].length();
	}
	w = Math.max(w, kwid);
    }

    // print option list
    for (int i = 0; i < nspecs; i++) {
	if (descr[i] == null || descr[i].length() == 0) {
	    continue;
	}
	StringBuffer b = new StringBuffer("  ");
	b.append(key[i]);
	if (var[i] != null) {
	    b.append(' ').append(var[i]);
	}
	for (int j = b.length(); j < w + 4; j++) {
	    b.append(' ');
	}
	b.append(descr[i]);
	p.println(b.toString());
    }

    // print footer
    if (header != null) {
	p.println(footer);
    }
}



/** Parses an argument list and returns the index of the first
 *  non-option argument.
 *  This is an argument that is not an option value, and that
 *  does not begin with "-", or is exactly "-", or follows "--".
 *
 *  <P> After parsing, option values can be retrieved by calling
 *  {@link #getValue}.
 *
 *  <P> An Exception is thrown, with an explanatory message, if an
 *  invalid argument is found.
 * @param args main argument list
 * @throws Exception thrown when an invalid argument is found
 * @return the index of the first non-option argument
 */
public int parse(String[] args) throws Exception {

    seen.setLength(0);
    value = new String[nspecs];

    for (int i = 0; i < args.length; i++) {
	String s = args[i];
	if (! s.startsWith("-") || s.equals("-")) {
	    return firstpos = i;
	}
	if (s.equals("--")) {
	    return firstpos = i + 1;
	}
	for (int j = 1; j < s.length(); j++) {
	    char c = s.charAt(j);
	    seen.append(c);
	    int k = findKey(c);
	    if (k < 0) {
		throw new Exception("no such option: -" + c);
	    }
	    if (var[k] == null) {
		// this option takes no argument
		value[k] = "";
	    } else {
		// this option takes an argument
		if (j + 1 < s.length()) {
		    value[k] = s.substring(j + 1);
		    break;
		} else if (++i < args.length) {
		    value[k] = args[i];
		    break;
		} else {
		    throw new Exception(
			"option -" + c + " requires an argument");
		}
	    }
	}
    }
    return firstpos = args.length;
}



/**  Retrieves the value specified for the given option letter
 *  in the most recently parsed argument list.
 *
 *  <P> For an option that accepts an argument, the argument
 *  value is returned.  If no value was specified, the default
 *  value is returned.  If no default was specified, the null
 *  value is returned.
 *
 *  <P> For an option that does not accept an argument, the
 *  empty string is returned if the option was invoked, and
 *  the null value is returned if not.
 * @param c The argument for which a value must be extracted
 * @return The value corresponding to the argumet c or the default one if c is not followed
 * by a value. If the default value has not been specified returns null.
 */
public String getValue(char c) {
    int i = findKey(c);
    if (i < 0) {
	throw new Error("no such option: -" + c);
    }
    return (value[i] != null) ? value[i] : dflt[i];
}

private int findKey(char c) {
    String s = "-" + c;
    for (int i = 0; i < nspecs; i++) {
	if (s.equals(key[i])) {
	    return i;
	}
    }
    return -1;
}



/**  Returns the index of the first positional argument in the
 *  most recently parsed argument list.
 *  This is an argument that is not an option value, and that
 *  does not begin with "-", or is exactly "-", or follows "--".
 *  If no argument list has been parsed, or if an error was detected,
 *  the return value is indeterminate.
 * @return   returns the index of the first positional argument in the
 *  most recently parsed argument list.
 *  This is an argument that is not an option value, and that
 *  does not begin with "-", or is exactly "-", or follows "--".
 *  If no argument list has been parsed, or if an error was detected,
 *  the return value is indeterminate.
 */
public int getIndex() {
    return firstpos;
}



/**  Returns a string containing the option letters specified in the
 *  most recently parsed argument list.
 *  Letters appear in the order seen, and duplicates may be present.
 *  If no argument list has been parsed, or if an error was detected,
 *  the return value is indeterminate.
 * @return   returns a string containing the option letters specified in the
 *  most recently parsed argument list.
 *  Letters appear in the order seen, and duplicates may be present.
 *  If no argument list has been parsed, or if an error was detected,
 *  the return value is indeterminate.
 */
public String getWhich() {
    return seen.toString();
}



//------------------------------------------------------------



/**  Tests the Options code.  When run with arguments, parses them using
 *  a predefined options specification.  When run with no arguments,
 *  executes a canned test set.
 * @param args arguments passed to the Option class to be managed.
 */

public static void main(String args[]) {

    String cmdname = "java OptionsTester";
    String[] speclist = new String[] {
	"-d",		"delete afterwards",
	"-f ifile",	"read input from ifile",
	"-n nproc 1",	"use N parallel processes",	// 1 is default
	"-p",		"preserve dates and times",
	"-v",		"provide verbose commentary",
	"-x nnn",	"horizontal position",
	"-y nnn",	"vertical position",
	"src",		"data source",
	"...",		"",
	"dst",		"destination",
    };

    Options o = new Options(speclist, cmdname, args);
    if (args.length > 0) {
	dump(o, args);
    } else {
	o.setHeader("MyProgram Deluxe version 1.3.2 (A-43F-q7)"); 
	o.setFooter("So long, and thanks for all the fish."); 
	o.usage(System.out, cmdname);
	test(o, "");
	test(o, "a b c");
	test(o, "- a b c");
	test(o, "-- -a -b -c");
	test(o, "-d -n 12 -v");
	test(o, "-d -n12 -v -- qrst");
	test(o, "-dn12 -v - mnop");
	test(o, "-dpv -f somefile -x111 -y222 -vn2 src1 src2 src3 destn");
	test(o, "-d -v -p -!");
	test(o, "-d -p -v -n");
    }
}



private static void test(Options o, String cmdline) {
    System.out.println();
    StringTokenizer tk = new StringTokenizer(cmdline);
    int n = tk.countTokens();
    String[] arglist = new String[n];
    for (int i = 0; i < n; i++) {
	arglist[i] = tk.nextToken();
    }
    try {
	o.parse(arglist);
	dump(o, arglist);
    } catch (Exception e) {
	System.out.println(cmdline);
	System.out.println(e);
    }
}



private static void dump(Options o, String[] arglist) {
    System.out.print("[i=" + o.getIndex() + "]:");
    for (int i = 0; i < arglist.length; i++) {
	System.out.print(" " + arglist[i]);
    }
    System.out.println();
    String s = o.getWhich();
    for (int i = 0; i < s.length(); i++) {
	char c = s.charAt(i);
	System.out.println("  -" + c + " " + o.getValue(c));
    }
    System.out.println("[ -f " + o.getValue('f') + " ]");
    System.out.println("[ -n " + o.getValue('n') + " ]");
}



} // class Options
