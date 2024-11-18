package com.example.myapplication2

import kotlin.reflect.KProperty

fun main() {
    val breeds = mapOf(
        Breed.HUSKYDOG to "Хаски",
        Breed.CORGIDOG to "Корги",
        Breed.SCOTTISHCAT to "Шотландский",
        Breed.SEAMESECAT to "Сиамский",
        Breed.NONE to "Нет породы"
    )

    var husky: Animal = HuskyDog(20f, 11)
    var corgi: Animal = CorgiDog(9f, 3)
    var scottishCat: Animal = ScottishCat(6.4f, 4)
    var seameseCat = SeameseCat(3.95f, 6)
    var human = Human(77f, 38)

    var shop = ZooShop()

    println(breeds[shop.getAnimalBreed(husky)])
    println(breeds[shop.getAnimalBreed(corgi)])
    println(breeds[shop.getAnimalBreed(scottishCat)])
    println(breeds[shop.getAnimalBreed(seameseCat)])
    println(breeds[shop.getAnimalBreed(human)])
    println()
    println((corgi as CorgiDog).getPrikus())
    println(seameseCat.getIsActive())
    println()
    println(shop.identifyDogOrCat(shop.getAnimalBreed(seameseCat)))
    println(shop.identifyDogOrCat(shop.getAnimalBreed(husky)))
    println(shop.identifyDogOrCat(shop.getAnimalBreed(human)))
}

open class ZooShop {
    fun getAnimalBreed (animal: Animal): Breed {
        if (animal is Breedable)
            return animal.getBreed()

        return Breed.NONE
    }

    fun identifyDogOrCat(breed: Breed): String {
        if (breed == Breed.HUSKYDOG || breed == Breed.CORGIDOG)
            return "Собака"
        if (breed == Breed.SCOTTISHCAT || breed == Breed.SEAMESECAT)
            return "Котик"

        return "Ни собака, ни котик"
    }
}

abstract class Animal {
    abstract var weight: Float
    abstract var age: Int
}

// Не у всех животных может быть порода, но у собак и кошек есть
interface Breedable {
    fun getBreed(): Breed
}

interface Doggable: Breedable {
    fun getPrikus(): String
}

interface Cattable: Breedable {
    fun getIsActive(): Boolean
}

class HuskyDog (
    override var weight: Float,
    override var age: Int,
) : Animal(), Doggable {

    override fun getBreed(): Breed {
        return Breed.HUSKYDOG
    }

    override fun getPrikus(): String {
        return "Прямой"
    }

}

class CorgiDog (
    override var weight: Float,
    override var age: Int,
) : Animal(), Doggable {

    override fun getBreed(): Breed {
        return Breed.CORGIDOG
    }

    override fun getPrikus(): String {
        return "Недокус"
    }

}

class ScottishCat (
    override var weight: Float,
    override var age: Int,
) : Animal(), Cattable {

    override fun getBreed(): Breed {
        return Breed.SCOTTISHCAT
    }

    override fun getIsActive(): Boolean {
        return true
    }

}

class SeameseCat (
    override var weight: Float,
    override var age: Int,
) : Animal(), Cattable {

    override fun getBreed(): Breed {
        return Breed.SEAMESECAT
    }

    override fun getIsActive(): Boolean {
        return false
    }

}

class Human(
    override var weight: Float,
    override var age: Int,
): Animal()

enum class Breed {
    HUSKYDOG,
    CORGIDOG,
    SCOTTISHCAT,
    SEAMESECAT,
    NONE
}