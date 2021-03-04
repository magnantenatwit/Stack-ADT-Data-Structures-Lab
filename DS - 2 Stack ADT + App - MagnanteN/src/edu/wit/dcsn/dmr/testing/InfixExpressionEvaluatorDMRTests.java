/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Stack Application
 * Fall, 2020
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post nor otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.dcsn.dmr.testing ;

import edu.wit.dcsn.comp2000.stack.app.InfixExpressionEvaluator ;
//import edu.wit.dcsn.comp2000.stack.app.InfixExpressionEvaluatorMultiDigit ;
//import edu.wit.dcsn.comp2000.stack.app.InfixExpressionEvaluatorSingleDigit ;
import edu.wit.dcsn.dmr.testing.junit.ExitException ;
import edu.wit.dcsn.dmr.testing.junit.NoExitSecurityManager ;
import edu.wit.dcsn.dmr.testing.junit.TestingBase ;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.io.PrintStream ;
import java.nio.file.Files ;
import java.nio.file.Path ;
import java.util.Calendar ;
import java.util.Scanner ;

/**
 * Test driver for InfixExpressionEvaluator.
 * <p>
 * Standard (minimum): Support for valid expressions containing:
 * <ul>
 * <li>single-digit, unsigned, decimal operands
 * <li>operators: {@code +, -, *, /}
 * <li>parenthesized subexpressions, including nested parentheses
 * <li>spaces, which are ignored
 * </ul>
 * and invalid expressions containing:
 * <ul>
 * <li>invalid characters
 * </ul>
 * <p>
 * Optional: Support for valid expressions containing all of the above plus:
 * <ul>
 * <li>multi-digit unsigned decimal operands.
 * </ul>
 * <p>
 * Optional: Support for valid expressions containing all of the above plus:
 * <ul>
 * <li>unbalanced parentheses
 * <li>multiple consecutive operators
 * <li>division by zero
 * <li>consecutive operands
 * </ul>
 *
 * @author Dave Rosenberg
 * @version 1.0.0 2019-02-08 initial implementation
 * @version 2.0.0 2019-06-08
 *     <ul>
 *     <li>add support for comments in expression files
 *     <li>fix error in calculation of incorrect results
 *     <li>revise output format to resemble JUnit tests
 *     <li>fix error in recognition of errors distinguishing valid/invalid expression
 *     evaluation
 *     <li>consolidate all testing into a single run
 *     <ul>
 *     <li>recognize single-digit, multi-digit, and invalid expressions
 *     <li>separate counts for each expression category
 *     <li>enhance reporting per category
 *     <li>calculate %-age correct by category
 *     </ul>
 *     </ul>
 * @version 2.0.1 2019-06-22 minor fix to comment
 * @version 2.1.0 2019-10-13 remove unused import; reverse order of equals() test to
 *     avoid NullPointerException
 * @version 2.1.1 2019-10-15 update to next test data file version
 * @version 2.2.0 2020-02-17 cleanup toward DRCo coding standard compliance
 * @version 2.3.0 2020-02-21 fix missing 'incorrect' message
 * @version 2.4.0 2020-10-07
 *     <ul>
 *     <li>minor revisions to reflect changes to assignment wrt invalid expressions
 *     <li>redirect output to detailed log file
 *     </ul>
 */
