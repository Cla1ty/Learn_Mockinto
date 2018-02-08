package com.kristal.mockinto

import com.kristal.mockinto.base.*
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*




/**
 * Created by Dwi_Ari on 11/1/17.
 */

class Verify : Instance() {
    @Test
    fun behaviour() {
        //using mock object
        mockedList.add("one")
        mockedList.clear()

        //verification
        verify(mockedList).add("one")
        verify(mockedList).clear()
    }

    @Test
    fun argumentValue() {
        //stubbing using built-in anyInt() argument matcher
        `when`(mockedList[anyInt()]).thenReturn("element")

        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
//        `when`(mockedList.contains(argThat(isValid()))).thenReturn("element")

        //following prints "element"
        println(mockedList[999])

        //you can also verify using an argument matcher
        verify(mockedList)[anyInt()]

        //argument matchers can also be written as Java 8 Lambdas
        verify(mockedList).add(argThat { someString -> someString.length > 5 })
    }

    @Test
    fun test() {
        verify(mock).someMethod(anyInt(), anyString(), eq("third argument"))
        //above is correct - eq() is also an argument matcher

        verify(mock).someMethod(anyInt(), anyString(), "third argument")
        //above is incorrect - exception will be thrown because third argument is given without an argument matcher.

    }

    @Test
    fun exactNumber() {
        //using mock
        mockedList.add("once")

        mockedList.add("twice")
        mockedList.add("twice")

        mockedList.add("three times")
        mockedList.add("three times")
        mockedList.add("three times")

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once")
        verify(mockedList, times(1)).add("once")

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice")
        verify(mockedList, times(3)).add("three times")

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened")

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times")
        verify(mockedList, atLeast(2)).add("three times")
        verify(mockedList, atMost(5)).add("three times")
    }

    @Test
    fun order() {
        // A. Single mock whose methods must be invoked in a particular order
//        val singleMock = mock(List<*>::class.java)

        //using a single mock
        mockedList.add("was added first")
        mockedList.add("was added second")

        //create an inOrder verifier for a single mock
        inOrder(mockedList)
                .apply {
                    //following will make sure that add is first called with "was added first, then with "was added second"
                    verify<ArrayList<String>>(mockedList).add("was added first")
                    verify<ArrayList<String>>(mockedList).add("was added second")
                }


        // B. Multiple mocks that must be used in a particular order
        val firstMock = mock(ArrayList<String>()::class.java)
        val secondMock = mock(ArrayList<String>()::class.java)

        //using mocks
        firstMock.add("was called first")
        secondMock.add("was called second")

        //create inOrder object passing any mocks that need to be verified in order
        inOrder(firstMock, secondMock)
                .apply {
                    //following will make sure that firstMock was called before secondMock
                    verify<ArrayList<String>>(firstMock).add("was called first")
                    verify<ArrayList<String>>(secondMock).add("was called second")
                }

        // Oh, and A + B can be mixed together at will
    }

    @Test
    fun interactionNeverHappen() {
        val firstMock = mock(ArrayList<String>()::class.java)
        val secondMock = mock(ArrayList<String>()::class.java)

        //using mocks - only mockOne is interacted
        mockedList.add("one");

        //ordinary verification
        verify(mockedList).add("one");

        //verify that method was never called on a mock
        verify(mockedList, never()).add("two");

        //verify that other mocks were not interacted
        verifyZeroInteractions(firstMock, secondMock);
    }

    @Test
    fun findRedundantInvocations() {
        //using mocks
        mockedList.add("one");
        mockedList.add("two");

        verify(mockedList).add("one");
        verify(mockedList).add("two");

        verifyNoMoreInteractions(mockedList);
    }

    @Test
    fun spy() {
        val list = LinkedList<String>()
        val spy = spy(list)

        //optionally, you can stub out some methods:
        `when`<Int>(spy.size).thenReturn(100)

        //using the spy calls *real* methods
        spy.add("one")
        spy.add("two")

        //prints "one" - the first element of a list
        println(spy[0])

        //size() method was stubbed - 100 is printed
        println(spy.size)

        //optionally, you can verify
        verify(spy).add("one")
        verify(spy).add("two")
    }

