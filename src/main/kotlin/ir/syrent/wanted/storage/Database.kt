package ir.syrent.wanted.storage

import ir.syrent.wanted.WPlayer
import me.mohamad82.ruom.Ruom
import me.mohamad82.ruom.configuration.YamlConfig
import me.mohamad82.ruom.database.Priority
import me.mohamad82.ruom.database.Query
import me.mohamad82.ruom.database.mysql.MySQLCredentials
import me.mohamad82.ruom.database.mysql.MySQLDatabase
import me.mohamad82.ruom.database.sqlite.SQLiteDatabase
import org.bukkit.configuration.ConfigurationSection
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture


class Database {

    private var database: me.mohamad82.ruom.database.Database? = null
    var type: DBType = DBType.SQLITE

    init {
        val storage = YamlConfig(Ruom.getPlugin().dataFolder, "storage.yml")

        if (storage.config.getString("type").equals("MySQL", true)) {
            val section: ConfigurationSection = storage.config.getConfigurationSection("mysql")!!
            val credentials = MySQLCredentials.mySQLCredentials(
                section.getString("address"),
                section.getInt("port"),
                section.getString("database"),
                section.getBoolean("ssl"),
                section.getString("username"),
                section.getString("password")
            )
            database = MySQLDatabase(credentials, section.getInt("pooling_size"))
            type = DBType.MYSQL
        } else {
            database = SQLiteDatabase(File(Ruom.getPlugin().dataFolder, "storage.db"))
        }

        database!!.connect()
        database!!.queueQuery(Query.query("CREATE TABLE IF NOT EXISTS wanted_players (UUID VARCHAR(64), username VARCHAR(16), wanted INT, PRIMARY KEY (UUID));"), Priority.HIGHEST)
    }

    fun shutdown() {
        database!!.shutdown()
    }

    fun saveWPlayer(wPlayer: WPlayer) {
        database!!.queueQuery(
            Query.query("SELECT UUID FROM wanted_players WHERE UUID = ?;")
            .setStatementValue(1, wPlayer.uuid.toString())).completableFuture.whenComplete { result, _ ->
            if (!result.next()) {
                database!!.queueQuery(
                    Query.query("INSERT ${if (type == DBType.MYSQL) "IGNORE " else ""}INTO wanted_players (UUID, username, wanted) VALUES (?,?,?);")
                    .setStatementValue(1, wPlayer.uuid.toString())
                    .setStatementValue(2, wPlayer.username)
                    .setStatementValue(3, wPlayer.wanted))
            } else {
                database!!.queueQuery(
                    Query.query("UPDATE wanted_players SET username = ?, wanted = ? WHERE UUID = ?;")
                    .setStatementValue(1, wPlayer.username)
                    .setStatementValue(2, wPlayer.wanted)
                    .setStatementValue(3, wPlayer.uuid.toString()))
            }
        }
    }

    fun getWPlayer(uuid: UUID): CompletableFuture<WPlayer> {
        val completableFuture = CompletableFuture<WPlayer>()
        database!!.queueQuery(
            Query.query("SELECT * FROM wanted_players WHERE UUID = ?;")
            .setStatementValue(1, uuid.toString())).completableFuture.whenComplete { result, _ ->
            completableFuture.complete(if (result.next()) WPlayer(uuid, result.getString("username"), result.getInt("wanted")) else null)
        }
        return completableFuture
    }

    fun saveWPlayers(wPlayers: List<WPlayer>) {
        for (wPlayer in wPlayers) {
            saveWPlayer(wPlayer)
        }
    }

    fun getWPlayers(): CompletableFuture<List<WPlayer>> {
        val completableFuture = CompletableFuture<List<WPlayer>>()
        val wPlayers = ArrayList<WPlayer>()

        database!!.queueQuery(Query.query("SELECT * FROM wanted_players;")).completableFuture.whenComplete { result, _ ->
            while (result.next()) {
                wPlayers.add(WPlayer(UUID.fromString(result.getString("UUID")), result.getString("username"), result.getInt("wanted")))
            }
        }
        completableFuture.complete(wPlayers)

        return completableFuture
    }

    fun hasStats(uuid: UUID): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()

        database!!.queueQuery(
            Query.query("SELECT UUID FROM wanted_players WHERE UUID = ?;")
                .setStatementValue(1, uuid.toString())).completableFuture.whenComplete { result, _ ->
            completableFuture.complete(result.next())
        }

        return completableFuture
    }

    companion object {
        val instance = Database()

        enum class DBType {
            MYSQL,
            SQLITE
        }
    }
}