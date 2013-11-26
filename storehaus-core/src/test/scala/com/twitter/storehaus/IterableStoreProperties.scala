/*
 * Copyright 2013 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.storehaus

import com.twitter.util.{ Await, Future }

import org.scalacheck.{ Arbitrary, Properties }
import org.scalacheck.Gen.choose
import org.scalacheck.Prop._

object IterableStoreProperties extends Properties("IterableStore") {

  def iterableStoreLaw[K: Arbitrary, V: Arbitrary](fn: Map[K, V] => IterableStore[K, V]) =
    forAll { m: Map[K,V] =>
      val store = fn(m)
      Await.result(store.items.flatMap { _.toSeq }).toMap == m
    }

  property("MapStore obeys the IterableStore laws") = {
    val filter : (((Int, String)) => Boolean) = { case ((k, v)) => k % 2 == 0 }
    iterableStoreLaw[Int, String](IterableStore.fromMap(_))
  }
}

