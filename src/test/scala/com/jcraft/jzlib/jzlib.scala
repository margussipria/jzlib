package com.jcraft

import java.io.{ByteArrayInputStream, InputStream, OutputStream}

package object jzlib {
  implicit class readIS(is: InputStream) {
    def -> (out: OutputStream)(implicit buf: Array[Byte] = new Array[Byte](1024)): Unit = {
      Stream
        .continually(is.read(buf))
        .takeWhile(-1 != _)
        .foreach(i => out.write(buf, 0, i))
      is.close()
    }
  }

  // reading a resource file
  implicit class fromResource(str: String ) {
    def fromResource: Array[Byte] = {
      scala.io.Source
        .fromURL(getClass.getResource(str))(io.Codec.ISO8859)
        .map(_.toByte)
        .toArray
    }
  }

  implicit class readArray(is: Array[Byte]) {
    def ->(out: OutputStream)(implicit buf: Array[Byte]): Unit = {
      new ByteArrayInputStream(is) -> out
    }
  }

  def randombuf(n: Int) = (0 to n).map { _ =>
    util.Random.nextLong.asInstanceOf[Byte]
  }.toArray
}
