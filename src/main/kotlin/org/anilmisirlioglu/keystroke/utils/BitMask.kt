package org.anilmisirlioglu.keystroke.utils

object BitMask{

    fun has(bit: Int, rights: Int): Boolean = (rights and bit) == bit

    fun getBits(bits: List<Int>, rights: Int): List<Int> = bits.filter{ has(it, rights) }

    fun add(bits: List<Int>, rights: Int): Int = this.add(toInt(bits), rights)

    fun add(bits: Int, rights: Int): Int = rights or bits

    fun delete(bits: List<Int>, rights: Int): Int = this.delete(toInt(bits), rights)

    fun delete(bits: Int, rights: Int): Int = if(has(bits, rights)) rights xor bits else rights

    fun toInt(bits: List<Int>): Int = bits.reduce{ a, b -> a or b }

    fun equal(bits1: List<Int>, bits2: List<Int>): Boolean = this.equal(
        toInt(bits1),
        toInt(bits2)
    )

    fun equal(bits1: Int, bits2: Int): Boolean = bits1 == bits2

}