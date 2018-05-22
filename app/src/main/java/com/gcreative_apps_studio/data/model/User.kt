package com.gcreative_apps_studio.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class User(
        @Id var id:             Long = 0,
        var username:           String = "",
        var password:           String = "",
        var email:              String = "",
        var security_question:  String = "",
        var security_answer:    String = ""
)