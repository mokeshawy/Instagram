package com.example.instagram.model

import java.io.Serializable

data class UserModel( var uid       : String = "" ,
                      var userName  : String = "" ,
                      var fullName  : String = "" ,
                      var bio       : String = "" ,
                      var image     : String = ""
) : Serializable