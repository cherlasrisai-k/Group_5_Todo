package com.example.todo.viewmodel

import app.cash.turbine.test
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import com.example.todo.data.User
import com.example.todo.data.UserDao
import com.example.todo.rules.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        userDao = FakeUserDao()
        taskDao = FakeTaskDao()
        viewModel = AuthViewModel(userDao, taskDao)
    }

    @Test
    fun `test registration with empty name`() {
        runTest {
            viewModel.validateAndRegister("", "1234567890", "password", "password")
            assertEquals("Name cannot be empty", viewModel.registerError)
        }
    }

    @Test
    fun `test registration with invalid mobile number`() {
        runTest {
            viewModel.validateAndRegister("Test User", "123", "password", "password")
            assertEquals("Enter a valid 10-digit mobile number", viewModel.registerError)
        }
    }

    @Test
    fun `test successful registration`() {
        runTest {
            viewModel.validateAndRegister("Test User", "1234567890", "password", "password")
            assertNull(viewModel.registerError)
            assertNotNull(viewModel.user)
        }
    }

    @Test
    fun `test login with empty mobile number`() {
        runTest {
            viewModel.validateAndLogin("", "password")
            assertEquals("Enter mobile number", viewModel.loginError)
        }
    }

    @Test
    fun `test login with invalid mobile number`() {
        runTest {
            viewModel.validateAndLogin("123", "password")
            assertEquals("Enter valid 10 digit number", viewModel.loginError)
        }
    }

    @Test
    fun `test successful login`() {
        runTest {
            // First, register a user to ensure there is a user to log in
            (userDao as FakeUserDao).users.add(User("1234567890", "Test User", "password"))

            viewModel.validateAndLogin("1234567890", "password")

            viewModel.loginRegister.test {
                // Check if the user is logged in
                assertNotNull(viewModel.user)
                assertEquals(true, viewModel.user?.isLoggedIn)

                // Check if there are no login errors
                assertNull(viewModel.loginError)

                // Check if the UI state is cleared
                val emission = awaitItem()
                assertEquals("", emission.mobile)
                assertEquals("", emission.name)
            }
        }
    }
}

class FakeUserDao : UserDao {
    val users = mutableListOf<User>()

    override suspend fun register(user: User) {
        users.add(user)
    }

    override suspend fun login(mobile: String, password: String): User? {
        return users.find { it.mobile == mobile && it.password == password }
    }

    override suspend fun getUserByMobile(mobile: String): User? {
        return users.find { it.mobile == mobile }
    }

    override suspend fun logout() {
        val loggedInUser = users.find { it.isLoggedIn }
        loggedInUser?.let {
            val index = users.indexOf(it)
            users[index] = it.copy(isLoggedIn = false)
        }
    }

    override suspend fun updateUser(user: User) {
        val index = users.indexOfFirst { it.mobile == user.mobile }
        if (index != -1) {
            users[index] = user
        }
    }

    override suspend fun deleteUser(user: User) {
        users.remove(user)
    }

    override suspend fun getLoggedInUser(): User? {
        return users.find { it.isLoggedIn }
    }

    override suspend fun updatePassword(mobile: String, password: String) {
        val user = users.find { it.mobile == mobile }
        user?.let {
            val index = users.indexOf(it)
            users[index] = it.copy(password = password)
        }
    }
}

class FakeTaskDao : TaskDao {
    val tasks = mutableListOf<Task>()

    override fun todayTasks(mobile: String): Flow<List<Task>> {
        return flowOf(tasks.filter { it.userMobile == mobile && !it.isCompleted })
    }

    override fun completedTasks(mobile: String): Flow<List<Task>> {
        return flowOf(tasks.filter { it.userMobile == mobile && it.isCompleted })
    }

    override suspend fun insert(task: Task) {
        tasks.add(task)
    }

    override suspend fun update(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    override suspend fun delete(task: Task) {
        tasks.remove(task)
    }

    override suspend fun deleteTasksByUser(mobile: String) {
        tasks.removeAll { it.userMobile == mobile }
    }

    override suspend fun deleteCompletedTasks(mobile: String) {
        tasks.removeAll { it.userMobile == mobile && it.isCompleted }
    }

    override suspend fun countTodayTasks(): Int {
        return tasks.count { !it.isCompleted }
    }
}
