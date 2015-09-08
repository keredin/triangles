package generator

import pyramid._

import scala.util.Random

/**
 * Created by maciej on 26.08.15.
 */
class PyramidGenerator(val numberOfRows: Int, val maxNum: Int = 100) {
	var equations : Set[Equation] = Set()

	def generate(): Pyramid = {
		var rows = Seq[Row]()
		var lastRow : Option[Row] = None
		(1 to numberOfRows).foreach(rowId => {
//			println(lastRow)
			val newRow = generateRow(lastRow)
			lastRow = Option(newRow)
			rows = rows :+ newRow
		})

		new Pyramid(rows)
	}

	private def generateRow(rowBefore: Option[Row]): Row = {
		rowBefore match {
			case None => {
				val sides = Seq(None, None, Option(getEquation))
				new Row(1, Seq(TrianglePointingUp(None, None, Option(getEquation))))
			}
			case Some(oldRow) => {
				val newRowNum = oldRow.rowNum + 1
				val lastOldTriangle = oldRow.triangles.last
				var triangles = Seq[Triangle]()
				val firstSides = Seq(None, Option(getEquation), Option(getEquation))
				val bottom1 = if (newRowNum == numberOfRows) None else Option(getEquation)
				triangles = triangles :+ TrianglePointingUp(None, Option(getEquation), bottom1)

				oldRow.triangles
					.filter(oldTriangle => oldTriangle.isInstanceOf[TrianglePointingUp])
					.foreach(topTriangle => {
						val top = topTriangle.asInstanceOf[TrianglePointingUp]
						val topValue = top.bottomSide.get.value
						val left = triangles.last.asInstanceOf[TrianglePointingUp]
						val leftValue = left.rightSide.get.value
						val rightSide = getEquation

						triangles = triangles :+ TrianglePointingDown(
							Result(topValue),
							Result(leftValue),
							rightSide
						)

						var rightSide2 : Option[Equation] = None
						if (!lastOldTriangle.eq(top)) {
							rightSide2 = Option(getEquation)
						}
						val bottom2 = if (newRowNum == numberOfRows) None else Option(getEquation)
						triangles = triangles :+ TrianglePointingUp(
							Option(Result(rightSide.value)),
							rightSide2,
							bottom2
						)
					})
				new Row(newRowNum, triangles)
			}
		}
	}

	private def getEquation: Equation = {
		var eq : Equation = null

		val result = Random.nextInt(maxNum - 10) + 10
//		println(s"Result: $result")
		do {
			if (Random.nextBoolean()) {
				val operand1 = Random.nextInt(result)
				val operand2 = result - operand1
				eq = new Sum(operand1, operand2)
			} else {
				val operand2 = result - Random.nextInt(result)
				eq = new Subtract(result, operand2)
			}
		} while (equations.contains(eq))

		equations = equations + eq
		eq
	}

}
