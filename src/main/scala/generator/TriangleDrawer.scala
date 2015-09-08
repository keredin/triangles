package generator

import java.awt.{Font, BasicStroke, Graphics2D, Color}
import java.awt.geom.Line2D

import pyramid.{TrianglePointingDown, TrianglePointingUp}

/**
 * Created by maciej on 07.09.15.
 */
class TriangleDrawer(val triangleWallSize: Int, val triangleHeight: Int, val g: Graphics2D) {
	val wallMiddlePoint = (triangleWallSize / 2) + 1
	val rightTextOffsetX = -60
	val rightTextOffsetY = 80
	val leftTextOffsetX = -30
	val leftTextOffsetY = 80
	val bottomTextOffsetY = -10
	val bottomTextOffsetX = -30

	def setFontAndStroke: Unit = {
		g.setStroke(new BasicStroke(5))  // reset to default
		g.setFont(new Font("Batang", Font.PLAIN, 20))
	}

	def drawTriangleUp(tr: TrianglePointingUp, startX: Int, startY:Int) : Int = {
		// draw a line
//		println("Print triangle up")
		setFontAndStroke

		val endX = startX + triangleWallSize
		val halfX = startX + wallMiddlePoint
		val top = startY - triangleHeight

//		println(s"Draw bottom line from $startX to $endX")
		g.setColor(Color.GREEN)
		g.draw(new Line2D.Double(startX, startY, endX, startY))
		tr.bottomSide match {
			case Some(bottom) =>
				g.drawString(bottom.toString, halfX + bottomTextOffsetX, startY - 4 + bottomTextOffsetY)
			case None =>
		}

		g.setColor(Color.BLUE)
		g.draw(new Line2D.Double(startX, startY, halfX, top))
		tr.leftSide match {
			case Some(left) =>
				g.drawString(left.toString, startX + (triangleWallSize /4) + leftTextOffsetX, startY - (triangleHeight/2) + leftTextOffsetY)
			case None =>
		}

		g.setColor(Color.RED)
		g.draw(new Line2D.Double(halfX, top, endX, startY))
		tr.rightSide match {
			case Some(right) => g.drawString(right.toString, halfX + (triangleWallSize / 4) + rightTextOffsetX, startY - (triangleHeight/2) + rightTextOffsetY)
			case None =>
		}

		endX
	}

	def drawTriangleDown(tr: TrianglePointingDown, startX: Int, startY:Int) = {
//		println("Print triangle down")
		setFontAndStroke

		g.setColor(Color.RED)
		g.drawString(tr.leftSide.toString, startX - (triangleWallSize / 4) + leftTextOffsetX, startY - (triangleHeight / 2) - leftTextOffsetY)

		val halfWall = triangleWallSize / 2
		g.setColor(Color.BLUE)
		g.draw(new Line2D.Double(startX, startY, startX + halfWall, startY - triangleHeight))
		g.drawString(tr.rightSide.toString, startX + (triangleWallSize / 4) + rightTextOffsetX , startY - (triangleHeight / 2) - rightTextOffsetY)

		g.setColor(Color.GREEN)
		val topY = startY - triangleHeight
		g.draw(new Line2D.Double(startX - halfWall, topY, startX + halfWall, topY))
		g.drawString(tr.topSide.toString, startX , startY - triangleHeight - bottomTextOffsetY + 20)
	}
}
