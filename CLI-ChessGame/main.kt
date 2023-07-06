
fun main() {
    
    val board = Chessboard()
    
    //sets up white pawns
    for (i in 1..8) {
        board.addPiece(Pawn(i, 2, 1, board))
    }

    //sets up black pawns
    for (i in 1..8) {
        board.addPiece(Pawn(i, 7, -1, board))
    }

    //white pieces
    board.addPiece(Rook(1, 1, 1, board))
    board.addPiece(Knight(2, 1, 1, board))
    board.addPiece(Bishop(3, 1, 1, board))
    board.addPiece(Queen(4, 1, 1, board))
    board.addPiece(King(5, 1, 1, board))
    board.addPiece(Bishop(6, 1, 1, board))
    board.addPiece(Knight(7, 1, 1, board))
    board.addPiece(Rook(8, 1, 1, board))

    //black pieces
    board.addPiece(Rook(1, 8, -1, board))
    board.addPiece(Knight(2, 8, -1, board))
    board.addPiece(Bishop(3, 8, -1, board))
    board.addPiece(Queen(4, 8, -1, board))
    board.addPiece(King(5, 8, -1, board))
    board.addPiece(Bishop(6, 8, -1, board))
    board.addPiece(Knight(7, 8, -1, board))
    board.addPiece(Rook(8, 8, -1, board))

    board.printBoard()

    
    while (true) {

        println("Select piece to move")
        var (a, b) = board.getSquare() ?: Pair(1, 1)
        var pp = board.squares[a - 1][b - 1]
        if (pp == null) {
            println("can't find piece")
        }
        else {
            println("Select square to move piece to")
            var (i, j) = board.getSquare() ?: Pair(1, 1)
            board.move(pp, i, j)
            board.printBoard() 
        }
    }
}

//Parent function for chess pieces
abstract class Piece (x: Int, y: Int, side: Int, board: Chessboard) {
    var x: Int = x;
    var y: Int = y;
    abstract var type: String;
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
    
    override var type = "P";
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
                return false;
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
        }

        //Path 4, invalid input square
        else {
            println("Invalid input square!")
            return false;
        }

        alreadyMoved = true;
        return true;
    }
}

//Inherited class for rook
class Rook(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {

    override var type = "R";

    //moves the rook to the input square
    override fun doMove(i: Int, j: Int): Boolean {

        //invalid square: not on same file or rank 
        if (i != x && j != y) {
            println("input square not on same file or rank as selected rook")
            return false;
        }

        //check along rank for obstructing piece
        for (f in Math.min(x,i) + 1..Math.max(x, i) - 1) {
            if (board.squares[f - 1][y - 1] != null) {
                println("piece obstructing path along rank to input square")
                return false;
            }
        }

        //check along file for obstructing piece
        for (f in Math.min(y,j) + 1..Math.max(y, j) - 1) {
            if (board.squares[x - 1][f - 1] != null) {
                println("piece obstructing path along file to input square")
                return false;
            }
        }

        return true;

    }
}

class Bishop(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {

    override var type = "B";
    
    override fun doMove(i: Int, j: Int): Boolean {

        //checks if input square is on same diagonal as selected bishop
        if (Math.abs(x - i) != Math.abs(y - j)) {
            println("input square not on same diagonal as selected bishop")
            return false;
        }

        val xdif = i - x    
        val ydif = j - y

        //checks for obstructing pieces along diagonal
        for (c in 1..Math.abs(xdif) - 1) {
            //index = final value - (sub)
            //sub is the the larger value between the iterator and the distance subtracted by the smaller value
            if (board.squares[i - (Math.max(c, xdif) - Math.min(c, xdif))][j - (Math.max(c, ydif) - Math.min(c, ydif))] != null) {
                println("piece obstructing diagonal path to input square")
                return false;
            }
        }

        return true;
    }
}

class Knight(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {

    override var type = "N";

    override fun doMove(i: Int, j: Int): Boolean {

        val xdif = Math.abs(i - x);
        val ydif = Math.abs(j - y);

        //checks if one dif value is one and other dif value is 2. if not return false
        if (!((xdif == 1|| ydif == 1) && (xdif == 2|| ydif == 2))) {
            println("Knight piece cannot maneuver to target square")
            return false;
        }

        return true;
    }
}

class Queen(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {

    override var type = "Q"

    override fun doMove(i: Int, j: Int): Boolean {

        //Path 1, treat the queen as a rook
        if ((i == x && j != y) || (i != x && j == y)) {

            //check along rank for obstructing piece
            for (f in Math.min(x,i) + 1..Math.max(x, i) - 1) {
                if (board.squares[f - 1][y - 1] != null) {
                    println("piece obstructing path along rank to input square")
                    return false;
                }
            }

            //check along file for obstructing piece
                for (f in Math.min(y,j) + 1..Math.max(y, j) - 1) {
                if (board.squares[x - 1][f - 1] != null) {
                    println("piece obstructing path along file to input square")
                    return false;
                }
            }
            
            return true;
        }

        //Path 2, treat the queen like a bishop
        else if (Math.abs(x - i) == Math.abs(y - j)) {

            val xdif = i - x    
            val ydif = j - y

            //checks for obstructing pieces along diagonal
            for (c in 1..Math.abs(xdif) - 1) {
                //index = final value - (sub)
                //sub is the the larger value between the iterator and the distance subtracted by the smaller value
                if (board.squares[i - (Math.max(c, xdif) - Math.min(c, xdif))][j - (Math.max(c, ydif) - Math.min(c, ydif))] != null) {
                    println("piece obstructing diagonal path to input square")
                    return false;
                }
        }

            return true;
        }

        //Path 3, invalid target square
        else {

            println("invalid input square")
            return false;
        }
    }   
}

class King(x: Int, y: Int, side: Int, board: Chessboard) : Piece(x, y, side, board) {

    override var type = "K";

    override fun doMove(i: Int, j: Int): Boolean {

        if (Math.abs(i - x) > 1 || Math.abs(j - y) > 1) {
            println("King can only move one square at a time")
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
        
        if (p.side == -1) {
            p.type = p.type.lowercase()
        }
        squares[p.x - 1][p.y - 1] = p;
    }

    //Call input Piece object's move function and adjust squares array accordingly
    fun move(p: Piece?, i: Int, j: Int) {

        if (squares[i - 1][j - 1]?.side == p?.side) {
            println("piece of same colour occupying input square")
            return;
        }

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

        val input = readLine() ?: "a1"
        val xString: String = input[0].toString()
        if (!(xString in letterToNumber)) {
            println("Invalid input")
            return null
        }
        val x = letterToNumber[xString] ?: 1

        val y: Int = input[1].toString().toInt()

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
            print("${8 - i}  ")

            for (j in 0..7){

                print(squares[j][7 - i]?.type ?: " ")
                print(" ")
            }
            println()
        }
        println()
        println("   a b c d e f g h")
    }
}

