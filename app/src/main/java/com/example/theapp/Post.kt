package com.example.theapp

class Post {
    // will likely need to add a postId and comments here,
    // this implementation is mainly for testing recyclerView
    var postClue: String? = null
    var postRating: String? = null // will need to discuss rating system a bit further
    var postOP: String? = null

    constructor(postClue: String?, postRating: String?, postOP: String?) {
        this.postClue = postClue
        this.postRating = postRating
        this.postOP = postOP
    }
}