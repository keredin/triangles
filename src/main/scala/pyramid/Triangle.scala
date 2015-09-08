package pyramid

/**
 * Created by maciej on 07.09.15.
 */
sealed abstract class Triangle

case class TrianglePointingUp(
	leftSide: Option[Result],
	rightSide: Option[Equation],
	bottomSide: Option[Equation]
) extends Triangle {
	override def toString: String = {
		var str = " ("
		if (leftSide.isDefined) str += leftSide.get
		str += ", "
		if (rightSide.isDefined) str += rightSide.get
		str += ", "
		if (bottomSide.isDefined) str += (bottomSide.get + ") ")

		str
	}
}

case class TrianglePointingDown(
	topSide: Result,
	leftSide: Result,
	rightSide: Equation
) extends Triangle {
}

sealed abstract class Side

case class Result(result: Int) extends Side {
	override def toString: String = result.toString
}

abstract class Equation(val operand1: Int, val operand2: Int) extends Side {
	def value : Int = operation(operand1, operand2)

	def operation(num1: Int, num2: Int): Int
}

class Sum(override val operand1: Int, override val operand2: Int) extends Equation(operand1, operand2) {
	override def operation(num1: Int, num2: Int): Int = operand1 + operand2
	override def toString: String = s"$operand1 + $operand2"
}

class Subtract(override val operand1: Int, override val operand2: Int) extends Equation(operand1, operand2) {
	override def operation(num1: Int, num2: Int): Int = operand1 - operand2
	override def toString: String = s"$operand1 - $operand2"
}
