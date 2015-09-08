package pyramid

/**
 * Created by maciej on 07.09.15.
 */
class Pyramid(val rows: Seq[Row]) {
	override def toString: String = {
		var str = ""
		rows.foreach(row => str = str + s"$row \n")

		str
	}
}

class Row(val rowNum: Int, val triangles: Seq[Triangle]) {
	override def toString: String = {
		var str = ""
		triangles.foreach(triangle => {
			str = str + s"$triangle "
		})

		str
	}
}




