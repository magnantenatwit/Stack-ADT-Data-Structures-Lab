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

package edu.wit.dcsn.comp2000.stack.app ;

import edu.wit.dcsn.comp2000.stack.common.StackInterface ;

import java.util.NoSuchElementException ;
import java.util.Scanner ;

/**
 * A class to evaluate infix arithmetic expressions:
 * <ul>
 * <li>all values are represented as {@code long}s
 * <li>all arithmetic operations are performed with integer arithmetic (no fractional
 * values)
 * <li>all arithmetic operations are performed with Java binary operators
 * <li>no Java Class Library (JCL) classes or methods are used to perform the
 * expression evaluation
 * </ul>
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
 * @version 1.1.0 2019-06-08
 *     <ul>
 *     <li>rename operandsStack to valuesStack
 *     <li>ignore embedded blanks in expression
 *     <li>replace multi-digit input to hand-rolled instead of building a string then
 *     using JCL to parse it
 *     <li>check for consecutive operands
 *     <li>check for null expression (0-length)
 *     </ul>
 * @version 1.2.0 2020-02-27
 *     <ul>
 *     <li>make compliant with class coding standard
 *     <li>enhance test for whitespace
 *     </ul>
 * @version 1.2.1 2020-02-28
 *     <ul>
 *     <li>validation of {@code expression} argument to {@code evaluate()}
 *     <li>move {@code ')'} precedence to highest value
 *     </ul>
 * @version 1.3.0 2020-06-15 modify {@code doOperations(operatorStack, valuesStack)}
 *     to push the result onto the {@code valuesStack} rather than returning the
 *     result
 * @version 1.4.0 2020-10-07
 *     <ul>
 *     <li>simplify invalid expression testing - limit to empty/non-empty stack
 *     verification
 *     <li>add interactive test driver in {@code main()}
 *     </ul>
 * @author Nicholas Magnante // DONE
 * @version 1.5.0 2020-10-08 implement per assignment
 */
