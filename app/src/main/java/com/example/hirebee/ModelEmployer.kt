package com.example.hirebee

class ModelEmployer {

    var uid:String = ""
    var id:String = ""
    var company:String = ""
    var time:String = ""
    var category:String = ""
    var price:Int = 0
    var categoryId:String = ""
    var url:String = ""
    var timestamp:Long = 0

    constructor()

    constructor(
        uid: String,
        id: String,
        company: String,
        time: String,
        category: String,
        price: Int,
        categoryId: String,
        url: String,
        timestamp: Long
    ) {
        this.uid = uid
        this.id = id
        this.company = company
        this.time = time
        this.category = category
        this.price = price
        this.categoryId = categoryId
        this.url = url
        this.timestamp = timestamp
    }
}