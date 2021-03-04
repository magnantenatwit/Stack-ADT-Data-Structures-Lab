/* @formatter:off * * Dave Rosenberg * Comp 2000 - Data Structures * Lab: Stack ADT and Application * Fall, 2020 *  * Usage restrictions: *  * You may use this code for exploration, experimentation, and furthering your * learning for this course. You may not use this code for any other * assignments, in my course or elsewhere, without explicit permission, in * advance, from myself (and the instructor of any other course). *  * Further, you may not post nor otherwise share this code with anyone other than * current students in my sections of this course. Violation of these usage * restrictions will be considered a violation of the Wentworth Institute of * Technology Academic Honesty Policy. * * Do not remove this notice. * * @formatter:on */package edu.wit.dcsn.comp2000.stack.common;import java.util.EmptyStackException ;/** * An interface for the ADT stack. *  * @author Frank M. Carrano * @author Timothy M. Henry * @version 5.0 *  * @param <T> *        the class of objects the stack will hold */public interface StackInterface<T>	{	/** Removes all entries from this stack. */	public void clear() ;	/**	 * Detects whether this stack is empty.	 * 	 * @return True if the stack is empty.	 */	public boolean isEmpty() ;	/**	 * Retrieves this stack's top entry.	 * 	 * @return The object at the top of the stack.	 * @throws EmptyStackException	 *         if the stack is empty before the operation.	 */	public T peek() ;	/**	 * Removes and returns this stack's top entry.	 * 	 * @return The object at the top of the stack.	 * @throws EmptyStackException	 *         if the stack is empty before the operation.	 */	public T pop() ;		/**	 * Adds a new entry to the top of this stack.	 * 	 * @param newEntry	 *        An object to be added to the stack.	 */	public void push( T newEntry ) ;	} // end StackInterface