public class InfixExpressionEvaluatorDMRTests extends TestingBase
    {

    /**
     * prevent instantiation
     */
    private InfixExpressionEvaluatorDMRTests()
        {
        // can't instantiate an InfixExpressionEvaluatorDMRTests
        
        }	// end constructor


    /**
     * Test driver for the InfixExpressionEvaluator's evaluate()
     * 
     * @param args
     *     -unused-
     * 
     * @throws FileNotFoundException
     *     if the expressions file can't be opened
     */
    public static void main( final String[] args ) throws FileNotFoundException
        {
        /*
         * set up detailed logging
         */
        // by default, send detailed log entries to the standard output
        detailedLogStream =             System.out ;

        // create the detailed log - name is TestClass-yyyy-mm-dd-hhmmss.log
        Calendar now =                  Calendar.getInstance() ;
        String timestamp =              String.format( "%TF-%<TH%<TM%<TS",
                                                       now
                                                       ) ;

        Path testLogsPath =     new File( "./test-logs" ).toPath().toAbsolutePath().normalize() ;
        
        String outputFilename = String.format( "%s%c%s-%s.log",
                                               testLogsPath,
                                               File.separatorChar,
                                               InfixExpressionEvaluatorDMRTests.class.getSimpleName(),
                                               timestamp ) ;
        
        try
            {
            testLogsPath =
                Files.createDirectories( testLogsPath ) ;
            
            detailedLogStream =         new PrintStream( outputFilename ) ;
            writeConsole( "Detailed log in: %s%n%n",
                          outputFilename ) ;
            }
        catch ( FileNotFoundException e )
            {
            writeSyserr( "Unable to create log file: %s%n\t%s%n\tusing System.out%n",
                         e.getMessage(),
                         outputFilename );
            }
        catch ( IOException e )
            {
            writeSyserr( "Unable to create log folder: %s%n\t%s%n\tusing System.out%n",
                         e.getMessage(),
                         testLogsPath );
            }

        
        
        // convenience variable
        final String expressionsFilename =
                        "./data/Infix Expressions -- DMR -- 2020-10-07 1106.dat" ;

        // counters for statistics reporting
        int lineCount = 0 ;

//        // indices into xxxCounts arrays
//        final int SINGLE_DIGIT_EXPRESSION = 0 ;
//        final int MULTI_DIGIT_EXPRESSION = 1 ;
//        final int INVALID_EXPRESSION = 2 ;
        final int[] expressionCounts = { 0, 0, 0 } ;
        final int[] correctResultCounts = { 0, 0, 0 } ;
        final String[] expressionTypes = { "single-digit", 
                                           "multi-digit",
                                           "invalid" } ;

        int expressionType = -1 ;

        /*
         * prevent System.exit() from terminating tests
         */
        // save the current security manager
        final SecurityManager savedSecurityManager = System.getSecurityManager() ;

        // enable ours
        System.setSecurityManager( new NoExitSecurityManager() ) ;

        // these declarations are outside try{} so they're available to the catch{}
        String fullLine = null ;
        String messagePrefix = "[]" ;
        
        /*
         * evaluate all expressions in the data file
         */
        try ( Scanner expressions = new Scanner( new File( expressionsFilename ) ) )
            {
            while ( expressions.hasNextLine() )
                {
                // get an expression from the file
                fullLine = expressions.nextLine() ;

                lineCount++ ;

                final String[] fullLineParts = fullLine.split( "#" ) ;

                // only evaluate the expression if there is one
                if ( ( fullLineParts.length > 0 ) &&
                     ( fullLineParts[ 0 ].length() > 0 ) )
                    {
                    /* @formatter:off
                     * 
                     * expression lines are formatted as: 
                     *  {t}: {expression} = {expected result}
                     * where
                     *  {t} is a single-digit representing the expression type
                     *  {expression} is the expression to evaluate
                     *  {expected result} is the correct response from
                     *      InfixExpressionEvaluator.evaluate()
                     * 
                     * @formatter:on
                     */
                    expressionType = fullLineParts[ 0 ].charAt( 0 ) - '0' ;
                    expressionCounts[ expressionType ]++ ;

                    // extract the expression and the expected result
                    final String[] expressionParts = fullLineParts[ 0 ].substring( 3 )
                                                                       .split( "=" ) ;

                    final String expression = expressionParts[ 0 ].trim() ;
                    final String expressionResult = expressionParts[ 1 ].trim() ;

                    boolean expressionIsValid = true ;

                    long expectedResult ;
                    long actualResult ;

                    // if we can convert the info to the right of the '=' to a value,
                    // the expression is valid, otherwise, it's the message
                    // evaluate() will include when it throws an ArithmeticException
                    try
                        {
                        expectedResult = Long.parseLong( expressionResult ) ;
                        }
                    catch ( @SuppressWarnings( "unused" ) final NumberFormatException e )
                        {
                        expectedResult = 0 ;
                        expressionIsValid = false ;
                        }   // end try
                    
                    messagePrefix = String.format( "[%,d, %s, %,d]",
                                                   lineCount,
                                                   expressionTypes[ expressionType ],
                                                   expressionCounts[ expressionType ] ) ;

                    // display what we're evaluating
                    writeLog( "%s expression: '%s'%n", messagePrefix, expression ) ;

                    if ( expressionIsValid )
                        {
                        writeLog( "%s expect: %,d%n",
                                  messagePrefix,
                                  expectedResult ) ;
                        }
                    else
                        {
                        writeLog( "%s expect: '%s'%n",
                                  messagePrefix,
                                  expressionResult ) ;
                        }   // end if

                    // evaluate the expression
                    try
                        {
                        // uncomment the particular implementation invocation:
                        //      - single-digit, valid expressions
                        //      - multi-digit, valid expressions
                        //      - single-digit, invalid expressions
//                        actualResult = InfixExpressionEvaluatorSingleDigit.evaluate( expression ) ;
//                        actualResult = InfixExpressionEvaluatorMultiDigit.evaluate( expression ) ;
                        actualResult = InfixExpressionEvaluator.evaluate( expression ) ;

                        // no exception thrown: display the actual result
                        writeLog( "%s actual: %,d%n",
                                  messagePrefix,
                                  actualResult ) ;

                        // and whether it's correct
                        if ( ( expressionIsValid ) &&
                             ( actualResult == expectedResult ) )
                            {
                            correctResultCounts[ expressionType ]++ ;
                            // count it if it is

                            writeLog( "%s correct%n", messagePrefix ) ;
                            }
                        else    // incorrect - didn't throw an exception
                            {
                            writeLog( "%s incorrect%n", messagePrefix ) ;
                            }
                        }
                    catch ( final ArithmeticException e )
                        {   // all errors are reported by throwing an
                            // ArithmeticException with an informative message

                        if ( !expressionIsValid )
                            {
                            // display the actual result
                            writeLog( "%s actual: '%s'%n",
                                      messagePrefix,
                                      e.getMessage() ) ;

                            // and whether it's correct
                            if ( expressionResult.equals( e.getMessage() ) )
                                {
                                correctResultCounts[ expressionType ]++ ;

                                writeLog( "%s correct%n", messagePrefix ) ;
                                }
                            else    // incorrect
                                {
                                writeLog( "%s incorrect%n", messagePrefix ) ;
                                }
                            }
                        else    // incorrect - expression is valid
                            {
                            writeLog( "%s incorrect%n", messagePrefix ) ;
                            }
                        }
                    catch ( @SuppressWarnings( "unused" ) final ExitException e )
                        {
                        // ignore - message already displayed
                        // will continue testing...

                        writeLog( "%s incorrect%n", messagePrefix ) ;
                        }
                    catch ( final Throwable e )
                        {
                        writeLog( "%s actual: %s%s%n",
                                  messagePrefix,
                                  e.getClass().getSimpleName(),
                                  e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;

                        writeLog( "%s incorrect%n", messagePrefix ) ;
                        }   // end try

                    writeLog( "%n" ) ;
                    }   // end only evaluate the expression if there is one
                else
                    {   // didn't evaluate anything - display the input
                        // but skip 'deleted' tests
                    if ( ( fullLine.length() >= 4) &&
                         !fullLine.substring( 0, 4 ).equals( "###-" ) )
                        {
                        writeLog( "[%,d] %s%n", lineCount, fullLine ) ;
                        }
                    
                    }

                }   // end while

            }
        catch ( final Throwable e )
            {
            // typically indicates an error in the expressions file
            writeLog( "%s Unexpected exception:%n\t'%s'%n\t'%s'%n\tinput: %s%n",
                      messagePrefix,
                      e.toString(),
                      e.getMessage(),
                      fullLine ) ;
            }   // end try

        // display test execution summary
        writeLog( "-----\n" ) ;

        for ( expressionType = 0 ;
              expressionType < expressionTypes.length ;
              expressionType++ )
            {
            if ( expressionCounts[ expressionType ] > 0 )
                {
                writeConsole( "%,d of %,d %s expressions (%3d%%) evaluated correctly%n",
                              correctResultCounts[ expressionType ],
                              expressionCounts[ expressionType ],
                              expressionTypes[ expressionType ],
                              ( correctResultCounts[ expressionType ] *
                                100 ) / expressionCounts[ expressionType ] ) ;
                }
            else
                {
                writeLog( "No %s expressions evaluated%n",
                          expressionTypes[ expressionType ] ) ;
                }
            }

        /*
         * re-enable System.exit()
         */
        // restore the saved security manager
        System.setSecurityManager( savedSecurityManager ) ;

        }   // end main()

    }   // end class InfixExpressionEvaluatorDMRTests