public final class InfixExpressionEvaluator
    {

    /**
     * prevent instantiation
     */
    private InfixExpressionEvaluator()
        {
        // can't instantiate an InfixExpressionEvaluator
        }	// end constructor


    /**
     * Evaluate an infix arithmetic expression.
     *
     * @param expression
     *     an infix expression composed of unsigned decimal operands and a
     *     combination of operators ({@code +, -, *, /}) including parenthesized
     *     (sub-)expressions; may include spaces
     * @return the result of evaluating {@code expression}
     * @throws ArithmeticException
     *     if {@code expression} is syntactically invalid, is null/0-length, attempts
     *     to divide by zero, or contains unrecognized characters
     */
    public static long evaluate( final String expression ) throws ArithmeticException
        {
        // we'll use 2 stacks to remember what's left to evaluate
        final StackInterface<Character> operatorStack = new VectorStack<>() ;
        final StackInterface<Long> valuesStack = new VectorStack<>() ;

        // DONE - implement this

        for ( int i = 0 ; i < expression.length() ; i++ )
            {
            if ( Character.isWhitespace( expression.charAt( i ) ) )
                {
                continue ;
                }

            if ( Character.isDigit( expression.charAt( i ) ) )
                {
                long operand = 0 ;

                while ( i < expression.length() &&
                        Character.isDigit( expression.charAt( i ) ) )
                    {
                    operand = ( operand * 10 ) + ( expression.charAt( i ) - '0' ) ;
                    i++ ;
                    }
                i-- ;
                valuesStack.push( operand ) ;
                continue ;
                }

            switch ( expression.charAt( i ) )
                {
                case '+':
                case '-':
                case '*':
                case '/':
                    while ( !operatorStack.isEmpty() &&
                            checkPrecedence( expression.charAt( i ),
                                             operatorStack.peek() ) )
                        {
                        char operator = operatorStack.pop() ;
                        long operand1 = valuesStack.pop() ;
                        long operand2 = valuesStack.pop() ;

                        valuesStack.push( doOperation( operand2,
                                                       operand1,
                                                       operator ) ) ;
                        }
                    operatorStack.push( expression.charAt( i ) ) ;
                    break ;
                case '(':
                    operatorStack.push( expression.charAt( i ) ) ;
                    break ;
                case ')':
                    while ( operatorStack.peek() != '(' )
                        {
                        char operator = operatorStack.pop() ;
                        long operand1 = valuesStack.pop() ;
                        long operand2 = valuesStack.pop() ;

                        valuesStack.push( doOperation( operand2,
                                                       operand1,
                                                       operator ) ) ;
                        }
                    operatorStack.pop() ;
                    break ;
                default:
                    throw new ArithmeticException( "unrecognized character: '" +
                                                   expression.charAt( i ) + "'" ) ;
                }
            }

        while ( !operatorStack.isEmpty() )
            {
            char operator = operatorStack.pop() ;
            long operand1 = valuesStack.pop() ;
            long operand2 = valuesStack.pop() ;

            valuesStack.push( doOperation( operand2, operand1, operator ) ) ;
            }
        // the result is on top of the values/operand stack
        return valuesStack.pop() ;

        }	// end evaluate()


    /**
     * @param firstOp
     *     The first operator in the comparison
     * @param secondOp
     *     The second operator in the comparison
     * @return if the first operator has precedence over of the second operator.
     */
    private static boolean checkPrecedence( char firstOp,
                                            char secondOp )
        {
        if ( secondOp == '(' || secondOp == ')' )
            {
            return false ;
            }

        if ( ( firstOp == '*' || firstOp == '/' ) &&
             ( secondOp == '+' || secondOp == '-' ) )
            {
            return false ;
            }
        return true ;
        }


    /**
     * @param operand1
     *     First number in the operation
     * @param operand2
     *     Second number in the operation
     * @param operator
     *     Operator to be applies to the first and second operand
     * @return The output of the operation/equation.
     */
    private static long doOperation( long operand1,
                                     long operand2,
                                     char operator )
        {
        switch ( operator )
            {
            case '+':
                return operand1 + operand2 ;
            case '-':
                return operand1 - operand2 ;
            case '*':
                return operand1 * operand2 ;
            case '/':
                if ( operand2 == 0 )
                    {
                    throw new ArithmeticException( "division by zero" ) ;
                    }
                return operand1 / operand2 ;
            default:
                throw new ArithmeticException( "unrecognized character: '" +
                                               operator + "'" ) ;
            }
        }


    /**
     * (optional) Driver for testing/debugging.
     * <p>
     * Facilitates interactive testing of {@code evaluate()}.
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // interactive via console
        try ( Scanner input = new Scanner( System.in ) )
            {
            while ( true )
                {
                System.out.printf( "%nEnter expression (or quit): " ) ;
                String expression = input.nextLine() ;

                // continue or exit?
                String trimmedExpression = expression.trim() ;
                if ( ( trimmedExpression.length() <= 4 ) &&
                     !trimmedExpression.equals( "" ) &&
                     trimmedExpression.equalsIgnoreCase( "quit".substring( 0,
                                                                           trimmedExpression.length() ) ) )
                    {
                    break ; // exit
                    }

                System.out.print( "Result: " ) ;
                try
                    {
                    System.out.printf( "%,d%n", evaluate( expression ) ) ;
                    }
                catch ( final Throwable e )
                    {
                    e.printStackTrace( System.out ) ;
                    }   // end try/catch

                }   // end while
            }
        catch ( @SuppressWarnings( "unused" ) NoSuchElementException e )
            {
            // ignore end-of-file/input - done processing
            }
        catch ( final Throwable e )
            {
            e.printStackTrace( System.out ) ;
            }   // end try/catch

        System.out.printf( "%ndone%n" ) ;

        }	// end main()

    }	// end class InfixExpressionEvaluator
