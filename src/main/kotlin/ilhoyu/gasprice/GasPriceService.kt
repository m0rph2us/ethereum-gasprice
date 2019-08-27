package ilhoyu.gasprice

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.math.BigDecimal

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
        val blockNumberLatest: Int?,
        val transactionSize: Int,
        val gasPriceAvg: BigDecimal,
        val gasPriceMax: BigDecimal,
        val gasPriceMin: BigDecimal,
        val gasPrices: List<GasPrice>
) {

    companion object {
        fun from(ethBlock: EthBlock): GasPriceSummary {
            return GasPriceSummary(
                    ethBlock.number?.toInt2(),
                    ethBlock.transactions.size,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    listOf()
            )
        }
    }

}

@Service
class GasPriceService {
    fun gasPrice(): GasPriceSummary {
        return GasPriceSummary(0, 0, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, listOf())
    }
}