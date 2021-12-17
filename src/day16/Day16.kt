package day16

import checkAnswer
import readInput
import java.util.concurrent.atomic.AtomicInteger

fun main() {

    fun String.hexToBinary(): String = map {
            it.toString().toInt(16).toString(2).padStart(4, '0')
        }.joinToString("")


    fun parseSubPacket(binary: String): Pair<Packet,Int>{
        val version = binary.substring(0, 3).toInt(2)
        val typeId = binary.substring(3, 6).toInt(2)
        return when {
            typeId == 4 -> {
                var groupStart = 1
                var value = ""
                do {
                    groupStart += 5
                    value += binary.substring(groupStart+1, groupStart+5)
                } while(binary[groupStart] == '1')
                Pair(Packet(version, typeId, value = value.toLong(2)), groupStart+5)
            }
            binary[6] == '0' -> {
                val subPacketLength = binary.substring(7, 22).toInt(2)
                var pointer = 22
                val subPackets = mutableListOf<Packet>()
                while (pointer < 22+subPacketLength){
                    parseSubPacket(binary.substring(pointer)).apply {
                        subPackets.add(first)
                        pointer += second
                    }
                }
                Pair(Packet(version, typeId, subpackets = subPackets), pointer)
            }
            else -> {
                val subPacketCount = binary.substring(7, 18).toInt(2)
                var pointer = 18
                val subPackets = mutableListOf<Packet>()
                for(i in 1..subPacketCount){
                    parseSubPacket(binary.substring(pointer)).apply {
                        subPackets.add(first)
                        pointer += second
                    }
                }
                Pair(Packet(version, typeId, subpackets = subPackets), pointer)
            }
        }
    }

    fun parsePacket(packet: String): Packet{
        return parseSubPacket(packet.hexToBinary()).first
    }

    fun part1(input: String): Long {
        return parsePacket(input).getVersionSum()
    }

    fun part2(input: String): Long {
        return parsePacket(input).calculateValue()
    }

    fun getInput(file: String): String {
        return readInput(file)[0]
    }

    // test other methods
    checkAnswer("D2FE28".hexToBinary(), "110100101111111000101000", "hexToBinary")
    checkAnswer(parseSubPacket("110100101111111000101000"), Pair(Packet(6, 4, value = 2021L), 21), "parseSubPacket_literal")
    checkAnswer(parsePacket("38006F45291200"),
        Packet(1, 6, subpackets = mutableListOf(
            Packet(6, 4, value = 10),
            Packet(2, 4, value = 20)
        )),
        "parsePacket_operatorLengthType0")
    checkAnswer(parsePacket("EE00D40C823060"),
        Packet(7, 3, subpackets = mutableListOf(
            Packet(2, 4, value = 1),
            Packet(4, 4, value = 2),
            Packet(1, 4, value = 3)
        )),
        "parsePacket_operatorLengthType1")

    // test if implementation meets criteria from the description, like:
    checkAnswer(part1("8A004A801A8002F478"), 16, "part1 testInput1")
    checkAnswer(part1("620080001611562C8802118E34"), 12, "part1 testInput2")
    checkAnswer(part1("C0015000016115A2E0802F182340"), 23, "part1 testInput3")
    checkAnswer(part1("A0016C880162017C3686B18A3D4780"), 31, "part1 testInput4")
    //checkAnswer(part2(testInput), 1, "part2 testInput")

    val input = getInput("day16/input")
    println(part1(input)) // 860
    println(part2(input)) // 470949537659
}

data class Packet(
    val version: Int,
    val typeId: Int,
    val value: Long = -1L,
    val subpackets: MutableList<Packet> = mutableListOf()
){
    fun getVersionSum(): Long {
        return version + subpackets.sumOf { it.getVersionSum() }
    }

    fun calculateValue(): Long{
        return when(typeId){
            0 -> subpackets.sumOf { it.calculateValue() }
            1 -> subpackets.fold(1L){ sum, packet -> sum * packet.calculateValue()}
            2 -> subpackets.minOf { it.calculateValue() }
            3 -> subpackets.maxOf { it.calculateValue() }
            4 -> value
            5 -> if(subpackets[0].calculateValue() > subpackets[1].calculateValue()) 1 else 0
            6 -> if(subpackets[0].calculateValue() < subpackets[1].calculateValue()) 1 else 0
            7 -> if(subpackets[0].calculateValue() == subpackets[1].calculateValue()) 1 else 0
            else -> throw IllegalStateException("Unrecognised packet type $typeId")
        }
    }
}
