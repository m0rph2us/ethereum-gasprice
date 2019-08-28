package ilhoyu.gasprice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class GasPriceController @Autowired constructor(
        val ethService: EthService
) {

    @RequestMapping(value = "gasprice", method = [RequestMethod.GET], produces = ["application/json"])
    fun gasPrice(): String {
        return ethService.getLatestBlockGasPriceSummary().toString()
    }

}