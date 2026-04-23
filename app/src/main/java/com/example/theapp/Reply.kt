package com.example.theapp

import android.os.Parcelable
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reply(
    @get:PropertyName("postID") @set:PropertyName("postID")
    var postID: Int? = 0,
    @get:PropertyName("replyID") @set:PropertyName("replyID")
    var replyID: Int? = 0,
    @get:PropertyName("answer") @set:PropertyName("answer")
    var replyAnswer: String? = "",
    @get:PropertyName("comment") @set:PropertyName("comment")
    var replyComment: String? = "",
    @get:PropertyName("name") @set:PropertyName("name")
    var replyPoster: String? = "",
    @get:PropertyName("userID") @set:PropertyName("userID")
    var replyPosterID: Int? = 0,
    @get:PropertyName("grade") @set:PropertyName("grade")
    var replyGrade: Int? = 0
) : Parcelable {

    suspend fun gradeReply(postAnswer: String?, apiKey: String) {
        //check for easy correct answer
        if (replyAnswer?.trim().equals(postAnswer?.trim(), ignoreCase = true)) {
            replyGrade = 100
            return
        }

        val config = generationConfig {
            temperature = 0.1f // Keep it deterministic for grading
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = config
        )

        val prompt = """ 
            You are a grading assistant. Compare the User's Answer to the Correct Answer.
    
            Correct Answer: "$postAnswer"
            User's Answer: "$replyAnswer"
    
            Instructions:
            1. If the answer is perfectly correct, synonymous, or an acronym (e.g., "LOL" vs "League of Legends"), give it 100.
            2. If it is partially correct or contains parts of the answer, give a score between 10 and 90 based on accuracy. 
                Some partially correct sample cases:
                Correct Answer: Minecraft
                User's Answer: Vintage Story
                Grade: 30 (A block-based sandbox survival game)
                Example 2:
                Correct Answer: Halo Reach
                User's Answer: Halo Combat Evolved
                Grade: 60 (Same franchise, wrong game)
            3. If it is completely wrong, give it 0.
    
            Return ONLY a single integer between 0 and 100.
            """.trimIndent()

        try {
            val response = generativeModel.generateContent(prompt)
            // Extract the first number found in the response
            val resultText = response.text?.trim() ?: ""
            val score = Regex("\\d+").find(resultText)?.value?.toIntOrNull()
            
            replyGrade = score?.coerceIn(0, 100) ?: 0
        } catch (e: Exception) {
            Log.e("ReplyGrading", "Error during AI grading: ${e.message}", e)
            replyGrade = 0
        }
    }







//        val lowerReply = replyAnswer?.lowercase()
//        val lowerAnswer = postAnswer?.lowercase()
//
//        replyGrade = if (lowerReply == lowerAnswer) {
//            1
//        } else {
//            -1
//        }
    }