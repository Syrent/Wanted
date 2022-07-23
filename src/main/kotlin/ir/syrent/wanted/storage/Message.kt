package ir.syrent.wanted.storage

/**
 * This class responsible
 */
enum class Message(val path: String) {
    RAW_PREFIX("general.raw_prefix"),
    PREFIX("general.prefix"),
    CONSOLE_PREFIX("general.console_prefix"),
    SUCCESSFUL_PREFIX("general.successful_prefix"),
    WARN_PREFIX("general.warn_prefix"),
    ERROR_PREFIX("general.error_prefix"),
    ONLY_PLAYERS("general.only_players"),
    VALID_PARAMS("general.valid_parameters"),
    UNKNOWN_MESSAGE("general.unknown_message"),
    NO_PERMISSION("command.no_permission"),
    WANTED_USAGE("command.wanted.usage"),
    WANTED_USE("command.wanted.use"),
    WANTED_RELOAD_USE("command.wanted.reload.use"),
    WANTED_NO_TARGET("command.wanted.no_target"),
    WANTED_ADD_USAGE("command.wanted.add.usage"),
    WANTED_ADD_USE("command.wanted.add.use"),
    WANTED_SET_USAGE("command.wanted.set.usage"),
    WANTED_SET_USE("command.wanted.set.use"),
    WANTED_TAKE_USAGE("command.wanted.take.usage"),
    WANTED_TAKE_USE("command.wanted.take.use"),
    EMPTY("");
}