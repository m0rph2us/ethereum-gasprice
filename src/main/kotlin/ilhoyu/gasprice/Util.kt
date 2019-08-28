package ilhoyu.gasprice

import java.math.BigDecimal

fun String.toComputableHex(): String {
    return this.replaceFirst("0x", "")
}

fun String.toLong2(): Long {
    return this.toComputableHex().toLong(16)
}

fun String.toLongOrNull2(): Long? {
    return this.toComputableHex().toLongOrNull(16)
}

fun String.toBigDecimal2(): BigDecimal {
    return this.toComputableHex().toLong2().toBigDecimal()
}
