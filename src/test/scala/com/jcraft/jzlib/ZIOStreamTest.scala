/* -*-mode:scala; c-basic-offset:2; indent-tabs-mode:nil -*- */
package com.jcraft.jzlib

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream => OIS, ObjectOutputStream => OOS}

import com.jcraft.jzlib.JZlib._
import org.scalatest._

class ZIOStreamTest extends FlatSpec with BeforeAndAfter with Matchers {

  before {
  }

  after {
  }

  behavior of "DeflaterOutputStream and InflaterInputStream"

  it can "deflate and inflate data." in {
    val hello = "Hello World!"

    val byteArrayOutputStream = new ByteArrayOutputStream()
    val deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream)
    val objectOutputStream = new OOS(deflaterOutputStream)
    objectOutputStream.writeObject(hello)
    deflaterOutputStream.close()

    val byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray)
    val inflaterInputStream = new InflaterInputStream(byteArrayInputStream)
    val objectInputStream = new OIS(inflaterInputStream)

    objectInputStream.readObject.toString should equal (hello)
  }

  behavior of "DeflaterOutputStream and InflaterInputStream"

  it can "support nowrap data." in {

    implicit val buf: Array[Byte] = new Array[Byte](100)

    val hello = "Hello World!".getBytes

    val deflater = new Deflater(Z_DEFAULT_COMPRESSION, true)

    val byteArrayOutputStream = new ByteArrayOutputStream
    val deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater)
    hello -> deflaterOutputStream
    deflaterOutputStream.close()

    val baos2 = new ByteArrayOutputStream
    val inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray), true)
    inflaterInputStream -> baos2
    val data2 = baos2.toByteArray

    data2.length should equal (hello.length)
    data2 should equal (hello)
  }
}
