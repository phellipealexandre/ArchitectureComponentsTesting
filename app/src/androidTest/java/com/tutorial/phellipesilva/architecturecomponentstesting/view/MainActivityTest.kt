package com.tutorial.phellipesilva.architecturecomponentstesting.view

import android.content.pm.ActivityInfo
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.tutorial.phellipesilva.architecturecomponentstesting.R
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    public var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
    }

    @After
    fun tearDown() {
        server.shutdown()
        InstrumentationRegistry.getTargetContext().deleteDatabase("Arch Database")
    }

    @Test
    fun startsActivityWithEmptyCardState() {
        onView(withId(R.id.txtUserName)).check(matches(withText("")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("")))
    }

    @Test
    fun fetchesUserSuccessfullyFromRemoteEndpointAndShowOnScreen() {
        val json = readJsonFromResources("simple_response_id_1.json")
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("Test Name")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("test@domain.com")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("(99) 9999-9999")))
    }

    @Test
    fun replacesUserCardWhenFetchingOtherUserFromRemoteEndpoint() {
        val mockResponse1 =
            MockResponse().setBody(readJsonFromResources("simple_response_id_1.json"))
        val mockResponse2 =
            MockResponse().setBody(readJsonFromResources("simple_response_id_2.json"))
        server.enqueue(mockResponse1)
        server.enqueue(mockResponse2)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        onView(withId(R.id.edtUserIdForm)).perform(clearText())
        onView(withId(R.id.edtUserIdForm)).perform(typeText("2"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("Other Name")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("hello@domain.com")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("(88) 8888-9999")))
    }

    @Test
    fun changesUserPhoneOnCardWhenUpdatingUserPhone() {
        val json = readJsonFromResources("simple_response_id_1.json")
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        onView(withId(R.id.edtUserPhone)).perform(typeText("123456"))
        onView(withId(R.id.btnUpdatePhone)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("Test Name")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("test@domain.com")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("123456")))
    }

    @Test
    fun getUserWithChangedPhoneFromDatabaseWhenRequestingPreviousUserFromDb() {
        val mockResponse1 =
            MockResponse().setBody(readJsonFromResources("simple_response_id_1.json"))
        val mockResponse2 =
            MockResponse().setBody(readJsonFromResources("simple_response_id_2.json"))
        server.enqueue(mockResponse1)
        server.enqueue(mockResponse2)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        onView(withId(R.id.edtUserPhone)).perform(typeText("123456"))
        onView(withId(R.id.btnUpdatePhone)).perform(click())
        onView(withId(R.id.edtUserIdForm)).perform(typeText("2"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        onView(withId(R.id.edtGetUserFromDBId)).perform(typeText("1"))
        onView(withId(R.id.btnGetFromDb)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("Test Name")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("test@domain.com")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("123456")))
    }

    @Test
    fun maintainDataOnCardWhenRotationScreen() {
        val json = readJsonFromResources("simple_response_id_1.json")
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        activityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("Test Name")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("test@domain.com")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("(99) 9999-9999")))
    }

    @Test
    fun clearCardWhenPressingClearButton() {
        val json = readJsonFromResources("simple_response_id_1.json")
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnClearUsers)).perform(click())

        onView(withId(R.id.txtUserName)).check(matches(withText("")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("")))
    }

    @Test
    fun deletesUserWhenPressingClearButton() {
        val json = readJsonFromResources("simple_response_id_1.json")
        val mockResponse = MockResponse().setBody(json)
        server.enqueue(mockResponse)
        server.start(4040)

        onView(withId(R.id.edtUserIdForm)).perform(typeText("1"))
        onView(withId(R.id.btnReplaceUser)).perform(click())
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnClearUsers)).perform(click())
        onView(withId(R.id.edtGetUserFromDBId)).perform(typeText("1"))
        onView(withId(R.id.btnGetFromDb)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.txtUserName)).check(matches(withText("")))
        onView(withId(R.id.txtUserEmail)).check(matches(withText("")))
        onView(withId(R.id.txtUserPhone)).check(matches(withText("")))
    }

    private fun readJsonFromResources(fileName: String): String {
        return this.javaClass.classLoader
            .getResourceAsStream(fileName)
            .bufferedReader().use { it.readText() }
    }
}