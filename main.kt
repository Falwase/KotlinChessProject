
fun main() {
    
    val p1 = Piece(4, 5)
    val p2 = Piece(2, 2)



    val array = arrayOf(p1, p2)

    val board = Chessboard(array)
    board.printBoard()
}

//Parent function for chess pieces
class Piece (x: Number, y: Number) {
    var x = x;
    var y = y;
}

class Chessboard (array: Array<Piece>) {

    var array = array;

    //Boolean Predicate. Returns true if the position of the input Piece matches the input board coordinates
    fun matchPos (p: Piece, i: Number, j: Number): Boolean {

        if (p.x == i && p.y == j) {return true;}

        else {return false;}
    }

    //Prints out board to command line
    fun printBoard () {

        for (i in 1..8) {
            for (j in 1..8) {

                //If there is a piece at the current i and j value, print 'X'. Else print 'O'
                if (array.any {matchPos(it, i, j)}) {print("X")}
                else {print("O")}
            }
            println()
        }
    }
}