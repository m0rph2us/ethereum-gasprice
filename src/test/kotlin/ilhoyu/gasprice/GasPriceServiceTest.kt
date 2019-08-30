package ilhoyu.gasprice

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import ilhoyu.gasprice.config.EthBlock
import ilhoyu.gasprice.config.EthTransaction
import ilhoyu.gasprice.config.InfuraRpcClient
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@SpringBootTest
class GasPriceServiceTest {

    @Autowired
    lateinit var gasPriceService: GasPriceService

    @MockBean
    @Autowired
    lateinit var rpcClient: InfuraRpcClient

    @Test
    fun `요약된 gas price 정보를 가져와야 한다`() {
        whenever(
                rpcClient.eth_getBlockByNumber(any(), any())
        ).then {
            EthBlock(
                    "0x80ebe1",
                    "0x113dd102d39c2886d33eaa98ddcd6cf2bbe6a7ee22dcd337838035ec776eaf2a",
                    "0xc0979538ccc09409a14224629e4ea869fdd4d318d862167d9fabf27251739746",
                    "0xdc414ffc3210787e",
                    "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
                    "0xd18920cc181424140148058a06090baa4df0028404068818214f498056801c145071" +
                            "001219f0e0044900300a96caf1503008391b412c040f81000911083805051138c0821111c" +
                            "0040a8ed408971c7a7854000004d140600a0080017d4282a2004040904382458441803d43" +
                            "482444046018406034049c86000466413010024020231601a6810c20021c826880ae52052" +
                            "12050141461a011402044980283f448ae330600b6248464c3540314802824842146140102" +
                            "6118051440c560a4160504021a3000720100384000883030514821101851820127180a640" +
                            "3810022525b02a3103ae604a4e4088ac91e085004443248040608610393c00d00b1622260" +
                            "947006",
                    "0xb9e9695ee5a7c37428dfe0a121439c9fae2d37a59779a0ce18a2e6bd3823bbfb",
                    "0x41eb124a97529ee042cfdf633130537422aa78de91f37982af0fc7df0f59a878",
                    "0xb9e9695ee5a7c37428dfe0a121439c9fae2d37a59779a0ce18a2e6bd3823bbfb",
                    "0x52bc44d5378309ee2abf1539bf71de1b7d7be3b5",
                    "0x4c44c082e36086bda6c50ee65941def05a37b3bf3ac113794c9a1904c80e63dc",
                    "0x80ba162e2a75e",
                    "0x2796a800a5a136ca430",
                    "0x50505945206e616e6f706f6f6c2e6f7267",
                    "0x8137",
                    "0x7a121d",
                    "0x79c40c",
                    "0x5d688fb2",
                    listOf(
                            EthTransaction(
                                    "0x113dd102d39c2886d33eaa98ddcd6cf2bbe6a7ee22dcd337838035ec776eaf2a",
                                    "0x80ebe1",
                                    "0x00a3c51905691cb84f3b6b0859de85b81e6bfec7",
                                    "0x9466",
                                    "0x77359400",
                                    "0x9503fbf1240d97b2e698d92c8d67ccc1b2722a308b82bacc0eddc237353668fd",
                                    "0xa9059cbb000000000000000000000000f801d3d3fb92c7fc914631981" +
                                            "64c5f027aba5506000000000000000000000000000000000000000000" +
                                            "0000000000000578b58b00",
                                    "0x1c",
                                    "0xab14e14a7164b75a04decab86a397c3eb39d5c1d28a659f591086d6de2d587b",
                                    "0x33bb91f695c6b6862ed87143fa60b2a59b2e7fcaca7b2de53a55e70029255a24",
                                    "0xc5faadd1206ca91d9f8dd015b3498affad9a58bc",
                                    "0x0",
                                    "0x1c",
                                    "0x0"
                            )
                    )
            )
        }

        gasPriceService.getLatestBlockGasPriceSummary().run {
            assertNotNull(this)

            this?.run {
                assertEquals(8448993, blockNumberLatest!!)
                assertEquals(1, transactionSize)
                assertEquals(BigDecimal("2.0"), gasPriceAvg)
                assertEquals(BigDecimal("2.0"), gasPriceMax)
                assertEquals(BigDecimal("2.0"), gasPriceMin)
                assertEquals(1, gasPrices.size)
                assertEquals(BigDecimal("2.0"), gasPrices[0].gasPrice)
                assertEquals(1, gasPrices[0].count)
            }
        }
    }

    @Test
    fun `블록 넘버가 null 이면 null 을 반환해야 한다`() {
        whenever(
                rpcClient.eth_getBlockByNumber(any(), any())
        ).then {
            EthBlock(null)
        }

        assertNull(gasPriceService.getLatestBlockGasPriceSummary())
    }

}