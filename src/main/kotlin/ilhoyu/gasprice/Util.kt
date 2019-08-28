package ilhoyu.gasprice

import java.math.BigDecimal

fun String.toComputableHex() = replaceFirst("0x", "")

fun String.hexToLong(): Long = toComputableHex().toLong(16)

fun String.hexToBigDecimal() = toComputableHex().hexToLong().toBigDecimal()

class EthUnit(private val eth: BigDecimal) {

    private enum class ScaleToEth(val value: BigDecimal) {
        WEI(powOf(18.0)),
        GWEI(powOf(9.0)),
        ETH(powOf(0.0));
    }

    companion object {
        private fun powOf(factor: Double) = Math.pow(10.0, factor).toBigDecimal()

        fun fromWei(wei: BigDecimal): EthUnit {
            val eth = wei.divide(ScaleToEth.WEI.value)
            return EthUnit(eth)
        }
    }

    fun toGwei(): BigDecimal {
        return eth.multiply(ScaleToEth.GWEI.value)
    }

}