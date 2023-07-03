
fun main() {
    
    val board = Chessboard()
    
    board.addPiece(Pawn(3, 4, -1, board))
    board.addPiece(Pawn(2, 2, 1, board))

    board.printBoard()

    var (a, b) = board.getSquare() ?: Pair(1, 1)
    var pp = board.squares[a - 1][b - 1]
    if (pp == null) {println("can't find piece")}

    board.move(pp, 2, 3)

    board.printBoard() 

}

//Parent function for chess pieces
abstract class Piece (x: Int, y: Int, side: Int, board: Chessboard) {
    var x: Int = x;
    var y: Int = y;
    abstract val type: String;
    var side = side;
    var board = board;

    //Boolean Predicate. Returns true if the position of the input Piece matches the input board coordinates
    fun matchPos (p: Piece, i: Number, j: Number): Boolean {

        if (p.x == i && p.y == j) {return true;}

        else {return false;}
    }

    abstract fun doMove(i: Int, j: Int): Boolean;

}

//Inherited class for pawn
class Pawn(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {
    
    override val type = "P";
    var alreadyMoved = false;

    //en passant yet to be implemented
    //moves the pawn to the input square
    override fun doMove(i: Int, j: Int): Boolean {

        //Path 1, straight ahead, 1 square
        if (i == x && j == y + side) {
            //checks if there is a piece already occupying the target square
            if (board.squares[i - 1][j - 1] != null) {
                println("Square is already occupied!")
                return false;
            }
        }

        //Path 2, straight ahead, 2 squares
        else if (i == x && j == y + (2 * side)) {
            //checks if piece has already moved
            if (alreadyMoved == true) {
                println("Piece has already moved!")
            }

            //checks if there is a piece already occupying the target square
            if (board.squares[i - 1][j - 1] != null) {
                println("Square is already occupied!")
                return false;
            }

            //checks if there is a piece blocking the way
            if (board.squares[i - 1][j - side - 1] != null) {
                println("There is a piece blocking the way!")
                return false;
            }
        }

        //Path 3, take diagonally
        else if (Math.abs(i - x) == 1 && j == y + side) {

            //checks if there is no piece at the target square
            if (board.squares[i - 1][j - 1] == null) {
                println("Square is empty!")
                return false;
            }

            //checks if the pieces are the same colour
            if (board.squares[x - 1][y - 1]!!.side == side) {
                println("Cannot capture a piece of the same colour!")
                return false;
            }
        }

        //Path 4, invalid input square
        else {
            println("Invalid input square!")
            return false;
        }

        return true;
    }


}

class Chessboard () {

    var squares = Array<Array<Piece?>>(8) {arrayOfNulls(8)}
    val letterToNumber = mapOf<String, Int>("a" to 1, "b" to 2, "c" to 3, "d" to 4, "e" to 5, "f" to 6, "g" to 7, "h" to 8)

    //adds input piece to squares array
    fun addPiece (p: Piece) {
        squares[p.x - 1][p.y - 1] = p;
    }

    //Call input Piece object's move function and adjust squares array accordingly
    fun move(p: Piece?, i: Int, j: Int) {

        if ( !(p?.doMove(i, j) ?: false)) {
            println("move failed")
        }
        else {
            squares[i - 1][j - 1] = p;
            squares[p!!.x - 1][p!!.y - 1] = null;

            squares[i - 1][j - 1]?.x = i;
            squares[i - 1][j - 1]?.y = j;
        } 
    }

    //get input square
    fun getSquare(): Pair<Int, Int>? {

        //file input
        println("Input file of piece to move")
        val xString = readLine()
        if (!(xString in letterToNumber)) {
            println("Invalid input")
            return null
        }
        val x = letterToNumber[xString] ?: 1
        println(x)

        //rank input
        println("Input rank of piece to move") 
        val yString = readLine()
        if (yString == null) {
            println("Null input")
            return null
        }
        val y = yString.toInt()
        if (!(y in letterToNumber.values)) {
            println("Invalid input")
            return null
        }

        return Pair(x, y)
    }

    //Prints out board to command line
    //i is y value
    //j is x value
    fun printBoard () {

        for (i in 0..7) {
            print(8 - i)

            for (j in 0..7){

                print(squares[j][7 - i]?.type ?: " ")
                print(" ")
            }
            println()
        }
        println(" a b c d e f g h")
    }
}

