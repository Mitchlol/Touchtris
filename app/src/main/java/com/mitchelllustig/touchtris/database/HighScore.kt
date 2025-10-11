package com.mitchelllustig.touchtris.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class HighScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val timestamp: Long,
    val score: Long,
    val level: Int,
    @ColumnInfo(name = "memory_count") val memoryCount: Int,
    @ColumnInfo(name = "next_piece_count") val nextPieceCount: Int,
    @ColumnInfo(name = "ghost_piece") val ghostPiece: Int,
    @ColumnInfo(name = "hold_piece") val holdPiece: Int,
    @ColumnInfo(name = "random_bag") val randomBag: Int,
)

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM highscore")
    suspend fun getAll(): List<HighScore>

    @Query("""
SELECT 
    * 
FROM 
    highscore 
WHERE 
    memory_count LIKE :memoryCount 
    AND next_piece_count LIKE :nextPieceCount 
    AND ghost_piece LIKE :ghostPiece
    AND hold_piece LIKE :holdPiece
    AND random_bag LIKE :randomBag
ORDER BY score DESC
""")
    suspend fun findByGameMode(memoryCount: Int, nextPieceCount: Int, ghostPiece: Int, holdPiece: Int, randomBag: Int): List<HighScore>

    @Query("""
SELECT 
    * 
FROM 
    highscore 
WHERE 
    memory_count LIKE :memoryCount 
    AND next_piece_count LIKE :nextPieceCount 
    AND ghost_piece LIKE :ghostPiece
    AND hold_piece LIKE :holdPiece
    AND random_bag LIKE :randomBag
ORDER BY score DESC
""")
    fun findByGameModeFlow(memoryCount: Int, nextPieceCount: Int, ghostPiece: Int, holdPiece: Int, randomBag: Int): Flow<List<HighScore>>

    @Query("""
SELECT * FROM highscore
WHERE
    memory_count = (SELECT memory_count FROM highscore WHERE id = :id)
    AND next_piece_count = (SELECT next_piece_count FROM highscore WHERE id = :id)
    AND ghost_piece = (SELECT ghost_piece FROM highscore WHERE id = :id)
    AND hold_piece = (SELECT hold_piece FROM highscore WHERE id = :id)
    AND random_bag = (SELECT random_bag FROM highscore WHERE id = :id)
ORDER BY score DESC
""")
    fun findBySameGameModeAsId(id: Long): Flow<List<HighScore>>

    @Insert
    suspend fun insert(score: HighScore): Long

    @Query("UPDATE highscore SET name = :name WHERE id = :id")
    suspend fun updateNameById(id: Long, name: String)

    @Insert
    suspend fun insertAll(vararg highScores: HighScore)

    @Delete
    suspend fun delete(highScore: HighScore)
}
