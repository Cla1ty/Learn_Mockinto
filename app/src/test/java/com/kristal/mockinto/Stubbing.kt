package com.kristal.mockinto

import com.kristal.mockinto.base.Instance
import com.kristal.mockinto.base.MockObject
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import java.util.*




/**
 * Created by Dwi_Ari on 11/1/17.
 */

class Stubbing : Instance() {
    // ketika tidak di stubbing
    // return bakal menghasilkan nilai default
    // false, 0, null
    @Test
    fun stubbing() {
        //stubbing
        `when`(mockedList[0]).thenReturn("first")
        `when`(mockedList[1]).thenThrow(RuntimeException())

        //following prints "first"
        println(mockedList[0])

        //following throws runtime exception
        println(mockedList[1])

        //following prints "null" because get(999) was not stubbed
        println(mockedList[999])

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList)[0]
    }

    @Test
    fun voidMethodWithException() {
        // doReturn
        // doThrow
        // doAnswer
        // doNothing
        // doCallRealMethod
        doThrow(RuntimeException()).`when`(mockedList).clear()
        //following throws RuntimeException:
        mockedList.clear()
    }

    @Test
    fun consecutiveCalls() {
        `when`(mock.someMethod("some arg"))
                .thenThrow(RuntimeException())
                .thenReturn("foo")

        //First call: throws runtime exception:
        mock.someMethod("some arg")

        //Second call: prints "foo"
        System.out.println(mock.someMethod("some arg"))

        //Any consecutive call: prints "foo" as well (last stubbing wins).
        System.out.println(mock.someMethod("some arg"))
    }

    @Test
    fun consecutiveCallsSimple() {
        `when`(mock.someMethod("some arg"))
                .thenReturn("one", "two", "three")

        println(mock.someMethod("some arg"))
        println(mock.someMethod("some arg"))
        println(mock.someMethod("some arg"))
    }

    @Test
    fun multi() {
        //All mock.someMethod("some arg") calls will return "two"
        `when`(mock.someMethod("some arg"))
                .thenReturn("one")
        `when`(mock.someMethod("some arg"))
                .thenReturn("two")

        println(mock.someMethod("some arg"))
    }

    @Test
    fun callback() {
        `when`(mock.someMethod(anyString()))
                .thenAnswer { invocation ->
                    val args = invocation.arguments
                    val mock = invocation.mock
                    "called with arguments: " + args[0]
                }

        //the following prints "called with arguments: foo"
        println(mock.someMethod("foo"))
    }

    @Test
    fun realPartialMocks(){
        //you can create partial mock with spy() method:
        val list = spy(LinkedList<String>())

        //you can enable partial mock capabilities selectively on mocks:
        val mock = mock(MockObject::class.java)
        //Be sure the real implementation is 'safe'.
        //If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
        `when`(mock.someMethod()).thenCallRealMethod()
    }

    @Test
    fun resetting(){
        `when`(mockedList.size).thenReturn(10)
        mockedList.add("1")

        reset(mockedList)
        //at this point the mock forgot any interactions & stubbing
    }
}