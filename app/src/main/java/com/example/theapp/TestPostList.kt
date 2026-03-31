package com.example.theapp

object TestPostList {
    fun getPostData(): ArrayList<Post> {
        val postList = ArrayList<Post>()
        val post1 = Post("I Am Placing Blocks And Shit", "5/5", "Notch")
        postList.add(post1)
        postList.add(post1)
        postList.add(post1)
        postList.add(post1)
        postList.add(post1)
        //post1.saveUserToFirestore()
        val post2 = Post("I Am Stacking Blocks And Shit", "5/5", "AlexeyPajitnov")
        postList.add(post2)
        //post2.saveUserToFirestore()
        val post3 = Post("Nothing Like Arcane", "1/5", "hnzi")
        postList.add(post3)
        //post3.saveUserToFirestore()
        val post4 = Post("jump over spike", "3/5", "blingus")
        postList.add(post4)
        //post4.saveUserToFirestore()


        return postList
    }
}