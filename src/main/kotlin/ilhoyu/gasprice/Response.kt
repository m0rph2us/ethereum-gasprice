package ilhoyu.gasprice

data class Response(
    val code: Int,
    val message: String,
    val data: Any?
) {

    enum class Code(val value: Int, val message: String) {
        SUCCESS(0, "Success."),
        ERROR(100, "Internal service error."),
        ERROR_JSON_RPC_CLIENT(200, "Json rpc client error.")
    }

    companion object {

        fun success(data: Any?): Response {
            return Response(Code.SUCCESS.value, Code.SUCCESS.message, data)
        }

        fun error(code: Int = Code.ERROR.value, message: String = Code.ERROR.message): Response {
            return Response(code, message, null)
        }

    }

}