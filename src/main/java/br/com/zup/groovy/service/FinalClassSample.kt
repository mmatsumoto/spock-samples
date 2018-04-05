package br.com.zup.groovy.service

class FinalClassSample {
    fun finalMethod(i: Int): Int = throw IllegalAccessError("you should not see this")
}