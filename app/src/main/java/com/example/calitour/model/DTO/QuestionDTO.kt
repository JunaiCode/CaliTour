package com.example.calitour.model.DTO

data class QuestionDTO (
    val id : String = "",
    val title : String = "",
    val correctAns : String = "",
    val options :ArrayList<String> =ArrayList()
)