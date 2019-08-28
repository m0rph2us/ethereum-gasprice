package ilhoyu.gasprice

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@SpringBootTest
class RpcClientTest {

    @Autowired
    lateinit var rpcClient: InfuraEthRpcClient

    @Test
    fun `test`() {
        val ethBlock = rpcClient.eth_getBlockByNumber("latest", true)

        val v = ethBlock.transactions.map {
            Pair(EthUnit.fromWei(it.gasPrice.toBigDecimal2()).toGwei().round(2), 1)
        }.groupingBy(Pair<BigDecimal, Int>::first).aggregate { _, acc: Int?, ele, _ ->
            // 지급아이디별로 정산대상금액 합계를 구한다.
            (acc ?: 0) + ele.second
        }

        for (v in ethBlock.transactions) {
            val x = EthUnit.fromWei(v.gasPrice.toLong2().toBigDecimal()).toGwei()
            val a = EthUnit.fromWei(v.gasPrice.toBigDecimal2()).toGwei()
            val b = a.round(2)
            println("$x : $a : $b")
        }
    }

    @Test
    fun `xx`() {
        BigDecimal("1606.1738187790").divide(125.toBigDecimal())
    }

}