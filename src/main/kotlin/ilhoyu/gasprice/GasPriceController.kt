package ilhoyu.gasprice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException

@RestController
@RequestMapping("/api/v1/")
class GasPriceController @Autowired constructor(
        private val gasPriceService: GasPriceService,
        private val retryTemplate: RetryTemplate
) {

    @RequestMapping(
            value = ["gasprice"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun gasPrice(): ResponseEntity<Any> {
        return retryTemplate.execute<GasPriceSummary?, RuntimeException> {
            gasPriceService.getLatestBlockGasPriceSummary()
        }.let { result ->
            ResponseEntity(Response.success(result), HttpStatus.OK)
        }
    }

}