package com.rikkei.myapplication.model

import java.io.Serializable

class UserModel : Serializable{

    var id: Int
    var name: String?
    var gender: String?
    var age : Int?
    var address: String?

    constructor(id: Int, name: String, gender: String, age: Int, address: String) {
        this.id = id
        this.name = name
        this.gender = gender
        this.age = age
        this.address = address
    }

    override fun toString(): String {
        return "UserModel(id=$id, name='$name', gender='$gender', age=$age, address='$address')"
    }


}