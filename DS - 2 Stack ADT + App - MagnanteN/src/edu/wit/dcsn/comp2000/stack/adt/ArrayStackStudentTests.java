/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Stack ADT
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

package edu.wit.dcsn.comp2000.stack.adt ;

import static edu.wit.dcsn.dmr.testing.junit.Reflection.getIntField ;
import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.assertFalse ;
import static org.junit.jupiter.api.Assertions.assertNotNull ;
import static org.junit.jupiter.api.Assertions.assertThrows ;
import static org.junit.jupiter.api.Assertions.assertTrue ;
import static org.junit.jupiter.api.Assertions.fail ;

import edu.wit.dcsn.comp2000.stack.common.ArrayStackCapacity ;
import edu.wit.dcsn.comp2000.stack.common.StackInterface ;

import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.Order ;
import org.junit.jupiter.api.Test ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;

import java.util.EmptyStackException ;

/**
 * @author Nicholas Magnante // DONE
 * @version 1.0.0 initial implementation
 */
@DisplayName( "Testing ArrayStack" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
class ArrayStackStudentTests
    {

    /*
     * test constructors
     */

    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#ArrayStack()}.
     */
    @Test
    @Order( 100 )
    void testArrayStack()
        {
        System.out.println( "\nTesting: no-arg constructor" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        System.out.println( "...checking capacity" ) ;
        int testTopIndex = getIntField( testStack, "topIndex" ) ;
        assertEquals( ArrayStackCapacity.DEFAULT.capacityValue, testTopIndex ) ;

        System.out.println( "Test passed." ) ;

        }	// end testArrayStack()


    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#ArrayStack(int)}.
     */
    @Test
    @Order( 200 )
    void testArrayStackInt()
        {
        System.out.println( "\nTesting: 1-arg constructor" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>( 3 ) ;

        System.out.println( "...checking capacity" ) ;
        int testTopIndex = getIntField( testStack, "topIndex" ) ;
        assertEquals( 3, testTopIndex ) ;

        System.out.println( "...push() 3 values to expand" ) ;
        testStack.push( 1 ) ;
        testStack.push( 2 ) ;
        testStack.push( 3 ) ;
        testStack.push( 4 ) ;

        System.out.println( "...checking index/capacity" ) ;
        testTopIndex = getIntField( testStack, "topIndex" ) ;
        assertEquals( 2, testTopIndex ) ;

        System.out.println( "Test passed." ) ;

        }	// end testArrayStackInt()

    /*
     * test API methods
     */


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#clear()}.
     */
    @Test
    @Order( 300 )
    void testClear()
        {
        System.out.println( "\nTesting: clear()" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        System.out.println( "...push() onto the stack (3 values)" ) ;
        testStack.push( 1 ) ;
        testStack.push( 2 ) ;
        testStack.push( 3 ) ;

        System.out.println( "...clear() stack" ) ;
        testStack.clear() ;

        System.out.println( "...testing if stack is empty" ) ;
        assertTrue( testStack.isEmpty() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 10 ) ;

        System.out.println( "...clear() stack" ) ;
        testStack.clear() ;

        System.out.println( "...testing if stack is empty" ) ;
        assertTrue( testStack.isEmpty() ) ;

        System.out.println( "Test passed." ) ;

        }	// end testClear()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#isEmpty()}.
     */
    @Test
    @Order( 400 )
    void testIsEmpty()
        {
        System.out.println( "\nTesting isEmpty()" ) ;

        // DONE
        
        System.out.println( "...instantiate a stack with default initial capacity" ) ;
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        assertNotNull( testStack ) ;    // unnecessary - for illustration

        System.out.println( "...must be empty" ) ;
        assertTrue( testStack.isEmpty() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 3 ) ;
//        testStack.push( 3 ) ;   // if you uncomment this, this isEmpty() test
//                                // succeeds but the next isEmpty() test fails
        System.out.println( "...must not be empty" ) ;
        assertFalse( testStack.isEmpty() ) ;

        System.out.println( "...pop() from the stack" ) ;
        testStack.pop() ;
        System.out.println( "...must be empty again" ) ;
        assertEquals( true, testStack.isEmpty() ) ;
                                // for illustration - use assertTrue()
        
        System.out.println( "...must throw EmptyStackException" ) ;
        assertThrows( EmptyStackException.class, () -> testStack.pop() ) ;

        System.out.println( "Test passed." ) ;

        }   // end testIsEmpty()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#peek()}.
     */
    @Test
    @Order( 500 )
    void testPeek()
        {
        System.out.println( "\nTesting peek()" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        System.out.println( "...push() onto the stack (3 values)" ) ;
        testStack.push( 1 ) ;
        testStack.push( 2 ) ;
        testStack.push( 3 ) ;

        System.out.println( "...must be equal to 3" ) ;
        assertEquals( 3, testStack.peek() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 50 ) ;

        System.out.println( "...must be equal to 50" ) ;
        assertEquals( 50, testStack.peek() ) ;

        System.out.println( "...pop() from the stack" ) ;
        testStack.pop() ;

        System.out.println( "...must be equal to 3 again" ) ;
        assertEquals( 3, testStack.peek() ) ;

        System.out.println( "Test passed." ) ;

        }	// end testPeek()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#pop()}.
     */
    @Test
    @Order( 600 )
    void testPop()
        {
        System.out.println( "\nTesting pop()" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        System.out.println( "...push() onto the stack (3 values)" ) ;
        testStack.push( 10 ) ;
        testStack.push( 20 ) ;
        testStack.push( 30 ) ;

        System.out.println( "...pop() returns value popped (30)" ) ;
        assertEquals( 30, testStack.pop() ) ;

        System.out.println( "...top of stack must be equal to 20" ) ;
        assertEquals( 20, testStack.peek() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 40 ) ;

        System.out.println( "...pop() returns value popped (40)" ) ;
        assertEquals( 40, testStack.pop() ) ;

        System.out.println( "...top of stack must be equal to 20" ) ;
        assertEquals( 20, testStack.peek() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 50 ) ;

        System.out.println( "...pop() returns value popped (50)" ) ;
        assertEquals( 50, testStack.pop() ) ;

        System.out.println( "...pop() returns value popped (20)" ) ;
        assertEquals( 20, testStack.pop() ) ;

        System.out.println( "...top of stack must be equal to 10" ) ;
        assertEquals( 10, testStack.peek() ) ;

        System.out.println( "...pop() returns value popped (10)" ) ;
        assertEquals( 10, testStack.pop() ) ;

        System.out.println( "...must be empty" ) ;
        assertTrue( testStack.isEmpty() ) ;

        System.out.println( "Test passed." ) ;

        }	// end testPop()


    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.stack.adt.ArrayStack#push(java.lang.Object)}.
     */
    @Test
    @Order( 700 )
    void testPush()
        {
        System.out.println( "\nTesting push()" ) ;

        // DONE
        final StackInterface<Integer> testStack = new ArrayStack<>() ;

        System.out.println( "...push() onto the stack (3 values)" ) ;
        testStack.push( 10 ) ;
        testStack.push( 20 ) ;
        testStack.push( 30 ) ;

        System.out.println( "...peek() returns 30" ) ;
        assertEquals( 30, testStack.peek() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 40 ) ;

        System.out.println( "...peek() returns 40" ) ;
        assertEquals( 40, testStack.peek() ) ;

        System.out.println( "...push() onto the stack" ) ;
        testStack.push( 50 ) ;

        System.out.println( "...pop() from stack" ) ;
        testStack.pop() ;

        System.out.println( "...peek() returns 40" ) ;
        assertEquals( 40, testStack.peek() ) ;

        System.out.println( "Test passed." ) ;

        }	// end testPush()

    }	// end class ArrayStackStudentTests
