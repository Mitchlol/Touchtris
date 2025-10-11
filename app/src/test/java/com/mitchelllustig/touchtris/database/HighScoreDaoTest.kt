package com.mitchelllustig.touchtris.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mitchelllustig.touchtris.database.TouchtrisDB // Correct import
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class HighScoreDaoTest {

    private lateinit var highScoreDao: HighScoreDao
    private lateinit var db: TouchtrisDB // Correct database class

    // Game Mode A
    private val score1_modeA = HighScore(name = "AAA", timestamp = 1000L, score = 200, level = 8, memoryCount = 2, nextPieceCount = 3, ghostPiece = 1, holdPiece = 1, randomBag = 1)
    private val score2_modeA = HighScore(name = "BBB", timestamp = 2000L, score = 100, level = 5, memoryCount = 2, nextPieceCount = 3, ghostPiece = 1, holdPiece = 1, randomBag = 1)

    // Game Mode B
    private val score3_modeB = HighScore(name = "CCC", timestamp = 3000L, score = 150, level = 6, memoryCount = 0, nextPieceCount = 1, ghostPiece = 0, holdPiece = 0, randomBag = 0)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TouchtrisDB::class.java // Correct database class
        ).allowMainThreadQueries().build()
        highScoreDao = db.highScoreDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun `insert and getAll retrieves scores`() = runTest {
        highScoreDao.insert(score1_modeA)
        highScoreDao.insert(score3_modeB)
        val allScores = highScoreDao.getAll()
        assertEquals(2, allScores.size)
        assertTrue(allScores.any { it.name == "AAA" && it.score == 200L })
    }

    @Test
    fun `findByGameMode returns correct scores sorted`() = runTest {
        highScoreDao.insertAll(score1_modeA, score2_modeA, score3_modeB)

        val modeAResults = highScoreDao.findByGameMode(2, 3, 1, 1, 1)

        assertEquals(2, modeAResults.size)
        assertEquals(200, modeAResults[0].score)
        assertEquals(100, modeAResults[1].score)
    }

    @Test
    fun `findByGameModeFlow returns correct scores flow`() = runTest {
        highScoreDao.insertAll(score1_modeA, score2_modeA, score3_modeB)

        val modeBResults = highScoreDao.findByGameModeFlow(0, 1, 0, 0, 0).first()

        assertEquals(1, modeBResults.size)
        assertEquals(150, modeBResults[0].score)
    }

    @Test
    fun `updateNameById correctly changes name`() = runTest {
        val id = highScoreDao.insert(score1_modeA)
        val unchangedId = highScoreDao.insert(score2_modeA)
        highScoreDao.updateNameById(id, "NEW")

        val updatedScore = highScoreDao.getAll().first { it.id == id }
        assertEquals("NEW", updatedScore.name)

        val unchangedScore = highScoreDao.getAll().first { it.id == unchangedId }
        assertEquals(score2_modeA.name, unchangedScore.name)
    }

    @Test
    fun `delete removes score from database`() = runTest {
        val id1 = highScoreDao.insert(score1_modeA)
        val id2 = highScoreDao.insert(score3_modeB)

        highScoreDao.delete(score1_modeA.copy(id = id1))

        val allScores = highScoreDao.getAll()
        assertEquals(1, allScores.size)
        assertEquals(id2, allScores[0].id)
    }

    @Test
    fun `findBySameGameModeAsId returns correct list`() = runTest {
        val id1 = highScoreDao.insert(score1_modeA)
        highScoreDao.insert(score2_modeA)
        highScoreDao.insert(score3_modeB)

        val similarScores = highScoreDao.findBySameGameModeAsId(id1).first()
        assertEquals(2, similarScores.size)
        assertEquals(200, similarScores[0].score)
        assertEquals(100, similarScores[1].score)
    }
}
