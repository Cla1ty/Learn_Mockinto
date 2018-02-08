package com.kristal.mockinto.base

/**
 * Created by Dwi_Ari on 11/1/17.
 */

interface MockObject {
    fun someMethod(int: Int, string1: String, string2: String)
    fun someMethod(string1: String) :String

    fun doSomething(person: Person)
    fun doSomething()
    fun someMethod(): Any
    fun foo()
    fun bar()
    fun doSomething(anyString: String?, anyString1: String?, anyString2: String?)
}
