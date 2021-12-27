package day24

class CalculatingMonad(program: List<String>):
    Monad<String>(program,mutableMapOf(
        'w' to "w", 'x' to "x",
        'y' to "y", 'z' to "z"
    )){

    override fun get(variable: String): String{
        return if(variable.matches(Regex("-?\\d+"))){
            variable
        } else variables[variable[0]]?: variable
    }

    override fun input(args: String, input: String)= set(args[0], input)
    override fun add(args: String) = instr(args){ n1,n2 ->
        if(n1.isNum() && n2.isNum()){
            (n1.toLong() + n2.toLong()).toString()
        }
        else if (n1 == "0") n2
        else if (n2 == "0") n1
        else "($n1+$n2)"
    }

    override fun multiply(args: String) = instr(args){ n1,n2 ->
        if(n1.isNum() && n2.isNum()){
            (n1.toLong() * n2.toLong()).toString()
        } else if(n1 == "0" || n2 == "0") "0"
        else if (n1 == "1") n2
        else if (n2 == "1") n1
        else "($n1*$n2)"
    }

    override fun divide(args: String) = instr(args){ n1,n2 ->
        if(n1.isNum() && n2.isNum()){
            (n1.toLong() / n2.toLong()).toString()
        } else if(n1 == "0") "0"
        else if (n1 == "1") "0"
        else if (n2 == "1") n1
        else if (n1 == n2) "1"
        else "($n1/$n2)"
    }

    override fun mod(args: String) = instr(args){ n1,n2 ->
        if(n1.isNum() && n2.isNum()){
            (n1.toLong() % n2.toLong()).toString()
        } else if(n1 == "0") "0"
        else if (n1 == "1") "1"
        else if (n2 == "1") "0"
        else if (n1 == n2) "0"
        else "($n1%$n2)"
    }

    override fun equal(args: String) = instr(args){ n1,n2 ->
        if(n1.isNum() && n2.isNum()){
            if(n1.toLong() == n2.toLong()) "1" else "0"
        }
        else if(n1.isVar() && n2.isNum() && !LongRange(1,9).contains(n2.toLong())){
            "0"
        }
        else if(n2.isVar() && n1.isNum() && !LongRange(1,9).contains(n1.toLong())){
            "0"
        }
        else "($n1==$n2)"
    }
}

fun String.isNum(): Boolean = matches(Regex("-?\\d+"))
fun String.isVar(): Boolean = matches(Regex("n\\d+"))
