import java.io.File

import generator.{PrintDrawer, PyramidGenerator, PyramidDrawer}

/**
 * Created by maciej on 26.08.15.
 */
object Bootstrap {
	val numberOfRows = 7
	val maxNumber = 100

	def main(args: Array[String]) = {
		if (args.length == 1) {
			val pyramidsNum = args(0).toInt
			(1 to pyramidsNum).foreach(num => {
				val folderName = num.toString
				generate(folderName)
				println(s"generated $folderName")
			})
		} else {
			generate("1")
			println(s"generated 1")
		}
	}

	def generate(folderName: String): Unit = {
		new File(folderName).mkdir()

		val pyramidGenerator = new PyramidGenerator(numberOfRows, maxNumber)
		val pyramid = pyramidGenerator.generate()
		val drawer = new PrintDrawer(2, 1754, 1240, folderName)
		drawer.draw(pyramid)
		val cheat = new PyramidDrawer(1754 * 2, 1240 * 2, folderName)
		cheat.draw(pyramid)
	}
}
