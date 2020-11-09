package org.anilmisirlioglu.keystroke.utils

object BitMask{

    fun has(rights: Int, bits: Int): Boolean = (rights and bits) == bits

    fun getBits(rights: Int, bits: List<Int>): List<Int> = bits.filter{ has(rights, it) }

    fun add(rights: Int, bits: List<Int>): Int = this.add(rights, toInt(bits))

    fun add(rights: Int, bits: Int): Int = rights or bits

    fun delete(rights: Int, bits: List<Int>) = this.delete(rights, toInt(bits))

    fun delete(rights: Int, bits: Int) = rights xor bits

    fun toInt(bits: List<Int>): Int = bits.reduce{ a, b -> a or b }

    fun equal(bits1: List<Int>, bits2: List<Int>): Boolean = this.equal(
        toInt(bits1),
        toInt(bits2)
    )

    fun equal(bits1: Int, bits2: Int): Boolean = bits1 == bits2

}