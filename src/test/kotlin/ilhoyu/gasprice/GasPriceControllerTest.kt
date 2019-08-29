package ilhoyu.gasprice

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal

@AutoConfigureMockMvc
@RunWith(SpringRunner::class)
@SpringBootTest
class GasPriceControllerTest {

    @Autowired
    lateinit var wac: WebApplicationContext

    @MockBean
    @Autowired
    lateinit var gasPriceService: GasPriceService

    var mockMvc: MockMvc? = null

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    fun gasPriceRequest(): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get("/api/v1/gasprice")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
    }

    @Test
    fun `요청이 성공했을 경우 성공에 대한 JSON 데이터가 반환되어야 한다`() {
        whenever(
                gasPriceService.getLatestBlockGasPriceSummary()
        ).then {
            GasPriceSummary(
                    blockNumberLatest = 123456,
                    transactionSize = 1,
                    gasPriceAvg = BigDecimal("1.1"),
                    gasPriceMax = BigDecimal("1.1"),
                    gasPriceMin = BigDecimal("1.1"),
                    gasPrices = listOf(GasPrice(BigDecimal("1.1"), 1))
            )
        }

        mockMvc!!.perform(gasPriceRequest()).run {
            andExpect(status().isOk)

            andExpect(
                    ResultMatcher.matchAll(
                            jsonPath("$.code", Matchers.`is`(Response.Code.SUCCESS.value)),
                            jsonPath("$.message", Matchers.`is`(Response.Code.SUCCESS.message)),
                            jsonPath("$.data.blockNumberLatest", Matchers.`is`(123456)),
                            jsonPath("$.data.transactionSize", Matchers.`is`(1)),
                            jsonPath("$.data.gasPriceAvg", Matchers.`is`(1.1)),
                            jsonPath("$.data.gasPriceMax", Matchers.`is`(1.1)),
                            jsonPath("$.data.gasPriceMin", Matchers.`is`(1.1)),
                            jsonPath("$.data.gasPrices[0].gasPrice", Matchers.`is`(1.1)),
                            jsonPath("$.data.gasPrices[0].count", Matchers.`is`(1))
                    )
            )
        }
    }

    @Test
    fun `예외가 발생했을 경우 내부 서비스 오류에 대한 JSON 데이터가 반환되어야 한다`() {
        whenever(
                gasPriceService.getLatestBlockGasPriceSummary()
        ).then {
            throw Exception()
        }

        mockMvc!!.perform(gasPriceRequest()).run {
            andExpect(status().isInternalServerError)

            andExpect(
                    ResultMatcher.matchAll(
                            jsonPath("$.code", Matchers.`is`(Response.Code.ERROR.value)),
                            jsonPath("$.message", Matchers.`is`(Response.Code.ERROR.message)),
                            jsonPath("$.data", Matchers.isEmptyOrNullString())
                    )
            )
        }
    }

}