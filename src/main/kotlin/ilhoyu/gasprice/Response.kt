package ilhoyu.gasprice

data class Response(
    val code: Int,
    val message: String,
    val data: Any?
) {

    enum class Code(val value: Int, val message: String) {
        SUCCESS(0, "Success."),
        ERROR(1, "Internal service error.")
    }

    companion object {

        fun success(data: Any?): Response {
            return Response(Code.SUCCESS.value, Code.SUCCESS.message, data)
        }

        fun error(message: String? = null): Response {
            return Response(Code.ERROR.value, message ?: Code.ERROR.message, null)
        }

    }

}