    @Test
    fun spyImposible() {
        // cara lama
//        val list = LinkedList<String>()
//        val spy = spy(list)

        // kalau ada ini yg di bawah sukses
//        spy.add("Foo")
        //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
        `when`<Any>(spy[0]).thenReturn("foo")

        //You have to use doReturn() for stubbing
        doReturn("foo").`when`(spy)[0]
    }

    @Test
    fun captureArgument() {
        val argument = ArgumentCaptor.forClass<Person, Person>(Person::class.java)
        verify(mock).doSomething(argument.capture())
        Assert.assertEquals("John", argument.value.name)
    }

    @Test
    fun aliasesForBehaviourDevelopment() {
        val seller = mock(Seller::class.java)
        val shop = Shop(seller)
        given(seller.askForBread()).willReturn(Bread())

        //when
        val goods = shop.buyBread()

        //then
//        Assert.assertThat(goods, containBread())
    }

    @Test
    fun timeOut() {

        //passes when someMethod() is called within given time span
        verify(mock, timeout(100)).someMethod()
        //above is an alias to:
        verify(mock, timeout(100).times(1)).someMethod()

        //passes when someMethod() is called *exactly* 2 times within given time span
        verify(mock, timeout(100).times(2)).someMethod()

        //passes when someMethod() is called *at least* 2 times within given time span
        verify(mock, timeout(100).atLeast(2)).someMethod()

        //verifies someMethod() within given time span using given verification mode
        //useful only if you have your own custom verification modes.
//        verify(mock, Timeout(100, yourOwnVerificationMode)).someMethod()
    }

    // todo ????
    @Test
    fun verificaionIgnoreStub() {
        mock.foo()
        mockTwo.bar()

        verify(mock).foo()
        verify(mockTwo).bar()

        //ignores all stubbed methods:
        verifyNoMoreInteractions(*ignoreStubs(mock, mockTwo))

        //creates InOrder that will ignore stubbed
        val inOrder = inOrder(*ignoreStubs(mock, mockTwo))
        inOrder.verify(mock).foo()
        inOrder.verify(mockTwo).bar()
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun mockDetail() {
        //To identify whether a particular object is a mock or a spy:
        Mockito.mockingDetails(mock).isMock
        Mockito.mockingDetails(mock).isSpy

        //Getting details like type to mock or default answer:
        mockingDetails(mock)
                .apply {
                    mockCreationSettings.typeToMock
                    mockCreationSettings.defaultAnswer
                }


        //Getting interactions and stubbings of the mock:
        mockingDetails(mock)
                .apply {
                    //                    getInteractions()
                    stubbings
                }

        //Printing all interactions (including stubbing, unused stubs)
        println(mockingDetails(mock).printInvocations())
    }

    @Test
    fun failureMessage() {

        // will print a custom message on verification failure
        verify(mock, description("This will print on failure")).someMethod();

        // will work with any verification mode
        verify(mock, times(2).description("someMethod should be called twice")).someMethod();
    }

    @Test
    fun lamda() {
        // verify a list only had strings of a certain length added to it
        // note - this will only compile under Java 8
        verify(mockedList, times(2))
                .add(argThat { string -> string.length < 5 })

//        // more complex Java 8 example - where you can specify complex verification behaviour functionally
//        verify(target, times(1)).receiveComplexObject(argThat { obj -> obj.getSubObject().get(0).equals("expected") })
//
//        // this can also be used when defining the behaviour of a mock under different inputs
//        // in this case if the input list was fewer than 3 items the mock returns null
//        `when`(mock.someMethod(argThat { list -> list.size() < 3 })).willReturn(null)

        // ==========

        // answer by returning 12 every time
        doAnswer { invocation -> 12 }.`when`(mock).doSomething()

        // answer by using one of the parameters - converting into the right
        // type as your go - in this case, returning the length of the second string parameter
        // as the answer. This gets long-winded quickly, with casting of parameters.
        doAnswer { invocation -> (invocation.getArgument<Any>(1) as String).length }
                .`when`(mock).doSomething(anyString(), anyString(), anyString())

    }
}
