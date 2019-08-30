package ilhoyu.gasprice

import ilhoyu.gasprice.config.InfuraRpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.reflect.UndeclaredThrowableException

interface GasPriceService {
    fun getLatestBlockGasPriceSummary(): GasPriceSummary?
}

@Service
class GasPriceServiceImpl @Autowired constructor(
        private val rpcClient: InfuraRpcClient
) : GasPriceService {

    override fun getLatestBlockGasPriceSummary(): GasPriceSummary? {
        return try {
            rpcClient.eth_getBlockByNumber(
                    "latest", true
            ).let { ethBlock ->
                GasPriceSummary.from(ethBlock, GasPriceSummary.GasPriceSortedBy.ASC)
            }
        } catch (e: UndeclaredThrowableException) {
            throw e.undeclaredThrowable
        }
    }

}