package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Marketing Doctor", appName)
  }

  @Test
  fun `verify lead score calculation for high intent query`() {
    val conversations = com.example.data.repository.MarketingRepository.conversations.value
    val firstConv = conversations.first()
    val score = com.example.data.repository.MarketingRepository.calculateLeadScore(firstConv)
    org.junit.Assert.assertTrue(score.score >= 60)
    org.junit.Assert.assertEquals(com.example.data.model.ScoreCategory.HOT, score.category)
  }
}
