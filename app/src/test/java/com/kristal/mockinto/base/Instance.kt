package com.kristal.mockinto.base

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by Dwi_Ari on 11/1/17.
 */

@RunWith(MockitoJUnitRunner::class)
abstract class Instance {
    // cara baru
    // pakai annotation
    // butuh MockitoJUnitRunner
    @Mock
    lateinit var mock: MockObject
    @Mock
    lateinit var mockTwo: MockObject
    @Mock
    lateinit var mockedList: ArrayList<String>

    @Spy
    lateinit var spy : ArrayList<String>

    init {
        // cara lama
//        mock = Mockito.mock(MockObject::class.java)

        // cara lain mock pengganti
        // @RunWith(MockitoJUnitRunner::class)
        // di class yg sudah final
        MockitoAnnotations.initMocks(this);

        val serializableMock = mock(
                ArrayList<String>()::class.java,
                withSettings().serializable())


        val list = ArrayList<Any>()
        val spy = mock(ArrayList::class.java,
                withSettings()
                        .spiedInstance(list)
                        .defaultAnswer(CALLS_REAL_METHODS)
                        .serializable())
    }

}