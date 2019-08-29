package ilhoyu.gasprice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class GasPriceController @Autowired constructor(
        val ethereumService: GasPriceService
) {

    @RequestMapping(
            value = ["gasprice"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun gasPrice(): ResponseEntity<Any> {
        val result = ethereumService.getLatestBlockGasPriceSummary()
        return ResponseEntity(Response.success(result), HttpStatus.OK)
    }

}