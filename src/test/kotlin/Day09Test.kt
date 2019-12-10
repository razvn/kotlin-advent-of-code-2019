import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day09Test : StringSpec({

    "test new commands computer" {
        forall(
                row(listOf<Long>(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99), listOf<Long>(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)),
                row(listOf<Long>(1102,34915192,34915192,7,4,7,99,0), listOf<Long>(1219070632396864)),
                row(listOf<Long>(104,1125899906842624,99), listOf<Long>(1125899906842624))
        ) { input, result ->
            val computer = InitCodeComputerDay09(input)
            val resp = computer.runProgram(
                    InitCodeComputerDay09.AdventQueue(1L),
                    true,
                    false
            )
            resp.output shouldBe result
        }
    }
})