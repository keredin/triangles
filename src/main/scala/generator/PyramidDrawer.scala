package generator

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Font}
import javax.imageio.ImageIO

import pyramid.{TrianglePointingDown, TrianglePointingUp, Pyramid}

/**
 * Created by maciej on 06.09.15.
 */
class PyramidDrawer(height: Int = 500, width: Int = 500, foldersName: String) extends Drawer {
	override def draw(pyramid: Pyramid) = {
		val triangleWallSize = {
			val row = pyramid.rows.last
			val trianglesPointingUpCount = row.triangles.count(tri => tri.isInstanceOf[TrianglePointingUp])
//			println(s"$trianglesPointingUpCount triangles pointing up in bottom row")
			val sizeCandidate = (width-10) / trianglesPointingUpCount
//			println(s"size candidate: $sizeCandidate")

			if (sizeCandidate % 2 == 1) sizeCandidate - 1 else sizeCandidate
		}
		val wallMiddlePoint = (triangleWallSize / 2) + 1
		val triangleHeight = ((triangleWallSize * Math.sqrt(3)) / 2).toInt
		val rowsNum = pyramid.rows.size

		val rightTextOffsetX = 35

		// create an image
		val canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

		// get Graphics2D for the image
		val g = canvas.createGraphics()

		// clear background
		g.setColor(Color.WHITE)
		g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

		// enable anti-aliased rendering (prettier lines and circles)
		// Comment it out to see what this does!
		g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
			java.awt.RenderingHints.VALUE_ANTIALIAS_ON)

		g.setStroke(new BasicStroke(5))  // reset to default
		g.setFont(new Font("Batang", Font.PLAIN, 10))

		val triangleDrawer = new TriangleDrawer(triangleWallSize, triangleHeight, g)

		pyramid.rows.reverseIterator.foreach(row => {
			var lastX = 5 + ((rowsNum - row.rowNum) * wallMiddlePoint)
			val lastY = height - ((rowsNum - row.rowNum) * triangleHeight) - 5
			row.triangles.foreach {
				case tr : TrianglePointingUp => lastX = triangleDrawer.drawTriangleUp(tr, lastX, lastY)
				case tr : TrianglePointingDown => triangleDrawer.drawTriangleDown(tr, lastX, lastY)
			}
		})

		// done with drawing
		g.dispose()

		// write image to a file
		ImageIO.write(canvas, "png", new java.io.File(s"$foldersName/sciaga.png"))
	}
}
