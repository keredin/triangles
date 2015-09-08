package generator

import java.awt.{Graphics2D, Color}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import pyramid.{TrianglePointingDown, TrianglePointingUp, Triangle, Pyramid}

/**
 * Created by maciej on 07.09.15.
 */
class PrintDrawer(val trianglesInRow: Int = 2, height: Int, width: Int, folderName: String) extends Drawer {
	val triangleWallSize = (width.toDouble / (trianglesInRow + 0.7)).toInt
	val triangleHeight = ((triangleWallSize * Math.sqrt(3)) / 2).toInt
	val rowsNum = (height - 10) / triangleHeight


	override def draw(pyramid: Pyramid): Unit = {
		val allTriangles: Seq[Triangle] = pyramid.rows.flatMap(row => row.triangles)

		// triangles pointing up or down
		val pointingUp = allTriangles.filter(triangle => triangle.isInstanceOf[TrianglePointingUp])
		val pointingDown = allTriangles.filter(triangle => triangle.isInstanceOf[TrianglePointingDown])
//		println(s"pointingUp: ${pointingUp.size}, pointingDown: ${pointingDown.size}")

		// triangles grouped by rows to print in
		val upGroups = pointingUp.grouped(trianglesInRow).toSeq
		val downGroups = pointingDown.grouped(trianglesInRow).toSeq
//		println(s"rowsUp: ${upGroups.size}, rowsDown: ${downGroups.size}, $trianglesInRow triangles in row")

		// rows grouped by page to print on
		val upPage = upGroups.grouped(rowsNum).toSeq
		val downPage = downGroups.grouped(rowsNum).toSeq
		val downPageIter = downPage.toIterator
//		println(s"upPages: ${upPage.size}, downPages: ${downPage.size}, $rowsNum rows on page")

		var pageNum = 1
		upPage.foreach(upRows => {

			val canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
			val graphic = getGraphic(canvas)
			val triangleDrawer = new TriangleDrawer(triangleWallSize, triangleHeight, graphic)

//			println(s"Print page $pageNum")
			val downRows = if (downPageIter.hasNext) Option(downPageIter.next) else None
			printPage(upRows, downRows, triangleDrawer)

			graphic.dispose()
			// write image to a file
			ImageIO.write(canvas, "png", new java.io.File(s"$folderName/trojkaty$pageNum.png"))
			pageNum = pageNum + 1
		})
	}

	def getGraphic(canvas: BufferedImage): Graphics2D = {
		val g = canvas.createGraphics()
		g.setColor(Color.WHITE)
		g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

		g
	}

	def printPage(upPageRws: Seq[Seq[Triangle]], downPageRws: Option[Seq[Seq[Triangle]]], trPainter: TriangleDrawer): Unit = {
//		println(s"Printing page up: ${upPageRws}, down ${downPageRws}")
		var rowNum = 0
		downPageRws match {
			case Some(downRows) => {
				val downRowsIter = downRows.iterator
				upPageRws.foreach(upTrs => {
					val downTrs = if (downRowsIter.hasNext) Option(downRowsIter.next()) else None
					printRow(upTrs, downTrs, rowNum, trPainter)
					rowNum = rowNum + 1
				})
			}
			case None => {
				upPageRws.foreach(upTrs => {
					printRow(upTrs, None, rowNum, trPainter)
					rowNum = rowNum + 1
				})
			}
		}
//		println("Page printed")
	}

	def printRow(upTriangles: Seq[Triangle], downTriangles: Option[Seq[Triangle]], rowNum : Int, triangleDrawer: TriangleDrawer): Unit = {
		var pairNum = 0
		downTriangles match {
			case Some(downTrs) => {
				val downIter = downTrs.iterator
				upTriangles.foreach(upTr => {
					val downTr = if (downIter.hasNext) Option(downIter.next()) else None
					printPair(upTr, downTr, rowNum, pairNum, triangleDrawer)
					pairNum = pairNum + 1
				})
			}
			case None => {
				upTriangles.foreach(upTr => {
					printPair(upTr, None, rowNum, pairNum, triangleDrawer)
					pairNum = pairNum + 1
				})
			}
		}
	}

	def printPair(upTr: Triangle, downTr: Option[Triangle], rowNum: Int, pairNum: Int, triangleDrawer: TriangleDrawer): Unit = {
		val offsetY = height - (rowNum * (triangleHeight + 2)) - 10
		val offsetX1 = (pairNum * triangleWallSize) + 10
		triangleDrawer.drawTriangleUp(upTr.asInstanceOf[TrianglePointingUp], offsetX1, offsetY)
		if (downTr.isDefined) {
			val offsetX2 = offsetX1 + triangleWallSize
			triangleDrawer.drawTriangleDown(downTr.get.asInstanceOf[TrianglePointingDown], offsetX2, offsetY)
		}
	}
}
