package day24

class ExecutingMonad(program: List<String>):
    Monad<Long>(program,mutableMapOf(
        'w' to 0L, 'x' to 0L,
        'y' to 0L, 'z' to 0L
    )){

    override fun get(variable: String): Long{
        return if(variable.matches(Regex("-?\\d+"))){
            variable.toLong()
        } else variables[variable[0]]?:
        throw IllegalArgumentException("Bad variable: $variable")
    }

    override fun input(args: String, input: Long)= set(args[0], input)
    override fun add(args: String) = instr(args){ n1,n2 -> n1+n2}
    override fun multiply(args: String) = instr(args){ n1,n2 -> n1*n2 }
    override fun divide(args: String) = instr(args){ n1,n2 -> n1/n2}
    override fun mod(args: String) = instr(args){ n1,n2 -> n1%n2}
    override fun equal(args: String) = instr(args){ n1,n2 ->
        if(n1 == n2) 1 else 0
    }
}