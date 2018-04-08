/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2000-2011 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 * This program is based on zlib-1.1.3, so all credit should go authors
 * Jean-loup Gailly(jloup@gzip.org) and Mark Adler(madler@alumni.caltech.edu)
 * and contributors of zlib.
 */

package com.jcraft.jzlib;

/**
 * ZStream
 */
public abstract class ZStream {

  static final private int MAX_WBITS = 15;        // 32K LZ77 window
  static final private int DEF_WBITS = MAX_WBITS;

  public byte[] next_in;     // next input byte
  public int next_in_index;
  public int avail_in;       // number of bytes available at next_in
  public long total_in;      // total nb of input bytes read so far

  public byte[] next_out;    // next output byte should be put there
  public int next_out_index;
  public int avail_out;      // remaining free space at next_out
  public long total_out;     // total nb of bytes output so far

  public String msg;

  Checksum adler;

  protected ZStream() {
    this(new Adler32());
  }

  protected ZStream(Checksum adler) {
    this.adler = adler;
  }

  public long getAdler() {
    return adler.getValue();
  }

  public void free() {
    next_in = null;
    next_out = null;
    msg = null;
  }

  public void setOutput(byte[] buf) {
    setOutput(buf, 0, buf.length);
  }

  public void setOutput(byte[] buf, int off, int len) {
    next_out = buf;
    next_out_index = off;
    avail_out = len;
  }

  public void setInput(byte[] buf) {
    setInput(buf, 0, buf.length, false);
  }

  public void setInput(byte[] buf, boolean append) {
    setInput(buf, 0, buf.length, append);
  }

  public void setInput(byte[] buf, int off, int len, boolean append) {
    if (len <= 0 && append && next_in != null) return;

    if (avail_in > 0 && append) {
      byte[] tmp = new byte[avail_in + len];
      System.arraycopy(next_in, next_in_index, tmp, 0, avail_in);
      System.arraycopy(buf, off, tmp, avail_in, len);
      next_in = tmp;
      next_in_index = 0;
      avail_in += len;
    } else {
      next_in = buf;
      next_in_index = off;
      avail_in = len;
    }
  }

  public byte[] getNextIn() {
    return next_in;
  }

  public void setNextIn(byte[] next_in) {
    this.next_in = next_in;
  }

  public int getNextInIndex() {
    return next_in_index;
  }

  public void setNextInIndex(int next_in_index) {
    this.next_in_index = next_in_index;
  }

  public int getAvailIn() {
    return avail_in;
  }

  public void setAvailIn(int avail_in) {
    this.avail_in = avail_in;
  }

  public byte[] getNextOut() {
    return next_out;
  }

  public void setNextOut(byte[] next_out) {
    this.next_out = next_out;
  }

  public int getNextOutIndex() {
    return next_out_index;
  }

  public void setNextOutIndex(int next_out_index) {
    this.next_out_index = next_out_index;
  }

  public int getAvailOut() {
    return avail_out;

  }

  public void setAvailOut(int avail_out) {
    this.avail_out = avail_out;
  }

  public long getTotalOut() {
    return total_out;
  }

  public long getTotalIn() {
    return total_in;
  }

  public String getMessage() {
    return msg;
  }

  public abstract int end();

  public abstract boolean finished();
}
