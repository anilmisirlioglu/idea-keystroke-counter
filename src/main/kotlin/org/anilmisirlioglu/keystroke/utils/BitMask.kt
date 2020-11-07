package org.anilmisirlioglu.keystroke.utils

object BitMask{

    fun has(rights: Int, permissions: Int): Boolean = (rights and permissions) == permissions

    fun getBits(rights: Int, permissions: List<Int>): List<Int> = permissions.filter{ has(rights, it) }

    fun add(rights: Int, permissions: List<Int>): Int = this.add(rights, toInt(permissions))

    fun add(rights: Int, permissions: Int): Int = rights or permissions

    fun delete(rights: Int, permissions: List<Int>) = this.delete(rights, toInt(permissions))

    fun delete(rights: Int, permissions: Int) = rights xor permissions

    fun toInt(permissions: List<Int>): Int = permissions.reduce{ a, b -> a or b }

    fun equal(permissions1: List<Int>, permissions2: List<Int>): Boolean = this.equal(
        toInt(permissions1),
        toInt(permissions2)
    )

    fun equal(permissions1: Int, permissions2: Int): Boolean = permissions1 == permissions2

}