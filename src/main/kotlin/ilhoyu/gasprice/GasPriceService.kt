package ilhoyu.gasprice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * • 최신 블록의 Block Number (10 진수로)
 * • 블록의 트랜잭션 갯수
 * • 트랜잭션의 gas price 의 평균값 (Gwei 단위로 소숫점 이하 1 자리까지)
 * • 트랜잭션의 gas price 의 최대값 (Gwei 단위로 소숫점 이하 1 자리까지)
 * • 트랜잭션의 gas price 의 최소값 (Gwei 단위로 소숫점 이하 1 자리까지)
 * • 트랜잭션의 gas price 를 오름차순으로 정렬하고, 해당 gas price 의 트랜잭션 수를 함께 표시
 *
 * 예를 들어 gas price 가 1.5 가 1 개 1.8 이 2 개라면
 * prices: [{gasprice:1.5,count:1}, {gasprice:1.8, count:2}, ... ]
 * 이런식으로 gas price 로 그룹핑해서 가격순으로 정렬하고 같은 해당 가격의 트랜잭션 수를 넣습니다.
 */
data class GasPrice(
        val gasPrice: BigDecimal,
        val count: Int
)

data class GasPriceSummary(
        val blockNumberLatest: Long?,
        val transactionSize: Int,
        val gasPriceAvg: BigDecimal,
        val gasPriceMax: BigDecimal,
        val gasPriceMin: BigDecimal,
        val gasPrices: List<GasPrice>
) {

    companion object {
        fun from(ethBlock: EthBlock): GasPriceSummary {
            val gasPrices = ethBlock.transactions.map {
                Pair(EthUnit.fromWei(it.gasPrice.toBigDecimal2()).toGwei().round(2), 1)
            }.groupingBy(Pair<BigDecimal, Int>::first).aggregate { _, acc: Int?, ele, _ ->
                (acc ?: 0) + ele.second
            }.map {
                GasPrice(it.key, it.value)
            }.toList()

            val gasPriceAvg = EthUnit.fromWei(
                    ethBlock.transactions.fold(BigDecimal.ZERO) { acc, e -> acc + e.gasPrice.toBigDecimal2() }
            ).toGwei().let {
                it.divide(ethBlock.transactions.size.toBigDecimal(),2, RoundingMode.HALF_EVEN)
            }

            val gasPriceMax = gasPrices.maxBy { it.gasPrice }!!.gasPrice

            val gasPriceMin = gasPrices.minBy { it.gasPrice }!!.gasPrice

            return GasPriceSummary(
                    ethBlock.number?.toLong2(),
                    ethBlock.transactions.size,
                    gasPriceAvg,
                    gasPriceMax,
                    gasPriceMin,
                    gasPrices
            )
        }
    }

}

class EthUnit(private val eth: BigDecimal) {

    companion object {
        fun fromWei(value: BigDecimal): EthUnit {
            return EthUnit(
                    value.divide(Math.pow(10.0, 18.0).toBigDecimal())
            )
        }
    }

    fun toGwei(): BigDecimal {
        return eth.multiply(Math.pow(10.0, 9.0).toBigDecimal())
    }

}

fun BigDecimal.round(at: Int): BigDecimal {
    return this.setScale(at, RoundingMode.HALF_EVEN)
}

interface EthService {
    fun getLatestBlockGasPriceSummary(): GasPriceSummary?
}

@Service
class EthServiceImpl @Autowired constructor(
        private val rpcClient: InfuraEthRpcClient
) : EthService {
    override fun getLatestBlockGasPriceSummary(): GasPriceSummary? {
        val ethBlock = rpcClient.eth_getBlockByNumber("latest", true)

        return GasPriceSummary.from(ethBlock)
    }
}