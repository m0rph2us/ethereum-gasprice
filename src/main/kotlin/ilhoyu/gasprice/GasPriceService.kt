package ilhoyu.gasprice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class GasPrice(
        val gasPrice: BigDecimal,
        val count: Int
)

data class GasPriceSummary(
        val blockNumberLatest: Long?,   // 최신 블록의 Block Number (10 진수로)
        val transactionSize: Int,       // 블록의 트랜잭션 갯수
        val gasPriceAvg: BigDecimal,    // 트랜잭션의 gas price 의 평균값 (Gwei 단위로 소숫점 이하 1 자리까지)
        val gasPriceMax: BigDecimal,    // 트랜잭션의 gas price 의 최대값 (Gwei 단위로 소숫점 이하 1 자리까지)
        val gasPriceMin: BigDecimal,    // 트랜잭션의 gas price 의 최소값 (Gwei 단위로 소숫점 이하 1 자리까지)
        val gasPrices: List<GasPrice>   // 트랜잭션의 gas price 를 오름차순으로 정렬하고, 해당 gas price 의 트랜잭션 수를 함께 표시
                                        // 예를 들어 gas price 가 1.5 가 1 개 1.8 이 2 개라면
                                        // * prices: [{gasprice:1.5,count:1}, {gasprice:1.8, count:2}, ... ]
                                        // * 이런식으로 gas price 로 그룹핑해서 가격순으로 정렬하고 같은 해당 가격의 트랜잭션 수를 넣습니다.
) {

    enum class GasPriceSortedBy { ASC, DESC }

    companion object {

        fun mathContext() = MathContext(1, RoundingMode.HALF_EVEN)

        fun gasPricesFrom(transactions: List<EthTransaction>, sortOrder: GasPriceSortedBy): List<GasPrice> {
            val mc = mathContext()
            return transactions.map { tran ->
                tran.gasPrice.hexToBigDecimal().let { value ->
                    EthUnit.fromWei(value).toGwei().setScale(mc.precision, mc.roundingMode)
                }.let { value ->
                    Pair(value, 1)
                }
            }.groupingBy(Pair<BigDecimal, Int>::first).aggregate { _, acc: Int?, ele, _ ->
                (acc ?: 0) + ele.second
            }.map {
                GasPrice(it.key, it.value)
            }.toList().let { gasPrices ->
                when (sortOrder) {
                    GasPriceSortedBy.ASC -> gasPrices.sortedBy { it.gasPrice }
                    GasPriceSortedBy.DESC -> gasPrices.sortedByDescending { it.gasPrice }
                }
            }
        }

        fun gasPriceAvgFrom(transactions: List<EthTransaction>): BigDecimal {
            val mc = mathContext()
            return transactions.fold(BigDecimal.ZERO) {
                acc, e -> acc + e.gasPrice.hexToBigDecimal()
            }.let { total ->
                EthUnit.fromWei(total).toGwei()
            }.let { total ->
                total.divide(transactions.size.toBigDecimal(), mc.precision, mc.roundingMode)
            }
        }

        fun gasPriceMinFrom(gasPrices: List<GasPrice>) = gasPrices.minBy { it.gasPrice }!!.gasPrice

        fun gasPriceMaxFrom(gasPrices: List<GasPrice>) = gasPrices.maxBy { it.gasPrice }!!.gasPrice

        fun from(ethBlock: EthBlock, sortOrder: GasPriceSortedBy): GasPriceSummary {
            return gasPricesFrom(ethBlock.transactions, sortOrder).let { gasPrices ->
                GasPriceSummary(
                        ethBlock.number?.hexToLong(),
                        ethBlock.transactions.size,
                        gasPriceAvgFrom(ethBlock.transactions),
                        gasPriceMaxFrom(gasPrices),
                        gasPriceMinFrom(gasPrices),
                        gasPrices
                )
            }
        }

    }

}

interface GasPriceService {
    fun getLatestBlockGasPriceSummary(): GasPriceSummary?
}

@Service
class GasPriceServiceImpl @Autowired constructor(
        private val rpcClient: InfuraRpcClient
) : GasPriceService {

    override fun getLatestBlockGasPriceSummary(): GasPriceSummary? {
        return rpcClient.eth_getBlockByNumber(
                "latest", true
        ).let { ethBlock ->
            GasPriceSummary.from(ethBlock, GasPriceSummary.GasPriceSortedBy.ASC)
        }
    }

}