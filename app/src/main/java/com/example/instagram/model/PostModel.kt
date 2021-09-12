package com.example.instagram.model

import java.io.Serializable

class PostModel (
    var postId      : String = "" ,
    var postImage   : String = "" ,
    var publishre   : String = "" ,
    var description : String = ""
) : Serializable