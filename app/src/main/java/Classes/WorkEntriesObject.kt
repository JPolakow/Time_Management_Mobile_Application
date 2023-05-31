package Classes

class WorkEntriesObject {

    var WEID: Int = 0
        private set(value) {
            field = value
        }
    fun getWEID(): Int {
        return WEID
    }

    var WEActivityID: Int = 0
        private set(value) {
            field = value
        }
    fun getWEActivityID(): Int {
        return WEActivityID
    }

    var WEUserID: Int = 0
        private set(value) {
            field = value
        }
    fun getWEUserID(): Int {
        return WEUserID
    }

    var WEDuration: Int = 0
        private set(value) {
            field = value
        }
    fun getWEDuration(): Int {
        return WEDuration
    }

    var WEDate: Int = 0
        private set(value) {
            field = value
        }
    fun getWEDate(): Int {
        return WEDate
    }
}