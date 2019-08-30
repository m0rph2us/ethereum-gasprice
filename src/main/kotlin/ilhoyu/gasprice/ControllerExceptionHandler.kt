package ilhoyu.gasprice

import com.googlecode.jsonrpc4j.JsonRpcClientException
import org.apache.juli.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerExceptionHandler {

    private val logger = LogFactory.getLog(ControllerExceptionHandler::class.java)

    @ExceptionHandler(JsonRpcClientException::class)
    fun handleJsonRpcClientException(e: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.error(e.message, e)
        return ResponseEntity(
                Response.error(Response.Code.ERROR_JSON_RPC_CLIENT.value),
                HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.error(e.message, e)
        return ResponseEntity(Response.error(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}