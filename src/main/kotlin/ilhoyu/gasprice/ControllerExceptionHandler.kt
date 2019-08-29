package ilhoyu.gasprice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.lang.Exception

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(Response.error(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}